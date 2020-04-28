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
import data.lab.ongdb.search.NeoSeeker;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.PropertyConfigurator;
import org.neo4j.driver.v1.Config;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.visual
 * @Description: TODO(NEWS REPRINT NETWORK TEST)
 * @date 2019/9/3 16:00
 */
public class NewsReprintVisualTest {

    private final static String ipPorts = "localhost:7687";
    private NeoSearcher neoSearcher = new NeoSearcher(ipPorts, "neo4j", "123456",
            Config.builder().withMaxTransactionRetryTime(600, TimeUnit.SECONDS).build());

    private void run() throws IOException {

        // 加载事件2的新闻转载网络
        JSONObject result = neoSearcher.execute(NeoSeeker.pathSeeker()
                        .setStartNodeId(41823)
                        .setPathLength(6)
                        .setStart(0)
                        .setRow(100)
                        .toQuery()
                , CRUD.RETRIEVE);
        // 点击此HTML查看可视化图static/index.html
        new Visualization().run(result);
    }

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configureAndWatch("config/log4j.properties");
        new NewsReprintVisualTest().run();
    }
}
