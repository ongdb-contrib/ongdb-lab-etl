package data.lab.ongdb.index;

import data.lab.ongdb.common.Labels;
import data.lab.ongdb.model.Label;
import data.lab.ongdb.model.RelationshipType;
import data.lab.ongdb.search.NeoSeeker;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.index
 * @Description: TODO(Neo index test)
 * @date 2019/7/18 15:41
 */
public class NeoIndexerTest {

    // BLOT协议
    private final static String ipPorts = "192.168.12.19:7687";

    private NeoIndexer neoIndexer = new NeoIndexer(ipPorts, "neo4j", "123456");

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.properties");
    }

    @Test
    public void addNodeFieldIndexTest1() {
        /**
         * @param label     :标签名
         * @param fieldName :字段名称
         * @return
         * @Description: TODO(给某个标签增加单属性索引)
         */
        neoIndexer.addNodeFieldIndex(Label.label("专题事件"), "_unique_uuid", "_entity_name", "eid");
        System.out.println(neoIndexer.execute());
        neoIndexer.reset();
    }

    @Test
    public void addNodeFieldIndexTest2() {
        /**
         * @param label     :标签名
         * @param fieldName :可以传入多个字段名
         * @return
         * @Description: TODO(给某个标签增加复合索引)
         */
        neoIndexer.addNodeFieldIndex(Label.label("虚拟账号"), "_unique_uuid", "_entity_name", "user_id", "data_type");
        System.out.println(neoIndexer.execute());
        neoIndexer.reset();
    }

    /**
     * @param
     * @return
     * @Description: TODO(关系单属性索引)
     */
    @Test
    public void addRelationFieldIndexTest1() {
        neoIndexer.addRelationFieldIndex(RelationshipType.withName("参与组织"), "_unique_uuid");
        System.out.println(neoIndexer.execute());
        neoIndexer.reset();
    }

    /**
     * @param
     * @return
     * @Description: TODO(关系复合索引)
     */
    @Test
    public void addRelationFieldIndexTest2() {
        neoIndexer.addRelationFieldIndex(RelationshipType.withName("参与组织"), "_unique_uuid", "_entity_name");
        System.out.println(neoIndexer.execute());
        neoIndexer.reset();
    }

    /**
     * @param
     * @return
     * @Description: TODO(关系复合索引 - 指定关系匹配模式进行索引)
     */
    @Test
    public void addRelationFieldIndexTest3() {
        String autoMatchRelation = "MATCH (:组织)-[r:参与组织]-(:虚拟账号) ";
        neoIndexer.addRelationFieldIndex(autoMatchRelation, RelationshipType.withName("参与组织"), "_unique_uuid", "_entity_name");
        neoIndexer.setDEBUG(true);
        System.out.println(neoIndexer.execute());
        neoIndexer.reset();
    }

    @Test
    public void relationPattern() {
        System.out.println(NeoSeeker.relationPattern()
                .setStartLabel(Label.label("Test"))
                .setRelation(RelationshipType.withName("LIKE"))
                .setEndLabel(Label.label("Movie"))
                .toPattern());
        System.out.println(NeoSeeker.relationPattern()
                .setStartLabel(Label.label("Test"))
                .setEndLabel(Label.label("Movie"))
                .toPattern());
        System.out.println(NeoSeeker.relationPattern()
                .setStartLabel(Label.label("Test"))
                .toPattern());
        System.out.println(NeoSeeker.relationPattern()
                .toPattern());
    }

    /**
     * @param
     * @return
     * @Description: TODO(关系复合索引 - 指定关系匹配模式进行索引)
     */
    @Test
    public void addRelationFieldIndexTest4() {
        String autoMatchRelation = NeoSeeker.relationPattern()
                .setStartLabel(Label.label("Test"))
                .setEndLabel(Label.label("Movie"))
                .toPattern();
        neoIndexer.addRelationFieldIndex(autoMatchRelation, RelationshipType.withName("参与组织"), "_unique_uuid", "_entity_name");
        neoIndexer.setDEBUG(true);
        System.out.println(neoIndexer.execute());
        neoIndexer.reset();
    }

    @Test
    public void addNodeFullTextSearch() {
        /**
         * @param fullTextSearchName:全文检索名称-在创建好全文检索之后搜索时使用
         * @param fullTextMap:为节点增加全文检索属性
         * @param autoUpdate:是否配置全文检索的自动更新
         * @return
         * @Description: TODO(给节点增加全文检索)
         */
        neoIndexer.addNodeFullTextSearch("search", FullTextMap.initFullTextNode()
                .add(Labels.专题事件, "name", "eid")
                .add(Labels.新浪微博ID, "name"), true);
        neoIndexer.setDEBUG(true);
        System.out.println(neoIndexer.execute());
        neoIndexer.reset();
    }

    @Test
    public void addNodeFullTextSearchAutoUpdate() {
        /**
         * @param fullTextSearchName :全文检索名称-在创建好全文检索之后搜索时使用
         * @param fullTextMap        :为节点增加全文检索属性
         * @Description: TODO(给节点增加全文检索)
         */
        neoIndexer.addNodeFullTextSearchAutoUpdate("test", FullTextMap.initFullTextNode()
                .add(Labels.专题事件, "name", "eid")
                .add(Labels.新浪微博ID, "name")
        );
        neoIndexer.setDEBUG(true);
        System.out.println(neoIndexer.execute());
        neoIndexer.reset();
    }

//    CALL db.index.fulltext.createNodeIndex('nodesProperties',["公司","YouTubeID","专题事件","重点人组织","新浪微博发帖","Twitter发帖","FacebookID","新浪微博ID","InstagramID","TwitterID","学校","认证机构","Instagram发帖","Facebook发帖","其它组织","志愿机构","现实人员","颁发机构","YouTube发帖","LinkedinID"],["nameNodeSpace","eid","gid"])
//
//    CALL db.index.fulltext.createNodeIndex('重点人组织', ["重点人组织"],["nameNodeSpace","gid"]);
//    CALL db.index.fulltext.createNodeIndex('专题事件',["专题事件"],["nameNodeSpace","eid"]);
//    CALL db.index.fulltext.createNodeIndex('其它组织', ["其它组织"],["nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('公司',["公司"],["nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('学校', ["学校"],["nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('认证机构', ["认证机构"],["nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('颁发机构', ["颁发机构"],["nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('志愿机构', ["志愿机构"],["nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('新浪微博发帖',["新浪微博发帖"],["content","nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('Instagram发帖', ["Instagram发帖"],["content","nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('Twitter发帖', ["Twitter发帖"],["nameNodeSpace","content"]);
//    CALL db.index.fulltext.createNodeIndex('YouTube发帖', ["YouTube发帖"],["nameNodeSpace","content"]);
//    CALL db.index.fulltext.createNodeIndex('Facebook发帖',["Facebook发帖"],["content","nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('现实人员',["现实人员"],["nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('InstagramID', ["InstagramID"],["nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('YouTubeID', ["YouTubeID"],["nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('LinkedinID', ["LinkedinID"],["nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('FacebookID', ["FacebookID"],["nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('TwitterID', ["TwitterID"],["nameNodeSpace"]);
//    CALL db.index.fulltext.createNodeIndex('新浪微博ID',["新浪微博ID"],["nameNodeSpace"]);
//
//    CALL db.index.fulltext.queryNodes('nodesProperties', '中国共产党') YIELD node RETURN node SKIP 0 LIMIT 10;
//    CALL db.index.fulltext.queryNodes('新浪微博ID', '中国共产党') YIELD node RETURN node SKIP 0 LIMIT 10;

    @Test
    public void addNodeFullTextSearchAutoUpdate_02() {
        /**
         * @param fullTextSearchName :全文检索名称-在创建好全文检索之后搜索时使用
         * @param fullTextMap        :为节点增加全文检索属性
         * @Description: TODO(给节点增加全文检索)
         */
        neoIndexer.addNodeFullTextSearchAutoUpdate("nodesProperties", FullTextMap.initFullTextNode()
                .add(Labels.专题事件, "nameNodeSpace", "eid")
                .add(Labels.重点人组织, "nameNodeSpace", "gid")
                .add(Labels.其它组织, "nameNodeSpace")
                .add(Labels.公司, "nameNodeSpace")
                .add(Labels.学校, "nameNodeSpace")
                .add(Labels.认证机构, "nameNodeSpace")
                .add(Labels.颁发机构, "nameNodeSpace")
                .add(Labels.志愿机构, "nameNodeSpace")
                .add(Labels.新浪微博发帖, "nameNodeSpace")
                .add(Labels.Instagram发帖, "nameNodeSpace")
                .add(Labels.Twitter发帖, "nameNodeSpace")
                .add(Labels.YouTube发帖, "nameNodeSpace")
                .add(Labels.Facebook发帖, "nameNodeSpace")
                .add(Labels.现实人员, "nameNodeSpace")
                .add(Labels.InstagramID, "nameNodeSpace")
                .add(Labels.YouTubeID, "nameNodeSpace")
                .add(Labels.LinkedinID, "nameNodeSpace")
                .add(Labels.FacebookID, "nameNodeSpace")
                .add(Labels.TwitterID, "nameNodeSpace")
                .add(Labels.新浪微博ID, "nameNodeSpace")
        );
        neoIndexer.setDEBUG(true);
        System.out.println(neoIndexer.execute());
        neoIndexer.reset();
    }

    @Test
    public void dropFullText() {
        /**
         * @param fullTextSearchName :创建的全文检索名称
         * @return
         * @Description: TODO(删除创建的全文检索接口)
         */
        System.out.println(neoIndexer.dropFullText("test"));
    }

    @Test
    public void addUniqueFieldIndex() {
        neoIndexer.addNodeFieldUniqueIndex(Label.label("Test"), "id");
        neoIndexer.setDEBUG(true);
        System.out.println(neoIndexer.execute());
        neoIndexer.reset();
    }

    @Test
    public void db() {
        // 查看已有索引
        System.out.println(neoIndexer.dbIndexes());
        // 查看已有标签
        System.out.println(neoIndexer.dbLabels());
        // 查看已有关系类型
        System.out.println(neoIndexer.dbRelationshipTypes());
    }

    @Test
    public void addIndex() {
        // 获取库中所有标签，然后添加索引
        JSONArray labels = neoIndexer.dbLabels().getJSONArray("retrieve_properties");
        neoIndexer.setDEBUG(true);
        for (Object obj : labels) {
            JSONObject object = (JSONObject) obj;
            String label = object.getString("label");
            neoIndexer.addNodeFieldIndex(Label.label(label), "nameNodeSpace");
        }
        neoIndexer.execute();
        neoIndexer.reset();
    }
}


