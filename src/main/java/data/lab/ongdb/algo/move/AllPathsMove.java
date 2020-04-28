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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.algo
 * @Description: TODO(Two vertex all paths)
 * @date 2019/8/22 19:41
 */
public class AllPathsMove {

    // 最大的路径搜索数量
    private int MAX_FIND_PATH_NUM;

    // 临时保存路径节点的栈
    private Stack<AdjacencyNodeMove> stack = new Stack<>();

    // 存储路径的集合
    private ArrayList<Object[]> allPaths = new ArrayList<>();
    // 存储路径的集合
    private ArrayList<String> allPathsStr = new ArrayList<>();

    private AdjacencyNodeMove[] node;

    public AllPathsMove(int vertexNum) {
        this.node = new AdjacencyNodeMove[vertexNum];
    }

    public ArrayList<Object[]> getAllPaths() {
        return allPaths;
    }

    public ArrayList<String> getAllPathsStr() {
        if (MAX_FIND_PATH_NUM != 0) System.out.println("MAX FIND PATH NUM:" + MAX_FIND_PATH_NUM);
        return allPathsStr;
    }

    /**
     * @param
     * @return
     * @Description: TODO(初始化图邻接表)
     */
    public void initGraphAdjacencyList(int[][] nodeRelations) {
        // 定义节点数组
        for (int i = 0; i < nodeRelations.length; i++) {
            node[i] = new AdjacencyNodeMove();
            node[i].setName(String.valueOf(i));
        }
        // 定义与节点相关联的节点集合
        for (int i = 0; i < nodeRelations.length; i++) {
            ArrayList<AdjacencyNodeMove> List = new ArrayList<>();
            for (int j = 0; j < nodeRelations[i].length; j++) {
                List.add(node[nodeRelations[i][j]]);
            }
            node[i].setRelationNodes(List);
        }
        printInitGraphAdjacencyList();
    }

    /**
     * @param
     * @return
     * @Description: TODO(初始化图邻接表)
     */
    public void initGraphAdjacencyList(AdjacencyNodeMove[] nodes) {
        this.node = nodes;
        printInitGraphAdjacencyList();
    }

    private void printInitGraphAdjacencyList() {
        for (int i = 0; i < node.length; i++) {
            AdjacencyNodeMove nd = node[i];
            System.out.print("Node-Index:" + nd.getName() + " AdjacencyList:");
            ArrayList<AdjacencyNodeMove> nodeArrayList = nd.getRelationNodes();
            System.out.print("[ ");
            for (int j = 0; j < nodeArrayList.size(); j++) {
                AdjacencyNodeMove nd2 = nodeArrayList.get(j);
                System.out.print(nd2.getName() + " ");
            }
            System.out.println("]");
        }
    }

    // 判断节点是否在栈中
    public boolean isNodeInStack(AdjacencyNodeMove node) {
        Iterator<AdjacencyNodeMove> it = stack.iterator();
        while (it.hasNext()) {
            AdjacencyNodeMove node1 = it.next();
            if (node == node1)
                return true;
        }
        return false;
    }

    // 此时栈中的节点组成一条所求路径，转储并打印输出
    public void showAndSavePath() {
        MAX_FIND_PATH_NUM++;
        StringBuilder builder = new StringBuilder();
        Object[] o = stack.toArray();
        for (int i = 0; i < o.length; i++) {
            AdjacencyNodeMove nNode = (AdjacencyNodeMove) o[i];

            if (i < (o.length - 1)) {
                System.out.print(nNode.getName() + "->");
                builder.append(nNode.getName() + "->");
            } else {
                System.out.print(nNode.getName());
                builder.append(nNode.getName());
            }
        }
        // 存储路径
        allPaths.add(o);
        allPathsStr.add(builder.toString());
        System.out.println("\n");
    }

    public void allPaths(int startNodeIndex, int endNodeIndex) {
        AdjacencyNodeMove startNode = getNode(startNodeIndex);
        AdjacencyNodeMove endNode = getNode(endNodeIndex);
        recursionAllPaths(startNode, null, startNode, endNode);
    }

    public void allPaths(int startNodeIndex, int endNodeIndex, int maxFindPathNum) {
        AdjacencyNodeMove startNode = getNode(startNodeIndex);
        AdjacencyNodeMove endNode = getNode(endNodeIndex);
        recursionAllPaths(startNode, null, startNode, endNode, maxFindPathNum);
    }

    private AdjacencyNodeMove getNode(int index) {
        for (int i = 0; i < node.length; i++) {
            if (i == index) return node[i];
        }
        return null;
    }

    /**
     * @param cNode:当前的起始节点currentNode
     * @param pNode:当前起始节点的上一节点previousNode
     * @param sNode:最初的起始节点startNode
     * @param eNode:终点endNode
     * @return Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
     * @Description: TODO(寻找路径的方法)
     */
    private boolean recursionAllPaths(AdjacencyNodeMove cNode, AdjacencyNodeMove pNode, AdjacencyNodeMove sNode, AdjacencyNodeMove eNode) {
        /**
         * 1.整理节点间的关系，为每个节点建立邻接表，该集合中保存所有与该节点直接相连的节点（不包括该节点自身）；
         * 2.定义两点一个为起始节点，另一个为终点，求解两者之间的所有路径的问题可以被分解为如下所述的子问题：
         *   对每一个与起始节点直接相连的节点，求解它到终点的所有路径（路径上不包括起始节点）得到一个路径集合，将这些路径集合相加就可以得到起始节点到终点的所有路径；
         *   依次类推就可以应用递归的思想，层层递归直到终点，若发现希望得到的一条路径，则转储并打印输出；
         *   若发现环路，或发现死路，则停止寻路并返回；  
         * 3.用栈保存当前已经寻到的路径（不是完整路径）上的节点，在每一次寻到完整路径时弹出栈顶节点；
         *   而在遇到从栈顶节点无法继续向下寻路时也弹出该栈顶节点，从而实现回溯。
         *
         * **/
        AdjacencyNodeMove nNode;
        /* 如果符合条件判断说明出现环路，不能再顺着该路径继续寻路，返回false */
        if (cNode != null && pNode != null && cNode == pNode)
            return false;

        if (cNode != null) {
            int i = 0;
            /* 起始节点入栈 */
            stack.push(cNode);
            /* 如果该起始节点就是终点，说明找到一条路径 */
            if (cNode == eNode) {
                /* 转储并打印输出该路径，返回true */
                showAndSavePath();
                return true;
            }
            /* 如果不是,继续寻路 */
            else {
                /*
                 * 从与当前起始节点cNode有连接关系的节点集中按顺序遍历得到一个节点
                 * 作为下一次递归寻路时的起始节点
                 */
                nNode = cNode.getRelationNodes().get(i);
                while (nNode != null) {
                    /*
                     * 如果nNode是最初的起始节点或者nNode就是cNode的上一节点或者nNode已经在栈中 ，
                     * 说明产生环路 ，应重新在与当前起始节点有连接关系的节点集中寻找nNode
                     */
                    if (pNode != null
                            && (nNode == sNode || nNode == pNode || isNodeInStack(nNode))) {
                        i++;
                        if (i >= cNode.getRelationNodes().size())
                            nNode = null;
                        else
                            nNode = cNode.getRelationNodes().get(i);
                        continue;
                    }
                    /* 以nNode为新的起始节点，当前起始节点cNode为上一节点，递归调用寻路方法 */
                    if (recursionAllPaths(nNode, cNode, sNode, eNode))/* 递归调用 */ {
                        /* 如果找到一条路径，则弹出栈顶节点 */
                        stack.pop();
                    }
                    /* 继续在与cNode有连接关系的节点集中测试nNode */
                    i++;
                    if (i >= cNode.getRelationNodes().size())
                        nNode = null;
                    else
                        nNode = cNode.getRelationNodes().get(i);
                }
                /*
                 * 当遍历完所有与cNode有连接关系的节点后，
                 * 说明在以cNode为起始节点到终点的路径已经全部找到
                 */
                stack.pop();
                return false;
            }
        } else
            return false;
    }

    /**
     * @param cNode:当前的起始节点currentNode
     * @param pNode:当前起始节点的上一节点previousNode
     * @param sNode:最初的起始节点startNode
     * @param eNode:终点endNode
     * @param maxFindPathNum:最大的分析路径数量
     * @return Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
     * @Description: TODO(寻找路径的方法)
     */
    private boolean recursionAllPaths(AdjacencyNodeMove cNode, AdjacencyNodeMove pNode, AdjacencyNodeMove sNode, AdjacencyNodeMove eNode, int maxFindPathNum) {
        if (MAX_FIND_PATH_NUM > maxFindPathNum) return false;
        /**
         * 1.整理节点间的关系，为每个节点建立邻接表，该集合中保存所有与该节点直接相连的节点（不包括该节点自身）；
         * 2.定义两点一个为起始节点，另一个为终点，求解两者之间的所有路径的问题可以被分解为如下所述的子问题：
         *   对每一个与起始节点直接相连的节点，求解它到终点的所有路径（路径上不包括起始节点）得到一个路径集合，将这些路径集合相加就可以得到起始节点到终点的所有路径；
         *   依次类推就可以应用递归的思想，层层递归直到终点，若发现希望得到的一条路径，则转储并打印输出；
         *   若发现环路，或发现死路，则停止寻路并返回；  
         * 3.用栈保存当前已经寻到的路径（不是完整路径）上的节点，在每一次寻到完整路径时弹出栈顶节点；
         *   而在遇到从栈顶节点无法继续向下寻路时也弹出该栈顶节点，从而实现回溯。
         *
         * **/
        AdjacencyNodeMove nNode;
        /* 如果符合条件判断说明出现环路，不能再顺着该路径继续寻路，返回false */
        if (cNode != null && pNode != null && cNode == pNode)
            return false;

        if (cNode != null) {
            int i = 0;
            /* 起始节点入栈 */
            stack.push(cNode);
            /* 如果该起始节点就是终点，说明找到一条路径 */
            if (cNode == eNode) {
                /* 转储并打印输出该路径，返回true */
                showAndSavePath();
                return true;
            }
            /* 如果不是,继续寻路 */
            else {
                /*
                 * 从与当前起始节点cNode有连接关系的节点集中按顺序遍历得到一个节点
                 * 作为下一次递归寻路时的起始节点
                 */
                nNode = cNode.getRelationNodes().get(i);
                while (nNode != null) {
                    /*
                     * 如果nNode是最初的起始节点或者nNode就是cNode的上一节点或者nNode已经在栈中 ，
                     * 说明产生环路 ，应重新在与当前起始节点有连接关系的节点集中寻找nNode
                     */
                    if (pNode != null
                            && (nNode == sNode || nNode == pNode || isNodeInStack(nNode))) {
                        i++;
                        if (i >= cNode.getRelationNodes().size())
                            nNode = null;
                        else
                            nNode = cNode.getRelationNodes().get(i);
                        continue;
                    }
                    /* 以nNode为新的起始节点，当前起始节点cNode为上一节点，递归调用寻路方法 */
                    if (recursionAllPaths(nNode, cNode, sNode, eNode, maxFindPathNum))/* 递归调用 */ {
                        /* 如果找到一条路径，则弹出栈顶节点 */
                        stack.pop();
                    }
                    /* 继续在与cNode有连接关系的节点集中测试nNode */
                    i++;
                    if (i >= cNode.getRelationNodes().size())
                        nNode = null;
                    else
                        nNode = cNode.getRelationNodes().get(i);
                }
                /*
                 * 当遍历完所有与cNode有连接关系的节点后，
                 * 说明在以cNode为起始节点到终点的路径已经全部找到
                 */
                stack.pop();
                return false;
            }
        } else
            return false;
    }
}

