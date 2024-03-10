const { Pool } = require("pg");
const { convert } = require("pinyin-pro");
const fs = require("fs").promises;

// PostgreSQL 数据库配置
const pool = new Pool({
  user: "postgres",
  password: "new_password",
  host: "localhost",
  database: "mydatabase",
  port: 5432, // 默认 PostgreSQL 端口
});

// SQL语句
const dropTableQuery = "DROP TABLE IF EXISTS chinese_phrases";
const createTableQuery = `
    CREATE TABLE chinese_phrases (
        id SERIAL PRIMARY KEY,
        word VARCHAR(1000) NOT NULL,
        pinyin VARCHAR(255) NOT NULL,
        abbr VARCHAR(255),
        explanation TEXT,
        pinyin_without_tone VARCHAR(255)
    );
`;
const insertQuery =
  "INSERT INTO chinese_phrases (word, pinyin, abbr, explanation, pinyin_without_tone) VALUES ($1, $2, $3, $4, $5)";
const createIndexQuery =
  "CREATE INDEX idx_pinyin_without_tone ON chinese_phrases (pinyin_without_tone)";

async function main() {
  const client = await pool.connect();

  try {
    // 删除旧表
    await client.query(dropTableQuery);
    console.log("旧表已删除");
    // 创建新表
    await client.query(createTableQuery);
    console.log("新表已创建");

    // 导入数据
    const phrasesData = JSON.parse(
      await fs.readFile("./data/chinese_phrases.json", "utf8")
    );
    for (const item of phrasesData) {
      if (item.word && item.word.length === 2) {
        console.log(item.word);
        await client.query(insertQuery, [
          item.word,
          item.pinyin,
          item.abbr,
          item.explanation,
          convert(item.pinyin, { format: "toneNone" }),
        ]);
      }
    }
    console.log("数据导入成功");

    // 创建索引
    await client.query(createIndexQuery);
    console.log("索引创建成功");
  } catch (err) {
    console.error(err);
  } finally {
    client.release();
  }
}

main();
