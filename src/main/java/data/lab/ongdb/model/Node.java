package data.lab.ongdb.model;
/*
*
 * Data Lab - graph database organization.
*
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.model
 * @Description: TODO(节点实体)
 * @date 2019/7/9 14:39
 */
public class Node extends Entity {

    /**
     * 节点标签
     **/
    private List<Label> lables;

    /**
     * 节点其它属性
     **/
    private Map<String, Object> properties;

    public List<Label> getLables() {
        return lables;
    }

    public void setLables(List<Label> lables) {
        this.lables = lables;
    }

    public void setLables(Label... lables) {
        List<Label> labelList = new ArrayList<>();
        for (int i = 0; i < lables.length; i++) {
            Label lable = lables[i];
            labelList.add(lable);
        }
        this.lables = labelList;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Node{" +
                "lables=" + lables +
                ", properties=" + properties +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Node node = (Node) o;
        return Objects.equals(lables, node.lables) &&
                Objects.equals(properties, node.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lables, properties);
    }
}

