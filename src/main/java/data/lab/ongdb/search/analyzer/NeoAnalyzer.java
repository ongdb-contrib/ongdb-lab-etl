package data.lab.ongdb.search.analyzer;
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

import data.lab.ongdb.model.Label;
import data.lab.ongdb.model.RelationshipType;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.search
 * @Description: TODO(图分析工具)
 * @date 2019/7/9 9:39
 */
public class NeoAnalyzer {

    private static Logger logger = Logger.getLogger(NeoAnalyzer.class);

    /**
     * @param result:D3格式的数据
     * @param pathGraphIsVirtualConditions:添加的生成虚拟图的条件
     * @return
     * @Description: TODO(分析虚拟图)
     */
    public static JSONObject loadVirtualGraph(JSONObject result, List<Object[]> pathGraphIsVirtualConditions) {
        if (pathGraphIsVirtualConditions.size() == 1) {
            try {
                Object[] objects = pathGraphIsVirtualConditions.get(0);
                Label startMergeNodeLabel = (Label) objects[0];
                Label endMergeNodeLabel = (Label) objects[1];
                RelationshipType virtualRelationshipType = (RelationshipType) objects[2];
                Label filterLabel = (Label) objects[3];

                return loadVirtualGraph(result, startMergeNodeLabel, endMergeNodeLabel, virtualRelationshipType, filterLabel);

            } catch (Exception e) {
                logger.info("Load virtual graph exception...", new IllegalArgumentException());
                e.printStackTrace();
            }
        } else {
            logger.info("Load virtual graph exception...", new IllegalArgumentException());
        }
        return result;
    }

    /**
     * @param result:原始结果数据
     * @param startMergeNodeLabel:开始类型节点标签
     * @param endMergeNodeLabel:结束类型节点标签
     * @param virtualRelationshipType:虚拟关系类型
     * @return
     * @Description: TODO(根据D3格式数据生成虚拟图)
     */
    public static JSONObject loadVirtualGraph(JSONObject result, Label startMergeNodeLabel, Label endMergeNodeLabel,
                                               RelationshipType virtualRelationshipType, Label filterLabel) {

        JSONArray results = result.getJSONArray("results");
        if (!result.isEmpty()) {
            JSONArray data = results.getJSONObject(0).getJSONArray("data");
            JSONObject graph = data.getJSONObject(0).getJSONObject("graph");

            JSONArray relationships = graph.getJSONArray("relationships");
            JSONArray nodes = graph.getJSONArray("nodes");


            // 通过开始类型找到结束类型，添加新关系，然后移除中间所有关系和节点
            if (!nodes.isEmpty()) {
                JSONObject virtualGraph = virtualNodeRelation(relationships, nodes, startMergeNodeLabel, endMergeNodeLabel,
                        virtualRelationshipType, filterLabel);
                data.getJSONObject(0).put("graph", virtualGraph);
            }
        }
        return result;
    }

    private static JSONObject virtualNodeRelation(JSONArray relationships, JSONArray nodes, Label startMergeNodeLabel, Label endMergeNodeLabel,
                                                  RelationshipType virtualRelationshipType, Label filterLabel) {

        // 过滤节点
        List<Long> filterNodeIds = new ArrayList<>();
        nodes = nodes.stream().filter(v -> {
            JSONObject object = (JSONObject) v;
            if (!object.getJSONArray("labels").contains(filterLabel.name())) {
                return true;
            } else {
                filterNodeIds.add(object.getLongValue("id"));
            }
            return false;
        }).collect(Collectors.toCollection(JSONArray::new));

        // 过滤关系
        relationships = relationships.stream().filter(v -> {
            JSONObject object = (JSONObject) v;
            long startId = object.getLongValue("startNode");
            long stopId = object.getLongValue("endNode");
            if (!filterNodeIds.contains(startId) && !filterNodeIds.contains(stopId)) {
                return true;
            }
            return false;
        }).collect(Collectors.toCollection(JSONArray::new));

        // 新增虚拟关系

        // 被指向的节点
        JSONObject endNode = nodes.stream().filter(v -> {
            JSONObject object = (JSONObject) v;
            return object.getJSONArray("labels").contains(endMergeNodeLabel.name());
        }).collect(Collectors.toCollection(JSONArray::new)).getJSONObject(0);

        // 需要连接到被指向节点的节点列表
        JSONArray startNode = nodes.stream().filter(v -> {
            JSONObject object = (JSONObject) v;
            return object.getJSONArray("labels").contains(startMergeNodeLabel.name());
        }).collect(Collectors.toCollection(JSONArray::new));

        JSONArray newRelation = startNode.stream().map(v -> {
            JSONObject object = (JSONObject) v;
            JSONObject relation = new JSONObject();

            long targetId = object.getLongValue("id");
            long endId = endNode.getLongValue("id");

            relation.put("startNode", String.valueOf(targetId));
            relation.put("id", String.valueOf(VirtualGraphManager.relaIdProducer(targetId, endId, virtualRelationshipType.name())));
            relation.put("type", virtualRelationshipType.name());
            relation.put("endNode", String.valueOf(endId));
            JSONObject pro = new JSONObject();
            pro.put("name", virtualRelationshipType.name());
            relation.put("properties", pro);

            return relation;
        }).collect(Collectors.toCollection(JSONArray::new));

        relationships.addAll(newRelation);

        JSONObject graph = new JSONObject();
        graph.put("relationships", relationships);
        graph.put("nodes", nodes);
        return graph;


//        for (int i = 0; i < nodes.size(); i++) {
//            JSONObject node = nodes.getJSONObject(i);
//            if (isStartNode(node, startMergeNodeLabel)) {
//
//                // 找到要和它连接的节点
//                JSONArray midsideNodes = getMidsideNodes(relationships, nodes, node.getLongValue("id"));
//
////                // 寻找直连的节点
////                for (int j = 0; j < nodes.size(); j++) {
////                    JSONObject node = nodes.getJSONObject(j);
////
////                    // 通过直连节点，找到
////
////                }
//            }
//        }
//        return null;
    }

    private static JSONArray getMidsideNodes(JSONArray relationships, JSONArray nodeList, long id) {
        JSONArray nodes = new JSONArray();
        relationships.forEach(v -> {
            JSONObject object = (JSONObject) v;
            long startId = object.getLongValue("startNode");
            long stopId = object.getLongValue("endNode");
            if (id == startId || id == stopId) {
                nodes.add(packNode(nodeList, object, id));
            }
        });
        return nodes;
    }

    private static JSONObject packNode(JSONArray nodeList, JSONObject object, long id) {
        long tempIdStart = object.getLongValue("startNode");
        long tempIdStop = object.getLongValue("endNode");
        long temp;
        if (tempIdStart == id) {
            temp = tempIdStop;
        } else {
            temp = tempIdStart;
        }
        return nodeList.stream().filter(v -> {
            JSONObject node = (JSONObject) v;
            long nodeId = node.getLongValue("id");
            if (nodeId == temp) {
                return true;
            }
            return false;
        }).collect(Collectors.toCollection(JSONArray::new)).getJSONObject(0);
    }

    private static boolean isStartNode(JSONObject node, Label label) {
        return node.getJSONArray("labels").contains(label.name());
    }


}


