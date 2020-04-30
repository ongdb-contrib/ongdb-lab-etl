package data.lab.ongdb.etl.driver;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import data.lab.ongdb.etl.properties.ServerConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.neo4j.driver.*;

import java.io.File;

import static org.neo4j.driver.Values.parameters;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.driver
 * @Description: TODO
 * @date 2020/4/29 10:31
 */
public class StandAloneConfiguration_1 implements AutoCloseable {

    private final Driver driver;

    public StandAloneConfiguration_1(String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    public void printGreeting( final String message )
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "CREATE (a:Greeting) " +
                                    "SET a.message = $message " +
                                    "RETURN a.message + ', from node ' + id(a)",
                            parameters( "message", message ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }

    public static void main( String... args ) throws Exception
    {
        PropertyConfigurator.configureAndWatch("conf" + File.separator + "log4j.properties");
        Configurator.setAllLevels("", Level.INFO);

        // DEV
        try ( StandAloneConfiguration_1 greeter = new StandAloneConfiguration_1(
                ServerConfiguration.getDev().getUris().randomSingleNode(),
                ServerConfiguration.getDev().getUserName(),
                ServerConfiguration.getDev().getPassword() ) )
        {
            greeter.printGreeting( "hello, world" );
        }
    }
}

