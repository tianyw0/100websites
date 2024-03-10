function validationParams(body) {
  // todo validation
  if (body.lastName == null) {
    throw new Error("lastName is required");
  }
  return;
}

function GennameRule(rule) {
  this.character = rule.character;
  this.polyphonic = rule.polyphonic;
  this.strokeCountMin = rule.strokeCountMin;
  this.strokeCountMax = rule.strokeCountMax;
  this.radical = rule.radical;
  this.radicalNot = rule.radicalNot;
  this.initial = rule.initial;
  this.initialNot = rule.initialNot;
  this.final = rule.final;
  this.finalNot = rule.finalNot;
  this.usageLevel = rule.usageLevel;
  this.tone = rule.tone;
  this.toneNot = rule.toneNot;
  this.homophonic = rule.homophonic;
  this.homophonicNot = rule.homophonicNot;
  this.forbiddenCharsCustom = rule.forbiddenCharsCustom;
  this.forbiddenCharsCommon = rule.forbiddenCharsCommon;

  this.genWhereStatements = function () {
    let whereStatements = new Array();
    if (this.polyphonic != null) {
      whereStatements.push(`polyphonic = ${this.polyphonic}`);
    }
    if (this.strokeCountMin != null) {
      whereStatements.push(`stroke_count >= ${this.strokeCountMin}`);
    }
    if (this.strokeCountMax != null) {
      whereStatements.push(`stroke_count <= ${this.strokeCountMax}`);
    }
    if (this.radical.length > 0 && this.radical.some(Boolean)) {
      whereStatements.push(
        `radical in (${this.radical
          .filter((c) => c)
          .filter((c) => c)
          .filter((c) => c)
          .map((c) => `'${c}'`)
          .join(",")})`
      );
    }
    if (this.radicalNot.length > 0 && this.radicalNot.some(Boolean)) {
      whereStatements.push(
        `radical not in (${this.radicalNot
          .filter((c) => c)
          .map((c) => `'${c}'`)
          .join(",")})`
      );
    }
    if (this.initial.length > 0 && this.initial.some(Boolean)) {
      whereStatements.push(
        `initial in (${this.initial
          .filter((c) => c)
          .map((c) => `'${c}'`)
          .join(",")})`
      );
    }
    if (this.initialNot.length > 0 && this.initialNot.some(Boolean)) {
      whereStatements.push(
        `initial not in (${this.initialNot
          .filter((c) => c)
          .map((c) => `'${c}'`)
          .join(",")})`
      );
    }
    if (this.final.length > 0 && this.final.some(Boolean)) {
      whereStatements.push(
        `final in (${this.final
          .filter((c) => c)
          .map((c) => `'${c}'`)
          .join(",")})`
      );
    }
    if (this.finalNot.length > 0 && this.forbiddenCharsCommon.some(Boolean)) {
      whereStatements.push(
        `final not in (${this.finalNot
          .filter((c) => c)
          .map((c) => `'${c}'`)
          .join(",")})`
      );
    }
    if (this.usageLevel.length > 0 && this.usageLevel.some(Boolean)) {
      whereStatements.push(
        `usage_level in (${this.usageLevel
          .filter((c) => c)
          .map((c) => `'${c}'`)
          .join(",")})`
      );
    }
    if (this.tone.length > 0 && this.tone.some(Boolean)) {
      whereStatements.push(
        `tone in (${this.tone
          .filter((c) => c)
          .map((c) => `'${c}'`)
          .join(",")})`
      );
    }
    if (this.toneNot.length > 0 && this.toneNot.some(Boolean)) {
      whereStatements.push(
        `tone not in (${this.toneNot
          .filter((c) => c)
          .map((c) => `'${c}'`)
          .join(",")})`
      );
    }
    if (this.homophonic.length > 0 && this.homophonic.some(Boolean)) {
      whereStatements.push(
        `pinyin_without_tone in (${this.homophonic
          .filter((c) => c)
          .map((c) => `'${c}'`)
          .join(",")})`
      );
    }
    if (this.homophonicNot.length > 0 && this.homophonicNot.some(Boolean)) {
      whereStatements.push(
        `pinyin_without_tone not in (${this.homophonicNot
          .filter((c) => c)
          .map((c) => `'${c}'`)
          .join(",")})`
      );
    }
    if (
      this.forbiddenCharsCustom.length > 0 &&
      this.forbiddenCharsCustom.some(Boolean)
    ) {
      whereStatements.push(
        `character not in (${this.forbiddenCharsCustom
          .filter((c) => c)
          .map((c) => `'${c}'`)
          .join(",")})`
      );
    }
    if (
      this.forbiddenCharsCommon.length > 0 &&
      this.forbiddenCharsCommon.some(Boolean)
    ) {
      whereStatements.push(
        `character not in (${this.forbiddenCharsCommon
          .filter((c) => c)
          .map((c) => `'${c}'`)
          .join(",")})`
      );
    }
    return whereStatements;
  };
}
function ParamData(data) {
  this.pageSize = data.pageSize;
  this.pageNum = data.pageNum;
  this.lastName = data.lastName;
  this.joinRules = data.joinRules;
  this.rules = new Array();
  data.rules.forEach((rule) => {
    this.rules.push(new GennameRule(rule));
  });
  //
  this.paramsContainResult = function () {
    if (this.rules.length == 1 && this.rules[0].character != null) {
      return true;
    }
    if (
      this.rules.length == 2 &&
      this.rules[0].character != null &&
      this.rules[1].character != null
    ) {
      return true;
    }
    return false;
  };
  //
  this.paramsResult = function () {
    if (this.rules.length == 1 && this.rules[0].character != null) {
      return this.lastName + this.rules[0].character;
    }
    if (
      this.rules.length == 2 &&
      this.rules[0].character != null &&
      this.rules[1].character != null
    ) {
      return this.lastName + this.rules[0].character + this.rules[1].character;
    }
    return null;
  };
  //
  this.genConditionSql = function () {
    let conditionSqls = new Array();
    this.rules.forEach((rule, index) => {
      conditionSqls.push(
        ...rule.genWhereStatements().map((item) => `(t${index}.${item})`)
      );
    });
    console.log("conditionSqls: ", conditionSqls);
    return conditionSqls.join(" and ");
  };
  //
  this.genCountSql = function () {
    if (this.paramsContainResult()) {
      return null;
    }
    if (this.rules.length == 1) {
      return `SELECT count(1) as ttotal FROM cchar t0 WHERE ${this.genConditionSql()};`;
    } else {
      return `SELECT count(1) as ttotal FROM ${this.rules.map((v, i) => `cchar t${i}`).join(', ')} WHERE ${this.genConditionSql()};`;
    }
  };

  this.genPageSql = function () {
    if (this.paramsContainResult()) {
      return null;
    }
    if (this.rules.length == 1) {
      return `
      SELECT concat('${this.lastName}', t0.character) as name, t0.pinyin_with_tone as pinyin, t0.stroke_count 
      FROM cchar t0 
      where 
      ${this.genConditionSql()} 
      limit ${this.pageSize} offset ${this.pageSize * (this.pageNum - 1)};`;
    } else {
      return `SELECT 
      concat('${this.lastName}', ${this.rules
        .map((item, index) => `t${index}.character`)
        .join(", ")}) as name, 
      concat(${this.rules
        .map((item, index) => `t${index}.pinyin_with_tone`)
        .join('," ",')}) as pinyin,
      ${this.rules.map((item, index) => `t${index}.stroke_count`).join(", ")}, 
      ${this.rules
        .map((item, index) => `t${index}.stroke_count`)
        .join(" + ")} as total_stroke 
      FROM ${this.rules.map((item, index) => `cchar t${index}`).join(", ")}
      WHERE 
      ${this.genConditionSql()} 
      limit ${this.pageSize} offset ${this.pageSize * (this.pageNum - 1)};`;
    }
  };
}

export async function onRequestPost(context) {
  try {
    const data = await context.request.json();
    validationParams(data);

    const param = new ParamData(data);
    console.log(param);
    if (param.paramsContainResult()) {
      return Response.json({ result: param.paramsResult() });
    }
    const genCountSql = param.genCountSql();
    console.log("gencountsql: ", genCountSql);
    let ps = context.env.db_cchar.prepare(genCountSql);
    const totalRes = await ps.all();
    console.log("totalRes.results: ", totalRes.results);

    const genPageSql = param.genPageSql();
    console.log("genPageSql: ", genPageSql);
    ps = context.env.db_cchar.prepare(genPageSql);
    const res = await ps.all();
    return Response.json(res.results);
  } catch (err) {
    return new Response(`${err.message}\n${err.stack}`, { status: 500 });
  }
}
