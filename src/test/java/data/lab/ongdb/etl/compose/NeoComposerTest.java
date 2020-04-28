package data.lab.ongdb.etl.compose;

import data.lab.ongdb.etl.compose.pack.*;
import data.lab.ongdb.etl.model.Label;
import data.lab.ongdb.etl.model.RelationshipType;
import data.lab.ongdb.search.Property;
import data.lab.ongdb.etl.util.MD5Digest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.v1.Config;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.etl.compose
 * @Description: TODO(Composer Test)
 * @date 2019/7/9 18:35
 */
public class NeoComposerTest {

    // NeoComposer Composer Accessor
    private static NeoComposer composer;

    private final static String ipPorts = "192.168.12.19:7687";

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.properties");
        NeoComposer.HTTP_SERVICE_IS_OPEN = false;
        // 支持多节点容灾

        /**
         * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
         * @param authAccount:节点的用户名
         * @param authPassword:节点用户名密码
         * @return
         * @Description: TODO(构造函数)
         */
//        composer = new NeoComposer(ipPorts, "neo4j", "123456");

        /**
         * @param contents:指定图数据使用什么格式返回，默认GRAPH格式返回
         * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
         * @param authAccount:节点的用户名
         * @param authPassword:节点用户名密码
         * @return
         * @Description: TODO(构造函数)
         */
//        composer = new NeoComposer(ResultDataContents.GRAPH, ipPorts, "neo4j", "123456");

        // 使用默认连接池配置
//        composer = new NeoComposer(ipPorts, "neo4j", "123456");

        // 设置持续尝试重试事务函数的最大时间
        composer = new NeoComposer(ipPorts, "neo4j", "123456"
                , Config.builder().withMaxTransactionRetryTime(600, TimeUnit.SECONDS).build());
    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * 批量动态导入一批节点-属性都更新
     **/
    @Test
    public void addMergeDynamicNodes() {
        List<UpdateNode> updateNodeList = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            UpdateNode updateNode = new UpdateNode();
            updateNode.setLabel(Labels.TwitterID);
            updateNode.set_entity_name("ertyu" + i + 2);
            updateNode.set_unique_uuid(MD5Digest.MD5(updateNode.get_entity_name()));
            Map<String, Object> properties = new HashMap<>();
            properties.put("url", "www.twitter.com/u/123452");
            properties.put("id", 75334231);
            properties.put("json", JSONObject.parseObject(JSON.toJSONString(properties)).toJSONString());
            updateNode.setProperties(properties);

            updateNodeList.add(updateNode);
        }

        System.out.println(JSONArray.parseArray(JSON.toJSONString(updateNodeList)));

        composer.addMergeDynamicNodes(updateNodeList, 200);

        System.out.println(composer.execute());
        composer.reset();
    }

    /**
     * 批量动态导入一批关系-属性都更新
     **/
    @Test
    public void addMergeDynamicRelations() {
        while (true) {
            List<UpdateRela> updateRelaList = new ArrayList<>();

            for (int i = 0; i < 1000; i++) {
                UpdateRela updateRela = new UpdateRela();

                updateRela.setFrom(Labels.TwitterID, MD5Digest.MD5("ertuy" + i));
                updateRela.setTo(Labels.TwitterID, MD5Digest.MD5("ertuy" + i + 2));

                updateRela.setType(Relationships.好友);
                updateRela.set_entity_name(Relationships.好友.toString());
                updateRela.set_unique_uuid(MD5Digest.MD5(("ertuy" + i) + ("ertuy" + i + 2) + (Relationships.好友)));

                Map<String, Object> properties = new HashMap<>();
                //            properties.put("url", "www.twitter.com/u/123452");
                //            properties.put("id", 75334231);
                //            properties.put("json", JSONObject.parseObject(JSON.toJSONString(properties)).toJSONString());
                updateRela.setProperties(properties);
                updateRelaList.add(updateRela);
            }

            composer.addMergeDynamicRelations(updateRelaList, 200);
            System.out.println(composer.execute());

            composer.reset();
        }
    }

    @Test
    public void addMergeDynamicNodesAndaddMergeDynamicRelations() {

        int num = 0;
        while (true) {
            num++;
            // 接口中构造请求无先后顺序，但是在构建关系时需要先保证节点已经被构建或者与关系同时构建

            // ==================================构造关系请求==================================
            List<UpdateRela> updateRelaList = new ArrayList<>();
            for (int i = 0; i < 2000; i++) {
                UpdateRela updateRela = new UpdateRela();

                updateRela.setFrom(Labels.TwitterID, MD5Digest.MD5("ertuy" + i + num));
                updateRela.setTo(Labels.TwitterID, MD5Digest.MD5("ertuy" + i + 2 + num));

                updateRela.setType(Relationships.好友);
                updateRela.set_entity_name(Relationships.好友.toString());
                updateRela.set_unique_uuid(MD5Digest.MD5(("ertuy" + i) + ("ertuy" + i + 2) + (Relationships.好友)));

                Map<String, Object> properties = new HashMap<>();
                properties.put("url", "www.twitter.com/u/123452");
                properties.put("id", 75334231);
                properties.put("json", JSONObject.parseObject(JSON.toJSONString(properties)).toJSONString());
                updateRela.setProperties(properties);
                updateRelaList.add(updateRela);
            }
            // 多次请求
            composer.addMergeDynamicRelations(updateRelaList, 200);
            composer.addMergeDynamicRelations(updateRelaList, 200);
            composer.addMergeDynamicRelations(updateRelaList, 200);
            composer.addMergeDynamicRelations(updateRelaList, 200);

            // ==================================构造节点请求==================================
            List<UpdateNode> updateNodeList = new ArrayList<>();
            for (int i = 0; i < 2000; i++) {
                UpdateNode updateNode = new UpdateNode();
                updateNode.setLabel(Labels.TwitterID);
                updateNode.set_entity_name("ertuy" + i + 2 + num);
                updateNode.set_unique_uuid(MD5Digest.MD5(updateNode.get_entity_name()));
                Map<String, Object> properties = new HashMap<>();
                properties.put("url", "www.twitter.com/u/123452");
                properties.put("id", 75334231);
                properties.put("json", JSONObject.parseObject(JSON.toJSONString(properties)).toJSONString());
                updateNode.setProperties(properties);

                updateNodeList.add(updateNode);
            }

            // 多次请求
            composer.addMergeDynamicNodes(updateNodeList, 200);
            composer.addMergeDynamicNodes(updateNodeList, 200);
            composer.addMergeDynamicNodes(updateNodeList, 200);
            composer.addMergeDynamicNodes(updateNodeList, 200);

            // 同时构造节点构建与关系构建请求，构造完请求执行一并提交执行,最后统一RESET
            System.out.println(composer.execute());
            composer.reset();

        }

    }

    @Test
    public void execute() {

//        System.out.println(composer.execute("MATCH p=()-[r]-(),MATCH p2=()-[r]-() RETURN p,p2 LIMIT 10"));

        System.out.println(composer.execute("" +
                "MATCH p=()-[r]-() RETURN p LIMIT 2\n" +
                "UNION ALL\n" +
                "MATCH p=()-[r]-() RETURN p LIMIT 2", CRUD.RETRIEVE));
    }

    @Test
    public void importCsvFileNodesAndRelations() {

        String nodesCsvName = "node-test.csv";
        String relationsCsvName = "relation-test.csv";
        composer.deleteCsv(nodesCsvName);
        composer.deleteCsv(relationsCsvName);

        // =======================================================生成NODE=======================================================
        /**
         * 勿必保证CSV文件字段列的写入顺序和执行导入时传入字段的顺序一致
         * **/
        String[] nodeRows = new String[]{"asdsad32423,john,这是导入的CSV,10\r\n",
                "345ssadsadsa,peter,这是导入的CSV,120324\r\n",
                "dsadsad,刘备,这是导入的CSV,3425\r\n",
                "sadsadasda,诸葛亮,这是导入的CSV,56465\r\n",
                "sadsadasdasd3v,司马懿,90870\r\n"
        };
//        String[] nodeRows = new String[4000000];
//        for (int i = 0; i <100000; i++) {
//            nodeRows[i]="asdsad32423"+i+",john,这是导入的CSV,10\r\n";
//            nodeRows[i+1]="345ssadsadsa"+i+",peter,这是导入的CSV,120324\r\n";
//            nodeRows[i+2]="dsadsad"+i+",刘备,这是导入的CSV,3425\r\n";
//            nodeRows[i+3]="asdsad32423"+i+",john,这是导入的CSV,10\r\n";
//            nodeRows[i+4]="sadsadasda"+i+",诸葛亮,这是导入的CSV,56465\r\n";
//            nodeRows[i+5]="sadsadasdasd3v"+i+",司马懿,90870\r\n";
//        }

        for (int i = 0; i < nodeRows.length; i++) {
            String datum = nodeRows[i];
            composer.writeCsvBody(nodesCsvName, datum);
        }

        /**
         * @param csvName:CSV文件名（数据写入CSV的顺序需要和方法传入参数顺序保持一致）String _uniqueField, String... _key
         * @param label:节点标签
         * @param _uniqueField:合并的唯一字段
         * @param _key:MERGE的属性字段
         * @return
         * @Description: TODO(导入节点CSV)
         */
        System.out.println(composer.executeImportCsv(1000, nodesCsvName, Label.label("Person"), Field.UNIQUEUUID.getSymbolValue(),
                Field.ENTITYNAME.getSymbolValue(), "comment", "count"));

        // =======================================================生成关系=======================================================
        /**
         * 勿必保证CSV文件字段列的写入顺序和执行导入时传入字段的顺序一致
         * **/
        String[] relationRows = new String[]{"asdsad32423,345ssadsadsa," + System.currentTimeMillis() + ",这是导入的CSV\r\n",
                "sadsadasda,asdsad32423," + System.currentTimeMillis() + ",这是导入的CSV\r\n",
                "345ssadsadsa,sadsadasdasd3v," + System.currentTimeMillis() + ",这是导入的CSV\r\n"
        };
        for (int i = 0; i < relationRows.length; i++) {
            String datum = relationRows[i];
            composer.writeCsvBody(relationsCsvName, datum);
        }
        /**
         * @param csvName:CSV文件名（数据写入CSV的顺序需要和方法传入参数顺序保持一致）String _uniqueFieldStart,String _uniqueFieldEnd, String... _key
         * @param relationshipType:生成的关系名
         * @param startNodeLabel:起始节点的标签
         * @param _uniqueFieldStart:开始节点
         * @param endNodeLabel:结束节点的标签
         * @param _uniqueFieldEnd:结束节点
         * @param _key:MERGE的属性字段
         * @return
         * @Description: TODO(导入关系CSV)
         */
        System.out.println(composer.executeImportCsv(1000, relationsCsvName, RelationshipType.withName("好友"), Label.label("Person"),
                Label.label("Person"), Field.UNIQUEUUID.getSymbolValue(), Field.UNIQUEUUID.getSymbolValue(), "current_time", "comment"));

    }

    // UNWIND
    // [["asdsad32423", "345ssadsadsa", "1563875148889", "这是导入的CSV"],
    // ["sadsadasda", "asdsad32423", "1563875148889", "这是导入的CSV"],
    // ["345ssadsadsa", "sadsadasdasd3v", "1563875148889", "这是导入的CSV"]]
    // AS row RETURN row


    @Test
    public void unwindMerge() {
        // =======================================================生成NODE=======================================================
        /**
         * @param nodes:节点列表           Object[]中数据存入的顺序和接口参数传入字段顺序需要保持一致(String _uniqueField, String... _key)
         * @param label:节点标签
         * @param _uniqueField:合并的唯一字段
         * @param _key:MERGE的属性字段
         * @return
         * @Description: TODO(导入节点)
         */
        Object[] objects1 = new Object[]{"0asdsad32423", "john", System.currentTimeMillis(), 100};
        Object[] objects2 = new Object[]{"1asdsad32423", "john1", System.currentTimeMillis(), 101};
        Object[] objects3 = new Object[]{"2asdsad32423", "john2", System.currentTimeMillis(), 102};
        Object[] objects4 = new Object[]{"3asdsad32423", "john3", System.currentTimeMillis(), 103};
        List<Object[]> nodes = new ArrayList<>();
        nodes.add(objects1);
        nodes.add(objects2);
        nodes.add(objects3);
        nodes.add(objects4);

        System.out.println(composer.executeImport(nodes, Label.label("人物"),
                Field.UNIQUEUUID.getSymbolValue(), Field.ENTITYNAME.getSymbolValue(), "current_time", "count"));

        // =======================================================生成关系=======================================================
        /**
         * @param relations:关系列表          Object[]中数据存入的顺序和接口参数传入字段顺序需要保持一致(String _uniqueFieldStart, String _uniqueFieldEnd, String... _key)
         * @param relationshipType:生成的关系名
         * @param startNodeLabel:起始节点的标签
         * @param _uniqueFieldStart:开始节点
         * @param endNodeLabel:结束节点的标签
         * @param _uniqueFieldEnd:结束节点
         * @param _key:MERGE的属性字段
         * @return
         * @Description: TODO(导入关系)
         */
        Object[] objectsR1 = new Object[]{"0asdsad32423", "1asdsad32423", System.currentTimeMillis(), 100};
        Object[] objectsR2 = new Object[]{"1asdsad32423", "2asdsad32423", System.currentTimeMillis(), 111};
        Object[] objectsR3 = new Object[]{"2asdsad32423", "3asdsad32423", System.currentTimeMillis(), 112};
        Object[] objectsR4 = new Object[]{"3asdsad32423", "2asdsad32423", System.currentTimeMillis(), 113};
        List<Object[]> relations = new ArrayList<>();
        relations.add(objectsR1);
        relations.add(objectsR2);
        relations.add(objectsR3);
        relations.add(objectsR4);
        System.out.println(composer.executeImport(relations, RelationshipType.withName("好友"), Label.label("人物"), Label.label("人物"),
                Field.UNIQUEUUID.getSymbolValue(), Field.UNIQUEUUID.getSymbolValue(), "current_time", "count"));

    }

    @Test
    public void importApocMergeNodes() {
        List<NoUpdateNode> noUpdateNodeList = new ArrayList<>();

//        NoUpdateNode noUpdateNode = new NoUpdateNode();
//        noUpdateNode.setLabels(new Labels[]{Labels.TwitterID, Labels.虚拟账号, Labels.人});
//        noUpdateNode.set_unique_uuid("123213");
//        noUpdateNode.set_entity_name("古力娜扎");
//        noUpdateNode.setProperties("name", "古力娜扎", "test", true);

        NoUpdateNode noUpdateNode = new NoUpdateNode(new Labels[]{Labels.TwitterID, Labels.虚拟账号, Labels.人}, "123213",
                "name", "古力娜扎", "test", true);

        noUpdateNodeList.add(noUpdateNode);

        System.out.println(composer.importApocMergeNodes(noUpdateNodeList));
    }

    @Test
    public void importApocMergeNodes2() {
        List<NoUpdateNode> noUpdateNodeList = new ArrayList<>();

//        NoUpdateNode noUpdateNode = new NoUpdateNode();
//        noUpdateNode.setLabels(new Labels[]{Labels.TwitterID, Labels.虚拟账号, Labels.人});
//        noUpdateNode.set_unique_uuid("123213");
//        noUpdateNode.set_entity_name("古力娜扎");
//        noUpdateNode.setProperties("name", "古力娜扎", "test", true);

        NoUpdateNode noUpdateNode = new NoUpdateNode(new Labels[]{Labels.TwitterID, Labels.虚拟账号, Labels.人}, "123213",
                "name", "古力娜扎", "test", true);

        noUpdateNodeList.add(noUpdateNode);

        // 指定唯一字段
        System.out.println(composer.importApocMergeNodes(noUpdateNodeList, "gid"));
    }

    @Test
    public void importApocMergeRelations() {
        List<NoUpdateRela> noUpdateRelaList = new ArrayList<>();
//        NoUpdateRela noUpdateRela = new NoUpdateRela();
//        noUpdateRela.setFrom(100510);
//        noUpdateRela.setType(Relationships.回复);
//        noUpdateRela.setName(Relationships.回复.name());
//        noUpdateRela.setProperties("_mark",32423);
//        noUpdateRela.setTo(100599);
        NoUpdateRela noUpdateRela = new NoUpdateRela(100510, Relationships.点赞, Relationships.点赞.name(), 100599, "_mark", 32423);
        noUpdateRelaList.add(noUpdateRela);
        System.out.println(composer.importApocMergeRelations(noUpdateRelaList));
    }

    @Test
    public void mergeCypher() {
        String mergeCypher = Cypher.mergeNode()
                .setLabel(Label.label("Test"))
                .setUniqueField("unique", "adasu32423ugda")
                .setProperties("_entity_name", "test", "eid", 213)
                .setReturnNodeId(false)
                .toMerge();
        composer.execute(mergeCypher, CRUD.MERGE);
    }

    @Test
    public void mergeCypher_2() {
        String mergeCypher = Cypher.mergeNode()
                .setLabel(Label.label("Test"))
                .setUniqueField("unique", "adasu32423ugda")
                .setProperties("_entity_name", "test", "eid", 213)
                .setReturnNodeId(true)
                .toMerge();
        System.out.println(composer.execute(mergeCypher, CRUD.MERGE_RETURN_NODE_ID));
    }

    @Test
    public void mergeCypher_3() {
        String mergeCypher = Cypher.mergeRelation()
                .setStartId(13)
                .setRelationshipType(RelationshipType.withName("MERGE_RELATION_TEST"))
                .setProperties(
                        new Property("cluster_id", "213d12sad13fdwr234fd"),
                        new Property("cluster_master", 1))
                .setReturn(false)
                .setEndId(12)
                .toMerge();
        System.out.println(composer.execute(mergeCypher, CRUD.MERGE));
    }

    @Test
    public void mergeCypher_4() {
        String mergeCypher = Cypher.mergeNode()
                .setStartId(23432)
                .setProperties(
                        new Property("is_repub", 1)
                )
                .toMerge();
        System.out.println(composer.execute(mergeCypher, CRUD.MERGE));
    }

    @Test
    public void cutListByBatchSize() {
        // ==================================构造关系请求==================================
        List<Object[]> updateRelaList = new ArrayList<>();
        for (int i = 0; i < 2002; i++) {
            updateRelaList.add(new Object[]{1, 2312, 1231});
        }
        List<List<Object[]>> list = composer.cutListByBatchSize(updateRelaList, 200);
    }

    @Test
    public void deleter() {
        List<Long> ids = new ArrayList<>();
        ids.add(219141L);
//        composer.relationDeleter(ids);
    }

    @Test
    public void executeMagicIncorporateGraph_01() {
        /**
         * @param sourceId                       :归并的节点--★★★<被归并节点的关系全部转移到这个节点>
         * @param targetId                       :被归并的节
         * @param uniqueKey:用来排重关系的属性--保持与已有关系一致
         * @param isDelete                       :合并完成之后是否删除被归并节点和关系
         * @return
         * @Description: TODO(合并图谱 - 子图合并)
         */
        composer.executeMagicIncorporateGraph(11227, 14586, "name", true, 10);

        // 合并重复的关系类型
        // TEST:MATCH (n)-[r:隶属虚拟账号]-(m) where id(n)=85 AND id(m)=273 RETURN COLLECT(PROPERTIES(r)) AS propertiesList
        composer.checkRepeatRelationType(11227);
    }

    @Test
    public void executeMagicIncorporateGraph_02() {
        /**
         * @param sourceId                       :归并的节点--★★★<被归并节点的关系全部转移到这个节点>
         * @param targetId                       :被归并的节
         * @param uniqueKey:用来排重关系的属性--保持与已有关系一致
         * @param isDelete                       :合并完成之后是否删除被归并节点和关系
         * @return
         * @Description: TODO(合并图谱 - 子图合并)
         */
        List<Long> ids = new ArrayList<>();
        ids.add(8470L);
        ids.add(9267L);
        ids.add(9073L);
        ids.add(92L);
        ids.add(991L);
        ids.add(18484L);
        ids.add(12570L);
        ids.add(8873L);
        composer.executeMagicIncorporateGraph(11227, ids, "name", true, 10);

        // 合并重复的关系类型
        // TEST:MATCH (n)-[r:隶属虚拟账号]-(m) where id(n)=85 AND id(m)=273 RETURN COLLECT(PROPERTIES(r)) AS propertiesList
        composer.checkRepeatRelationType(11227);
    }

    @Test
    public void executeMagicIncorporateGraph_packData_02() {
        String nList = "MATCH (n)-[r]-(m) where id(n)=85 RETURN COLLECT(n) AS nList";
        JSONArray result = composer.execute(nList, CRUD.RETRIEVE_PROPERTIES).getJSONArray("retrieve_properties");
        System.out.println(result);
    }
}

