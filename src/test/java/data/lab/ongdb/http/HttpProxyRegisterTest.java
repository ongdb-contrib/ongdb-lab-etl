package data.lab.ongdb.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.PropertyConfigurator;
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
 * @PACKAGE_NAME: casia.isi.neo4j.http
 * @Description: TODO(Describe the role of this JAVA class)
 * @date 2019/7/9 10:13
 */
public class HttpProxyRegisterTest {

    /**
     * http访问对象 支持绝对接口地址和相对接口地址
     **/
    private HttpProxyRequest request = new HttpProxyRequest(HttpPoolSym.DEFAULT.getSymbolValue(),"neo4j", "123456");

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.properties");
        HttpProxyRegister.register("localhost:7474", "neo4j", "123456");
        String queryResult = request.httpGet("/user/neo4j");
        System.out.println(queryResult);
    }

    @Test
    public void register() {
        String query = "{\n" +
                "  \"statements\": [\n" +
                "    {\n" +
                "      \"statement\": \"MATCH p=(n)-[]-() WHERE n.name CONTAINS 'ssa' RETURN p LIMIT 10\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        String queryResult = request.httpPost("/db/data/transaction/commit", query);
        JSONObject result = JSONObject.parseObject(queryResult);
        System.out.println(result);
    }

}

