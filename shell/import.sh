#测试
bin/cypher-shell -a localhost:7787 -u neo4j -p datalab%dev "USING PERIODIC COMMIT 1000
LOAD CSV FROM 'file:/twitter-2010.txt.gz' AS line FIELDTERMINATOR ' '
WITH toInt(line[0]) as id
MERGE (n:PersonTest {id:id})
  ON CREATE
  SET
  n.name = toString(id),
  n.sex = ['男','女'][(id % 2)],
  n.age = (id % 50) + 15,
n.country = ['中国','美国','法国', '英国', '俄罗斯', '加拿大', '德国', '日本', '意大利'][(id % 9)];"

#生产
bin/cypher-shell -a localhost:7687 -u neo4j -p datalab%pro "USING PERIODIC COMMIT 1000
LOAD CSV FROM 'file:/twitter-2010.txt.gz' AS line FIELDTERMINATOR ' '
WITH toInt(line[0]) as id
MERGE (n:PersonTest {id:id})
  ON CREATE
  SET
  n.name = toString(id),
  n.sex = ['男','女'][(id % 2)],
  n.age = (id % 50) + 15,
n.country = ['中国','美国','法国', '英国', '俄罗斯', '加拿大', '德国', '日本', '意大利'][(id % 9)];"
