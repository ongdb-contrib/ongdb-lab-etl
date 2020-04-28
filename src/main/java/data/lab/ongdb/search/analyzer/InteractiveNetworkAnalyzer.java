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

import data.lab.ongdb.common.Labels;
import data.lab.ongdb.common.Relationships;
import data.lab.ongdb.util.Neo4jDataUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.zdr.graph.services.graph.interactive
 * @Description: TODO(互动网络)
 * @date 2019/5/15 14:41
 */
public class InteractiveNetworkAnalyzer {

    // 互动类关系 (任意一条路径中关系列表中某一个关系不包含在此列表中则返回FALSE)
    public final static String relationships = "['" + Relationships.隶属虚拟账号 + "','" + Relationships.发帖 + "','" + Relationships.点赞 + "'," +
            "'" + Relationships.评论 + "','" + Relationships.转发 + "','" + Relationships.回复 + "']";

    // 人物类标签
    public final static String lables = "['" + Labels.LinkedinID + "','" + Labels.TwitterID + "','" + Labels.FacebookID + "'," +
            "'" + Labels.现实人员 + "','" + Labels.YouTubeID + "','" + Labels.InstagramID + "','" + Labels.虚拟账号 + "']";

//    // 从RESULT中获取NODES - KEY
//    private final static String nodesFieldName = "nodes";
//
//    // 从RESULT中获取RELATIONSHIPS - KEY
//    private final static String relationshipsFieldName = "relationships";

    // 生成的虚拟关系名称
    private final static String virtualRelaName = "互动";

    // 自增关系ID变量 - 互动网络虚拟关系线ID都为负值
//    private static long relationshipId;

    // 关系线虚拟ID管理LIST
//    private final static CopyOnWriteArrayList<VirtualRelaId> virtualRelaIdManager = VirtualGraphManager.virtualRelaIdManager;

    /**
     * @param
     * @param targetId:被分析的人物ID
     * @param
     * @return
     * @Description: TODO(增加虚拟关系线字段)
     */
    public JSONObject analyzer(JSONObject reslut, long targetId) {

        // 获取节点列表
        JSONArray nodes = Neo4jDataUtils.getNodeOrRelaList(reslut, VirtualGraphManager.nodesFieldName);

        // 过滤节点列表
//        String[] labelSupports = new String[]{Labels.现实人员.toString(), Labels.TwitterID.toString(), Labels.FacebookID.toString(), Labels.YouTubeID.toString(), Labels.InstagramID.toString()};

        JSONArray labelSupportArray = JSONArray.parseArray(lables);

        nodes = filterNodes(nodes, labelSupportArray);

        // 建立虚拟关系
        JSONArray oldRela = Neo4jDataUtils.getNodeOrRelaList(reslut, VirtualGraphManager.relationshipsFieldName);
        JSONArray relationships = buildVirtualRelas(nodes, oldRela, targetId);
        relationships.removeIf(v -> v == null);

        // 添加账号和人的关系（图模型存在的关系ID为正数）
        relationships.addAll(addPersonAccountRela(oldRela));

        // 重新组织RESULT并返回
        reslut = Neo4jDataUtils.putNodeOrRelaList(reslut, nodes, VirtualGraphManager.nodesFieldName);
        reslut = Neo4jDataUtils.putNodeOrRelaList(reslut, relationships, VirtualGraphManager.relationshipsFieldName);
        return reslut;
    }

    /**
     * @param
     * @return
     * @Description: TODO(添加账号和人的关系)
     */
    private JSONArray addPersonAccountRela(JSONArray oldRela) {
        return oldRela.parallelStream()
                .filter(v -> {
                    JSONObject object = (JSONObject) v;
                    String rela = object.getString("type");
                    if (Relationships.隶属虚拟账号.toString().equals(rela)) {
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toCollection(JSONArray::new));
    }

    /**
     * @param nodes:被过滤后的节点
     * @param relationships:原始关系线段
     * @param targetId:被分析的节点
     * @return
     * @Description: TODO(创建互动关系)
     */
    private JSONArray buildVirtualRelas(JSONArray nodes, JSONArray relationships, long targetId) {
        List<Map<String, Object>> targetVirList = getTargetBuildRelaId(nodes, relationships, targetId);
        return nodes.stream()
                .map(v -> {
                    JSONObject node = (JSONObject) v;
                    return buildVirtualRelasThread(node, targetId, targetVirList);
                })
                .collect(Collectors.toCollection(JSONArray::new));
    }

    /**
     * @param node:当前这个需要被建立关系的节点
     * @param targetId:目标账号
     * @param targetVirList:目标账号集合(做为关系的起始节点)
     * @return
     * @Description: TODO(封装关系数据)
     */
    private JSONObject buildVirtualRelasThread(JSONObject node, long targetId, List<Map<String, Object>> targetVirList) {
        long endId = node.getLong("id");
        if (targetVirList.isEmpty()) {
            return VirtualGraphManager.virRelas(targetId, virtualRelaName, endId, null);

        } else {
            JSONArray label = node.getJSONArray("labels");
            // 分账号建立关系（例如：FB账号只能连接FB账号）
            Optional<JSONArray> optional = Optional.of(targetVirList.parallelStream()
                    .filter(v -> {
                        JSONArray oldlabels = (JSONArray) v.get("labels");
                        if (isLabelEquals(oldlabels, label)) {
                            return true;
                        }
                        return false;
                    })
                    .map(v -> {
                        long targetIdNew = Long.valueOf(String.valueOf(v.get("id")));
                        return VirtualGraphManager.virRelas(targetIdNew, virtualRelaName, endId, null);
                    })
                    .collect(Collectors.toCollection(JSONArray::new)));

            if (optional.isPresent()) {
                if (!optional.get().isEmpty()) {
                    return (JSONObject) optional.get().get(0);
                }
            }
            return null;
        }
    }

    /**
     * @param
     * @return
     * @Description: TODO(两个节点是否是同一个平台的账号)
     */
    private boolean isLabelEquals(JSONArray oldlabels, JSONArray label) {
        // 移除两个父标签
        oldlabels.remove(Labels.人.toString());
        oldlabels.remove(Labels.虚拟账号ID.toString());
        label.remove(Labels.人.toString());
        label.remove(Labels.虚拟账号ID.toString());
        for (int i = 0; i < oldlabels.size(); i++) {
            String nodeLabel = oldlabels.getString(i);
            if (label.contains(nodeLabel)) {
                return true;
            }
        }
        return false;
    }

//    private JSONObject virRelas(long targetId, String virRelaName, long endId, JSONObject properties) {
//        if (targetId != endId) {
//            JSONObject pro = new JSONObject();
//            pro.put("name", virRelaName);
//            pro.putAll(properties);
//            JSONObject relationship = new JSONObject();
//            relationship.put("startNode", targetId);
//            relationship.put("id", VirtualGraphManager.relaIdProducer(targetId, endId));
//            relationship.put("type", virRelaName);
//            relationship.put("endNode", endId);
//            relationship.put("properties", properties);
//            return relationship;
//        }
//        return null;
//    }

//    /**
//     * @param
//     * @param targetId
//     * @param endId
//     * @return
//     * @Description: TODO(关系ID生成)
//     */
//    private long relaIdProducer(long targetId, long endId) {
//
//        Optional optional = Optional.ofNullable(getVirtualRelaId(targetId, endId));
//        if (optional.isPresent() && (long) optional.get() != 0) {
//            return (long) optional.get();
//        } else {
//            if (virtualRelaIdManager.size() >= 1000) {
//                virtualRelaIdManager.clear();
//            }
//            virtualRelaIdManager.add(new VirtualRelaId(targetId, endId, -++relationshipId));
//            return -relationshipId;
//        }
//    }

//    /**
//     * @param
//     * @return
//     * @Description: TODO(从虚拟关系ID列表中获取关系ID)
//     */
//    private long getVirtualRelaId(long targetId, long endId) {
//        return virtualRelaIdManager.parallelStream()
//                .filter(v -> v.pathEquals(targetId, endId))
//                .findAny().orElse(new VirtualRelaId()).getRelationshipId();
//    }

    /**
     * @param targetId:被分析的目标节点 - 如果是现实人员则需要返回他的虚拟账号ID
     * @return
     * @Description: TODO(找到当前人的所有虚拟账号)
     */
    private List<Map<String, Object>> getTargetBuildRelaId(JSONArray nodes, JSONArray relationships, long targetId) {

        // 获取所有账号的IDS
        List<Long> listIds = getListIds(relationships, targetId);

        // 封装标签
        return packListIdsLabel(listIds, nodes);
    }

    private List<Map<String, Object>> packListIdsLabel(List<Long> listIds, JSONArray nodes) {
        return nodes.parallelStream()
                .filter(v -> {
                    JSONObject object = (JSONObject) v;
                    long id = object.getLong("id");
                    if (listIds.contains(id)) {
                        return true;
                    }
                    return false;
                })
                .map(v -> {
                    JSONObject object = (JSONObject) v;
                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", object.get("id"));
                    hashMap.put("labels", object.getJSONArray("labels"));
                    return hashMap;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<Long> getListIds(JSONArray relationships, long targetId) {
        return relationships.parallelStream()
                .filter(v -> {
                    JSONObject object = (JSONObject) v;
                    long id = object.getLong("startNode");
                    if (targetId == id) {
                        return true;
                    }
                    return false;
                })
                .map(v -> {
                    JSONObject object = (JSONObject) v;
                    return object.getLong("endNode");
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * @param nodes:原始节点
     * @param labelSupportArray:需要保留的节点中包含的标签(OR)
     * @return
     * @Description: TODO(过滤原始节点 （ 只保留人节点 ） - 并过滤掉自定义标签)
     */
    private JSONArray filterNodes(JSONArray nodes, JSONArray labelSupportArray) {
        return nodes.parallelStream()
                .filter(v -> {
                    JSONObject node = (JSONObject) v;
                    JSONArray lables = node.getJSONArray("labels");
                    if (isLabelSupport(lables, labelSupportArray)) {
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toCollection(JSONArray::new));
    }

    /**
     * @param lables:原始节点标签
     * @param labelSupportArray:被支持的标签
     * @return
     * @Description: TODO(标签判断)
     */
    private boolean isLabelSupport(JSONArray lables, JSONArray labelSupportArray) {
        for (int i = 0; i < lables.size(); i++) {
            String label = lables.getString(i);
            if (labelSupportArray.contains(label)) {
                return true;
            }
        }
        return false;
    }

}

