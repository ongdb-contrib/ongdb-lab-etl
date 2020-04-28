package data.lab.ongdb.util;

import data.lab.ongdb.common.CRUD;
import data.lab.ongdb.common.Labels;
import data.lab.ongdb.model.Label;
import data.lab.ongdb.search.NeoSearcher;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.v1.Config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.util
 * @Description: TODO(JSON TOOL TEST)
 * @date 2019/7/13 16:45
 */
public class JSONToolTest {

    private final static String ipPorts = "localhost:7687";
//    private final static String ipPorts = "192.168.12.19:7687";
//    private final static String ipPorts = "192.168.7.178:7688";

    private static Map<Label, List<Label>> childLabelsMap;
    private NeoSearcher neoSearcher = new NeoSearcher(ipPorts, "neo4j", "123456",
            Config.builder().withMaxTransactionRetryTime(600, TimeUnit.SECONDS).build());

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("config/log4j.properties");

        // 在为数据补充标签时，childLabelsMap最好设置为全局
        // 标签树的查询
        String labelsTreeCypher = "MATCH p=(:" + Labels.LabelsTree.name() + ")-[]-(:" + Labels.LabelsTree.name() + ") RETURN p";
        JSONObject labelsTreeResult = neoSearcher.execute(labelsTreeCypher, CRUD.RETRIEVE);

        // 查询叶子节点
        String leafCypher = "MATCH (n:LabelsTree) WHERE NOT (n)-->() RETURN DISTINCT n";
        JSONObject leafResult = neoSearcher.execute(leafCypher, CRUD.RETRIEVE);
        childLabelsMap = JSONTool.labelsTreeChildMap(labelsTreeResult, leafResult);
    }

    @Test
    public void sortD3GraphDataByNodeProperty() {
        JSONObject result = JSONObject.parseObject("{\"totalNodeSize\":6,\"totalRelationSize\":6,\"consume\":\"Total consume 0s,average consume 0s/request\",\"failed\":0,\"totalQuery\":1,\"message\":true,\"results\":[{\"data\":[{\"graph\":{\"relationships\":[{\"startNode\":22742,\"id\":2109287,\"type\":\"参与事件\",\"endNode\":280,\"properties\":{\"realType\":0,\"name\":\"参与事件\",\"behaviorType\":1}},{\"startNode\":1405802,\"id\":3394552,\"type\":\"参与事件\",\"endNode\":280,\"properties\":{\"realType\":\"0\",\"name\":\"参与事件\",\"behaviorType\":\"1\"}},{\"startNode\":22742,\"id\":29840,\"type\":\"参与事件\",\"endNode\":19807,\"properties\":{\"realType\":0,\"name\":\"参与事件\",\"behaviorType\":1}},{\"startNode\":1405802,\"id\":3391974,\"type\":\"参与事件\",\"endNode\":19807,\"properties\":{\"realType\":\"0\",\"name\":\"参与事件\",\"behaviorType\":\"1\"}},{\"startNode\":19798,\"id\":26521,\"type\":\"隶属虚拟账号\",\"endNode\":20103,\"properties\":{\"realType\":0,\"linkMode\":\"vid\",\"name\":\"隶属虚拟账号\",\"update_time_mills\":1565748119651,\"behaviorType\":1,\"status\":\"1\"}},{\"startNode\":19798,\"id\":2127500,\"type\":\"参与事件\",\"endNode\":19807,\"properties\":{\"realType\":0,\"name\":\"参与事件\",\"behaviorType\":1}}],\"nodes\":[{\"id\":22742,\"properties\":{\"zdr_name\":\"胡总马甲\",\"name\":\"胡总马甲\",\"insert_time\":\"2017-08-04 00:00:00\",\"update_time_mills\":1565967041463,\"inst_id\":\"0\",\"sysuser_id\":\"1\",\"rid\":595,\"status\":\"1\"},\"labels\":[\"人\",\"现实人员\"]},{\"id\":1405802,\"properties\":{\"zdr_name\":\"南宁律师覃永沛\",\"name\":\"南宁律师覃永沛\",\"insert_time\":\"2018-07-13 17:22:17\",\"update_time_mills\":1565967041577,\"sysuser_id\":\"3\",\"inst_id\":\"2\",\"text\":\"南宁律师覃永沛\",\"id\":1405802,\"rid\":1211,\"status\":\"1\"},\"labels\":[\"人\",\"现实人员\"]},{\"id\":280,\"properties\":{\"start_time\":\"2018-05-01 00:00:00\",\"eid\":1532,\"dep_id\":\"35\",\"create_time\":\"2018-07-31 23:59:59\",\"user_id\":\"285\",\"name\":\"武警广西总队停止有偿工作_1532\",\"end_time\":\"2018-07-31 23:59:59\",\"event_name\":\"武警广西总队停止有偿工作\",\"category\":\"1\",\"status\":\"1\"},\"labels\":[\"事\",\"专题事件\"]},{\"id\":19807,\"properties\":{\"start_time\":\"2016-01-18 00:00:00\",\"eid\":842,\"dep_id\":\"2\",\"create_time\":\"2016-01-31 23:59:59\",\"user_id\":\"11\",\"end_time\":\"2016-01-31 23:59:59\",\"name\":\"LDR测试_842\",\"event_name\":\"LDR测试\",\"category\":\"1\",\"status\":\"1\"},\"labels\":[\"事\",\"专题事件\"]},{\"id\":20103,\"properties\":{\"site_name\":\"微信\",\"vid\":\"1408\",\"site\":\"微信\",\"update_time\":\"2019-08-02 08:45:33\",\"user_type\":\"7\",\"vir_name\":\"杨l幂后援会\",\"name\":\"杨l幂后援会_3270642935_微信\",\"sysuser_id\":\"381\",\"bloggerId\":\"3270642935\",\"url\":\"yangmihyh\",\"status\":\"1\"},\"labels\":[\"人\",\"虚拟账号ID\",\"微信公众号ID\"]},{\"id\":19798,\"properties\":{\"zdr_name\":\"大幂幂\",\"name\":\"大幂幂\",\"insert_time\":\"2018-05-29 00:00:00\",\"update_time_mills\":1565967041520,\"inst_id\":\"0\",\"sysuser_id\":\"381\",\"rid\":1003,\"status\":\"1\"},\"labels\":[\"人\",\"现实人员\"]}],\"properties\":[]}}],\"columns\":[\"p\"]}],\"successful\":1}\n");
//        System.out.println(JSONTool.sortD3GraphDataByNodeProperty(result, "eid", SortOrder.DESC));
        System.out.println(JSONTool.transferToOtherD3(result));
    }

    @Test
    public void supplementFatherLabels() {

        // 一些没有父级标签的数据
        String pathCypher = "MATCH p=(:新浪微博ID)-[]-() RETURN p LIMIT 100";
        JSONObject result = neoSearcher.execute(pathCypher, CRUD.RETRIEVE);

        /**
         * @param leafLabelsMap:标签树叶子节点MAP，KEY是叶子节点，VALUE是叶子节点的所有父级节点
         * @param result:一些检索到的数据
         * @return
         * @Description: TODO(根据标签树补充父级标签)
         */
        JSONObject supplyData = JSONTool.supplyFatherLabels(childLabelsMap, result);
        System.out.println(supplyData);
    }

    @Test
    public void mergeRetrieveResult() {
        JSONObject result1 = neoSearcher.execute("MATCH p=(:热菜市场)-->() RETURN p LIMIT 1", CRUD.RETRIEVE);
        JSONObject result2 = neoSearcher.execute("MATCH p=()<--() RETURN p LIMIT 1", CRUD.RETRIEVE);

        JSONObject result = JSONTool.mergeResult(result1, result2);
        System.out.println(result);
    }

    @Test
    public void mergeRetrieveResult2() {
        JSONObject result1 = null;
        JSONObject result2 = neoSearcher.execute("MATCH p=()<--() RETURN p LIMIT 1", CRUD.RETRIEVE);

        JSONObject result = JSONTool.mergeResult(result1, result2);
        System.out.println(result);
    }

    @Test
    public void substitude() {
        JSONObject result = neoSearcher.execute("MATCH p=()<-[r]-() WHERE r.organizer=1 OR r.spreader=1 RETURN p", CRUD.RETRIEVE);
        JSONArray relationships = getNodeOrRelaList(result, "relationships");
        JSONArray nodes = getNodeOrRelaList(result, "nodes");

        JSONArray jointPropertiesNodes = nodes.stream().map(v -> {
            JSONObject object = (JSONObject) v;
            long id = object.getLongValue("id");

            // 判断是否为组织者
            if (isOrganizer(id, relationships)) {
                object.getJSONObject("properties").put("img","organizer_img");

                // 判断是否为传播者
            } else if (isSpreader(id, relationships)) {
                object.getJSONObject("properties").put("img","spreader_img");
            }
            return object;
        }).collect(Collectors.toCollection(JSONArray::new));

        System.out.println(jointPropertiesNodes);
    }

    private boolean isSpreader(long id, JSONArray relationships) {
        final String spreader = "spreader";
        final String organizer = "organizer";
        for (Object obj : relationships) {
            JSONObject relation = (JSONObject) obj;
            long start = relation.getLongValue("startNode");
            String type = relation.getString("type");
            JSONObject properties = relation.getJSONObject("properties");
            if ((start == id) && "参与事件".equals(type)) {
                if ((!properties.containsKey(organizer) || properties.getIntValue(organizer) != 1) &&
                        properties.containsKey(spreader) && properties.getIntValue(spreader) == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOrganizer(long id, JSONArray relationships) {
        final String organizer = "organizer";
        for (Object obj : relationships) {
            JSONObject relation = (JSONObject) obj;
            long start = relation.getLongValue("startNode");
            String type = relation.getString("type");
            JSONObject properties = relation.getJSONObject("properties");
            if ((start == id) && "参与事件".equals(type)) {
                if (properties.containsKey(organizer) && properties.getIntValue(organizer) == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param resultObject:结果集合
     * @param key:拿到节点或者关系集合    relationships-获取关系列表 nodes-获取节点列表
     * @return
     * @Description: TODO(从结果集解析NODE列表 - 判断结果集是否NODES为空)
     */
    public static JSONArray getNodeOrRelaList(JSONObject resultObject, String key) {
        if (resultObject != null) {
            JSONArray jsonArray = resultObject.getJSONArray("results");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("graph");
            return jsonObject2.getJSONArray(key);
        }
        return new JSONArray();
    }
}


