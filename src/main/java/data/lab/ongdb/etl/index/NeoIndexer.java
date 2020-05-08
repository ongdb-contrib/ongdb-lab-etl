package data.lab.ongdb.etl.index;
/*
*
 * Data Lab - graph database organization.
*
 */


import data.lab.ongdb.etl.common.CRUD;
import data.lab.ongdb.etl.common.NeoAccessor;
import data.lab.ongdb.etl.model.Condition;
import data.lab.ongdb.etl.model.Label;
import data.lab.ongdb.etl.model.RelationshipType;
import data.lab.ongdb.etl.model.Result;
import data.lab.ongdb.etl.util.JSONTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.*;
import org.neo4j.driver.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.index
 * @Description: TODO(索引工具)
 * @date 2019/7/18 15:25
 */
public class NeoIndexer extends NeoAccessor implements Indexer {

    private static final Logger LOGGER = LogManager.getLogger(NeoIndexer.class);

    // 节点属性索引条件
    private List<Condition> nodeFieldIndex = new ArrayList<>();

    // 关系属性索引条件
    private List<Condition> relationFieldIndex = new ArrayList<>();

    // 请求集合
    private JSONArray queryResultList = new JSONArray();

    /**
     * @param ipPorts      :服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount  :节点的用户名
     * @param authPassword :节点用户名密码
     * @return
     * @Description: TODO(构造函数 - 默认使用JAVA - DRIVER发送请求 ， D3_GRAPH格式返回数据)
     */
    public NeoIndexer(String ipPorts, String authAccount, String authPassword) {
        super(ipPorts, authAccount, authPassword);
    }

    /**
     * @param ipPorts      :服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount  :节点的用户名
     * @param authPassword :节点用户名密码
     * @return
     * @Description: TODO(构造函数 - 默认使用JAVA - DRIVER发送请求 ， D3_GRAPH格式返回数据)
     */
    public NeoIndexer(String ipPorts, String authAccount, String authPassword, Config config) {
        super(ipPorts, authAccount, authPassword, config);
    }

    /**
     * @param label:标签名
     * @param fieldName:可以传入多个字段名
     * @return
     * @Description: TODO(给某个标签增索引 - 支持单属性索引和复合索引)
     */
    @Override
    public void addNodeFieldIndex(Label label, String... fieldName) {

        StringBuilder builder = new StringBuilder();
        String[] fields = fieldName;
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            builder.append(field + ",");
        }
        String fieldNames = builder.substring(0, builder.length() - 1);
        Condition condition = new Condition();
        condition.setStatement("CREATE index ON :" + label.name() + "(" + fieldNames + ") ", super.contents);
        this.nodeFieldIndex.add(condition);
    }

    /**
     * @param label     :标签名
     * @param fieldName :字段名
     * @return
     * @Description: TODO(唯一索引)
     */
    @Override
    public void addNodeFieldUniqueIndex(Label label, String fieldName) {
        Condition condition = new Condition();
        condition.setStatement("CREATE CONSTRAINT ON (node:"+label.name()+") ASSERT node."+fieldName+" IS UNIQUE ", super.contents);
        this.nodeFieldIndex.add(condition);
    }

    /**
     * @param relationshipType :关系名称
     * @param fieldName        :被索引字段
     * @return
     * @Description: TODO(某个关系增加索引 - 支持单属性索引和复合索引 - 使用默认的关系索引模式)
     */
    @Override
    public void addRelationFieldIndex(RelationshipType relationshipType, String... fieldName) {
        String[] fields = fieldName;
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            builder.append("'" + field + "',");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(builder.substring(0, builder.length() - 1));
        stringBuilder.append("]");
        Condition condition = new Condition();
        condition.setStatement(new StringBuilder().append("MATCH ()-[r:" + relationshipType.name() + "]->() ")
                .append("CALL apoc.index.addRelationship(r," + stringBuilder.toString() + ") RETURN count(*) ").toString(), super.contents);
        this.relationFieldIndex.add(condition);
    }

    /**
     * @param autoMatchRelationCypher
     * @param relationshipType        :关系名称
     * @param fieldName               :被索引字段
     * @return
     * @Description: TODO(某个关系增加索引 - 支持单属性索引和复合索引 - 使用指定的关系索引模式)
     */
    @Override
    public void addRelationFieldIndex(String autoMatchRelationCypher, RelationshipType relationshipType, String... fieldName) {
        String[] fields = fieldName;
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            builder.append("'" + field + "',");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(builder.substring(0, builder.length() - 1));
        stringBuilder.append("]");
        Condition condition = new Condition();
        condition.setStatement(new StringBuilder().append(autoMatchRelationCypher + " ")
                .append("CALL apoc.index.addRelationship(r," + stringBuilder.toString() + ") RETURN count(*) ").toString(), super.contents);
        this.relationFieldIndex.add(condition);
    }

    /**
     * @param fullTextSearchName:全文检索名称-在创建好全文检索之后搜索时使用
     * @param fullTextMap:为节点增加全文检索属性
     * @param autoUpdate:是否配置全文检索的自动更新
     * @return
     * @Description: TODO(给节点增加全文检索)
     */
    @Override
    public void addNodeFullTextSearch(String fullTextSearchName, data.lab.ongdb.etl.index.FullTextMap.FullTextNodeMap fullTextMap, boolean autoUpdate) {
        JSONObject search = JSONObject.parseObject(JSON.toJSONString(fullTextMap.getFullTextMap()));
        JSONObject auto = new JSONObject();
        auto.put("autoUpdate", autoUpdate);
        Condition condition = new Condition();
        condition.setStatement(new StringBuilder()
                .append("CALL apoc.index.addAllNodes('" + fullTextSearchName + "'," + JSONTool.removeOnlyJSONObjectKeyDoubleQuotation(search) + ","
                        + JSONTool.removeOnlyJSONObjectKeyDoubleQuotation(auto) + ")")
                .toString(), super.contents);
        this.nodeFieldIndex.add(condition);
    }

    /**
     * @param fullTextSearchName :全文检索名称-在创建好全文检索之后搜索时使用
     * @param fullTextMap        :为节点增加全文检索属性
     * @Description: TODO(给节点增加全文检索)
     */
    @Override
    public void addNodeFullTextSearchAutoUpdate(String fullTextSearchName, data.lab.ongdb.etl.index.FullTextMap.FullTextNodeMap fullTextMap) {
        JSONArray labels = new JSONArray();
        JSONArray properties = new JSONArray();
        Map<Label, String[]> map = fullTextMap.getFullTextMap();
        for (Map.Entry entry : map.entrySet()) {
            Label label = (Label) entry.getKey();
            String[] propertiesArray = (String[]) entry.getValue();
            if (!labels.contains(label)) {
                labels.add(label);
            }
            for (int i = 0; i < propertiesArray.length; i++) {
                String property = propertiesArray[i];
                if (!properties.contains(property)) {
                    properties.add(property);
                }
            }
        }
        Condition condition = new Condition();
        condition.setStatement(new StringBuilder()
                .append("CALL db.index.fulltext.createNodeIndex('" + fullTextSearchName + "'," + labels.toJSONString() + "," + properties.toJSONString() + ")")
                .toString(), super.contents);
        this.nodeFieldIndex.add(condition);
    }

    /**
     * @param fullTextSearchName :创建的全文检索名称
     * @return
     * @Description: TODO(删除创建的全文检索接口)
     */
    @Override
    public JSONObject dropFullText(String fullTextSearchName) {
        Condition condition = new Condition();
        condition.setStatement(new StringBuilder()
                .append("CALL db.index.fulltext.drop('" + fullTextSearchName + "')")
                .toString(), super.contents);
        return Result.message(super.chooseSendCypherWay(condition, CRUD.DELETE));
    }

    /**
     * @return
     * @Description: TODO(NEO4J访问对象的全局变量重置方法)
     */
    @Override
    public void reset() {
        this.nodeFieldIndex.clear();
        this.relationFieldIndex.clear();
        this.queryResultList.clear();
    }

    /**
     * @return
     * @Description: TODO(执行请求 - 拼接请求之后执行 - 默认返回节点或者关系的所有属性字段)
     */
    @Override
    public JSONObject execute() {

        /**
         * List<Condition>分开处理：每个Condition也需要控制大小
         *
         * **/

        long startMill = System.currentTimeMillis();

        this.nodeFieldIndex.stream().forEach(condition -> {
            this.queryResultList.add(Result.message(super.chooseSendCypherWay(condition, CRUD.INDEX)));
        });

        this.relationFieldIndex.stream().forEach(condition -> {
            this.queryResultList.add(Result.message(super.chooseSendCypherWay(condition, CRUD.RETRIEVE)));
        });

        long stopMill = System.currentTimeMillis();
        JSONObject result = Result.statistics(this.queryResultList);

        // 统计所有请求的耗时，以及每个请求的平均耗时
        result.put("consume", Result.statisticsConsume(startMill, stopMill, this.queryResultList.size()));
        return result;
    }

}

