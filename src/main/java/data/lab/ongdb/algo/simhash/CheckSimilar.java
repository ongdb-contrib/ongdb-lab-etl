package data.lab.ongdb.algo.simhash;

import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CheckSimilar {

    /**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(this.getClass());

//	//标题和内容所占比例
//	static double title_conent_rate = 0.4;
//	//相似性阈值，大于等于该值才算相似
//	static double similarity_threshold = 0.85;
//	//标题或者正文超过这个比例，直接认定为相似
//	static double highest_rate = 0.91;
//	//标题或者正文有一个低于这个比例，直接认定为不相似
//	static double lowest_rate = 0.5;

    //标题和内容所占比例
    static double title_conent_rate = 0.4;
    //相似性阈值，大于等于该值才算相似
    static double similarity_threshold = 0.85;
    //标题或者正文超过这个比例，直接认定为相似
    static double highest_rate = 0.91;
    //正文低于这个比例，直接认定为不相似
    static double lowest_rate_content = 0.5;
    //标题低于这个比例，直接认定为不相似
    static double lowest_rate_title = 0.3;


    DecimalFormat df = new DecimalFormat("0.00");

    //simhash的海明距离与相似度的转换
    static Map<Integer, Double> rateMap = new HashMap<Integer, Double>();

    static {
        //距离小于等于2的直接认定为相似
        rateMap.put(0, 1.0);
        rateMap.put(1, 1.0);
        rateMap.put(2, 0.95);

        rateMap.put(3, 0.9);
        //rateMap.put(4, 0.8);
        //rateMap.put(5, 0.8);
        //rateMap.put(6, 0.7);
    }


    /**
     * 计算两条记录的相似性
     *
     * @return
     */
    public static double simHash(String simhash1, String simhash2) {

        if (simhash1 == null || simhash1.equals("")) {
            return -1;
        }
        if (simhash2 == null || simhash2.equals("")) {
            return -1;
        }

        double similarity = 0;

//    	int length = SimHash.hashDistance(simhash1, simhash2);
        int length = SimHash.hashDistance(simhash1, simhash2);

        Double sim = rateMap.get(length);
        if (sim == null)
            sim = 0.0;

        similarity = sim;

        return similarity;
    }

    /**
     * 计算两条记录的相似性
     *
     * @return
     */
    public static double simHash(BigInteger simhash1, BigInteger simhash2) {

        if (simhash1 == null || simhash1.equals("")) {
            return -1;
        }
        if (simhash2 == null || simhash2.equals("")) {
            return -1;
        }

        double similarity = 0;

//    	int length = SimHash.hashDistance(simhash1, simhash2);
        int length = SimHash.hammingDistance(simhash1, simhash2);

        Double sim = rateMap.get(length);
        if (sim == null)
            sim = 0.0;

        similarity = sim;

        return similarity;
    }

    public static int hamming(String s1, String s2) {
        if (s1.length() != s2.length()) return 0;
        int dis = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) dis++;
        }
        return dis;
    }


    /**
     * 计算相似度
     *
     * @return
     */
    public static boolean checkSimilarity(double sim_title, double sim_content) {


        double sim = 0;

        if ((sim_title >= 0 && sim_title <= lowest_rate_title) || (sim_content >= 0 && sim_content <= lowest_rate_content)) {
            //标题或者正文小于最小阈值，认定为不相似
            sim = 0;
        } else if (sim_title >= highest_rate) {
            //标题相似度超过阈值
            sim = sim_title;
        } else if (sim_content >= highest_rate) {
            //标题相似度超过阈值
            sim = sim_content;
        } else {

            if (sim_title > 0) {
                sim += sim_title * title_conent_rate;
            }
            if (sim_content > 0) {
                sim += sim_content * (1 - title_conent_rate);
            }


        }
        if (sim >= similarity_threshold)
            return true;
        else
            return false;

    }

}

