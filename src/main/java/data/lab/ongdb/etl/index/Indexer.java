package data.lab.ongdb.etl.index;
/*
 *
 * Data Lab - graph database organization.
 *
 */import data.lab.ongdb.etl.model.Label;
import data.lab.ongdb.etl.model.RelationshipType;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.etl.index
 * @Description: TODO(索引接口)
 * @date 2019/7/10 18:43
 */
public interface Indexer {

    /**
     * 1、单属性索引
     * 2、复合索引
     * 3、全文索引
     */

    /**
     * @param label:标签名
     * @param fieldName:可以传入多个字段名
     * @return
     * @Description: TODO(给某个标签增索引 - 支持单属性索引和复合索引)
     */
    void addNodeFieldIndex(Label label, String... fieldName);

    /**
     * @param label:标签名
     * @param fieldName:字段名
     * @return
     * @Description: TODO(唯一索引)
     */
    void addNodeFieldUniqueIndex(Label label, String fieldName);

    /**
     * @param relationshipType:关系名称
     * @param fieldName:被索引字段
     * @return
     * @Description: TODO(某个关系增加索引 - 支持单属性索引和复合索引 - 使用默认的关系索引模式)
     */
    void addRelationFieldIndex(RelationshipType relationshipType, String... fieldName);

    /**
     * @param autoMatchRelationCypher:匹配的关系模式
     * @param relationshipType:关系名称
     * @param fieldName:被索引字段
     * @return
     * @Description: TODO(某个关系增加索引 - 支持单属性索引和复合索引 - 使用指定的关系索引模式)
     */
    void addRelationFieldIndex(String autoMatchRelationCypher, RelationshipType relationshipType, String... fieldName);

    /**
     * @param fullTextSearchName:全文检索名称-在创建好全文检索之后搜索时使用
     * @param fullTextMap:为节点增加全文检索属性
     * @param autoUpdate:是否配置全文检索的自动更新
     * @return
     * @Description: TODO(给节点增加全文检索)
     */
    void addNodeFullTextSearch(String fullTextSearchName, FullTextMap.FullTextNodeMap fullTextMap, boolean autoUpdate);

    /**
     * @param fullTextSearchName:全文检索名称-在创建好全文检索之后搜索时使用
     * @param fullTextMap:为节点增加全文检索属性
     * @Description: TODO(给节点增加全文检索)
     */
    void addNodeFullTextSearchAutoUpdate(String fullTextSearchName, FullTextMap.FullTextNodeMap fullTextMap);

    /**
     * @param fullTextSearchName:创建的全文检索名称
     * @return
     * @Description: TODO(删除创建的全文检索接口)
     */
    JSONObject dropFullText(String fullTextSearchName);
}



