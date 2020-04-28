package data.lab.ongdb.compose.pack;
/*
 *
 * Data Lab - graph database organization.
 *
 */import data.lab.ongdb.common.Field;
import data.lab.ongdb.model.Entity;
import data.lab.ongdb.model.Label;
import data.lab.ongdb.model.RelationshipType;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.compose
 * @Description: TODO(封装批量导入数据)
 * @date 2019/7/9 18:46
 */
public class UpdateRela extends Entity {

    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * batchDemo:('/'符号之前是当前节点的最子层级标签)
     * [{from:"ables213-SPLIT-&-sdasdsad234fdgsasdfas33",type:"Friend",_entity_name:"Friend",properties:{_entity_name:"Friend"},to:"label31-SPLIT-&-ssds12ad23489gsasdfas33"}]
     * fields:from,type,name,properties,to
     */

    private String from;

    private RelationshipType type;
    private Map<String, Object> properties = new HashMap<>();

    private String to;

    @Override
    public String toString() {
        return "UpdateRela{" +
                "from='" + from + '\'' +
                ", type=" + type +
                ", properties=" + properties +
                ", to='" + to + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UpdateRela that = (UpdateRela) o;
        return Objects.equals(logger, that.logger) &&
                Objects.equals(from, that.from) &&
                Objects.equals(type, that.type) &&
                Objects.equals(properties, that.properties) &&
                Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), logger, from, type, properties, to);
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(Label label, String _unique_uuid) {
        addFields();
        this.from = new FromToCheck(label, _unique_uuid).toString();
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        addFields();
        this.type = type;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        addFields();
        this.properties.putAll(properties);
    }

    public String getTo() {
        return to;
    }

    public void setTo(Label label, String _unique_uuid) {
        addFields();
        this.to = new FromToCheck(label, _unique_uuid).toString();
    }

    private void addFields() {
        this.properties.put(Field.ENTITYNAME.getSymbolValue(), super.get_entity_name());
        this.properties.put(Field.UNIQUEUUID.getSymbolValue(), super.get_unique_uuid());
    }

}

