package data.lab.ongdb.etl.model;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import java.util.Map;
import java.util.Objects;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.model.Relationship
 * @Description: TODO(关系实体)
 * @date 2020/4/28 19:32
 */
public class Relationship extends Entity {

    /**
     * 默认将关系类型和关系名称统一处理 name==type
     **/

    private Node startNode;

    private Node endNode;

    private RelationshipType relationshipType;

    /**
     * 关系的其它属性
     **/
    private Map<String, Object> properties;

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "startNode=" + startNode +
                ", endNode=" + endNode +
                ", relationshipType=" + relationshipType +
                ", properties=" + properties +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Relationship that = (Relationship) o;
        return Objects.equals(startNode, that.startNode) &&
                Objects.equals(endNode, that.endNode) &&
                Objects.equals(relationshipType, that.relationshipType) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startNode, endNode, relationshipType, properties);
    }

}

