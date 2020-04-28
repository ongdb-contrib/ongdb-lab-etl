package data.lab.ongdb.visual;

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
import data.lab.ongdb.common.CRUD;
import data.lab.ongdb.search.NeoSearcher;
import data.lab.ongdb.util.GraphTraversal;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.PropertyConfigurator;
import org.neo4j.driver.v1.Config;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.visual
 * @Description: TODO(Visualization Plugin Test)
 * @date 2019/8/22 10:33
 */
public class VisualizationTest_02 {

     private final static String ipPorts = "192.168.12.19:7687";

    private NeoSearcher neoSearcher = new NeoSearcher(ipPorts, "neo4j", "123456",
            Config.builder().withMaxTransactionRetryTime(600, TimeUnit.SECONDS).build());

    private void run() throws IOException {

        // 加载数据
        String cypher="MATCH p=(:LabelsTree)--() RETURN p";
        JSONObject result=neoSearcher.execute(cypher,CRUD.RETRIEVE);
        System.out.println(result);

        /**
         * 使用可视化插件校验图分析数据：
         * 1、直接调用可视化插件类Visualization
         * 2、然后双击static/index.html页面即可
         * **/
        System.out.println(result);
        new Visualization().run(result);
    }

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configureAndWatch("config/log4j.properties");
        GraphTraversal.setDEBUG(true);
        new VisualizationTest_02().run();
    }
}


