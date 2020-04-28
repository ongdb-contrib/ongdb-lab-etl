package data.lab.ongdb.common;
/*
*
 * Data Lab - graph database organization.
*
 */


/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.common
 * @Description: TODO(PATH中关系出现情况)
 * @date 2019/7/11 13:18
 */
public enum RelationOccurs {

    // PATH中必须全部是这种关系类型
    MUST_ALL("must_all"),

    // PATH中一定要包含此关系
    MUST_CONTAINS("must_contains"),

    // PATH中不包含这种关系类型
    MUST_NOT_CONTAINS("must_not_contains");

    private String symbol;

    RelationOccurs(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolValue() {
        return this.symbol;
    }
}

