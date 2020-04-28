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

import java.util.*;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.search
 * @Description: TODO(RELATION SEEKER)
 * @date 2019/7/26 16:06
 */
public class RelationSeeker {
    private Logger logger = Logger.getLogger(this.getClass());
    private int skip = DataSize.SIZE_0.getSymbolValue();
    private int limit = DataSize.SIZE_10.getSymbolValue();
    private String cypher;
    private List<Long> ids = new ArrayList<>();

    private List<RelationshipType> relationshipTypes = new ArrayList<>();
    private Property property;
    private String propertiesDSL;

    private List<Long> startNodeIds = new ArrayList<>();

    private List<Label> startNodeLabel = new ArrayList<>();
    private Property startNodeProperty;

    private List<Label> endNodeLabel = new ArrayList<>();
    private Property endNodeProperty;

    private boolean isReturnEndNode = false;

    // 路径插件
    private PathPlugin pathPlugin;

    public RelationSeeker setId(long id) {
        this.ids.add(id);
        return this;
    }


    public RelationSeeker setStartNodeLabel(Label label) {
        this.startNodeLabel.add(label);
        return this;
    }

    public RelationSeeker setStartNodeProperties(Property properties) {
        this.startNodeProperty = properties;
        return this;
    }

    public RelationSeeker setEndNodeProperties(Property properties) {
        this.endNodeProperty = properties;
        return this;
    }

    public RelationSeeker setReturnEndNode(boolean isReturnEndNode) {
        this.isReturnEndNode = isReturnEndNode;
        return this;
    }

    public RelationSeeker setProperties(Property properties) {
        this.property = properties;
        return this;
    }

    public RelationSeeker setEndNodeLabel(Label label) {
        this.endNodeLabel.add(label);
        return this;
    }

    public RelationSeeker setPlugin(PathPlugin pathPlugin) {
        this.pathPlugin = pathPlugin;
        return this;
    }

    public RelationSeeker setProperties(String propertiesDSL) {
        this.propertiesDSL = propertiesDSL;
        return this;
    }

    public RelationSeeker setProperties(Property... _key_value) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < _key_value.length; i++) {
            map.put(_key_value[i].getKey(), _key_value[i].getValue());
        }
        this.propertiesDSL = CypherTool.appendProperty("r", map).replace(",", " AND ");
        return this;
    }

    public RelationSeeker setStartNodeId(long id) {
        this.startNodeIds.add(id);
        return this;
    }

    public RelationSeeker setStartNodeId(long... id) {
        long[] longs = id;
        Long[] lons = new Long[longs.length];
        for (int i = 0; i < longs.length; i++) {
            lons[i] = longs[i];
        }
        this.startNodeIds.addAll(Arrays.asList(lons));
        return this;
    }

    public RelationSeeker setStartNodeId(JSONArray ids) {
        for (int i = 0; i < ids.size(); i++) {
            this.startNodeIds.add(ids.getLongValue(i));
        }
        return this;
    }

    public RelationSeeker setId(long... id) {
        long[] longs = id;
        Long[] lons = new Long[longs.length];
        for (int i = 0; i < longs.length; i++) {
            lons[i] = longs[i];
        }
        this.ids.addAll(Arrays.asList(lons));
        return this;
    }

    public String toQuery() {
        if (!this.startNodeIds.isEmpty()) {
            if (this.property != null) {
                this.cypher = new StringBuilder().
                        append("MATCH p=(n)-[r" + CypherTool.appendRelationTypes(this.relationshipTypes) + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") " +
                                "WHERE id(n) IN " + JSONArray.parseArray(JSON.toJSONString(this.startNodeIds)) + " AND " + CypherTool.appendProperty("r", this.property) + " RETURN p" + " SKIP " + this.skip + " LIMIT " + this.limit + "")
                        .toString();
            } else {
                if (this.propertiesDSL != null) {
                    this.cypher = new StringBuilder().
                            append("MATCH p=(n)-[r" + CypherTool.appendRelationTypes(this.relationshipTypes) + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") " +
                                    "WHERE id(n) IN " + JSONArray.parseArray(JSON.toJSONString(this.startNodeIds)) + " AND " + this.propertiesDSL + " RETURN p" + " SKIP " + this.skip + " LIMIT " + this.limit + "")
                            .toString();
                } else {
                    this.cypher = new StringBuilder().
                            append("MATCH p=(n)-[r" + CypherTool.appendRelationTypes(this.relationshipTypes) + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") " +
                                    "WHERE id(n) IN " + JSONArray.parseArray(JSON.toJSONString(this.startNodeIds)) + " RETURN p" + " SKIP " + this.skip + " LIMIT " + this.limit + "")
                            .toString();
                }
            }
        } else if (!this.startNodeLabel.isEmpty()) {
            if (this.startNodeProperty != null && this.endNodeProperty != null) {
                this.cypher = new StringBuilder().
                        append("MATCH p=(n" + CypherTool.appendLabels(this.startNodeLabel) + ")-[r" + CypherTool.appendRelationTypes(this.relationshipTypes) + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") " +
                                "WHERE " + CypherTool.appendProperty("n", this.startNodeProperty) + " AND " + CypherTool.appendProperty("m", this.endNodeProperty) + " RETURN " + returnPara() + " SKIP " + this.skip + " LIMIT " + this.limit + "")
                        .toString();

            } else if (this.startNodeProperty != null) {
                if (this.property != null) {
                    this.cypher = new StringBuilder().
                            append("MATCH p=(n" + CypherTool.appendLabels(this.startNodeLabel) + ")-[r" + CypherTool.appendRelationTypes(this.relationshipTypes) + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") " +
                                    "WHERE " + CypherTool.appendProperty("n", this.startNodeProperty) + " AND " + CypherTool.appendProperty("r", this.property) + "  RETURN " + returnPara() + " SKIP " + this.skip + " LIMIT " + this.limit + "")
                            .toString();
                }else if (this.propertiesDSL!=null){
                    this.cypher = new StringBuilder().
                            append("MATCH p=(n" + CypherTool.appendLabels(this.startNodeLabel) + ")-[r" + CypherTool.appendRelationTypes(this.relationshipTypes) + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") " +
                                    "WHERE " + CypherTool.appendProperty("n", this.startNodeProperty) + " AND " + this.propertiesDSL + "  RETURN " + returnPara() + " SKIP " + this.skip + " LIMIT " + this.limit + "")
                            .toString();
                }else {
                    this.cypher = new StringBuilder().
                            append("MATCH p=(n" + CypherTool.appendLabels(this.startNodeLabel) + ")-[r" + CypherTool.appendRelationTypes(this.relationshipTypes) + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") " +
                                    "WHERE " + CypherTool.appendProperty("n", this.startNodeProperty) + " RETURN " + returnPara() + " SKIP " + this.skip + " LIMIT " + this.limit + "")
                            .toString();
                }
            } else if (this.startNodeProperty == null && this.endNodeProperty == null) {
                this.cypher = new StringBuilder().
                        append("MATCH p=(n" + CypherTool.appendLabels(this.startNodeLabel) + ")-[r" + CypherTool.appendRelationTypes(this.relationshipTypes) + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") " +
                                " RETURN " + returnPara() + " SKIP " + this.skip + " LIMIT " + this.limit + "")
                        .toString();
            }
        } else if (!this.ids.isEmpty()) {
            this.cypher = new StringBuilder().
                    append("MATCH p=()-[r]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") WHERE id(r) IN " + JSONArray.parseArray(JSON.toJSONString(ids)) + " RETURN p  SKIP " + this.skip + " LIMIT " + this.limit + "")
                    .toString();
        } else {
            if (this.property == null && this.propertiesDSL == null && !this.relationshipTypes.isEmpty()) {
                this.cypher = new StringBuilder()
                        .append("MATCH p=()-[r" + CypherTool.appendRelationTypes(this.relationshipTypes) + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                        .toString();
            } else if (this.property != null && this.propertiesDSL == null && !this.relationshipTypes.isEmpty()) {
                this.cypher = new StringBuilder()
                        .append("MATCH p=()-[r" + CypherTool.appendRelationTypes(this.relationshipTypes) + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") " +
                                "WHERE " + CypherTool.appendProperty("r", this.property) + " RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                        .toString();
            } else if (this.property == null && this.propertiesDSL != null && !this.relationshipTypes.isEmpty()) {
                this.cypher = new StringBuilder()
                        .append("MATCH p=()-[r" + CypherTool.appendRelationTypes(this.relationshipTypes) + "]-(m" + CypherTool.appendLabels(this.endNodeLabel) + ") " +
                                "WHERE " + this.propertiesDSL + " RETURN p SKIP " + this.skip + " LIMIT " + this.limit + "")
                        .toString();
            }
        }
        // 设置路径插件
        if (pathPlugin != null) this.cypher = pathPlugin.appendPathPlugin(this.cypher);

        return reset();
    }

    private String returnPara() {
        return isReturnEndNode ? " m " : " p ";
    }

    private String reset() {
        StringBuilder builder = new StringBuilder(this.cypher);
        this.cypher = null;
        this.ids.clear();
        this.skip = DataSize.SIZE_0.getSymbolValue();
        this.limit = DataSize.SIZE_10.getSymbolValue();
        this.relationshipTypes.clear();
        this.property = null;
        this.propertiesDSL = null;
        this.startNodeIds.clear();
        this.startNodeLabel.clear();
        this.startNodeProperty = null;
        this.isReturnEndNode = false;
        this.endNodeLabel.clear();
        this.pathPlugin = null;
        this.endNodeProperty = null;
        return builder.toString();
    }

    public RelationSeeker setRelationType(RelationshipType relationshipType) {
        this.relationshipTypes.add(relationshipType);
        return this;
    }

    public RelationSeeker setRelationType(RelationshipType... relationshipType) {
        this.relationshipTypes.addAll(Arrays.asList(relationshipType));
        return this;
    }

    /**
     * @param skip :默认0
     * @return
     * @Description: TODO(设置分页开始的数据)
     */
    public RelationSeeker setStart(int skip) {
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
    public RelationSeeker setRow(int limit) {
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
}