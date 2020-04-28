package data.lab.ongdb.common;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import data.lab.ongdb.compose.NeoComposer;
import data.lab.ongdb.compose.pack.Cypher;
import data.lab.ongdb.compose.pack.NoUpdateRela;
import data.lab.ongdb.driver.Neo4jDriver;
import data.lab.ongdb.model.Condition;
import data.lab.ongdb.model.Result;
import data.lab.ongdb.update.NeoUpdater;
import data.lab.ongdb.util.ClientUtils;
import data.lab.ongdb.util.JSONTool;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.common
 * @Description: TODO(图谱构建器 / 增 / 删 / 改 / 查工具的父类)
 * @date 2019/7/10 19:18
 */
public abstract class NeoAccessor implements Accessor {

    private Logger logger = Logger.getLogger(this.getClass());

    // java_driver访问对象
    public Driver driver;

    // DEBUG标记开关
    public boolean DEBUG = false;

    // 默认图数据用什么格式返回 - 默认用GRAPH格式返回
    public ResultDataContents contents = ResultDataContents.D3_GRAPH;

    // 默认使用哪种方式与NEO4J进行交互
    public AccessOccurs accessOccurs = AccessOccurs.JAVA_DRIVER;

    public NeoAccessor() {
    }

    /**
     * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount:节点的用户名
     * @param authPassword:节点用户名密码
     * @return
     * @Description: TODO(构造函数 - 默认使用JAVA - DRIVER发送请求 ， D3_GRAPH格式返回数据)
     */
    public NeoAccessor(String ipPorts, String authAccount, String authPassword) {
        // 使用JAVA-DRIVER访问
        this.driver = GraphDatabase.driver("bolt://" + ClientUtils.getBoltUrl(ipPorts), AuthTokens.basic(authAccount, authPassword));
    }

    /**
     * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount:节点的用户名
     * @param authPassword:节点用户名密码
     * @return
     * @Description: TODO(构造函数 - 默认使用JAVA - DRIVER发送请求 ， D3_GRAPH格式返回数据)
     */
    public NeoAccessor(String ipPorts, String authAccount, String authPassword, Config config) {
        // 使用JAVA-DRIVER访问
        this.driver = GraphDatabase.driver("bolt://" + ClientUtils.getBoltUrl(ipPorts), AuthTokens.basic(authAccount, authPassword), config);
    }

    /**
     * @param contents:指定图数据使用什么格式返回，默认GRAPH格式返回
     * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount:节点的用户名
     * @param authPassword:节点用户名密码
     * @return
     * @Description: TODO(构造函数 - 默认使用HTTP - API发送请求)
     * 支持多种格式返回数据ROW/GRAPH/ROW_GRAPH/D3_GRAPH
     */
    @Deprecated
    public NeoAccessor(ResultDataContents contents, String ipPorts, String authAccount, String authPassword) {
        HttpProxyRegister.register(ipPorts, authAccount, authPassword);
        this.request = new HttpProxyRequest(HttpPoolSym.DEFAULT.getSymbolValue(), authAccount, authPassword);
        this.contents = contents;
    }

    /**
     * @param accessOccurs:选择用哪种方式与NEO4J进行交互
     * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount:节点的用户名
     * @param authPassword:节点用户名密码
     * @return
     * @Description: TODO(构造函数)
     * RESTFUL_API（使用HTTP访问（支持多种格式返回数据ROW/GRAPH/ROW_GRAPH/D3_GRAPH）
     * JAVA_DRIVER（使用JAVA-DRIVER访问（支持一种格式返回数据D3_GRAPH）
     */
    @Deprecated
    public NeoAccessor(AccessOccurs accessOccurs, String ipPorts, String authAccount, String authPassword) {
        if (AccessOccurs.RESTFUL_API.equals(accessOccurs)) {
            HttpProxyRegister.register(ipPorts, authAccount, authPassword);
            this.request = new HttpProxyRequest(HttpPoolSym.DEFAULT.getSymbolValue(), authAccount, authPassword);
        } else if (AccessOccurs.JAVA_DRIVER.equals(accessOccurs)) {
            // 使用JAVA-DRIVER访问
            this.driver = GraphDatabase.driver("bolt://" + ClientUtils.getBoltUrl(ipPorts), AuthTokens.basic(authAccount, authPassword));
        }
        this.accessOccurs = accessOccurs;
    }

    /**
     * @param contents:指定图数据使用什么格式返回，默认GRAPH格式返回
     * @param accessOccurs:选择用哪种方式与NEO4J进行交互
     * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount:节点的用户名
     * @param authPassword:节点用户名密码
     * @return
     * @Description: TODO(构造函数)
     * RESTFUL_API（使用HTTP访问（支持多种格式返回数据ROW/GRAPH/ROW_GRAPH/D3_GRAPH）
     * JAVA_DRIVER（使用JAVA-DRIVER访问（支持一种格式返回数据D3_GRAPH）
     */
    @Deprecated
    public NeoAccessor(ResultDataContents contents, AccessOccurs accessOccurs, String ipPorts, String authAccount, String authPassword) {
        if (AccessOccurs.RESTFUL_API.equals(accessOccurs)) {
            HttpProxyRegister.register(ipPorts, authAccount, authPassword);
            this.request = new HttpProxyRequest(HttpPoolSym.DEFAULT.getSymbolValue(), authAccount, authPassword);
        } else if (AccessOccurs.JAVA_DRIVER.equals(accessOccurs)) {
            // 使用JAVA-DRIVER访问
            this.driver = GraphDatabase.driver("bolt://" + ClientUtils.getBoltUrl(ipPorts), AuthTokens.basic(authAccount, authPassword));
        }
        this.contents = contents;
        this.accessOccurs = accessOccurs;
    }

    public boolean isDEBUG() {
        return DEBUG;
    }

    public void setDEBUG(boolean DEBUG) {
        this.DEBUG = DEBUG;
    }

    /**
     * @return
     * @Description: TODO(NEO4J访问对象的全局变量重置方法)
     */
    @Override
    public abstract void reset();

    /**
     * @return
     * @Description: TODO(执行请求 - 拼接请求之后执行 - 默认返回节点或者关系的所有属性字段)
     */
    @Override
    public abstract JSONObject execute();

    /**
     * @param cypher@return
     * @Description: TODO(跳过条件添加直接使用CYPHER查询 - 默认返回节点或者关系的所有属性字段)
     */
    @Override
    public JSONObject execute(String cypher, CRUD crudType) {
        // 禁止某些危险查询
        if (cypher.contains("DELETE") || cypher.contains("REMOVE") ||
                cypher.contains("delete") || cypher.contains("remove")) {
            this.logger.info("The cypher statement forbidden execution!", new IllegalAccessException());
            return null;
        }
        long startMill = System.currentTimeMillis();
        JSONArray queryResultList = new JSONArray();
        Condition condition = new Condition();
        condition.setStatement(cypher, this.contents);
        queryResultList.add(Result.message(chooseSendCypherWay(condition, crudType)));

        JSONObject result = Result.statistics(queryResultList);
        // 统计所有请求的耗时，以及每个请求的平均耗时
        long stopMill = System.currentTimeMillis();
        result.put("consume", Result.statisticsConsume(startMill, stopMill, queryResultList.size(), this.logger));

        if (ResultDataContents.D3_GRAPH.equals(this.contents) && !CRUD.RETRIEVE_PROPERTIES.equals(crudType)) {
            result = JSONTool.packD3Json(result);
        }
        if (this.DEBUG) {
            this.logger.info("Debug condition:" + condition.toString());
        }
        return result;
    }

    /**
     * invoke cypherAction in batched transactions being feeded from cypherIteration running in main thread
     *
     * @param cypherIterate
     * @param cypherAction
     * @param config
     * @return
     * @Description: TODO(使用迭代器执行CYPHER -
     *apoc.periodic.iterate ( ' statement returning items ',
     *' statement per item ',
     * {batchSize:1000,iterateList:true,parallel:false,params:{},concurrency:50,retries:0})
     * YIELD batches, total - run the second statement for each item returned by the first statement. Returns number of batches and total processed rows)
     */
    @Override
    public JSONObject executeIterate(String cypherIterate, String cypherAction, Object... config) {
        // 禁止某些危险查询  - 只有更新对象才可以无限制操作图谱
        if (!(this instanceof NeoUpdater || this instanceof NeoComposer)) {
            logger.info("Just neo4j updater or composer can operate!");
            throw new IllegalArgumentException();
        }
        String paras = JSONTool.removeOnlyJSONObjectKeyDoubleQuotation(JSONTool.tansferGenericPara(config));
        String cypher = new StringBuilder()
                .append("CALL apoc.periodic.iterate(")
                .append("\"" + cypherIterate + "\", ")
                .append("\"" + cypherAction + "\", ")
                .append("" + paras + "")
                .append(") ")
                .append("YIELD  batches,total,timeTaken,committedOperations,failedOperations,failedBatches,retries,errorMessages,batch,operations ")
                .append("RETURN batches,total,timeTaken,committedOperations,failedOperations,failedBatches,retries,errorMessages,batch,operations ")
                .toString();
        Condition condition = new Condition();
        condition.setStatement(cypher, this.contents);
        return Result.message(chooseSendCypherWay(condition, CRUD.RETRIEVE_PROPERTIES));
    }

    /**
     * @param
     * @return
     * @Description: TODO(使用REST - API或者JAVA - DRIVER执行请求)
     */
    public JSONObject chooseSendCypherWay(Condition condition, CRUD crudType) {
        if (this.DEBUG) {
            this.logger.info("Debug condition:" + condition.toString());
        }
        if (AccessOccurs.RESTFUL_API.equals(this.accessOccurs)) {
            return JSONObject.parseObject(this.request.httpPost(NeoUrl.DB_DATA_TRANSACTION_COMMIT.getSymbolValue(), condition.toString()));
        } else if (AccessOccurs.JAVA_DRIVER.equals(this.accessOccurs)) {
            if (CRUD.RETRIEVE.equals(crudType)) {
                return Neo4jDriver.searcher(this.driver, condition.getStatement(condition.toString()));
            } else if (CRUD.MERGE_CSV.equals(crudType)) {
                return Neo4jDriver.composerAutoCommit(this.driver, condition.getStatement(condition.toString()));
            } else if (CRUD.RETRIEVE_PROPERTIES.equals(crudType)) {
                return Neo4jDriver.rowProperties(this.driver, condition.getStatement(condition.toString()));
            } else if (CRUD.MERGE_RETURN_NODE_ID.equals(crudType)) {
                return Neo4jDriver.composerReturnNodeId(this.driver, condition.getStatement(condition.toString()));
            } else {
                return Neo4jDriver.composer(this.driver, condition.getStatement(condition.toString()));
            }
        }
        return new JSONObject();
    }

    /**
     * @return
     * @Description: TODO(查看已有索引)
     */
    @Override
    public JSONObject dbIndexes() {
        String cypher = "CALL db.indexes() YIELD description,indexName,tokenNames,properties,state,type,progress,provider,id,failureMessage " +
                "RETURN description,indexName,tokenNames,properties,state,type,progress,provider,id,failureMessage;";
        return Neo4jDriver.rowProperties(this.driver, cypher);
    }

    /**
     * @return
     * @Description: TODO(查看已有标签)
     */
    @Override
    public JSONObject dbLabels() {
        String cypher = "CALL db.labels();";
        return Neo4jDriver.rowProperties(this.driver, cypher);
    }

    /**
     * @return
     * @Description: TODO(查看已有关系类型)
     */
    @Override
    public JSONObject dbRelationshipTypes() {
        String cypher = "CALL db.relationshipTypes();";
        return Neo4jDriver.rowProperties(this.driver, cypher);
    }

    /**
     * @param
     * @return
     * @Description: TODO(全局访问对象的关闭方法)
     */
    public void close() {
        if (this.driver != null) {
            this.driver.close();
        }
        if (this.request != null) {
            this.request = null;
        }
    }

    /**
     * @param
     * @return
     * @Description: TODO(全局访问对象的关闭方法)
     */
    public void closeAsync() {
        if (this.driver != null) {
            this.driver.closeAsync();
        }
        if (this.request != null) {
            this.request = null;
        }
    }

    /**
     * @param
     * @return
     * @Description: TODO(计算两两节点之间是否相似 - > SIMHASH海明距离)
     * <p>
     * 节点的计算属性：
     * sim_hash: {"titleSimHash":"1010111110101100100101010100110010000110000111011001000000110111",
     * "contentSimHash":"1010111110110101000001000100110010000110100111001110110010001000"}
     */
    public JSONObject calNewsSimilarBySimHash(JSONObject result) {
        JSONArray nodes = JSONTool.getNodeOrRelaList(result, "nodes");
        JSONObject newsSimilar = new JSONObject();
        JSONArray similar = new JSONArray();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {

                JSONObject newsFingerPrintOne = nodes.getJSONObject(i);
                JSONObject newsFingerPrintTwo = nodes.getJSONObject(j);

                JSONObject fingerPrintOne = JSONObject.parseObject(newsFingerPrintOne.getJSONObject("properties").getString("sim_hash"));
                JSONObject fingerPrintTwo = JSONObject.parseObject(newsFingerPrintTwo.getJSONObject("properties").getString("sim_hash"));

                boolean bool = SimHash.isSimilar(new NewsFingerPrint(fingerPrintOne.getString("titleSimHash"), fingerPrintOne.getString("contentSimHash")),
                        new NewsFingerPrint(fingerPrintTwo.getString("titleSimHash"), fingerPrintTwo.getString("contentSimHash")));

                JSONObject newsSim = new JSONObject();
                newsSim.put("source", newsFingerPrintOne);
                newsSim.put("similar", bool);
                newsSim.put("target", newsFingerPrintTwo);
                similar.add(newsSim);
            }

        }
        newsSimilar.put("similar", similar);
        return newsSimilar;
    }

    /**
     * @param
     * @return
     * @Description: TODO(将一个大数据包分割成多个小数据包)
     */
    public List<List<Object[]>> cutListByBatchSize(final List<Object[]> bigList, final int batchSize) {
        if (bigList == null || bigList.isEmpty()) {
            this.logger.info("Data package is empty missing execute!");
            return new ArrayList<>();
        }
        final int MAX_SEND = batchSize;

        int size = bigList.size();
        final double node_temp = (double) (size) / (double) MAX_SEND;
        final int node_limit = (int) Math.ceil(node_temp);
        if (this.logger.isInfoEnabled()) {
            this.logger.info("DATA SIZE:" + size + ",EXECUTE BATCH SIZE:" + node_limit);
        }
        List<List<Object[]>> splitList = new ArrayList<>();
        Stream.iterate(0, n -> n + 1)
                .limit(node_limit)
                .forEach(a -> {
                    List<Object[]> sendList = bigList.parallelStream().skip(a * MAX_SEND).limit(MAX_SEND).collect(Collectors.toList());
                    splitList.add(sendList);
                });
        return splitList;
    }

    /**
     * @param
     * @return
     * @Description: TODO(将一个大数据包分割成多个小数据包)
     */
    public List<List<NoUpdateRela>> cutObjListByBatchSize(final List<NoUpdateRela> bigList, final int batchSize) {
        if (bigList == null || bigList.isEmpty()) {
            this.logger.info("Data package is empty missing execute!");
            return new ArrayList<>();
        }
        final int MAX_SEND = batchSize;

        int size = bigList.size();
        final double node_temp = (double) (size) / (double) MAX_SEND;
        final int node_limit = (int) Math.ceil(node_temp);
        if (this.logger.isInfoEnabled()) {
            this.logger.info("DATA SIZE:" + size + ",EXECUTE BATCH SIZE:" + node_limit);
        }
        List<List<NoUpdateRela>> splitList = new ArrayList<>();
        Stream.iterate(0, n -> n + 1)
                .limit(node_limit)
                .forEach(a -> {
                    List<NoUpdateRela> sendList = bigList.parallelStream().skip(a * MAX_SEND).limit(MAX_SEND).collect(Collectors.toList());
                    splitList.add(sendList);
                });
        return splitList;
    }

    /**
     * @param
     * @return
     * @Description: TODO(将一个大数据包分割成多个小数据包)
     */
    public List<List<Cypher>> cutCyphersByBatchSize(final List<Cypher> bigList, final int batchSize) {
        if (bigList == null || bigList.isEmpty()) {
            this.logger.info("Data package is empty missing execute!");
            return new ArrayList<>();
        }
        final int MAX_SEND = batchSize;

        int size = bigList.size();
        final double node_temp = (double) (size) / (double) MAX_SEND;
        final int node_limit = (int) Math.ceil(node_temp);
        if (this.logger.isInfoEnabled()) {
            this.logger.info("DATA SIZE:" + size + ",EXECUTE BATCH SIZE:" + node_limit);
        }
        List<List<Cypher>> splitList = new ArrayList<>();
        Stream.iterate(0, n -> n + 1)
                .limit(node_limit)
                .forEach(a -> {
                    List<Cypher> sendList = bigList.parallelStream().skip(a * MAX_SEND).limit(MAX_SEND).collect(Collectors.toList());
                    splitList.add(sendList);
                });
        return splitList;
    }
}


