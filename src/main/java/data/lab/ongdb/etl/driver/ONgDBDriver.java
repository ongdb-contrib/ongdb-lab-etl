package data.lab.ongdb.etl.driver;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import data.lab.ongdb.etl.common.CRUD;
import data.lab.ongdb.etl.common.NeoAccessor;
import data.lab.ongdb.etl.common.TimeUnit;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import data.lab.ongdb.http.register.OngdbHeartBeat;
import org.apache.log4j.Logger;
import org.neo4j.driver.*;
import org.neo4j.driver.internal.value.*;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;
import org.neo4j.driver.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.driver
 * @Description: TODO(NEO4J_JAVA_DRIVER驱动 - 与ONgDB交互的类)
 * @date 2019/7/13 19:50
 */
public class ONgDBDriver {

    /**
     * JAVA_DRIVER返回的数据格式统一按照D3_GRAPH格式返回
     **/

    private final static Logger logger = org.apache.log4j.Logger.getLogger(ONgDBDriver.class);

    /**
     * @param driver:传入NEO4J_JAVA驱动
     * @param statement:cypher
     * @return
     * @Description: TODO(构图导入等操作访问)
     */
    public synchronized static JSONObject composer(Driver driver, String statement) {
        long startMill = System.currentTimeMillis();
        if (OngdbHeartBeat.isRegister()) {
            driver = NeoAccessor.ongdbHeartBeat.getWriterBlotMappingLocalDriver();
            if (NeoAccessor.driverIsResetDriver){
                driver = NeoAccessor.driver;
            }
        }
        boolean flag = false;
        // RETRY FAILURE QUERY
        while (!flag && Objects.nonNull(driver)) {
            try (Session session = driver.session()) {
                session.run(statement);
                flag = true;
            } catch (Exception e) {
                logger.info("Retrying query " + statement + "\r\n");
                e.printStackTrace();
            }
        }
        long stopMill = System.currentTimeMillis();
        long consume = (stopMill - startMill) / Integer.parseInt(String.valueOf(TimeUnit.MILL_SECOND_CV.getSymbolValue()));
        if (consume != 0) {
            logger.info("ONgDB driver composer success!consume:" + consume + "s");
        } else {
            logger.info("ONgDB driver composer success!consume:" + (stopMill - startMill) + "mills");
        }
        return new JSONObject();
    }

    /**
     * @param driver:传入NEO4J_JAVA驱动
     * @param statement:cypher
     * @return
     * @Description: TODO(构图导入等操作访问)
     */
    public synchronized static JSONObject composerWrite(Driver driver, String statement) {
        long startMill = System.currentTimeMillis();
        if (OngdbHeartBeat.isRegister()) {
            driver = NeoAccessor.ongdbHeartBeat.getWriterBlotMappingLocalDriver();
            if (NeoAccessor.driverIsResetDriver){
                driver = NeoAccessor.driver;
            }
        }
        boolean flag = false;
        // RETRY FAILURE QUERY
        while (!flag) {
            try (Session session = driver.session()) {
                flag = session.writeTransaction(tx -> {
                    tx.run(statement);
                    return true;
                });
            } catch (Exception e) {
                logger.info("Retrying query " + statement + "\r\n");
                e.printStackTrace();
            }
        }
        long stopMill = System.currentTimeMillis();
        long consume = (stopMill - startMill) / Integer.parseInt(String.valueOf(TimeUnit.MILL_SECOND_CV.getSymbolValue()));
        if (consume != 0) {
            logger.info("ONgDB driver composer success!consume:" + consume + "s");
        } else {
            logger.info("ONgDB driver composer success!consume:" + (stopMill - startMill) + "mills");
        }
        return new JSONObject();
    }

    /**
     * @param driver:传入NEO4J_JAVA驱动
     * @param statement:cypher
     * @return
     * @Description: TODO(构图导入等操作访问)
     */
    public synchronized static JSONObject composerAutoCommitTx(Driver driver, String statement) {
        long startMill = System.currentTimeMillis();
        if (OngdbHeartBeat.isRegister()) {
            driver = NeoAccessor.ongdbHeartBeat.getWriterBlotMappingLocalDriver();
            if (NeoAccessor.driverIsResetDriver){
                driver = NeoAccessor.driver;
            }
        }
        boolean flag = false;
        // RETRY FAILURE QUERY
        while (!flag) {
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    tx.run(statement);
                    tx.commit();
                    flag = true;
                }
            } catch (Exception e) {
                logger.info("Retrying query " + statement + "\r\n");
                e.printStackTrace();
            }
        }
        long stopMill = System.currentTimeMillis();
        long consume = (stopMill - startMill) / Integer.parseInt(String.valueOf(TimeUnit.MILL_SECOND_CV.getSymbolValue()));
        if (consume != 0) {
            logger.info("ONgDB driver composer success!consume:" + consume + "s");
        } else {
            logger.info("ONgDB driver composer success!consume:" + (stopMill - startMill) + "mills");
        }
        return new JSONObject();
    }

    /**
     * @param driver:传入NEO4J_JAVA驱动
     * @param statement:cypher
     * @return
     * @Description: TODO(构图导入等操作访问)
     */
    public synchronized static JSONObject composerReturnNodeId(Driver driver, String statement) {
        if (OngdbHeartBeat.isRegister()) {
            driver = NeoAccessor.ongdbHeartBeat.getWriterBlotMappingLocalDriver();
            if (NeoAccessor.driverIsResetDriver){
                driver = NeoAccessor.driver;
            }
        }
        JSONObject resultMessage = new JSONObject();
        long startMill = System.currentTimeMillis();
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                Result result = tx.run(statement);
                while (result.hasNext()) {
                    Record record = result.next();
                    Map<String, Object> map = record.asMap();
                    JSONObject object = JSONObject.parseObject(JSON.toJSONString(map));
                    resultMessage.putAll(object);
                }
//                tx.success();  // Mark this write as successful.
            }
        }
        long stopMill = System.currentTimeMillis();
        long consume = (stopMill - startMill) / Integer.parseInt(String.valueOf(TimeUnit.MILL_SECOND_CV.getSymbolValue()));
        if (consume != 0) {
            logger.info("ONgDB merge composer success!consume:" + consume + "s");
        } else {
            logger.info("ONgDB merge composer success!consume:" + (stopMill - startMill) + "mills");
        }
        return resultMessage;
    }

    /**
     * @param driver:传入NEO4J_JAVA驱动
     * @param statement:cypher
     * @return
     * @Description: TODO(构图导入等操作访问)
     */
    public synchronized static JSONObject composerAutoCommit(Driver driver, String statement) {
        if (OngdbHeartBeat.isRegister()) {
            driver = NeoAccessor.ongdbHeartBeat.getWriterBlotMappingLocalDriver();
            if (NeoAccessor.driverIsResetDriver){
                driver = NeoAccessor.driver;
            }
        }
        long startMill = System.currentTimeMillis();
        try (Session session = driver.session()) {
            /**
             * AUTO COMMIT TRANSACTION
             * **/
            session.run(statement);
        }
        long stopMill = System.currentTimeMillis();
        long consume = (stopMill - startMill) / Integer.parseInt(String.valueOf(TimeUnit.MILL_SECOND_CV.getSymbolValue()));
        logger.info("ONgDB driver composer auto commit success!consume:" + consume + "s");
        return new JSONObject();
    }

    public static JSONObject rowProperties(Driver driver, String cypher) {
        if (OngdbHeartBeat.isRegister()) {
            driver = NeoAccessor.ongdbHeartBeat.getWriterBlotMappingLocalDriver();
            if (NeoAccessor.driverIsResetDriver){
                driver = NeoAccessor.driver;
            }
        }
        JSONObject resultObject = new JSONObject();
        JSONArray resultArray = new JSONArray();
        try (Session session = driver.session()) {
            Result result = session.run(cypher);
            while (result.hasNext()) {
                Record record = result.next();
                Map<String, Object> map = record.asMap();
                JSONObject object = transferObject(map);
                resultArray.add(object);
            }
        }
        resultObject.put(CRUD.RETRIEVE_PROPERTIES.getSymbolValue(), resultArray);
        return resultObject;
    }

    public static JSONObject rowPropertiesReadOnly(Driver driver, String cypher) {
        if (OngdbHeartBeat.isRegister()) {
            driver = NeoAccessor.ongdbHeartBeat.getWriterBlotMappingLocalDriver();
            if (NeoAccessor.driverIsResetDriver){
                driver = NeoAccessor.driver;
            }
        }
        JSONObject resultObject = new JSONObject();
        JSONArray resultArray = new JSONArray();
        try (Session session = driver.session()) {
            session.readTransaction(tx -> {
                Result result = tx.run(cypher);
                while (result.hasNext()) {
                    Record record = result.next();
                    Map<String, Object> map = record.asMap();
                    JSONObject object = transferObject(map);
                    resultArray.add(object);
                }
                return tx;
            });
        }
        resultObject.put(CRUD.RETRIEVE_PROPERTIES.getSymbolValue(), resultArray);
        return resultObject;
    }

    private static JSONObject transferObject(Map<String, Object> map) {
        JSONObject object = new JSONObject();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value instanceof Relationship) {
                Relationship relationship = (Relationship) value;
                JSONObject relation = packRelation(relationship);
                object.put(key, relation);
            } else if (value instanceof List) {
                JSONArray array = transferArray(value);
                object.put(key, array);
            } else {
                object.put(key, value);
            }
        }
        return object;
    }

    private static JSONArray transferArray(Object value) {
        List<Object> list = (List<Object>) value;
        JSONArray array = new JSONArray();
        for (Object object : list) {
            if (object instanceof Relationship) {
                JSONObject relation = packRelation((Relationship) object);
                array.add(relation);
            } else if (object instanceof Node) {
                JSONObject relation = packNode((Node) object);
                array.add(relation);
            } else {
                array.add(object);
            }
        }
        return array;
    }

    /**
     * @param driver:传入NEO4J_JAVA驱动
     * @param statement:cypher
     * @return
     * @Description: TODO(检索)
     */
    public static JSONObject searcher(Driver driver, String statement) throws Exception {
        if (OngdbHeartBeat.isRegister()) {
            driver = NeoAccessor.ongdbHeartBeat.getReaderBlotMappingLocalDriver();
            if (NeoAccessor.driverIsResetDriver){
                driver = NeoAccessor.driver;
            }
        }
        long startMill = System.currentTimeMillis();

        JSONObject results = new JSONObject();
        JSONArray resultArray = new JSONArray();
        JSONObject arrayObject = new JSONObject();

        JSONArray columns = new JSONArray();
        JSONArray data = new JSONArray();
        JSONObject dataObject = new JSONObject();
        JSONObject graph = new JSONObject();

        JSONArray relationships = new JSONArray();
        JSONArray nodes = new JSONArray();
        JSONArray properties = new JSONArray();

        try (Session session = driver.session()) {
            // Auto-commit transactions are a quick and easy way to wrap a read.

            Result result = session.run(statement);
            // Each Cypher execution returns a stream of records.
            while (result.hasNext()) {
                Record record = result.next();
                // Values can be extracted from a record by index or name.

                List<Pair<String, Value>> list = record.fields();

                JSONObject propertiesPack = new JSONObject();
                for (int i = 0; i < list.size(); i++) {
                    Pair<String, Value> stringValuePair = list.get(i);
                    if (!columns.contains(stringValuePair.key())) {
                        columns.add(stringValuePair.key());
                    }
                    Value value = stringValuePair.value();
                    if (value instanceof NodeValue) {
                        JSONObject objectNode = packNode(value.asNode());
                        if (!nodes.contains(objectNode)) {
                            nodes.add(objectNode);
                        }
                    } else if (value instanceof PathValue) {

                        JSONArray objectNodes = packNodeByPath(value.asPath());
                        objectNodes.forEach(node -> {
                            JSONObject nodeObj = (JSONObject) node;
                            if (!nodes.contains(nodeObj)) {
                                nodes.add(nodeObj);
                            }
                        });

                        JSONArray objectRelas = packRelations(value.asPath());
                        objectRelas.forEach(relation -> {
                            JSONObject relationObj = (JSONObject) relation;
                            if (!relationships.contains(relationObj)) {
                                relationships.add(relationObj);
                            }
                        });
                    } else {
                        propertiesPack.putAll(packStringValuePair(stringValuePair));
                    }
                }
                if (!propertiesPack.isEmpty()) {
                    properties.add(propertiesPack);
                }
            }
        }
        graph.put("relationships", relationships);
        graph.put("nodes", nodes);
        graph.put("properties", properties);
        dataObject.put("graph", graph);
        data.add(dataObject);
        arrayObject.put("data", data);
        arrayObject.put("columns", columns);
        resultArray.add(arrayObject);
        results.put("results", resultArray);
        results.put("totalNodeSize", nodes.size());
        results.put("totalRelationSize", relationships.size());

        long stopMill = System.currentTimeMillis();
        long consume = (stopMill - startMill) / Integer.parseInt(String.valueOf(TimeUnit.MILL_SECOND_CV.getSymbolValue()));
        logger.info("ONgDB driver searcher success!consume:" + consume + "s");
        return results;
    }

    /**
     * @param driver:传入NEO4J_JAVA驱动
     * @param statement:cypher
     * @return
     * @Description: TODO(检索)
     */
    public static JSONObject readOnlySearcher(Driver driver, String statement) {
        if (OngdbHeartBeat.isRegister()) {
            driver = NeoAccessor.ongdbHeartBeat.getReaderBlotMappingLocalDriver();
            if (NeoAccessor.driverIsResetDriver){
                driver = NeoAccessor.driver;
            }
        }
        long startMill = System.currentTimeMillis();

        JSONObject results = new JSONObject();
        JSONArray resultArray = new JSONArray();
        JSONObject arrayObject = new JSONObject();

        JSONArray columns = new JSONArray();
        JSONArray data = new JSONArray();
        JSONObject dataObject = new JSONObject();
        JSONObject graph = new JSONObject();

        JSONArray relationships = new JSONArray();
        JSONArray nodes = new JSONArray();
        JSONArray properties = new JSONArray();

        try (Session session = driver.session()) {
            // Auto-commit transactions are a quick and easy way to wrap a read.
            int num = session.readTransaction(tx -> {
                Result result = tx.run(statement);
                // Each Cypher execution returns a stream of records.
                while (result.hasNext()) {
                    Record record = result.next();
                    // Values can be extracted from a record by index or name.

                    List<Pair<String, Value>> list = record.fields();

                    JSONObject propertiesPack = new JSONObject();
                    for (int i = 0; i < list.size(); i++) {
                        Pair<String, Value> stringValuePair = list.get(i);
                        if (!columns.contains(stringValuePair.key())) {
                            columns.add(stringValuePair.key());
                        }
                        Value value = stringValuePair.value();
                        if (value instanceof NodeValue) {
                            JSONObject objectNode = packNode(value.asNode());
                            if (!nodes.contains(objectNode)) {
                                nodes.add(objectNode);
                            }
                        } else if (value instanceof PathValue) {

                            JSONArray objectNodes = packNodeByPath(value.asPath());
                            objectNodes.forEach(node -> {
                                JSONObject nodeObj = (JSONObject) node;
                                if (!nodes.contains(nodeObj)) {
                                    nodes.add(nodeObj);
                                }
                            });

                            JSONArray objectRelas = packRelations(value.asPath());
                            objectRelas.forEach(relation -> {
                                JSONObject relationObj = (JSONObject) relation;
                                if (!relationships.contains(relationObj)) {
                                    relationships.add(relationObj);
                                }
                            });
                        } else {
                            propertiesPack.putAll(packStringValuePair(stringValuePair));
                        }
                    }
                    if (!propertiesPack.isEmpty()) {
                        properties.add(propertiesPack);
                    }
                }
                return 1;
            });

        }
        graph.put("relationships", relationships);
        graph.put("nodes", nodes);
        graph.put("properties", properties);
        dataObject.put("graph", graph);
        data.add(dataObject);
        arrayObject.put("data", data);
        arrayObject.put("columns", columns);
        resultArray.add(arrayObject);
        results.put("results", resultArray);
        results.put("totalNodeSize", nodes.size());
        results.put("totalRelationSize", relationships.size());

        long stopMill = System.currentTimeMillis();
        long consume = (stopMill - startMill) / Integer.parseInt(String.valueOf(TimeUnit.MILL_SECOND_CV.getSymbolValue()));
        logger.info("ONgDB driver searcher success!consume:" + consume + "s");
        return results;
    }

    private static JSONObject packStringValuePair(Pair<String, Value> stringValuePair) {
        String key = stringValuePair.key();
        Value value = stringValuePair.value();
        JSONObject object = new JSONObject();
        if (value instanceof BooleanValue) {
            object.put(key, value.asBoolean());
        } else if (value instanceof FloatValue) {
            object.put(key, value.asFloat());
        } else if (value instanceof IntegerValue) {
            // object.put(key, value.asInt());
            object.put(key, value.asLong());
        } else if (value instanceof ListValue) {
            object.put(key, value.asList());
        } else {
            object.put(key, value.asString());
        }
        return object;
    }

    private static JSONArray packRelations(Path path) {
        JSONArray arrayRelations = new JSONArray();
        for (Relationship relationship : path.relationships()) {
            arrayRelations.add(packRelation(relationship));
        }
        return arrayRelations;
    }

    private static JSONObject packRelation(Relationship relationship) {
        JSONObject currentRelation = new JSONObject();
        currentRelation.put("startNode", relationship.startNodeId());
        currentRelation.put("id", relationship.id());
        currentRelation.put("type", relationship.type());
        currentRelation.put("endNode", relationship.endNodeId());
        currentRelation.put("properties", JSONObject.parseObject(JSON.toJSONString(relationship.asMap())));
        return currentRelation;
    }

    private static JSONArray packNodeByPath(Path path) {
        JSONArray pathNodes = new JSONArray();
        for (Node node : path.nodes()) {
            pathNodes.add(packNode(node));
        }
        return pathNodes;
    }

    private static JSONObject packNode(Node node) {
        JSONObject currentNode = new JSONObject();
        currentNode.put("id", node.id());
        currentNode.put("properties", JSONObject.parseObject(JSON.toJSONString(node.asMap())));
        currentNode.put("labels", JSONArray.parseArray(JSON.toJSONString(node.labels())));
        return currentNode;
    }

}


