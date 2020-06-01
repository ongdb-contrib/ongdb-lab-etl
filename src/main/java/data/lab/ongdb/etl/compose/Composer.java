package data.lab.ongdb.etl.compose;
/*
 *
 * Data Lab - graph database organization.
 *
 */import data.lab.ongdb.etl.common.Accessor;
import data.lab.ongdb.etl.compose.pack.*;
import data.lab.ongdb.etl.model.Label;
import data.lab.ongdb.etl.model.RelationshipType;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.etl.compose
 * @Description: TODO(构图接口)
 * @date 2019/7/9 11:50
 */
public interface Composer extends Accessor {

    /**
     * 通用场景下使用的接口（效率较差-适合数据量较少但是结构复杂的更新创建）
     **/

    // ========================================================only cypher========================================================
    JSONObject executeImport(List<Cypher> cypherList) throws Exception;


    /**
     *
     * 非通用场景下使用的接口（每一类的数据生成CYPHER）- 必须指定字段名称 （此种指定属性字段的方式比较高效）
     *
     * **/

    // ========================================================only cypher========================================================

    /**
     * @param nodes:节点列表           Object[]中数据存入的顺序和接口参数传入字段顺序需要保持一致(String _uniqueField, String... _key)
     * @param label:节点标签
     * @param _uniqueField:合并的唯一字段
     * @param _key:MERGE的属性字段
     * @return
     * @Description: TODO(导入节点)
     */
    JSONObject executeImport(List<Object[]> nodes, Label label, String _uniqueField, String... _key) throws Exception;

    /**
     * @param nodes:节点列表             Object[]中数据存入的顺序和接口参数传入字段顺序需要保持一致(String _uniqueField, String... _key)
     * @param label:节点标签
     * @param setOtherLabels:添加额外的标签
     * @param _uniqueField:合并的唯一字段
     * @param _key:MERGE的属性字段
     * @return
     * @Description: TODO(导入节点)
     */
    JSONObject executeImport(List<Object[]> nodes, Label label, Label[] setOtherLabels, String _uniqueField, String... _key) throws Exception;

    /**
     * @param relations:关系列表          Object[]中数据存入的顺序和接口参数传入字段顺序需要保持一致(String _uniqueFieldStart, String _uniqueFieldEnd, String... _key)
     * @param relationshipType:生成的关系名
     * @param startNodeLabel:起始节点的标签
     * @param _uniqueFieldStart:开始节点
     * @param endNodeLabel:结束节点的标签
     * @param _uniqueFieldEnd:结束节点
     * @param _key:MERGE的属性字段
     * @return
     * @Description: TODO(导入关系)
     */
    JSONObject executeImport(List<Object[]> relations, RelationshipType relationshipType, Label startNodeLabel,
                             Label endNodeLabel, String _uniqueFieldStart, String _uniqueFieldEnd, String... _key) throws Exception;


    // ========================================================only cypher - http csv========================================================

    /**
     * @param csvName:CSV文件名
     * @param _keyFields:CSV文件头
     * @return
     * @Description: TODO(生成CSV文件头 - 覆盖写)
     */
    @Deprecated
    void writeCsvHeader(String csvName, String... _keyFields) throws Exception;

    /**
     * @param csvName:CSV文件名
     * @param row:写入的一行数据（生成的一行数据需要与文件头的字段数据一一对应）
     * @return
     * @Description: TODO(生成CSV文件体 - 追加写)
     */
    void writeCsvBody(String csvName, String row) throws Exception;

    /**
     * @param
     * @return
     * @Description: TODO(根据文件名删除CSV文件)
     */
    void deleteCsv(String csvName) throws Exception;

    /**
     * @param commitBatchSize:批量提交数量
     * @param csvName:CSV文件名（数据写入CSV的顺序需要和方法传入参数顺序保持一致）String _uniqueField, String... _key
     * @param label:节点标签
     * @param _uniqueField:合并的唯一字段
     * @param _key:MERGE的属性字段
     * @return
     * @Description: TODO(导入节点CSV)
     */
    JSONObject executeImportCsv(int commitBatchSize, String csvName, Label label, String _uniqueField, String... _key) throws Exception;

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
    JSONObject executeImportCsv(int commitBatchSize, String csvName, RelationshipType relationshipType, Label startNodeLabel, Label endNodeLabel,
                                String _uniqueFieldStart, String _uniqueFieldEnd, String... _key) throws Exception;

    /**
     *
     * 通用构图场景下使用 - 不需要单独指定字段名称（具有更强的自定义特性适合动态批量导入属性，不需要指定属性字段）
     *
     * **/

    //========================================================apoc.merge.*========================================================

    /**
     * @param
     * @return
     * @Description: TODO(批量导入节点 - 不支持属性MERGE更新)
     */
    JSONObject importApocMergeNodes(List<NoUpdateNode> noUpdateNodeList) throws Exception;

    /**
     * @param uniqueFieldName:指定字段名
     * @return
     * @Description: TODO(批量导入节点 - 不支持属性MERGE更新)
     */
    JSONObject importApocMergeNodes(List<NoUpdateNode> noUpdateNodeList, String uniqueFieldName) throws Exception;

    /**
     * @param
     * @return
     * @Description: TODO(批量导入关系 - 不支持属性MERGE更新)★★★默认使用name属性排重关系
     */
    JSONObject importApocMergeRelations(List<NoUpdateRela> noUpdateRelaList) throws Exception;

    /**
     * @param
     * @param uniqueKey:用来排重关系的属性--保持与已有关系一致
     * @return
     * @Description: TODO(批量导入关系 - 不支持属性MERGE更新)★★★使用指定属性排重关系
     */
    JSONObject importApocMergeRelations(List<NoUpdateRela> noUpdateRelaList, String uniqueKey) throws Exception;

    //========================================================apoc.cypher.doIt.*========================================================
    // ★★★★★--EXECUTE METHOD TO RUN
    // ☆☆☆☆☆--MUST TO BE SET

    /**
     * @param updateNodeList:需要更新的节点列表
     * @param batchSize:每个REQUEST种STATEMENT提交的最大数量
     * @return
     * @Description: TODO(批量导入节点 - 支持属性MERGE更新)
     */
    void addMergeDynamicNodes(List<UpdateNode> updateNodeList, int batchSize) throws Exception;

    /**
     * @param updateRelaList:需要更新的关系列表
     * @param batchSize:每个REQUEST种STATEMENT提交的最大数量
     * @return
     * @Description: TODO(批量导入关系 - 支持属性MERGE更新)
     */
    void addMergeDynamicRelations(List<UpdateRela> updateRelaList, int batchSize) throws Exception;

    /**
     * @param updateNodeList:需要更新的节点列表
     * @return
     * @Description: TODO(批量导入节点 - 支持属性MERGE更新)
     */
    void addMergeDynamicNodes(List<UpdateNode> updateNodeList) throws Exception;

    /**
     * @param updateRelaList:需要更新的关系列表
     * @return
     * @Description: TODO(批量导入关系 - 支持属性MERGE更新)
     */
    void addMergeDynamicRelations(List<UpdateRela> updateRelaList) throws Exception;

    //========================================================other composers========================================================

    /**
     * @param sourceId:归并的节点--★★★<被归并节点的关系全部转移到这个节点>
     * @param nodesIds:节点ID列表--★★★<其它节点的关系全部归并到sourceId这个节点>
     * @param uniqueKey:用来排重关系的属性--保持与已有关系一致
     * @param isDelete:合并完成之后是否删除被归并节点的关
     * @param unwindCommitSize:批量提交的尺寸
     * @return
     * @Description: TODO(合并图谱)
     */
    JSONObject executeMagicIncorporateGraph(long sourceId, List<Long> nodesIds, String uniqueKey, boolean isDelete, int unwindCommitSize) throws Exception;

    /**
     * @param sourceId:归并的节点--★★★<被归并节点的关系全部转移到这个节点>
     * @param targetId:被归并的节点
     * @param uniqueKey:用来排重关系的属性--保持与已有关系一致
     * @param isDelete:合并完成之后是否删除被归并节点的关系
     * @param unwindCommitSize:批量提交的尺寸
     * @return
     * @Description: TODO(合并图谱)
     */
    JSONObject executeMagicIncorporateGraph(long sourceId, long targetId, String uniqueKey, boolean isDelete, int unwindCommitSize) throws Exception;
}


