package data.lab.ongdb.update;

import data.lab.ongdb.search.NeoSearcher;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.v1.Config;

import java.io.File;
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
 * @PACKAGE_NAME: casia.isi.neo4j.update
 * @Description: TODO(Describe the role of this JAVA class)
 * @date 2019/8/13 15:48
 */
public class NeoUpdaterTest {

        private final static String ipPorts = "localhost:7687";
//    private final static String ipPorts = "192.168.12.19:7688";

//    // 默认构造函数
//    private NeoUpdater neoUpdater = new NeoUpdater(ipPorts, "neo4j", "123456");

    // 设置持续尝试重试事务函数的最大时间
    private NeoUpdater neoUpdater = new NeoUpdater(ipPorts, "neo4j", "123456"
            , Config.builder().withMaxTransactionRetryTime(600, TimeUnit.SECONDS).build());

    private NeoSearcher neoSearcher = new NeoSearcher(ipPorts, "neo4j", "123456");

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.properties");
        neoUpdater.setDEBUG(true);
    }

    @Test
    public void reset() {
    }

    @Test
    public void execute() {
    }

    @Test
    public void executeIterate_1() {
        /**
         * invoke cypherAction in batched transactions being feeded from cypherIteration running in main thread
         *
         * CALL apoc.periodic.iterate("MATCH (n:人)-[r:`隶属虚拟账号`]-(m) where r.update_time_mills=1565091464264 RETURN r",
         * "WITH {r} AS r SET r.update_time_mills=-1", {parallel:false,batchSize:1000}) YIELD
         * batches,total,timeTaken,committedOperations,failedOperations,failedBatches,retries,errorMessages,batch,operations
         * RETURN batches,total,timeTaken,committedOperations,failedOperations,failedBatches...
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
        String cypherIterate = "MATCH (n:专题事件)<-[r:`命中关键词`]-(m) RETURN n,r,m";
        String cypherAction = "WITH {n} AS n,{r} AS r,{m} AS m MERGE (n)<-[r2:`参与事件`]-(m) SET r2.name='参与事件' DELETE r";
        System.out.println(neoUpdater.executeIterate(cypherIterate, cypherAction, "batchSize", 1000, "parallel", false));
    }

}

