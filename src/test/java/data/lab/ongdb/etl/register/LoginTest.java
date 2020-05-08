package data.lab.ongdb.etl.register;

import data.lab.ongdb.etl.properties.ServerConfiguration;
import org.junit.Test;

import static org.junit.Assert.*;

/*
 *
 * Data Lab - graph database organization.
 *
 */

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.register
 * @Description: TODO
 * @date 2020/5/8 14:41
 */
public class LoginTest {

    @Test
    public void getHostMap() {
        System.out.println(ServerConfiguration.getDev().toString());
    }
}