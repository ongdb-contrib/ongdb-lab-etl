package data.lab.ongdb.etl.common;
/*
 *
 * Data Lab - graph database organization.
 *
 */
/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.etl.common
 * @Description: TODO(设置数据量)
 * @date 2019/7/11 9:16
 */
public enum DataSize {

    SIZE_20_000(20_000),

    SIZE_10_000(10_000),

    SIZE_5_000(5_000),

    SIZE_1_000(1_000),

    SIZE_200(200),

    SIZE_50(50),

    SIZE_10(10),

    SIZE_5(5),

    SIZE_0(0);

    private int symbol;

    DataSize(int symbol) {
        this.symbol = symbol;
    }

    public int getSymbolValue() {
        return this.symbol;
    }
}
