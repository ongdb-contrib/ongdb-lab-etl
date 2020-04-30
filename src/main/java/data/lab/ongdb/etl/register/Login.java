package data.lab.ongdb.etl.register;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import data.lab.ongdb.etl.common.Symbol;
import data.lab.ongdb.http.extra.HttpProxyRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.driver.*;

import java.util.Objects;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.register
 * @Description: TODO(LOGIN)
 * @date 2020/4/29 14:39
 */
public class Login {

    private static final Logger LOGGER = LogManager.getLogger(Login.class);

    private String uriStr;
    private String userName;
    private String password;
    private Uris uris;

    public Login(String uri, String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.uriStr = uri;
        // 封装URIS
        this.uris = new Uris();
        pack(uri);
        // 设置每个URIS的角色
        if (uris.size() > 1) findRole(uris);
    }

    private void findRole(Uris uris) {
        Http
        uris.all().forEach(server -> {




            Driver driver = GraphDatabase.driver(AccessPrefix.SINGLE_NODE.getSymbol() + server.getHost() + ":" + server.getPort(),
                    AuthTokens.basic(this.userName, this.password));
            try (Session session = driver.session()) {
                String role = session.writeTransaction(tx -> {
                    Result result = tx.run("CALL dbms.cluster.role YIELD role AS role");
                    return result.single().get(0).asString();
                });
                if (Role.READ_REPLICA.name().equals(role)) server.setRole(Role.READ_REPLICA);
                else server.setRole(Role.CORE);
            }
            driver.close();
        });
    }

    private void pack(String uriStr) {
        if (uriStr == null || "".equals(uriStr)) LOGGER.error("conf error:conf\\ongdb.properties");
        String[] uriArray = Objects.requireNonNull(uriStr).split(Symbol.SPLIT_CHARACTER.getSymbolValue());
        for (String uri : uriArray) {
            String[] ipPort = uri.split(":");
            String ip = ipPort[0];
            String port = ipPort[1];
            this.uris.add(new Address(ip, Integer.parseInt(port)));
        }
        LOGGER.info(new StringBuilder().append("ONgDB configuration...\r\n")
                .append("username:").append(this.userName).append(" ")
                .append("password:").append(this.password).append(" ")
                .append("uri:").append(uriStr));
    }

    public String getUriStr() {
        return uriStr;
    }

    public void setUriStr(String uriStr) {
        this.uriStr = uriStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Uris getUris() {
        return uris;
    }

    public void setUris(Uris uris) {
        this.uris = uris;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return Objects.equals(uriStr, login.uriStr) &&
                Objects.equals(userName, login.userName) &&
                Objects.equals(password, login.password) &&
                Objects.equals(uris, login.uris);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uriStr, userName, password, uris);
    }

    @Override
    public String toString() {
        return "Login{" +
                "uriStr='" + uriStr + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", uris=" + uris +
                '}';
    }
}
