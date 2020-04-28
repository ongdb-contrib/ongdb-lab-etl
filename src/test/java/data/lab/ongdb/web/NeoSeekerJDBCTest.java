package data.lab.ongdb.web;
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

import data.lab.ongdb.common.AccessOccurs;
import data.lab.ongdb.common.CRUD;
import data.lab.ongdb.search.NeoSearcher;
import data.lab.ongdb.search._web_inter.StoreTimeField;
import data.lab.ongdb.util.DateUtil;
import data.lab.zdr.graph.common.label.Labels;
import data.lab.zdr.graph.model.Dbproperties;
import data.lab.zdr.graph.model.UserJson;
import data.lab.zdr.graph.utils.DbUtil;
import data.lab.zdr.graph.utils.FileOperate;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.LinkedHashMap;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.search
 * @Description: TODO(JDBC构造函数测试)
 * @date 2020/1/14 17:31
 */
public class NeoSeekerJDBCTest {

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

    private NeoSearcher neoSearcher;


    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.properties");
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
        // 其它新版本图谱中不建议使用此构造函数
        // 使用此构造含函数-除了旧接口使用JDBC，其它接口全部使用默认使用JAVA-DRIVER交互
        neoSearcher = new NeoSearcher(AccessOccurs.JDBC, dbproperties);
        // 使用此构造含函数-除了旧接口使用JDBC，其它接口全部使用默认使用JAVA-DRIVER交互
        ComboPooledDataSource pooledDataSource = DbUtil.getConNeo4jPool();
        neoSearcher = new NeoSearcher(AccessOccurs.JDBC, pooledDataSource);
    }

    @Test
    public void matchNodeTest() {
        System.out.println(neoSearcher.execute("MATCH (n) RETURN n LIMIT 10", CRUD.RETRIEVE));
    }

    @Test
    public void getNodeById() {
        System.out.println(neoSearcher.execute("MATCH (n) RETURN n LIMIT 10", CRUD.RETRIEVE));
        // 方法的参数参考 data\zdr改版SCJ接口v1.xls
        UserJson userJson = new UserJson();
        userJson.setId(11L);
        userJson.setAuthorityIds(null);
        System.out.println(neoSearcher.getNodeById(userJson));
        System.out.println(neoSearcher.getNodeById(userJson));
        System.out.println(neoSearcher.getNodeById(userJson));
    }

    @Test
    public void getGraphAllOneDataByName() {
        UserJson userJson = new UserJson();
        userJson.setId(123L);
        neoSearcher.getGraphAllOneDataByName(userJson);
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
        neoSearcher.shortestPathsByMultiNode(userJson);

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
        UserJson userJson = new UserJson();
        // 搜索关键词
        userJson.setName("易");
        // 节点标签数组
        JSONArray array = new JSONArray();
        array.add("新浪微博ID");
        userJson.setLabelsArray(array);
        neoSearcher.expansionAnalysisBaseInitNodeSearch(userJson);
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

        System.out.println(neoSearcher.pagingLoadNodeAndRelationships(userJson));
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
        UserJson userJson = new UserJson();
        userJson.setUserAndMarkFavorite(1);
        String result = neoSearcher.expansionAnalysisBaseInitFavorite(userJson);
        System.out.println(result);
    }

    @Test
    public void userDefinedImageUrl() {
    }

    @Test
    public void addNodeTags() {
    }

    @Test
    public void loadUserDefinedLabels() {
        String result = neoSearcher.loadUserDefinedLabels(new UserJson());
        System.out.println(result);
    }

    @Test
    public void loadUserDefinedLabelsStatistics() {
        UserJson userJson = new UserJson();
        userJson.setAuthorityIds(null);
        neoSearcher.loadUserDefinedLabelsStatistics(userJson);
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
    public void intimacyCalculation_01() {
        /**
         * 个人之间的亲密度计算：
         * 维度名称	亲密度	权重	详情
         * 个人互动维度	924.00	关系权重：互动类关系使用平均权重（发出推荐/技能背书人/关注/点赞/评论/转发/回复）	关系924条
         * 组织维度	0.00	空	空
         * 地点维度	0.00	空	空
         * 时间维度	0.00	空	空
         * 最短可达路径维度	5.00	关系权重：1层可达（5.83）2层可达（5.00）3层可达（4.17）4层可达（2.50）5层可达（1.67）6层可达（0.83）	当前节点与被计算节点路径可达性分析为2层，计算得分为5.00
         *
         * **/
        UserJson userJson = new UserJson();
        userJson.setMaster(474);
        userJson.setSlaveArray(JSONArray.parseArray("[137,479,478]")); //137,479,478
        System.out.println(neoSearcher.intimacyCalculation(userJson));
    }

    @Test
    public void intimacyCalculation_02() {

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

        // 0 代表相似 ~
        int similarityNum = 0;
        // 1 代表相同
        int sameNum = 1;
        // 2 不同
        int notSame = 2;
        // 中文检索不友好/增加一个
        String condition = "{\n" +
                "        \"0\": {\n" +
                "            \"昵称\": {\n" +
                "                \"nameNodeSpace\": \"吃瓜群众CJ\"\n" +
                "            }\n" +
                "        },\n" +
                "        \"1\": {\n" +
                "            \"昵称\": {\n" +
                "                \"nameNodeSpace\": \"吃瓜群众CJ\"\n" +
                "            }\n" +
                "        },\n" +
                "        \"2\": {}\n" +
                "    }";
        // 属性扩展
        UserJson userJson =new UserJson();
        userJson.setLabels("新浪微博ID");
        userJson.setMultiLabelMark(0);
        userJson.setProperties(JSONObject.parseObject(condition));
        System.out.println(neoSearcher.nodePropertiesExtend(userJson));
    }

    @Test
    public void nodePropertiesExtendCondition() {
        System.out.println(neoSearcher.nodePropertiesExtendCondition());
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
            System.out.println(neoSearcher.relationshipCalculate(userJson));
        }

//        System.out.println(neoSearcher.relationshipCalculate(userJson));
//        userJson.setRelationship("评论");
//        System.out.println(neoSearcher.relationshipCalculate(userJson));

    }

    @Test
    public void recommendKeyPerson() {
        UserJson userJson = new UserJson();
        userJson.setStartNodeId(544);
        userJson.setChinaFilter(false);
        userJson.setSkip(0);
        userJson.setLimit(10);
        System.out.println(neoSearcher.recommendKeyPerson(userJson));
    }

    @Test
    public void recommendKeyPersonCommunication() {
        UserJson userJson = new UserJson();
        userJson.setStartNodeId(473);
        userJson.setSkip(0);
        userJson.setLimit(10);
        System.out.println(neoSearcher.recommendKeyPersonCommunication(userJson));
    }

    @Test
    public void recommendKeyFriendPersonCommunication() {
        UserJson userJson = new UserJson();
        userJson.setStartNodeId(473);
        userJson.setSkip(0);
        userJson.setLimit(10);
        System.out.println(neoSearcher.recommendKeyFriendPersonCommunication(userJson));
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

        System.out.println(neoSearcher.targetPersonPublicFriendSort(userJson));
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
        System.out.println(neoSearcher.interactiveNetworkAnalyzer(userJson));
    }

    @Test
    public void interactiveReprintNetworkAnalyzer() {
        UserJson userJson = new UserJson();
        // 被分析的节点
        userJson.setId(544L);
        // 分页设置
        userJson.setSkip(0);
        userJson.setLimit(15);
        // 转发网络分析
        System.out.println(neoSearcher.interactiveReprintNetworkAnalyzer(userJson));
    }

    @Test
    public void interactiveReprintNetworkAnalyzerTraceSource() {
        // 被分析的节点
        UserJson userJson = new UserJson();
        userJson.setId(544L);
        // 被拖拽的IDS
        Integer[] arr = new Integer[]{2313};
        JSONArray ids = JSONArray.parseArray(JSON.toJSONString(arr));
        userJson.setIds(ids);
        userJson.setTracingSourceBool(true); // true不看帖子
        // 转发网络分析溯源
        System.out.println(neoSearcher.interactiveReprintNetworkAnalyzerTraceSource(userJson));
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
//        System.out.println(neoSearcher.interactiveNetworkAnalyzerTraceSource(userJson));

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
            System.out.println(neoSearcher.interactiveNetworkAnalyzerTraceSource(userJson));
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
            System.out.println(neoSearcher.shortestPathsByMultiNode(userJson));
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
        System.out.println(neoSearcher.shortestPathsByMultiNode(userJson));
    }

    @Test
    public void name() {
        // 39516L
        System.out.println(neoSearcher.eventOrgZdrExpand(39646L));
    }


    @Test
    public void loadHighRankSearchCondition_01() {
        // 高级检索的检索条件重置
        String condition = neoSearcher.highRankSearchCondition();
        JSONArray array = JSONArray.parseArray(condition);
        array.clear();
        putCondition(array, "节点名称", new String[]{"nameNodeSpace"}, Labels.values());
        System.out.println(array);
    }

    @Test
    public void loadHighRankSearchCondition_02() {
        // 高级检索的检索条件重置
        String condition = neoSearcher.highRankSearchCondition();
        System.out.println(condition);
    }

    // 无数值检索项
    // ------使用标签拼接
    @Test
    public void advancedSearchTestByFullText_01() {
        // 精确：EXACT 模糊：FUZZY
        // 条件：AND|OR|NOT
        JSONArray array = new JSONArray();
        // 第一个条件
        LinkedHashMap<String, Object> object1 = putCondition("节点名称", new String[]{"nameNodeSpace"}, Labels.values());
        object1.put("value", "引蛇出洞");
        object1.put("match", "EXACT");
        object1.put("condition", "AND");
        array.add(object1);
        // 第二个条件
        LinkedHashMap<String, Object> object2 = putCondition("节点名称", new String[]{"nameNodeSpace"}, Labels.values());
        object2.put("value", "警用");
        object2.put("match", "EXACT");
        object2.put("condition", "AND");
        array.add(object2);

        UserJson userJson = new UserJson();
        userJson.setEntityArray(array);
        String result = neoSearcher.freeTextSearch(userJson);
        System.out.println(result);
    }

    // EXACT:精确搜索 FUZZY:相关性评分搜索
    // ------不使用标签拼接
    // 纯全文检索检索所有节点
    @Test
    public void advancedSearchTestByFullText_02() {
        // 精确：EXACT 模糊：FUZZY
        // 条件：AND|OR|NOT
        JSONArray array = new JSONArray();
        // 第一个条件
        LinkedHashMap<String, Object> object1 = putCondition("节点名称", new String[]{"nameNodeSpace"}, Labels.values());
        object1.put("value", "引蛇出洞");
        object1.put("match", "FUZZY");
        object1.put("condition", "AND");
        array.add(object1);
        // 第二个条件
        LinkedHashMap<String, Object> object2 = putCondition("节点名称", new String[]{"nameNodeSpace"}, Labels.values());
        object2.put("value", "警用");
        object2.put("match", "FUZZY");
        object2.put("condition", "AND");
        array.add(object2);

        UserJson userJson = new UserJson();
        userJson.setEntityArray(array);
        String result = neoSearcher.freeTextSearchNoLabelJoint(userJson);
        System.out.println(result);
    }

    // ------不使用标签拼接
    // 纯全文检索检索所有节点，增加数值类检索条件
    @Test
    public void advancedSearchTestByFullText_03() {
        // 精确：EXACT 模糊：FUZZY
        // 条件：AND|OR|NOT
        JSONArray array = new JSONArray();
        // 第一个条件
        LinkedHashMap<String, Object> object1 = putCondition("节点名称", new String[]{"nameNodeSpace"}, Labels.values());
        object1.put("value", "引蛇出洞");
        object1.put("match", "EXACT");
        object1.put("condition", "AND");
        array.add(object1);
        // 第二个条件
        LinkedHashMap<String, Object> object2 = putCondition("节点名称", new String[]{"nameNodeSpace"}, Labels.values());
        object2.put("value", "警用");
        object2.put("match", "EXACT");
        object2.put("condition", "AND");
        array.add(object2);

        UserJson userJson = new UserJson();
        userJson.setEntityArray(array);

//        默认全部不限【下拉框中选择过滤项】
//        发布时间  开始 结束 pub_time_mills       <=n.pub_time_mills<=
//        转发时间  开始 结束 pub_time_mills       <=n.pub_time_mills<=
//        更新时间  开始 结束 update_time_mills    <=n.update_time_mills<=
//        事件时间  开始 结束 start_time_mills end_time_mills   <=start_time_mills AND end_time_mills<=

        long startTimeMill = DateUtil.dateToMillisecond("2020-01-01 12:00:00");
        long stopTimeMill = DateUtil.dateToMillisecond("2020-03-05 12:00:00");
        // 发布时间
//        String result = neoSearcher.freeTextSearchNoLabelJoint(userJson, StoreTimeField.pub_time_mills,startTimeMill,
//                StoreTimeField.pub_time_mills,stopTimeMill);
        // 转发时间
//        String result = neoSearcher.freeTextSearchNoLabelJoint(userJson, StoreTimeField.pub_time_mills,startTimeMill,
//                StoreTimeField.pub_time_mills,stopTimeMill);
        // 更新时间
        String result = neoSearcher.freeTextSearchNoLabelJoint(userJson, StoreTimeField.update_time_mills, 1582275901343L,
                StoreTimeField.update_time_mills, 1582275901343L);
        // 事件时间
//        String result = neoSearcher.freeTextSearchNoLabelJoint(userJson, StoreTimeField.start_time_mills, 1573401600000L,
//                StoreTimeField.end_time_mills, 1577807999000L);

        System.out.println(result);
    }

    @Test
    public void advancedSearchLabels() {
        // 最子层标签加载
        String cypher = "MATCH (n:LabelsTree)-->(m:LabelsTree) WHERE m.labelName <> '虚拟账号ID' AND m.labelName <> '人' RETURN m.labelName AS label";
        System.out.println(neoSearcher.execute(cypher, CRUD.RETRIEVE_PROPERTIES));
    }

    // 有数值检索项
    @Test
    public void advancedSearchTestByUniversal_01() {
        // 精确：EXACT 模糊：FUZZY
        // 条件：AND|OR|NOT
        JSONArray array = new JSONArray();
        // 第一个条件
        LinkedHashMap<String, Object> object1 = putCondition("节点名称", new String[]{"nameNodeSpace"}, new String[]{"新浪微博发帖"});
        object1.put("value", "引蛇出洞");
        object1.put("match", "EXACT");
        object1.put("condition", "AND");
        array.add(object1);
        // 第二个条件
        LinkedHashMap<String, Object> object2 = putCondition("节点名称", new String[]{"nameNodeSpace"}, new String[]{"新浪微博发帖"});
        object2.put("value", "警用");
        object2.put("match", "EXACT");
        object2.put("condition", "AND");
        array.add(object2);

        UserJson userJson = new UserJson();
        userJson.setEntityArray(array);

//        默认全部不限【下拉框中选择过滤项】
//        发布时间  开始 结束 pub_time_mills       <=n.pub_time_mills<=
//        转发时间  开始 结束 pub_time_mills       <=n.pub_time_mills<=
//        更新时间  开始 结束 update_time_mills    <=n.update_time_mills<=
//        事件时间  开始 结束 start_time_mills end_time_mills   <=start_time_mills AND end_time_mills<=

//        long startTimeMill = DateUtil.dateToMillisecond("2020-01-01 12:00:00");
//        long stopTimeMill = DateUtil.dateToMillisecond("2020-03-05 12:00:00");
        String timeFilterCondition = "1582275901340<=n.pub_time_mills<=1582275901346";
        String result = neoSearcher.universalSearchLabelJoint(userJson, timeFilterCondition);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(高级检索条件数据封装)
     */
    private static LinkedHashMap<String, Object> putCondition(String chineseKey, String[] englishKey, String[] labelRelation) {

        // OBJECT start 0 end >0
        // 加载用户可选条件
        // chineseKey 中文条件（一个）
        // englishKey 英文字段（多个）
        // labelRelation 可关联的标签类别

        // 加载检索条件
        // value 检索值（为空不添加）
        // match  EXACT FUZZY
        // condition 与或非(AND OR NOT)

        LinkedHashMap<String, Object> object = new LinkedHashMap<>();
        object.put("chineseKey", chineseKey);
        object.put("englishKey", englishKey);
        object.put("labelRelation", labelRelation);
        return object;
    }

    /**
     * @param
     * @return
     * @Description: TODO(高级检索条件数据封装)
     */
    private static LinkedHashMap<String, Object> putCondition(String chineseKey, String[] englishKey, Labels[] labelRelation) {

        // OBJECT start 0 end >0
        // 加载用户可选条件
        // chineseKey 中文条件（一个）
        // englishKey 英文字段（多个）
        // labelRelation 可关联的标签类别

        // 加载检索条件
        // value 检索值（为空不添加）
        // match  EXACT FUZZY
        // condition 与或非(AND OR NOT)

        LinkedHashMap<String, Object> object = new LinkedHashMap<>();
        object.put("chineseKey", chineseKey);
        object.put("englishKey", englishKey);
        object.put("labelRelation", labelRelation);
        return object;
    }

    /**
     * @param
     * @return
     * @Description: TODO(高级检索条件数据封装)
     */
    private static JSONArray putCondition(JSONArray conditionArray, String chineseKey, String[] englishKey, Labels[] labelRelation) {

        // OBJECT start 0 end >0
        // 加载用户可选条件
        // chineseKey 中文条件（一个）
        // englishKey 英文字段（多个）
        // labelRelation 可关联的标签类别

        // 加载检索条件
        // value 检索值（为空不添加）
        // match  EXACT FUZZY
        // condition 与或非(AND OR NOT)

        JSONObject object = new JSONObject();
        object.put("chineseKey", chineseKey);
        object.put("englishKey", englishKey);
        object.put("labelRelation", labelRelation);
        conditionArray.add(object);
        return conditionArray;
    }
}


