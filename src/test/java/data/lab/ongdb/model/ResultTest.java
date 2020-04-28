package data.lab.ongdb.model;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import java.io.File;
/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.model
 * @Description: TODO(Describe the role of this JAVA class)
 * @date 2019/7/11 19:41
 */
public class ResultTest {

    @Test
    public void requestDebugLogs() {
        PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.properties");
        Logger logger = Logger.getLogger(this.getClass());
//        Result.requestDebugLogs(logger,"Request error",new IllegalArgumentException());
    }

}