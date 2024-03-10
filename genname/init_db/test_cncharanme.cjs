const cnchar = require('cnchar');
const name = require('cnchar-name');
const radical = require('cnchar-radical');
cnchar.use(name); // use 在浏览器环境中非必须
cnchar.use(radical); // use 在浏览器环境中非必须

const names = cnchar.name("田", {
  number: 1,
  gender: "boy",
  length: 3,
});
// console.log(names);
const aa = '锕'
console.log(cnchar.stroke(aa));
// console.log(cnchar.radical("刚")[0].radical);

