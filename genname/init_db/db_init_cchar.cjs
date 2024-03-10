const { pinyin, convert } = require("pinyin-pro");
const { Client } = require("pg");
const {
  chars140,
  chars232,
  chars380,
  chars1000,
  chars2000,
  standard1,
  standard2,
  standard3,
} = require("./data/chardata.json"); // 导入外部数据
const cnchar = require("cnchar");
const name = require("cnchar-name");
const radical = require("cnchar-radical");
cnchar.use(name); // use 在浏览器环境中非必须
cnchar.use(radical); // use 在浏览器环境中非必须
const fs = require("fs");

// 数据库连接信息
const dbConfig = {
  user: "postgres",
  password: "new_password",
  host: "localhost",
  database: "mydatabase",
  port: 5432, // 默认 PostgreSQL 端口
};

// 创建一个 PostgreSQL 客户端
const client = new Client(dbConfig);

// 将 SQL 语句存储到文件
async function saveSqlToFile(sqlStatements, path) {
  try {
    // 将 SQL 语句数组连接成一个字符串
    const sqlContent = sqlStatements.join("\n");

    // 将字符串写入文件
    fs.writeFileSync(path, sqlContent);
    console.log("SQL 语句成功写入文件 cchar.sql。");
  } catch (error) {
    console.error("写入文件出错:", error);
  }
}

// 从文件中读取并执行 SQL 语句
async function executeSqlFromFile(path) {
  try {
    // 从文件中读取 SQL 语句
    const sqlContent = fs.readFileSync(path, "utf8");
    // 连接到数据库
    await client.connect();

    await client.query(sqlContent);
    console.log("SQL 语句批量执行成功");
  } catch (error) {
    console.error("执行 SQL 语句出错:", error);
  } finally {
    // 关闭数据库连接
    await client.end();
  }
}

async function createTable(chars) {
  let sqlStatements = [];
  sqlStatements.push("DROP TABLE IF EXISTS cchar;");

  // 创建表
  sqlStatements.push(`
      CREATE TABLE cchar (
        id SERIAL PRIMARY KEY,
        character VARCHAR(10) NOT NULL,
        pinyin_with_tone VARCHAR(255) NOT NULL,
        pinyin_without_tone VARCHAR(255) NOT NULL,
        tone INT,
        stroke_count INT NOT NULL,
        polyphonic BOOLEAN NOT NULL,
        meanings TEXT,
        popularity INT,
        negative_words BOOLEAN NOT NULL,
        negative_explanation TEXT,
        initial VARCHAR(10),
        final VARCHAR(10),
        final_head VARCHAR(10),
        final_body VARCHAR(10),
        final_tail VARCHAR(10),
        initial_articulation_type INT,
        final_articulation_type INT,
        usage_level VARCHAR(255),
        standard_level VARCHAR(255),
        radical VARCHAR(255)
      );
    `);

  // 添加注释
  const comments = [
    { column: "character", comment: "汉字" },
    { column: "pinyin_with_tone", comment: "拼音（带音调）" },
    { column: "pinyin_without_tone", comment: "拼音（不带音调）" },
    { column: "tone", comment: "音调" },
    { column: "stroke_count", comment: "笔画数" },
    { column: "polyphonic", comment: "是否多音字" },
    { column: "meanings", comment: "汉字的意义或用法说明" },
    { column: "popularity", comment: "使用频率或流行程度" },
    { column: "negative_words", comment: "是否为负面词汇" },
    { column: "negative_explanation", comment: "负面词汇的具体说明" },
    { column: "initial", comment: "声母" },
    { column: "final", comment: "韵母" },
    { column: "final_head", comment: "韵母的声母部分" },
    { column: "final_body", comment: "韵母的韵母主体部分" },
    { column: "final_tail", comment: "韵母的韵母尾部分" },
    { column: "initial_articulation_type", comment: "声母的发音部位类型" },
    { column: "final_articulation_type", comment: "韵母的发音部位类型" },
    {
      column: "usage_level",
      comment: "汉字的使用级别如mcu1000-1到mcu1000-5",
    },
    {
      column: "standard_level",
      comment: "汉字的使用级别如standard1到standard3",
    },
    { column: "radical", comment: "部首" },
  ];
  for (const { column, comment } of comments) {
    // sqlStatements.push(`COMMENT ON COLUMN cchar.${column} IS '${comment}';`);
  }

  console.log('数据库表 "cchar" 创建成功。');

  // 遍历外部数据并插入数据
  console.log("开始插入数据...");
  const charArray = chars.split("");
  let index = 0;
  for (const character of charArray) {
    // 考虑多音字，获取所有音调的拼音数组
    const pinyinArray = pinyin(character, { type: "all", multiple: true }); // 获取所有音调的拼音数组
    isPolyphonic = pinyinArray.length > 1; // 是否多音字

    // 改成for in循环，以便获取index
    for (let i = 0; i < pinyinArray.length; i++) {
      const pinyinInfo = pinyinArray[i];
      // 构造插入 SQL 语句
      const insertQuery = `
            INSERT INTO cchar (
                id, character, pinyin_with_tone, pinyin_without_tone, tone, stroke_count,
                polyphonic, meanings, popularity, negative_words, negative_explanation,
                initial, final, final_head, final_body, final_tail, initial_articulation_type, final_articulation_type, standard_level, usage_level, radical
            )
            VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18, $19, $20, $21);
          `;

      // 执行插入操作
      try {
        const fsql = formatSql(insertQuery, [
          // {"origin":"好","pinyin":"hǎo","initial":"h","final":"ǎo","first":"h","finalHead":"","finalBody":"ǎ","finalTail":"o","num":3,"isZh":true}
          index++, // id
          pinyinInfo.origin, // 汉字
          pinyinInfo.pinyin, // 拼音（带音调）
          pinyinInfo.initial +
            convert(pinyinInfo.final, { format: "toneNone" }), // 拼音（不带音调）
          pinyinInfo.num, // 音调
          cnchar.stroke(character) == 0 ? 100 : cnchar.stroke(character), // 笔画数,
          isPolyphonic, // 是否多音字
          // todo: meanings
          "11",
          // todo: popularity
          1,
          // todo: negative_words
          false,
          // todo: negative_explanation
          "11",
          pinyinInfo.initial, // 声母
          convert(pinyinInfo.final, { format: "toneNone" }), // 韵母
          pinyinInfo.finalHead, // 韵母的头部
          pinyinInfo.finalBody, // 韵母的主体部分
          pinyinInfo.finalTail, // 韵母的尾部
          get_initial_articulation_type(pinyinInfo.initial),
          get_final_articulation_type(
            convert(pinyinInfo.final, { format: "toneNone" })
          ),
          get_standard_level(character),
          get_useage_level(character),
          cnchar.radical(character)[0].radical, // 部首
        ]);
        sqlStatements.push(fsql);
        console.log(`插入汉字 '${character}' 的第 ${i + 1} 音成功`);
      } catch (error) {
        console.error(
          `插入汉字 '${character}' 的第 ${i + 1} 音失败: ${error.message}`
        );
      }
    }
  }

  const path = "./init_db/db_init_cchar.sql"
  saveSqlToFile(sqlStatements, path);
  executeSqlFromFile(path)
}

// 将参数化 SQL 转换成字符串形式
function formatSql(sql, params) {
  let formattedSql = sql;
  // 替换占位符 $1, $2, ... 等为参数值
  params.forEach((param, index) => {
    formattedSql = formattedSql.replace(`$${index + 1}`, formatParam(param));
  });
  return formattedSql;
}

// 格式化参数值
function formatParam(param) {
  // 在实际应用中，根据参数类型进行格式化，这里简化处理
  if (param === undefined) {
    return null
  } else if (typeof param === "string") {
    return `'${param}'`;
  } else {
    return param;
  }
}

//
function get_initial_articulation_type(initial) {
  /**
   * 汉语的声母共有22个（21个辅音，1个是零声母y），按发音部位可分为四组：
一组：bpmf（唇）
二组：dtnl（舌尖）
三组：gkh（舌根）
四组：jqxzhchshrzcs（舌面、舌尖后、舌尖前）
   */
  if (["b", "p", "m", "f"].includes(initial)) {
    return 1;
  }
  if (["d", "t", "n", "l"].includes(initial)) {
    return 2;
  }
  if (["g", "k", "h"].includes(initial)) {
    return 3;
  }
  if (["j", "q", "x", "zh", "ch", "sh", "r", "z", "c", "s"].includes(initial)) {
    return 4;
  }
}

function get_final_articulation_type(final) {
  /**
   * 汉语的韵母有38个，可分为5种类型：
一类开口呼：aaiaoanang
（注：开口呼为非i、u、ü或i、u、ü开头的韵母。）
二类开口呼：eereieneng
三类开口呼：oouong
四类齐齿呼：iiaieiaoiouianiniangingiong
（注：齐齿呼是i或以i开头的韵母。）
撮口呼：uueuanun（注：撮口呼是ü或以ü开头的韵母。）
五类：合口呼uuauouaiueiuanuenuangueng
（注：合口呼是u或以u开头的韵母。）
   */
  if (["a", "ai", "ao", "an", "ang"].includes(final)) {
    return 1;
  }
  if (["e", "ei", "er", "en", "eng"].includes(final)) {
    return 1;
  }
  if (["o", "ou", "ong"].includes(final)) {
    return 1;
  }
  if (
    ["i", "ia", "ie", "iao", "iou", "ian", "in", "iang", "iong"].includes(final)
  ) {
    return 2;
  }
  if (["u", "ue", "uan", "un"].includes(final)) {
    return 3;
  }
  // 合口呼
  if (
    ["u", "ua", "uo", "ua", "iu", "ei", "uan", "uen", "uang", "ueng"].includes(
      final
    )
  ) {
    return 4;
  }
}

// 生成多个避讳字的拼音
function get_avoidance_pinyin(character) {
  const pinyinArray = pinyin(character, { type: "all", multiple: true }); // 获取所有音调的拼音数组
  const pinyinArrayWithoutTone = pinyinArray.map((pinyinInfo) => {
    return (
      pinyinInfo.initial + convert(pinyinInfo.final, { format: "toneNone" })
    );
  });
  return pinyinArrayWithoutTone;
}

// 生成postgresql查询语句
function generate_query_sql(avoidanceChars, secondAvoidChars) {
  const pinyinArrayWithoutTone = get_avoidance_pinyin(avoidanceChars);
  const conditionSql = `
  WHERE 
  a.polyphonic = false AND b.polyphonic = false
  AND a.usage_level IN ('standard1', 'standard2')
  AND ((a.tone = 1 AND b.tone = 2) or (a.tone = 4 AND b.tone = 2))
  AND a.character NOT IN (${secondAvoidChars
    .split("")
    .map((p) => `'${p}'`)
    .join(", ")})
    AND a.pinyin_without_tone NOT IN (${pinyinArrayWithoutTone
      .map((p) => `'${p}'`)
      .join(", ")})
      AND a.id != b.id;
      `;
  const querySql = `
  SELECT concat(a.character, b.character) as name
  FROM Characters a
  JOIN Characters b ON a.id != b.id
  LEFT JOIN chinese_phrases p ON concat(a.pinyin_without_tone, ' ', b.pinyin_without_tone) = p.pinyin_without_tone
    `;
  return querySql + conditionSql;
}

function get_standard_level(character) {
  // chars140, chars232, chars380, chars1000, chars2000, standard1, standard2, standard3
  if (standard1.content.indexOf(character) !== -1) {
    return standard1.name;
  }
  if (standard2.content.indexOf(character) !== -1) {
    return standard2.name;
  }
  if (standard3.content.indexOf(character) !== -1) {
    return standard3.name;
  }
  return null;
}

function get_useage_level(character) {
  // chars140, chars232, chars380, chars1000, chars2000
  if (chars140.content.indexOf(character) !== -1) {
    return chars140.name;
  }
  if (chars232.content.indexOf(character) !== -1) {
    return chars232.name;
  }
  if (chars380.content.indexOf(character) !== -1) {
    return chars380.name;
  }
  if (chars1000.content.indexOf(character) !== -1) {
    return chars1000.name;
  }
  if (chars2000.content.indexOf(character) !== -1) {
    return chars2000.name;
  }
  return null;
}

// 创建表并插入数据
createTable(standard1.content + standard2.content + standard3.content);
// console.log(
//   get_avoidance_pinyin(
//     "田兆志鲁学会田永威海霞潘自礼胡书凤秋霞全威田广义刘世兰王军田建伟田彬国恒轩浩宇珊亚楠八"
//   )
//     .map((p) => "'" + p + "'")
//     .join(", ")
// );
