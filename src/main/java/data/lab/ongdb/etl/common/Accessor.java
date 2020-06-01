package data.lab.ongdb.etl.common;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import com.alibaba.fastjson.JSONObject;

/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.etl.common
 * @Description: TODO(ONgDB COMPOSER增删改的接口的所有父类接口)
 * @date 2019/7/9 19:20
 */
public interface Accessor {

    /**
     * @param
     * @return
     * @Description: TODO(NEO4J访问对象的全局变量重置方法)
     */
    void reset() throws Exception;

    /**
     * @param
     * @return
     * @Description: TODO(执行请求 - 拼接请求之后执行 - 默认返回节点或者关系的所有属性字段)
     */
    JSONObject execute() throws Exception;

    /**
     * @param
     * @return
     * @Description: TODO(跳过条件添加直接使用CYPHER查询 - 默认返回节点或者关系的所有属性字段)
     */
    JSONObject execute(String cypher, CRUD crudType) throws Exception;

    /**
     * invoke cypherAction in batched transactions being feeded from cypherIteration running in main thread
     *
     * @param
     * @return
     * @Description: TODO(使用迭代器执行CYPHER -
     *apoc.periodic.iterate ( ' statement returning items ',
     *' statement per item ',
     * {batchSize:1000,iterateList:true,parallel:false,params:{},concurrency:50,retries:0})
     * YIELD batches, total - run the second statement for each item returned by the first statement. Returns number of batches and total processed rows)
     */
    JSONObject executeIterate(String cypherIterate, String cypherAction, Object... config) throws Exception;

    /**
     * @param
     * @return
     * @Description: TODO(查看已有索引)
     */
    JSONObject dbIndexes() throws Exception;

    /**
     * @param
     * @return
     * @Description: TODO(查看已有标签)
     */
    JSONObject dbLabels() throws Exception;

    /**
     * @param
     * @return
     * @Description: TODO(查看已有关系类型)
     */
    JSONObject dbRelationshipTypes() throws Exception;

//    JSONObject getAllIndexes();
//
//    JSONObject getAllLabels();
//
//    JSONObject getAllRelationships();
//
//    JSONObject getAllProperties();
//
//    JSONObject countNodes();
//
//    JSONObject countRelationships();
//
//    JSONObject getDbmsFunctions();
//
//    JSONObject getDbSchema();
//
//    JSONObject getDbmsProcedures();

//    JSONObject getNeoPlugin();

}

