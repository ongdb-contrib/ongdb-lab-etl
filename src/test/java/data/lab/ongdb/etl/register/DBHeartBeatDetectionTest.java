package data.lab.ongdb.etl.register;

import data.lab.ongdb.etl.properties.ServerConfiguration;
import org.junit.Test;

import java.io.IOException;

/*
 *
 * Data Lab - graph database organization.
 *
 */

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.register
 * @Description: TODO
 * @date 2020/4/30 16:07
 */
public class DBHeartBeatDetectionTest {

    // 测试
//    private static final Login login = ONgDBConfiguration.getDev();

    // 生产
    private static final Login login = ServerConfiguration.getPro();

    @Test
    public void run_1() {
        System.out.println("IS REGISTER?:" + DBHeartBeatDetection.isRegister());
        System.out.println("LEADER:" + DBHeartBeatDetection.getLeader());
    }

    @Test
    public void run_2() {
        try {
            DBHeartBeatDetection.run(login.getUris().all(), login.getUserName(), login.getPassword());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("IS REGISTER?:" + DBHeartBeatDetection.isRegister());
        System.out.println("LEADER:" + DBHeartBeatDetection.getLeader());
    }
}

