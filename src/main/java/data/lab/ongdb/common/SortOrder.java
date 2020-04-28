package data.lab.ongdb.common;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.common.SortOrder
 * @Description: TODO(设置排序方式)
 * @date 2019/7/13 16:26
 */
public enum SortOrder {
    /**
     * 正序
     */
    ASC("asc"),
    /**
     * 倒序
     */
    DESC("desc");

    private String symbol;

    SortOrder(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolValue() {
        return this.symbol;
    }
}
