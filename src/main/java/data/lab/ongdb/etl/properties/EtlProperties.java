package data.lab.ongdb.etl.properties;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.properties
 * @Description: TODO(负责加载ongdb.properties配置以及对配置的操作)
 * @date 2020/4/30 15:33
 */
public class EtlProperties {

    private static final String CONFIGRATION_PATH = "conf" + File.separator + "ongdb.properties";

    private static Properties ongdbProperties() throws IOException {
        FileInputStream inStream = new FileInputStream(new File(CONFIGRATION_PATH));
        Properties properties = new Properties();
        properties.load(inStream);
        return properties;
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过KEY前缀过滤出对应属性)
     */
    public static Properties rejector(String keyPrefixName) {
        Properties newProperties = new Properties();
        try {
            Properties properties = ongdbProperties();
            for (Object pro : properties.keySet()) {
                String key = String.valueOf(pro);
                String value = String.valueOf(properties.getProperty(key));
                if (key.contains(keyPrefixName)) {
                    newProperties.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newProperties;
    }

    /**
     * @param
     * @return
     * @Description: TODO(通过配置名拿到配置值)
     */
    public static String getConfigurationByKey(String keyOngdbHttpDetectionInterval) {
        try {
            return ongdbProperties().getProperty(keyOngdbHttpDetectionInterval);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

