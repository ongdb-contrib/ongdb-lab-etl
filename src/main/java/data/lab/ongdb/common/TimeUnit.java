package data.lab.ongdb.common;
/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.common
 * @Description: TODO(时间单位)
 * @date 2019/7/10 17:49
 */
public enum TimeUnit {

    // 毫秒-秒-换算单位
    MILL_SECOND_CV(1_000);

    private Object symbol;

    TimeUnit(Object symbol) {
        this.symbol = symbol;
    }

    public Object getSymbolValue() {
        return this.symbol;
    }
}
