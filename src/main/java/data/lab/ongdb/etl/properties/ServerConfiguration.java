package data.lab.ongdb.etl.properties;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import data.lab.ongdb.etl.common.Symbol;
import data.lab.ongdb.etl.register.Login;

import java.util.*;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.register
 * @Description: TODO(ONgDB configuration - 负责加载DEV和PRO配置)
 * @date 2020/4/29 8:47
 */
public class ServerConfiguration {

    private static final String DEV = "ongdb.dev.";
    private static final String PRO = "ongdb.pro.";

    private static final String URI = "uri";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static final String KEY_ONGDB_HTTP_DETECTION_INTERVAL = "ongdb.http.detection.interval";
    private static final String KEY_ONGDB_withMaxTransactionRetryTime = "ongdb.withMaxTransactionRetryTime";
    private static final String KEY_ONGDB_heartHealthDetect = "ongdb.heartHealthDetect";
    private static final String KEY_ONGDB_HTTP_timeOut = "ongdb.http.timeOut";
    private static final String KEY_PRO_ONGDB_URI_BOLT = "ongdb.pro.uri.bolt";
    private static final String KEY_DEV_ONGDB_URI_BOLT = "ongdb.dev.uri.bolt";

    public static String uriBolt() {
        String uriBolt = Objects.requireNonNull(EtlProperties.getConfigurationByKey(KEY_PRO_ONGDB_URI_BOLT));
        if (!"".equals(uriBolt)) {
            return uriBolt;
        }
        return uriDevBolt();
    }

    private static String uriDevBolt() {
        return Objects.requireNonNull(EtlProperties.getConfigurationByKey(KEY_DEV_ONGDB_URI_BOLT));
    }

    public static int httpDetectionInterval() {
        return Integer.parseInt(Objects.requireNonNull(EtlProperties.getConfigurationByKey(KEY_ONGDB_HTTP_DETECTION_INTERVAL)));
    }

    public static int withMaxTransactionRetryTime() {
        return Integer.parseInt(Objects.requireNonNull(EtlProperties.getConfigurationByKey(KEY_ONGDB_withMaxTransactionRetryTime)));
    }

    public static int heartHealthDetect() {
        return Integer.parseInt(Objects.requireNonNull(EtlProperties.getConfigurationByKey(KEY_ONGDB_heartHealthDetect)));
    }

    public static int httpTimeOut() {
        return Integer.parseInt(Objects.requireNonNull(EtlProperties.getConfigurationByKey(KEY_ONGDB_HTTP_timeOut)));
    }

    public static Login getDev() {
        Properties properties = EtlProperties.rejector(DEV);
        String uriStr = properties.getProperty(DEV + URI);
        String userName = properties.getProperty(DEV + USERNAME);
        String password = properties.getProperty(DEV + PASSWORD);
        String initHost = packInitHost(uriStr);
        Map<String, String> hostMap = packHostMap(uriStr);
        return new Login(userName, password, initHost, hostMap);
    }

    public static Login getPro() {
        Properties properties = EtlProperties.rejector(PRO);
        String uriStr = properties.getProperty(PRO + URI);
        String userName = properties.getProperty(PRO + USERNAME);
        String password = properties.getProperty(PRO + PASSWORD);
        String initHost = packInitHost(uriStr);
        Map<String, String> hostMap = packHostMap(uriStr);
        return new Login(userName, password, initHost, hostMap);
    }

    private static Map<String, String> packHostMap(String uriStr) {
        Map<String, String> hostMap = new HashMap<>();
        String[] servers = uriStr.split(Symbol.SPLIT_CHARACTER.getSymbolValue());
        for (String server : servers) {
            String[] array = server.split(Symbol.COMMA_CHARACTER.getSymbolValue());
            String host = array[0];
            String initHost = array[1].split(Symbol.COLON.getSymbolValue())[0];
            hostMap.put(host, initHost);
        }
        return hostMap;
    }

    private static String packInitHost(String uriStr) {
        StringBuilder builder = new StringBuilder();
        String[] servers = uriStr.split(Symbol.SPLIT_CHARACTER.getSymbolValue());
        for (String server : servers) {
            String[] array = server.split(Symbol.COMMA_CHARACTER.getSymbolValue());
            builder.append(array[1]).append("|");
        }
        return builder.substring(0, builder.length() - 1);
    }

}


