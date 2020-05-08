package data.lab.ongdb.etl.driver;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import data.lab.ongdb.etl.properties.ServerConfiguration;
import data.lab.ongdb.etl.register.AccessPrefix;
import data.lab.ongdb.etl.register.Login;
import data.lab.ongdb.http.register.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.driver
 * @Description: TODO(初始化驱动池)
 * @date 2020/5/8 10:41
 */
public class DriverPool {

    private static final Logger LOGGER = LogManager.getLogger(DriverPool.class);

    private static Map<Role, CopyOnWriteArrayList<DriverDbServer>> roleDriverListMap;
    private static Map<Role, CopyOnWriteArrayList<DbServer>> roleListMap;
    private static Login login;

    public static void init(Login login, OngdbHeartBeat heartBeat) {
        roleListMap = heartBeat.getRoleListMap();
        login = login;
        addDriver();
        runDriverThread();
    }

    /**
     * @param
     * @return
     * @Description: TODO(集群路由信息增加DRIVER)
     */
    private static void addDriver() {
        for (Role role : roleListMap.keySet()) {
            CopyOnWriteArrayList<DbServer> dbServers = roleListMap.get(role);
            CopyOnWriteArrayList<DriverDbServer> driverDbServers = dbServers.stream()
                    .map(dbServer -> {
                        Address address = getHttpAddress(dbServer.getAddressList());
                        // 本地映射
                        String serverAddressMappingLocal = address.getServerAddressMappingLocal();
                        // 远程主机名
                        String serverAddress = address.getServerAddress();
                        // 初始化驱动器
                        if (isExist(serverAddressMappingLocal)) {
                            Driver driverServerAddressMappingLocal = GraphDatabase.driver(AccessPrefix.SINGLE_NODE + serverAddressMappingLocal, AuthTokens.basic(login.getUserName(), login.getPassword()));
                            saveDriver(serverAddressMappingLocal, driverServerAddressMappingLocal);
                        }
                        if (isExist(serverAddress)) {
                            Driver driverServerAddress = GraphDatabase.driver(AccessPrefix.SINGLE_NODE + serverAddressMappingLocal, AuthTokens.basic(login.getUserName(), login.getPassword()));
                            saveDriver(driverServerAddress, driverServerAddressMappingLocal);
                        }
                        return new DriverDbServer(dbServer.getId(), dbServer.getAddressList(), dbServer.getRole(),
                                dbServer.getGroups(), dbServer.getDatabase(), dbServer.isStatus(), );
                    })
                    .collect(Collectors.toList());
            roleDriverListMap.put(role, driverDbServers);
        }
    }

    private static Address getHttpAddress(List<Address> addressList) {
        for (Address address : addressList) {
            if (Protocol.BLOT.equals(address.getProtocol())) {
                return address;
            }
        }
        return new Address("127.0.0.1", 7687, Protocol.BLOT, "localhost", true);
    }

    private static void runDriverThread() {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(
                DriverPool::addDriver, 0L, ServerConfiguration.httpDetectionInterval(), TimeUnit.SECONDS);
    }

    public static DriverDbServer getReader() {
        try {
            List<DriverDbServer> serverFollowerList = roleDriverListMap.containsKey(Role.FOLLOWER) ? roleDriverListMap.get(Role.FOLLOWER).stream()
                    .filter(DriverDbServer::isStatus).collect(Collectors.toList()) : new ArrayList<>();
            List<DriverDbServer> serverReadReplicaList = roleDriverListMap.containsKey(Role.READ_REPLICA) ? roleDriverListMap.get(Role.READ_REPLICA).stream()
                    .filter(DriverDbServer::isStatus).collect(Collectors.toList()) : new ArrayList<>();
            List<DriverDbServer> serverLeaderList = roleDriverListMap.containsKey(Role.LEADER) ? roleDriverListMap.get(Role.LEADER).stream()
                    .filter(DriverDbServer::isStatus).collect(Collectors.toList()) : new ArrayList<>();

            serverReadReplicaList.addAll(serverFollowerList);
            serverReadReplicaList.addAll(serverLeaderList);
            // 负载检测模块
            // 根据QUERY_COUNT倒排列表
            List<DriverDbServer> dbServerList = serverReadReplicaList.stream()
                    .sorted(Comparator.comparingInt(DriverDbServer::getQueryCount)).collect(Collectors.toList());

            return !dbServerList.isEmpty() ? dbServerList.get(0) : null;
        } catch (Exception e) {
            LOGGER.error("Get reader error!");
        }
        return null;
    }

    public static DriverDbServer getWriter() {
        // 写入请求只支持LEADER节点
        try {
            return roleDriverListMap.containsKey(Role.LEADER) ? roleDriverListMap.get(Role.LEADER).stream()
                    .filter(DriverDbServer::isStatus).collect(Collectors.toList()).get(0) : null;
        } catch (Exception e) {
            LOGGER.error("Get writer error!");
        }
        return null;
    }
}

