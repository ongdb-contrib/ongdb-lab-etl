package data.lab.ongdb.index;
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

import data.lab.ongdb.model.Label;
import data.lab.ongdb.model.RelationshipType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.index
 * @Description: TODO(全文检索参数对象)
 * @date 2019/8/8 11:13
 */
public class FullTextMap {


    public static FullTextNodeMap initFullTextNode() {
        return new FullTextNodeMap();
    }

    public static FullTextRelationMap initFullTextRelation() {
        return new FullTextRelationMap();
    }

    public static class FullTextNodeMap {
        private Map<Label, String[]> fullTextMap = new HashMap<>();

        public FullTextNodeMap add(Label label, String... properties) {
            this.fullTextMap.put(label, properties);
            return this;
        }

        public Map<Label, String[]> getFullTextMap() {
            return fullTextMap;
        }
    }

    public static class FullTextRelationMap {
        private Map<RelationshipType, String[]> fullTextMap = new HashMap<>();

        public FullTextRelationMap add(RelationshipType label, String... properties) {
            this.fullTextMap.put(label, properties);
            return this;
        }

        public Map<RelationshipType, String[]> getFullTextMap() {
            return fullTextMap;
        }
    }
}

