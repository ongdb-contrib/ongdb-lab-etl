package data.lab.ongdb.etl.compose;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import com.alibaba.fastjson.JSONObject;
import data.lab.ongdb.etl.common.CRUD;
import data.lab.ongdb.http.HttpRequestProxy;
import data.lab.ongdb.http.extra.HttpProxyRequest;
import data.lab.ongdb.http.extra.HttpRequest;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.compose
 * @Description: TODO
 * @date 2020/5/27 10:38
 */
public class NeoComposerTest_4 {

    private NeoComposer neoComposer;

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("conf" + File.separator + "log4j.properties");
        Configurator.setAllLevels("", Level.INFO);

        /**
         * @Description: setUp TODO(加载所有PRO-ongdb.properties配置)
         * @param
         * @return void
         */
        this.neoComposer = new NeoComposer();

        /**
         * TRUE-什么都不做初始化 FALSE-做一些其他操作
         * **/
        this.neoComposer = new NeoComposer(true);
    }

    @After
    public void tearDown() throws Exception {
        this.neoComposer.close();
    }

    @Test
    public void execute_01() throws Exception {
        int i = 0;
        for (; ; ) {
            i++;
            String cypher = "MERGE (n:`PRE公司中文名称` {name:'test" + i + "'})";
            JSONObject result = this.neoComposer.execute(cypher, CRUD.MERGE);
            System.out.println(result);
        }
    }

    @Test
    public void execute_02() throws Exception {
        for (; ; ) {
            String cypher = "MATCH (n) RETURN n LIMIT 10";
            JSONObject result = this.neoComposer.execute(cypher, CRUD.RETRIEVE);
            System.out.println(result);
        }
    }

    @Test
    public void http_01() {
        HttpProxyRequest httpRequest = new HttpProxyRequest("default", "ongdb", "datalab%pro");
        for (; ; ) {
            /**
             * 使用Nginx地址
             * **/
            String url = "http://10.20.13.200/db/data/transaction/commit";
            String cypher = "{\n" +
                    "    \"statements\": [\n" +
                    "        {\n" +
                    "            \"statement\": \"MATCH p=(n)--() RETURN p LIMIT 25\",\n" +
                    "            \"resultDataContents\": [\n" +
                    "                \"row\",\n" +
                    "                \"graph\"\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
            String result = httpRequest.httpPost(url, cypher);
            System.out.println(JSONObject.parseObject(result));
        }
    }
}

