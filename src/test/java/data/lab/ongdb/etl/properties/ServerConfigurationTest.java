package data.lab.ongdb.etl.properties;

import org.junit.Test;

import static org.junit.Assert.*;

/*
 *
 * Data Lab - graph database organization.
 *
 */

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.properties
 * @Description: TODO
 * @date 2020/5/8 9:57
 */
public class ServerConfigurationTest {

    @Test
    public void getDev() {
        System.out.println(ServerConfiguration.getDev().getInitHost());
    }
    @Test
    public void getPro() {
        System.out.println(ServerConfiguration.getPro().getInitHost());
    }
}
