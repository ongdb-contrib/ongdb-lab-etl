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
 * @Description: TODO
 * @date 2020/4/30 15:33
 */
public class ETLProperties {

    private static final String configurationPath = "conf" + File.separator + "ongdb.properties";

    public static Properties ONgDBproperties() throws IOException {
        FileInputStream inStream = new FileInputStream(new File(configurationPath));
        Properties properties = new Properties();
        properties.load(inStream);
        return properties;
    }

}
