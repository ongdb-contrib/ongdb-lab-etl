package data.lab.ongdb.etl.compose.pack;

import data.lab.ongdb.etl.common.Labels;
import data.lab.ongdb.etl.util.MD5Digest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*;
/*
 *
 * Data Lab - graph database organization.
 *
 */

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.compose.pack
 * @Description: TODO(Describe the role of this JAVA class)
 * @date 2019/8/2 12:47
 */
public class NoUpdateNodeTest {

    @Test
    public void set_unique_uuid() {
        NoUpdateNode noUpdateNode = new NoUpdateNode();
        noUpdateNode.setLabels(new Labels[]{Labels.valueOf("账号"), Labels.valueOf("TwitterID")});
        Map map = new Hashtable();
        map.put("test", 1312);
        noUpdateNode.setProperties(map);
        noUpdateNode.set_unique_uuid("123");
        System.out.println(JSONObject.parseObject(JSON.toJSONString(noUpdateNode)).toJSONString());
    }

    // 通过反射，动态修改注解的某个属性值
    @Test
    public void _unique_uuidFix() throws NoSuchFieldException, IllegalAccessException {
        NoUpdateNode noUpdateNode = new NoUpdateNode();

        Field field = noUpdateNode.getClass().getDeclaredField("_unique_uuid");
        FixUnique fixUnique = field.getAnnotation(FixUnique.class);
        InvocationHandler h = Proxy.getInvocationHandler(fixUnique);
        Field hField = h.getClass().getDeclaredField("memberValues");
        hField.setAccessible(true);
        Map memberValues = (Map) hField.get(h);
        memberValues.put("value", "ddd");
        String value = fixUnique.value();
        System.out.println(value); // ddd


        noUpdateNode.setLabels(new Labels[]{Labels.valueOf("账号"), Labels.valueOf("TwitterID")});
        Map map = new Hashtable();
        map.put("test", 1312);
        noUpdateNode.setProperties(map);
        noUpdateNode.set_unique_uuid("123");

        System.out.println(JSONObject.parseObject(JSON.toJSONString(noUpdateNode)).toJSONString());
    }

    @Test
    public void testToJson() {
        List<UpdateNode> updateNodeList = new ArrayList<>();
        // 1.Labels To JSON
//        UpdateNode node = new UpdateNode();
//        node.set_unique_uuid(MD5Digest.MD5(String.valueOf(630)));
//        node.setLabel(Labels.专题事件);
//        node.setProperties(new HashMap<>());
//        updateNodeList.add(node);
//        System.out.println(JSONArray.parseArray(JSON.toJSONString(updateNodeList)));

        // 2.Label Interface To JSON
        UpdateNode node = new UpdateNode();
        node.set_unique_uuid(MD5Digest.MD5(String.valueOf(630)));
        // 使用接口的枚举实现类传入LABEL参数，否则转换JSON错误
        // 测试未通过
        node.setLabel(() -> "事件");
        node.setProperties(new HashMap<>());
        updateNodeList.add(node);
        System.out.println(JSONArray.parseArray(JSON.toJSONString(updateNodeList)));

    }
}


