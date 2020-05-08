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

    private void addPersonCluster(String name) {
        Login login = ServerConfiguration.getPro();
//        String server = login.getUris().coreSingleNode().replace(AccessPrefix.MULTI_NODES.getSymbol(), "");
        String user = login.getUserName();
        String pwd = login.getPassword();
//        try (Driver driver = createDriverCluster(
//                AccessPrefix.MULTI_NODES.getSymbol() + server, user, pwd,
//                ServerAddress.of("pro-ongdb-2", 7687),
//                ServerAddress.of("pro-ongdb-replica-1", 7687))) {
//
//            try (Session session = driver.session()) {
//                session.run("CREATE (a:Person {name: $name})", parameters("name", name));
//            }
//        }
    }

    public static void main(String... args) {

        PropertyConfigurator.configureAndWatch("conf" + File.separator + "log4j.properties");
        Configurator.setAllLevels("", Level.INFO);
        // DEV
        ClusterConfiguration_1 example = new ClusterConfiguration_1();

        example.addPersonCluster("Test");

    }

}

