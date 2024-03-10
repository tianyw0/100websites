const patternFunction = {
  "模式1": function (...pricesArgs) {
    let prices = pricesArgs[0]
    console.log("模式1的参数", prices)
    if (prices.length < 5) {
      console.error("模式1对应的参数个数不够5个")
      return null;
    }
    const [fw_price_1kg, fw_price_2kg, fw_price_3kg, cw_price_3kg, fw_price_5kg] =
      [prices[0], prices[1], prices[2], prices[3], prices[4]]
    return function (weight) {
      // 向上取整
      weight = Math.ceil(weight)
      if (weight > 0 && weight <= 1) {
        return fw_price_1kg;
      } else if (weight > 1 && weight <= 2) {
        return fw_price_2kg;
      } else if (weight > 2 && weight <= 3) {
        return fw_price_3kg;
      } else if (weight > 3 && weight <= 5) {
        return fw_price_3kg + (weight - 3) * cw_price_3kg;
      } else if (weight > 5) {
        return fw_price_5kg + (weight - 3) * cw_price_3kg;
      }
    }
  },
  "模式2": function (...pricesArgs) {
    let prices = pricesArgs[0]
    console.log("模式2的参数", prices)
    if (prices.length < 4) {
      console.error("模式2对应的参数个数不够4个")
      return null;
    }
    const [fw_price_1kg, fw_price_2kg, fw_price_3kg, cw_price_3kg] =
      [prices[0], prices[1], prices[2], prices[3]]
    return function (weight) {
      // 向上取整
      weight = Math.ceil(weight)
      if (weight > 0 && weight <= 1) {
        return fw_price_1kg;
      } else if (weight > 1 && weight <= 2) {
        return fw_price_2kg;
      } else if (weight > 2 && weight <= 3) {
        return fw_price_3kg;
      } else if (weight > 3) {
        return fw_price_3kg + (weight - 3) * cw_price_3kg;
      }
    }
  }
}

const patternTitle = {
  "模式1_1": ["0.1-1kg", "1.01-2kg(元/票)", "2.01-3kg(元/票)", "3kg以上续重(元/kg)", "5kg以上(含3kg)"],
  "模式2": ["0.1-1kg", "1.01-2kg(元/票)", "2.01-3kg(元/票)", "3kg以上续重(元/kg)"],
}

const provinces = [
  "河南省",
  "河南",
  "湖北省",
  "湖北",
  "河北省",
  "河北",
  "山东省",
  "山东",
  "陕西省",
  "陕西",
  "浙江省",
  "浙江",
  "安徽省",
  "安徽",
  "天津市",
  "天津",
  "山西省",
  "山西",
  "湖南省",
  "湖南",
  "江苏省",
  "江苏",
  "广东省",
  "广东",
  "福建省",
  "福建",
  "江西省",
  "江西",
  "贵州省",
  "贵州",
  "重庆市",
  "重庆",
  "云南省",
  "云南",
  "辽宁省",
  "辽宁",
  "吉林省",
  "吉林",
  "四川省",
  "四川",
  "黑龙江省",
  "黑龙江",
  "广西壮族自治区",
  "广西",
  "甘肃省",
  "甘肃",
  "海南省",
  "海南",
  "宁夏回族自治区",
  "宁夏",
  "内蒙古自治区",
  "内蒙古",
  "青海省",
  "青海",
  "北京市",
  "北京",
  "上海市",
  "上海",
  "新疆维吾尔自治区",
  "新疆",
  "西藏自治区",
  "西藏"
];

function genProvinceNames(provinceName) {
  if (provinceName.endsWith("河南省")) {
    return ["河南省", "河南"]
  } else if (provinceName.endsWith("湖北省")) {
    return ["湖北省", "湖北"]
  } else if (provinceName.endsWith("河北省")) {
    return ["河北省", "河北"]
  } else if (provinceName.endsWith("山东省")) {
    return ["山东省", "山东"]
  } else if (provinceName.endsWith("陕西省")) {
    return ["陕西省", "陕西"]
  } else if (provinceName.endsWith("浙江省")) {
    return ["浙江省", "浙江"]
  } else if (provinceName.endsWith("安徽省")) {
    return ["安徽省", "安徽"]
  } else if (provinceName.endsWith("天津市")) {
    return ["天津市", "天津"]
  } else if (provinceName.endsWith("天津")) {
    return ["天津市", "天津"]
  } else if (provinceName.endsWith("山西省")) {
    return ["山西省", "山西"]
  } else if (provinceName.endsWith("湖南省")) {
    return ["湖南省", "湖南"]
  } else if (provinceName.endsWith("江苏省")) {
    return ["江苏省", "江苏"]
  } else if (provinceName.endsWith("广东省")) {
    return ["广东省", "广东"]
  } else if (provinceName.endsWith("福建省")) {
    return ["福建省", "福建"]
  } else if (provinceName.endsWith("江西省")) {
    return ["江西省", "江西"]
  } else if (provinceName.endsWith("贵州省")) {
    return ["贵州省", "贵州"]
  } else if (provinceName.endsWith("重庆市")) {
    return ["重庆市", "重庆"]
  } else if (provinceName.endsWith("重庆")) {
    return ["重庆市", "重庆"]
  } else if (provinceName.endsWith("云南省")) {
    return ["云南省", "云南"]
  } else if (provinceName.endsWith("辽宁省")) {
    return ["辽宁省", "辽宁"]
  } else if (provinceName.endsWith("吉林省")) {
    return ["吉林省", "吉林"]
  } else if (provinceName.endsWith("四川省")) {
    return ["四川省", "四川"]
  } else if (provinceName.endsWith("黑龙江省")) {
    return ["黑龙江省", "黑龙江"]
  } else if (provinceName.endsWith("广西壮族自治区")) {
    return ["广西壮族自治区", "广西"]
  } else if (provinceName.endsWith("甘肃省")) {
    return ["甘肃省", "甘肃"]
  } else if (provinceName.endsWith("海南省")) {
    return ["海南省", "海南"]
  } else if (provinceName.endsWith("宁夏回族自治区")) {
    return ["宁夏回族自治区", "宁夏回族"]
  } else if (provinceName.endsWith("内蒙古自治区")) {
    return ["内蒙古自治区", "内蒙古"]
  } else if (provinceName.endsWith("青海省")) {
    return ["青海省", "青海"]
  } else if (provinceName.endsWith("北京")) {
    return ["北京市", "北京"]
  } else if (provinceName.endsWith("北京市")) {
    return ["北京市", "北京"]
  } else if (provinceName.endsWith("上海")) {
    return ["上海市", "上海"]
  } else if (provinceName.endsWith("上海市")) {
    return ["上海市", "上海"]
  } else if (provinceName.endsWith("新疆维吾尔自治区")) {
    return ["新疆维吾尔自治区", "新疆"]
  } else if (provinceName.endsWith("西藏自治区")) {
    return ["西藏自治区", "西藏"]
  }
}

function equalArr(a, b) {
  // 判断数组的长度
  if (a.length !== b.length) {
    return false
  } else {
    // 循环遍历数组的值进行比较
    for (let i = 0; i < a.length; i++) {
      if (a[i].toLowerCase() !== b[i].toLowerCase()) {
        return false
      }
    }
    return true;
  }
}

// 打印
let str = Object.keys(patternFunction).map(function (key) {
  let value = patternFunction[key].toString().replace(/\n/g, "")
  return "\"" + key + "\": \"" + value + "\""
}).join(",\n\t");
console.log(`{\n\t${str}\n}`);

exports.genProvinceNames = genProvinceNames
exports.patternFunction = patternFunction
exports.patternTitle = patternTitle
exports.equalArr = equalArr
exports.provinces = provinces