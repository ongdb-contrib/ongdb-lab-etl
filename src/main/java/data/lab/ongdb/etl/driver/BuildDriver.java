package data.lab.ongdb.etl.driver;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import data.lab.ongdb.etl.common.Symbol;
import data.lab.ongdb.etl.properties.EtlProperties;
import data.lab.ongdb.http.register.AccessPrefix;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.net.ServerAddress;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.driver
 * @Description: TODO
 * @date 2020/6/1 13:57
 */
public class BuildDriver {

    private final static String[] BOLTS = EtlProperties.getConfigurationByKey("ongdb.pro.uri.bolt").split(Symbol.SPLIT_CHARACTER.getSymbolValue());

    private final static long withMaxTransactionRetryTime = Long.parseLong(EtlProperties.getConfigurationByKey("ongdb.withMaxTransactionRetryTime"));

    private static Driver createDriverCluster(String virtualUri, String user, String password, List<ServerAddress> addresses) {
        Config config = Config.builder()
                // 多种子连接配置
                .withResolver(address -> new HashSet<>(addresses))
                // 事务超时时间设置
                .withMaxTransactionRetryTime(withMaxTransactionRetryTime, TimeUnit.SECONDS)
                .build();
        return (GraphDatabase.driver(virtualUri, AuthTokens.basic(user, password), config));
    }

    public static Driver build(String user, String password) {

        // 多节点运行监控线程
        if (BOLTS.length > 1) {

            // 多节点添加BLOT ROUTING驱动
            return addRoutingBlotDriver(user, password);

        } else {
            // 单节点DRIVER
            return addBlotDriver(user, password);
        }
    }

    private static Driver addBlotDriver(String authAccount, String authPassword) {
        String address = BOLTS[0];
        return GraphDatabase.driver(AccessPrefix.SINGLE_NODE.getSymbol() + address, AuthTokens.basic(authAccount, authPassword));
    }

    private static Driver addRoutingBlotDriver(String authAccount, String authPassword) {
        List<ServerAddress> addresses = getServerAddress();
        String virtualUri = BOLTS[0].split(Symbol.COLON.getSymbolValue())[0];
        return createDriverCluster(AccessPrefix.MULTI_NODES.getSymbol() + virtualUri, authAccount, authPassword, addresses);
    }

    private static List<ServerAddress> getServerAddress() {
        List<ServerAddress> addresses = new ArrayList<>();
        for (String bolt : BOLTS) {
            String[] ipPort = bolt.split(Symbol.COLON.getSymbolValue());
            addresses.add(ServerAddress.of(ipPort[0], Integer.parseInt(ipPort[1])));
        }
        return addresses;
    }
}

