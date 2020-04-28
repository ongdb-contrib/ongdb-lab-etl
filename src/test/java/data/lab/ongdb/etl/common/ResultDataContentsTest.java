package data.lab.ongdb.etl.common;

import org.junit.Test;

/*
 *
 * Data Lab - graph database organization.
 *
 */

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.common
 * @Description: TODO
 * @date 2020/4/28 18:17
 */
public class ResultDataContentsTest {

    @Test
    public void getSymbolValue() {
        Weekday day = Weekday.SUN;
        if (day == Weekday.SAT || day == Weekday.SUN) {
            System.out.println("Work at home!");
        } else {
            System.out.println("Work at office!");
        }
    }
    enum Weekday {
        SUN, MON, TUE, WED, THU, FRI, SAT;
    }
}