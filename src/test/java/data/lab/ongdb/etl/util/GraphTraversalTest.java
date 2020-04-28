package data.lab.ongdb.etl.util;

import data.lab.ongdb.etl.common.CRUD;
import data.lab.ongdb.search.NeoSearcher;
import data.lab.ongdb.search.NeoSeeker;
import data.lab.ongdb.search._plugin.PathPlugin;
import data.lab.ongdb.visual.Visualization;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.v1.Config;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.etl.util
 * @Description: TODO(VertexEdgeMatrix Reasoning Tool)
 * @date 2019/8/19 13:44
 */
public class GraphTraversalTest {

    // private final static String ipPorts = "localhost:7687";
    // private final static String ipPorts = "192.168.12.19:7688";
    private final static String ipPorts = "192.168.7.178:7688";

    private NeoSearcher neoSearcher = new NeoSearcher(ipPorts, "neo4j", "123456",
            Config.builder().withMaxTransactionRetryTime(600, TimeUnit.SECONDS).build());

    private JSONObject result;

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.properties");
        neoSearcher.setDEBUG(true);

        // 加载多点深度推理的分析结果
        result = neoSearcher.execute(NeoSeeker.pathSeeker()
                .setStartNodeId(1405802, 22742, 20103)
                .setPlugin(PathPlugin.twinNodeAllShortestPaths())
                .setStart(0)
                .setRow(1000).toQuery(), CRUD.RETRIEVE);
    }

//    @After
//    public void tearDown() throws Exception {
//        neoSearcher.close();
//    }

    private JSONObject getDepthReasoningResult() {
        return result;
    }

    @Test
    public void depthReasoning() {
        /**
         * @param dragNodeId:分析的起始节点
         * @param result:深度推理的分析结果
         * @return
         * @Description: TODO(Depth reasoning and then drag the node)
         */
        JSONObject result = getDepthReasoningResult();
        System.out.println(result);
//        long[] dragIds = new long[]{20181, 2884470};
//        long[] dragIds = new long[]{2884470,19807};
        long[] dragIds = new long[]{2884470};
        JSONObject reasoningDragResult = GraphTraversal.depthReasoning(new long[]{1405802, 22742, 20103}, dragIds, result);
        System.out.println(reasoningDragResult == null ? null : reasoningDragResult.toJSONString());
        System.out.println(reasoningDragResult == null ? null : JSONTool.transferToOtherD3(reasoningDragResult));
    }

    @Test
    public void depthReasoning_2() throws IOException {
        // 加载多点深度推理的分析结果
        result = neoSearcher.execute(NeoSeeker.pathSeeker()
                .setStartNodeId(3214884, 3185547)
                .setPlugin(PathPlugin.twinNodeAllShortestPaths())
                .setStart(0)
                .setRow(1000).toQuery(), CRUD.RETRIEVE);

        /**
         * @param dragNodeId:分析的起始节点
         * @param result:深度推理的分析结果
         * @return
         * @Description: TODO(Depth reasoning and then drag the node)
         */
        JSONObject result = getDepthReasoningResult();
        System.out.println(result);
        long[] dragIds = new long[]{3441235};
        GraphTraversal.setDEBUG(false);
        JSONObject reasoningDragResult = GraphTraversal.depthReasoning(new long[]{3214884, 3185547}, dragIds, result);

        /**
         * 使用可视化插件校验图分析数据-可视化插件的使用方式（一）：
         * 1、将查询结果进行格式转换
         * 2、转换后的数据写入到neo-import-csv/check-graph-traversal.json文件
         * 3、启动HTTP-SERVICE:neo4j-engine-inter\src\main\java\casia\isi\neo4j\http\server\HttpService.java
         * 4、双击neo4j-engine-inter\src\main\resources\static\index.html文件使用浏览器显示
         * **/

        /**
         * 使用可视化插件校验图分析数据-可视化插件的使用方式（二）：
         * 1、直接调用可视化插件类Visualization
         * 2、然后双击static/index.html页面即可
         * **/
        System.out.println(reasoningDragResult);
        new Visualization().run(reasoningDragResult);
    }

}


