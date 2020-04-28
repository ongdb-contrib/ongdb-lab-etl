package data.lab.ongdb.search;

import data.lab.ongdb.common.*;
import data.lab.ongdb.model.Label;
import data.lab.ongdb.model.RelationshipType;
import data.lab.ongdb.search.analyzer.NeoAnalyzer;
import data.lab.ongdb.util.JSONTool;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

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

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.search
 * @Description: TODO(NEO4j检索接口测试)
 * @date 2019/7/11 9:36
 */
public class NeoSearcherTest {

    //    private final static String ipPorts = "localhost:7474,192.168.12.19:7474";

    // HTTP协议
    private final static String ipPorts = "192.168.12.19:7474";

    // BLOT协议
//    private final static String ipPorts = "localhost:7687";

    private static NeoSearcher searcher;

    @Before
    public void setUp() throws Exception {

        PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.properties");

        // NEO4J检索接口的四种构造函数

        /**
         * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
         * @param authAccount:节点的用户名
         * @param authPassword:节点用户名密码
         * @return
         * @Description: TODO(构造函数 - 默认使用JAVA - DRIVER发送请求 ， D3_GRAPH格式返回数据)
         */
//        searcher = new NeoSearcher(ipPorts, "neo4j", "123456");

//        /**
//         * @param contents:指定图数据使用什么格式返回，默认GRAPH格式返回
//         * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
//         * @param authAccount:节点的用户名
//         * @param authPassword:节点用户名密码
//         * @return
//         * @Description: TODO(构造函数 - 默认使用HTTP - API发送请求)
//         * 支持多种格式返回数据ROW/GRAPH/ROW_GRAPH/D3_GRAPH
//         */
//        searcher = new NeoSearcher(AccessOccurs.RESTFUL_API, ipPorts, "neo4j", "123456");
//
//        /**
//         * @param accessOccurs:选择用哪种方式与NEO4J进行交互
//         * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
//         * @param authAccount:节点的用户名
//         * @param authPassword:节点用户名密码
//         * @return
//         * @Description: TODO(构造函数)
//         * RESTFUL_API（使用HTTP访问（支持多种格式返回数据ROW/GRAPH/ROW_GRAPH/D3_GRAPH）
//         * JAVA_DRIVER（使用JAVA-DRIVER访问（支持一种格式返回数据D3_GRAPH）
//         */
////        searcher = new NeoSearcher(ResultDataContents.GRAPH, ipPorts, "neo4j", "123456");
//
//        /**
//         * @param contents:指定图数据使用什么格式返回，默认GRAPH格式返回
//         * @param accessOccurs:选择用哪种方式与NEO4J进行交互
//         * @param ipPorts:服务节点的地址列表（IP:PORT）多地址使用逗号隔开
//         * @param authAccount:节点的用户名
//         * @param authPassword:节点用户名密码
//         * @return
//         * @Description: TODO(构造函数)
//         * RESTFUL_API（使用HTTP访问（支持多种格式返回数据ROW/GRAPH/ROW_GRAPH/D3_GRAPH）
//         * JAVA_DRIVER（使用JAVA-DRIVER访问（支持一种格式返回数据D3_GRAPH）
//         */
        searcher = new NeoSearcher(ResultDataContents.GRAPH, AccessOccurs.RESTFUL_API, ipPorts, "neo4j", "123456");
////        searcher = new NeoSearcher(ResultDataContents.ROW, AccessOccurs.RESTFUL_API, ipPorts, "neo4j", "123456");
////        searcher = new NeoSearcher(ResultDataContents.ROW_GRAPH, AccessOccurs.RESTFUL_API, ipPorts, "neo4j", "123456");
////        searcher = new NeoSearcher(ResultDataContents.D3_GRAPH, AccessOccurs.RESTFUL_API, ipPorts, "neo4j", "123456");
////        searcher = new NeoSearcher(ResultDataContents.D3_GRAPH, AccessOccurs.JAVA_DRIVER, ipPorts, "neo4j", "123456");
    }

    @After
    public void tearDown() throws Exception {
        searcher.close();
    }

    /**
     * 检索接口查询优先级：
     *
     * 节点查询（存在节点查询条件）>关系查询（无节点查询条件存在关系查询条件）>路径查询（无节点/无关系查询条件/存在路径查询条件）
     *
     * searcher.setStart(10);
     * searcher.setRow(100);
     * 出于安全与检索效率的考虑：接口使用时这两个方法必须设置（除了直接用节点或关系ID检索外的其它检索方式），不设置时使用默认值skip=0,limit=10
     *
     * **/

    /**
     * ====================================================节点检索接口测试====================================================
     **/
    @Test
    public void addNodeId() {
        /**
         * @param id :节点ID-NEO4J自动生成的ID
         * @return
         * @Description: TODO(通过节点ID查找节点)
         */
        searcher.addNodeId(123);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    @Test
    public void addNodeLabel() {
        /**
         * @param label :节点标签
         * @return
         * @Description: TODO(节点标签 - 通过节点标签检索节点) - 添加多个标签是与的关系
         */
        searcher.addNodeLabel(Label.label("虚拟账号"));
//        searcher.addNodeLabel(Label.label("组织"));
        searcher.setStart(0);
        searcher.setRow(1000);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    @Test
    public void addNodeLabelAndaddNodeProperties() {

        // 通过节点标签和节点属性检索节点
        searcher.addNodeLabel(Label.label("虚拟账号"));
        /**
         * @param key   :属性KEY
         * @param value :属性VALUE
         * @return
         * @Description: TODO(通过节点属性检索节点)
         */
        searcher.addNodeProperties("_unique_uid", "2sa23dwqwtrtr5qwedcdsfsaas");
//        searcher.addNodeProperties("mid", 3412434);
        searcher.addNodeProperties("_entity_name", "社会热点播报");

        searcher.setStart(0);
        searcher.setRow(1000);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    @Test
    public void addNodeProperties() {
        // 尽量避免仅仅使用属性检索或者用没有索引的属性检索-提高效率（可以增加标签来检索）
        searcher.addNodeProperties("_unique_uid", "2sa23dwqwtrtr5qwedcdsfsaas");
        searcher.setStart(0);
        searcher.setRow(1000);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    /**
     * ====================================================关系检索接口====================================================
     **/
    @Test
    public void addRelationId() {
        /**
         * @param id :关系ID-NEO4J自动生成的ID
         * @return
         * @Description: TODO(通过关系ID查找关系)
         */
        searcher.addRelationId(10);
        searcher.addRelationId(12);
        JSONObject result = searcher.execute();
        System.out.println(result);
    }

    @Test
    public void addRelationType() {
        /**
         * @param relationshipType :关系类型名
         * @return
         * @Description: TODO(通过关系类型检索关系)
         */
        searcher.addRelationType(RelationshipType.withName("命中关键词"));
//        searcher.addRelationProperties("test","test");
        JSONObject result = searcher.execute();
        System.out.println(result);
    }

    /**
     * ====================================================节点关系同时检索-PATH-子图检索接口====================================================
     *
     * **/
    /**
     * @param
     * @return
     * @Description: TODO(节点与关系同时过滤 - 检索路径)
     */
    @Test
    public void setStartNodeConditionTest1() {
        /**
         * 检索开始节点ID是0，关系层数是N的关系。(返回PATH包含关系和节点)。N层关系检索
         */
        searcher.addPathStartNodeId(0);
//        searcher.addPathLength(1);
        searcher.addPathLength(4);
        searcher.setStart(10);
        searcher.setRow(100);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    /**
     * @param
     * @return
     * @Description: TODO(节点与关系同时过滤 - 检索路径)
     */
    @Test
    public void setStartNodeConditionTest2() {
        /**
         * 检索开始节点ID是0，关系层数是N的关系。(返回PATH包含关系和节点)。N层关系检索
         */
        searcher.addPathStartNodeLabel(Label.label("虚拟账号"));
        searcher.addPathStartNodeProperties("_unique_uid", "2sa23dwqwtrtr5qwedcdsfsaas");

        searcher.addPathLength(4);
        searcher.setStart(0);
        searcher.setRow(100);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    /**
     * @param
     * @return
     * @Description: TODO(节点与关系同时过滤 - 检索路径)
     */
    @Test
    public void setStartNodeConditionTest3() {
        /**
         * 检索开始节点ID是0，关系层数是N的关系。(返回PATH包含关系和节点)。N层关系检索
         */
        searcher.addPathStartNodeLabel(Label.label("虚拟账号"));
        searcher.addPathStartNodeProperties("_unique_uid", "2sa23dwqwtrtr5qwedcdsfsaas");

        searcher.addPathEndNodeProperties("_unique_uid", "2sa234sdwqwtrrtr5qwedcdsfsaas");
        searcher.addPathEndNodeProperties("_entity_name", "热点传播");

        searcher.addPathLength(4);
        searcher.setStart(0);
        searcher.setRow(100);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    /**
     * @param
     * @return
     * @Description: TODO(节点与关系同时过滤 - 检索路径)
     */
    @Test
    public void setStartNodeConditionTest4() {
        // 设置开始节点ID
        searcher.addPathStartNodeId(375166807);

        // 设置结束节点标签
        searcher.addPathEndNodeLabel(Label.label("组织"));

        // 设置路径长度
        searcher.addPathLength(1);
        searcher.setStart(0);
        searcher.setRow(100);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    /**
     * @param
     * @return
     * @Description: TODO(节点与关系同时过滤 - 检索路径)
     */
    @Test
    public void setStartNodeConditionTest5() {
        // 设置开始节点ID
        searcher.addPathStartNodeId(375166807);

        // 设置结束节点ID
        searcher.addPathEndNodeId(375184813);

        // 设置路径长度
        searcher.addPathLength(4);
        searcher.setStart(0);
        searcher.setRow(100);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    /**
     * @param
     * @return
     * @Description: TODO(节点与关系同时过滤 - 检索路径)
     */
    @Test
    public void setStartNodeConditionTest6() {

        // 设置结束节点标签
        searcher.addPathStartNodeLabel(Label.label("组织"));

        /// 设置结束节点标签
        searcher.addPathEndNodeLabel(Label.label("虚拟账号"));

        // 设置路径长度
        searcher.addPathLength(4);
        searcher.setStart(0);
        searcher.setRow(100);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    /**
     * @param
     * @return
     * @Description: TODO(节点与关系同时过滤 - 检索路径)
     */
    @Test
    public void setStartNodeConditionTest7() {
        // 设置开始节点ID
        searcher.addPathStartNodeId(375166807);

        // 设置结束节点标签
        searcher.addPathEndNodeLabel(Label.label("专题事件"));

        // 路径中关系的出现情况
//        searcher.addPathMidSideRelationType(RelationshipType.withName("发帖"), RelationOccurs.MUST_CONTAINS);
//        searcher.addPathMidSideRelationType(RelationshipType.withName("命中关键词"), RelationOccurs.MUST_CONTAINS);

        // 设置路径长度
        searcher.addPathLength(1);
        searcher.setStart(0);
        searcher.setRow(100);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    /**
     * @param
     * @return
     * @Description: TODO(节点与关系同时过滤 - 检索路径)
     */
    @Test
    public void setStartNodeConditionTest8() {
        // 账号 事件 组织初始化
        // 设置开始节点ID
        searcher.addPathStartNodeLabel(Label.label("专题事件"));
        searcher.addPathStartNodeProperties("eid", "560");

        // 设置结束节点标签
        searcher.addPathEndNodeLabel(Label.label("组织"));

        // 设置路径长度
        searcher.addPathLength(3);
        searcher.setStart(0);
        searcher.setRow(100);
        searcher.setDEBUG(true);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    @Test
    public void intitMinyunGraph() {
        searcher.setDEBUG(true);
        JSONObject result = searcher.execute(new StringBuilder()
                        .append("MATCH p=(n:专题事件)<-[:参与事件]-(:虚拟账号)-[:参与组织]-(:组织) ")
                        .append("WHERE n.eid='560' OR n.eid=560 ")
                        .append("RETURN p LIMIT 100 ").toString(),
                CRUD.RETRIEVE);

//        JSONObject result = searcher.execute(new StringBuilder()
//                        .append("MATCH p=(n:专题事件)<-[:参与事件]-(:虚拟账号) ")
//                        .append("WHERE n.eid='560' OR n.eid=560 ")
//                        .append("RETURN p LIMIT 100 ").toString(),
//                CRUD.RETRIEVE);

        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(节点与关系同时过滤 - 检索路径)
     */
    @Test
    public void setStartNodeConditionTest9() {
        // 扩展组织者 传播者 参与者
        // 设置开始节点ID
        searcher.addPathStartNodeId(385424719);

        // 设置排序条件
        searcher.setPathEndNodeSort("interactiveHotEventCount", SortOrder.DESC);

        // 设置路径长度
        searcher.addPathLength(1);

        // 组织者
//        searcher.setStart(0);
//        searcher.setRow(1);

        // 传播者
//        searcher.setStart(1);
//        searcher.setRow(2);

        // 参与者
//        searcher.setStart(2);
//        searcher.setRow(1000);

        searcher.setDEBUG(true);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    @Test
    public void searchPathTest1() {

        while (true) {
            // 设置EID与标签
            searcher.addPathStartNodeLabel(Label.label("专题事件"));
//        searcher.addPathStartNodeProperties("eid", 12);
            searcher.addPathStartNodeProperties("eid", "560");
//            searcher.addPathStartNodeProperties("eid", 560);

            // 或者直接用节点ID查询
//        searcher.addPathStartNodeId(375184813);
//        searcher.addPathStartNodeId(379962851);
//        searcher.addPathStartNodeId(379962831); // 560

            // 设置路径中节点一定要包含的标签
            // 路径必须经过组织类节点
            searcher.addPathMidSideNodeLabel(Label.label("组织"), LabelOccurs.MUST_CONTAINS);
            // 路径必须经过虚拟账号类节点
            searcher.addPathMidSideNodeLabel(Label.label("虚拟账号"), LabelOccurs.MUST_CONTAINS);
            // 路径必须经过专题事件类节点
//        searcher.addPathMidSideNodeLabel(Label.label("专题事件"), LabelOccurs.MUST_CONTAINS);

            // 设置搜索路径深度
            searcher.addPathLength(3);

            // 设置虚拟图匹配模式，被虚拟的路径将会在返回路径中移除掉
            /**
             * @param isVirtualGraph:是否返回虚拟图
             * @param startMergeNodeLabel:虚拟图开始的标签类型-当前类型的标签数组
             * @param endMergeNodeLabel:虚拟图结束的标签类型-当前类型的标签数组
             * @param virtualRelationshipType:生成虚拟图的关系名称
             * @return 返回的数据中会将被虚拟的关系路径移除掉
             * @Description: TODO(是否返回虚拟图 - 虚拟图的生成从开始结束类型节点忽略中间所有的关系和路径 ， 直接根据上一次匹配的数据进行生成新的路径)
             * <p>
             * P1=(n:虚拟账号)-[]-(f:发帖)-[]-(m:专题事件)-[]-(h:帖子)
             * <p>
             * addPathGraphIsVirtual(true, new Label[](Label.label("虚拟账号")), new Label[](Label.label("专题事件")))
             * addVirtualP2=(n:虚拟账号)-[]-(m:专题事件)
             */
            // 此功能可以将与事件相连的帖子隐藏
            searcher.addPathGraphIsVirtual(true, Label.label("虚拟账号"), Label.label("专题事件"), RelationshipType.withName("参与事件"), Label.label("帖子"));

            // 设置搜索的关系路径数量
            searcher.setStart(0);
            searcher.setRow(1000);

            searcher.setDEBUG(true);

            System.out.println(searcher.execute());
            searcher.reset();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void searchPathTest2() {
        // 设置开始节点ID
        searcher.addPathStartNodeId(375166807);

        // 扩展节点时设置关系和节点的过滤条件

        searcher.addPathEndNodeLabel(Label.label("组织"));
        searcher.addPathEndNodeLabel(Label.label("专题事件"));

        searcher.addPathLength(2);
        searcher.setStart(0);
        searcher.setRow(100);

        System.out.println(searcher.execute());
        searcher.reset();
    }

    @Test
    public void searchPathTest3() {
        // 扩展当前图中所有虚拟账号的事件，一次性扩展
        while (true) {
            // 设置虚拟账号节点
            searcher.addPathStartNodeId(new long[]{380136635, 380294157, 380269315, 380135981, 380281883, 380294873, 380294870, 380135965, 380294881, 380294877, 380259675});

            // 设置路径中节点一定要包含的标签
            // 路径必须经过帖子类节点
            searcher.addPathMidSideNodeLabel(Label.label("帖子"), LabelOccurs.MUST_CONTAINS);
            // 路径必须经过专题事件类节点
            searcher.addPathMidSideNodeLabel(Label.label("专题事件"), LabelOccurs.MUST_CONTAINS);


            // 设置搜索路径深度
            searcher.addPathLength(2);

            // 设置虚拟图匹配模式，被虚拟的路径将会在返回路径中移除掉
            /**
             * @param isVirtualGraph:是否返回虚拟图
             * @param startMergeNodeLabel:虚拟图开始的标签类型-当前类型的标签数组
             * @param endMergeNodeLabel:虚拟图结束的标签类型-当前类型的标签数组
             * @param virtualRelationshipType:生成虚拟图的关系名称
             * @return 返回的数据中会将被虚拟的关系路径移除掉
             * @Description: TODO(是否返回虚拟图 - 虚拟图的生成从开始结束类型节点忽略中间所有的关系和路径 ， 直接根据上一次匹配的数据进行生成新的路径)
             * <p>
             * P1=(n:虚拟账号)-[]-(f:发帖)-[]-(m:专题事件)-[]-(h:帖子)
             * <p>
             * addPathGraphIsVirtual(true, new Label[](Label.label("虚拟账号")), new Label[](Label.label("专题事件")))
             * addVirtualP2=(n:虚拟账号)-[]-(m:专题事件)
             */
            // 此功能可以将与事件相连的帖子隐藏
            searcher.addPathGraphIsVirtual(true, Label.label("虚拟账号"), Label.label("专题事件"), RelationshipType.withName("参与事件"), Label.label("帖子"));

            // 设置搜索的关系路径数量
            searcher.setStart(0);
            searcher.setRow(1000);

            searcher.setDEBUG(true);

            System.out.println(searcher.execute());
            searcher.reset();
        }
    }

    @Test
    public void searchPathTest4() {
    }

    @Test
    public void searchPathTest5() {

//        /**
//         * TEST:
//         * create (n:专题事件) set n.eid=12,n._entity_name='dsadsadas',n._unique_uid='asdwqwtrrtr5qwedcdsfsaas' return n
//         * create (n:帖子) set n._entity_name='今天看见一个人dsadsadas',n._unique_uid='a234sdwqwtrrtr5qwedcdsfsaas' return n
//         * create (n:虚拟账号) set n._entity_name='社会热点播报',n._unique_uid='2sa234sdwqwtrrtr5qwedcdsfsaas' return n
//         * create (n:组织) set n._entity_name='热点传播',n._unique_uid='2sa234sdwqwtrrtr5qwedcdsfsaas' return n
//         * create (n:组织) set n._entity_name='组织',n._unique_uid='1sa234sdwqwtrrtr5qwedcdsfsaas' return n
//         *
//         * 参与组织：match (n),(m) where id(n)=375166807 and id(m)=375111286
//         *          create p=(n)-[r:参与组织]->(m) set r._entity_name='参与组织',r._unique_uid='2sa23dwqwtrtr5qwedcdsfsaas' return p
//         * 发帖：match (n),(m) where id(n)=375166807 and id(m)=374825589
//         *      create p=(n)-[r:发帖]->(m) set r._entity_name='发帖',r._unique_uid='2sa23wqwtrtr5qwedcdsfsaas' return p
//         * 命中关键词：match (n),(m) where id(n)=374825589 and id(m)=375184813
//         *           create p=(n)-[r:命中关键词]->(m) set r._entity_name='命中关键词',r._unique_uid='2sa23wqwtrtr5qwedcdsfsaas' return p
//         * 点赞：create (n:虚拟账号) set n._entity_name='新闻达人',n._unique_uid='1sa234sdwqwtrtr5qwedcdsfsaas' return n
//         *   377753683
//         *      match (n),(m) where id(n)=377753683 and id(m)=374825589
//         *      create p=(n)-[r:点赞]->(m) set r._entity_name='点赞',r._unique_uid='2sa23wqwtrtr5qwedcdsfsaas' return p
//         * **/
//
//        // 设置开始节点IDS
//        searcher.addPathStartNodeId(new long[]{375166807, 1, 23323, 123434, 347898, 2323, 1222});
//        // 关系类型
//        searcher.addPathMidSideRelationType(Relationships.参与事件, RelationOccurs.SHOULD_CONTAINS);
//
//        // 结束节点的过滤条件
//        // 结束节点必须是这些类型的节点
//        searcher.addPathEndNodeLabel(Label.label("虚拟账号"));
//        // 参与者
//        searcher.addPathEndNodeProperties("isEventParticipant", true);
//        // 互动者
//        searcher.addPathEndNodeProperties("isEventInteraction", true);
//        // 设置关系路径长度（在此层数范围内进行搜索）
//        searcher.addPathLength(2);
//
//        // 是否返回虚拟图（人与事件是通过帖子关联，如果不设置此项，数据匹配不到参与事件这个关系将只会返回节点，
//        // 设置这个之后会返回虚拟图（参与事件关系类型是没有持久化到NEO4J的），即时分析得出。类似与互动网络图，也是即时分析得出）
//        searcher.addPathGraphIsVirtual(true);
//
//        // 设置路径搜索的数量
//        searcher.setRow(1000);
//
//        System.out.println(searcher.execute());
//        searcher.reset();
    }

    /**
     * @param
     * @return
     * @Description: TODO(节点与关系同时过滤 - 检索路径)
     */
    @Test
    public void searchPathTest6() {
//        /**
//         * 根据当前节点，扩展指定层数内的其它节点和关系
//         */
//        searcher.addPathStartNodeProperties("eid", 1);
//        searcher.addPathLength(3);
//        searcher.addPathMidSideNodeLabel(Label.label("专题事件"));
////        searcher.addPathMidSideNodeLabel(Labels.专题事件);
//        searcher.addPathMidSideNodeLabel(Label.label("组织"));
//        searcher.addPathMidSideNodeLabel(Label.label("账号"));
//        searcher.setStart(0);
//        searcher.setRow(1000);
//        System.out.println(searcher.execute());
//        searcher.reset();
    }

    @Test
    public void searchPathTest7() {
        // 组织者
//        JSONObject result = searcher.execute("MATCH p=(n:专题事件)<-[r:参与事件]-(m:虚拟账号) WHERE r.statisticsPostNum IS NOT NULL " +
//                "RETURN p ORDER BY TOINT(r.statisticsPostNum) DESC SKIP 0 LIMIT 1");
//        System.out.println(result);

        // 传播者
//        JSONObject result = searcher.execute("MATCH p=(n:专题事件)<-[r:参与事件]-(m:虚拟账号) WHERE r.statisticsPostNum IS NOT NULL " +
//                "RETURN p ORDER BY TOINT(r.statisticsPostNum) DESC SKIP 1 LIMIT 10");
//        System.out.println(result);

        // 参与者
        JSONObject result = searcher.execute("MATCH p=(n:专题事件)<-[r:参与事件]-(m:虚拟账号) WHERE r.statisticsPostNum IS NOT NULL " +
                "RETURN p ORDER BY TOINT(r.statisticsPostNum) DESC SKIP 10 LIMIT 10000", CRUD.RETRIEVE);
        System.out.println(result);
    }

    @Test
    public void analyzerToolTest1() {
        /**
         * @param targetId:被分析人ID
         * @param countPropertyName:在返回的节点中设置统计属性
         * @param skip
         * @param limit:每次最多分析做少数据
         * @return
         * @Description: TODO(互动网络分析)
         */
        System.out.println(searcher.interactiveNetworkAnalyzer(377753683, "interactiveNetworkAnalyzerCount", 0, 1000));
    }

    @Test
    public void analyzerToolTest2() {
        while (true) {
            /**
             * @param startId:当前事件ID
             * @param targetLabel:账号标签
             * @param countPropertyName:在返回的节点中设置统计属性
             * @param skip
             * @param limit:每次最多分析多少数据
             * @return
             * @Description: TODO(事件分析)
             */
            JSONObject result = searcher.interactiveHotEventAnalyzer(379962831, Label.label("虚拟账号"), "interactiveHotEventCount", 0, 1000);
            System.out.println(result);

            // 结果集里面过滤（只保留指定标签的节点）
//        Label[] labels = new Label[]{Labels.虚拟账号, Labels.帖子};
//            Label[] labels = new Label[]{Labels.虚拟账号};
//            result = JSONTool.filterD3GraphDataByNodeLabel(result, labels);
//            System.out.println(result);

            // 生成虚拟图
            result = NeoAnalyzer.loadVirtualGraph(result, Label.label("虚拟账号"), Label.label("专题事件"), RelationshipType.withName("参与事件"), Label.label("帖子"));

            // 使用排序方法
            System.out.println(JSONTool.sortD3GraphDataByNodeProperty(result, "interactiveHotEventCount", SortOrder.DESC));
        }

    }

    @Test
    public void getOnePath() {
        searcher.addPathStartNodeLabel(Label.label("虚拟账号"));
        searcher.addPathEndNodeLabel(Label.label("专题事件"));
        searcher.addPathLength(1);
        searcher.setStart(0);
        searcher.setRow(2);
        searcher.setPathEndNodeSort("eid", SortOrder.DESC);
        System.out.println(searcher.execute());
    }

    @Test
    public void getOneNode() {
        /**
         * @param id :节点ID-NEO4J自动生成的ID
         * @return
         * @Description: TODO(通过节点ID查找节点 - 多次设置此条件可以检索多个节点)
         */
        searcher.addNodeId(123);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    /**
     * ====================================================执行自定义CYPHER查询====================================================
     **/
    @Test
    public void execute() {
        //        System.out.println(searcher.execute("MATCH p=()-[r]-(),MATCH p2=()-[r]-() RETURN p,p2 LIMIT 10"));

//        System.out.println(searcher.execute("" +
//                "MATCH p=()-[r]-() RETURN p LIMIT 2\n" +
//                "UNION ALL\n" +
//                "MATCH p=()-[r]-() RETURN p LIMIT 2"));

//        System.out.println(searcher.execute("MATCH p=()-[r]-() RETURN p LIMIT 1"));
//        System.out.println(searcher.execute("MATCH p=()-[r]-() WHERE id(r)=32432 DELETE r"));

//        JSONObject result = searcher.execute("MATCH p=(n:专题事件 {eid:'638'})<-[r:参与事件]-(m:虚拟账号) WHERE id(m)=385495833 RETURN p");
//        System.out.println(result);

//        System.out.println(searcher.resultIsEmpty(result));

    }

}


