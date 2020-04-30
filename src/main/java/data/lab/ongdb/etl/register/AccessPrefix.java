package data.lab.ongdb.etl.register;
/*
 *
 * Data Lab - graph database organization.
 *
 */

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.register
 * @Description: TODO(ACCESS PREFIX)
 * @date 2020/4/29 14:42
 */
public enum AccessPrefix {

    SINGLE_NODE("bolt://"),
    MULTI_NODES("neo4j://"),
    MULTI_NODES_ROUTING("bolt+routing://");

    private final String symbol;

    AccessPrefix(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}

