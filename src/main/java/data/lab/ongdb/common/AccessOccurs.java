package data.lab.ongdb.common;
/**
 * 　　　　　　　 ┏┓       ┏┓+ +
 * 　　　　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　 ┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 █████━█████  ┃+
 * 　　　　　　　┃　　　　　　 ┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　 ┃ + +
 * 　　　　　　　┗━━┓　　　 ┏━┛
 * ┃　　  ┃
 * 　　　　　　　　　┃　　  ┃ + + + +
 * 　　　　　　　　　┃　　　┃　Code is far away from     bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ +
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 */

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.common
 * @Description: TODO(选择用哪种方式与NEO4J交互)
 * @date 2019/7/11 8:24
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
