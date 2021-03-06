package data.lab.ongdb.etl.model;
/*
 *
 * Data Lab - graph database organization.
 *
 */import data.lab.ongdb.etl.common.TimeUnit;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.etl.model
 * @Description: TODO(所有接口返回结果封装)
 * @date 2019/7/9 19:22
 */
public class Result {

    private static final Logger LOGGER = LogManager.getLogger(Result.class);

    /**
     * @param
     * @return
     * @Description: TODO(富化单个请求 - 成功响应即为TRUE)
     */
    public static JSONObject message(JSONObject result) {
        JSONArray errors = result.getJSONArray("errors");
        if (errors == null || errors.isEmpty()) {
            result.put("message", true);
        } else {
            result.put("message", false);
        }
        return result;
    }

    /**
     * @param result:查询结果
     * @param pathGraphIsVirtual:是否分析虚拟图
     * @return
     * @Description: TODO(富化单个请求)
     */
    public static JSONObject message(JSONObject result, boolean pathGraphIsVirtual) {
        JSONArray errors = result.getJSONArray("errors");
        if (errors == null || errors.isEmpty()) {
            result.put("message", true);
        } else {
            result.put("message", false);
        }
        return result;
    }

    /**
     * @param
     * @return
     * @Description: TODO(对请求结果列表进行统计)
     */
    public static JSONObject statistics(JSONArray queryResultList) {
        JSONObject object = new JSONObject();
        JSONObject count = getCount(queryResultList);
        object.put("successful", count.getIntValue("successfulCount"));
        object.put("failed", count.getIntValue("failedCount"));
        object.put("totalQuery", queryResultList.size());
        object.put("queryResultList", queryResultList);
        return object;
    }

    private static JSONObject getCount(JSONArray queryResultList) {
        int successfulCount = 0;
        int failedCount = 0;
        for (int i = 0; i < queryResultList.size(); i++) {
            JSONObject result = queryResultList.getJSONObject(i);
            boolean message = result.getBoolean("message");
            if (message) {
                successfulCount++;
            } else {
                failedCount++;
            }
        }
        JSONObject object = new JSONObject();
        object.put("successfulCount", successfulCount);
        object.put("failedCount", failedCount);
        return object;
    }

    public static String statisticsConsume(long startMill, long stopMill, int requestSendSize) {
        if (requestSendSize == 0) requestSendSize = 1;
        String consumeMessage = new StringBuilder().append("Total consume ")
                .append((stopMill - startMill) / Integer.parseInt(String.valueOf(TimeUnit.MILL_SECOND_CV.getSymbolValue())))
                .append("s,average consume ")
                .append(((stopMill - startMill) / Integer.parseInt(String.valueOf(TimeUnit.MILL_SECOND_CV.getSymbolValue()))) / requestSendSize)
                .append("s/request").toString();
        LOGGER.info(consumeMessage);
        return consumeMessage;
    }

}

