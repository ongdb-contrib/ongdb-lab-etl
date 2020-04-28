package data.lab.ongdb.update;
/*
*
 * Data Lab - graph database organization.
*
 */


import data.lab.ongdb.common.CRUD;
import data.lab.ongdb.common.NeoAccessor;
import data.lab.ongdb.model.Condition;
import data.lab.ongdb.model.Result;
import com.alibaba.fastjson.JSONObject;
import org.neo4j.driver.v1.Config;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.update
 * @Description: TODO(VertexEdgeMatrix Updater - ( CREATE / DELETE / UPDATE))
 * @date 2019/8/13 15:46
 */
public class NeoUpdater extends NeoAccessor implements Updater {
    /**
     * @param ipPorts      :服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount  :节点的用户名
     * @param authPassword :节点用户名密码
     * @return
     * @Description: TODO(构造函数 - 默认使用JAVA - DRIVER发送请求 ， D3_GRAPH格式返回数据)
     */
    public NeoUpdater(String ipPorts, String authAccount, String authPassword) {
        super(ipPorts, authAccount, authPassword);
    }

    /**
     * @param ipPorts      :服务节点的地址列表（IP:PORT）多地址使用逗号隔开
     * @param authAccount  :节点的用户名
     * @param authPassword :节点用户名密码
     * @param config
     * @return
     * @Description: TODO(构造函数 - 默认使用JAVA - DRIVER发送请求 ， D3_GRAPH格式返回数据)
     */
    public NeoUpdater(String ipPorts, String authAccount, String authPassword, Config config) {
        super(ipPorts, authAccount, authPassword, config);
    }

    /**
     * @param cypher   @return
     * @param crudType
     * @Description: TODO(跳过条件添加直接使用CYPHER查询 - 默认返回节点或者关系的所有属性字段)
     */
    @Override
    public JSONObject execute(String cypher, CRUD crudType) {
        Condition condition = new Condition();
        condition.setStatement(cypher, this.contents);
        return Result.message(chooseSendCypherWay(condition, crudType));
    }

    /**
     * @return
     * @Description: TODO(NEO4J访问对象的全局变量重置方法)
     */
    @Override
    public void reset() {

    }

    /**
     * @return
     * @Description: TODO(执行请求 - 拼接请求之后执行 - 默认返回节点或者关系的所有属性字段)
     */
    @Override
    public JSONObject execute() {
        return null;
    }
}
