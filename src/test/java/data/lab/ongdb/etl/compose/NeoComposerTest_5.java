package data.lab.ongdb.etl.compose;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import data.lab.ongdb.etl.common.CRUD;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.compose
 * @Description: TODO
 * @date 2020/6/1 10:26
 */
public class NeoComposerTest_5 {
    private NeoComposer neoComposer;

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("conf" + File.separator + "log4j.properties");
        Configurator.setAllLevels("", Level.INFO);

        /**
         * @Description: setUp TODO(加载所有PRO-ongdb.properties配置)
         * @param
         * @return void
         */
        this.neoComposer = new NeoComposer("ongdb","ongdb%dev");
    }

    @After
    public void tearDown() throws Exception {
        this.neoComposer.close();
    }

    @Test
    public void execute() {
        int i = 0;
        for (; ; ) {
            i++;
            String cypher = "MATCH (n:`PRE公司中文名称`) RETURN n SKIP "+i+" LIMIT 1";
            JSONObject result = this.neoComposer.execute(cypher, CRUD.RETRIEVE);
            System.out.println(result);
        }
    }
}

