package data.lab.ongdb.search;
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

import data.lab.ongdb.common.*;
import data.lab.ongdb.model.Condition;
import data.lab.ongdb.model.Label;
import data.lab.ongdb.model.RelationshipType;
import data.lab.ongdb.model.Result;
import data.lab.ongdb.search.analyzer.InteractiveNetworkAnalyzer;
import data.lab.ongdb.search.analyzer.NeoAnalyzer;
import data.lab.ongdb.util.JSONTool;
import data.lab.ongdb.util.CypherTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.neo4j.driver.v1.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.search
 * @Description: TODO(NEO4J - 检索分析工具)
 * @date 2019/7/9 9:37
 */
public class NeoSearcher extends NeoAccessor implements Searcher {

    private Logger logger = Logger.getLogger(this.getClass());

    // 请求结果集合
    private JSONArray queryResultList = new JSONArray();

    // 分页起始默认值
    private int skip = DataSize.SIZE_0.getSymbolValue();

    // 分页结束值 - 默认数据量限制
    private int limit = DataSize.SIZE_10.getSymbolValue();

    // ==========================================节点==========================================
    // 是否存在节点检索条件
    private boolean existNodeCondition = false;

    // 节点IDS条件/节点标签条件/节点的属性条件
    private List<Long> nodeIds = new ArrayList<>();
    private List<Label> nodeLabels = new ArrayList<>();
    private List<Object[]> nodeProperties = new ArrayList<>();
    // ==========================================节点==========================================


    // ==========================================关系==========================================
    // 是否存在关系检索条件
    private boolean existRelationCondition = false;
    // 关系IDS条件
    private List<Long> relationIds = new ArrayList<>();
    private List<RelationshipType> relationshipTypes = new ArrayList<>();
    // ==========================================关系==========================================


    // ==========================================PATH==========================================
    // 是否存在PATH检索条件
    private boolean existPathCondition = false;

    // PATH START NODE IDS/LABEL/PROPERTIES
    private List<Long> pathStartNodeIds = new ArrayList<>();
    private List<Label> pathStartNodeLabels = new ArrayList<>();
    private List<Object[]> pathStartNodeProperties = new ArrayList<>();

    // PATH END NODE IDS/LABEL/PROPERTIES
    private List<Long> pathEndNodeIds = new ArrayList<>();
    private List<Label> pathEndNodeLabels = new ArrayList<>();
    private List<Object[]> pathEndNodeProperties = new ArrayList<>();

    // PATH MIDSIDE NODE IDS/LABEL/PROPERTIES
    private List<Long> pathMidSideNodeIds = new ArrayList<>();
    private List<Object[]> pathMidSideNodeLabels = new ArrayList<>();
    private List<Object[]> pathMidSideNodeProperties = new ArrayList<>();
    private List<Object[]> pathMidSideRelationType = new ArrayList<>();

    private List<Object[]> pathEndNodeSorts = new ArrayList<>();

    // PATH LENGTH 路径长度
    private int pathLength = 1;

    // 是否返回虚拟图
    private boolean pathGraphIsVirtual = false;
    private List<Object[]> pathGraphIsVirtualConditions = new ArrayList<>();

    // ==========================================PATH==========================================

    /**
     * @param ipPorts      :服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount  :节点的用户名
     * @param authPassword :节点用户名密码
     * @return
     * @Description: TODO(构造函数)
     */
    public NeoSearcher(String ipPorts, String authAccount, String authPassword) {
        super(ipPorts, authAccount, authPassword);
    }

    /**
     * @param ipPorts      :服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount  :节点的用户名
     * @param authPassword :节点用户名密码
     * @return
     * @Description: TODO(构造函数)
     */
    public NeoSearcher(String ipPorts, String authAccount, String authPassword, Config config) {
        super(ipPorts, authAccount, authPassword, config);
    }

    /**
     * @param accessOccurs:选择用哪种方式与NEO4J进行交互
     * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount:节点的用户名
     * @param authPassword:节点用户名密码
     * @return
     * @Description: TODO(构造函数)
     */
    @Deprecated
    public NeoSearcher(AccessOccurs accessOccurs, String ipPorts, String authAccount, String authPassword) {
        super(accessOccurs, ipPorts, authAccount, authPassword);
    }

//    /**
//     * @param accessOccurs:选择用哪种方式与NEO4J进行交互
//     * @param dbproperties:连接配置
//     * @return
//     * @Description: TODO(构造函数)
//     */
//    @Deprecated
//    public NeoSearcher(AccessOccurs accessOccurs, Dbproperties dbproperties) {
//        super(accessOccurs, dbproperties);
//    }

//    /**
//     * @param accessOccurs:选择用哪种方式与NEO4J进行交互
//     * @param pooledDataSource:连接配置
//     * @return
//     * @Description: TODO(构造函数)
//     */
//    @Deprecated
//    public NeoSearcher(AccessOccurs accessOccurs, ComboPooledDataSource pooledDataSource) {
//        super(accessOccurs, pooledDataSource);
//    }

    /**
     * @param contents     :指定图数据使用什么格式返回，默认GRAPH格式返回
     * @param ipPorts      :服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount  :节点的用户名
     * @param authPassword :节点用户名密码
     * @return
     * @Description: TODO(构造函数)
     */
    @Deprecated
    public NeoSearcher(ResultDataContents contents, String ipPorts, String authAccount, String authPassword) {
        super(contents, ipPorts, authAccount, authPassword);
    }

    /**
     * @param contents     :指定图数据使用什么格式返回，默认GRAPH格式返回
     * @param accessOccurs :选择用哪种方式与NEO4J进行交互
     * @param ipPorts      :服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount  :节点的用户名
     * @param authPassword :节点用户名密码
     * @return
     * @Description: TODO(构造函数)
     */
    @Deprecated
    public NeoSearcher(ResultDataContents contents, AccessOccurs accessOccurs, String ipPorts, String authAccount, String authPassword) {
        super(contents, accessOccurs, ipPorts, authAccount, authPassword);
    }

    /**
     * @param skip :默认0
     * @return
     * @Description: TODO(设置分页开始的数据)
     */
    @Override
    public void setStart(int skip) {
        if (skip <= DataSize.SIZE_20_000.getSymbolValue()) {
            this.skip = skip;
        } else {
            this.skip = skip; // 还是允许但是尽量避免
            if (this.logger.isInfoEnabled()) {
                this.logger.info(new StringBuilder().append("The skip window size too lager,the skip default ")
                        .append(this.skip)
                        .append(",please reset skip size..."));
            }
        }
    }

    /**
     * @param limit :默认20000
     * @return
     * @Description: TODO(设置分页开始的数据)
     */
    @Override
    public void setRow(int limit) {
        if (limit <= DataSize.SIZE_20_000.getSymbolValue()) {
            this.limit = limit;
        } else {
            this.limit = limit; // 还是允许但是尽量避免
            if (this.logger.isInfoEnabled()) {
                this.logger.info(new StringBuilder().append("The limit window size too lager,the limit default ")
                        .append(this.limit)
                        .append(",please reset limit size..."));
            }
        }
    }

    /**
     * @param id :节点ID-NEO4J自动生成的ID
     * @return
     * @Description: TODO(通过节点ID查找节点)
     */
    @Override
    public void addNodeId(long id) {
        this.nodeIds.add(id);
    }

    /**
     * @param label :节点标签
     * @return
     * @Description: TODO(节点标签 - 多次添加标签默认AND检索)
     */
    @Override
    public void addNodeLabel(Label label) {
        this.nodeLabels.add(label);
    }

    /**
     * @param key   :属性KEY
     * @param value :属性VALUE
     * @return
     * @Description: TODO(通过节点属性检索节点)
     */
    @Override
    public void addNodeProperties(String key, Object value) {
        this.nodeProperties.add(new Object[]{key, value});
    }

    /**
     * @param id :关系ID-NEO4J自动生成的ID
     * @return
     * @Description: TODO(通过关系ID查找关系)
     */
    @Override
    public void addRelationId(long id) {
        this.relationIds.add(id);
    }

    /**
     * @param relationshipType :关系类型名
     * @return
     * @Description: TODO(通过关系类型检索关系)
     */
    @Override
    public void addRelationType(RelationshipType relationshipType) {
        this.relationshipTypes.add(relationshipType);
    }

    /**
     * @param key   :属性KEY
     * @param value :属性VALUE
     * @return
     * @Description: TODO(通过关系属性检索关系)
     */
    @Override
    @Deprecated
    public void addRelationProperties(String key, Object value) {

    }

    /**
     * @param id :节点ID
     * @return
     * @Description: TODO(设置路径开始节点ID)
     */
    @Override
    public void addPathStartNodeId(long id) {
        this.pathStartNodeIds.add(id);
    }

    /**
     * @param ids@return
     * @Description: TODO(设置路径开始节点IDS)
     */
    @Override
    public void addPathStartNodeId(long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            long id = ids[i];
            this.pathStartNodeIds.add(id);
        }
    }

    /**
     * @param label@return
     * @Description: TODO(设置路径开始节点标签)
     */
    @Override
    public void addPathStartNodeLabel(Label label) {
        this.pathStartNodeLabels.add(label);
    }

    /**
     * @param key
     * @param value
     * @return
     * @Description: TODO(设置路径开始节点属性)
     */
    @Override
    public void addPathStartNodeProperties(String key, Object value) {
        this.pathStartNodeProperties.add(new Object[]{key, value});
    }

    /**
     * @param id :节点ID
     * @return
     * @Description: TODO(设置路径结束节点ID)
     */
    @Override
    public void addPathEndNodeId(long id) {
        this.pathEndNodeIds.add(id);
    }

    /**
     * @param label@return
     * @Description: TODO(设置路径结束节点标签)
     */
    @Override
    public void addPathEndNodeLabel(Label label) {
        this.pathEndNodeLabels.add(label);
    }

    /**
     * @param key
     * @param value
     * @return
     * @Description: TODO(设置路径结束节点属性 - 属性之间过滤默认是AND)
     */
    @Override
    public void addPathEndNodeProperties(String key, Object value) {
        this.pathEndNodeProperties.add(new Object[]{key, value});
    }

    /**
     * @param id :节点ID
     * @return
     * @Description: TODO(设置路径中间节点)
     */
    @Override
    @Deprecated
    public void addPathMidSideNodeId(long id) {

    }

    /**
     * @param label:设置标签
     * @param labelOccurs:标签出现情况
     * @return
     * @Description: TODO(设置路径中间节点标签)
     */
    @Override
    public void addPathMidSideNodeLabel(Label label, LabelOccurs labelOccurs) {
        this.pathMidSideNodeLabels.add(new Object[]{label, labelOccurs});
    }

    /**
     * @param key
     * @param value
     * @return
     * @Description: TODO(设置路径结束节点属性)
     */
    @Override
    @Deprecated
    public void addPathMidSideNodeProperties(String key, Object value) {

    }

    /**
     * @param relationshipType :关系类型
     * @param relationOccurs   :关系在路径中的出现情况(路径中全部是这种关系/路径中包含这种关系/路径中没有这种关系)
     * @return
     * @Description: TODO(路径扩展的关系设置)
     */
    @Override
    public void addPathMidSideRelationType(RelationshipType relationshipType, RelationOccurs relationOccurs) {
        this.pathMidSideRelationType.add(new Object[]{relationshipType, relationOccurs});
    }

    /**
     * @param length@return
     * @Description: TODO(设置关系路径长度)
     */
    @Override
    public void addPathLength(int length) {
        this.pathLength = length;
    }

    /**
     * @param fieldName :排序字段名称
     * @param sort      :排序方式
     * @return
     * @Description: TODO(根据路径结束节点属性排序)
     */
    @Override
    public void setPathEndNodeSort(String fieldName, SortOrder sort) {
        this.pathEndNodeSorts.add(new Object[]{fieldName, sort});
    }

    /**
     * @param isVirtualGraph:是否返回虚拟图
     * @param startMergeNodeLabel:虚拟图开始的标签类型
     * @param endMergeNodeLabel:虚拟图结束的标签类型
     * @param virtualRelationshipType:生成虚拟图的关系名称
     * @param filterLabel:路径中间被过滤掉的节点标签
     * @return 返回的数据中会将被虚拟的关系路径移除掉
     * @Description: TODO(是否返回虚拟图 - 虚拟图的生成从开始结束类型节点忽略中间所有的关系和路径 ， 直接根据上一次匹配的数据进行生成新的路径)
     * <p>
     * P1=(n:虚拟账号)-[]-(f:发帖)-[]-(m:专题事件)-[]-(h:帖子)
     * <p>
     * addPathGraphIsVirtual(true, new Label[](Label.label("虚拟账号")), new Label[](Label.label("专题事件")))
     * addVirtualP2=(n:虚拟账号)-[]-(m:专题事件)
     */
    @Override
    public void addPathGraphIsVirtual(boolean isVirtualGraph, Label startMergeNodeLabel, Label endMergeNodeLabel,
                                      RelationshipType virtualRelationshipType, Label filterLabel) {

        if (ResultDataContents.D3_GRAPH.equals(super.contents)) {
            this.pathGraphIsVirtual = isVirtualGraph;
        } else {
            logger.info("Just analyze d3 data...", new IllegalArgumentException());
        }
        if (this.pathGraphIsVirtualConditions.isEmpty()) {
            this.pathGraphIsVirtualConditions.add(new Object[]{startMergeNodeLabel, endMergeNodeLabel, virtualRelationshipType, filterLabel});
        } else {
            logger.info("The addPathGraphIsVirtual refused[MAX SET 1],the pathGraphIsVirtualConditions be reset!", new IllegalArgumentException());
            this.pathGraphIsVirtualConditions.clear();
        }
    }

    /**
     * @return
     * @Description: TODO(NEO4J访问对象的全局变量重置方法)
     */
    @Override
    public void reset() {
        // RESET VARIABLE
        this.skip = DataSize.SIZE_0.getSymbolValue();
        this.limit = DataSize.SIZE_10.getSymbolValue();
        this.existNodeCondition = false;
        this.existRelationCondition = false;
        this.existPathCondition = false;
        this.pathLength = 1;
        this.pathGraphIsVirtual = false;

        // RESET OBJECT
        this.nodeIds.clear();
        this.nodeLabels.clear();
        this.nodeProperties.clear();
        this.queryResultList.clear();
        this.relationIds.clear();
        this.pathEndNodeIds.clear();
        this.pathStartNodeIds.clear();
        this.pathStartNodeProperties.clear();
        this.pathMidSideNodeLabels.clear();
        this.relationshipTypes.clear();
        this.pathStartNodeLabels.clear();
        this.pathEndNodeProperties.clear();
        this.pathMidSideRelationType.clear();
        this.pathGraphIsVirtualConditions.clear();

        this.pathEndNodeSorts.clear();
    }

    /**
     * @return
     * @Description: TODO(执行请求 - 拼接请求之后执行)
     */
    @Override
    public JSONObject execute() {

        JSONObject result = null;

        // 检查设置条件
        setExistNodeRelationPathSearchCondition();

        // 检测是否存在多请求的情况，如果存在则拒绝执行
        if (hasMultipleRequestCypher()) {
            this.logger.info("Multiple request are not support,please reset condition!", new IllegalArgumentException());
            return null;
        }

        /**
         * 查询优先级：
         *
         * 节点查询（存在节点查询条件）>关系查询（无节点查询条件存在关系查询条件）>路径查询（无节点/无关系查询条件/存在路径查询条件）
         *
         * **/

        // 查节点
        if (this.existNodeCondition) {
            result = executeQueryNodes();
        }

        // 查关系
        else if (this.existRelationCondition) {
            result = executeQueryRelations();
        }

        // 查询PATH - 节点和关系
        else if (this.existPathCondition) {
            result = executeQueryNodeRelation();
        }

        if (ResultDataContents.D3_GRAPH.equals(super.contents)) {
            result = JSONTool.packD3Json(result);
            if (this.pathGraphIsVirtual && ResultDataContents.D3_GRAPH.equals(super.contents)) {
                result = NeoAnalyzer.loadVirtualGraph(result, this.pathGraphIsVirtualConditions);
            }
        }

        return result;

    }

    private boolean hasMultipleRequestCypher() {
        boolean[] booleans = new boolean[]{this.existNodeCondition, this.existRelationCondition, this.existPathCondition};
        int cypherNum = 0;
        for (int i = 0; i < booleans.length; i++) {
            boolean aBoolean = booleans[i];
            if (aBoolean) {
                cypherNum++;
            }
        }
        if (cypherNum == 1) {
            return false;
        }
        return true;
    }

    private void setExistNodeRelationPathSearchCondition() {
        this.existNodeCondition = isExistNodeCondition();
        this.existRelationCondition = isExistRelationCondition();
        this.existPathCondition = isExistPathCondition();
    }

    /**
     * @param
     * @return
     * @Description: TODO(执行路径检索)
     */
    private JSONObject executeQueryNodeRelation() {

        if (!this.pathEndNodeSorts.isEmpty()) {
            String cypher = "MATCH p=(n)-[*.." + this.pathLength + "]-(m) " +
                    " WHERE id(n) IN " + JSONArray.parseArray(JSON.toJSONString(this.pathStartNodeIds)) + " " +
                    " RETURN p ORDER BY m." + getSortProperty() + " " + getSortType() + " SKIP " + this.skip + " LIMIT " + this.limit + "";
            return addCypherAndExecute(cypher);
        }

        // 路径遍历的中间节点标签是否设置
        if (this.pathMidSideNodeLabels.isEmpty()) {

            // 有开始节点IDS
            if (!this.pathStartNodeIds.isEmpty() && this.pathEndNodeIds.isEmpty() && this.pathEndNodeLabels.isEmpty() && this.pathEndNodeProperties.isEmpty()) {
                String cypher = "MATCH p=(n)-[*.." + this.pathLength + "]-(m) " +
                        " WHERE id(n) IN " + JSONArray.parseArray(JSON.toJSONString(this.pathStartNodeIds)) + " " +
                        " RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "";
                return addCypherAndExecute(cypher);

                // 有开始结束节点IDS
            } else if (!this.pathStartNodeIds.isEmpty() && !this.pathEndNodeIds.isEmpty()) {
                String cypher = "MATCH p=(n)-[*.." + this.pathLength + "]-(m) " +
                        " WHERE id(n) IN " + JSONArray.parseArray(JSON.toJSONString(this.pathStartNodeIds)) + " AND id(m) IN " + JSONArray.parseArray(JSON.toJSONString(this.pathEndNodeIds)) +
                        " RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "";
                return addCypherAndExecute(cypher);

                // 又开始节点IDS，没有结束节点IDS，有结束节点Label
            } else if (!this.pathStartNodeIds.isEmpty() && !this.pathEndNodeLabels.isEmpty() && this.pathEndNodeIds.isEmpty()) {
                String cypher = "MATCH p=(n)-[*.." + this.pathLength + "]-(m" + CypherTool.appendLabels(this.pathEndNodeLabels) + ") " +
                        " WHERE id(n) IN " + JSONArray.parseArray(JSON.toJSONString(this.pathStartNodeIds)) + " " +
                        " RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "";
                return addCypherAndExecute(cypher);

                // 只设置了开始结束节点的标签
            } else if (!this.pathStartNodeLabels.isEmpty() || !this.pathEndNodeLabels.isEmpty()) {
                String cypher = "MATCH p=(n" + CypherTool.appendLabels(this.pathStartNodeLabels) + ")-[*.." + this.pathLength + "]-(m" + CypherTool.appendLabels(this.pathEndNodeLabels) + ") " +
                        " " + setPropertiesCondition("n", this.pathStartNodeProperties, "m", this.pathEndNodeProperties) + " " +
                        " RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "";
                return addCypherAndExecute(cypher);
            }
        } else if (!this.pathMidSideNodeLabels.isEmpty()) {
            return midSideSearchSubGraph();
        }

//        " + setAppendLabels(this.pathEndNodeLabels
//        // 有开始节点结束节点的条件设置
//        if (hasSetPathStartEndNode()) {
//            String cypher = hasSetPathStartEndNodeCypher();
//            return addCypherAndExecute(cypher);
//        }

        return null;
    }

    private String getSortType() {
        return String.valueOf(this.pathEndNodeSorts.get(0)[1]);
    }

    private String getSortProperty() {
        return String.valueOf(this.pathEndNodeSorts.get(0)[0]);
    }

    /**
     * @param
     * @return
     * @Description: TODO(路径遍历的中间节点标签被设置 - 则调用此方法)
     */
    private JSONObject midSideSearchSubGraph() {

        if (this.pathStartNodeIds.isEmpty()) {
            String cypher = "MATCH p=(n" + CypherTool.appendLabels(this.pathStartNodeLabels) + ")-[*.." + this.pathLength + "]-(m) " + setPropertiesCondition("n", pathStartNodeProperties) + " " +
                    "AND casia.filter.pathByNodeLabels(nodes(p)," + setAppendJSONLabels(this.pathMidSideNodeLabels).toJSONString() + ")=true " +
                    "RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "";
            return addCypherAndExecute(cypher);
        } else if (!this.pathStartNodeIds.isEmpty()) {
            String cypher = "MATCH p=(n)-[*.." + this.pathLength + "]-(m) WHERE id(n) IN " + JSONArray.parseArray(JSON.toJSONString(this.pathStartNodeIds)) + " " +
                    "AND casia.filter.pathByNodeLabels(nodes(p)," + setAppendJSONLabels(this.pathMidSideNodeLabels).toJSONString() + ")=true " +
                    "RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "";
            return addCypherAndExecute(cypher);
        }

        return null;
    }

    /**
     * @param
     * @return
     * @Description: TODO(拼接多个标签)
     */
    private JSONArray setAppendJSONLabels(List<Object[]> labels) {
        JSONArray array = new JSONArray();
        labels.forEach(v -> {
            Object[] objects = v;
            Label label = (Label) v[0];
            array.add(label.name());
        });
        return array;
    }

    private String hasSetPathStartEndNodeCypher() {
        if (!this.pathStartNodeIds.isEmpty() && this.pathEndNodeLabels.size() == 1) {

            return new StringBuilder().append("MATCH p=(n)-[*" + this.pathLength + "]-(m:" + this.pathEndNodeLabels.get(0).name() + ") ")
                    .append(setPropertiesCondition("m", this.pathEndNodeProperties))
                    .append("RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "").toString();

        }
        return null;
    }

    /**
     * @param properties:属性键值对列表
     * @return
     * @Description: TODO(WHERE条件 - 拼接属性)
     */
    private String setPropertiesCondition(String variable, List<Object[]> properties) {
        if (!properties.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("WHERE ");
            for (int i = 0; i < properties.size(); i++) {
                Object[] objects = properties.get(i);
                if (objects[1] instanceof String) {
                    builder.append(variable + "." + objects[0] + "='" + objects[1] + "'");
                } else {
                    builder.append(variable + "." + objects[0] + "=" + objects[1] + "");
                }
                builder.append(" AND ");
            }
            return builder.substring(0, builder.length() - 4);
        }
        return "";
    }

    /**
     * @param n_properties:属性键值对列表
     * @return
     * @Description: TODO(WHERE条件 - 拼接属性)
     */
    private String setPropertiesCondition(String n_variable, List<Object[]> n_properties, String m_variable, List<Object[]> m_properties) {
        if (!n_properties.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("WHERE ");
            for (int i = 0; i < n_properties.size(); i++) {
                Object[] objects = n_properties.get(i);
                if (objects[1] instanceof String) {
                    builder.append(n_variable + "." + objects[0] + "='" + objects[1] + "'");
                } else {
                    builder.append(n_variable + "." + objects[0] + "=" + objects[1] + "");
                }
                builder.append(" AND ");
            }
            for (int i = 0; i < m_properties.size(); i++) {
                Object[] objects = m_properties.get(i);
                if (objects[1] instanceof String) {
                    builder.append(m_variable + "." + objects[0] + "='" + objects[1] + "'");
                } else {
                    builder.append(m_variable + "." + objects[0] + "=" + objects[1] + "");
                }
                builder.append(" AND ");
            }
            return builder.substring(0, builder.length() - 4);
        }
        return "";
    }

    /**
     * @param cypher
     * @return
     * @Description: TODO(执行检索CYPHER ， 单个请求 - NODE RELATION PATH EXECUTE)
     */
    private JSONObject addCypherAndExecute(String cypher) {
        long startMill = System.currentTimeMillis();

        Condition condition = new Condition();
        condition.setStatement(cypher, super.contents);

//        this.queryResultList.add(Result.message(JSONObject.parseObject(
//                this.request.httpPost(NeoUrl.DB_DATA_TRANSACTION_COMMIT.getSymbolValue(), condition.toString()))));

        this.queryResultList.add(Result.message(super.chooseSendCypherWay(condition, CRUD.RETRIEVE)));

        JSONObject result = Result.statistics(this.queryResultList);
        // 统计所有请求的耗时，以及每个请求的平均耗时
        long stopMill = System.currentTimeMillis();
        result.put("consume", Result.statisticsConsume(startMill, stopMill, 1, logger));

        if (super.DEBUG) {
            logger.info("Debug condition:" + condition.toString());
        }

        return result;
    }

    /**
     * @param
     * @return
     * @Description: TODO(检查是否存在路径检索条件 - 存在返回TRUE)
     */
    private boolean isExistPathCondition() {
        if (!this.pathStartNodeIds.isEmpty() || !this.pathStartNodeLabels.isEmpty() || !this.pathStartNodeProperties.isEmpty()
                || !this.pathEndNodeIds.isEmpty() || !this.pathEndNodeLabels.isEmpty() || !this.pathEndNodeProperties.isEmpty()
                || !this.pathMidSideNodeIds.isEmpty() || !this.pathMidSideNodeLabels.isEmpty() || !this.pathMidSideNodeProperties.isEmpty()) {
            this.existPathCondition = true;
            return true;
        }
        return false;
    }

    /**
     * @param
     * @return
     * @Description: TODO(检查是否存在关系查询条件)
     */
    private boolean isExistRelationCondition() {
        if (!this.relationIds.isEmpty() || !this.relationshipTypes.isEmpty()) {
            this.existRelationCondition = true;
            return true;
        }
        return false;
    }

    /**
     * @param
     * @return
     * @Description: TODO(检查是否存在节点查询条件)
     */
    private boolean isExistNodeCondition() {
        if (!this.nodeIds.isEmpty() || !this.nodeLabels.isEmpty() || !this.nodeProperties.isEmpty()) {
            this.existNodeCondition = true;
            return true;
        }
        return false;
    }

    /**
     * 有IDS，直接执行IDS查询-添加属性条件；无IDS则使用标签查询-添加属性条件；无标签则用属性查询
     **/
    private JSONObject executeQueryRelations() {

        if (!this.relationIds.isEmpty()) {
            return addCypherAndExecute("MATCH p=()-[r]-() WHERE id(r) IN " + JSONArray.parseArray(JSON.toJSONString(this.relationIds)) + " RETURN p");

        } else if (!this.relationshipTypes.isEmpty()) {
            return addCypherAndExecute("MATCH p=()-[r:" + this.relationshipTypes.get(0).name() + "]-() RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "");
        }
        return null;
    }

    /**
     * 有IDS，直接执行IDS查询；无IDS则使用标签查询-判断是否添加属性条件；无标签无IDS则用属性查询
     **/
    private JSONObject executeQueryNodes() {


        if (!this.nodeIds.isEmpty()) {
            return addCypherAndExecute("MATCH (n) WHERE id(n) IN " + JSONArray.parseArray(JSON.toJSONString(this.nodeIds)) + " RETURN n");

        } else if (!this.nodeLabels.isEmpty()) {
            if (this.nodeProperties.isEmpty()) {
                return addCypherAndExecute("MATCH (n" + CypherTool.appendLabels(this.nodeLabels) + ") RETURN n SKIP " + this.skip + " LIMIT " + this.limit + "");
            } else {
                return addCypherAndExecute("MATCH (n" + CypherTool.appendLabels(this.nodeLabels) + ") " + setPropertiesCondition("n", this.nodeProperties) + " " +
                        "RETURN n SKIP " + skip + " LIMIT " + limit + "");
            }
        } else if (!this.nodeProperties.isEmpty()) {
            return addCypherAndExecute("MATCH (n) " + setPropertiesCondition("n", this.nodeProperties) + " " +
                    "RETURN n SKIP " + skip + " LIMIT " + limit + "");
        }

        return null;
    }


    /**
     * @param targetId:被分析人ID
     * @param countPropertyName:在返回的节点中设置统计属性
     * @param skip
     * @param limit:每次最多分析做少数据
     * @return
     * @Description: TODO(互动网络分析)
     */
    public JSONObject interactiveNetworkAnalyzer(long targetId, String countPropertyName, int skip, int limit) {

        // 找到与之互动的人
        String cypher = "MATCH p=(n)-[*..2]->(post)<--(m) WHERE id(n)=" + targetId + " AND " +
                "zdr.apoc.targetNodesRelasFilter(relationships(p)," + InteractiveNetworkAnalyzer.relationships + ",NULL,NULL)=true" +
                " WITH m AS node,count(*) AS count ORDER BY count DESC SKIP " + skip + " LIMIT " + limit + " SET node." + countPropertyName + "=count WITH node " +
                " MATCH p2=(n2)-[*..2]->(post2)<--(m2) WHERE id(n2)=" + targetId + " AND id(m2)=id(node) AND " +
                "zdr.apoc.targetNodesRelasFilter(relationships(p2)," + InteractiveNetworkAnalyzer.relationships + ",NULL,NULL)=true" +
                " RETURN p2";

        JSONObject result = addCypherAndExecute(cypher);
        if (ResultDataContents.D3_GRAPH.equals(super.contents)) {
            return JSONTool.packD3Json(new InteractiveNetworkAnalyzer().analyzer(result, targetId));
        }
        return new InteractiveNetworkAnalyzer().analyzer(result, targetId);
    }


    /**
     * @param startId:当前事件ID
     * @param targetLabel:账号标签
     * @param countPropertyName:在返回的节点中设置统计属性
     * @param skip
     * @param limit:每次最多分析多少数据
     * @return
     * @Description: TODO(事件分析)
     */
    public JSONObject interactiveHotEventAnalyzer(long startId, Label targetLabel, String countPropertyName, int skip, int limit) {
        String cypher = "MATCH p=(n)-[*..2]-(m:" + targetLabel + ") WHERE id(n)=" + startId + " WITH n,m \n" +
                "MATCH p2=(n)-[*..2]-(m) WITH p2,m,count(p2) AS count SET m." + countPropertyName + "=count RETURN p2 SKIP " + skip + " LIMIT " + limit + "";
        JSONObject result = addCypherAndExecute(cypher);
        if (ResultDataContents.D3_GRAPH.equals(super.contents)) {
            return JSONTool.packD3Json(result);
        }
        return result;
    }

//
//    /**
//     * =================================================================================================================
//     * =================================================================================================================
//     * ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
//     * =================================================================================================================
//     * =================================================================================================================
//     *
//     * 以下是旧版遗留的原始接口建议新版不再使用
//     *
//     * =================================================================================================================
//     * =================================================================================================================
//     * ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
//     * =================================================================================================================
//     * =================================================================================================================
//     **/
//
//    /**
//     * -----------------------------图谱接口方法-----------------------------
//     *
//     * **/
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(扩展度为1的所有节点关系 - 部分节点存在权限过滤和时间过滤)
//     */
//    public String getGraphAllOneDataByName(UserJson userJson) {
//        return controller.getGraphAllOneDataByName(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(通过id获取节点)
//     */
//    public String getNodeById(UserJson userJson) {
//        return controller.getNodeById(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(通过ids获取节点)
//     */
//    public String getNodeByIdsArray(UserJson userJson) {
//        return controller.getNodeByIdsArray(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(图谱搜索 - 搜索问答句处理响应)
//     */
//    public String getZdrAnswer(UserJson userJson) {
//
//        return controller.getZdrAnswer(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(图谱搜索 - 单关键词实时返回节点)
//     */
//    public String getZdrAnswerRealTime(UserJson userJson) {
//        return controller.getZdrAnswerRealTime(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(加载默认支持问句模式)
//     */
//
//    public String loadQuestions(UserJson userJson) {
//
//        return controller.loadQuestions(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(多点模糊 / 多点精确分析接口)
//     */
//    public String multiEntitiesCrossMatch(UserJson userJson) {
//
//        return controller.multiEntitiesCrossMatch(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(两点可达路径的中间节点是否可达查询扩展)
//     */
//
//    public String shortestPathsByMultiNode(UserJson userJson) {
//
//        return controller.shortestPathsByMultiNode(userJson);
//
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(多实体模糊关系查询节点类型统计 - 按照节点在路径上出现的次数统计)
//     */
//
//    public String multiEntitiesFuzzyCrossMatchCount(UserJson userJson) {
//
//        return controller.multiEntitiesFuzzyCrossMatchCount(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(两点之间最短可达路径数量统计接口)
//     */
//
//    public String allShortestPathsCount(UserJson userJson) {
//        return controller.allShortestPathsCount(userJson);
//
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(用户新增节点 - 标签通过统一的标签层级来管理)
//     */
//    public String addNode(Entity entity) {
//        return controller.addNode(entity);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(用户新增关系)
//     */
//
//    public String addRelationship(Trituple trituple) {
//
//        return controller.addRelationship(trituple);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(用户删除自己新增的节点 - 同时删除与节点相关联的所有关系)
//     */
//    public String deleteNode(Entity entity) {
//
//        return controller.deleteNode(entity);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(用户删除自己新增的关系 - 和预处理构建的关系 - 无限制删除)
//     */
//
//    public String deleteRelationship(UserJson userJson) {
//
//        return controller.deleteRelationship(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(用户关系属性更新 - 无限制更新)
//     */
//    public String updateRelationship(UserJson userJson) {
//
//        return controller.updateRelationship(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(基础扩线初始化页面)
//     */
//
//    public String expansionAnalysisBaseInit(UserJson userJson) {
//
//        return controller.expansionAnalysisBaseInit(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(基础扩线初始化页面实体搜索框搜索)
//     */
//
//    public String expansionAnalysisBaseInitNodeSearch(UserJson userJson) {
//
//        return controller.expansionAnalysisBaseInitNodeSearch(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(获取知识图谱关系列表)
//     */
//    public String getRelationshipNameList(UserJson userJson) {
//
//        return controller.getRelationshipNameList(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(获取知识图谱属性列表)
//     */
//
//    public String getPropertyKeyList(UserJson userJson) {
//        return controller.getPropertyKeyList(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(获取知识图谱标签列表)
//     */
//    public String getLabelList(UserJson userJson) {
//
//        return controller.getLabelList(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(获取最子层级标签列表)
//     */
//    public String getLabelChildList(UserJson userJson) {
//        return controller.getLabelChildList(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(通过父标签获取子标签)
//     */
//    public String getLabelsTree(UserJson userJson) {
//
//        return controller.getLabelsTree(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(通过子标签获取父标签)
//     */
//    public String getLabelsTreeFather(UserJson userJson) {
//
//        return controller.getLabelsTreeFather(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(获取知识图谱标签列表) 不设置调用方式默认支持GET和POST
//     */
//
//    public String getHierarchyLabels(UserJson userJson) {
//
//        return controller.getHierarchyLabels(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(定向扩展 - 获取当前节点的可扩展关系列表)
//     */
//    public String getCurrentNodeRelationships(UserJson userJson) {
//
//        return controller.getCurrentNodeRelationships(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(定向扩展 - 根据当前节点特定关系分页扩展节点)
//     */
//
//    public String pagingLoadNodeAndRelationships(UserJson userJson) {
//
//        return controller.pagingLoadNodeAndRelationships(userJson);
//
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(扩展当前节点特定关系下的节点)
//     */
//    public String getNodesByRelation(UserJson userJson) {
//
//        return controller.getNodesByRelation(userJson);
//
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(给节点添加收藏)
//     */
//    public String addNodeFavorite(UserJson userJson) {
//
//        return controller.addNodeFavorite(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(节点收藏夹数据加载 - 初始化加载用户自定义标签数据加载)
//     */
//    public String expansionAnalysisBaseInitFavorite(UserJson userJson) {
//
//        return controller.expansionAnalysisBaseInitFavorite(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(用户自定义领英用户图片URL)
//     */
//    public String userDefinedImageUrl(UserJson userJson) {
//
//        return controller.userDefinedImageUrl(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(用户自定义标签 - 节点关联标签和取消关联标签)
//     */
//    public String addNodeTags(UserJson userJson) {
//
//        return controller.addNodeTags(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(用户自定义标签 - 加载用户自定义标签列表)
//     */
//
//    public String loadUserDefinedLabels(UserJson userJson) {
//
//        return controller.loadUserDefinedLabels(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(图标签管理 - 用户自定义标签列表统计接口)
//     */
//    public String loadUserDefinedLabelsStatistics(UserJson userJson) {
//
//        return controller.loadUserDefinedLabelsStatistics(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(图标签管理 - 单标签下节点分页加载接口)
//     */
//    public String loadUserDefinedLabelNode(UserJson userJson) {
//        return controller.loadUserDefinedLabelNode(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(人员履历 （ 校友 / 同事 ） 时间碰撞 - 组织中人员在时间分析)
//     */
//    public String timeCollisions(UserJson userJson) {
//        return controller.timeCollisions(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(人员履历 （ 校友 / 同事 ） 时间碰撞 - 组织中人员在时间分析统计)
//     */
//    public String timeCollisionsCount(UserJson userJson) {
//        return controller.timeCollisionsCount(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(最近收藏的节点)
//     */
//    public String loadRecentlyFavorite(UserJson userJson) {
//        return controller.loadRecentlyFavorite(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(节点历史搜索记录更新)
//     */
//    public String updateHistorySearchDequeNodes(UserJson userJson) {
//
//        return controller.updateHistorySearchDequeNodes(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(节点历史搜索记录加载)
//     */
//    public String loadHistorySearchDequeNodes(UserJson userJson) {
//
//        return controller.loadHistorySearchDequeNodes(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(初始化列表图模型标签列表统计接口)
//     */
//    public String loadGraphModelLabelsStatistics(UserJson userJson) {
//
//        return controller.loadGraphModelLabelsStatistics(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(个人之间的亲密度计算)
//     */
//    public String intimacyCalculation(UserJson userJson) {
//
//        return controller.intimacyCalculation(userJson);
//    }
//    /**
//     * 1、典型元战法
//     * 2、典型组合战法
//     * 4、自定义战法（对典型战法再次进行逻辑组合）
//     * 3、智能战法
//     */
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(战法接口)
//     */
//    public String graphCombatMethod(UserJson userJson) {
//
//        return controller.graphCombatMethod(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(加载战法类别)
//     */
//
//    public String getGraphCombatType(UserJson userJson) {
//        return controller.getGraphCombatType(userJson);
//    }
//
//    /**
//     * @param userJson:ids为空时分析单人
//     * @return
//     * @Description: TODO(综合智能战法分析 - 分析当前人 / 群体相关的所有战法并且按照战法类别合并返回结果)
//     */
//    public String graphCombatAutoRun(UserJson userJson) {
//
//        return controller.graphCombatAutoRun(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(初始化列表根据类型获取统计数据)
//     */
//    public String accordingToTypeStatistics(UserJson userJson) {
//
//        return controller.accordingToTypeStatistics(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(根据标签获取改标签下节点列表)
//     */
//    public String getNodesForTags(UserJson userJson) {
//
//        return controller.getNodesForTags(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(节点二维扩展)
//     */
//    public String getGraphTwoDimensionNode(UserJson userJson) {
//
//        return controller.getGraphTwoDimensionNode(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(选择展示节点二维扩展)
//     */
//    public String getSelectGraphTwoDimensionNode(UserJson userJson) {
//
//        return controller.getSelectGraphTwoDimensionNode(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(用户导入数据更新图谱)
//     */
//    public String importData(UserJson userJson) {
//        return controller.importData(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(用户修改节点属性)
//     */
//    public String updateNodeProperties(UserJson userJson) {
//
//        return controller.updateNodeProperties(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(节点属性扩展 - 根据节点属性扩展节点 （ 只返回节点 ）)
//     */
//    public String nodePropertiesExtend(UserJson userJson) {
//
//        return controller.nodePropertiesExtend(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(节点属性扩展 - 节点的扩展条件加载)
//     */
//
//    public String nodePropertiesExtendCondition() {
//        return controller.nodePropertiesExtendCondition();
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(高级检索 【 参考 】 http : / / wanfangdata.com.cn)
//     */
//    public String freeTextSearch(UserJson userJson) {
//        return controller.freeTextSearch(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(高级检索 【 参考 】 http : / / wanfangdata.com.cn)
//     */
//    public String freeTextSearchNoLabelJoint(UserJson userJson) {
//        JSONArray properties = userJson.getEntityArray();
//        String cypher = AdvancedSearchInterCypher.freeTextSearchNoLabelJoint(properties);
//        return execute(cypher, CRUD.RETRIEVE).toJSONString();
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(高级检索 【 参考 】 http : / / wanfangdata.com.cn)
//     */
//    public String freeTextSearchNoLabelJoint(UserJson userJson, StoreTimeField start, long startMill, StoreTimeField stop, long stopMill) {
//        JSONArray properties = userJson.getEntityArray();
//        String cypher = AdvancedSearchInterCypher.freeTextSearchNoLabelJoint(properties, start, startMill, stop, stopMill);
//        return execute(cypher, CRUD.RETRIEVE).toJSONString();
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(高级检索 【 参考 】 http : / / wanfangdata.com.cn)
//     */
//    public String universalSearchLabelJoint(UserJson userJson, String timeFilterConditon) {
//        JSONArray properties = userJson.getEntityArray();
//        String cypher = AdvancedSearchInterCypher.universalSearchLabelJoint(properties, timeFilterConditon);
//        return execute(cypher, CRUD.RETRIEVE).toJSONString();
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(高级检索条件加载)
//     */
//
//    public String highRankSearchCondition() {
//        JSONArray array = JSONArray.parseArray(controller.highRankSearchCondition());
//        array.add(0, AdvancedSearchInterCypher.putCondition("节点名称", new String[]{"nameNodeSpace"}, Labels.values()));
//        return array.toJSONString();
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(关系计算计算条件加载)
//     */
//    public String loadCalculateCategory() {
//        return controller.loadCalculateCategory();
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(关系计算 - 快速计算 （ 用户触发式计算接口支持关系溯源 ）)-与图谱战法的区别是关系计算结果进入持久化并用字段标记
//     */
//    public String relationshipCalculate(UserJson userJson) {
//
//        return controller.relationshipCalculate(userJson);
//    }
//
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(列表推荐 - 以人为核心 （ 根据当前人推荐其它人员 ）)
//     * <p>
//     * 1、潜在可能直接认识的人排序列表：六度关系以内人脉网络-（只返回人节点）(用户页面触发式接口)（排序：关系层数）
//     * 2、潜在可能便于业务人员落地和线下工作的人排序列表：六度关系以内人脉-寻找属性包含中文或与中国相关的节点(用户页面触发式接口)(排序：属性中涉及中文内容字符个数)
//     */
//    public String recommendKeyPerson(UserJson userJson) {
//
//        return controller.recommendKeyPerson(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(列表推荐 - 社交联系最紧密的人)
//     */
//    public String recommendKeyPersonCommunication(UserJson userJson) {
//        /**
//         * 遍历出节点-》统计路径-》设置属性-》返回节点
//         *
//         * **/
//
//        // 当前节点ID
//        long startNodeId = userJson.getStartNodeId();
//        int skip = userJson.getSkip();
//        int limit = userJson.getLimit();
//
//        String cypher = "MATCH (n) WHERE id(n)=" + startNodeId + "\n" +
//                "CALL apoc.path.subgraphNodes(n,{maxDepth:2,relationshipFilter:'互动|发出推荐|技能背书人|关注|发帖|点赞|评论|转发|回复|转推|发推',labelFilter:'/新浪微博ID|/LinkedinID|/TwitterID|/FacebookID|/现实人员|/虚拟账号ID|/网易博客ID|/新浪博客ID|/腾讯微博ID|/百度贴吧ID|/天涯论坛ID|/微信公众号ID|/YouTubeID|/InstagramID'}) YIELD node WITH n,node\n" +
//                "CALL apoc.path.expandConfig(n, {maxLevel:2,relationshipFilter:'互动|发出推荐|技能背书人|关注|发帖|点赞|评论|转发|回复|转推|发推',labelFilter:'/新浪微博ID|/LinkedinID|/TwitterID|/FacebookID|/现实人员|/虚拟账号ID|/网易博客ID|/新浪博客ID|/腾讯微博ID|/百度贴吧ID|/天涯论坛ID|/微信公众号ID|/YouTubeID|/InstagramID',terminatorNodes:[node]}) YIELD path WITH n,node,count(path) AS count\n" +
//                "SET node.recommendPersonCommunicationNum=count RETURN node ORDER BY node.recommendPersonCommunicationNum DESC SKIP " + skip + " LIMIT " + limit + ";\n";
//        return DbUtil.exetueCypherJDBC(cypher, null).toJSONString();
////        return controller.recommendKeyPersonCommunication(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(列表推荐 - 好友推荐)
//     */
//    public String recommendKeyFriendPersonCommunication(UserJson userJson) {
//        /**
//         * 遍历出节点-》统计路径-》设置属性-》返回节点
//         *
//         * **/
//
//        // 当前节点ID
//        long startNodeId = userJson.getStartNodeId();
//        int skip = userJson.getSkip();
//        int limit = userJson.getLimit();
//
//        String cypher = "MATCH (m)-[:好友]-(n) WHERE id(m)=" + startNodeId + "\n" +
//                " WITH m AS n,n AS node\n" +
//                "CALL apoc.path.expandConfig(n, {maxLevel:2,relationshipFilter:'互动|发出推荐|技能背书人|关注|发帖|点赞|评论|转发|回复|转推|发推',labelFilter:'/新浪微博ID|/LinkedinID|/TwitterID|/FacebookID|/现实人员|/虚拟账号ID|/网易博客ID|/新浪博客ID|/腾讯微博ID|/百度贴吧ID|/天涯论坛ID|/微信公众号ID|/YouTubeID|/InstagramID',terminatorNodes:[node]}) YIELD path WITH n,node,count(path) AS count\n" +
//                "SET node.recommendPersonCommunicationNum=count RETURN node ORDER BY node.recommendPersonCommunicationNum DESC SKIP " + skip + " LIMIT " + limit + ";\n";
//        return DbUtil.exetueCypherJDBC(cypher, null).toJSONString();
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(三叉戟已有功能保留 - 目标群体中好友关系数统计 ( 每个人与当前人的好友关系数)) - 列表分页
//     */
//    public String targetGroupFriendsRelaCount(UserJson userJson) {
//
//        return controller.targetGroupFriendsRelaCount(userJson);
//    }
//
//    /**
//     * @param
//     * @return 返回现实人/M选民对应的关系数(只包含现实意义上的人，虚拟账号下面的关系合并到人身上)
//     * @Description: TODO(三叉戟已有功能保留 - 目标群体中关系数统计 ( 每个人与当前人的友关系数 ( 现实人隶属的账号下面的关系数包含在现实人)))  - 列表分页
//     */
//    public String targetGroupPersonRelaCount(UserJson userJson) {
//
//        return controller.targetGroupPersonRelaCount(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(三叉戟已有功能保留 - 群体关系呈现)
//     */
//    public String targetPersonRelaDig(UserJson userJson) {
//
//        return controller.targetPersonRelaDig(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(三叉戟已有功能保留 - 与目标群体中存在潜在关系的人物挖掘 - 通过潜在人与目标人群的关系紧密度SCORE排行)
//     */
//    public String targetGroupPersonDigger(UserJson userJson) {
//
//        return controller.targetGroupPersonDigger(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(三叉戟已有功能保留 - 目标人物 （ 及其好友 ） 的共同好友排行)
//     */
//    public String targetPersonPublicFriendSort(UserJson userJson) {
//
//        return controller.targetPersonPublicFriendSort(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(三叉戟已有功能保留 - 目标人物 （ 及其好友 ） 的好友交互次数排行)
//     */
//    public String targetPersonFriendInteractionSort(UserJson userJson) {
//
//        return controller.targetPersonFriendInteractionSort(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(打包当前IDS节点权限下所有的节点和关系)
//     */
//    public String packCurrentAuthorityNodesRela(UserJson userJson) {
//        return controller.packCurrentAuthorityNodesRela(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(账号的互动网络分析)
//     */
//    public String interactiveNetworkAnalyzer(UserJson userJson) {
//        return super.controller.interactiveNetworkAnalyzer(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(互动网络分析之后的结果溯源)
//     */
//    public String interactiveNetworkAnalyzerTraceSource(UserJson userJson) {
//        return controller.interactiveNetworkAnalyzerTraceSource(userJson);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(账号的转发网络分析)
//     */
//    public String interactiveReprintNetworkAnalyzer(UserJson userJson) {
//        // 目标人物ID
//        long targetId = userJson.getId();
//        int skip = userJson.getSkip();
//        int limit = userJson.getLimit();
//        int cutLimit = 2 * limit + skip;
//        String cypher = "MATCH (n) WHERE id(n)=" + targetId + " WITH n \n" +
//                "CALL apoc.path.subgraphNodes(n,{maxDepth:2,relationshipFilter:'隶属虚拟账号|发帖|评论|转发|回复|转评',labelFilter:'-专题事件|/虚拟账号ID|/新浪微博ID|/网易博客ID|/新浪博客ID|/腾讯微博ID|/TwitterID|/百度贴吧ID|/天涯论坛ID|/微信公众号ID|/FacebookID|/LinkedinID|/YouTubeID|/InstagramID',limit:" + cutLimit + "}) YIELD node WITH node,n\n" +
//                "MATCH p=(n)-->(post)<--(node) WHERE zdr.apoc.targetNodesRelasFilter(relationships(p),['隶属虚拟账号','发帖','评论','转发','回复','转评'],NULL,NULL)=true WITH node,count(p) AS count ORDER BY count DESC SKIP " + skip + " LIMIT " + limit + " SET node.interactiveNetworkAnalyzerCount=count RETURN node;\n";
//        JSONObject result = execute(cypher, CRUD.RETRIEVE);
//        return Neo4jDataUtils.orderByinteractiveNetworkAnalyzerCount(result, "interactiveNetworkAnalyzerCount").toJSONString();
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(转发网络分析之后的结果溯源)
//     */
//    public String interactiveReprintNetworkAnalyzerTraceSource(UserJson userJson) {
//        // 目标人物ID
//        long targetId = userJson.getId();
//
//        // 被溯源IDS
//        JSONArray ids = userJson.getIds();
//
//        // 是否溯源
//        boolean tracingSourceBool = userJson.isTracingSourceBool();
//        String cypher = "MATCH (n),(m) WHERE id(n)=" + targetId + " AND id(m) IN " + ids.toJSONString() + " WITH n,m AS node\n" +
//                "MATCH p=(n)-->(post)<--(node) WHERE zdr.apoc.targetNodesRelasFilter(relationships(p),['隶属虚拟账号','发帖','评论','转发','回复','转评'],NULL,NULL)=true RETURN p;";
//        if (!tracingSourceBool) {
//            return execute(cypher, CRUD.RETRIEVE).toJSONString();
//        } else {
//            // 增加解析（直观互动图不包含帖子节点与帖子相连的关系线）
//            InteractiveReprintNetworkAnalyzer networkAnalyzer = new InteractiveReprintNetworkAnalyzer();
//            JSONObject result = execute(cypher, CRUD.RETRIEVE);
//            return networkAnalyzer.analyzer(result, targetId).toJSONString();
//        }
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description: TODO(事件右键 【 事件分析 】 - - 扩展出事件参与度较高的前N个人和事件相关的组织)
//     */
//    public JSONObject eventOrgZdrExpand(long nodeId) {
//        return controller.eventOrgZdrExpand(nodeId);
//    }
}


