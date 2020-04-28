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

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.index
 * @Description: TODO(关系索引时的匹配模式)
 * @date 2019/8/26 15:17
 */
public class RelationPattern {

    private final static String EMPTY_STRING = "NULL";

    private Label startLabel = Label.label(EMPTY_STRING);
    private Label endLabel = Label.label(EMPTY_STRING);
    private RelationshipType relationshipType = RelationshipType.withName(EMPTY_STRING);

    public RelationPattern setStartLabel(Label startLabel) {
        this.startLabel = startLabel;
        return this;
    }

    public RelationPattern setRelation(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
        return this;
    }

    public RelationPattern setEndLabel(Label endLabel) {
        this.endLabel = endLabel;
        return this;
    }

    public String toPattern() {
        String cypher = "MATCH (:" + this.startLabel.name() + ")-[r:" + this.relationshipType.name() + "]-(:" + this.endLabel.name() + ") ";
        return cypher.replace(":"+EMPTY_STRING, "");
    }
}

