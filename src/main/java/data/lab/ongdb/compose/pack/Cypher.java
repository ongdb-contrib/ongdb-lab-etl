package data.lab.ongdb.compose.pack;
/*
 *
 * Data Lab - graph database organization.
 *
 */import data.lab.ongdb.model.Label;
import data.lab.ongdb.model.RelationshipType;
import data.lab.ongdb.search.Property;
import data.lab.ongdb.util.CypherTool;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.compose.pack
 * @Description: TODO(Cypher)
 * @date 2019/8/1 19:54
 */
public class Cypher {

    private String cypher;

    public Cypher(String cypher) {
        this.cypher = cypher;
    }

    public static MergeNode mergeNode() {
        return new MergeNode();
    }

    public static MergeRelation mergeRelation() {
        return new MergeRelation();
    }

    public String getCypher() {
        return cypher;
    }

    public void setCypher(String cypher) {
        this.cypher = cypher;
    }

    @Override
    public String toString() {
        return "Cypher{" +
                "cypher='" + cypher + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cypher cypher1 = (Cypher) o;
        return Objects.equals(cypher, cypher1.cypher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cypher);
    }


    /**
     * @author Yc-Ma 
     * @PACKAGE_NAME: data.lab.ongdb.compose.pack.Cypher
     * @Description: TODO(合并节点)
     * @date 2019/8/29 11:00
     */
    public static class MergeNode {

        private long startId;

        private String uniqueFieldName;
        private String uniqueFieldValue;

        private Map<String, Object> properties;
        private Label label;
        private boolean isReturn;

        public MergeNode setStartId(long startId) {
            this.startId = startId;
            return this;
        }

        public MergeNode setLabel(Label label) {
            this.label = label;
            return this;
        }

        public MergeNode setUniqueField(String uniqueFieldName, String uniqueFieldValue) {
            this.uniqueFieldName = uniqueFieldName;
            this.uniqueFieldValue = uniqueFieldValue;
            return this;
        }

        public MergeNode setProperties(Object... _key_value) {
            if (_key_value.length % 2 != 0) throw new IllegalArgumentException();
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < _key_value.length; i++) {
                if (i == _key_value.length - 1) break;
                Object key = _key_value[i];
                map.put(String.valueOf(key), _key_value[i + 1]);
                i += 1;
            }
            this.properties = map;
            return this;
        }

        public MergeNode setProperties(Property... _key_value) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < _key_value.length; i++) {
                map.put(_key_value[i].getKey(), _key_value[i].getValue());
            }
            this.properties = map;
            return this;
        }

        public MergeNode setProperties(Map<String, Object> properties) {
            this.properties = properties;
            return this;
        }

        public MergeNode setReturnNodeId(boolean isReturn) {
            this.isReturn = isReturn;
            return this;
        }

        public String toMerge() {
            if (this.label != null) {
                return ("MERGE (n:" + this.label.name() + " {" + uniqueFieldName + ":" + CypherTool.appendValue(uniqueFieldValue) + "}) ") +
                        "SET " + CypherTool.appendProperty("n", this.properties) +
                        (isReturn ? " RETURN id(n) AS id " : " RETURN n");
            } else {
                return ("MATCH (n) WHERE id(n)=" + this.startId + " ") +
                        "SET " + CypherTool.appendProperty("n", this.properties) +
                        (isReturn ? " RETURN id(n) AS id " : " RETURN n");
            }
        }
    }

    /**
     * @author Yc-Ma 
     * @PACKAGE_NAME: data.lab.ongdb.compose.pack.Cypher
     * @Description: TODO(合并关系)
     * @date 2019/8/29 11:00
     */
    public static class MergeRelation {

        private long startId;
        private long endId;
        private Map<String, Object> properties;
        private RelationshipType relationshipType;
        private boolean isReturn;

        public MergeRelation setStartId(long startId) {
            this.startId = startId;
            return this;
        }

        public MergeRelation setEndId(long endId) {
            this.endId = endId;
            return this;
        }

        public MergeRelation setProperties(Object... _key_value) {
            if (_key_value.length % 2 != 0) throw new IllegalArgumentException();
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < _key_value.length; i++) {
                if (i == _key_value.length - 1) break;
                Object key = _key_value[i];
                map.put(String.valueOf(key), _key_value[i + 1]);
                i += 1;
            }
            this.properties = map;
            return this;
        }

        public MergeRelation setProperties(Property... _key_value) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < _key_value.length; i++) {
                map.put(_key_value[i].getKey(), _key_value[i].getValue());
            }
            this.properties = map;
            return this;
        }

        public MergeRelation setProperties(Map<String, Object> properties) {
            this.properties = properties;
            return this;
        }

        public MergeRelation setRelationshipType(RelationshipType relationshipType) {
            this.relationshipType = relationshipType;
            return this;
        }

        public MergeRelation setReturn(boolean aReturn) {
            isReturn = aReturn;
            return this;
        }

        public String toMerge() {
            if (this.relationshipType == null) throw new NullPointerException();
            return ("MATCH (n),(m) WHERE id(n)=" + this.startId + " AND id(m)=" + this.endId + " ") +
                    "MERGE p=(n)-[r" + CypherTool.appendRelationTypes(this.relationshipType) + "]-(m) " +
                    "SET " + CypherTool.appendProperty("r", this.properties) + " " +
                    (isReturn ? " RETURN id(r) AS id " : " RETURN p");
        }

    }

}
