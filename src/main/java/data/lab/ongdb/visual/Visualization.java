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

import data.lab.ongdb.http.server.HttpService;
import data.lab.ongdb.util.FileUtil;
import data.lab.ongdb.util.JSONTool;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.visual
 * @Description: TODO(Visualization Plugin Run)
 * @date 2019/8/22 10:14
 */
public class Visualization {

    private final static String VISUAL_DATA_PATH = "neo-import-csv/check-graph-traversal.json";

    /**
     * @param queryResult:检索工具的查询结果
     * @return
     * @Description: TODO(Run graph data visualization)
     */
    public void run(JSONObject queryResult) throws IOException {
        JSONObject result = JSONTool.transferToOtherD3(queryResult);
        FileUtil.writeFileByNewFile(VISUAL_DATA_PATH, result.toJSONString());
        new HttpService().run();
    }

    /**
     * @param queryResult:检索工具的查询结果
     * @param port:指定HTTP服务的端口启动    - 需要对应在neo4j-engine-inter\src\main\resources\static\js\graph.js中修改HTTP请求的端口
     * @return
     * @Description: TODO(Run graph data visualization)
     */
    public void run(JSONObject queryResult, int port) throws IOException {
        JSONObject result = JSONTool.transferToOtherD3(queryResult);
        FileUtil.writeFileByNewFile(VISUAL_DATA_PATH, result.toJSONString());
        new HttpService().run(port);
    }

}
