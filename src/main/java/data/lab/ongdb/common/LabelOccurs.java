package data.lab.ongdb.common;
/*
 *
 * Data Lab - graph database organization.
 *
 */
/**
 * @PACKAGE_NAME: data.lab.ongdb.common
 * @Description: TODO(PATH中标签出现情况)
 * @author Yc-Ma 
 * @date 2019/7/12 10:42
 *
 *
 */
public enum LabelOccurs {
    // PATH中必须全部是这种标签类型
    MUST_ALL("must_all"),

    // PATH中一定要包含此关系
    MUST_CONTAINS("must_contains"),

    // PATH中不包含这种标签类型
    MUST_NOT_CONTAINS("must_not_contains");

    private String symbol;

    LabelOccurs(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolValue() {
        return this.symbol;
    }
}

