package data.lab.ongdb.etl.util;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import data.lab.ongdb.etl.common.CRUD;
import data.lab.ongdb.etl.compose.NeoComposer;
import data.lab.ongdb.etl.compose.pack.Cypher;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import data.lab.ongdb.etl.model.Label;
import data.lab.ongdb.http.extra.HttpRequest;

import java.util.Map;

/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.etl.util
 * @Description: TODO(数据转储工具)
 * @date 2019/12/10 14:14
 */
public class NeoImportTool {

    /**
     * 使用自动生成的ID作为唯一
     * 节点默认排重属性名称：_dump_default_id[对应自增ID]
     **/

    private final static String _dump_default_id = "_dump_default_id";

    private final static Label dumpLabel = Label.label("DUMP");

    private final static Cypher initCypher = new Cypher("CREATE index ON :" + dumpLabel.name() + "(" + _dump_default_id + ");");

    /**
     * @param
     * @return
     * @Description: TODO(文件中写入建立索引的CYPHER)
     */
    public static void initIndex(String pathName, String cqlFileName, boolean isAppend) {
        FileUtil.writeIDSToFile(initCypher.getCypher(), pathName, cqlFileName, isAppend);
    }

    public static JSONObject queryCypher(String url, String cypher) {
        // 通过HTTP直接执行CYPHER
        HttpRequest request = new HttpRequest();
        JSONObject query = new JSONObject();
        query.put("cypher", cypher);
        String response = request.httpPost(url, query.toJSONString());
        return JSONObject.parseObject(response);
    }

    public static JSONObject queryCypher(String url) {
        HttpRequest request = new HttpRequest();
        url = url.replaceAll(" ", "%20");
        String response = request.httpGet(url);
        return JSONObject.parseObject(response);
    }

    public static void dump(NeoComposer targetComposer, JSONObject result) throws Exception {

        JSONArray nodes = JSONTool.getNodeOrRelaList(result, "nodes");
        JSONArray relationships = JSONTool.getNodeOrRelaList(result, "relationships");

        relationships = JSONTool.removeNull(relationships);

        writeCypherToCql(targetComposer, nodes, true);
        writeCypherToCql(targetComposer, relationships, false);
    }

//    /**
//     * @param source:目标COMPOSER
//     * @param dumpCypher:转储语句
//     * @param target:目标COMPOSER
//     * @return
//     * @Description: TODO(根据CYPHER将数据完整转储到另外一个库)
//     */
//    public JSONObject dump(NeoSearcher source, String dumpCypher, NeoComposer target) {
//        return null;
//    }

//    /**
//     * @param source:目标COMPOSER
//     * @param dumpCypher:转储语句
//     * @param pathName:目标文件夹名称
//     * @param cqlFileName:在根目录cql文件夹生成转储的cql文件
//     * @param isAppend:是否追加写入
//     * @return
//     * @Description: TODO(根据CYPHER将数据完整生成CQL)
//     */
//    public static boolean dump(NeoSearcher source, String dumpCypher, String pathName, String cqlFileName, boolean isAppend, boolean dumpByHttp) {
//
//        JSONObject result = source.execute(dumpCypher, CRUD.RETRIEVE);
//        JSONArray nodes = JSONTool.getNodeOrRelaList(result, "nodes");
//        JSONArray relationships = JSONTool.getNodeOrRelaList(result, "relationships");
//
//        writeCypherToCql(nodes, true, pathName, cqlFileName, isAppend, dumpByHttp);
//        writeCypherToCql(relationships, false, pathName, cqlFileName, isAppend, dumpByHttp);
//
//        return JSONTool.isNeoD3ObjEmpty(result);
//    }

    private static void writeCypherToCql(JSONArray array, boolean isNodeObj, String pathName, String cqlFileName, boolean isAppend, boolean dumpByHttp) {
        array.forEach(obj -> {
            JSONObject object = (JSONObject) obj;
            Cypher cypher;
            if (isNodeObj) {
                cypher = nodeObjToCypher(object);
            } else {
                cypher = relationObjToCypher(object);
            }
            if (!dumpByHttp) {
                FileUtil.writeIDSToFile(cypher.getCypher(), pathName, cqlFileName, isAppend);
            } else {
                // 通过HTTP直接执行CYPHER
                HttpRequest request = new HttpRequest();
                JSONObject query = new JSONObject();
                query.put("cypher", cypher.getCypher());
                String response = request.httpPost("http://60.10.65.219:9125/sub_person/graph/postDetailAttr", query.toJSONString());
            }
        });
    }

    private static void writeCypherToCql(NeoComposer composer, JSONArray array, boolean isNodeObj) throws Exception{
        for (Object obj:array){
            JSONObject object = (JSONObject) obj;
            Cypher cypher;
            if (isNodeObj) {
                cypher = nodeObjToCypher(object);
            } else {
                cypher = relationObjToCypher(object);
            }
            composer.execute(cypher.getCypher(), CRUD.MERGE);
        }
    }

    /**
     * @param node:节点对象
     * @return
     * @Description: TODO(节点对象转为一个MERGE CYPHER)
     */
    private static Cypher nodeObjToCypher(JSONObject node) {

        StringBuilder builder = new StringBuilder();
        builder.append("MERGE ");
        builder.append("(node:" + dumpLabel.name() + " {" + _dump_default_id + ":" + node.getLongValue("id") + "}) ");

        String propertiesStr = appendProperties(node.getJSONObject("properties"));

        builder.append("SET node+=" + propertiesStr + ",node" + appendLabels(node.getJSONArray("labels")) + "");
        builder.append(";");

        return new Cypher(builder.toString());
    }

    private static String appendProperties(JSONObject properties) {
//        if (properties.containsKey("name")) properties.put("nameNodeSpace", properties.getString("name").split("_")[0]);
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (Map.Entry entry : properties.entrySet()) {
            Object object = entry.getValue();
            if (object instanceof Integer || object instanceof Long) {
                builder.append(entry.getKey() + ":" + object + ",");
            } else {
                object = removeCharFromJson(String.valueOf(object));
                builder.append(entry.getKey() + ":'" + object + "',");
            }
        }
        builder = new StringBuilder().append(builder.subSequence(0, builder.length() - 1));
        builder.append("}");
        return builder.toString();
    }

    /**
     * @param
     * @return
     * @Description: TODO(原始字符串 - 去掉原始字符串中影响转换JSON的特殊字符)
     */
    private static String removeCharFromJson(String rawStr) {
        // ["\\","\b","\f","\n","\r","\t"]
        //                .replace(" ", "&nbsp;")
//        return  rawStr
        return rawStr.trim().replace(">", "")
                .replace("<", "")
                .replace("\"", "")
                .replace("\'", "")
                .replace("\\", "")
                .replace("\n", "")
                .replace("\r", "")
                .replace("\"", "")
                .replace(":", "");
    }

    private static String appendLabels(JSONArray labels) {
        StringBuilder builder = new StringBuilder();
        labels.forEach(v -> builder.append(":").append(v));
        return builder.toString();
    }

    /**
     * @param relationship:节点对象
     * @return
     * @Description: TODO(关系对象转为一个MERGE CYPHER)
     */
    private static Cypher relationObjToCypher(JSONObject relationship) {
        StringBuilder builder = new StringBuilder();
        builder.append("MATCH (from:" + dumpLabel.name() + " {" + _dump_default_id + ":" + relationship.getLongValue("startNode") + "})," +
                "(to:" + dumpLabel.name() + " {" + _dump_default_id + ":" + relationship.getLongValue("endNode") + "}) ");

        builder.append("MERGE p=(from)-[r:" + relationship.getString("type") + "]->(to) ");

        String propertiesStr = appendProperties(relationship.getJSONObject("properties"));
        builder.append("SET r+=" + propertiesStr + "");
        builder.append(";");

        return new Cypher(builder.toString());
    }

}


