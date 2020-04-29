package data.lab.ongdb.etl.register;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.register
 * @Description: TODO(ONgDB configuration)
 * @date 2020/4/29 8:47
 */
public class ONgDBConfiguration {

    private static final String configurationPath = "conf" + File.separator + "ongdb.properties";

    private static final String DEV = "ongdb.dev.";
    private static final String PRO = "ongdb.pro.";

    private static final String URI = "uri";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public static Login getDev() {
        Properties properties = rejector(DEV);
        String uriStr = properties.getProperty(DEV + URI);
        String userName = properties.getProperty(DEV + USERNAME);
        String password = properties.getProperty(DEV + PASSWORD);
        return new Login(uriStr, userName, password);
    }

    public static Login getPro() {
        Properties properties = rejector(PRO);
        String uriStr = properties.getProperty(PRO + URI);
        String userName = properties.getProperty(PRO + USERNAME);
        String password = properties.getProperty(PRO + PASSWORD);
        return new Login(uriStr, userName, password);
    }

    private static Properties properties() throws IOException {
        FileInputStream inStream = new FileInputStream(new File(configurationPath));
        Properties properties = new Properties();
        properties.load(inStream);
        return properties;
    }

    private static Properties rejector(String key_prefix_name) {
        Properties newProperties = new Properties();
        try {
            Properties properties = properties();
            for (Object pro : properties.keySet()) {
                String key = String.valueOf(pro);
                String value = String.valueOf(properties.getProperty(key));
                if (key.contains(key_prefix_name)) {
                    newProperties.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newProperties;
    }
}

