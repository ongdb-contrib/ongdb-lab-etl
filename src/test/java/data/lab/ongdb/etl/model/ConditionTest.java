package data.lab.ongdb.etl.model;

import data.lab.ongdb.etl.common.ResultDataContents;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.etl.model
 * @Description: TODO(Describe the role of this JAVA class)
 * @date 2019/7/10 10:16
 */
public class ConditionTest {

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.properties");
    }

    @Test
    public void setStatement() {
        Condition condition = new Condition();
//        condition.setStatement("CREATE (n {props}) RETURN n", "props", "name", "MY node", "id", 23434);
//        condition.setStatement("CREATE (n {props}) RETURN n", new Para("props", "name", "MY node", "id", 23434));

        condition.setStatement("CREATE (n {name:'Test'}) RETURN n", ResultDataContents.GRAPH);

//        List<Para> parasList = new ArrayList<>();
//        parasList.add(new Para("props", "name", "MY node", "id", 23434));
//        parasList.add(new Para("paras", "name", "MY node", "id", 23434));
//        condition.setStatement("CREATE (n {props}) SET n={paras} RETURN n", parasList);
//        condition.setStatement("CREATE (n {props}) SET n={paras} RETURN n", new Para("props", "name", "MY node", "id", 23434),
//                new Para("paras", "name", "MY node", "id", 23434));
        System.out.println(condition.toString());
    }

}


