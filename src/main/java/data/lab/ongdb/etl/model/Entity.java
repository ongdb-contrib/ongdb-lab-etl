package data.lab.ongdb.etl.model;
/*
 *
 * Data Lab - graph database organization.
 *
 */import com.alibaba.fastjson.annotation.JSONField;

import java.util.Objects;

/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.etl.model
 * @Description: TODO(关系与节点的父类实体)
 * @date 2019/7/9 14:40
 */
public class Entity {

    /**
     * 设置UUID或者MD5用来排重
     **/
    @JSONField(name = "_unique_uuid")
    public String _unique_uuid;

    /**
     * 实体名称
     **/
    @JSONField(name = "_entity_name")
    private String _entity_name;

    public String get_unique_uuid() {
        return _unique_uuid;
    }

    public void set_unique_uuid(String _unique_uuid) {
        this._unique_uuid = _unique_uuid;
    }

    public String get_entity_name() {
        return _entity_name;
    }

    public void set_entity_name(String _entity_name) {
        this._entity_name = _entity_name;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "_unique_uuid='" + _unique_uuid + '\'' +
                ", _entity_name='" + _entity_name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(_unique_uuid, entity._unique_uuid) &&
                Objects.equals(_entity_name, entity._entity_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_unique_uuid, _entity_name);
    }
}

