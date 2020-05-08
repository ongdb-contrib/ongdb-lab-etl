package data.lab.ongdb.etl.driver;
/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @PACKAGE_NAME: data.lab.ongdb.etl.driver
 * @Description: TODO
 * @author Yc-Ma
 * @date 2019/7/10 22:04
 */

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.neo4j.driver.*;

import java.io.File;

import static org.neo4j.driver.Values.parameters;

public class StandAloneConfiguration_2 {
    // Driver objects are thread-safe and are typically made available application-wide.
    Driver driver;

    public StandAloneConfiguration_2(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    private void addPerson(String name) {
        // Sessions are lightweight and disposable connection wrappers.
        try (Session session = driver.session()) {
            // Wrapping a Cypher Query in a Managed Transaction provides atomicity
            // and makes handling errors much easier.
            // Use `session.writeTransaction` for writes and `session.readTransaction` for reading data.
            // These methods are also able to handle connection problems and transient errors using an automatic retry mechanism.
            session.writeTransaction(tx -> tx.run("MERGE (a:Person {name: $x})", parameters("x", name)));
        }
    }

    private void printPeople(String initial) {
        try (Session session = driver.session()) {
            // A Managed Transaction transactions are a quick and easy way to wrap a Cypher Query.
            // The `session.run` method will run the specified Query.
            // This simpler method does not use any automatic retry mechanism.
            Result result = session.run(
                    "MATCH (a:Person) WHERE a.name STARTS WITH $x RETURN a.name AS name",
                    parameters("x", initial));
            // Each Cypher execution returns a stream of records.
            while (result.hasNext()) {
                Record record = result.next();
                // Values can be extracted from a record by index or name.
                System.out.println(record.get("name").asString());
            }
        }
    }

    public void close() {
        // Closing a driver immediately shuts down all open connections.
        driver.close();
    }

    public static void main(String... args) {

        PropertyConfigurator.configureAndWatch("conf" + File.separator + "log4j.properties");
        Configurator.setAllLevels("", Level.INFO);
        // DEV
//        StandAloneConfiguration_2 example = new StandAloneConfiguration_2(ServerConfiguration.getDev().getUris().randomSingleNode(),
//                ServerConfiguration.getDev().getUserName(),
//                ServerConfiguration.getDev().getPassword());
//
//        example.addPerson("Adaa");
//        example.addPerson("Alicea");
//        example.addPerson("Boba");
//        example.printPeople("A");
//        example.close();
    }
}

