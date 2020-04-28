package data.lab.ongdb.util;
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

import data.lab.ongdb.algo.AllPaths;
import data.lab.ongdb.algo.FloydShortestPath;
import data.lab.ongdb.algo.structure.AdjacencyNode;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.util
 * @Description: TODO(图遍历工具)
 * @date 2019/8/19 13:42
 */
public class GraphTraversal {

    // DEFAULT PATH WEIGHT
    private final static int DEFAULT_PATH_WEIGHT = 10;

    // 表示无穷大即不可达
    private final static int MAX_DIST = Integer.MAX_VALUE;

    private static boolean DEBUG = true;

    public static boolean isDEBUG() {
        return DEBUG;
    }

    public static void setDEBUG(boolean DEBUG) {
        GraphTraversal.DEBUG = DEBUG;
    }

    /**
     * @param analysisNodeIds:被分析的节点
     * @param dragNodeIds:路径中必须经过的节点
     * @param result:深度推理的分析结果
     * @return
     * @Description: TODO(Depth reasoning and then drag the node)
     */
    public static JSONObject depthReasoning(long[] analysisNodeIds, long[] dragNodeIds, JSONObject result) {
        JSONArray nodes = JSONTool.getNodeOrRelaList(result, "nodes");
        JSONArray relationships = JSONTool.getNodeOrRelaList(result, "relationships");

        // 使用INDEX替换
        HashMap<Long, Integer> nodeIndex = transferNodeIndex(nodes);

        // 使用INDEX替换关系中节点ID
        JSONArray transferRelations = transferRelations(nodeIndex, relationships);

        // 初始化矩阵并进行最短路径分析（找到两两之间的一条最短路径）
//        if (dragNodeIds.length == 1) {
//            return JSONTool.recountD3NodeRelation(traversal(analysisNodeIds, nodeIndex, dragNodeIds, transferRelations, result));
//        } else {
        return JSONTool.recountD3NodeRelation(traversalAllPaths(analysisNodeIds, nodeIndex, dragNodeIds, transferRelations, result));
//        }
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
        AdjacencyNode[] adjacencyNodes = initAdjacencyMatrix(relationsMatrix);

        AllPaths allPaths = new AllPaths(relationsMatrix.length);
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
        JSONTool.putNodeOrRelaList(result, relationships, "relationships");

        // 过滤节点
        JSONArray nodes = filterNodes(relationships, result);
        JSONTool.putNodeOrRelaList(result, nodes, "nodes");
        return result;
    }

    private static List<String> graphFilterPaths(HashMap<Long, Integer> nodeIndex, long[] dragNodeIds, List<String> graphPaths) {
        List<Integer> dragNodeIndex = dragNodeIdsIndex(nodeIndex, dragNodeIds);
        return graphPaths.parallelStream()
                .filter(v -> {
                    String[] idIndex = v.split("->");
                    for (int i = 0; i < idIndex.length; i++) {
                        String index = idIndex[i];
                        if (dragNodeIndex.contains(Integer.valueOf(index))) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static AdjacencyNode[] initAdjacencyMatrix(int[][] relationsMatrix) {
        AdjacencyNode[] node = new AdjacencyNode[relationsMatrix.length];
        // 定义节点数组
        for (int i = 0; i < relationsMatrix.length; i++) {
            node[i] = new AdjacencyNode();
            node[i].setName(String.valueOf(i));
        }
        // 定义与节点相关联的节点集合
        for (int i = 0; i < relationsMatrix.length; i++) {
            ArrayList<AdjacencyNode> list = new ArrayList<>();
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
     * @param startIndex:开始节点索引值
     * @param endIndex:结束节点索引值
     * @param relationsMatrix:图矩阵
     * @return
     * @Description: TODO(搜索子图两点之间的所有路径)
     */
    private static void subGraphAllPaths(int startIndex, int endIndex, int[][] relationsMatrix) {
        /**
         *       将始点设置为已访问，将其入栈；
         * repeat:A：查看栈顶节点V在图中，有没有可以到达、且没有入栈、且没有从这个节点V出发访问过的节点，
         *              如果有，则将找到的这个节点入栈；
         *              如果没有，则将节点V访问到下一个节点的集合中每个元素赋值为零，V出栈。
         *        B：当栈顶元素为终点时，设置终点没有被访问过，打印栈中元素，弹出栈顶节点
         * until:重复执行A – B,直到栈中元素为空
         * **/

        int vertexNum = relationsMatrix.length;

        // 存储节点上一次访问的记录
        Map<Integer, Integer> nodeVisitMap = new HashMap<>();

        // 存储路径
        Stack<Integer> path = new Stack<>();
        path.push(startIndex);

        while (!path.empty()) {

            // 获取栈顶点
            int topVertexPath = path.peek();
            for (int j = 0; j < vertexNum; j++) {
                if (relationsMatrix[topVertexPath][j] != -1 && relationsMatrix[topVertexPath][j] != MAX_DIST
                        && !path.contains(j) && nodeVisitMap.get(j) != 1) {
                    path.push(j);
                } else {
                    nodeVisitMap.put(j, 0);
                    path.pop();
                }
                if (j == endIndex) {
                    System.out.println("Find path:");
                    path.forEach(v -> System.out.print(v + " "));
                }
            }
        }

    }

    /**
     * @param analysisNodeIds:被分析的节点
     * @param nodeIndex:ID-索引值映射MAP
     * @param relationships:索引值替换ID之后的RELATIONS
     * @return
     * @Description: TODO(初始化矩阵并进行最短路径分析 （ 找到两两之间的一条最短路径 ） - 不能找到所有最短路径)
     */
    private static JSONObject traversal(long[] analysisNodeIds, HashMap<Long, Integer> nodeIndex, long[] dragNodeIds, JSONArray relationships, JSONObject result) {

        int initVertex = nodeIndex.size();

        // 初始化矩阵
        int[][] relationsMatrix = initRelationsMatrix(relationships, initVertex, nodeIndex, dragNodeIds);
        if (DEBUG) {
            System.out.println("Initialize matrix:");
            FloydShortestPath.print(relationsMatrix);
        }
        FloydShortestPath floydShortestPath = new FloydShortestPath(initVertex);
        floydShortestPath.floyd(relationsMatrix);

        if (DEBUG) {
            System.out.println("Distance matrix:");
            FloydShortestPath.print(floydShortestPath.dist);
        }

        if (DEBUG) {
            System.out.println("Path matrix:");
            FloydShortestPath.print(floydShortestPath.path);
        }

        // 两两之间的最短路径寻找(JUST ONE SHORTEST PATH)
        List<int[]> twinVertexList = new ArrayList<>();
        for (int i = 0; i < analysisNodeIds.length; i++) {
            for (int j = i + 1; j < analysisNodeIds.length; j++) {
                int start = nodeIndex.get(analysisNodeIds[i]);
                int end = nodeIndex.get(analysisNodeIds[j]);
                String pathStr = floydShortestPath.findPathToStr(start, end);
                String[] arrayVertex = pathStr.split("->");
                for (int k = 0; k < arrayVertex.length - 1; k++) {
                    twinVertexList.add(new int[]{Integer.parseInt(arrayVertex[k].trim()), Integer.parseInt(arrayVertex[k + 1].trim())});
                }
            }
        }

        // 过滤转换关系
        relationships = filterMapRelations(twinVertexList, nodeIndex, relationships);
        JSONTool.putNodeOrRelaList(result, relationships, "relationships");

        // 过滤节点
        JSONArray nodes = filterNodes(relationships, result);
        JSONTool.putNodeOrRelaList(result, nodes, "nodes");
        return result;
    }

    private static JSONArray filterNodes(JSONArray relationships, JSONObject result) {
        return JSONTool.getNodeOrRelaList(result, "nodes").parallelStream()
                .filter(v -> {
                    JSONObject node = (JSONObject) v;
                    return relationIsContainsNode(node, relationships);
                })
                .collect(Collectors.toCollection(JSONArray::new));
    }

    private static boolean relationIsContainsNode(JSONObject node, JSONArray relationships) {
        return relationships.parallelStream().anyMatch(v -> {
            JSONObject relation = (JSONObject) v;
            long startId = relation.getLongValue("startNode");
            long endId = relation.getLongValue("endNode");
            return node.getLongValue("id") == startId || node.getLongValue("id") == endId;
        });
    }

    private static JSONArray filterMapRelations(List<int[]> twinVertexList, HashMap<Long, Integer> nodeIndex, JSONArray relationships) {
        return relationships.parallelStream()
                .filter(v -> {
                    JSONObject relation = (JSONObject) v;
                    int startIndex = relation.getIntValue("startNode");
                    int endIndex = relation.getIntValue("endNode");
                    return containsIndexPath(startIndex, endIndex, twinVertexList);
                })
                .map(v -> {
                    JSONObject relation = (JSONObject) v;
                    relation.put("startNode", getNodeIdByIndex(relation.getIntValue("startNode"), nodeIndex));
                    relation.put("endNode", getNodeIdByIndex(relation.getIntValue("endNode"), nodeIndex));
                    return relation;
                })
                .collect(Collectors.toCollection(JSONArray::new));
    }

    private static boolean containsIndexPath(int startIndex, int endIndex, List<int[]> twinVertexList) {
        return twinVertexList.parallelStream().anyMatch(v -> (v[0] == startIndex && v[1] == endIndex) || (v[0] == endIndex && v[1] == startIndex));
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
                    matrix[i][j] = FloydShortestPath.MAX;
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

    private static JSONArray transferRelations(HashMap<Long, Integer> nodeIndex, JSONArray relationships) {
        return relationships.parallelStream()
                .map(v -> {
                    JSONObject edge = (JSONObject) v;
                    edge.put("startNode", nodeIndex.get(edge.getLongValue("startNode")));
                    edge.put("endNode", nodeIndex.get(edge.getLongValue("endNode")));
                    return edge;
                })
                .collect(Collectors.toCollection(JSONArray::new));
    }

    /**
     * @param
     * @return map的KEY是节点的ID，value对应节点的索引位
     * @Description: TODO(节点集合使用索引索引位来替换)
     */
    private static HashMap<Long, Integer> transferNodeIndex(JSONArray nodes) {
        HashMap<Long, Integer> nodeIndex = new HashMap<>();

        // NODE ID升序排序
        nodes = nodes.parallelStream().sorted((v1, v2) -> {
            JSONObject object1 = (JSONObject) v1;
            JSONObject object2 = (JSONObject) v2;
            return object1.getInteger("id") - object2.getInteger("id");
        }).collect(Collectors.toCollection(JSONArray::new));

        for (int i = 0; i < nodes.size(); i++) {
            JSONObject node = nodes.getJSONObject(i);
            nodeIndex.put(node.getLongValue("id"), i);
        }

        // PRINT==========================================================
        if (DEBUG) {
            System.out.println("Initialize index map:");
            List<HashMap<String, Object>> list = new ArrayList<>();
            for (Map.Entry entry : nodeIndex.entrySet()) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("NODE-ID", entry.getKey());
                map.put("NODE-INDEX", entry.getValue());
                list.add(map);
            }
            list.parallelStream().sorted(Comparator.comparingInt(v -> Integer.valueOf(String.valueOf(v.get("NODE-INDEX")))))
                    .collect(Collectors.toCollection(ArrayList::new))
                    .forEach(v -> System.out.println("NODE-ID:" + v.get("NODE-ID") + " NODE-INDEX:" + v.get("NODE-INDEX")));
        }
        return nodeIndex;
    }

}


