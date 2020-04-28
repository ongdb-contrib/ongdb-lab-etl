package data.lab.ongdb.etl.util;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.util.Neo4jDataUtils
 * @Description: TODO(D3 - NEO4J : data tool)
 * @date 2020/4/28 14:40
 */
public class Neo4jDataUtils {

    private final static Logger logger = Logger.getLogger(Neo4jDataUtils.class);

    /**
     * @param
     * @return
     * @Description: TODO(过滤权限关系)
     */
    private static JSONArray filterAuthorityRela(JSONArray relationShips, List<Long> filterAuthorityNodeIds) {
        JSONArray relationShipsAfterFilter = new JSONArray();
        if (relationShips != null && !relationShips.isEmpty() && filterAuthorityNodeIds != null && !filterAuthorityNodeIds.isEmpty()) {
            relationShips.forEach(v -> {
                JSONObject relaObj = (JSONObject) v;
                if (!filterAuthorityNodeIds.contains(relaObj.getLong("startNode")) && !filterAuthorityNodeIds.contains(relaObj.getLong("endNode"))) {
                    relationShipsAfterFilter.add(relaObj);
                }
            });
            return relationShipsAfterFilter;
        } else {
            return relationShips;
        }
    }

    /**
     * @param
     * @return
     * @Description: TODO(排重节点并去掉标签的null值)
     */
    private static JSONArray distinctAndRemoveNull(JSONArray nodes) {
        nodes.removeIf(v -> v == null);
        if (!nodes.isEmpty()) {
            return nodes.parallelStream().filter(distinctById(v -> {
                JSONObject object = (JSONObject) v;
                if (object != null) {
                    return object.getString("id");
                } else {
                    return null;
                }
            })).map(v -> {
                JSONObject object = (JSONObject) v;
                JSONArray labels = object.getJSONArray("labels");
                labels = labels.parallelStream().filter(obj -> obj != null).collect(Collectors.toCollection(JSONArray::new));
                object.put("labels", labels);
                return object;
            }).sorted((object1, object2) -> {
                // searchEngineWeight排序
                JSONObject nodePro1 = object1.getJSONObject("properties");
                JSONObject nodePro2 = object2.getJSONObject("properties");
                Double dou1 = nodePro1.getDouble("searchEngineWeight");
                Double dou2 = nodePro2.getDouble("searchEngineWeight");
                return weightCompare(dou1, dou2);

            }).collect(Collectors.toCollection(JSONArray::new));
        } else {
            return nodes;
        }
    }

    /**
     * @param
     * @return
     * @Description: TODO(权重比较)
     */
    private static int weightCompare(Double d1, Double d2) {
        Optional<Double> dou1 = Optional.ofNullable(d1);
        Optional<Double> dou2 = Optional.ofNullable(d2);
        Integer int1 = 0, int2 = 0;
        if (dou1.orElse(0.0).intValue() == dou2.orElse(0.0).intValue()) {
            if (dou1.orElse(0.0) > dou2.orElse(0.0)) {
                int1 = dou1.orElse(0.0).intValue() + 1;
                int2 = dou2.orElse(0.0).intValue();
            } else if (dou1.orElse(0.0) < dou2.orElse(0.0)) {
                int1 = dou1.orElse(0.0).intValue();
                int2 = dou2.orElse(0.0).intValue() + 1;
            }
        } else {
            int1 = dou1.orElse(0.0).intValue();
            int2 = dou2.orElse(0.0).intValue();
        }
        // SEARCH ENGINE WEIGHT RESULT ASC
        return int2 - int1;
    }

    /**
     * @param
     * @return
     * @Description: TODO(对节点集通过ID去重)
     */
    private static <T> Predicate<T> distinctById(Function<? super T, ?> idExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(idExtractor.apply(t), Boolean.TRUE) == null;
    }

//    /**
//     * @return
//     * @Description: TODO(获取JSONObject格式的node数据)
//     */
//    public static JSONObject getNodeData(Value value) {
//        // 转为node
//        Node n = value.asNode();
//        JSONObject NodeData = Neo4jDataUtils.getNodeObj(n);
//        return NodeData;
//    }

    // 被过滤的节点列表
    private static List<Long> filterAuthorityNodeIds;

    /**
     * @return
     * @Description: TODO(获取JSONObject格式的node + relationship数据)
     */
    public static JSONObject getPathData(Value value) {
        // 转换路径
        Path p = value.asPath();
        // 结果obj
        JSONObject result = new JSONObject();
        // 节点arr
        JSONArray nodesArr = Neo4jDataUtils.getNodeArr(p.nodes());
        // 关系arr
        JSONArray relationshipsArr = Neo4jDataUtils.getRelationShipsArr(p.relationships());
        result.put("nodes", nodesArr);
        result.put("relationships", relationshipsArr);
        return result;
    }

    /**
     * @param n
     * @return
     * @Description: TODO(获取节点对象)
     */
    public static JSONObject getNodeObj(Node n) {
        // 创建node对象
        JSONObject NodeData = new JSONObject();
        // id
        NodeData.put("id", n.id());
        // labels
        Iterable<String> labels = n.labels();
        JSONArray NodeLabels = new JSONArray();
        for (String label : labels) {
            NodeLabels.add(label);
        }
        NodeData.put("labels", NodeLabels);
        // properties
        JSONObject NodeProperties = new JSONObject();
        Map<String, Object> mapNodePro = n.asMap();
        for (Entry<String, Object> entry : mapNodePro.entrySet()) {
            NodeProperties.put(entry.getKey().toString(), entry.getValue());
        }
        NodeData.put("properties", NodeProperties);
        return NodeData;
    }

    /**
     * @param
     * @return
     * @Description: TODO(清洗节点字段数据)
     */
    protected static Object shuffleFileds(String key, Object value) {
        if ("skills".equals(key)) {
            return value.toString().replace("\"", "")
                    .replace("'", "")
                    .replace("[", "")
                    .replace("]", "");
        }
        return value;
    }

    /**
     * @param authorityIds:权限ID(一个系统使用者会有多个工程ID)
     * @return
     * @Description: TODO(节点权限过滤)
     */
    protected static JSONObject filterAuthorityNode(JSONObject nodeData, JSONArray authorityIds) {

        // 权限前缀：sysuser_id_
        String prefix = "sysuser_id_";

        // 初始化工程ID并过滤权限
        JSONObject properties = nodeData.getJSONObject("properties");
        JSONArray authorityPrefixIds = new JSONArray();

        if (properties != null) {
            for (Entry entry : properties.entrySet()) {
                String authority = (String) entry.getKey();
                if (authority.contains(prefix)) {
                    authorityPrefixIds.add(authority.replace(prefix, ""));
                }
            }
        }

        if (!authorityPrefixIds.isEmpty()) {
            authorityIds = authorityIds.parallelStream().map(object -> {
                String projectId = (String) object;
                projectId = projectId.replace("-", "");

                boolean bool = authorityPrefixIds.contains(projectId);

                return bool;
            }).distinct().collect(Collectors.toCollection(JSONArray::new));

            // 当前节点无权限字段直接返回
        } else {
            return nodeData;
        }

        if (authorityIds.contains(true)) {
            return nodeData;
        } else {
            return null;
        }
    }

    /**
     * @param
     * @param nodeData
     * @return
     * @Description: TODO(添加统一名称的命名空间 - nameNodeSpace)
     */
    private static JSONObject copyField(JSONObject nodeData, String... fullname) {
        JSONObject properties = nodeData.getJSONObject("properties");

//        if (!properties.containsKey("nameNodeSpace")) {   // 每次覆盖nameNodeSpace-节点的显示名称
//            String nameNodeSpace = properties.getString(fullname);

        String nameNodeSpace = propertyNodeSpaceName(properties, fullname);

        if (nameNodeSpace == null || "".equals(nameNodeSpace)) {
            properties.put("nameNodeSpace", properties.getString("name").split("_")[0]);
        } else {
            int postNameSize = 40;
            if (nameNodeSpace.length() > postNameSize) {
                properties.put("nameNodeSpace", nameNodeSpace.substring(0, postNameSize));
            } else {
                properties.put("nameNodeSpace", nameNodeSpace);
            }
        }
//        }
        return nodeData;
    }

    /**
     * @param
     * @return
     * @Description: TODO(给节点设置一个供于显示的名称)
     */
    private static String propertyNodeSpaceName(JSONObject properties, String[] fullname) {
        for (int i = 0; i < fullname.length; i++) {
            String name = fullname[i];
            String nameNodeSpace = properties.getString(name);
            if (nameNodeSpace != null && !"".equals(nameNodeSpace)) {
                if (!"name".equals(name)) {
                    return nameNodeSpace;
                } else {
                    return nameNodeSpace.split("_")[0];
                }
            }
        }
        return "";
    }

    /**
     * @param
     * @return
     * @Description: TODO(判断是否包含子层级标签集合的标签)
     */
    private static String childLabel(JSONArray childTwoHierarchy, JSONArray array) {
        JSONArray threeHierarchyArray;
        if (childTwoHierarchy != null) {
            threeHierarchyArray = childTwoHierarchy.parallelStream().map(threeHierarchyObject -> {

                // 第三层标签
                JSONObject threeJsonObject = (JSONObject) threeHierarchyObject;
                if (threeJsonObject != null) {
                    String labelName = threeJsonObject.getJSONObject("properties").getString("labelName");
                    if (array.contains(labelName)) {
                        return labelName;
                    }
                }
                return "";
            }).collect(Collectors.toCollection(JSONArray::new));
            threeHierarchyArray.removeIf(v -> v.equals(""));

            if (threeHierarchyArray.size() >= 1) {
                return threeHierarchyArray.getString(0);
            } else {
                return "";
            }
        }
        return "";
    }

    /**
     * @param relationShips
     * @return
     * @Description: TODO(获取关系数组)
     */
    public static JSONArray getRelationShipsArr(Iterable<Relationship> relationShips) {
        JSONArray RelationShipsArr = new JSONArray();
        for (Relationship relationship : relationShips) {
            JSONObject relationShipsObj = Neo4jDataUtils.getRelationShipsObj(relationship);
            RelationShipsArr.add(relationShipsObj);
        }
        return RelationShipsArr;
    }

    /**
     * @param r
     * @return
     * @Description: TODO(获取关系对象)
     */
    public static JSONObject getRelationShipsObj(Relationship r) {
        // 关系对象
        JSONObject relationShipObj = new JSONObject();
        // 关系id
        relationShipObj.put("id", r.id());
        // 关系类型名称
        relationShipObj.put("type", r.type());
        // 关系开始节点
        relationShipObj.put("startNode", r.startNodeId());
        // 关系结束节点
        relationShipObj.put("endNode", r.endNodeId());
        // 关系属性
        // properties
        JSONObject RelationShipProperties = new JSONObject();
        Map<String, Object> mapRelationShipPro = r.asMap();
        for (Entry<String, Object> entry : mapRelationShipPro.entrySet()) {
            RelationShipProperties.put(entry.getKey().toString(), entry.getValue());
        }
        relationShipObj.put("properties", RelationShipProperties);
        return relationShipObj;
    }

    /**
     * @param r
     * @return
     * @Description: TODO(获取关系对象)
     */
    public static JSONObject getRelationShipsObj(Object r) {
        JSONObject jo = JSONObject.parseObject(r.toString());
        // 关系对象
        JSONObject relationShipObj = new JSONObject();
        // 关系id
        relationShipObj.put("id", jo.get("id"));
        // 关系类型名称
        relationShipObj.put("type", jo.get("type"));
        // 关系开始节点
        relationShipObj.put("startNode", jo.get("startId"));
        // 关系结束节点
        relationShipObj.put("endNode", jo.get("endId"));
        // 关系属性
        // properties
        JSONObject RelationShipProperties = new JSONObject();
        Map<String, Object> mapRelationShipPro = jo.getInnerMap();
        for (Entry<String, Object> entry : mapRelationShipPro.entrySet()) {
            if (entry.getKey().toString() != "id" && entry.getKey().toString() != "type" && entry.getKey().toString() != "startId" && entry.getKey().toString() != "endId") {
                RelationShipProperties.put(entry.getKey().toString(), entry.getValue());
            }
        }
        relationShipObj.put("properties", RelationShipProperties);
        return relationShipObj;
    }

    /**
     * @param nodes
     * @return
     * @Description: TODO(获取Nodes节点array)
     */
    public static JSONArray getNodeArr(Iterable<Node> nodes) {
        JSONArray NodesArr = new JSONArray();
        // 遍历节点
        for (Node n : nodes) {
            JSONObject NodeObj = getNodeObj(n);
            NodesArr.add(NodeObj);
        }
        return NodesArr;
    }

    /**
     * @param
     * @return
     * @Description: TODO(Get call result)
     */
    public static JSONArray getCallResult(ResultSet result) {
        JSONArray data = new JSONArray();
        try {
            while (result.next()) {
                String resultString = result.getString(1);
                if (resultString != null) {
                    data.add(resultString);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * @param
     * @return
     * @Description: TODO(Get call result)
     */
    public static JSONArray getCallResultNoRepetition(ResultSet result) {
        JSONArray data = new JSONArray();
        try {
            while (result.next()) {
                String resultString = result.getString(1);
                if (resultString != null) {
                    if (!data.contains(resultString)) {
                        data.add(resultString);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * @param
     * @return
     * @Description: TODO(关系列表)
     */
    public static JSONObject getCurrentNodeRelationships(ResultSet result) {
        JSONObject data = new JSONObject();  //data object
        int total = 0;
        try {
            while (result.next()) {
                String relationshipName = result.getString(1);
                int count = result.getInt(2);
                total += count;
                data.put(relationshipName, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        data.put("total", total);
        return data;
    }

    /**
     * @param
     * @return
     * @Description: TODO(组织维度分解标签列表)
     */
    public static JSONObject getCurrentNodeLabelsRelationships(ResultSet result) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        int total = 0;
        try {
            while (result.next()) {
                ArrayList labels = (ArrayList) result.getObject(1);
                String label = fiterOrgGraphModelLabel(labels);

                String master = result.getString(3).split("_")[0];
                String slave = result.getString(4).split("_")[0];

                array.add(master + "和" + slave + "存在" + label + "关系\n");

                int count = result.getInt(2);
                total += count;
                data.put(label, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        data.put("detail", array);
        data.put("total", total);
        return data;
    }

    /**
     * @param
     * @return
     * @Description: TODO(同级校友碰撞)
     */
    public static JSONObject getSameLevelAlumniCount(ResultSet result, String relationshipType, String featureWord) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        int total = 0;
        try {
            while (result.next()) {
                ArrayList labels = (ArrayList) result.getObject(1);
                String label = fiterOrgGraphModelLabel(labels);

                String master = splitString(result.getString(3), 0);
                String slave = splitString(result.getString(4), 0);
                String startTime = splitString(result.getString(5), 0);
                String school = result.getString(6);

                array.add(master + "和" + slave + "存在" + relationshipType + "关系，他们在" + startTime + "年共同进入" + school + "" + featureWord + "\n");

                int count = result.getInt(2);
                total += count;
                data.put(label, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        data.put("detail", array);
        data.put("total", total);
        return data;
    }

    /**
     * @param
     * @return
     * @Description: TODO(分割字符串通过索引位获取值)
     */
    private static String splitString(String string, int i) {
        if (string != null) {
            String[] array = string.split("_");
            if (array.length >= i) {
                return array[i];
            }
        }
        return null;
    }

    /**
     * @param
     * @return
     * @Description: TODO(交集校友碰撞)
     */
    public static JSONObject getIntersectionAlumniCount(ResultSet result, String relationshipType, String
            featureWord) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        int total = 0;
        try {
            while (result.next()) {
                ArrayList labels = (ArrayList) result.getObject(1);
                String label = fiterOrgGraphModelLabel(labels);

                String master = splitString(result.getString(3), 0);
                String slave = splitString(result.getString(4), 0);

                String r1StartTime = splitString(result.getString(5), 0);
                String r1StopTime = splitString(result.getString(6), 0);
                String r2StartTime = splitString(result.getString(7), 0);
                String r2StopTime = splitString(result.getString(8), 0);

                String school = result.getString(9);

                array.add(master + "和" + slave + "存在" + relationshipType + "关系，" + master + "在" + r1StartTime + "到" + r1StopTime + "期间在" + school + "" + featureWord + "，" +
                        "" + slave + "在" + r2StartTime + "到" + r2StopTime + "期间在" + school + "" + featureWord + "\n");

                int count = result.getInt(2);
                total += count;
                data.put(label, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        data.put("detail", array);
        data.put("total", total);
        return data;
    }

    /**
     * @param
     * @return
     * @Description: TODO(组织图模型标签)
     */
    private static String fiterOrgGraphModelLabel(ArrayList labels) {
        // 认证机构,学校,公司,颁发机构,志愿机构,其它组织
        String[] orgGraphModelLabels = {"认证机构", "学校", "公司", "颁发机构", "志愿机构", "其它组织"};
        for (int i = 0; i < orgGraphModelLabels.length; i++) {
            String orgGraphModelLabel = orgGraphModelLabels[i];
            if (labels.contains(orgGraphModelLabel)) {
                return orgGraphModelLabel;
            }
        }
        // 只包含组织的时候返回其它组织
        if (labels.size() == 1) {
            if ("组织".equals(labels.get(0))) {
                return "其它组织";
            }
        }
        return null;
    }

    public static JSONObject getCurrentNodes(ResultSet result) {
        JSONObject data = new JSONObject();  //data object
        int total = 0;
        try {
            while (result.next()) {
                Array array = result.getArray(1);
                Object object = array.getArray();
                Object[] arrayLabels = (Object[]) object;
                String label = (String) arrayLabels[arrayLabels.length - 1];  // 尽量设置为最底层节点标签
                int count = result.getInt(2);
                total += count;
                if (data.containsKey(label)) {
                    data.put(label, data.getIntValue(label) + count);
                } else {
                    data.put(label, count);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        data.put("total", total);
        return data;
    }

    /**
     * @param
     * @return
     * @Description: TODO(构造暂存NODE对象)
     */
    private static JSONObject nodeMap(JSONObject node, long startNode) {
        JSONObject nodeMap = new JSONObject();
        nodeMap.put("node", node);
        nodeMap.put("start", startNode);
        return nodeMap;
    }

    /**
     * @param
     * @return
     * @Description: TODO(将N层节点放入N - 1层节点的Child)
     */
    private static JSONObject putHierarchy(JSONArray treeArray, long startNode, JSONObject node, int hierarchy, List<
            Long> markList) {
        JSONObject returnObject = new JSONObject();
        if (hierarchy == 2) {
            for (int i = 0; i < treeArray.size(); i++) {
                JSONObject object = treeArray.getJSONObject(i);
                if (object.getLong("id") == startNode) {
                    putChildNodeDefineHierarchy(object, node, markList);
                }
            }
            returnObject.put("array", treeArray);
            returnObject.put("bool", true);
        } else if (hierarchy == 3) {
            for (int i = 0; i < treeArray.size(); i++) {
                JSONObject object = treeArray.getJSONObject(i);
                JSONArray childArray = object.getJSONArray("child");
                if (childArray != null) {
                    for (int j = 0; j < childArray.size(); j++) {
                        JSONObject nodeObject = childArray.getJSONObject(j);
                        if (nodeObject.getLong("id") == startNode) {
                            putChildNodeDefineHierarchy(nodeObject, node, markList);
                        }
                    }
                }
            }
            returnObject.put("array", treeArray);
            returnObject.put("bool", true);
        }
        return returnObject;
    }

    /**
     * @param
     * @return
     * @Description: TODO(构造孩子ARRAY并将已存在的孩子节点放入CHILD ARRAY)
     */
    private static void putChildNodeDefineHierarchy(JSONObject object, JSONObject node, List<Long> markList) {
        JSONArray childArray = object.getJSONArray("child");
        if (childArray == null) {
            JSONArray newChildArray = new JSONArray();
            newChildArray.add(node);
            object.put("child", newChildArray);
            markList.add(node.getLong("id"));
        } else {
            if (!markList.contains(node.getLong("id"))) {
                childArray.add(node);
                markList.add(node.getLong("id"));
            }
        }
    }

    /**
     * @param
     * @param pauseSaveHierarchyNode
     * @return
     * @Description: TODO(处理暂存的2层和3层节点)
     */
    private static void putPauseHierarchyNode(JSONArray treeArray, JSONArray pauseSaveHierarchyNode,
                                              int hierarchy, List<Long> markList) {
        for (int i = 0; i < pauseSaveHierarchyNode.size(); i++) {
            JSONObject object = pauseSaveHierarchyNode.getJSONObject(i);
            putHierarchy(treeArray, object.getLong("start"), object.getJSONObject("node"), hierarchy, markList);
        }
    }

    /**
     * @param
     * @return
     * @Description: TODO(标签拼接)
     */
    public static List<String> jointLabe(ResultSet result) {
        List<String> data = new ArrayList<>();  //data object
        try {
            while (result.next()) {
                Array array = result.getArray(1);
                Object object = array.getArray();

                Object[] arrayLabels = (Object[]) object;
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < arrayLabels.length; i++) {
                    InternalNode internalNode = (InternalNode) arrayLabels[i];
                    Map<String, Object> map = internalNode.asMap();
                    String label = (String) map.get("labelName");

                    builder.append(label + "_");

                }
                data.add(builder.substring(0, builder.length() - 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * @param
     * @return
     * @Description: TODO(获取节点ID)
     */
    public static JSONObject getNodesId(ResultSet result) {
        JSONObject nodeId = new JSONObject();  //节点KEY(name)唯一
        try {
            while (result.next()) {
                String name = result.getString(1);
                long id = result.getLong(2);
                if (name != null) {
                    nodeId.put(name, id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nodeId;
    }

    /**
     * @param
     * @return
     * @Description: TODO(搜索记录解析)
     */
    public static String parseSearchRecodJsonString(ResultSet result) throws Exception {
        while (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    /**
     * @param
     * @return
     * @Description: TODO(获取节点IDS)
     */
    public static JSONArray getNodesIds(ResultSet result) {
        JSONArray ids = new JSONArray();  //节点IDS
        try {
            while (result.next()) {
                long id = result.getLong(1);
                ids.add(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    /**
     * @param
     * @return
     * @Description: TODO(生成子图)
     */
    public static List<Map<String, Long>> createSubGraphNodes(ResultSet result) {
        List<Map<String, Long>> mapList = new ArrayList<>();
        try {
            while (result.next()) {
                long id = result.getLong(1);
                long pathResetNodeAnalyzerId = result.getLong(2);
                Map<String, Long> map = new HashMap<>();
                map.put("id", id);
                map.put("pathResetNodeAnalyzerId", pathResetNodeAnalyzerId);
                mapList.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapList;
    }

    /**
     * @param resultObject:查询结果
     * @param interactiveNetworkAnalyzerCount:排序字段名称
     * @return
     * @Description: TODO(对返回的结果数据使用字段值排序)
     */
    public static JSONObject orderByinteractiveNetworkAnalyzerCount(JSONObject resultObject, String
            interactiveNetworkAnalyzerCount) {
        if (resultObject != null) {
            JSONArray jsonArray = resultObject.getJSONArray("results");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("graph");
            JSONArray nodes = jsonObject2.getJSONArray("nodes");
            nodes = nodes.parallelStream()
                    .sorted((object1, object2) -> {
                        JSONObject nodeOne = (JSONObject) object1;
                        JSONObject nodeTwo = (JSONObject) object2;
                        // 根据传入的节点属性字段值排序
                        JSONObject nodePro1 = nodeOne.getJSONObject("properties");
                        JSONObject nodePro2 = nodeTwo.getJSONObject("properties");
                        Integer int1 = nodePro1.getIntValue(interactiveNetworkAnalyzerCount);
                        Integer int2 = nodePro2.getIntValue(interactiveNetworkAnalyzerCount);
                        return int2 - int1;

                    })
                    .collect(Collectors.toCollection(JSONArray::new));
            jsonObject2.put("nodes", nodes);
        }
        return resultObject;
    }

    /**
     * @param resultObject:结果集合
     * @param key:拿到节点或者关系集合    relationships-获取关系列表 nodes-获取节点列表
     * @return
     * @Description: TODO(从结果集解析NODE列表 - 判断结果集是否NODES为空)
     */
    public static JSONArray getNodeOrRelaList(JSONObject resultObject, String key) {
        if (resultObject != null) {
            JSONObject queryList = resultObject.getJSONArray("queryResultList").getJSONObject(0);
            JSONArray jsonArray = queryList.getJSONArray("results");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("graph");
            return jsonObject2.getJSONArray(key);
        }
        return new JSONArray();
    }

    /**
     * @param resultObject:结果集合
     * @param nodesOrRelas:需要被放回的节点列表或关系列表
     * @param key:拿到节点或者关系集合               relationships-获取关系列表 nodes-获取节点列表
     * @return
     * @Description: TODO(从结果集解析NODE列表 - 判断结果集是否NODES为空)
     */
    public static JSONObject putNodeOrRelaList(JSONObject resultObject, JSONArray nodesOrRelas, String key) {
        if (resultObject != null) {
            JSONObject queryList = resultObject.getJSONArray("queryResultList").getJSONObject(0);
            JSONArray jsonArray = queryList.getJSONArray("results");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("graph");
            jsonObject2.put(key, nodesOrRelas);
            return resultObject;
        }
        return resultObject;
    }

}


