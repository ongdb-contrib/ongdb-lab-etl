package data.lab.ongdb.etl.driver;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import org.neo4j.driver.*;
import static org.neo4j.driver.Values.parameters;

/**
 * @author Yc-Ma
 * @date 2022/11/3 10:08
 */
public class HelloWorldExample implements AutoCloseable{

    private final Driver driver;

    public HelloWorldExample(String uri, String user, String password )
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
            String greeting = session.readTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "RETURN $message AS message",
                            parameters( "message", message ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }

    public static void main( String... args ) throws Exception {
        try ( HelloWorldExample greeter = new HelloWorldExample(
                "bolt://localhost:10002",
                "neo4j",
                "123qwe" ) )
        {
            greeter.printGreeting( "hello, world" );
        }
    }
}
