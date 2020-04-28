# ONgDB-LAB-ETL
- 协议：BOLT ROUTING

## 核心接口
### 批量构图工具接口
data.lab.ongdb.compose
- 1、支持批量动态更新导入
- 2、支持CSV文件导入（HTTP SERVICE方式加载文件）
- 3、支持批量merge
- 4、运行cql脚本

### 索引接口
data.lab.ongdb.index
- 节点/关系/路径索引接口

### 更新
data.lab.ongdb.update
- 节点/关系/路径更新接口 - 无CYPHER限制，可支持大批量删除操作

