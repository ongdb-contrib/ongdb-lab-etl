package data.lab.ongdb.etl.register;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import data.lab.ongdb.etl.properties.ETLProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.driver.Driver;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.register
 * @Description: TODO(DATABASE HEARTBEAT DETECTION)
 * @date 2020/4/30 15:26
 */
public class DBHeartBeatDetection {

    private static final Logger LOGGER = LogManager.getLogger(Uris.class);

    private static final Map<Role, List<Address>> map = new HashMap<>();

    // 默认未注册心跳检测机制
    private static boolean IS_REGISTER = false;

    /**
     * @param addresses    :db addresses
     * @param authAccount
     * @param authPassword
     * @return
     * @Description: TODO(运行集群心跳检测机制)
     */
    public void run(List<Address> addresses, String authAccount, String authPassword) throws IOException {
        long delay = Long.parseLong(ETLProperties.ONgDBproperties().getProperty("ongdb.heartbeat.detection.interval"));
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
               this::classifyNode,
                0,
                delay,
                TimeUnit.SECONDS);
        LOGGER.info("ONgDB heartbeat detection run... "+"ongdb.heartbeat.detection.interval:"+delay+"s");
    }

    private void classifyNode() {

    }

    /**
     * @param
     * @return
     * @Description: TODO(获取注册的LEADER节点 - 获取失败执行重试逻辑 ( 包括执行重新注册心跳检测机制))
     */
    public static Driver getLeader() {
        return null;
    }

    /**
     * @param
     * @return
     * @Description: TODO(注册标志位)
     */
    public static boolean isRegister() {
        return IS_REGISTER;
    }

}

