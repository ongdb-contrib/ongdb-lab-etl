package data.lab.ongdb.common;
/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.common
 * @Description: TODO(NEO4J - HTTP接口地址)
 * @date 2019/7/10 11:53
 */
public enum NeoUrl {

    // NEO4J REST API
    DB_DATA_TRANSACTION_COMMIT("db/data/transaction/commit"),

    // LOCALHOST HTTP SERVICE API
    NEO_CSV("neo-import-csv");

    private String symbol;

    NeoUrl(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolValue() {
        return this.symbol;
    }
}
