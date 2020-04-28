package data.lab.ongdb.algo.move;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.util
 * @Description: TODO(图遍历工具)
 * @date 2019/8/19 13:42
 */
public class GraphTraversalMove {

    // DEFAULT PATH WEIGHT
    private final static int DEFAULT_PATH_WEIGHT = 10;

    // 表示无穷大即不可达
    private final static int MAX_DIST = Integer.MAX_VALUE;

    private static boolean DEBUG = true;

    public static boolean isDEBUG() {
        return DEBUG;
    }

    public static void setDEBUG(boolean DEBUG) {
        GraphTraversalMove.DEBUG = DEBUG;
    }

    /**
     * @param analysisNodeIds:被分析的节点
     * @param dragNodeIds:路径中必须经过的节点
     * @param result:深度推理的分析结果
     * @return
     * @Description: TODO(Depth reasoning and then drag the node)
     */
    public static JSONObject depthReasoning(long[] analysisNodeIds, long[] dragNodeIds, JSONObject result) {
        JSONArray nodes = getNodeOrRelaList(result, "nodes");
        JSONArray relationships = getNodeOrRelaList(result, "relationships");

        // 使用INDEX替换
        HashMap<Long, Integer> nodeIndex = transferNodeIndex(nodes);

        // 使用INDEX替换关系中节点ID
        JSONArray transferRelations = transferRelations(nodeIndex, relationships);

        // 初始化矩阵并进行最短路径分析（找到两两之间的一条最短路径）
        return recountD3NodeRelation(traversalAllPaths(analysisNodeIds, nodeIndex, dragNodeIds, transferRelations, result));
    }

    /**
     * @param analysisNodeIds:被分析的节点
     * @param dragNodeIds:路径中必须经过的节点
     * @param result:深度推理的分析结果
     * @param maxFindPathNum:最大的分析路径数量
     * @return
     * @Description: TODO(Depth reasoning and then drag the node)
     */
    public static JSONObject depthReasoning(long[] analysisNodeIds, long[] dragNodeIds, JSONObject result, int maxFindPathNum) {
        JSONArray nodes = getNodeOrRelaList(result, "nodes");
        JSONArray relationships = getNodeOrRelaList(result, "relationships");

        // 使用INDEX替换
        HashMap<Long, Integer> nodeIndex = transferNodeIndex(nodes);

        // 使用INDEX替换关系中节点ID
        JSONArray transferRelations = transferRelations(nodeIndex, relationships);

        // 初始化矩阵并进行最短路径分析（找到两两之间的一条最短路径）
        return recountD3NodeRelation(traversalAllPaths(analysisNodeIds, nodeIndex, dragNodeIds, transferRelations, result, maxFindPathNum));
    }

    private static AdjacencyNodeMove[] initAdjacencyMatrix(int[][] relationsMatrix) {
        AdjacencyNodeMove[] node = new AdjacencyNodeMove[relationsMatrix.length];
        // 定义节点数组
        for (int i = 0; i < relationsMatrix.length; i++) {
            node[i] = new AdjacencyNodeMove();
            node[i].setName(String.valueOf(i));
        }
        // 定义与节点相关联的节点集合
        for (int i = 0; i < relationsMatrix.length; i++) {
            ArrayList<AdjacencyNodeMove> list = new ArrayList<>();
            for (int j = 0; j < relationsMatrix[i].length; j++) {
                int value = relationsMatrix[i][j];
                if (value != -1 && value != MAX_DIST) {
                    list.add(node[j]);
                }
            }
            node[i].setRelationNodes(list);
        }
        return node;
    }

    /**
     * @param analysisNodeIds:被分析的节点
     * @param nodeIndex:ID-索引值映射MAP
     * @param relationships:索引值替换ID之后的RELATIONS
     * @return
     * @Description: TODO(初始化图进行路径分析 - 找到两两之间的所有路径然后进行统一包含过滤)
     */
    private static JSONObject traversalAllPaths(long[] analysisNodeIds, HashMap<Long, Integer> nodeIndex, long[] dragNodeIds, JSONArray relationships, JSONObject result) {

        int initVertex = nodeIndex.size();

        // 初始化矩阵
        int[][] relationsMatrix = initRelationsMatrix(relationships, initVertex, nodeIndex, dragNodeIds);
        AdjacencyNodeMove[] adjacencyNodes = initAdjacencyMatrix(relationsMatrix);

        AllPathsMove allPaths = new AllPathsMove(relationsMatrix.length);
        allPaths.initGraphAdjacencyList(adjacencyNodes);

        // 两两之间的所有短路径寻找(JUST ONE SHORTEST PATH)
        List<String> graphPaths = new ArrayList<>();
        for (int i = 0; i < analysisNodeIds.length; i++) {
            for (int j = i + 1; j < analysisNodeIds.length; j++) {
                int startIndex = nodeIndex.get(analysisNodeIds[i]);
                int endIndex = nodeIndex.get(analysisNodeIds[j]);
                // BEGIN SEARCH SUB-GRAPH ALL PATHS
                if (startIndex != endIndex) {
                    allPaths.allPaths(startIndex, endIndex);
                    graphPaths.addAll(allPaths.getAllPathsStr());
                }
            }
        }
        // 将找到的路径进行过滤
        List<String> graphFilterPaths = graphFilterPaths(nodeIndex, dragNodeIds, graphPaths);

        // 路径转为节点对
        List<int[]> twinVertexList = new ArrayList<>();
        for (int i = 0; i < graphFilterPaths.size(); i++) {
            String pathStr = graphFilterPaths.get(i);
            String[] vertexArray = pathStr.split("->");
            for (int k = 0; k < vertexArray.length - 1; k++) {
                twinVertexList.add(new int[]{Integer.parseInt(vertexArray[k].trim()), Integer.parseInt(vertexArray[k + 1].trim())});
            }
        }

        // 过滤转换关系
        relationships = filterMapRelations(twinVertexList, nodeIndex, relationships);
        putNodeOrRelaList(result, relationships, "relationships");

        // 过滤节点
        JSONArray nodes = filterNodes(relationships, result);
        putNodeOrRelaList(result, nodes, "nodes");
        return result;
    }

    /**
     * @param analysisNodeIds:被分析的节点
     * @param nodeIndex:ID-索引值映射MAP
     * @param relationships:索引值替换ID之后的RELATIONS
     * @return
     * @Description: TODO(初始化图进行路径分析 - 找到两两之间的所有路径然后进行统一包含过滤)
     */
    private static JSONObject traversalAllPaths(long[] analysisNodeIds, HashMap<Long, Integer> nodeIndex, long[] dragNodeIds, JSONArray relationships, JSONObject result, int maxFindPathNum) {

        int initVertex = nodeIndex.size();

        // 初始化矩阵
        int[][] relationsMatrix = initRelationsMatrix(relationships, initVertex, nodeIndex, dragNodeIds);
        AdjacencyNodeMove[] adjacencyNodes = initAdjacencyMatrix(relationsMatrix);

        AllPathsMove allPaths = new AllPathsMove(relationsMatrix.length);
        allPaths.initGraphAdjacencyList(adjacencyNodes);

        // 两两之间的所有短路径寻找(JUST ONE SHORTEST PATH)
        List<String> graphPaths = new ArrayList<>();
        for (int i = 0; i < analysisNodeIds.length; i++) {
            for (int j = i + 1; j < analysisNodeIds.length; j++) {
                int startIndex = nodeIndex.get(analysisNodeIds[i]);
                int endIndex = nodeIndex.get(analysisNodeIds[j]);
                // BEGIN SEARCH SUB-GRAPH ALL PATHS
                if (startIndex != endIndex) {
                    allPaths.allPaths(startIndex, endIndex,maxFindPathNum);
                    graphPaths.addAll(allPaths.getAllPathsStr());
                }
            }
        }
        // 将找到的路径进行过滤
        List<String> graphFilterPaths = graphFilterPaths(nodeIndex, dragNodeIds, graphPaths);

        // 路径转为节点对
        List<int[]> twinVertexList = new ArrayList<>();
        for (int i = 0; i < graphFilterPaths.size(); i++) {
            String pathStr = graphFilterPaths.get(i);
            String[] vertexArray = pathStr.split("->");
            for (int k = 0; k < vertexArray.length - 1; k++) {
                twinVertexList.add(new int[]{Integer.parseInt(vertexArray[k].trim()), Integer.parseInt(vertexArray[k + 1].trim())});
            }
        }

        // 过滤转换关系
        relationships = filterMapRelations(twinVertexList, nodeIndex, relationships);
        putNodeOrRelaList(result, relationships, "relationships");

        // 过滤节点
        JSONArray nodes = filterNodes(relationships, result);
        putNodeOrRelaList(result, nodes, "nodes");
        return result;
    }

    private static JSONArray filterNodes(JSONArray relationships, JSONObject result) {
        JSONArray filterNodes = new JSONArray();
        JSONArray nodes = getNodeOrRelaList(result, "nodes");
        for (int i = 0; i < nodes.size(); i++) {
            JSONObject node = nodes.getJSONObject(i);
            if (relationIsContainsNode(node, relationships)) {
                filterNodes.add(node);
            }
        }
        return filterNodes;
    }

    private static boolean relationIsContainsNode(JSONObject node, JSONArray relationships) {
        for (int i = 0; i < relationships.size(); i++) {
            JSONObject relation = relationships.getJSONObject(i);
            long startId = relation.getLongValue("startNode");
            long endId = relation.getLongValue("endNode");
            if (node.getLongValue("id") == startId || node.getLongValue("id") == endId) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param relationships:本次分析的关系图路径
     * @param initMatrixNum:初始化矩阵大小
     * @param nodeIndex:节点ID与索引值的MAP
     * @param dragNodeIds:拖拽的节点IDS
     * @return
     * @Description: TODO(初始化矩阵图 - 如果设置必经节点则必经节点所关联的路径权重提高)
     */
    private static int[][] initRelationsMatrix(JSONArray relationships, int initMatrixNum, HashMap<Long, Integer> nodeIndex, long[] dragNodeIds) {

        // 拖拽的节点IDS转换为INDEX-VALUE
        List<Integer> dragNodeIdsIndex = dragNodeIdsIndex(nodeIndex, dragNodeIds);

        int[][] matrix = new int[initMatrixNum][initMatrixNum];
        for (int i = 0; i < relationships.size(); i++) {
            JSONObject object = relationships.getJSONObject(i);
            int startNodeIdIndex = object.getIntValue("startNode");
            int endNodeIdIndex = object.getIntValue("endNode");

            // 降低被拖拽节点所连接关系的权重
            if (dragNodeIdsIndex.contains(startNodeIdIndex) || dragNodeIdsIndex.contains(endNodeIdIndex)) {
                matrix[startNodeIdIndex][endNodeIdIndex] = DEFAULT_PATH_WEIGHT - 1;
                matrix[endNodeIdIndex][startNodeIdIndex] = DEFAULT_PATH_WEIGHT - 1;
                // 使用一般默认权重
            } else {
                matrix[startNodeIdIndex][endNodeIdIndex] = DEFAULT_PATH_WEIGHT;
                matrix[endNodeIdIndex][startNodeIdIndex] = DEFAULT_PATH_WEIGHT;
            }
        }
        for (int i = 0; i < initMatrixNum; i++) {
            for (int j = 0; j < initMatrixNum; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][j] = MAX_DIST;
                }
            }
        }
        return matrix;
    }

    /**
     * @param dragNodeIds:拖拽的节点IDS
     * @param nodeIndex:节点ID与索引值的MAP
     * @return
     * @Description: TODO(节点的IDS转换为INDEX - VALUE)
     */
    private static List<Integer> dragNodeIdsIndex(HashMap<Long, Integer> nodeIndex, long[] dragNodeIds) {
        List<Integer> dragNodeIdsIndex = new ArrayList<>();
        for (int i = 0; i < dragNodeIds.length; i++) {
            long dragNodeId = dragNodeIds[i];
            dragNodeIdsIndex.add(nodeIndex.get(dragNodeId));
        }
        return dragNodeIdsIndex;
    }

    private static boolean containsIndexPath(int startIndex, int endIndex, List<int[]> twinVertexList) {
        for (int i = 0; i < twinVertexList.size(); i++) {
            int[] v = twinVertexList.get(i);
            if ((v[0] == startIndex && v[1] == endIndex) || (v[0] == endIndex && v[1] == startIndex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param nodeIndexValue:节点的索引值
     * @param nodeIndex:节点ID与索引值的MAP
     * @return
     * @Description: TODO(通过索引值获取节点ID)
     */
    private static long getNodeIdByIndex(int nodeIndexValue, HashMap<Long, Integer> nodeIndex) {
        for (Map.Entry entry : nodeIndex.entrySet()) {
            long key = Long.parseLong(String.valueOf(entry.getKey()));
            int value = Integer.parseInt(String.valueOf(entry.getValue()));
            if (value == nodeIndexValue) return key;
        }
        return -1;
    }

    private static JSONArray filterMapRelations(List<int[]> twinVertexList, HashMap<Long, Integer> nodeIndex, JSONArray relationships) {
        JSONArray relationshipsFilter = new JSONArray();
        for (int i = 0; i < relationships.size(); i++) {
            JSONObject relation = relationships.getJSONObject(i);
            int startIndex = relation.getIntValue("startNode");
            int endIndex = relation.getIntValue("endNode");
            if (containsIndexPath(startIndex, endIndex, twinVertexList)) {
                relation.put("startNode", getNodeIdByIndex(relation.getIntValue("startNode"), nodeIndex));
                relation.put("endNode", getNodeIdByIndex(relation.getIntValue("endNode"), nodeIndex));
                relationshipsFilter.add(relation);
            }
        }
        return relationshipsFilter;
    }

    private static List<String> graphFilterPaths(HashMap<Long, Integer> nodeIndex, long[] dragNodeIds, List<String> graphPaths) {
        List<Integer> dragNodeIndex = dragNodeIdsIndex(nodeIndex, dragNodeIds);
        List<String> graphFilterPaths = new ArrayList<>();
        for (int i = 0; i < graphPaths.size(); i++) {
            String s = graphPaths.get(i);
            String[] idIndex = s.split("->");
            if (containsPath(idIndex, dragNodeIndex)) graphFilterPaths.add(s);
        }
        return graphFilterPaths;
    }

    private static boolean containsPath(String[] idIndex, List<Integer> dragNodeIndex) {
        for (int j = 0; j < idIndex.length; j++) {
            String index = idIndex[j];
            if (dragNodeIndex.contains(Integer.valueOf(index))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param
     * @return map的KEY是节点的ID，value对应节点的索引位
     * @Description: TODO(节点集合使用索引索引位来替换)
     */
    private static HashMap<Long, Integer> transferNodeIndex(JSONArray nodes) {
        HashMap<Long, Integer> nodeIndex = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            JSONObject node = nodes.getJSONObject(i);
            nodeIndex.put(node.getLongValue("id"), i);
        }
        return nodeIndex;
    }

    private static JSONArray transferRelations(HashMap<Long, Integer> nodeIndex, JSONArray relationships) {
        for (int i = 0; i < relationships.size(); i++) {
            JSONObject edge = relationships.getJSONObject(i);
            edge.put("startNode", nodeIndex.get(edge.getLongValue("startNode")));
            edge.put("endNode", nodeIndex.get(edge.getLongValue("endNode")));
        }
        return relationships;
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

    /**
     * @param resultObject:结果集合
     * @param nodesOrRelas:需要被放回的节点列表或关系列表
     * @param key:拿到节点或者关系集合               relationships-获取关系列表 nodes-获取节点列表
     * @return
     * @Description: TODO(从结果集解析NODE列表 - 判断结果集是否NODES为空)
     */
    public static JSONObject putNodeOrRelaList(JSONObject resultObject, JSONArray nodesOrRelas, String key) {
        if (resultObject != null) {
            JSONArray jsonArray = resultObject.getJSONArray("results");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("graph");
            jsonObject2.put(key, nodesOrRelas);
            return resultObject;
        }
        return resultObject;
    }

    /**
     * @param
     * @return
     * @Description: TODO(重新统计节点和关系数量)
     */
    public static JSONObject recountD3NodeRelation(JSONObject traversal) {
        traversal.put("totalNodeSize", getNodeOrRelaList(traversal, "nodes").size());
        traversal.put("totalRelationSize", getNodeOrRelaList(traversal, "relationships").size());
        return traversal;
    }

}
