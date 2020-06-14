package data.lab.ongdb.etl.driver;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import data.lab.ongdb.etl.register.Login;
import data.lab.ongdb.etl.properties.ServerConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.neo4j.driver.*;
import org.neo4j.driver.net.ServerAddress;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static org.neo4j.driver.Values.parameters;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.driver
 * @Description: TODO
 * @date 2020/4/29 9:58
 */
public class ClusterConfiguration_1 {

    private Driver createDriverCluster(String virtualUri, String user, String password, ServerAddress... addresses) {
        Config config = Config.builder()
                // 多种子连接配置
                .withResolver(address -> new HashSet<>(Arrays.asList(addresses)))
                // 事务超时时间设置
                .withMaxTransactionRetryTime(600, TimeUnit.SECONDS)
                .build();
        return (GraphDatabase.driver(virtualUri, AuthTokens.basic(user, password), config));
    }

    /**
     * @param
     * @return
     * @Description: TODO(以下测试可以看到查询只在LEADER节点运行 - 停止LEADER节点之后再重启可以自动发现节点)
     */
    private void addPersonCluster(String name) {
        Login login = ServerConfiguration.getPro();
        String user = login.getUserName();
        String pwd = login.getPassword();
        try (
//                Driver driver = createDriverCluster(
//                "neo4j://pro-ongdb-1", user, pwd,
//                ServerAddress.of("pro-ongdb-2", 7687),
//                ServerAddress.of("pro-ongdb-replica-1", 7687))

                Driver driver = createDriverCluster(
                        "neo4j://10.20.12.173", user, pwd,
                        ServerAddress.of("10.20.13.146", 7687),
                        ServerAddress.of("10.20.13.200", 7687))

        ) {

            for (int i = 0; i < 100000; i++) {
                name = name + i;
                try (Session session = driver.session()) {
                    String finalName = name;
                    session.writeTransaction(tx -> tx.run("CREATE (a:Person {name: $name})", parameters("name", finalName)));
                }
            }
        }
    }

    /**
     * @param
     * @return
     * @Description: TODO(以下测试可以看到查询在可读节点的自动路由)
     */
    private void getSomeNodes() {
        Login login = ServerConfiguration.getPro();
        String user = login.getUserName();
        String pwd = login.getPassword();
        try (
//                Driver driver = createDriverCluster(
//                "neo4j://pro-ongdb-1", user, pwd,
//                ServerAddress.of("pro-ongdb-2", 7687),
//                ServerAddress.of("pro-ongdb-replica-1", 7687))

                Driver driver = createDriverCluster(
                        "neo4j://10.20.12.173", user, pwd,
                        ServerAddress.of("10.20.13.146", 7687),
                        ServerAddress.of("10.20.13.200", 7687))

        ) {

//            for (int i = 0; i < 10000; i++) {
            try (Session session = driver.session()) {
//                int finalI = i;
                session.readTransaction(tx -> {
//                        Result result = tx.run("MATCH (n) WHERE n.name CONTAINS '" + finalI + "' RETURN n.name AS name LIMIT 10000");
                    Result result = tx.run("CALL apoc.es.query('https://vpc-knowledgegraph-4zarhbj33zcjjkqfo3afso45la.cn-north-1.es.amazonaws.com.cn','pre_org_cn_node','',null,{size:0,query:{bool:{}},aggs:{cluster_id:{terms:{field:'cluster_id',size:10,shard_size:100000,order:{_count:'DESC'}},aggs:{topHitsData:{top_hits:{size:100,_source:{includes:['name']}}}}},field_count:{cardinality:{precision_threshold:100000,field:'cluster_id'}}}}) yield value \n" +
                            "WITH value.aggregations.cluster_id.buckets AS buckets\n" +
                            "UNWIND buckets AS topHitsData\n" +
                            "RETURN topHitsData.key AS master,topHitsData.doc_count AS slaveCount,topHitsData.topHitsData.hits.hits AS slaves");
                    // Each Cypher execution returns a stream of records.
                    while (result.hasNext()) {
                        Record record = result.next();
                        // Values can be extracted from a record by index or name.
                        System.out.println(record.get("master").asInt());
                    }
                    return null;
                });
            }
//            }
        }
    }


    public static void main(String... args) {

        PropertyConfigurator.configureAndWatch("conf" + File.separator + "log4j.properties");
        Configurator.setAllLevels("", Level.INFO);
        // DEV
        ClusterConfiguration_1 example = new ClusterConfiguration_1();

        // WRITE TRANSACTION
//        example.addPersonCluster("Test");

        // READ TRANSACTION
        example.getSomeNodes();
    }
}

