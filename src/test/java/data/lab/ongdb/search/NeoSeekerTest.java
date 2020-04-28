package data.lab.ongdb.search;

import data.lab.ongdb.algo.simhash.NewsFingerPrint;
import data.lab.ongdb.common.CRUD;
import data.lab.ongdb.compose.pack.Cypher;
import data.lab.ongdb.model.Label;
import data.lab.ongdb.model.RelationshipType;
import data.lab.ongdb.search._plugin.PathPlugin;
import data.lab.ongdb.util.FileUtil;
import data.lab.ongdb.util.JSONTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.neo4j.driver.v1.Values.parameters;

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
 * @Description: TODO(CYPHER SEEKER)
 * @date 2019/7/26 10:30
 */
public class NeoSeekerTest {

    //        private final static String ipPorts = "localhost:7687";
    private final static String ipPorts = "192.168.12.19:7687";

    private NeoSearcher neoSearcher = new NeoSearcher(ipPorts, "neo4j", "123456",
            Config.builder().withMaxTransactionRetryTime(600, TimeUnit.SECONDS).build());

//    private NeoSearcher neoSearcher = new NeoSearcher("192.168.1.12:7687", "neo4j", "123456");

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.properties");
        neoSearcher.setDEBUG(true);
    }

    /**
     *
     * 三个核心检索接口：NodeSeeker/RelationSeeker/PathSeeker
     *
     * SEEKER接口数据返回格式为：NODE或者PATH
     *
     * **/

    /**
     * ====================================================节点检索接口测试====================================================
     **/

    /**
     * @param
     * @return
     * @Description: TODO(通过节点ID查询节点)
     */
    @Test
    public void seekNodeById() {

        JSONObject result = neoSearcher.execute(
                NeoSeeker.nodeSeeker()
//                        .setId(38)
//                        .setId(38, 38, 21)
                        .setId(new long[]{213, 343, 123, 345, 213112})
                        .toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过节点ID查询节点)
     */
    @Test
    public void seekNodeById_2() {
        JSONObject result = neoSearcher.execute(Cypher.mergeNode()
                        .setLabel(Label.label("Tset"))
                        .setProperties(new Property("json", JSONObject.parseObject(JSON.toJSONString(
                                new NewsFingerPrint("0101010101010101010101010101010010100101...",
                                        "0101010101010101010101010101010010100101...")))))
                        .setUniqueField("unique", "test-md5")
                        .toMerge()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过节点的标签查询节点)
     */
    @Test
    public void seekNodeByLabel() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.nodeSeeker()
//                        .setLabel(Label.label("Person"))
                        // 单个节点有多个标签的情况
                        .setLabel(Label.label("Person"), Label.label("三国人物"))
                        .setStart(0)
                        .setRow(10)
                        .toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过节点的标签和属性查询节点)
     */
    @Test
    public void seekNodeByLabelAndProperties() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.nodeSeeker()
                        .setLabel(Label.label("Person"))
//                        .setLabel(Label.label("Person"), Label.label("三国人物"))
                        //设置单个属性
//                        .setProperties(new Property("_entity_name", "刘备"))

                        // 多个属性组合使用自定义DSL（勿必使用有索引的字段进行检索）（为提高检索尽量使用单标签）
//                        .setProperties("n._entity_name='刘备' AND n._unique_uuid='dsadsad' AND n.count='3425'")
//                        .setProperties("(n._entity_name='曹操' OR n._unique_uuid='dsadsad') AND n.count='3425'")
                        .setProperties("(n._entity_name='曹操' OR n._unique_uuid='dsadsad') AND n.count IS NOT NULL")
                        .setStart(0)
                        .setRow(10)
                        .toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * ====================================================关系检索接口====================================================
     **/

    /**
     * @param
     * @return
     * @Description: TODO(通过关系ID检索关系)
     */
    @Test
    public void seekRelationById() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        .setId(0)
                        .toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过设定开始节点和关系的属性来搜索一层简单关系)
     */
    @Test
    public void seekRelationSetMoreCondition() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        // 设置开始节点
                        .setStartNodeId(38)
                        .setRelationType(RelationshipType.withName("参与事件"))
                        // 设置定关系属性
                        .setProperties(new Property("update_time_mills", 1565091819873l))
                        .toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
        List<Long> nodeIds = JSONTool.packNodeIds(result, 38);
        nodeIds.forEach(System.out::println);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过设定开始节点和关系的属性来搜索一层简单关系)
     */
    @Test
    public void seekRelationSetMoreCondition_2() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        // 设置开始节点
                        .setStartNodeId(38)
                        .setRelationType(RelationshipType.withName("参与事件"))
                        // 设置定关系属性
                        .setProperties(new Property("update_time_mills", 1565091819873l), new Property("time_mills", 1565091819873l))
                        .toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
        List<Long> nodeIds = JSONTool.packNodeIds(result, 38);
        nodeIds.forEach(System.out::println);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过关系类型检索关系)
     */
    @Test
    public void seekRelationByRelationType() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
//                        .setRelationType(RelationshipType.withName("好友"))
                        // 多个关系类型时是或查询
                        .setRelationType(RelationshipType.withName("好友"), RelationshipType.withName("兄弟"))
                        .setStart(0)
                        .setRow(10)
                        .toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过关系类型关系属性检索关系)
     */
    @Test
    public void seekRelationByRelationTypeAndProperties() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        .setRelationType(RelationshipType.withName("好友"))
//                        .setRelationType(RelationshipType.withName("好友"), RelationshipType.withName("兄弟"))

                        //设置单个属性
//                        .setProperties(new Property("current_time", "1564108798256"))

                        // 多个属性组合使用自定义DSL（勿必使用有索引的字段进行检索）
                        .setProperties("r.current_time='1564108798256' AND r.comment='这是导入的CSV'")
//                        .setProperties("(r.current_time='1564108798256' OR r.comment='这是导入的CSV') AND r.count IS NULL")
                        .setStart(0)
                        .setRow(10)
                        .toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }


    /**
     * ====================================================节点关系同时检索-PATH-子图检索接口====================================================
     **/
    /**
     * @param
     * @return
     * @Description: TODO(通过开始结束节点搜索关系)
     */
    @Test
    public void seekPath_1() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        // 开始节点设置方式
                        .setStartNodeId(2, 23, 123)
//                        .seStartNodeId(2)
//                        .seStartNodeId(new long[]{2,343,23})

                        .setPathLength(1)

                        // 结束节点的设置方式
//                        .setEndNodeId(2,23,123)
                        .setEndNodeId(36)
//                        .setEndNodeId(new long[]{2,343,23})

                        .setStart(0).setRow(10).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过开始节点搜索关系)
     */
    @Test
    public void seekPath_2() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        // 开始节点设置方式
                        .setStartNodeId(2, 23, 123)
//                        .seStartNodeId(2)
//                        .seStartNodeId(new long[]{2,343,23})

                        .setPathLength(3)

                        .setStart(0).setRow(10).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过结束节点搜索关系)
     */
    @Test
    public void seekPath_3() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        .setPathLength(3)

                        // 结束节点的设置方式
//                        .setEndNodeId(2,23,123)
                        .setEndNodeId(36)
//                        .setEndNodeId(new long[]{2,343,23})

                        .setStart(0).setRow(10).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过开始结束节点标签搜索关系)
     */
    @Test
    public void seekPath_4() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()

                        // 开始节点设置方式
                        .setStartNodeLabel(Label.label("Person"))
//                        .seStartNodeLabel(Label.label("Person"), Label.label("三国人物"))

                        .setPathLength(1)

                        // 结束节点的设置方式
                        .setEndNodeLabel(Label.label("Person"))
//                        .setEndNodeLabel(Label.label("Person"), Label.label("三国人物"))
//                        .setEndNodeId(new long[]{2,343,23})
                        .setStart(0).setRow(10).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过开始节点标签搜索关系)
     */
    @Test
    public void seekPath_5() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        // 开始节点设置方式
                        .setStartNodeLabel(Label.label("Person"))
//                        .seStartNodeLabel(Label.label("Person"), Label.label("三国人物"))
                        .setPathLength(3)
                        .setStart(0).setRow(10).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过结束节点标签搜索关系)
     */
    @Test
    public void seekPath_6() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        .setPathLength(3)
                        // 结束节点的设置方式
                        .setEndNodeLabel(Label.label("Person"))
//                        .setEndNodeLabel(Label.label("Person"), Label.label("三国人物"))
                        .setStart(0).setRow(100000000).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    // 锁定关系长度
    // 设置节点属性
    // 设置关系属性
    // 预留特殊遍历接口，自定义遍历插件选择

    /**
     * @param
     * @return
     * @Description: TODO(锁定路径长度 - 检索到path中关系长度为等于n ， 而不是小于等于n)
     */
    @Test
    public void seekPath_7() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        // 锁定路径长度
                        .setFixPathLength(3)
                        // 结束节点的设置方式
                        .setEndNodeLabel(Label.label("Person"))
//                        .setEndNodeLabel(Label.label("Person"), Label.label("三国人物"))
                        .setStart(0).setRow(100000000).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过开始节点标签和属性搜索关系)
     */
    @Test
    public void seekPath_8() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()

                        // 开始节点设置方式
                        .setStartNodeLabel(Label.label("Person"))
//                        .seStartNodeLabel(Label.label("Person"), Label.label("三国人物"))
                        .setStartNodeProperties(new Property("_entity_name", "诸葛亮"))
                        .setPathLength(3)
                        .setStart(0).setRow(10).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过开始节点标签和属性搜索关系)
     */
    @Test
    public void seekPath_9() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        // 开始节点设置方式-（使用全文检索）
                        .setStartNodeFullTextSearch("test", "香港", 0, 100)
                        .setPathLength(1)
                        .setStart(0).setRow(10).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过开始节点标签和属性搜索关系)
     */
    @Test
    public void seekPath_10() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        // 开始节点设置方式-（使用全文检索）-（返回全文检索的得分）
                        .setStartNodeFullTextSearchScore("test", "香港", 0, 100)
                        .setPathLength(1)
                        .setStart(0).setRow(10).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    @Test
    public void seekNodeByFullTextTest() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.nodeSeeker().fullTextQuery("test", "香港")
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    @Test
    public void seekNodeByFullTextTest2() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.nodeSeeker().setStart(0).setRow(10).fullTextQuery("test", "+(name:港独分子极端) AND -(name:香)")
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    @Test
    public void seekNodeByFullTextTest3() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.nodeSeeker().setStart(0).setRow(10).fullTextQueryScore("test", "+(name:港独分子极端) AND -(name:香)")
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(指定开始节点ID以及结束节点标签 ， 分页查询关联的关系节点)
     */
    @Test
    public void seekPath_11() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        .setStartNodeId(21321)
                        .setEndNodeLabel(Label.label("Test"))
                        .setStart(0)
                        .setRow(100).toQuery(),
                CRUD.RETRIEVE
        );
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过开始结束节点标签和属性搜索关系 - 设置遍历插件)
     */
    @Test
    public void seekPath_12() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        // 事务最大超时事件（设置此时间本次CYPHER执行将在指定时间内关闭事务）- 防止事务超时
                        .setRunTimeBoxed(1000)
                        .setPathLength(3)
                        .setStart(0).setRow(1000).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过开始结束节点标签和属性搜索关系 - 设置遍历插件)
     */
    @Test
    public void seekPath_13() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        .setStartNodeLabel(Label.label("组织"))

                        // 设置一个路径遍历插件
                        // 路径中的节点必须包含这些标签（当前路径中所有节点的标签包含在下面标签即可，不是所有节点必须是这些标签）
                        .setPlugin(PathPlugin.casia_filter_pathByNodeLabels(PathPlugin.BoolOccurs.TRUE,
                                Label.label("组织"), Label.label("专题事件")))

                        // 事务最大超时事件（设置此时间本次CYPHER执行将在指定时间内关闭事务）- 防止事务超时
                        .setRunTimeBoxed(10000)
                        .setPathLength(3)
                        .setStart(0).setRow(10).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(检索最短路径 - 使用最短路径插件 - 只分析一条最短路径)
     */
    @Test
    public void seekPath_14() {
        // 设置最短路径插件 - 检索两点之间的最短路径
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        .setStartNodeId(48)
                        .setPlugin(PathPlugin.shortestPath())
                        .setEndNodeId(47)
                        .setStart(0).setRow(10).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(检索最短路径 - 使用最短路径插件 - 分析所有最短路径)
     */
    @Test
    public void seekPath_15() {
        // 设置最短路径插件 - 检索两点之间的最短路径 - 分析所有最短路径
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        .setStartNodeId(48)
                        .setPlugin(PathPlugin.allShortestPaths())
                        .setEndNodeId(47)
                        .setStart(0).setRow(10).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(多点两两之间的关联关系检索 - 使用两两关系检索插件)
     */
    @Test
    public void seekPath_16() {
        // 两两之间共同关联节点的路径
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        .setStartNodeId(1888, 1591, 1252, 33)
                        .setPlugin(PathPlugin.twinNodePaths())
                        .setStart(0).setRow(1000).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(多点两两之间的关联关系检索 - 使用两两关系检索插件 - 统计关联出的节点类型和数量)
     */
    @Test
    public void countPathNode_1() {
        // 使用两两关系检索插件 -  统计关联出的节点类型和数量
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        .setStartNodeId(1888, 1591, 1252, 33)
                        .setPlugin(PathPlugin.twinNodePathsMidNodeCount())
                        .toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(多点精确的关联关系检索 - 使用多点指同一个节点的精确关系检索插件)
     */
    @Test
    public void seekPath_17() {
        // 设置最短路径插件 - 检索两点之间的最短路径 - 分析所有最短路径
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        .setStartNodeId(1888, 1591, 1252)
                        .setPlugin(PathPlugin.allNodePointToSameNode())
                        .setStart(0).setRow(1000).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(深度推理 - 多点两两之间的最短路径分析 - shortestPath ( 只分析一条最短路径))
     */
    @Test
    public void seekPath_18() {
        // 两两之间共同关联节点的路径
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        .setStartNodeId(1405802, 22742, 20103)
                        .setPlugin(PathPlugin.twinNodeShortestPath())
                        .setStart(0).setRow(1000).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(深度推理 - 多点两两之间的最短路径分析 - allShortestPaths ( 分析所有最短路径))
     */
    @Test
    public void seekPath_19() {
        // 两两之间共同关联节点的路径
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        .setStartNodeId(1405802, 22742, 20103)
                        .setPlugin(PathPlugin.twinNodeAllShortestPaths())
                        .setStart(0).setRow(1000).toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    /**
     * @param
     * @return
     * @Description: TODO(检索事件下新闻的转载网络)
     */
    @Test
    public void seekPath_20() {
        /**
         * PathPlugin.apocPathExpand:
         *
         * ▪ startNode：起始节点，可以是节点的Id或者节点变量
         * ▪ relationshipFilter：遍历关系的过滤条件，用‘|’分隔
         * ▪ labelFilter：遍历节点的过滤条件，用’|’分隔(见下页)
         * ▪ minLevel：最小遍历层级
         * ▪ maxLevel：最大遍历层级
         * ▪ path：返回的路径列表
         *
         * @param pathFilter:关系过滤
         * @param labelFilter:标签过滤
         * @param minLevel:最小过滤层级
         * @param maxLevel:最大过滤层级
         * @return
         * @Description: TODO(apoc.path.expand)
         * 节点扩展过程apoc.path.expand()可以从给定节点或节点列表开始，沿着指定的关
         * 系类型进行遍历，直到特定结束条件满足时停止，并返回路径或节点
         *
         * <p>pathFilter:
         * ‘PARENT_OF>|ANSWER’：遍历仅沿着这两个关系进行，其中PARENT_OF是有向的(从Post离开的)，ANSWER是双向的
         *
         * <p>labelFilter:
         * -Post 排除 Post节点不被遍历，也不被包括在返回的路径中。
         * +Post 包含 缺省。Post节点将被遍历，也被包括在返回的路径中。
         * /Post 终止且返回 遍历路径直到遇见Post类型的节点，然后仅返回Post节点。
         * >Post 终止但是继续 遍历路径只返回到达Post类型的节点(含)之前的部分，在Post
         * 节点之后的部分会继续被遍历，但是不会被返回。
         *
         */
//        JSONObject result = neoSearcher.execute(
//                NeoSeeker.pathSeeker()
//                        .setStartNodeLabel(Label.label("事件"))
//                        .setStartNodeProperties(new Property("eid", 2))
//                        .setEndNodeLabel(Label.label("新闻"))
//                        .setPlugin(PathPlugin.apocPathExpand("转载", "+新闻", 1, 10))
//                        .setStart(0)
//                        .setRow(100)
//                        .toQuery(), CRUD.RETRIEVE
//        );
//        System.out.println(result);

        // 优化查询
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        .setStartNodeLabel(Label.label("事件"))
                        .setStartNodeProperties(new Property("eid", 2))
                        .setRelationType(RelationshipType.withName("相关新闻"))
                        .setEndNodeLabel(Label.label("新闻"))
                        .setPlugin(PathPlugin.apocPathExpand("转载", "+新闻", 1, 10))
                        .setStart(0)
                        .setRow(100)
                        .toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    @Test
    public void seekRelation_21() {
        // 查询事件所有的聚簇中心
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        .setStartNodeLabel(Label.label("事件"))
                        .setStartNodeProperties(new Property("eid", 2))
                        .setRelationType(RelationshipType.withName("相关新闻"))
                        .setProperties(new Property("cluster_master", 1))
                        .setReturnEndNode(true)
                        .toQuery(),
                CRUD.RETRIEVE
        );
        System.out.println(result);
//
        // 查询事件所有的聚簇中心
//        JSONObject result = neoSearcher.execute(NeoSeeker.relationSeeker()
//                        .setStartNodeId(23123)
//                        .setProperties(new Property("cluster_master", 1))
//                        .setRelationType(RelationshipType.withName("相关新闻"))
//                        .setReturnEndNode(true)
//                        .toQuery()
//                , CRUD.RETRIEVE);
//        System.out.println(result);
    }

    @Test
    public void seekPath_22() {
        // 查看事件某个聚簇下的转载网络
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        .setStartNodeLabel(Label.label("事件"))
                        .setStartNodeProperties(new Property("eid", 2))
                        .setRelationType(RelationshipType.withName("相关新闻"))
                        .setProperties(new Property("cluster_id", "9cd30979042104322e2300f8758d0ebd"))
                        .setEndNodeLabel(Label.label("新闻"))
                        .setPlugin(PathPlugin.apocPathExpand("转载", "+新闻", 1, 10))
                        .setStart(0)
                        .setRow(100)
                        .toQuery(), CRUD.RETRIEVE
        );
        System.out.println(result);
    }

    @Test
    public void seekPath_23_1() {
        // 查看某个事件的聚簇
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        .setStartNodeLabel(Label.label("事件"))
                        .setStartNodeProperties(new Property("eid", 2))
                        .setRelationType(RelationshipType.withName("相关新闻"))
                        .setProperties(new Property("cluster_id", "9cd30979042104322e2300f8758d0ebd"))
                        .setEndNodeLabel(Label.label("新闻"))
                        .setReturnEndNode(true)
                        .setStart(0)
                        .setRow(100)
                        .toQuery(), CRUD.RETRIEVE
        );
        System.out.println(result);
    }

    @Test
    public void seekPath_23() {
//        // 1、查看事件下两个站点下新闻之间的转载关系
//        JSONObject result = neoSearcher.execute(
//                NeoSeeker.relationSeeker()
//                        .setStartNodeLabel(Label.label("新闻"))
//                        .setStartNodeProperties(new Property("site_name", "立场新闻"))
//                        .setRelationType(RelationshipType.withName("转载"))
//                        .setPlugin(PathPlugin.twoSiteReprintRelation(Label.label("事件"), new Property("eid", 816)))
//                        .setEndNodeLabel(Label.label("新闻"))
//                        .setEndNodeProperties(new Property("site_name", "美国有线新闻网"))
//                        .setStart(0)
//                        .setRow(100)
//                        .toQuery()
//                , CRUD.RETRIEVE
//        );
//        System.out.println(result);

        // 2、查看事件下两个站点下新闻之间的转载关系
        // TEST:事件eid 816 站点 光明网 新华网
        int eid = 816;
        String siteOne = "光明网";
        String siteTwo = "新华网";

//        String cypher = "MATCH (event:事件)<-[:相关新闻]-(m:新闻),(event:事件)<-[:相关新闻]-(f:新闻) \n" +
//                " WHERE event.eid=" + eid + " AND m.site_name='" + siteOne + "' AND f.site_name='" + siteTwo + "'\n" +
//                " WITH COLLECT(m) AS target,COLLECT(f) AS source\n" +
//                "UNWIND source AS sou\n" +
//                "UNWIND target AS tar \n" +
//                " WITH sou,tar \n" +
//                "CALL apoc.algo.allSimplePaths(sou, tar, '转载', 100) YIELD path AS p RETURN p";
        /**
         * 优化查询
         * **/
        String cypher = " MATCH (event:事件) WHERE event.eid=1138 WITH event\n" +
                " MATCH (event)<-[:相关新闻]-(m:新闻) WHERE m.site_name='搜狐' WITH COLLECT(m) AS target,event\n" +
                " MATCH (event)<-[:相关新闻]-(f:新闻) WHERE f.site_name='伊犁新闻网' WITH COLLECT(f) AS source,target,event\n" +
                "UNWIND source AS sou\n" +
                "UNWIND target AS tar \n" +
                " WITH sou,tar \n" +
                "CALL apoc.algo.allSimplePaths(sou, tar, '转载', 100) YIELD path AS p RETURN p;";

        JSONObject result = neoSearcher.execute(cypher, CRUD.RETRIEVE);
        System.out.println(result);
    }

    @Test
    public void seekPath_24() {
        // 查看某个事件下新闻的转载关系-返回属性
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        .setStartNodeLabel(Label.label("新闻"))
                        .setRelationType(RelationshipType.withName("转载"))
                        .setPlugin(PathPlugin.twoSiteReprintRelation(Label.label("事件"), new Property("eid", 2), true))
                        .setEndNodeLabel(Label.label("新闻"))
                        .setStart(0)
                        .setRow(100)
                        .toQuery()
                , CRUD.RETRIEVE_PROPERTIES
        );
        System.out.println(result);
    }

    @Test
    public void seekPath_25() {
        // 加载事件下站点之间的转载关系
        // 标签动态拼接SITES_REPRINT_EID
        Label label = Label.label("SITES_REPRINT_2");
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        .setStartNodeLabel(label)
                        .setRelationType(RelationshipType.withName("转载"))
                        .setEndNodeLabel(label)
                        .setStart(0)
                        .setRow(1000)
                        .toQuery()
                , CRUD.RETRIEVE
        );
        System.out.println(result);
    }

    @Test
    public void seekPath_25_1() {
        // 获取事件下需要重构的聚簇中心
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        .setStartNodeLabel(Label.label("事件"))
                        .setStartNodeProperties(new Property("eid", 2))
                        .setRelationType(RelationshipType.withName("相关新闻"))
                        .setProperties(new Property("cluster_master", 1), new Property("cluster_renovate", 1))
                        .setEndNodeLabel(Label.label("新闻"))
                        .setReturnEndNode(true)
//                        .setStart(0)
//                        .setRow(200)
                        .toQuery().split("SKIP")[0],
                CRUD.RETRIEVE
        );
        System.out.println(JSONTool.getNodeOrRelaList(result, "nodes").size());
    }

    @Test
    public void seekPath_26() {
        // 查看事件下某个站点发布的新闻
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        .setStartNodeLabel(Label.label("事件"))
                        .setStartNodeProperties(new Property("eid", 816))
                        .setRelationType(RelationshipType.withName("相关新闻"))
                        .setEndNodeLabel(Label.label("新闻"))
                        .setEndNodeProperties(new Property("site_name", "新浪"))
                        .setReturnEndNode(true)
                        .setStart(0)
                        .setRow(100)
                        .toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    @Test
    public void seekPath_26_1() {
        // 查看事件下未被分配到聚簇的新闻节点
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        .setStartNodeLabel(Label.label("事件"))
                        .setStartNodeProperties(new Property("eid", 1))
                        .setRelationType(RelationshipType.withName("相关新闻"))
                        .setProperties(new Property("cluster_master", -1))
                        .setEndNodeLabel(Label.label("新闻"))
                        .setReturnEndNode(true)
                        .setStart(0)
                        .setRow(100)
                        .toQuery()
                , CRUD.RETRIEVE);
        System.out.println(result);
    }

    @Test
    public void seekPath_27() {
        // 根据新闻节点ID，加载当前新闻的相似新闻
        // 获取聚簇cluster_id
//        JSONObject clusterObject = neoSearcher.execute(
//                "MATCH (n:事件)-[r:相关新闻]-(m:新闻) WHERE n.eid=2 AND id(m)=28354 RETURN r.cluster_id AS cluster_id",
//                CRUD.RETRIEVE_PROPERTIES
//        );
//        String cluster_id = JSONTool.resultRetrievePro(clusterObject).getJSONObject(0).getString("cluster_id");
//        // 根据聚簇cluster_id获取转载网络
//        JSONObject result = neoSearcher.execute(
//                NeoSeeker.relationSeeker()
//                        .setStartNodeLabel(Label.label("事件"))
//                        .setStartNodeProperties(new Property("eid", 2))
//                        .setRelationType(RelationshipType.withName("相关新闻"))
//                        .setProperties(new Property("cluster_id",cluster_id))
//                        .setEndNodeLabel(Label.label("新闻"))
//                        .setPlugin(PathPlugin.apocPathExpand("转载", "+新闻", 1, 10))
//                        .setStart(0)
//                        .setRow(100)
//                        .toQuery()
//                , CRUD.RETRIEVE);
//        System.out.println(result);
        int eid = 2;
        long newsId = 28354;
        String cypher = "MATCH (n:事件)-[r:相关新闻]-(m:新闻) WHERE n.eid=" + eid + " AND id(m)=" + newsId + " WITH n,r.cluster_id AS cluster_id\n" +
                "MATCH p=(n:事件)-[r:相关新闻]-(m:新闻) WHERE r.cluster_id=cluster_id WITH m \n" +
                "CALL apoc.path.expand(m,'转载','+新闻',0,10) YIELD path as p RETURN p";
        JSONObject result = neoSearcher.execute(cypher, CRUD.RETRIEVE);
        System.out.println(result);
    }

    @Test
    public void seekPath_28() {
        // 计算新闻节点两两之间是否相似
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        .setStartNodeLabel(Label.label("事件"))
                        .setStartNodeProperties(new Property("eid", 2))
                        .setRelationType(RelationshipType.withName("相关新闻"))
                        .setProperties(new Property("cluster_id", "9cd30979042104322e2300f8758d0ebd"))
                        .setEndNodeLabel(Label.label("新闻"))
                        .setPlugin(PathPlugin.apocPathExpand("转载", "+新闻", 1, 10))
                        .setStart(0)
                        .setRow(100)
                        .toQuery(), CRUD.RETRIEVE
        );
        JSONObject simHashResult = neoSearcher.calNewsSimilarBySimHash(result);
        System.out.println(simHashResult.toJSONString());
//        JSONArray similar = simHashResult.getJSONArray("similar");
//        for (int i = 0; i < similar.size(); i++) {
//            System.out.println(similar.getJSONObject(i).toJSONString());
//        }
    }

    @Test
    public void seekPath_29() {
        // 查询与当前站点相关的其它站点
        int eid = 816;
        JSONObject result = neoSearcher.execute(
                NeoSeeker.pathSeeker()
                        .setStartNodeId(26508)
                        .setRelationType(RelationshipType.withName("转载"))
                        .setEndNodeLabel(Label.label("SITES_REPRINT_" + eid))
                        .setStart(0)
                        .setRow(100)
                        .toQuery(), CRUD.RETRIEVE
        );
        System.out.println(result.toJSONString());
    }

    /**
     * 可配置的路径扩展：apoc.path.expandConfig
     * 参数名                类型                         缺省值     可为空？        说明
     * startNode            LONG - 节点id，或者节点列表     无         否          遍历的起始节点
     * {configuration}      配置选项列表                   NULL       是          具体配置项参见下面的说明
     * minDepth             INTEGER                      0           是          最小遍历层次数
     * maxDepth             INTEGER                      -1          是          最大遍历层次数。-1表示不限制，直到不再有可遍历的路径为止
     * relationshipFilter   字符串                        NULL        是         关系过滤器规则
     * abelFilter           字符串                        NULL        是         标签过滤器规则
     * bfs                  布尔值                        false       是         true–宽度优先遍历 false–深度优先遍历
     * uniqueness           字符串                        NULL        是         唯一性规则
     * filterStartNode      布尔值                        false       是         是否过对起始节点应用过滤规则
     * limit                正整数                        -1          是         返回路径的数目上限
     * optional             布尔值                        false       是         true–如果没有找到复合条件的路径，返回NULL值的序列；false–如果没有找到复合条件的路径，则不返回
     * endNodes             节点列表                      NULL        是          遍历终止节点列表
     * terminatorNodes      节点列表                      NULL        是         终止节点列表
     * sequence             字符串                        NULL        是         节点和标签过滤规则序列。指定sequence规则后labelFilter和relationshipFilter的内容会被忽略。
     * beginSequenceAtStart 布尔值                        true        是         是否对起始节点应用sequence中定义的规则
     * {maxDepth:2,relationshipFilter:'隶属虚拟账号|发帖|点赞|评论|转发|回复|互动',labelFilter:'/新浪微博ID',bfs:false,filterStartNode:false,limit:-1,optional:false}
     * **/

    /**
     * @return
     * @Description: TODO(查看已有索引 - 包括模式索引和全文索引)
     */
    @Test
    public void schema_1() {
        System.out.println(neoSearcher.dbIndexes());
    }

    /**
     * 执行自定义CYPHER查询（更多高级功能请直接编写CYPHER并使用遍历插件）
     **/
    @Test
    public void execute_1() {
        System.out.println(neoSearcher.execute("MATCH p=()-[r]-() RETURN p LIMIT 10", CRUD.RETRIEVE));
        System.out.println(neoSearcher.execute("MATCH (n) RETURN n LIMIT 10", CRUD.RETRIEVE));
    }

    @Test
    public void execute_2() {
        // 检索属性（单独检索属性返回的数据格式与检索节点和检索关系不一样，数据格式更简单）
//        System.out.println(neoSearcher.execute("MATCH (n) RETURN id(n) AS id,n.name AS name LIMIT 10", CRUD.RETRIEVE_PROPERTIES));
        JSONObject result = neoSearcher.execute("MATCH p=()-[r]-() RETURN id(r) AS relaId,r.name AS name LIMIT 10", CRUD.RETRIEVE_PROPERTIES);
        JSONArray properties = result.getJSONArray("queryResultList").getJSONObject(0).getJSONArray("retrieve_properties");

        // 打印查询结果
        properties.forEach(v -> System.out.println(v.toString()));

        // 打印查询结果
        properties.forEach(v -> {
            JSONObject object = (JSONObject) v;
            System.out.println(object.getString("name") != null ? object.getString("name") : "The parameters is null!!!");
        });
    }

    @Test
    public void execute_3() {
        JSONObject result_1 = neoSearcher.execute("MATCH p=()-[r]-() RETURN id(r) AS relaId,r.name AS name LIMIT 10", CRUD.RETRIEVE);
        System.out.println("PROPERTIES:" + result_1);
        JSONObject result_2 = neoSearcher.execute("MATCH p=()-[r]-() RETURN p LIMIT 10", CRUD.RETRIEVE);
        System.out.println("PATH:" + result_2);
        JSONObject result_3 = neoSearcher.execute("MATCH (n) RETURN n LIMIT 10", CRUD.RETRIEVE);
        System.out.println("NODE:" + result_3);

        // 从查询结果中获取properties
        System.out.println("GET PROPERTIES:" + JSONTool.getNodeOrRelaList(result_1, "properties"));

        // 从查询结果中获取relationships
        System.out.println("GET PATH:" + JSONTool.getNodeOrRelaList(result_2, "relationships"));
        System.out.println("GET NODE:" + JSONTool.getNodeOrRelaList(result_3, "nodes"));

        JSONObject result_4 = neoSearcher.execute("MATCH (n:专题事件) RETURN n.update_time_mills AS update_time_mills,id(n) AS id,n.name AS name LIMIT 100", CRUD.RETRIEVE);
        System.out.println(JSONTool.getNodeOrRelaList(result_4, "properties"));

        // MORE SEARCH RESULT
        System.out.println(neoSearcher.execute("MATCH p=()-[r]-() RETURN p LIMIT 1 UNION ALL MATCH p=()-[r]-() RETURN p skip 1 LIMIT 1", CRUD.RETRIEVE));
        System.out.println(neoSearcher.execute("MATCH (n),(m) WHERE id(n)=48 AND id(m)=47 MATCH p=shortestPath((n)-[*..15]-(m)) RETURN p LIMIT 1", CRUD.RETRIEVE));
        System.out.println(neoSearcher.execute("MATCH (n),(m),(f) WHERE id(n)=48 AND id(m)=47 AND id(f)=0 MATCH p=shortestPath((n)-[*..15]-(m)),p2=(f)-[:NEXT]-() RETURN p,p2", CRUD.RETRIEVE));
    }

    @Test
    public void execute_4() {
        String cypher = "MATCH p=()-[]-() RETURN p LIMIT 100";
        System.out.println(neoSearcher.execute(cypher, CRUD.RETRIEVE));
    }

    @Test
    public void execute_5() {
        JSONObject result = neoSearcher.execute(
                NeoSeeker.relationSeeker()
                        .setStartNodeLabel(Label.label("新闻"))
                        .setRelationType(RelationshipType.withName("转载"))
                        .setPlugin(PathPlugin.twoSiteReprintRelation(Label.label("事件"), new Property("eid", 1), true))
                        .setEndNodeLabel(Label.label("新闻"))
                        .setStart(0)
                        .setRow(200)
                        .toQuery()
                , CRUD.RETRIEVE_PROPERTIES);
        JSONArray sites = result.getJSONArray("retrieve_properties");
        System.out.println(sites);
    }

    @Test
    public void execute_6() {
        JSONObject result = neoSearcher.execute(
                "MATCH p=(n:SITES_REPRINT_816)-[r:转载]->(m:SITES_REPRINT_816) RETURN p"
                , CRUD.RETRIEVE);
        FileUtil.writeIDSToFile(result.toJSONString(), "check.json");
    }

    @Test
    public void execute_7() {
        String cypher = "MATCH p=(:`组织`)<-[:`参与组织`]-(:`现实人员`)-[r:`隶属虚拟账号`]->(:`虚拟账号`)-[:`参与事件`]-(m:`事件`) WHERE TOINT(m.eid)=654 RETURN p";
        JSONObject result = neoSearcher.execute(cypher, CRUD.RETRIEVE);
        FileUtil.writeIDSToFile(result.toJSONString(), "check.json");
    }

    @Test
    public void executeIterate_1() {
        /**
         * invoke cypherAction in batched transactions being feeded from cypherIteration running in main thread
         *
         * @param cypherIterate
         * @param cypherAction
         * @param config
         * @return
         * @Description: TODO(使用迭代器执行CYPHER -
         *apoc.periodic.iterate ( ' statement returning items ',
         *' statement per item ',
         * {batchSize:1000,iterateList:true,parallel:false,params:{},concurrency:50,retries:0})
         * YIELD batches, total - run the second statement for each item returned by the first statement. Returns number of batches and total processed rows)
         */
        // 适用场景 - 需要修改的数据比较多时可以考虑批量提交
        // 将`命中关键词`关系删除，同时修改为新的关系`参与事件`
        // 测试 - 禁止检索对象操作图更新接口
        // 以下操作将不会正常执行，只有Updater对象可以调用 - 访问Error:java.lang.IllegalArgumentException
        String cypherIterate = "MATCH (n:专题事件)<-[r:`命中关键词`]-(m) RETURN n,r,m";
        String cypherAction = "WITH {n} AS n,{r} AS r,{m} AS m MERGE (n)<-[r2:`参与事件`]-(m) SET r2.name='参与事件' DELETE r";
        System.out.println(neoSearcher.executeIterate(cypherIterate, cypherAction, "batchSize", 1000, "parallel", false));
    }

    /**
     * 使用NEO4J-JAVA-DRIVER自定义返回数据（单独封装数据）
     **/
    @Test
    public void driver() {
        // DRIVER IS  GLOBAL THREAD POOL
        try (Session session = neoSearcher.driver.session()) {
            // Auto-commit transactions are a quick and easy way to wrap a read.
            StatementResult result = session.run(
                    "MATCH (a:Person) WHERE a._entity_name STARTS WITH {x} RETURN a._entity_name AS name",
                    parameters("x", "刘"));
            // Each Cypher execution returns a stream of records.
            while (result.hasNext()) {
                Record record = result.next();
                // Values can be extracted from a record by index or name.
                System.out.println(record.get("name").asString());
            }
        }
    }

    @Test
    public void driverTest_2() {
        // Sessions are lightweight and disposable connection wrappers.
        try (Session session = neoSearcher.driver.session()) {
            // Wrapping Cypher in an explicit transaction provides atomicity
            // and makes handling errors much easier.
            try (Transaction tx = session.beginTransaction()) {
                tx.run("MERGE (a:Person {_entity_name: {x}})", parameters("x", "司马昭"));
                tx.success();  // Mark this write as successful.
            }
        }
    }

    @Test
    public void driverTest_3() {
        HashMap<Object[], List<Object[]>> masters = new HashMap<>();

        try (Session session = neoSearcher.driver.session()) {
            StatementResult result = session.run(
                    "MATCH p=(n:事件)-[r:相关新闻]-(m) WHERE n.eid={eid} AND r.cluster_master={master} RETURN m",
                    parameters("eid", 1, "master", 1));
            while (result.hasNext()) {
                Record record = result.next();
                Node node = record.get("m").asNode();
                Map<String, Object> map = node.asMap();
                String md5 = String.valueOf(map.get("_unique_uuid"));
                // 最后一个元素标记为是否需要更新
                Object[] master = new Object[]{md5, "adase4234234", md5, 1, System.currentTimeMillis(), false};
                setMaster(masters, master);
            }
        }
        System.out.println(masters.size());
    }

    @Test
    public void driverTest_4() {
        // 查询与当前站点相关的其它站点 - 并将数据封装为列表需要的格式
        List<Map<String, Object>> list = new ArrayList<>();
        int eid = 839;
        try (Session session = neoSearcher.driver.session()) {
            StatementResult result = session.run(
                    "MATCH p=(n)-[r:转载]-(m:SITES_REPRINT_" + eid + ") WHERE id(n)={id} RETURN n,r,m SKIP {skip} LIMIT {limit}",
                    parameters("id", 1026, "skip", 0, "limit", 100));
            while (result.hasNext()) {
                Map<String, Object> map = new HashMap<>();

                Record record = result.next();
                Node start = record.get("n").asNode();
                Node end = record.get("m").asNode();
                Map<String, Object> relation = record.get("r").asMap();

                // 0-双向转载 1-被转载 2-转载
                int mark = getReprintMark(relation, String.valueOf(start.asMap().get("_entity_name")));
                map.put("currentSite", start);
                map.put("reprintMark", mark);
                map.put("relationSite", end);
                list.add(map);
            }
        }
        System.out.println(list.size());
    }

    private int getReprintMark(Map<String, Object> relation, String site) {
        String statistics = String.valueOf(relation.get("statistics"));
        JSONArray statisticsArray = JSONArray.parseArray(statistics);
        if (statisticsArray.size() == 1) {
            JSONObject statisticsObj = statisticsArray.getJSONObject(0);
            String start = statisticsObj.getString("start");
            String end = statisticsObj.getString("end");
            if (start.equals(end)) {
                return 0;
            } else if (start.equals(site)) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 0;
        }
    }

    // 生成一个聚簇中心
    private void setMaster(HashMap<Object[], List<Object[]>> clusterMastersMap, Object[] master) {
        List<Object[]> nodeNews = new ArrayList<>();
        nodeNews.add(master);
        clusterMastersMap.put(master, nodeNews);
    }

    @Test
    public void eventDig() {
        neoSearcher = new NeoSearcher(ipPorts, "neo4j", "123456");
        eventOrgZdrExpand(39516L);
    }

    /**
     * @param
     * @return
     * @Description: TODO(事件右键 【 事件挖掘 】 - - 扩展出事件参与度较高的前N个人和事件相关的组织)
     */
    public JSONObject eventOrgZdrExpand(long nodeId) {
        String cql = "MATCH p=(n:专题事件)<-[:参与事件]-()-[:参与组织]->(:重点人组织) WHERE id(n)=" + nodeId + " RETURN p";
        JSONObject initialResult = neoSearcher.execute(cql, CRUD.RETRIEVE);

        String topnCql = "MATCH p=(n:专题事件)<-[r:参与事件]-() WHERE id(n)=" + nodeId + " RETURN p ORDER BY r.statisticsPostNum DESC LIMIT 50";
        JSONObject topnResult = neoSearcher.execute(topnCql, CRUD.RETRIEVE);

        return JSONTool.mergeResult(initialResult, topnResult);
    }

    @Test
    public void interactiveNetworkPathFilter() {
        String cypher = NeoSeeker.pathSeeker().setStartNodeId(11227)
                .setPlugin(PathPlugin.apocPathExpand("隶属虚拟账号|发帖|点赞|评论|转发|回复|互动", "-专题事件|-组织", 1, 3))
                .toQuery();

        JSONObject result = neoSearcher.execute(cypher, CRUD.RETRIEVE);
        System.out.println(result);
    }
}



