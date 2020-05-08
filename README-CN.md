# ONgDB-LAB-ETL
- 协议：BOLT ROUTING
- bolt://localhost:7687，主要用于单机版的Neo4j访问，或者集群中特定的机器
- bolt+routing://graph.example.com:7687，路由访问集群中的节点
- bolt+routing://graph.example.com:7687/?region=europe&country=sw
- jdbc:neo4j:bolt://host:port/?username=neo4j,password=xxxx
- jdbc:neo4j:bolt+routing://host1:port1,host2:port2,…,hostN:portN/?username=neo4j,password=xxxx
- 自定义路由规则某些请求发送到特定节点执行
- https://neo4j.com/docs/driver-manual/current/client-applications/#driver-connection-uris
- https://zhuanlan.zhihu.com/p/76254396

-- 多数据中心配置策略
- https://neo4j.com/docs/operations-manual/3.5/clustering-advanced/multi-data-center/load-balancing/
- https://neo4j.com/docs/operations-manual/3.5/clustering-advanced/multi-data-center/#multi-dc-licensing
- https://neo4j.com/docs/driver-manual/current/client-applications/#driver-connection-uris

There is no procedure with the name `CALL dbms.cluster.routing.getRoutingTable` registered for this database instance.
CALL dbms.routing.getRoutingTable({}, "neo4j")
╒═════╤══════════════════════════════════════════════════════════════════════╕
│"ttl"│"servers"                                                             │
╞═════╪══════════════════════════════════════════════════════════════════════╡
│300  │[{"addresses":["localhost:7687"],"role":"WRITE"},{"addresses":["localh│
│     │ost:7687"],"role":"READ"},{"addresses":["localhost:7687"],"role":"ROUT│
│     │E"}]                                                                  │
└─────┴──────────────────────────────────────────────────────────────────────┘
CALL dbms.cluster.routing.getRoutingTable({},"neo4j")
╒═════╤══════════════════════════════════════════════════════════════════════╕
│"ttl"│"servers"                                                             │
╞═════╪══════════════════════════════════════════════════════════════════════╡
│300  │[{"addresses":["localhost:7687"],"role":"WRITE"},{"addresses":["localh│
│     │ost:7687"],"role":"READ"},{"addresses":["localhost:7687"],"role":"ROUT│
│     │E"}]                                                                  │
└─────┴──────────────────────────────────────────────────────────────────────┘

-- 自动提交事务，事务函数和显式事务(推荐使用事务函数)
-- 写入操作必须判断是否在leader节点进行！！！

## 核心接口
### SERVER MANAGE
>分类节点角色CORE和READ_REPLICA(其中FOLLOWER和READ_REPLICA节点都只能READ)
>DEV表示测试版
>PRO表示生产环境
data.lab.ongdb.etl.properties.ServerConfiguration

### 批量构图工具接口
data.lab.ongdb.etl.compose
- 1、支持批量动态更新导入
- 2、支持CSV文件导入（HTTP SERVICE方式加载文件）
- 3、支持批量merge
- 4、运行cql脚本

### 索引接口
data.lab.ongdb.etl.index
- 节点/关系/路径索引接口

### 更新
data.lab.ongdb.etl.update
- 节点/关系/路径更新接口 - 无CYPHER限制，可支持大批量删除操作

### 支持功能
- 节点发现
- 读写分离
- 负载均衡
- 自动路由

