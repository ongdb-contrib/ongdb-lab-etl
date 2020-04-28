package data.lab.ongdb.compose.pack;
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

import data.lab.ongdb.common.Labels;
import data.lab.ongdb.model.Entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.compose
 * @Description: TODO(封装批量导入数据)
 * @date 2019/7/9 18:45
 */
public class NoUpdateNode extends Entity {

    /**-----------------------------------------------------------------------------------------------------------------------------
     * - 属性MERGE不更新
     * - 适用于存量不更新的数据
     * - 属性动态/标签动态
     * - 标签动态/关系类型动态
     *
     * - 可指定需要用来排重的字段
     * **/

    /**
     * batchDemo(节点唯一标识使用生成的唯一标识name):
     * [{labels:["label11","label21"],_unique_uuid:"sdasdsad234fdgsasdfas33",properties:{born:1978}},
     * {labels:["label31","label41"],_unique_uuid:"ssds12ad23489gsasdfas33",properties:{born:1978,age:22,sex:"男性"}}]
     * fields:labels,_unique_uuid,properties
     */

    private Labels[] labels;
    private Map<String, Object> properties;

    public NoUpdateNode() {
    }

    public NoUpdateNode(Labels[] labels, String _unique_uuid, Object... _key_value) {
        this.labels = labels;
        super._unique_uuid = _unique_uuid;
        this.setProperties(_key_value);
    }

    public Labels[] getLabels() {
        return labels;
    }

    public void setLabels(Labels[] labels) {
        this.labels = labels;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void setProperties(Object... _key_value) {
        Object[] kv = _key_value;
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < kv.length; i++) {
            if (i == kv.length - 1) break;
            Object key = kv[i];
            map.put(String.valueOf(key), kv[i + 1]);
            i += 1;
        }
        this.properties = map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NoUpdateNode that = (NoUpdateNode) o;
        return Arrays.equals(labels, that.labels) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), properties);
        result = 31 * result + Arrays.hashCode(labels);
        return result;
    }

    @Override
    public String toString() {
        return "NoUpdateNode{" +
                "labels=" + Arrays.toString(labels) +
                ", properties=" + properties +
                "} " + super.toString();
    }
}
