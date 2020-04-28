package data.lab.ongdb.etl.util;

import org.junit.Test;

/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.util
 * @Description: TODO(Describe the role of this JAVA class)
 * @date 2019/7/9 10:57
 */
public class Base64DigestTest {

    @Test
    public void encoder() {
        System.out.println("Encoder:" + data.lab.ongdb.etl.util.Base64Digest.encoder("user:123456"));
        System.out.println("Decoder:" + data.lab.ongdb.etl.util.Base64Digest.dncoder(data.lab.ongdb.etl.util.Base64Digest.encoder("user:123456")));
    }

    @Test
    public void splitTest() {
        String cypher = "4237|隶属虚拟账号";
        String[] spilt = cypher.split("\\|");
        System.out.println(spilt.length);
    }
}


