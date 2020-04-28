package data.lab.ongdb.http;
/**
 * 　　　　　　　 ┏┓       ┏┓+ +
 * 　　　　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　 ┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 █████━█████  ┃+
 * 　　　　　　　┃　　　　　　 ┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　 ┃ + +
 * 　　　　　　　┗━━┓　　　 ┏━┛
 * ┃　　  ┃
 * 　　　　　　　　　┃　　  ┃ + + + +
 * 　　　　　　　　　┃　　　┃　Code is far away from     bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ +
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 */

import org.apache.http.HttpResponse;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.elasticsearch.operation.http
 * @Description: TODO(HTTP INTERFACE)
 * @date 2019/7/1 10:09
 */
public interface HttpInter {

    /**
     * @param url:支持绝对接口地址、相对接口地址、或者同时支持
     * @return
     * @Description: TODO(GET)
     */
    String httpGet(String url);

    /**
     * @param url:支持绝对接口地址、相对接口地址、或者同时支持
     * @param query:DSL查询
     * @return
     * @Description: TODO(POST)
     */
    String httpPost(String url, String query);

    /**
     * @param url:支持绝对接口地址、相对接口地址、或者同时支持
     * @param query:DSL查询
     * @return
     * @Description: TODO(PUT)
     */
    String httpPut(String url, String query);

    /**
     * @param url:支持绝对接口地址、相对接口地址、或者同时支持
     * @param query:DSL查询
     * @return
     * @Description: TODO(DELETE)
     */
    String postDeleteRequest(String url, String query);

    /**
     * @param response:HTTP RESPONSE
     * @return
     * @Description: TODO(HTTP RESPONSE压缩格式处理)
     */
    byte[] getResponseBody(HttpResponse response);
}
