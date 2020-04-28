package data.lab.ongdb.common;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.common.Symbol
 * @Description: TODO(枚举类型, 符号)
 * @date 2019/7/9 9:26
 */
public enum Symbol {

    SPECIAL_SPLIT("-SPLIT-&-"),

    DIVIDE_SPLIT("/"),

    COMMA_CHARACTER(",");

    private String symbol;

    Symbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolValue() {
        return this.symbol;
    }

}