package data.lab.ongdb.driver;

import data.lab.ongdb.compose.NeoComposer;
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
 * @PACKAGE_NAME: data.lab.ongdb.driver
 * @Description: TODO(Describe the role of this JAVA class)
 * @date 2019/7/25 17:47
 */
public class Neo4jDriverTest {

    private final static String ipPorts = "localhost:7687";
    private NeoComposer composer;

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.properties");
        composer = new NeoComposer(ipPorts, "neo4j", "123456", 8080);
    }

    @Test
    public void composer() {
        String cypher = new StringBuilder()
                .append("USING PERIODIC COMMIT 1 ")
                .append("LOAD CSV FROM \"http://data.neo4j.com/examples/person.csv\" AS line\n" +
                        "MERGE (n:Person {id: toInt(line[0])})\n" +
                        "SET n.name = line[1]\n" +
                        "RETURN n")
                .toString();

        Neo4jDriver.composerAutoCommit(composer.driver,cypher);
    }

    @Test
    public void config() {
//        Config.build()
//                .withConnectionAcquisitionTimeout()
//                .withConnectionLivenessCheckTimeout()
//                .withMaxTransactionRetryTime();
    }
}


