SELECT concat('田', a.character, b.character) as name, concat(a.pinyin_with_tone, ' ',  b.pinyin_with_tone) as pinyin,a.stroke_count as a_stroke_count, b.stroke_count as b_stroke_count, a.stroke_count + b.stroke_count as total_stroke
FROM cchar a
JOIN cchar b ON a.id != b.id and a.final != b.final and a.initial != b.initial
-- LEFT JOIN chinese_phrases p ON concat(a.pinyin_with_tone, ' ', b.pinyin_with_tone) = p.pinyin
WHERE
-- p.id IS NULL
-- 多音字(不能两个都是多音字)
NOT (a.polyphonic = true AND b.polyphonic = true)
-- 笔画
-- AND a.stroke_count <= 10 AND b.stroke_count <= 10
-- AND (a.stroke_count - b.stroke_count) >= 3
-- AND (a.stroke_count + b.stroke_count) <= 20
AND a.stroke_count != 100
-- 偏旁部首
AND a.radical NOT IN ('扌', '犭', '女', '口', '钅', '疒', '走')
AND b.radical NOT IN ('扌', '犭', '女', '口', '钅', '疒', '走')
-- AND a.radical NOT IN ('氵')
-- AND b.radical NOT IN ('氵')
-- AND (a.radical IN ('火') OR b.radical IN ('火'))
-- AND a.radical IN ('艹')
-- 声母
-- AND a.initial != '' AND b.initial != ''
AND NOT (a.initial = 't' AND b.initial = 't')
-- AND a.initial IN ('h', 'q', 'x', 'z', 'c', 's', 'y')
-- AND a.initial NOT IN ('r', 'zh', 'ch', 'sh')
-- AND b.initial NOT IN ('r', 'zh', 'ch', 'sh')
-- 韵母不能再有前鼻音和后鼻音，因为田
-- AND a.final NOT IN ('an','ian','uan','üan','en','in','uen','ün','ang','iang','uang','eng','ing','ueng','ong','iong')
-- AND b.final NOT IN ('an','ian','uan','üan','en','in','uen','ün','ang','iang','uang','eng','ing','ueng','ong','iong')
-- 韵母不能雷同
-- ng
-- u ü ün un ue i
-- ün un in
-- an en
-- ia ua
-- ei ui
-- ua o
-- ie e ue
-- ua ui
AND NOT (a.final_tail = 'ng' AND b.final_tail = 'ng')
AND NOT ((a.final = 'u' OR a.final = 'ü' OR a.final = 'ün' OR a.final = 'un' OR a.final = 'ue' OR a.final = 'i') AND (b.final = 'u' OR b.final = 'ü' OR b.final = 'ün' OR b.final = 'un' OR b.final = 'ue' OR b.final = 'i'))
AND NOT ((a.final = 'ün' OR a.final = 'un' OR a.final = 'in') AND (b.final = 'ün' OR b.final = 'un' OR b.final = 'in'))
AND NOT ((a.final = 'an' OR a.final = 'en') AND (b.final = 'an' OR b.final = 'en'))
AND NOT ((a.final = 'ia' OR a.final = 'ua') AND (b.final = 'ia' OR b.final = 'ua'))
AND NOT ((a.final = 'ei' OR a.final = 'ui') AND (b.final = 'ei' OR b.final = 'ui'))
AND NOT ((a.final = 'ua' OR a.final = 'o') AND (b.final = 'ua' OR b.final = 'o'))
AND NOT ((a.final = 'ie' OR a.final = 'e' OR a.final = 'ue') AND (b.final = 'ie' OR b.final = 'e' OR b.final = 'ue'))
AND NOT ((a.final = 'ua' OR a.final = 'ui') AND (b.final = 'ua' OR b.final = 'ui'))
-- 汉语级别
-- AND a.usage_level IN ('chars140', 'chars232', 'chars380', 'chars1000')
-- AND b.usage_level IN ('chars140', 'chars232', 'chars380', 'chars1000')
AND a.standard_level IN ('standard1', 'standard2') AND b.standard_level IN ('standard1', 'standard2')
-- AND a.id < 1000 and b.id < 1000
-- AND (a.standard_level IN ('standard2') OR b.standard_level IN ('standard2'))
-- 数词只能在中间
AND NOT b.character IN ('一', '二', '三', '四', '五', '六', '七', '八', '九', '十')
-- 音调
-- AND ((a.tone = 1 AND b.tone = 2) OR (a.tone = 1 AND b.tone = 4) OR (a.tone = 4 AND b.tone = 1) OR (a.tone = 4 AND b.tone = 2))
-- AND (a.tone = 2 AND b.tone = 1)
-- AND NOT (a.tone = 3 AND b.tone = 3)
-- AND ((a.tone = 3 OR a.tone = 4) AND (b.tone = 1 OR b.tone = 2))
AND a.character NOT IN ('孽', '夫', '父', '爷', '爹', '草', '肛', '屎', '尿', '屁', '臭', '贱', '淫', '傻', '杀', '肮', '脏', '坟', '墓', '死', '亡', '丐', '坑', '亏', '奴', '阴', '毛', '污', '玷', '丑', '灾', '难', '讨', '犯', '讹', '诈', '哎', '鬼', '哀', '疤', '痕', '厕', '歹')
AND b.character NOT IN ('孽', '夫', '父', '爷', '爹', '草', '肛', '屎', '尿', '屁', '臭', '贱', '淫', '傻', '杀', '肮', '脏', '坟', '墓', '死', '亡', '丐', '坑', '亏', '奴', '阴', '毛', '污', '玷', '丑', '灾', '难', '讨', '犯', '讹', '诈', '哎', '鬼', '哀', '疤', '痕', '厕', '歹')
AND a.character NOT IN ('一', '叉', '巳', '个', '气', '内', '头', '那', '没', '色', '吗', '么', '次', '坏', '蛋', '优', '尤', '秃', '尴', '尬', '他', '沦', '忧', '牢', '局', '鸡', '驴', '肉', '讥', '沟', '困', '二', '苞', '卑', '鄙', '跌', '抄', '闭', '贝', '口', '孓', '曰', '刀', '刁', '灭', '灶', '孕', '炖', '炒', '炊', '烧', '焚', '烤', '灼', '火', '闩', '甩', '瓜', '母', '奶', '皿', '卯', '夭', '交', '凹', '凸', '炬', '勺', '八', '叭', '巴', '芭', '爸', '败')
AND b.character NOT IN ('一', '叉', '巳', '个', '气', '内', '头', '那', '没', '色', '吗', '么', '次', '坏', '蛋', '优', '尤', '秃', '尴', '尬', '他', '沦', '忧', '牢', '局', '鸡', '驴', '肉', '讥', '沟', '困', '二', '苞', '卑', '鄙', '跌', '抄', '唇', '闭', '贝', '口', '孓', '曰', '刀', '刁', '灭', '灶', '孕', '炖', '炒', '炊', '烧', '焚', '烤', '灼', '火', '闩', '甩', '瓜', '母', '奶', '皿', '卯', '夭', '交', '凹', '凸', '炬', '勺', '八', '叭', '巴', '芭', '爸', '败', '大', '不')
AND a.pinyin_without_tone NOT IN ('tai', 'shuai', 'qü', 'diao', 'ru', 'nai', 'bi', 'tian', 'zhao', 'zhi', 'lu', 'xüe', 'hui', 'tian', 'yong', 'wei', 'hai', 'xia', 'pan', 'zi', 'li', 'hu', 'shu', 'feng', 'qiu', 'xia', 'qüan', 'wei', 'tian', 'guang', 'liu', 'shi', 'lan', 'wang', 'jün', 'tian', 'jian', 'wei', 'tian', 'bin', 'guo', 'heng', 'xüan', 'hao', 'yu', 'shan', 'ya', 'nan', 'shuo')
AND b.pinyin_without_tone NOT IN ('yi', 'tai', 'shuai', 'qü', 'diao', 'ru', 'nai', 'bi', 'tian', 'zhao', 'zhi', 'lu', 'xüe', 'hui', 'tian', 'yong', 'wei', 'hai', 'xia', 'pan', 'zi', 'li', 'hu', 'shu', 'feng', 'qiu', 'xia', 'qüan', 'wei', 'tian', 'guang', 'liu', 'shi', 'lan', 'wang', 'jün', 'tian', 'jian', 'wei', 'tian', 'bin', 'guo', 'heng', 'xüan', 'hao', 'yu', 'shan', 'ya', 'nan', 'shuo')
AND a.character = '璟';
-- AND a.character = '亿'
-- AND a.pinyin_without_tone = 'zi'
-- AND b.pinyin_without_tone = 'nian'
;