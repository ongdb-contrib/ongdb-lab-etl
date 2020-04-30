package data.lab.ongdb.etl.compose;

import data.lab.ongdb.etl.properties.ServerConfiguration;
import data.lab.ongdb.etl.register.Login;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/*
 *
 * Data Lab - graph database organization.
 *
 */

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.compose
 * @Description: TODO
 * @date 2020/4/30 15:03
 */
public class NeoComposerTest_2 {

    // 测试
//    private static final Login login = ONgDBConfiguration.getDev();

    // 生产
    private static final Login login = ServerConfiguration.getPro();
    private static NeoComposer neoComposer = new NeoComposer(
            login.getUris().all(),
            login.getUserName(),
            login.getPassword());

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.properties");
        Configurator.setAllLevels("", Level.INFO);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void executeImport() {
    }
}