package data.lab.ongdb.etl.driver;

import data.lab.ongdb.etl.properties.ServerConfiguration;
import org.junit.Before;
import org.junit.Test;

/*
 *
 * Data Lab - graph database organization.
 *
 */

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.driver
 * @Description: TODO
 * @date 2020/5/8 10:44
 */
public class DriverPoolTest {

    @Before
    public void init() {
        DriverPool.init(ServerConfiguration.getPro());
    }

    @Test
    public void getReader() {
        System.out.println(DriverPool.getReader());
    }

    @Test
    public void getWriter() {
        System.out.println(DriverPool.getWriter());
    }
}

