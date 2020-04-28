package data.lab.ongdb.common;
/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.common
 * @Description: TODO(核心字段 - 节点与关系公用)
 * @date 2019/7/9 19:58
 */
public enum Field {

    // 实体唯一标记字段名（节点/关系）
    UNIQUEUUID("_unique_uuid"),

    // 实体名称（节点/关系）
    ENTITYNAME("_entity_name");

    private String symbol;

    Field(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolValue() {
        return this.symbol;
    }
}
