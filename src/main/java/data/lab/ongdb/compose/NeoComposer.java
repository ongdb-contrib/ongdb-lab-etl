package data.lab.ongdb.compose;
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
import data.lab.ongdb.compose.pack.*;
import data.lab.ongdb.driver.Neo4jDriver;
import data.lab.ongdb.http.server.HttpService;
import data.lab.ongdb.model.*;
import data.lab.ongdb.util.CypherTool;
import data.lab.ongdb.util.FileUtil;
import data.lab.ongdb.util.JSONTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.neo4j.driver.v1.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.compose
 * @Description: TODO(构图工具)
 * @date 2019/7/9 11:50
 */
public class NeoComposer extends NeoAccessor implements Composer {

    private Logger logger = Logger.getLogger(this.getClass());

    // 支持动态批量更新节点的请求列表
    public List<Condition> addMergeDynamicNodes = new ArrayList<>();

    // 支持动态批量更新关系的请求列表
    public List<Condition> addMergeDynamicRelations = new ArrayList<>();

    // 请求集合
    public JSONArray queryResultList = new JSONArray();

    // 是否启动接口的HTTP-SERVICE
    public static boolean HTTP_SERVICE_IS_OPEN = true;

    /**
     * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount:节点的用户名
     * @param authPassword:节点用户名密码
     * @return
     * @Description: TODO(构造函数)
     */
    public NeoComposer(String ipPorts, String authAccount, String authPassword) {
        super(ipPorts, authAccount, authPassword);
        if (HTTP_SERVICE_IS_OPEN) {
            try {
                new HttpService().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount:节点的用户名
     * @param authPassword:节点用户名密码
     * @return
     * @Description: TODO(构造函数)
     */
    public NeoComposer(String ipPorts, String authAccount, String authPassword, Config config) {
        super(ipPorts, authAccount, authPassword, config);
        if (HTTP_SERVICE_IS_OPEN) {
            try {
                new HttpService().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount:节点的用户名
     * @param authPassword:节点用户名密码
     * @param httpServicePort:指定启动的HTTP-SERVICE端口号
     * @return
     * @Description: TODO(构造函数)
     */
    public NeoComposer(String ipPorts, String authAccount, String authPassword, int httpServicePort) {
        super(ipPorts, authAccount, authPassword);
        if (HTTP_SERVICE_IS_OPEN) {
            try {
                new HttpService().run(httpServicePort);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    public NeoComposer(AccessOccurs accessOccurs, String ipPorts, String authAccount, String authPassword) {
        super(accessOccurs, ipPorts, authAccount, authPassword);
    }

    /**
     * @param contents:指定图数据使用什么格式返回，默认GRAPH格式返回
     * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount:节点的用户名
     * @param authPassword:节点用户名密码
     * @return
     * @Description: TODO(构造函数)
     */
    @Deprecated
    public NeoComposer(ResultDataContents contents, String ipPorts, String authAccount, String authPassword) {
        super(contents, ipPorts, authAccount, authPassword);
    }

    /**
     * @param contents:指定图数据使用什么格式返回，默认GRAPH格式返回
     * @param accessOccurs:选择用哪种方式与NEO4J进行交互
     * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount:节点的用户名
     * @param authPassword:节点用户名密码
     * @return
     * @Description: TODO(构造函数)
     */
    @Deprecated
    public NeoComposer(ResultDataContents contents, AccessOccurs accessOccurs, String ipPorts, String authAccount, String authPassword) {
        super(contents, accessOccurs, ipPorts, authAccount, authPassword);
    }

    /**
     * 通用场景下使用的接口（效率较差-适合数据量较少但是结构复杂的更新创建 - 自定义CYPHER）
     *
     * @param cypherList
     */
    @Override
    public JSONObject executeImport(List<Cypher> cypherList) {
        if (cypherList.isEmpty()) {
            return passEmpty();
        }
        JSONArray message = new JSONArray();
        cypherList.parallelStream().forEach(cypher ->
                message.add(Neo4jDriver.composer(this.driver, cypher.getCypher())));
        JSONObject result = new JSONObject();
        result.put("result", message);
        return result;
    }

    /**
     * @param cypher   @return
     * @param crudType
     * @Description: TODO(跳过条件添加直接使用CYPHER查询 - 默认返回节点或者关系的所有属性字段)
     */
    @Override
    public JSONObject execute(String cypher, CRUD crudType) {
        Condition condition = new Condition();
        condition.setStatement(cypher, this.contents);
        return chooseSendCypherWay(condition, crudType);
    }

    /**
     * @param nodes        :节点列表           Object[]中数据存入的顺序和接口参数传入字段顺序需要保持一致(String _uniqueField, String... _key)
     * @param label        :节点标签
     * @param _uniqueField :合并的唯一字段
     * @param _key         :MERGE的属性字段
     * @return
     * @Description: TODO(导入节点)
     */
    @Override
    public JSONObject executeImport(List<Object[]> nodes, Label label, String _uniqueField, String... _key) {
        if (nodes.isEmpty()) {return passEmpty();}
        StringBuilder builder = new StringBuilder();
        String[] keys = _key;
        int serialNumber;
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            serialNumber = i + 1;
            builder.append("n." + key + "=line[" + serialNumber + "],");
        }
        String propertiesStr = builder.substring(0, builder.length() - 1);

        String dataPackage = JSONArray.parseArray(JSON.toJSONString(nodes)).toJSONString();
        String cypher = new StringBuilder()
                .append("UNWIND " + dataPackage + " AS line ")
                .append("MERGE (n:" + label.name() + " {" + _uniqueField + ":line[0]}) ")
                .append("SET " + propertiesStr)
                .toString();
        Condition condition = new Condition();
        condition.setStatement(cypher, super.contents);
        return super.chooseSendCypherWay(condition, CRUD.MERGE);
    }

    /**
     * @param nodes          :节点列表             Object[]中数据存入的顺序和接口参数传入字段顺序需要保持一致(String _uniqueField, String... _key)
     * @param label          :节点标签
     * @param setOtherLabels :添加额外的标签
     * @param _uniqueField   :合并的唯一字段
     * @param _key           :MERGE的属性字段
     * @return
     * @Description: TODO(导入节点)
     */
    @Override
    public JSONObject executeImport(List<Object[]> nodes, Label label, Label[] setOtherLabels, String _uniqueField, String... _key) {
        if (nodes.isEmpty()) {return passEmpty();}
        StringBuilder builder = new StringBuilder();
        String[] keys = _key;
        int serialNumber;
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            serialNumber = i + 1;
            builder.append("n." + key + "=line[" + serialNumber + "],");
        }
        String propertiesStr = builder.substring(0, builder.length() - 1);
        String otherLabels = buildSetOtherLabels(setOtherLabels);

        String dataPackage = JSONArray.parseArray(JSON.toJSONString(nodes)).toJSONString();
        String cypher = new StringBuilder()
                .append("UNWIND " + dataPackage + " AS line ")
                .append("MERGE (n:" + label.name() + " {" + _uniqueField + ":line[0]}) ")
                .append("SET " + propertiesStr)
                .append(",n:" + otherLabels)
                .toString();
        Condition condition = new Condition();
        condition.setStatement(cypher, super.contents);
        return super.chooseSendCypherWay(condition, CRUD.MERGE);
    }

    private String buildSetOtherLabels(Label[] setOtherLabels) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < setOtherLabels.length; i++) {
            Label setOtherLabel = setOtherLabels[i];
            builder.append(setOtherLabel + ":");
        }
        return builder.substring(0, builder.length() - 1);
    }

    /**
     * @param relations         :关系列表          Object[]中数据存入的顺序和接口参数传入字段顺序需要保持一致(String _uniqueFieldStart, String _uniqueFieldEnd, String... _key)
     * @param relationshipType  :生成的关系名
     * @param startNodeLabel    :起始节点的标签
     * @param endNodeLabel      :结束节点的标签
     * @param _uniqueFieldStart :开始节点
     * @param _uniqueFieldEnd   :结束节点
     * @param _key              :MERGE的属性字段
     * @return
     * @Description: TODO(导入关系)
     */
    @Override
    public JSONObject executeImport(List<Object[]> relations, RelationshipType relationshipType, Label startNodeLabel, Label endNodeLabel, String _uniqueFieldStart, String _uniqueFieldEnd, String... _key) {
        if (relations.isEmpty()) {return passEmpty();}
        StringBuilder builder = new StringBuilder();
        String[] keys = _key;
        int serialNumber;
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            serialNumber = i + 2;
            builder.append("rel." + key + "=line[" + serialNumber + "],");
        }
        String propertiesStr = builder.substring(0, builder.length() - 1);

        String dataPackage = JSONArray.parseArray(JSON.toJSONString(relations)).toJSONString();
        String cypher = new StringBuilder()
                .append("UNWIND " + dataPackage + " AS line ")
                .append("MATCH (from:" + startNodeLabel + " {" + _uniqueFieldStart + ":line[0]}) ")
                .append("MATCH (to:" + endNodeLabel + " {" + _uniqueFieldEnd + ":line[1]}) ")
                .append("MERGE (from)-[rel:" + relationshipType.name() + "]->(to) ")
                .append("SET " + propertiesStr)
                .toString();
        Condition condition = new Condition();
        condition.setStatement(cypher, super.contents);
        return super.chooseSendCypherWay(condition, CRUD.MERGE);
    }

    /**
     * @param csvName    :CSV文件名
     * @param _keyFields :CSV文件头
     * @return
     * @Description: TODO(生成CSV文件头 - 覆盖写)
     */
    @Deprecated
    @Override
    public void writeCsvHeader(String csvName, String... _keyFields) {
        StringBuilder builder = new StringBuilder();
        String[] csvNodeHeader = _keyFields;
        for (int i = 0; i < csvNodeHeader.length; i++) {
            String s = csvNodeHeader[i];
            builder.append(s + ",");
        }
        String header = builder.substring(0, builder.length() - 1) + "\r\n";
        FileUtil.writeDataToCSV(header, NeoUrl.NEO_CSV.getSymbolValue(), csvName, false);
        this.logger.info("Write node csv header...");
    }

    /**
     * @param csvName :CSV文件名
     * @param row     :写入的一行数据（生成的一行数据需要与文件头的字段数据一一对应）- row结尾必须使用\r\n进行换行（UTF-8无BOM格式）
     * @return
     * @Description: TODO(生成CSV文件体 - 追加写)
     */
    @Override
    public void writeCsvBody(String csvName, String row) {
        FileUtil.writeDataToCSV(row, NeoUrl.NEO_CSV.getSymbolValue(), csvName, true);
        this.logger.info("Write node csv body...");
    }

    /**
     * @param csvName@return
     * @Description: TODO(根据文件名删除CSV文件)
     */
    @Override
    public void deleteCsv(String csvName) {
        FileUtil.deleteFile(NeoUrl.NEO_CSV.getSymbolValue(), csvName);
    }

    /**
     * @param commitBatchSize:批量提交数量
     * @param csvName:CSV文件名（数据写入CSV的顺序需要和方法传入参数顺序保持一致）String _uniqueField, String... _key
     * @param label:节点标签
     * @param _uniqueField:合并的唯一字段
     * @param _key:MERGE的属性字段
     * @return
     * @Description: TODO(导入节点CSV)
     */
    @Override
    public JSONObject executeImportCsv(int commitBatchSize, String csvName, Label label, String _uniqueField, String... _key) {

        StringBuilder builder = new StringBuilder();
        String[] keys = _key;
        int serialNumber;
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            serialNumber = i + 1;
            builder.append("n." + key + "=line[" + serialNumber + "],");
        }
        String propertiesStr = builder.substring(0, builder.length() - 1);

        String cypher = new StringBuilder()
                .append("USING PERIODIC COMMIT " + commitBatchSize + " ")
                .append("LOAD CSV FROM \"" + HttpService.getUrlInterface() + "/" + csvName + "\" AS line ")
                .append("MERGE (n:" + label.name() + " {" + _uniqueField + ":line[0]}) ")
                .append("SET " + propertiesStr)
                .toString();
        Condition condition = new Condition();
        condition.setStatement(cypher, super.contents);
        return super.chooseSendCypherWay(condition, CRUD.MERGE_CSV);
    }

    /**
     * @param commitBatchSize:批量提交数量
     * @param csvName:CSV文件名（数据写入CSV的顺序需要和方法传入参数顺序保持一致）String _uniqueFieldStart,String _uniqueFieldEnd, String... _key
     * @param relationshipType:生成的关系名
     * @param startNodeLabel:起始节点的标签
     * @param _uniqueFieldStart:开始节点
     * @param endNodeLabel:结束节点的标签
     * @param _uniqueFieldEnd:结束节点
     * @param _key:MERGE的属性字段
     * @return
     * @Description: TODO(导入关系CSV)
     */
    @Override
    public JSONObject executeImportCsv(int commitBatchSize, String csvName, RelationshipType relationshipType, Label startNodeLabel, Label endNodeLabel,
                                       String _uniqueFieldStart, String _uniqueFieldEnd, String... _key) {

        StringBuilder builder = new StringBuilder();
        String[] keys = _key;
        int serialNumber;
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            serialNumber = i + 2;
            builder.append("rel." + key + "=line[" + serialNumber + "],");
        }
        String propertiesStr = builder.substring(0, builder.length() - 1);

        String cypher = new StringBuilder()
                .append("USING PERIODIC COMMIT " + commitBatchSize + " ")
                .append("LOAD CSV FROM \"" + HttpService.getUrlInterface() + "/" + csvName + "\" AS line ")
                .append("MATCH (from:" + startNodeLabel + " {" + _uniqueFieldStart + ":line[0]}) ")
                .append("MATCH (to:" + endNodeLabel + " {" + _uniqueFieldEnd + ":line[1]}) ")
                .append("MERGE (from)-[rel:" + relationshipType.name() + "]->(to) ")
                .append("SET " + propertiesStr)
                .toString();
        Condition condition = new Condition();
        condition.setStatement(cypher, super.contents);
        return super.chooseSendCypherWay(condition, CRUD.MERGE_CSV);
    }

    /**
     * @param noUpdateNodeList
     * @return
     * @Description: TODO(批量导入节点 - 不支持属性MERGE更新)
     */
    @Override
    public JSONObject importApocMergeNodes(List<NoUpdateNode> noUpdateNodeList) {
        if (noUpdateNodeList.isEmpty()) {return passEmpty();}
        JSONArray nodes = JSONArray.parseArray(JSON.toJSONString(noUpdateNodeList));
        String dataPackage = JSONTool.removeKeyDoubleQuotationMark(nodes);
        String cypher = "UNWIND " + dataPackage + " as row " +
                "CALL apoc.merge.node(row.labels, {" + Field.UNIQUEUUID.getSymbolValue() + ":row." + Field.UNIQUEUUID.getSymbolValue() + "},row.properties) yield node \n" +
                "RETURN node";
        Condition condition = new Condition();
        condition.setStatement(cypher, super.contents);
        return super.chooseSendCypherWay(condition, CRUD.RETRIEVE);
    }

    /**
     * @param noUpdateNodeList
     * @param uniqueFieldName  :指定唯一字段名字段名
     * @return
     * @Description: TODO(批量导入节点 - 不支持属性MERGE更新)
     */
    @Override
    public JSONObject importApocMergeNodes(List<NoUpdateNode> noUpdateNodeList, String uniqueFieldName) {
        if (noUpdateNodeList.isEmpty()) {
            return passEmpty();
        }
        JSONArray nodes = JSONArray.parseArray(JSON.toJSONString(noUpdateNodeList));
        String dataPackage = JSONTool.removeKeyDoubleQuotationMark(nodes);
        String cypher = "UNWIND " + dataPackage + " as row " +
                "CALL apoc.merge.node(row.labels, {" + uniqueFieldName + ":row." + Field.UNIQUEUUID.getSymbolValue() + "},row.properties) yield node \n" +
                "RETURN node";
        Condition condition = new Condition();
        condition.setStatement(cypher, super.contents);
        return super.chooseSendCypherWay(condition, CRUD.RETRIEVE);
    }

    /**
     * @param noUpdateRelaList
     * @return
     * @Description: TODO(批量导入关系 - 不支持属性MERGE更新)★★★默认使用name属性排重关系
     */
    @Override
    public JSONObject importApocMergeRelations(List<NoUpdateRela> noUpdateRelaList) {
        if (noUpdateRelaList.isEmpty()) {
            return passEmpty();
        }
//        JSONArray nodes = JSONArray.parseArray(JSON.toJSONString(noUpdateRelaList));
        JSONArray nodes = new JSONArray();
        noUpdateRelaList.parallelStream().forEach(v -> nodes.add(v.getObject()));
        String dataPackage = JSONTool.removeKeyDoubleQuotationMark(nodes);

        String cypher = "UNWIND " + dataPackage + " as row\n" +
                "MATCH (from) WHERE id(from) = TOINT(row.from)\n" +
                "MATCH (to) where id(to) = TOINT(row.to)\n" +
                "CALL apoc.merge.relationship(from, row.type, {name: row.name}, row.properties, to) yield rel\n" +
                "RETURN count(*) AS count";
        Condition condition = new Condition();
        condition.setStatement(cypher, super.contents);
        return super.chooseSendCypherWay(condition, CRUD.RETRIEVE);
    }

    /**
     * @param noUpdateRelaList
     * @param uniqueKey:用来排重关系的属性--保持与已有关系一致
     * @return
     * @Description: TODO(批量导入关系 - 不支持属性MERGE更新)★★★使用指定属性排重关系
     */
    @Override
    public JSONObject importApocMergeRelations(List<NoUpdateRela> noUpdateRelaList, String uniqueKey) {
        if (noUpdateRelaList.isEmpty()) {
            return passEmpty();
        }
        JSONArray nodes = new JSONArray();
        noUpdateRelaList.stream().forEach(v -> nodes.add(v.getObject()));
        String dataPackage = JSONTool.removeKeyDoubleQuotationMark(nodes);

        String cypher = "UNWIND " + dataPackage + " as row\n" +
                "MATCH (from) WHERE id(from) = TOINT(row.from)\n" +
                "MATCH (to) where id(to) = TOINT(row.to)\n" +
                "CALL apoc.merge.relationship(from, row.type, {" + uniqueKey + ": row.name}, row.properties, to) yield rel\n" +
                "RETURN count(*) AS count";
        Condition condition = new Condition();
        condition.setStatement(cypher, super.contents);
        return super.chooseSendCypherWay(condition, CRUD.RETRIEVE);
    }

//    public JSONObject packRelationship(Trituple trituple) {
//        // {from:473522576,type:"校友",name:"校友",properties:{name:"校友"},to:473569095}
//        // fields:from,type,name,properties,to
//        JSONObject object = new JSONObject();
//        if (trituple != null) {
//            object.put("from", trituple.getEntityOne().getEntityName());
//            object.put("type", trituple.getRelationshipName());
//            object.put("name", trituple.getRelationshipName());
//            JSONObject pro = new JSONObject();
//            pro.put("name", trituple.getRelationshipName());
//            JSONObject jsonObject = trituple.getRelationshipProperties();
//            if (jsonObject != null && !jsonObject.isEmpty()) {
//                pro.putAll(jsonObject);
//            }
//            object.put("properties", pro);
//            object.put("to", trituple.getEntityTwo().getEntityName());
//        }
//        return object;
//    }

    /**
     * @param updateNodeList:需要更新的节点列表
     * @param batchSize:每个REQUEST种STATEMENT提交的最大数量
     * @return
     * @Description: TODO(批量导入节点 - 支持属性MERGE更新)
     */
    @Override
    public void addMergeDynamicNodes(List<UpdateNode> updateNodeList, int batchSize) {
        if (updateNodeList.isEmpty()) {passEmpty();}

        final int MAX_SEND = batchSize;

        // 节点分批操作 - 将CONDITION按照BATCH拆分添加STATEMENT列表
        final double node_temp = (double) (updateNodeList.size()) / (double) MAX_SEND;
        final int node_limit = (int) Math.ceil(node_temp);
        if (this.logger.isInfoEnabled()) {
            this.logger.info("NODE SIZE:" + updateNodeList.size() + ",EXECUTE BATCH SIZE:" + node_limit);
        }

        Stream.iterate(0, n -> n + 1)
                .limit(node_limit)
//                .parallel()
                .forEach(a -> {
                    List<UpdateNode> sendList = updateNodeList.parallelStream().skip(a * MAX_SEND).limit(MAX_SEND).collect(Collectors.toList());

                    // 开始添加CONDITION
                    JSONArray nodes = JSONArray.parseArray(JSON.toJSONString(sendList));
                    String dataPackage = JSONTool.removeKeyDoubleQuotationMark(nodes);
                    String cypher = "UNWIND " + dataPackage + " AS row\n" +
                            "CALL apoc.cypher.doIt('MERGE (n:`' + row.label + '` {_unique_uuid: {_unique_uuid}}) SET n += {properties}', {properties: row.properties, _unique_uuid: row._unique_uuid}) YIELD value\n" +
                            "RETURN " + nodes.size() + "";
                    Condition condition = new Condition();
                    condition.setStatement(cypher, super.contents);
                    this.addMergeDynamicNodes.add(condition);
                    if (this.logger.isInfoEnabled()) {
                        this.logger.info("Add merge dynamic import nodes condition...");
                    }
                });
    }

    /**
     * @param updateRelaList:需要更新的关系列表
     * @param batchSize:每个REQUEST种STATEMENT提交的最大数量
     * @return
     * @Description: TODO(批量导入关系 - 支持属性MERGE更新)
     */
    @Override
    public void addMergeDynamicRelations(List<UpdateRela> updateRelaList, int batchSize) {
        if (updateRelaList.isEmpty()) {
            passEmpty();
        }

        final int MAX_SEND = batchSize;

        // 关系分批操作 - 将CONDITION按照BATCH拆分添加STATEMENT列表
        final double rela_temp = (double) (updateRelaList.size()) / (double) MAX_SEND;
        final int rela_limit = (int) Math.ceil(rela_temp);
        if (this.logger.isInfoEnabled()) {
            this.logger.info("RELATIONSHIPS SIZE:" + updateRelaList.size() + ",EXECUTE BATCH SIZE:" + rela_limit);
        }

        Stream.iterate(0, n -> n + 1)
                .limit(rela_limit)
//                .parallel()
                .forEach(a -> {
                    List<UpdateRela> sendList = updateRelaList.parallelStream().skip(a * MAX_SEND).limit(MAX_SEND).collect(Collectors.toList());

                    JSONArray relationships = JSONArray.parseArray(JSON.toJSONString(sendList));
                    String dataPackage = JSONTool.removeKeyDoubleQuotationMark(relationships);
                    String cypher = "UNWIND " + dataPackage + " AS row " +
                            "WITH split(row.from, '-SPLIT-&-')  AS fromInfo, split(row.to, '-SPLIT-&-')  AS toInfo, row " +
                            "CALL apoc.cypher.doIt('MATCH (from:`' + fromInfo[0] + '` {_unique_uuid: {fromName}}) MATCH (to:`' + toInfo[0] + '` {_unique_uuid: {toName}}) MERGE (from)-[r:`' +  row.type + '` {_entity_name: {_entity_name}}]->(to) SET r += {properties}', {fromName: fromInfo[1], toName: toInfo[1], properties: row.properties, _entity_name: row._entity_name}) YIELD value " +
                            "return " + relationships.size() + "";
                    Condition condition = new Condition();
                    condition.setStatement(cypher, super.contents);
                    this.addMergeDynamicRelations.add(condition);
                    if (this.logger.isInfoEnabled()) {
                        this.logger.info("Add merge dynamic import relationships condition...");
                    }
                });
    }

    /**
     * @param updateNodeList :需要更新的节点列表
     * @return
     * @Description: TODO(批量导入节点 - 支持属性MERGE更新)
     */
    @Override
    public void addMergeDynamicNodes(List<UpdateNode> updateNodeList) {
        if (updateNodeList.isEmpty()) {passEmpty();}
        // 开始添加CONDITION
        JSONArray nodes = JSONArray.parseArray(JSON.toJSONString(updateNodeList));
        String dataPackage = JSONTool.removeKeyDoubleQuotationMark(nodes);
        String cypher = "UNWIND " + dataPackage + " AS row\n" +
                "CALL apoc.cypher.doIt('MERGE (n:`' + row.label + '` {_unique_uuid: {_unique_uuid}}) SET n += {properties}', {properties: row.properties, _unique_uuid: row._unique_uuid}) YIELD value\n" +
                "RETURN " + nodes.size() + "";
        Condition condition = new Condition();
        condition.setStatement(cypher, super.contents);
        this.addMergeDynamicNodes.add(condition);
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Add merge dynamic import nodes condition...");
        }
    }

    /**
     * @param updateRelaList :需要更新的关系列表
     * @return
     * @Description: TODO(批量导入关系 - 支持属性MERGE更新)
     */
    @Override
    public void addMergeDynamicRelations(List<UpdateRela> updateRelaList) {
        if (updateRelaList.isEmpty()) {passEmpty();}
        JSONArray relationships = JSONArray.parseArray(JSON.toJSONString(updateRelaList));
        String dataPackage = JSONTool.removeKeyDoubleQuotationMark(relationships);

        String cypher = "UNWIND " + dataPackage + " AS row " +
                "WITH split(row.from, '-SPLIT-&-')  AS fromInfo, split(row.to, '-SPLIT-&-')  AS toInfo, row " +
                "CALL apoc.cypher.doIt('MATCH (from:`' + fromInfo[0] + '` {_unique_uuid: {fromName}}) MATCH (to:`' + toInfo[0] + '` {_unique_uuid: {toName}}) MERGE (from)-[r:`' +  row.type + '` {_entity_name: {_entity_name}}]->(to) SET r += {properties}', {fromName: fromInfo[1], toName: toInfo[1], properties: row.properties, _entity_name: row._entity_name}) YIELD value " +
                "return " + relationships.size() + "";
        Condition condition = new Condition();
        condition.setStatement(cypher, super.contents);
        this.addMergeDynamicRelations.add(condition);
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Add merge dynamic import relationships condition...");
        }
    }

    /**
     * @param nodesIds                       :节点ID列表--★★★默认列表的第一个元素是归并的节点<其它节点的关系全部归并到这个节点>
     * @param uniqueKey:用来排重关系的属性--保持与已有关系一致
     * @param isDelete                       :合并完成之后是否删除被归并节点的关系
     * @param unwindCommitSize               :批量提交的尺寸
     * @return
     * @Description: TODO(合并图谱)
     */
    @Override
    public JSONObject executeMagicIncorporateGraph(long sourceId, List<Long> nodesIds, String uniqueKey, boolean isDelete, int unwindCommitSize) {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        nodesIds.forEach(targetId -> {
            JSONObject result = executeMagicIncorporateGraph(sourceId, targetId, uniqueKey, isDelete, unwindCommitSize);
            result.put("sourceId", sourceId);
            result.put("targetId", targetId);
            array.add(result);
        });
        object.put("incorporate", array);
        return object;
    }

    /**
     * @param sourceId                       :归并的节点--★★★<被归并节点的关系全部转移到这个节点>
     * @param targetId                       :被归并的节
     * @param uniqueKey:用来排重关系的属性--保持与已有关系一致
     * @param isDelete                       :合并完成之后是否删除被归并节点和关系
     * @param unwindCommitSize               :批量提交的尺寸
     * @return
     * @Description: TODO(合并图谱)
     */
    @Override
    public JSONObject executeMagicIncorporateGraph(long sourceId, long targetId, String uniqueKey, boolean isDelete, int unwindCommitSize) {
        JSONObject targetRelationsResult = execute("MATCH (n)-[r]-(m) WHERE id(n)=" + targetId + " RETURN r,ID(m) AS id", CRUD.RETRIEVE_PROPERTIES);
        JSONArray targetRelations = targetRelationsResult.getJSONArray("retrieve_properties");
        List<NoUpdateRela> noUpdateRelationList = new ArrayList<>();
        targetRelations.forEach(v -> {
            JSONObject object = (JSONObject) v;
            JSONObject relation = object.getJSONObject("r");
            long id = object.getLongValue("id");

            long start = relation.getLongValue("startNode");
            String type = relation.getString("type");
            JSONObject properties = relation.getJSONObject("properties");

            if (start == targetId) {
                noUpdateRelationList.add(new NoUpdateRela(sourceId, RelationshipType.withName(type), type, id, properties));
            } else {
                noUpdateRelationList.add(new NoUpdateRela(id, RelationshipType.withName(type), type, sourceId, properties));
            }
        });
        // 导入本次合并
        List<List<NoUpdateRela>> listList = cutObjListByBatchSize(noUpdateRelationList, unwindCommitSize);
        JSONObject output = new JSONObject();
        JSONArray array = new JSONArray();
        listList.forEach(dataPackage -> array.add(importApocMergeRelations(dataPackage, uniqueKey)));

        output.put("unwindCommit", array);
        output.put("incorporateSize", targetRelations.size());
        // 删除被合并的节点和关系
        if (isDelete) {
            output.put("deleter", deleter(targetId));
        }
        return output;
    }

    /**
     * @param
     * @return
     * @Description: TODO(删除节点以及与节点相链接的关系)
     */
    public JSONObject deleter(long nodeId) {
        String deleteCypher = "MATCH (n) WHERE id(n)=" + nodeId + " OPTIONAL MATCH (n)-[r]-() DELETE n,r";
        return execute(deleteCypher, CRUD.DELETE);
    }

    /**
     * @param
     * @return
     * @Description: TODO(删除节点)
     */
    public JSONObject nodeDeleter(long nodeId) {
        String deleteCypher = "MATCH (n) WHERE id(n)=" + nodeId + " DELETE n";
        return execute(deleteCypher, CRUD.DELETE);
    }

    /**
     * @param
     * @return
     * @Description: TODO(删除节点)
     */
    public JSONObject nodeDeleter(List<Long> nodeIds) {
        JSONArray ids = JSONArray.parseArray(JSON.toJSONString(nodeIds));
        String deleteCypher = "MATCH (n) WHERE id(n) IN " + ids.toJSONString() + " DELETE n";
        return execute(deleteCypher, CRUD.DELETE);
    }

    /**
     * @param
     * @return
     * @Description: TODO(删除关系)
     */
    public JSONObject relationDeleter(long relationId) {
        String deleteCypher = "MATCH ()-[r]-() WHERE id(r)=" + relationId + " DELETE r";
        return execute(deleteCypher, CRUD.DELETE);
    }

    /**
     * @param
     * @return
     * @Description: TODO(删除关系)
     */
    public JSONObject relationDeleter(List<Long> relationIds) {
        JSONArray ids = JSONArray.parseArray(JSON.toJSONString(relationIds));
        String deleteCypher = "MATCH ()-[r]-() WHERE id(r) IN " + ids + " DELETE r";
        return execute(deleteCypher, CRUD.DELETE);
    }

    /**
     * @param
     * @return
     * @Description: TODO(删除与当前节点相连的关系)
     */
    public JSONObject relationAboutNodeDeleter(long nodeId) {
        String deleteCypher = "MATCH (n) WHERE id(n)=" + nodeId + " OPTIONAL MATCH (n)-[r]-() DELETE r";
        return execute(deleteCypher, CRUD.DELETE);
    }

    /**
     * @param nodeId:被检查的节点ID
     * @return
     * @Description: TODO(合并重复的关系类型 - 删除与当前节点相连的关系)
     */
    public JSONObject checkRepeatRelationType(long nodeId) {
        String repeatIdsCypher = "MATCH (n)-[r]-(m) where id(n)=" + nodeId + " WITH type(r) AS type,count(r) AS count,id(m) AS id \n" +
                "WITH CASE WHEN count>=2 \n" +
                " THEN id+'|'+type\n" +
                " ELSE NULL\n" +
                " END AS idType\n" +
                "RETURN idType";
        List<String> repeatIds = execute(repeatIdsCypher, CRUD.RETRIEVE_PROPERTIES).getJSONArray("retrieve_properties")
                .stream().map(v -> {
                    JSONObject object = (JSONObject) v;
                    return object.getString("idType");
                }).filter(Objects::nonNull).collect(Collectors.toList());

        // 保留一个关系合并，不要新建关系
        mergeRepeatRelations(nodeId, repeatIds);
        JSONObject object = new JSONObject();
        object.put("mergeRepeatRelationsSize", repeatIds.size());
        return object;
    }

    private void mergeRepeatRelations(long nodeId, List<String> repeatIds) {
        for (String idType : repeatIds) {
            String[] split = idType.split("\\|");
            long id = Long.parseLong(split[0]);
            String type = split[1];
            String rList = "MATCH (n)-[r:" + type + "]-(m) where id(n)=" + nodeId + " AND id(m)=" + id + " RETURN COLLECT(r) AS rList";
            JSONArray result = execute(rList, CRUD.RETRIEVE_PROPERTIES).getJSONArray("retrieve_properties");
            for (Object object : result) {
                JSONObject jsonObject = (JSONObject) object;
                JSONArray array = jsonObject.getJSONArray("rList");
                // 默认保留第一个关系，其它关系合并到此关系
                JSONObject remainRelation = packRemainRelation(array);
                // 更新关系
                long remainRelationId = remainRelation.getLongValue("remainRelationId");
                JSONObject mergeProperties = remainRelation.getJSONObject("mergeProperties");
                String updateCypher = "MATCH ()-[r]-() WHERE id(r)=" + remainRelationId + " SET " + CypherTool.appendProperty("r", mergeProperties);
                execute(updateCypher, CRUD.UPDATE);
                // 删除其余关系
                relationDeleter((List<Long>) remainRelation.get("deleteRepeatRelations"));
            }
        }
    }

    private JSONObject packRemainRelation(JSONArray array) {
        String relationId = null;
        JSONObject properties = new JSONObject();
        List<Long> repeatRelationIds = new ArrayList<>();
        for (Object obj : array) {
            JSONObject object = (JSONObject) obj;
            String id = object.getString("id");
            if (relationId == null) {
                relationId = id;
            } else {
                repeatRelationIds.add(Long.parseLong(id));
            }
            JSONObject pros = object.getJSONObject("properties");
            properties.putAll(pros);
        }
        JSONObject object = new JSONObject();
        object.put("remainRelationId", relationId);
        object.put("mergeProperties", properties);
        object.put("deleteRepeatRelations", repeatRelationIds);
        return object;
    }

    private JSONObject passEmpty() {
        this.logger.info("Data package is empty missing execute!");
        return new JSONObject();
    }

    /**
     * @return
     * @Description: TODO(NEO4J访问对象的全局变量重置方法)
     */
    @Override
    public void reset() {
        this.addMergeDynamicNodes.clear();
        this.addMergeDynamicRelations.clear();
        this.queryResultList.clear();
    }

    /**
     * @return
     * @Description: TODO(执行请求 - 拼接条件之后执行查询)
     */
    @Override
    public JSONObject execute() {

        /**
         * List<Condition>分开处理：每个Condition也需要控制大小
         *
         * **/

        // 先执行构建节点的请求
        long startMill = System.currentTimeMillis();

        this.addMergeDynamicNodes.stream().forEach(condition -> {
            this.queryResultList.add(Result.message(super.chooseSendCypherWay(condition, CRUD.CREATE)));
        });

        // 再执行构建关系的请求
        this.addMergeDynamicRelations.stream().forEach(condition -> {
            this.queryResultList.add(Result.message(super.chooseSendCypherWay(condition, CRUD.CREATE)));
        });

        long stopMill = System.currentTimeMillis();
        JSONObject result = Result.statistics(this.queryResultList);

        // 统计所有请求的耗时，以及每个请求的平均耗时
        result.put("consume", Result.statisticsConsume(startMill, stopMill, this.queryResultList.size(), logger));
        return result;
    }

}


