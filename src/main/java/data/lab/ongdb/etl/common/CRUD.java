package data.lab.ongdb.etl.common;
/*
 *
 * Data Lab - graph database organization.
 *
 */

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.common
 * @Description: TODO(数据库操作类型)
 * @date 2019/7/18 15:48
 */
public enum CRUD {

    /**
     * 导入CSV请求
     **/
    MERGE_CSV("merge_csv"),

    /**
     * merge请求
     **/
    MERGE("merge"),

    /**
     * 新建请求
     **/
    CREATE("create"),

    /**
     * 检索请求（可包含read和write）
     **/
    RETRIEVE("retrieve"),

    /**
     * 只读请求
     **/
    RETRIEVE_READ_ONLY("retrieve_read_only"),

    /**
     * 检索属性请求
     **/
    RETRIEVE_PROPERTIES("retrieve_properties"),

    /**
     * 检索属性请求
     **/
    RETRIEVE_PROPERTIES_READ_ONLY("retrieve_properties_read_only"),

    /**
     * 更新请求
     **/
    UPDATE("update"),

    /**
     * 删除请求
     **/
    DELETE("delete"),

    /**
     * 索引请求
     **/
    INDEX("index"),

    /**
     * merge请求并返回节点ID
     **/
    MERGE_RETURN_NODE_ID("merge_return_node_id");

    private String symbol;

    CRUD(String symbol) {
        this.symbol = symbol;

    }

    public String getSymbolValue() {
        return this.symbol;
    }

}

