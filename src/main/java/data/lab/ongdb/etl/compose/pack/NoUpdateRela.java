package data.lab.ongdb.etl.compose.pack;
/*
 *
 * Data Lab - graph database organization.
 *
 */import data.lab.ongdb.etl.model.Entity;
import data.lab.ongdb.etl.model.RelationshipType;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.etl.compose
 * @Description: TODO(封装批量导入数据)
 * @date 2019/7/9 18:45
 */
public class NoUpdateRela extends Entity {

    /**
     * relationshipsARRAY中每个JSONObject需要包含的字段（name关系唯一标识使用关系类型即可）：
     * fields:from,type,name,properties,to
     * batchDemo:
     * [{from:473522576,type:"校友",name:"校友",properties:{name:"校友",_unique_uuid:"Asdfe=we3"},to:473569095}]
     * <p>
     * 返回每批关系构建的数量 - 构建关系之前需要先构建节点（节点的ID通过）buildBatchNodes获取
     * 或者设置为FROM-TO节点
     */
    private long from;
    private long to;
    private RelationshipType type;

    // ---★★★★★用来排重关系的属性指定KEY时，值依然保存在这个属性里面)
    private String name;
    private Map<String, Object> properties;

    private JSONObject object = new JSONObject();

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public NoUpdateRela(long from, RelationshipType type, String name, long to, Object... _key_value) {
        this.from = from;
        this.type = type;
        this.name = name;
        this.to = to;
        this.setProperties(_key_value);
        object.put("from", this.from);
        object.put("to", this.to);
        object.put("type", this.type.name());
        object.put("name", this.name);
        object.put("properties", this.properties);
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (kv.length == 1 && kv[0] instanceof JSONObject) {
            this.properties = (Map<String, Object>) kv[0];
        } else {
            for (int i = 0; i < kv.length; i++) {
                if (i == kv.length - 1) {
                    break;
                }
                Object key = kv[i];
                map.put(String.valueOf(key), kv[i + 1]);
                i += 1;
            }
            this.properties = map;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NoUpdateRela that = (NoUpdateRela) o;
        return from == that.from &&
                to == that.to &&
                Objects.equals(type, that.type) &&
                Objects.equals(name, that.name) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), from, to, type, name, properties);
    }

    @Override
    public String toString() {
        return "NoUpdateRela{" +
                "from=" + from +
                ", to=" + to +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", properties=" + properties +
                "} " + super.toString();
    }
}

