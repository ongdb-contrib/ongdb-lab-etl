MATCH p=()-[]-() RETURN p LIMIT 100;

// 1、创建索引
CREATE INDEX ON :PersonTest(id);

// 2、第一次导入：
USING PERIODIC COMMIT 1000
LOAD CSV FROM 'file:/twitter-2010.txt.gz' AS line FIELDTERMINATOR ' '
WITH toInt(line[0]) as id
MERGE (n:PersonTest {id:id})
  ON CREATE
  SET
  n.name = toString(id),
  n.sex = ['男','女'][(id % 2)],
  n.age = (id % 50) + 15,
n.country = ['中国','美国','法国', '英国', '俄罗斯', '加拿大', '德国', '日本', '意大利'][(id % 9)];

// 3、第二次导入：
USING PERIODIC COMMIT 1000
LOAD CSV FROM 'file:/twitter-2010.txt.gz' AS line FIELDTERMINATOR ' '
		WITH toInt(line[1]) as id
		MERGE (n:PersonTest {id:id})
		ON CREATE
		SET
			n.name = toString(id),
			n.sex = ['男', '女'][(id % 2)],
n.age = (id % 50) + 15,
n.country = ['中国', '美国', '法国', '英国', '俄罗斯', '加拿大', '德国', '日本', '意大利'][(id % 9)];
