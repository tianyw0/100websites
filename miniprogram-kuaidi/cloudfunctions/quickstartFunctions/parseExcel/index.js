const cloud = require('wx-server-sdk');
const randomstring = require("randomstring");
const xlsx = require('node-xlsx');
const moment = require('moment');
const util = require('./util');

cloud.init({
  env: cloud.DYNAMIC_CURRENT_ENV
});

exports.main = async (event, context) => {
  console.log("event", event)
  console.log("文件ID参数", event.data)
  // 1 通过fileID下载云存储里的excel文件
  const res = await cloud.downloadFile({
    fileID: event.data,
  })
  const buf1 = res.fileContent
  // 2 解析excel文件里的数据
  var sheets = xlsx.parse(buf1); //获取到所有sheets

  // 3 计算-组织数据
  /**
   * - 校验文件，不能缺少必要计算条件（省市信息，区间，价格数字的合法性）
   * - 生成计价函数
   * - 逐行计算费用
   * - 插入到指定行
   */
  let dataSheet = sheets[0]
  let priceSheet = sheets[1]
  if (!dataSheet.name.includes('原始数据')) {
    return {code: -1, errMsg: "第一个sheet应该是'原始数据'"}
  }
  if (!priceSheet.name.includes('价格表')) {
    return {code: -1, errMsg: "第二个sheet应该是'价格表'"}
  }

  // console.log(priceSheet.data)

  let provinceFunctionMap = new Map();
  let gotPatternFunction = false;
  let patternFunction;
  priceSheet.data.forEach((data, index) => {
    // console.log(data, index)
    if (!data[0]) return;
    if (!util.provinces.includes(data[0]) && !data[0].startsWith("目的地")) return;
    if (!gotPatternFunction && data[0].startsWith("目的地")) {
      data.shift();
      for (const item of Object.entries(util.patternTitle)) {
        if (util.equalArr(data, item[1])) {
          patternFunction = util.patternFunction[item[0].split("_")[0]]
          gotPatternFunction = true
          return
        }
      }
    }
    // 生成省份价格函数
    if (gotPatternFunction) {
      let provinceName = data[0]
      data.shift();
      util.genProvinceNames(provinceName).forEach(name => {
        provinceFunctionMap.set(name, patternFunction(data))
      })
    }
  })

  if (!gotPatternFunction || provinceFunctionMap.size == 0) {
    console.log("没有得到公式！")
    return
  }

  console.log("provinceFunctionMap", provinceFunctionMap)

  let gotWeightIndex = false;
  let weightIndex = 0;
  let gotProvinceIndex = false;
  let provinceIndex = 0;
  let gotCovidIndex = false;
  let covidIndex = 0;
  dataSheet.data.forEach((data, index) => {
    // console.log(data, index)
    if (!data[0]) return;
    if (!gotProvinceIndex && data[0].startsWith("运单号")) {
      // 追加一列
      data.push("计算运费")
      // 找出两个index备用
      data.forEach((item, index) => {
        if (item.includes("结算重量")) {
          weightIndex = index
          gotWeightIndex = true
        }
        if (item.includes("目的地省")) {
          provinceIndex = index
          gotProvinceIndex = true
        }
        if (item.includes("疫情加收")) {
          covidIndex = index
          gotCovidIndex = true
        }
      })
      if (gotProvinceIndex && gotWeightIndex) return;
    }
  })

  if (!gotProvinceIndex || !gotWeightIndex) {
    console.log("没有获取到省份或者结算重量对应的索引")
    return
  }

  let resultSheet = []
  resultSheet.push(["订单号", "目的地省", "重量", "结果"])
  dataSheet.data.forEach((data, index) => {
    // console.log(data, index)
    const provinceName = data[provinceIndex]
    if (!util.provinces.includes(provinceName)) return;
    const pFunc = provinceFunctionMap.get(provinceName)
    const weight = data[weightIndex]
    let result = pFunc(weight)
    if (gotCovidIndex) {
      if (data[covidIndex]) {
        result = (result * 10000 + data[covidIndex] * 10000) / 10000
      }
    }
    resultSheet.push([data[0], provinceName, weight, result])
  })
  // console.log("resultSheet", resultSheet)

  // 4 生成新的excel
  const excelName = '计算结果-' + moment().format("yyyyMMdd-hhmmss-") + randomstring.generate(5) + '.xlsx'
  const buf2 = await xlsx.build([{
    name: "resultSheet",
    data: resultSheet
  }]);
  // 5 生成云存储 fileId
  return await cloud.uploadFile({
    cloudPath: excelName,
    fileContent: buf2, //excel二进制文件
  })
}
