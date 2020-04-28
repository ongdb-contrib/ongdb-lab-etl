package data.lab.ongdb.util;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.util.DateUtil
 * @Description: TODO(DateUtil)
 * @date 2020/4/28 14:35
 */
public class DateUtil {

    /**
     * @param timeMillis:毫秒时间
     * @return
     * @Description: TODO(毫秒转为时间字符串)
     */
    public static String millToTimeStr(long timeMillis) {
        Date d = new Date(timeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

    /**
     * @param date:日期STRING格式:yyyy-MM-dd HH:mm:ss
     * @return
     * @Description: TODO(日期转为毫秒)
     */
    public static long dateToMillisecond(String date) {
        long millisecond = 0;
        try {
            if (date != null && !"".equals(date)) {
                millisecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millisecond;
    }

    public static String getCurrentIndexTime() {
        Date d = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

    public static String datePlus(String completeDate, long interval) {
        String reDate = null;
        long currentDateMillisecond = 0L;

        try {
            currentDateMillisecond = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(completeDate).getTime();
            long reMillisecond = currentDateMillisecond + interval;
            reDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(reMillisecond));
        } catch (ParseException var8) {
            var8.printStackTrace();
        }

        return reDate;
    }

    public static String dateSub(String completeDate, long interval) {
        String reDate = null;
        long currentDateMillisecond = 0L;

        try {
            currentDateMillisecond = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(completeDate).getTime();
            long reMillisecond = currentDateMillisecond - interval;
            reDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(reMillisecond));
        } catch (ParseException var8) {
            var8.printStackTrace();
        }

        return reDate;
    }
}
