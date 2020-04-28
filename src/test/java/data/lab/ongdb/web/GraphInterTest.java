package data.lab.ongdb.web;

import data.lab.zdr.graph.external.GraphInter;
import data.lab.zdr.graph.model.Dbproperties;
import data.lab.zdr.graph.model.UserJson;
import data.lab.zdr.graph.utils.DbUtil;
import data.lab.zdr.graph.utils.FileOperate;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * 　　　　　　　 ┏┓       ┏┓+ +
 * 　　　　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　 ┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 █████━█████  ┃+
 * 　　　　　　　┃　　　　　　 ┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　 ┃ + +
 * 　　　　　　　┗━━┓　　　 ┏━┛
 * ┃　　  ┃
 * 　　　　　　　　　┃　　  ┃ + + + +
 * 　　　　　　　　　┃　　　┃　Code is far away from     bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ +
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 */

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.zdr.graph.external
 * @Description: TODO(GraphInter类测试)
 * @date 2019/5/6 14:25
 */
public class GraphInterTest {

    private GraphInter graphInter;

    // ------------------配置一------------------
    private static String url = "jdbc:neo4j:bolt://192.168.12.19:7687";
    private static String userName = "neo4j";
    private static String password = "123456";
    private static String driver = "org.neo4j.jdbc.Driver";
    private static String initialPoolSize = "10";
    private static String minPoolSize = "1";
    private static String maxPoolSize = "500";
    private static String maxStatements = "0";
    private static String maxIdleTime = "200";
    private static String acquireIncrement = "10";
    private static String acquireRetryAttempts = "30";
    private static String breakAfterAcquireFailure = "false";
    private static String testConnectionOnCheckout = "false";
    private static String testConnectionOnCheckin = "true";
    private static String idleConnectionTestPeriod = "60";
    private static String checkoutTimeout = "10000";

    // ------------------配置二------------------
//    private static String url = "jdbc:neo4j:bolt://192.168.43.104:7687";
//    private static String userName = "neo4j";
//    private static String password = "123456";
//    private static String driver = "org.neo4j.jdbc.Driver";
//    private static String acquireIncrement = "10";
//    private static String idleConnectionTestPeriod = "60";
//    private static String initialPoolSize = "10";
//    private static String maxIdleTime = "200";
//    private static String maxPoolSize = "500";
//    private static String maxStatements = "0";
//    private static String minPoolSize = "5";


    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("config"+ File.separator+"log4j.properties");
    }

    @Before
    public void init() {
        // ------------------配置一------------------
        Dbproperties dbproperties = new Dbproperties();
        dbproperties.setUrl(url);
        dbproperties.setUserName(userName);
        dbproperties.setPassword(password);
        dbproperties.setAcquireRetryAttempts(acquireRetryAttempts);
        dbproperties.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
        dbproperties.setTestConnectionOnCheckout(testConnectionOnCheckout);
        dbproperties.setTestConnectionOnCheckin(testConnectionOnCheckin);
        dbproperties.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
        dbproperties.setDriver(driver);
        dbproperties.setInitialPoolSize(initialPoolSize);
        dbproperties.setMinPoolSize(minPoolSize);
        dbproperties.setMaxPoolSize(maxPoolSize);
        dbproperties.setMaxStatements(maxStatements);
        dbproperties.setMaxIdleTime(maxIdleTime);
        dbproperties.setAcquireIncrement(acquireIncrement);
        dbproperties.setCheckoutTimeout(checkoutTimeout);

        // ------------------配置二------------------
//        Dbproperties dbproperties = new Dbproperties();
//        dbproperties.setUrl(url);
//        dbproperties.setUserName(userName);
//        dbproperties.setPassword(password);
//        dbproperties.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
//        dbproperties.setDriver(driver);
//        dbproperties.setInitialPoolSize(initialPoolSize);
//        dbproperties.setMinPoolSize(minPoolSize);
//        dbproperties.setMaxPoolSize(maxPoolSize);
//        dbproperties.setMaxStatements(maxStatements);
//        dbproperties.setMaxIdleTime(maxIdleTime);
//        dbproperties.setAcquireIncrement(acquireIncrement);

        graphInter = new GraphInter(dbproperties);
        graphInter=new GraphInter(DbUtil.getConNeo4jPool());
    }

    @Test
    public void getGraphAllOneDataByName() {
    }

    @Test
    public void getNodeById() {
        // 方法的参数参考 data\zdr改版SCJ接口v1.xls
        UserJson userJson = new UserJson();
        userJson.setId(11l);
        userJson.setAuthorityIds(null);
        System.out.println(graphInter.getNodeById(userJson));
        System.out.println(graphInter.getNodeById(userJson));
        System.out.println(graphInter.getNodeById(userJson));
    }

    @Test
    public void getNodeByIdsArray() {
    }

    @Test
    public void getZdrAnswer() {
    }

    @Test
    public void getZdrAnswerRealTime() {
    }

    @Test
    public void loadQuestions() {
    }

    @Test
    public void multiEntitiesCrossMatch() {
    }

    @Test
    public void shortestPathsByMultiNode() {
        UserJson userJson = new UserJson();

        JSONArray entityIds = new JSONArray();
        Integer[] idsEntity = new Integer[]{30376, 49146, 53697, 53694, 53704, 53977, 53696, 943, 49145, 53699, 53975, 53700, 953, 53701, 53702, 53695, 53703, 53705, 53698};
        for (int i = 0; i < idsEntity.length; i++) {
            Integer integer = idsEntity[i];
            entityIds.add(integer);
        }
        userJson.setEntityIds(entityIds);
        JSONArray ids = new JSONArray();
        ids.add(127663);
        userJson.setIds(ids);
        graphInter.shortestPathsByMultiNode(userJson);

    }

    @Test
    public void multiEntitiesFuzzyCrossMatchCount() {
    }

    @Test
    public void allShortestPathsCount() {
    }

    @Test
    public void addNode() {
    }

    @Test
    public void addRelationship() {
    }

    @Test
    public void deleteNode() {
    }

    @Test
    public void deleteRelationship() {
    }

    @Test
    public void updateRelationship() {
    }

    @Test
    public void expansionAnalysisBaseInit() {
    }

    @Test
    public void expansionAnalysisBaseInitNodeSearch() {
    }

    @Test
    public void getRelationshipNameList() {
    }

    @Test
    public void getPropertyKeyList() {
    }

    @Test
    public void getLabelList() {
    }

    @Test
    public void getLabelChildList() {
    }

    @Test
    public void getLabelsTree() {
    }

    @Test
    public void getLabelsTreeFather() {
    }

    @Test
    public void getHierarchyLabels() {
    }

    @Test
    public void getCurrentNodeRelationships() {
    }

    @Test
    public void pagingLoadNodeAndRelationships() {
        UserJson userJson = new UserJson();
        userJson.setId(127764l);
        JSONArray array = new JSONArray();
        array.add("隶属虚拟账号");
        userJson.setRelationships(array);
        userJson.setSkip(0);
        userJson.setLimit(10);

        System.out.println(graphInter.pagingLoadNodeAndRelationships(userJson));
    }

    @Test
    public void loadRaw() {
        String cypher = "MATCH (n:`现实人员`) WITH collect(id(n)) AS ids\n" +
                "with ids as id_list \n" +
                "match (v) where id(v) in id_list\n" +
                "with collect(v) as nodes\n" +
                "unwind nodes as source\n" +
                "unwind nodes as target\n" +
                "with source,target where id(source)<id(target)\n" +
                "match paths = allShortestPaths((source)-[*..6]-(target)) \n" +
                "with paths limit 10 return paths";
        System.out.println(DbUtil.exetueCypherJDBC(cypher, null));
    }

    @Test
    public void getNodesByRelation() {
    }

    @Test
    public void addNodeFavorite() {
    }

    @Test
    public void expansionAnalysisBaseInitFavorite() {
    }

    @Test
    public void userDefinedImageUrl() {
    }

    @Test
    public void addNodeTags() {
    }

    @Test
    public void loadUserDefinedLabels() {
    }

    @Test
    public void loadUserDefinedLabelsStatistics() {
        UserJson userJson = new UserJson();
        userJson.setAuthorityIds(null);
        graphInter.loadUserDefinedLabelsStatistics(userJson);
    }

    @Test
    public void loadUserDefinedLabelNode() {
    }

    @Test
    public void timeCollisions() {
    }

    @Test
    public void timeCollisionsCount() {
    }

    @Test
    public void loadRecentlyFavorite() {
    }

    @Test
    public void updateHistorySearchDequeNodes() {
    }

    @Test
    public void loadHistorySearchDequeNodes() {
    }

    @Test
    public void loadGraphModelLabelsStatistics() {
    }

    @Test
    public void intimacyCalculation() {

        UserJson userJson = new UserJson();
        userJson.setMaster(51993);
        userJson.setSlaveArray(JSONArray.parseArray("[54859,219250,52372,54381,210743,117271]"));
        System.out.println(graphInter.intimacyCalculation(userJson));
    }

    @Test
    public void graphCombatMethod() {
    }

    @Test
    public void getGraphCombatType() {
    }

    @Test
    public void graphCombatAutoRun() {
    }

    @Test
    public void accordingToTypeStatistics() {
    }

    @Test
    public void getNodesForTags() {
    }

    @Test
    public void getGraphTwoDimensionNode() {
    }

    @Test
    public void getSelectGraphTwoDimensionNode() {
    }

    @Test
    public void importData() {
    }

    @Test
    public void updateNodeProperties() {
    }

    @Test
    public void nodePropertiesExtend() {
    }

    @Test
    public void nodePropertiesExtendCondition() {
    }

    @Test
    public void freeTextSearch() {
    }

    @Test
    public void highRankSearchCondition() {
    }

    @Test
    public void loadCalculateCategory() {
    }

    @Test
    public void relationshipCalculate() {
//        // 权限
//        JSONArray authorityIds = userJson.getAuthorityIds();
//
//        // 是否溯源
//        boolean tracingSourceBool = userJson.isTracingSourceBool();
//
//        String relationship = userJson.getRelationship();
//
//        long startNodeId = userJson.getStartNodeId();
//        long endNodeId = userJson.getEndNodeId();

        UserJson userJson = new UserJson();
        userJson.setAuthorityIds(null);
        userJson.setTracingSourceBool(false);
        userJson.setRelationship("转发");
        userJson.setStartNodeId(262699);
        userJson.setEndNodeId(259060);

        for (int i = 0; 1 < 100; i++) {
            System.out.println(graphInter.relationshipCalculate(userJson));
        }

//        System.out.println(graphInter.relationshipCalculate(userJson));
//        userJson.setRelationship("评论");
//        System.out.println(graphInter.relationshipCalculate(userJson));

    }

    @Test
    public void recommendKeyPerson() {
    }

    @Test
    public void recommendKeyPersonCommunication() {
    }

    @Test
    public void targetGroupFriendsRelaCount() {
    }

    @Test
    public void targetGroupPersonRelaCount() {
    }

    @Test
    public void targetPersonRelaDig() {
    }

    @Test
    public void targetGroupPersonDigger() {
    }

    @Test
    public void targetPersonPublicFriendSort() {
        UserJson userJson = new UserJson();
        // 被分析的节点
        userJson.setId(51999l);
        // 分页设置
        userJson.setSkip(0);
        userJson.setLimit(15);

        System.out.println(graphInter.targetPersonPublicFriendSort(userJson));
    }

    @Test
    public void targetPersonFriendInteractionSort() {
    }

    @Test
    public void packCurrentAuthorityNodesRela() {
    }

    @Test
    public void interactiveNetworkAnalyzer() {
        UserJson userJson = new UserJson();
        // 被分析的节点
        userJson.setId(258956l);
        // 分页设置
        userJson.setSkip(0);
        userJson.setLimit(15);
//        userJson.setStartTime("2019-05-16 00:00:00");
//        userJson.setStopTime("2019-05-17 23:59:59");
        System.out.println(graphInter.interactiveNetworkAnalyzer(userJson));
    }

    @Test
    public void interactiveNetworkAnalyzerTraceSource() {

//        // 被分析的节点
//        UserJson userJson = new UserJson();
//        userJson.setId(53702l);
//        // 被拖拽的IDS
//        JSONArray ids = new JSONArray();
//        ids.add(63438);
//        ids.add(63536);
//        userJson.setIds(ids);
//        System.out.println(graphInter.interactiveNetworkAnalyzerTraceSource(userJson));

        // 被分析的节点
        UserJson userJson = new UserJson();
        userJson.setId(359l);
        // 被拖拽的IDS
        Integer[] arr = new Integer[]{1638};
        JSONArray ids = JSONArray.parseArray(JSON.toJSONString(arr));
        userJson.setIds(ids);
        userJson.setTracingSourceBool(true); // true不看帖子

//        userJson.setStartTime("2019-05-16 00:00:00");
//        userJson.setStopTime("2019-05-17 23:59:59");

        for (int i = 0; i < 100; i++) {
            System.out.println(graphInter.interactiveNetworkAnalyzerTraceSource(userJson));
        }
    }

    @Test
    public void test() {
        for (int i = 0; i < 100; i++) {
            // 加载最短路径分析的结果
            String cypher = "MATCH (n:`现实人员`) WITH collect(id(n)) AS ids\n" +
                    "with ids as id_list \n" +
                    "match (v) where id(v) in id_list\n" +
                    "with collect(v) as nodes\n" +
                    "unwind nodes as source\n" +
                    "unwind nodes as target\n" +
                    "with source,target where id(source)<id(target)\n" +
                    "match paths = allShortestPaths((source)-[*..6]-(target)) \n" +
                    "with paths limit 100 return paths";
            JSONObject object = DbUtil.exetueCypherJDBC(cypher, null);

            // 开始子图分析
            JSONObject result = object;
            JSONArray targetIds = DbUtil.getNodesIds("MATCH (n:`现实人员`) RETURN id(n) AS id");

            JSONArray jsonArraySpecific = new JSONArray();
            jsonArraySpecific.add(192906);
            JSONArray specificIds = jsonArraySpecific;

            UserJson userJson = new UserJson();
            userJson.setKeyValue(result);
            userJson.setEntityIds(targetIds);
            userJson.setIds(specificIds);
            System.out.println(graphInter.shortestPathsByMultiNode(userJson));
        }
    }

    @Test
    public void shortestPathsByMultiNodeTest() {
        FileOperate operate = new FileOperate();
        JSONObject raw = JSONObject.parseObject(operate.readAllLine("data/raw.json", "UTF-8"));

        UserJson userJson = new UserJson();
        userJson.setKeyValue(raw.getJSONObject("keyValue"));
        userJson.setEntityIds(raw.getJSONArray("entityIds"));
        userJson.setIds(raw.getJSONArray("ids"));
        System.out.println(graphInter.shortestPathsByMultiNode(userJson));
    }

    @Test
    public void name() {
        // 39516L
        System.out.println(graphInter.eventOrgZdrExpand(39646L));
    }

}

