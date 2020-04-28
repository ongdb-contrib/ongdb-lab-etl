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

import data.lab.ongdb.model.Entity;
import data.lab.ongdb.model.Label;

import java.util.Map;
import java.util.Objects;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.compose
 * @Description: TODO(封装批量导入数据)
 * @date 2019/7/9 18:45
 */
public class UpdateNode extends Entity {
    /**-----------------------------------------------------------------------------------------------------------------------------
     * - 批量关系节点构建接口
     * - 属性MERGE更新
     * - 适用于存量更新/新增更新的数据
     * - 属性动态/标签动态
     * - 标签动态/关系类型动态
     * - 不需要单独获取节点ID
     * - 只能设置为最子标签（如果需要父级标签需要单独的线程更新）
     **/
    /**
     * batchDemo(节点唯一标识使用生成的唯一标识name):
     * [{_unique_uuid:"sdasdsad234fdgsasdfas33",properties:{born:1978},label:"ables213"},{_unique_uuid:"ssds12ad23489gsasdfas33",properties:{born:1978,sex:"男性",age:22},label:"label31"}]
     * fields:_unique_uuid,properties,label
     */

    private Label label;

    private Map<String, Object> properties;

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        if (properties != null) {
            properties.put("_entity_name", super.get_entity_name());
            properties.put("_unique_uuid", super.get_unique_uuid());
        }
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UpdateNode that = (UpdateNode) o;
        return Objects.equals(label, that.label) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), label, properties);
    }

    @Override
    public String toString() {
        return "UpdateNode{" +
                "label=" + label +
                ", properties=" + properties +
                ", _unique_uuid=" + super.get_unique_uuid() + "} ";
    }

}

