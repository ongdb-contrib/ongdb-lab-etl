package data.lab.ongdb.etl.model;

import data.lab.ongdb.etl.common.Relationships;
import org.junit.Test;
/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.etl.model
 * @Description: TODO(Describe the role of this JAVA class)
 * @date 2019/7/9 18:06
 */
public class RelationshipTest {

    @Test
    public void setRelationshipType() {
        Relationship relationship = new Relationship();
        relationship.setRelationshipType(RelationshipType.withName("FRIEND"));
        relationship.setRelationshipType(Relationships.NEXT);
    }

}

