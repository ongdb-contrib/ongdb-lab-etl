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
import data.lab.ongdb.util.CypherTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.search
 * @Description: TODO(NODE SEEKER)
 * @date 2019/7/26 16:05
 */
public class NodeSeeker {

    private Logger logger = Logger.getLogger(this.getClass());
    private int skip = DataSize.SIZE_0.getSymbolValue();
    private int limit = DataSize.SIZE_10.getSymbolValue();
    private String cypher;
    private List<Long> ids = new ArrayList<>();

    private List<Label> nodeLabels = new ArrayList<>();
    private Property property;
    private String propertiesDSL;

    public NodeSeeker setId(long id) {
        this.ids.add(id);
        return this;
    }

    public NodeSeeker setId(long... id) {
        long[] longs = id;
        Long[] lons = new Long[longs.length];
        for (int i = 0; i < longs.length; i++) {
            lons[i] = longs[i];
        }
        this.ids.addAll(Arrays.asList(lons));
        return this;
    }

    public String toQuery() {
        if (!ids.isEmpty()) {
            this.cypher = new StringBuilder().
                    append("MATCH (n) WHERE id(n) IN " + JSONArray.parseArray(JSON.toJSONString(ids)) + " RETURN n")
                    .toString();
        } else {
            if (this.property == null && this.propertiesDSL == null && !this.nodeLabels.isEmpty()) {
                this.cypher = new StringBuilder()
                        .append("MATCH (n" + CypherTool.appendLabels(this.nodeLabels) + ") RETURN n SKIP " + this.skip + " LIMIT " + this.limit + "")
                        .toString();
            } else if (this.property != null && this.propertiesDSL == null && !this.nodeLabels.isEmpty()) {
                this.cypher = new StringBuilder()
                        .append("MATCH (n" + CypherTool.appendLabels(this.nodeLabels) + ") WHERE " + CypherTool.appendProperty("n", this.property) + " RETURN n SKIP " + this.skip + " LIMIT " + this.limit + "")
                        .toString();
            } else if (this.property == null && this.propertiesDSL != null && !this.nodeLabels.isEmpty()) {
                this.cypher = new StringBuilder()
                        .append("MATCH (n" + CypherTool.appendLabels(this.nodeLabels) + ") WHERE " + this.propertiesDSL + " RETURN n SKIP " + this.skip + " LIMIT " + this.limit + "")
                        .toString();
            }
        }
        return reset();
    }

    private String reset() {
        StringBuilder builder = new StringBuilder(this.cypher);
        this.cypher = null;
        this.ids.clear();
        this.skip = DataSize.SIZE_0.getSymbolValue();
        this.limit = DataSize.SIZE_10.getSymbolValue();
        this.nodeLabels.clear();
        this.property = null;
        this.propertiesDSL = null;
        return builder.toString();
    }

    public NodeSeeker setLabel(Label label) {
        this.nodeLabels.add(label);
        return this;
    }

    public NodeSeeker setLabel(Label... label) {
        this.nodeLabels.addAll(Arrays.asList(label));
        return this;
    }

    /**
     * @param skip :默认0
     * @return
     * @Description: TODO(设置分页开始的数据)
     */
    public NodeSeeker setStart(int skip) {
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
    public NodeSeeker setRow(int limit) {
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

    public NodeSeeker setProperties(Property properties) {
        this.property = properties;
        return this;
    }

    public NodeSeeker setProperties(String propertiesDSL) {
        this.propertiesDSL = propertiesDSL;
        return this;
    }

    /**
     * @param fullTextName:全文索引名称
     * @param queryString:查询-支持lucene语法
     * @return
     * @Description: TODO(全文检索 - 搜索节点)
     */
    public String fullTextQuery(String fullTextName, String queryString) {
        String cypher = new StringBuilder()
                .append("CALL db.index.fulltext.queryNodes('" + fullTextName + "', '" + queryString + "') YIELD node RETURN node SKIP " + this.skip + " LIMIT " + this.limit + "")
                .toString();
        this.cypher = cypher;
        reset();
        return cypher;
    }

    /**
     * @param fullTextName:全文索引名称
     * @param queryString:查询-支持lucene语法
     * @return
     * @Description: TODO(全文检索 - 搜索节点)
     */
    public String fullTextQueryScore(String fullTextName, String queryString) {
        String cypher = new StringBuilder()
                .append("CALL db.index.fulltext.queryNodes('" + fullTextName + "', '" + queryString + "') YIELD node,score SET node.fullTextSearchScore=score " +
                        "RETURN node SKIP " + this.skip + " LIMIT " + this.limit + "")
                .toString();
        this.cypher = cypher;
        reset();
        return cypher;
    }

}

