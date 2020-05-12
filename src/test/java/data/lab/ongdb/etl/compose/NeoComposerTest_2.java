package data.lab.ongdb.etl.compose;

import com.alibaba.fastjson.JSONObject;
import data.lab.ongdb.etl.common.CRUD;
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
    private NeoComposer neoComposer;

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("conf" + File.separator + "log4j.properties");
        Configurator.setAllLevels("", Level.INFO);
        /**
         * @param login:LOGIN对象
         * @param IS_PRINT_CLUSTER_INFO:是否打印集群路由信息
         * @param IS_ADD_BLOT_DRIVER:是否自动添加BLOT驱动
         * @param httpDetectionInterval:服务监测的时间间隔-秒
         * @return
         * @Description: TODO(构造函数 - 默认使用JAVA - DRIVER发送请求 ， D3_GRAPH格式返回数据)
         */
        this.neoComposer = new NeoComposer(login, true, true, ServerConfiguration.httpDetectionInterval());
    }

    @After
    public void tearDown() throws Exception {
        this.neoComposer.close();
    }

    @Test
    public void execute() {
        for (; ; ) {
            String cypher = "MATCH (n) RETURN n LIMIT 1000";
            JSONObject result = this.neoComposer.execute(cypher, CRUD.RETRIEVE);
            System.out.println(result);
        }
    }
}

