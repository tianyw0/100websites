const parseExcel = require('./parseExcel/index');


// 云函数入口函数
exports.main = async (event, context) => {
  switch (event.type) {
    case 'parseExcel':
      return await parseExcel.main(event, context);
  }
};
