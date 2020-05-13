package data.lab.ongdb.etl.util;

import org.junit.Test;
/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.etl.util
 * @Description: TODO(TEST)
 * @date 2020/1/15 11:15
 */
public class ClientUtilsTest {

    @Test
    public void urlSpilt() {
        String url = "192.168.12.19:7687?useSSL=false";
        System.out.println(url.split("/?")[0]);
        System.out.println(url.split("//?")[0]);
        System.out.println(url.split("\\?")[0]);
    }

    @Test
    public void mD5Digest() {
        String cypher = "MATCH (n:PersonTest) RETURN n LIMIT 1";
        System.out.println(MD5Digest.MD5(cypher));
    }
}
