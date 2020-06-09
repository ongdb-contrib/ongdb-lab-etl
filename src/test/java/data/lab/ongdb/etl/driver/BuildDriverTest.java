package data.lab.ongdb.etl.driver;

import org.junit.Test;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;

/*
 *
 * Data Lab - graph database organization.
 *
 */

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.driver
 * @Description: TODO
 * @date 2020/6/1 13:57
 */
public class BuildDriverTest {
    @Test
    public void build() {
        Driver driver = BuildDriver.build("neo4j", "datalab%pro");
        Result result = driver.session().run("MATCH (n) RETURN id(n) AS id LIMIT 10;");
        while (result.hasNext()){
            System.out.println(result.next().get("id"));
        }
    }
}

