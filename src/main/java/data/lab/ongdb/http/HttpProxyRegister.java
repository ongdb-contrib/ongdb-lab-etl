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

import data.lab.component.http.HttpRequestProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.elasticsearch.operation.http
 * @Description: TODO(http负载均衡器配置)
 * @date 2019/6/27 15:31
 */
public class HttpProxyRegister {

    private static Logger logger = LoggerFactory.getLogger(HttpProxyRegister.class);

    /**
     * @param ipPorts:用逗号分隔的多个:IP:PORT,IP:PORT,IP:PORT,IP:PORT
     * @return
     * @Description: TODO(加载Map属性配置启动负载均衡器)
     */
    public static void register(String ipPorts) {

        Map<String, Object> configs = new HashMap<>();
        configs.put("http.poolNames", HttpPoolSym.DEFAULT.getSymbolValue());

        /**
         * 设置服务发现组件 - 通过discoverService服务发现的地址都会加入到清单中
         *
         * **/
//        configs.put("http.discoverService", new HttpDiscover());

        /**
         * health监控检查地址必须配置，否则将不会启动健康检查机制
         * 这个服务可以是一个静态图片或者html网页，也可以是一个自己实现的其他http服务（例如自己实现/health服务）
         *
         * **/
        configs.put("http.health", "/");

        /**
         * 注册地址
         *
         * **/
        configs.put("http.hosts", ipPorts.replace(" ", ""));
        HttpRequestProxy.startHttpPools(configs);

        if (logger.isInfoEnabled()) {
            logger.info(new StringBuilder().append("Register http pool[")
                    .append(HttpPoolSym.DEFAULT.getSymbolValue()).append("]").toString());
            logger.info(new StringBuilder().append("Register hosts[").append(ipPorts).append("]").toString());
        }

    }

    /**
     * @param ipPorts:用逗号分隔的多个:IP:PORT,IP:PORT,IP:PORT,IP:PORT
     * @param authAccount:用户名
     * @param authPassword:用户密码
     * @return
     * @Description: TODO(加载Map属性配置启动负载均衡器)
     */
    public static void register(String ipPorts, String authAccount, String authPassword) {

        Map<String, Object> configs = new HashMap<>();
        configs.put("http.poolNames", HttpPoolSym.DEFAULT.getSymbolValue());

        /**
         * 设置服务发现组件 - 通过discoverService服务发现的地址都会加入到清单中
         *
         * **/
//        configs.put("http.discoverService", new HttpDiscover());

        configs.put("http.authAccount", authAccount);
        configs.put("http.authPassword", authPassword);

        /**
         * health监控检查地址必须配置，否则将不会启动健康检查机制
         * 这个服务可以是一个静态图片或者html网页，也可以是一个自己实现的其他http服务（例如自己实现/health服务）
         *
         * **/
        configs.put("http.health", "/");

        /**
         * 注册地址
         *
         * **/
        configs.put("http.hosts", ipPorts.replace(" ", ""));
        HttpRequestProxy.startHttpPools(configs);

        if (logger.isInfoEnabled()) {
            logger.info(new StringBuilder().append("Register http pool[")
                    .append(HttpPoolSym.DEFAULT.getSymbolValue()).append("]").toString());
            logger.info(new StringBuilder().append("Register hosts[").append(ipPorts).append("]").toString());
        }
    }

}

