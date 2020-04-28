package data.lab.ongdb.etl.util;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import data.lab.ongdb.etl.model.Label;
import data.lab.ongdb.etl.model.Property;
import data.lab.ongdb.etl.model.RelationshipType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.search
 * @Description: TODO(CYPHER语句处理工具)
 * @date 2019/7/26 11:06
 */
public class CypherTool {
    /**
     * @param
     * @return
     * @Description: TODO(拼接节点标签)
     */
    public static String appendLabels(List<Label> labels) {
        if (!labels.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < labels.size(); i++) {
                Label label = labels.get(i);
                builder.append(":" + label.name());
            }
            return builder.toString();
        }
        return "";
    }

    /**
     * @param
     * @return
     * @Description: TODO(设置属性)
     */
    public static String appendProperty(String var, Property property) {

        StringBuilder builder = new StringBuilder();
        builder.append(var + "." + property.getKey() + "=");

        Object value = property.getValue();
        if (value instanceof String) {
            builder.append("'" + value + "'");
        } else if ((value instanceof Double) || (value instanceof Long) || (value instanceof Integer)) {
            builder.append(value);
        } else {
            builder.append("'" + value + "'");
        }
        return builder.toString();
    }

    /**
     * @param
     * @return
     * @Description: TODO(设置属性)
     */
    public static String appendProperty(String var, Map<String, Object> properties) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry entry : properties.entrySet()) {
            String single = appendProperty(var, new Property(entry.getKey(), entry.getValue()));
            builder.append(single + ",");
        }
        return builder.substring(0, builder.length() - 1);
    }

    /**
     * @param
     * @return
     * @Description: TODO(拼接关系类型)
     */
    public static String appendRelationTypes(List<RelationshipType> relationshipTypes) {
        if (relationshipTypes.isEmpty()) return "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < relationshipTypes.size(); i++) {
            RelationshipType relationshipType = relationshipTypes.get(i);
            builder.append(":" + relationshipType.name() + "|");
        }
        return builder.substring(0, builder.length() - 1);
    }

    /**
     * @param
     * @return
     * @Description: TODO(拼接关系类型)
     */
    public static String appendRelationTypes(RelationshipType relationshipType) {
        if (relationshipType == null) return "";
        List<RelationshipType> relationshipTypeList = new ArrayList<>();
        relationshipTypeList.add(relationshipType);
        return appendRelationTypes(relationshipTypeList);
    }

    public static Object appendValue(Object value) {
        if (value instanceof String) {
            return "'" + value + "'";
        } else if ((value instanceof Double) || (value instanceof Long) || (value instanceof Integer)) {
            return value;
        } else {
            return "'" + value + "'";
        }
    }
}


