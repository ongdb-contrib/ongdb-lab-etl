package data.lab.ongdb.etl.register;

import data.lab.ongdb.etl.properties.ServerConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
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
 * @PACKAGE_NAME: data.lab.ongdb.etl.register
 * @Description: TODO
 * @date 2020/4/29 14:48
 */
public class ONgDBConfigurationTest {

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("conf" + File.separator + "log4j.properties");
        Configurator.setAllLevels("", Level.INFO);
    }

    @Test
    public void getDev() {
    }

    @Test
    public void getPro() {
    }

    @Test
    public void role() {
        // 依赖于此配置文件的配置conf\ongdb.properties，区分DEV和PRO
        Uris uris = ServerConfiguration.getPro().getUris();

        // 单机模式访问=========================================================
        // 随机返回一个节点地址
        System.out.println("random:" + uris.randomSingleNode());
        // 返回一个只读节点地址
        System.out.println("read:" + uris.readSingleNode());
        // 返回一个核心节点地址
        System.out.println("core:" + uris.coreSingleNode());

        // 集群模式访问=========================================================
        System.out.println();
        // 随机返回一个节点地址
        System.out.println("random:" + uris.randomSingleNode());
        // 返回一个只读节点地址
        System.out.println("read:" + uris.readSingleNode());
        // 返回一个核心节点地址
        System.out.println("core:" + uris.coreSingleNode());
    }
}

