package data.lab.ongdb.search._plugin;
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
import data.lab.ongdb.search.Property;
import data.lab.ongdb.util.CypherTool;
import com.alibaba.fastjson.JSONArray;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.search._plugin
 * @Description: TODO(图检索遍历插件)
 * @date 2019/8/9 17:19
 */
public class PathPlugin {

    private String pluginCypher;
    private MethodMark methodMark;

    // APOC PATH EXPAND PARA
    private String pathFilter;
    private String labelFilter;
    private long minLevel;
    private long maxLevel;
    private Label label;
    private Property property;
    private boolean isReturnProperties;

    private PathPlugin(MethodMark methodMark) {
        this.pluginCypher = null;
        this.methodMark = methodMark;
    }

    private PathPlugin(String boolPluginCypher) {
        this.pluginCypher = boolPluginCypher;
    }

    public PathPlugin(MethodMark methodMark, String pathFilter, String labelFilter, long minLevel, long maxLevel) {
        this.pluginCypher = null;
        this.methodMark = methodMark;
        this.pathFilter = pathFilter;
        this.labelFilter = labelFilter;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    public PathPlugin(MethodMark methodMark, Label label, Property property, boolean isReturnProperties) {
        this.pluginCypher = null;
        this.methodMark = methodMark;
        this.label = label;
        this.property = property;
        this.isReturnProperties = isReturnProperties;
    }

    private enum MethodMark {
        shortestPath,
        allShortestPaths,
        twinNodePaths,
        twinNodePathsMidNodeCount,
        allNodePointToSameNode,
        twinNodeShortestPath,
        twinNodeAllShortestPaths,
        apocPathExpand,
        twoSiteReprintRelation,

        /**
         * 搜索子图的过程不会遍历所有可能的路径(即节点和边的所有可能序列)，因此在执行效率和成本方面都优于路径扩展过程。适用的场景包括：
         * 寻找节点的k-度邻居(k-nearest neighbours)；
         * 判断节点之间是否连通；
         * 对图进行划分子图的操作。
         *
         * **/
        apocPathSubgraphNodes,
        apocPathSubgraphAll
    }

    public enum BoolOccurs {
        TRUE, FALSE
    }

    /**
     * @param
     * @return
     * @Description: TODO(最短路径插件)
     */
    public static PathPlugin shortestPath() {
        return new PathPlugin(MethodMark.shortestPath);
    }

    /**
     * @param
     * @return
     * @Description: TODO(最短路径插件 - 分析所有最短路径)
     */
    public static PathPlugin allShortestPaths() {
        return new PathPlugin(MethodMark.allShortestPaths);
    }

    /**
     * @param
     * @return
     * @Description: TODO(两两之间关联关系检索 - 两层路径)
     */
    public static PathPlugin twinNodePaths() {
        return new PathPlugin(MethodMark.twinNodePaths);
    }

    /**
     * @param
     * @return
     * @Description: TODO(两两之间关联关系检索 - 两层路径 - 统计关联出的节点类型和数量)
     */
    public static PathPlugin twinNodePathsMidNodeCount() {
        return new PathPlugin(MethodMark.twinNodePathsMidNodeCount);
    }

    /**
     * @param
     * @return
     * @Description: TODO(多点精确的关联关系检索 - 使用多点指同一个节点的精确关系检索插件)
     */
    public static PathPlugin allNodePointToSameNode() {
        return new PathPlugin(MethodMark.allNodePointToSameNode);
    }

    /**
     * @param
     * @return
     * @Description: TODO(多点两两之间的最短路径分析 - shortestPath ( 只分析最短路径))
     */
    public static PathPlugin twinNodeShortestPath() {
        return new PathPlugin(MethodMark.twinNodeShortestPath);
    }

    /**
     * @param
     * @return
     * @Description: TODO(多点两两之间的最短路径分析 - allShortestPaths ( 分析所有最短路径))
     */
    public static PathPlugin twinNodeAllShortestPaths() {
        return new PathPlugin(MethodMark.twinNodeAllShortestPaths);
    }

    /**
     * @param
     * @return
     * @Description: TODO(站点之间的转载关系加载)
     */
    public static PathPlugin twoSiteReprintRelation(Label label, Property property, boolean isReturnProperties) {
        return new PathPlugin(MethodMark.twoSiteReprintRelation, label, property, isReturnProperties);
    }

    /**
     * @param
     * @return
     * @Description: TODO(站点之间的转载关系加载)
     */
    public static PathPlugin twoSiteReprintRelation(Label label, Property property) {
        return new PathPlugin(MethodMark.twoSiteReprintRelation, label, property, false);
    }

    /**
     * @param boolOccurs:包含还是不包含（路径中的节点标签必须全部被labels包含）
     * @return
     * @Description: TODO(路径中的节点必须包含这些标签 （ 路径中一批节点包含下面标签即可 ， 不是所有节点必须是这些标签 ）)
     */
    public static PathPlugin casia_filter_pathByNodeLabels(BoolOccurs boolOccurs, Label... labels) {
        Label[] labelsArray = labels;
        JSONArray array = new JSONArray();
        for (int i = 0; i < labelsArray.length; i++) {
            Label label = labelsArray[i];
            array.add(label.name());
        }
        String boolPlugin = new StringBuilder()
                .append("casia.filter.pathByNodeLabels(")
                .append("nodes(p)")
                .append(",")
                .append(array.toJSONString())
                .append(")=")
                .append(boolOccurs)
                .toString();
        return new PathPlugin(boolPlugin);
    }

    /**
     * ▪ startNode：起始节点，可以是节点的Id或者节点变量
     * ▪ relationshipFilter：遍历关系的过滤条件，用‘|’分隔
     * ▪ labelFilter：遍历节点的过滤条件，用’|’分隔(见下页)
     * ▪ minLevel：最小遍历层级
     * ▪ maxLevel：最大遍历层级
     * ▪ path：返回的路径列表
     * 基本扩展：
     *  CALL apoc.path.expand(startNode,relationshipFilter,labelFilter,minLevel,maxLevel) YIELD path
     * 高级扩展：
     * ▪ startNode 起始节点列表
     * ▪ {configuration}的配置：
     * ▪ minDepth INT 最小遍历层数
     * ▪ maxDepth INT 最大遍历层数 -1不限制
     * ▪ relationshipFilter STRING 关系过滤器
     * ▪ labelFilter STRING 标签过滤器
     * ▪ bfs BOOLEAN true-宽度优先遍历 false-广度优先遍历
     * ▪ uniqueness STRING 唯一性规则
     * ▪ filterStartNode BOOLEAN 是否对起始节点应用过滤规则
     * ▪ limit INT 返回路径的数目上限
     * ▪ optional BOOLEAN false-没找到符合条件的路径，则不返回
     * ▪ endNodes 节点列表 遍历终止节点列表
     * ▪ terminatorNodes 节点列表 终止节点列表
     * ▪ sequence 字符串 配置此项关系与标签过滤规则会被忽略
     * ▪ beginSequenceAtStart 是否对起始节点应用sequence中定义的规则
     * CALL apoc.path.expandConfig(startNode <id>|Node|list, {minLevel,maxLevel,uniqueness,relationshipFilter,labelFilter,uniqueness:'RELATIONSHIP_PATH',bfs:true, filterStartNode:false, limit:-1, optional:false, endNodes:[], terminatorNodes:[], sequence, beginSequenceAtStart:true}) yield path YIELD path
     *
     * @param pathFilter:关系过滤
     * @param labelFilter:标签过滤
     * @param minLevel:最小过滤层级
     * @param maxLevel:最大过滤层级
     * @return
     * @Description: TODO(apoc.path.expand)
     * 节点扩展过程apoc.path.expand()可以从给定节点或节点列表开始，沿着指定的关
     * 系类型进行遍历，直到特定结束条件满足时停止，并返回路径或节点
     *
     * <p>pathFilter:
     * ‘PARENT_OF>|ANSWER’：遍历仅沿着这两个关系进行，其中PARENT_OF是有向的(从Post离开的)，ANSWER是双向的
     *
     * <p>labelFilter:
     * -Post 排除 Post节点不被遍历，也不被包括在返回的路径中。
     * +Post 包含 缺省。Post节点将被遍历，也被包括在返回的路径中。
     * /Post 终止且返回 遍历路径直到遇见Post类型的节点，然后仅返回Post节点。
     * >Post 终止但是继续 遍历路径只返回到达Post类型的节点(含)之前的部分，在Post
     * 节点之后的部分会继续被遍历，但是不会被返回。
     */
    public static PathPlugin apocPathExpand(String pathFilter, String labelFilter, long minLevel, long maxLevel) {
        return new PathPlugin(MethodMark.apocPathExpand, pathFilter, labelFilter, minLevel, maxLevel);
    }

    /**
     * 搜索子图的过程不会遍历所有可能的路径(即节点和边的所有可能序列)，因此在执行效率和成本方面都优于路径扩展过程。
     * CALL apoc.path.subgraphNodes(startNode,{configuration}) YIELD node;
     * CALL apoc.path.subgraphAll(startNode,{configuration}) YIELD nodes, relationships;
     * 参数说明：
     * startNode:节点或节点列表
     * {configuration}的配置：
     * maxDepth INT 最大遍历层数
     * relationshipFilter STRING 关系过滤器
     * labelFilter STRING 标签过滤器
     * bfs BOOLEAN true-宽度优先遍历 false-广度优先遍历
     * filterStartNode BOOLEAN 是否对起始节点应用过滤规则
     * limit INT 返回路径的数目上限
     * optional BOOLEAN false-没找到符合条件的路径，则不返回
     * endNodes 节点列表 遍历终止节点列表
     * terminatorNodes 节点列表 终止节点列表
     * sequence 字符串 配置此项关系与标签过滤规则会被忽略
     * beginSequenceAtStart 是否对起始节点应用sequence中定义的规则
     * {maxDepth:2,relationshipFilter:'隶属虚拟账号|发帖|点赞|评论|转发|回复|互动',labelFilter:'/新浪微博ID',bfs:false,filterStartNode:false,limit:-1,optional:false}
     *
     * PROFILE MATCH (n) WHERE id(n)=5951 WITH n
     * CALL apoc.path.subgraphNodes(n,{maxDepth:2,relationshipFilter:'隶属虚拟账号|发帖|点赞|评论|转发|回复|互动',labelFilter:'/新浪微博ID|/TwitterID',bfs:false,filterStartNode:false,limit:110,optional:false}) YIELD node WITH node,n
     * MATCH p=(n)-->(post)<--(node) WHERE zdr.apoc.targetNodesRelasFilter(relationships(p),['隶属虚拟账号','发帖','点赞','评论','转发','回复','互动'],NULL,NULL)=true WITH node,count(p) AS count ORDER BY count DESC SKIP 100 LIMIT 10 SET node.interactiveNetworkAnalyzerCount=count RETURN node;
     * **/
    public static PathPlugin apocPathSubgraphNodes(String pathFilter, String labelFilter, long minLevel, long maxLevel) {
        return new PathPlugin(MethodMark.apocPathSubgraphNodes, pathFilter, labelFilter, minLevel, maxLevel);

    }

    public String appendPathPlugin(String cypher) {

        if (MethodMark.shortestPath.equals(this.methodMark)) {
            return setShortestPath(cypher);
        } else if (MethodMark.allShortestPaths.equals(this.methodMark)) {
            return setAllShortestPaths(cypher);
        } else if (MethodMark.twinNodePaths.equals(this.methodMark)) {
            return setTwinNodePaths(cypher);
        } else if (MethodMark.twinNodePathsMidNodeCount.equals(this.methodMark)) {
            return setTwinNodePathsMidNodeCount(cypher);
        } else if (MethodMark.allNodePointToSameNode.equals(this.methodMark)) {
            return setAllNodePointToSameNode(cypher);
        } else if (MethodMark.twinNodeShortestPath.equals(this.methodMark)) {
            return setTwinNodeShortestPath(cypher);
        } else if (MethodMark.twinNodeAllShortestPaths.equals(this.methodMark)) {
            return setTwinNodeAllShortestPaths(cypher);
        } else if (MethodMark.apocPathExpand.equals(this.methodMark)) {
            return setApocPathExpand(cypher);
        } else if (MethodMark.twoSiteReprintRelation.equals(this.methodMark)) {
            return setTwoSiteReprintRelation(cypher);
        } else {
            String[] array = cypher.split("return").length <= 0 ? cypher.split("return") :
                    (cypher.split("RETURN").length > 0 ? cypher.split("RETURN") : new String[]{});
            if (array.length > 0) {
                if (cypher.contains("where") || cypher.contains("WHERE")) {
                    return new StringBuilder()
                            .append(array[0])
                            .append(" AND" + pluginCypher + " ")
                            .append(" RETURN ")
                            .append(array.length > 1 ? array[1] : "")
                            .toString();

                } else {
                    return new StringBuilder()
                            .append(array[0])
                            .append(" WHERE " + pluginCypher + " ")
                            .append(" RETURN ")
                            .append(array.length > 1 ? array[1] : "")
                            .toString();
                }
            }
        }

        return new StringBuilder().toString();
    }

    private String setTwoSiteReprintRelation(String cypher) {
        String[] array = cypher.split("RETURN");
        return new StringBuilder()
                .append("MATCH (event:" + this.label.name() + ") WHERE " + CypherTool.appendProperty("event", this.property) + " WITH event ")
                .append(array[0] + " ")
                .append(array[0].contains("WHERE") ? "AND SIZE((event)--(n))>=1 AND SIZE((event)--(m))>=1 " :
                        "WHERE SIZE((event)--(n))>=1 AND SIZE((event)--(m))>=1 ")
                .append(isReturnProperties ? "RETURN n.site_name AS target,m.site_name AS source" + array[1].replace("p","") : "RETURN " + array[1])
                .toString();
    }

    /**
     * MATCH p=(n:`事件`)-[:`相关新闻`]-(m:`新闻`) WHERE n.eid=2 WITH m
     * CALL apoc.path.expand(m,'转载',"+新闻",1,10) YIELD path as paths RETURN paths SKIP 0 LIMIT 1000
     * <p>
     * MATCH p=(n:事件)-[*1]-(m:新闻) RETURN p SKIP 0 LIMIT 100
     **/
    private String setApocPathExpand(String cypher) {
        String[] array = cypher.split("RETURN");
        return new StringBuilder()
                .append(array[0])
                .append("WITH m ")
                .append("CALL apoc.path.expand(m,'" + this.pathFilter + "','" + this.labelFilter + "'," + this.minLevel + "," + this.maxLevel + ") YIELD path as p RETURN ")
                .append(array[1])
                .toString();
    }

    private String setTwinNodeAllShortestPaths(String cypher) {
        String[] array = cypher.split("IN")[1].split("RETURN");
        return new StringBuilder()
                .append("WITH " + array[0] + " AS id_list ")
                .append("MATCH (v) WHERE id(v) IN id_list " +
                        " WITH collect(v) AS nodes " +
                        " UNWIND nodes AS source " +
                        " UNWIND nodes AS target " +
                        " WITH source,target WHERE id(source)<id(target) ")
                .append("MATCH p=allShortestPaths((source)-[*..6]-(target)) ")
                .append("RETURN " + array[1])
                .toString();
    }

    private String setTwinNodeShortestPath(String cypher) {
        String[] array = cypher.split("IN")[1].split("RETURN");
        return new StringBuilder()
                .append("WITH " + array[0] + " AS id_list ")
                .append("MATCH (v) WHERE id(v) IN id_list " +
                        " WITH collect(v) AS nodes " +
                        " UNWIND nodes AS source " +
                        " UNWIND nodes AS target " +
                        " WITH source,target WHERE id(source)<id(target) ")
                .append("MATCH p=shortestPath((source)-[*..6]-(target)) ")
                .append("RETURN " + array[1])
                .toString();
    }

    private String setAllNodePointToSameNode(String cypher) {
        String[] array = cypher.split("IN")[1].split("RETURN");
        String idsStr = array[0];
        JSONArray ids = JSONArray.parseArray(idsStr);

        StringBuilder matchStarNode = new StringBuilder();
        StringBuilder whereStarNode = new StringBuilder();
        StringBuilder matchStarPath = new StringBuilder();
        StringBuilder returnStarPath = new StringBuilder();

        for (int i = 0; i < ids.size(); i++) {
            long id = ids.getLongValue(i);
            matchStarNode.append("(n" + i + "),");
            whereStarNode.append("id(n" + i + ")=" + id + " AND ");
            matchStarPath.append("p" + i + "=(n" + i + ")--(n),");
            returnStarPath.append("p" + i + ",");
        }
        return new StringBuilder()
                .append("MATCH (n) ")
                .append("MATCH " + matchStarNode.substring(0, matchStarNode.length() - 1) + " ")
                .append("WHERE " + whereStarNode.substring(0, whereStarNode.length() - 4) + " ")
                .append("MATCH " + matchStarPath.substring(0, matchStarPath.length() - 1) + " ")
                .append("RETURN  " + returnStarPath.substring(0, returnStarPath.length() - 1) + " ")
                .append("SKIP" + array[1].split("SKIP")[1])
                .toString();
    }

    private String setTwinNodePathsMidNodeCount(String cypher) {
        String[] array = cypher.split("IN")[1].split("RETURN");
        return new StringBuilder()
                .append("WITH " + array[0] + " AS id_list ")
                .append("MATCH (v) WHERE id(v) IN id_list " +
                        " WITH collect(v) AS nodes " +
                        " UNWIND nodes AS source " +
                        " UNWIND nodes AS target " +
                        " WITH source,target WHERE id(source)<id(target) ")
                .append("MATCH p=(source)--(n)--(target) ")
                .append("RETURN labels(n) as label,count(*) as count,id(n) as id")
                .toString();
    }

    private String setTwinNodePaths(String cypher) {
        String[] array = cypher.split("IN")[1].split("RETURN");
        return new StringBuilder()
                .append("WITH " + array[0] + " AS id_list ")
                .append("MATCH (v) WHERE id(v) IN id_list " +
                        " WITH collect(v) AS nodes " +
                        " UNWIND nodes AS source " +
                        " UNWIND nodes AS target " +
                        " WITH source,target WHERE id(source)<id(target) ")
                .append("MATCH p=(source)--(n)--(target) ")
                .append("RETURN " + array[1])
                .toString();
    }

    private String setAllShortestPaths(String cypher) {
        String indexStr = cypher.split("=")[1].split(" ")[0];
        return cypher.replace(indexStr, "allShortestPaths(" + indexStr + ")");
    }

    private String setShortestPath(String cypher) {
        String indexStr = cypher.split("=")[1].split(" ")[0];
        return cypher.replace(indexStr, "shortestPath(" + indexStr + ")");
    }

}

