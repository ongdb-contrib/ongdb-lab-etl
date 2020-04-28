package data.lab.ongdb.search.reprint;
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

import data.lab.ongdb.common.SortOrder;
import data.lab.ongdb.util.JSONTool;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.stream.Collectors;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.search.reprint
 * @Description: TODO(新闻转载网络分析)
 * @date 2019/10/15 10:21
 */
public class Reprint {

    private final static String startAllMark = "all";

    /**
     * @param result:某事件下站点之间的转载网络
     * @param sort:排序方式
     * @return
     * @Description: TODO(被转载站点 - 按照被转载数进行排序)
     */
    public static JSONArray wasRepublished(JSONObject result, SortOrder sort) {
        JSONArray nodes = JSONTool.getNodeOrRelaList(result, "nodes");
        JSONArray relationships = JSONTool.getNodeOrRelaList(result, "relationships");
        return collect(nodes, relationships, "start", sort);
    }

    /**
     * @param result:某事件下站点之间的转载网络
     * @param sort:排序方式
     * @return
     * @Description: TODO(转载站点 - 按照转载数进行排序)
     */
    public static JSONArray republished(JSONObject result, SortOrder sort) {
        JSONArray nodes = JSONTool.getNodeOrRelaList(result, "nodes");
        JSONArray relationships = JSONTool.getNodeOrRelaList(result, "relationships");
        return collect(nodes, relationships, "end", sort);
    }

    /**
     * @param result:某事件下站点之间的转载网络
     * @param sort:排序方式
     * @return
     * @Description: TODO(转载站点 - 按照所有转载数进行排序 - 包括转载和被转载)
     */
    public static JSONArray allRepublished(JSONObject result, SortOrder sort) {
        JSONArray nodes = JSONTool.getNodeOrRelaList(result, "nodes");
        JSONArray relationships = JSONTool.getNodeOrRelaList(result, "relationships");
        return collect(nodes, relationships, startAllMark, sort);
    }

    private static JSONArray collect(JSONArray nodes, JSONArray relationships, String startMark, SortOrder sort) {
        return nodes.parallelStream()
                .map(node -> statisticsRup((JSONObject) node, relationships, startMark))
                .sorted((site1, site2) -> {
                    // reprintCount排序
                    JSONObject nodePro1 = site1.getJSONObject("properties");
                    JSONObject nodePro2 = site2.getJSONObject("properties");
                    int intValue1 = nodePro1.getIntValue("reprintCount");
                    int intValue2 = nodePro2.getIntValue("reprintCount");
                    if (SortOrder.ASC.getSymbolValue().equals(sort.getSymbolValue())) return intValue1 - intValue2;
                    return intValue2 - intValue1;
                })
                .collect(Collectors.toCollection(JSONArray::new));
    }

    /**
     * @param
     * @return
     * @Description: TODO(统计站点的转载或者被转载数)
     */
    private static JSONObject statisticsRup(JSONObject node, JSONArray relationships, String startMark) {
        long nodeId = node.getLongValue("id");
        String uniqueName = node.getJSONObject("properties").getString("_entity_name");
        JSONArray linkRelations = relationships.parallelStream()
                .filter(v -> {
                    JSONObject relation = (JSONObject) v;
                    return relation.getLongValue("startNode") == nodeId || relation.getLongValue("endNode") == nodeId;
                })
                .map(v -> {
                    JSONObject relation = (JSONObject) v;
                    JSONObject properties = relation.getJSONObject("properties");
                    if (properties.containsKey("statistics")) {
                        String statistics = properties.getString("statistics");
                        return JSONArray.parseArray(statistics);
                    } else {
                        return new JSONArray();
                    }
                })
                .filter(v -> isCurrentStatistics(v, uniqueName, startMark))
                .collect(Collectors.toCollection(JSONArray::new));
        int count = linkRelations.parallelStream().map(v -> returnCount((JSONArray) v, uniqueName, startMark))
                .mapToInt(Integer::intValue).sum();
        node.getJSONObject("properties").put("reprintCount", count);
        return node;
    }

    private static int returnCount(JSONArray relation, String uniqueName, String startMark) {
        JSONArray array = currentReprintArray(relation, uniqueName, startMark);
        int intValue1 = getCount(0, array, startMark);
        int intValue2 = 0;
        if (array.size() > 1) {
            intValue2 = getCount(1, array, startMark);
        }
        return intValue1 + intValue2;
    }

    private static int getCount(int i, JSONArray array, String startMark) {
        JSONObject object = array.getJSONObject(i);
//        String start = object.getString("start");
//        String end = object.getString("end");
//        // 被转载和转载统计时，来源站点与发布站点相同，统计时乘以2倍
//        if (startAllMark.equals(startMark) && start.equals(end)) {
//            return object.getIntValue("reprintCount") * 2;
//        } else {
        return object.getIntValue("reprintCount");
//        }
    }

    private static boolean isCurrentStatistics(JSONArray statistics, String uniqueName, String startMark) {
        return !currentReprintArray(statistics, uniqueName, startMark).isEmpty();
    }

    private static JSONArray currentReprintArray(JSONArray relation, String uniqueName, String startMark) {
        return relation.parallelStream()
                .filter(sta -> {
                    JSONObject count = (JSONObject) sta;
                    String start = count.getString(startMark);
                    if (startAllMark.equals(startMark)) return true;
                    return start.equals(uniqueName);
                })
                .collect(Collectors.toCollection(JSONArray::new));
    }

}

