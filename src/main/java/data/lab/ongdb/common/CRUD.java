package data.lab.ongdb.common;
/*
 *
 * Data Lab - graph database organization.
 *
 */
/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.common
 * @Description: TODO(数据库操作类型)
 * @date 2019/7/18 15:48
 */
public enum CRUD {

    MERGE_CSV("merge_csv"),

    MERGE("merge"),

    CREATE("create"),

    RETRIEVE("retrieve"),

    RETRIEVE_PROPERTIES("retrieve_properties"),

    UPDATE("update"),

    DELETE("delete"),

    INDEX("index"),

    MERGE_RETURN_NODE_ID("merge_return_node_id");

    private String symbol;

    CRUD(String symbol) {
        this.symbol = symbol;

    }

    public String getSymbolValue() {
        return this.symbol;
    }

}

