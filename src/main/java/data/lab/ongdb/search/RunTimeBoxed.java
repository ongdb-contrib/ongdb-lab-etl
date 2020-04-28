package data.lab.ongdb.search;/**
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.search
 * @Description: TODO(CYPHER RUN TIME BOXED)
 * @date 2019/8/9 16:26
 */
public class RunTimeBoxed {

    /**
     * 事务运行时间盒配置：
     * 事务最大超时事件（设置此时间本次CYPHER执行将在指定时间内关闭事务）- 防止事务超时
     **/

    Map<String, Object> params;
    long timeout;

    public RunTimeBoxed(long timeout) {
        this.timeout = timeout;
    }

    public RunTimeBoxed(Map<String, Object> params, long timeout) {
        this.params = params;
        this.timeout = timeout;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getRunTimeBoxedCypher(String cypher) {
        if (getParams() == null) {
            return new StringBuilder()
                    .append("CALL apoc.cypher.runTimeboxed(")
                    .append("'" + cypher + "'")
                    .append(",")
                    .append("NULL")
                    .append(",")
                    .append(getTimeout())
                    .append(") YIELD value RETURN value.p AS p")
                    .toString();
        } else {
            JSONObject object = JSONObject.parseObject(JSON.toJSONString(getParams()));
            return new StringBuilder()
                    .append("CALL apoc.cypher.runTimeboxed(")
                    .append("'" + cypher + "'")
                    .append(",")
                    .append(object.toJSONString())
                    .append(",")
                    .append(getTimeout())
                    .append(") YIELD value RETURN value.p AS p")
                    .toString();
        }
    }
}


