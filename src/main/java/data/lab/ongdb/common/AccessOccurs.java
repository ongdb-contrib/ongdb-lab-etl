package data.lab.ongdb.common;
/*
 *
 * Data Lab - graph database organization.
 *
 */

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.common.AccessOccurs
 * @Description: TODO(Describe the role of this JAVA class)
 * @date 2020/4/28 13:51
 */
public enum AccessOccurs {

    /**
     * 所有接口默认使用HTTP-REST-API方式访问
     **/

    // 使用HTTP访问（支持多种格式返回数据ROW/GRAPH/ROW_GRAPH/D3_GRAPH）
    RESTFUL_API("restful_api"),

    // 使用JAVA-DRIVER访问（支持一种格式返回数据D3_GRAPH）
    JAVA_DRIVER("java_driver"),

    // 使用JAVA-DRIVER访问（支持一种格式返回数据D3_GRAPH）
    JDBC("jdbc");

    private String symbol;

    AccessOccurs(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolValue() {
        return this.symbol;
    }

}
