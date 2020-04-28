package data.lab.ongdb.util;

import data.lab.ongdb.common.CRUD;
import data.lab.ongdb.compose.NeoComposer;
import data.lab.ongdb.search.NeoSearcher;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.v1.Config;

import java.util.concurrent.TimeUnit;

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
 * @PACKAGE_NAME: casia.isi.neo4j.util
 * @Description: TODO(NEO DUMP TEST)
 * @date 2019/12/10 14:24
 */
public class NeoImportToolTest {

    private NeoSearcher searcher;

    private final static String ipPorts = "192.168.12.19:7687";

    private JSONObject resultObject;

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("config/log4j.properties");
        // 设置持续尝试重试事务函数的最大时间
        searcher = new NeoSearcher(ipPorts, "neo4j", "123456"
                , Config.builder().withMaxTransactionRetryTime(600, TimeUnit.SECONDS).build());
//        resultObject = searcher.execute("MATCH p=()--() RETURN p LIMIT 10;", CRUD.RETRIEVE);
    }

    @Test
    public void _dump() {
        NeoImportTool.initIndex("cql", "dump.cql", false);
        boolean mark = false;
        int skip = 0;
        while (true) {
            String cypher = "match p=(n)-[*..2]-() where id(n) IN [22216] return p AS n skip " + skip + " limit 200";
            skip += 200;
            mark = NeoImportTool.dump(searcher, cypher, "cql", "dump.cql", true, false);
            System.out.println(mark);
        }
    }

    // match p=(n)-[*..6]-() where id(n) IN [230869,22216] return nodes(p) AS n skip 100 limit 10
    @Test
    public void dump() {
        NeoImportTool.initIndex("cql", "dump.cql", false);
        boolean mark = false;
//        int skip = 4000;
        int skip = 0;
        while (true) {
//            String cypher = "match p=(n)-[*2]-() where id(n) IN [230869,22216] return p AS n skip " + skip + " limit 200";

            String cypher = "match (n) where id(n) IN [230869,22216] with n\n" +
                    "match (n)-[*1]-(m:LinkedinID) with m\n" +
                    "match p=(m)-[*1]-() return p skip " + skip + " limit 200";
            skip += 200;
            mark = NeoImportTool.dump(searcher, cypher, "cql", "dump.cql", true, false);
            System.out.println(mark);
        }
    }

    @Test
    public void dump02() {
        boolean mark;
        int skip = 5400;
        while (true) {
            String cypher = "match p=(n)-[*..6]-() where id(n) IN [230869,22216] return p AS n skip " + skip + " limit 200";
            skip += 200;
            mark = NeoImportTool.dump(searcher, cypher, "cql", "dump.cql", true, true);
            System.out.println(mark);
        }
    }

    @Test
    public void dump03() {
        boolean mark;
        int skip = 1000;
        while (true) {
            String cypher = "match p=(n)-[*..6]->() where id(n) IN [230869,22216] return p AS n skip " + skip + " limit 200";
            skip += 200;
            mark = NeoImportTool.dump(searcher, cypher, "cql", "dump.cql", true, true);
            System.out.println(mark);
        }
    }

    @Test
    public void dump_1() {
        boolean mark;
        int skip = 1000;
        while (true) {
            String cypher = "match p=(n)<-[*..6]-() where id(n) IN [230869,22216] return p AS n skip " + skip + " limit 200";
            skip += 200;
            mark = NeoImportTool.dump(searcher, cypher, "cql", "dump.cql", true, true);
            System.out.println(mark);
        }
    }

    @Test
    public void dump_2() {
        boolean mark;
        int skip = 1000;
        while (true) {
            String cypher = "match p=(n)-[*..6]->() where id(n) IN [230869,22216] return p AS n skip " + skip + " limit 200";
            skip += 200;
            mark = NeoImportTool.dump(searcher, cypher, "cql", "dump.cql", true, true);
            System.out.println(mark);
        }
    }

    @Test
    public void dump_3() {
        boolean mark;
        int skip = 1000;
        while (true) {
            String cypher = "match p=(n)-[*..6]-() where id(n) IN [230869,22216] return p AS n skip " + skip + " limit 200";
            skip += 200;
            mark = NeoImportTool.dump(searcher, cypher, "cql", "dump.cql", true, true);
            System.out.println(mark);
        }
    }

    @Test
    public void dump_4() {
        boolean mark;
        int skip = 0;
        while (true) {
            String cypher = "match p=(n)<-[*..6]-() where id(n) IN [22216] return p AS n skip " + skip + " limit 200";
            skip += 200;
            mark = NeoImportTool.dump(searcher, cypher, "cql", "dump.cql", true, true);
            System.out.println(mark);
        }
    }

    @Test
    public void dump_5() {
        boolean mark;
        int skip = 0;
        while (true) {
            String cypher = "match p=(n)-[*..6]->() where id(n) IN [22216] return p AS n skip " + skip + " limit 200";
            skip += 200;
            mark = NeoImportTool.dump(searcher, cypher, "cql", "dump.cql", true, true);
            System.out.println(mark);
        }
    }

    @Test
    public void dump_6() {
        boolean mark;
        int skip = 0;
        while (true) {
            String cypher = "match p=(n)-[*..6]-() where id(n) IN [22216] return p AS n skip " + skip + " limit 200";
            skip += 200;
            mark = NeoImportTool.dump(searcher, cypher, "cql", "dump.cql", true, true);
            System.out.println(mark);
        }
    }

    @Test
    public void dump_66() {
        boolean mark;
        int skip = 0;
        while (true) {
            String cypher = "match p=(n)-[*..1]-() where id(n) IN [230869] return p AS n skip " + skip + " limit 200";
            skip += 200;
            mark = NeoImportTool.dump(searcher, cypher, "cql", "dump.cql", true, true);
            System.out.println(mark);
        }
    }

    @Test
    public void dump_67() {
        boolean mark;
        int skip = 0;
        while (true) {
            String cypher = "match p=(n)-[*..1]-() where id(n) IN [22216] return p AS n skip " + skip + " limit 200";
            skip += 200;
            mark = NeoImportTool.dump(searcher, cypher, "cql", "dump.cql", true, true);
            System.out.println(mark);
        }
    }

    @Test
    public void dumpOtherNeo4j() throws Exception {

        // 目标库
        NeoComposer targetComposer = new NeoComposer("192.168.12.19:7687", "neo4j", "123456"
                , Config.builder().withMaxTransactionRetryTime(600, TimeUnit.SECONDS).build());

        String url = "http://60.10.65.219:9125/sub_person/graph/postDetailAttr?cypher=";

        int skip = 0;
        while (true) {
            String cypher = "MATCH p=(n)--(m) WHERE id(n)=5951 RETURN p SKIP " + skip + " LIMIT 200";
            JSONObject result = NeoImportTool.queryCypher(url + cypher);
            NeoImportTool.dump(targetComposer, result);
            skip += 200;
        }
    }

    @Test
    public void dumpNeo4jLabelTree() throws Exception {

        // 目标库
        NeoComposer.HTTP_SERVICE_IS_OPEN=false;
        NeoComposer targetComposer = new NeoComposer("192.168.12.19:7687", "neo4j", "123456"
                , Config.builder().withMaxTransactionRetryTime(600, TimeUnit.SECONDS).build());

//        String url = "http://60.10.65.219:9125/sub_person/graph/postDetailAttr?cypher=";
        String url = "http://123.57.52.67:8103/subsys_zdr/graph/postDetailAttr?cypher=";

        String[] labels = new String[]{"LabelsTree", "现实人员", "TwitterID", "专题事件", "LinkedinID", "FacebookID", "InstagramID", "YouTubeID"};

        int skip = 1000;
        while (true) {
            for (String label : labels) {
                String cypher = "MATCH p=(n:" + label + ")--() RETURN p SKIP " + skip + " LIMIT 200";
                JSONObject result = NeoImportTool.queryCypher(url + cypher);
                NeoImportTool.dump(targetComposer, result);
            }
            skip += 200;
        }
    }

    @Test
    public void dumpNeo4jEventAnalysis() {
        // 目标库
        NeoComposer.HTTP_SERVICE_IS_OPEN=false;
        NeoComposer targetComposer = new NeoComposer("192.168.12.19:7687", "neo4j", "123456"
                , Config.builder().withMaxTransactionRetryTime(600, TimeUnit.SECONDS).build());

        String url = "http://123.57.52.67:8103/subsys_zdr/graph/postDetailAttr?cypher=";

        String cypher = "MATCH p=(n)-[*..6]-() WHERE ID(n)=3580833 RETURN p";
        JSONObject result = NeoImportTool.queryCypher(url + cypher);
        NeoImportTool.dump(targetComposer, result);
    }

    @Test
    public void searcher() {
        searcher = new NeoSearcher("192.168.1.12:7687", "neo4j", "123456"
                , Config.builder().withMaxTransactionRetryTime(600, TimeUnit.SECONDS).build());
        resultObject = searcher.execute("MATCH p=(:组织)--() RETURN p LIMIT 10;", CRUD.RETRIEVE);
        System.out.println(resultObject);
    }

}





