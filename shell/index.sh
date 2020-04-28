#!/usr/bin/env bash
# 全文检索索引
# 3.4.x导入cql文件
nohup /u02/isi/zdr/graph/neo4j-community-3.4.9/bin/neo4j-shell -file build.cql >>indexGraph.log 2>&1 &

# 3.5.x导入cql文件
# type build.cql | bin\cypher-shell.bat -a localhost -u neo4j -p 123456

type C:\Users\11416\Desktop\project\dx_workspace\neo4j-engine-inter\cql\dump.cql | bin\cypher-shell.bat -a localhost -u neo4j -p 123456

