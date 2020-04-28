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

import data.lab.ongdb.common.DataSize;
import data.lab.ongdb.model.Label;
import data.lab.ongdb.model.RelationshipType;
import data.lab.ongdb.search._plugin.PathPlugin;
import data.lab.ongdb.util.CypherTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.search
 * @Description: TODO(PATH SEEK)
 * @date 2019/7/26 15:57
 */
public class PathSeeker {

    private Logger logger = Logger.getLogger(this.getClass());
    private int skip = DataSize.SIZE_0.getSymbolValue();
    private int limit = DataSize.SIZE_10.getSymbolValue();
    private String cypher;

    private List<Long> startNodeIds = new ArrayList<>();
    private List<Long> endNodeIds = new ArrayList<>();

    private List<Label> startNodeLabel = new ArrayList<>();
    private List<Label> endNodeLabel = new ArrayList<>();

    // PATH LENGTH 路径长度
    private int pathLength = 1;

    private int fixPathLength = 1;

    private List<RelationshipType> relationshipTypes = new ArrayList<>();

    private Property startNodeProperty;
    private String startNodePropertyDSL;

    private Property endNodeProperty;
    private String endNodePropertyDSL;

    private String startNodeFullTextSearch;
    private String startNodeFullTextSearchScore;

    // 时间盒插件
    private RunTimeBoxed runTimeBoxed;

    // 路径插件
    private PathPlugin pathPlugin;

    public PathSeeker setStartNodeId(long id) {
        this.startNodeIds.add(id);
        return this;
    }

    public PathSeeker setStartNodeId(long... id) {
        long[] longs = id;
        Long[] lons = new Long[longs.length];
        for (int i = 0; i < longs.length; i++) {
            lons[i] = longs[i];
        }
        this.startNodeIds.addAll(Arrays.asList(lons));
        return this;
    }

    public PathSeeker setStartNodeId(JSONArray ids) {
        for (int i = 0; i < ids.size(); i++) {
            this.startNodeIds.add(ids.getLongValue(i));
        }
        return this;
    }

    public PathSeeker setEndNodeId(long id) {
        this.endNodeIds.add(id);
        return this;
    }

    public PathSeeker setEndNodeId(long... id) {
        long[] longs = id;
        Long[] lons = new Long[longs.length];
        for (int i = 0; i < longs.length; i++) {
            lons[i] = longs[i];
        }
        this.endNodeIds.addAll(Arrays.asList(lons));
        return this;
    }

    public PathSeeker setStartNodeLabel(Label label) {
        this.startNodeLabel.add(label);
        return this;
    }

    public PathSeeker setStartNodeLabel(Label... label) {
        this.startNodeLabel.addAll(Arrays.asList(label));
        return this;
    }

    public PathSeeker setEndNodeLabel(Label label) {
        this.endNodeLabel.add(label);
        return this;
    }

    public PathSeeker setEndNodeLabel(Label... label) {
        this.endNodeLabel.addAll(Arrays.asList(label));
        return this;
    }

    public PathSeeker setStartNodeProperties(Property properties) {
        this.startNodeProperty = properties;
        return this;
    }

    public PathSeeker setStartNodeProperties(String propertiesDSL) {
        this.startNodePropertyDSL = propertiesDSL;
        return this;
    }

    public PathSeeker setEndNodeProperties(Property properties) {
        this.endNodeProperty = properties;
        return this;
    }

    public PathSeeker setEndNodeProperties(String propertiesDSL) {
        this.endNodePropertyDSL = propertiesDSL;
        return this;
    }


    public String toQuery() {

        // 优先使用全文检索
        if (this.startNodeFullTextSearch != null) {
            this.cypher = new StringBuilder()
                    .append(this.startNodeFullTextSearch)
                    .append(" MATCH p=(node)-[" + addPathLength() + "]-() RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                    .toString();

        } else if (this.startNodeFullTextSearchScore != null) {
            this.cypher = new StringBuilder()
                    .append(this.startNodeFullTextSearchScore)
                    .append(" MATCH p=(node)-[" + addPathLength() + "]-() RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                    .toString();

        } else if (!startNodeIds.isEmpty() && !endNodeIds.isEmpty()) {
            this.cypher = new StringBuilder().
                    append("MATCH p=(n)-[" + addPathLength() + "]-(m)  " +
                            "WHERE id(n) IN " + JSONArray.parseArray(JSON.toJSONString(this.startNodeIds)) + " AND id(m) IN " + JSONArray.parseArray(JSON.toJSONString(this.endNodeIds)) + " " +
                            "RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                    .toString();
        } else if (!startNodeIds.isEmpty() && endNodeIds.isEmpty()) {

            if (endNodeLabel.isEmpty()) {
                this.cypher = new StringBuilder().
                        append("MATCH p=(n)-[" + addPathLength() + "]-(m)  " +
                                "WHERE id(n) IN " + JSONArray.parseArray(JSON.toJSONString(this.startNodeIds)) + " RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                        .toString();
            } else {
                this.cypher = new StringBuilder().
                        append("MATCH p=(n)-[" + addPathLength() + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ")  " +
                                "WHERE id(n) IN " + JSONArray.parseArray(JSON.toJSONString(this.startNodeIds)) + " RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                        .toString();
            }

        } else if (startNodeIds.isEmpty() && !endNodeIds.isEmpty()) {
            this.cypher = new StringBuilder().
                    append("MATCH p=(n)-[" + addPathLength() + "]-(m)  " +
                            "WHERE id(m) IN " + JSONArray.parseArray(JSON.toJSONString(this.endNodeIds)) + " " +
                            "RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                    .toString();
        } else if (!startNodeLabel.isEmpty() && !endNodeLabel.isEmpty()) {
            if (startNodeProperty != null) {
                this.cypher = new StringBuilder().
                        append("MATCH p=(n" + CypherTool.appendLabels(this.startNodeLabel) + ")-[" + addPathLength() + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") WHERE "+CypherTool.appendProperty("n",startNodeProperty)+" RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                        .toString();
            } else if (startEndProperties()) {
                this.cypher = new StringBuilder().
                        append("MATCH p=(n" + CypherTool.appendLabels(this.startNodeLabel) + ")-[" + addPathLength() + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                        .toString();
            }

        } else if (!startNodeLabel.isEmpty() && endNodeLabel.isEmpty()) {

            if (startProperties()) {
                this.cypher = new StringBuilder().
                        append("MATCH p=(n" + CypherTool.appendLabels(this.startNodeLabel) + ")-[" + addPathLength() + "]-(m) RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                        .toString();
            }

        } else if (startNodeLabel.isEmpty() && !endNodeLabel.isEmpty()) {

            this.cypher = new StringBuilder().
                    append("MATCH p=(n)-[" + addPathLength() + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                    .toString();

        } else {
            this.cypher = new StringBuilder()
                    .append("MATCH p=(n)-[" + addPathLength() + "]-(m) RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                    .toString();
        }

        // 设置路径插件
        if (pathPlugin != null) this.cypher = pathPlugin.appendPathPlugin(this.cypher);

        // 设置时间盒执行CYPHER
        if (runTimeBoxed != null) this.cypher = runTimeBoxed.getRunTimeBoxedCypher(this.cypher);

        return reset();
    }

    private boolean startEndProperties() {
//        if (this.startNodeProperty != null && this.endNodeProperty != null) {
//            this.cypher = new StringBuilder().
//                    append("MATCH p=(n" + CypherTool.appendLabels(this.startNodeLabel) + ")-[" + addPathLength() + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") " +
//                            "WHERE  RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
//                    .toString();
//        }

        return true;
    }

    private boolean startProperties() {
        if (this.startNodeProperty != null) {
            this.cypher = new StringBuilder().
                    append("MATCH p=(n" + CypherTool.appendLabels(this.startNodeLabel) + ")-[" + addPathLength() + "]-(m) " +
                            "WHERE " + CypherTool.appendProperty("n", this.startNodeProperty) + " RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                    .toString();
            return false;
        } else if (this.startNodePropertyDSL != null) {
            this.cypher = new StringBuilder().
                    append("MATCH p=(n" + CypherTool.appendLabels(this.startNodeLabel) + ")-[" + addPathLength() + "]-(m) " +
                            "WHERE " + this.startNodePropertyDSL + " RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                    .toString();
            return false;
        }
        return true;
    }

    private String addPathLength() {
        if (this.pathLength == 1 && this.fixPathLength == 1) {
            return new StringBuilder().append("*1").toString();
        } else if (this.pathLength > 1 && this.fixPathLength == 1) {
            return new StringBuilder().append("*.." + this.pathLength).toString();
        } else if (this.pathLength == 1 && this.fixPathLength >= 1) {
            return new StringBuilder().append("*" + this.fixPathLength).toString();
        }
        return "";
    }

    private String reset() {
        StringBuilder builder = new StringBuilder(this.cypher);
        this.cypher = null;
        this.startNodeIds.clear();
        this.endNodeIds.clear();
        this.pathLength = 1;
        this.skip = DataSize.SIZE_0.getSymbolValue();
        this.limit = DataSize.SIZE_10.getSymbolValue();
        this.relationshipTypes.clear();
        this.startNodeProperty = null;
        this.startNodePropertyDSL = null;
        this.startNodeFullTextSearch = null;
        this.startNodeFullTextSearchScore = null;
        this.runTimeBoxed = null;
        return builder.toString();
    }

    public PathSeeker setRelationType(RelationshipType relationshipType) {
        this.relationshipTypes.add(relationshipType);
        return this;
    }

    public PathSeeker setRelationType(RelationshipType... relationshipType) {
        this.relationshipTypes.addAll(Arrays.asList(relationshipType));
        return this;
    }

    /**
     * @param skip :默认0
     * @return
     * @Description: TODO(设置分页开始的数据)
     */
    public PathSeeker setStart(int skip) {
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
        return this;
    }

    /**
     * @param limit :默认20000
     * @return
     * @Description: TODO(设置分页开始的数据)
     */
    public PathSeeker setRow(int limit) {
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
        return this;
    }

    public PathSeeker setPathLength(int pathLength) {
        this.pathLength = pathLength;
        return this;
    }

    public PathSeeker setFixPathLength(int fixPathLength) {
        this.fixPathLength = fixPathLength;
        return this;
    }

    public PathSeeker setPlugin(PathPlugin pathPlugin) {
        this.pathPlugin = pathPlugin;
        return this;
    }

    /**
     * @param fullTextName:全文索引名称
     * @param queryString:查询-支持lucene语法
     * @param startNodeSkip:设置分页参数
     * @param startNodeLimit:设置分页参数
     * @return
     * @Description: TODO(全文检索 - 搜索节点)
     */
    public PathSeeker setStartNodeFullTextSearch(String fullTextName, String queryString, int startNodeSkip, int startNodeLimit) {
        this.startNodeFullTextSearch = new StringBuilder()
                .append("CALL db.index.fulltext.queryNodes('" + fullTextName + "', '" + queryString + "') YIELD node WITH node SKIP " + startNodeSkip + " LIMIT " + startNodeLimit + "")
                .toString();
        return this;
    }

    public PathSeeker setStartNodeFullTextSearchScore(String fullTextName, String queryString, int startNodeSkip, int startNodeLimit) {
        this.startNodeFullTextSearchScore = new StringBuilder()
                .append("CALL db.index.fulltext.queryNodes('" + fullTextName + "', '" + queryString + "') YIELD node,score SET node.fullTextSearchScore=score " +
                        "WITH node SKIP " + this.skip + " LIMIT " + this.limit + "")
                .toString();
        return this;
    }

    public PathSeeker setRunTimeBoxed(int timeout) {
        this.runTimeBoxed = new RunTimeBoxed(timeout);
        return this;
    }

    public PathSeeker setRunTimeBoxed(Map<String, Object> params, int timeout) {
        this.runTimeBoxed = new RunTimeBoxed(params, timeout);
        return this;
    }

}


