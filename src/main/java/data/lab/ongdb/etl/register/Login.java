package data.lab.ongdb.etl.register;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import java.util.Map;
import java.util.Objects;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.register
 * @Description: TODO(LOGIN)
 * @date 2020/4/29 14:39
 */
public class Login {

    private String userName;
    private String password;
    private String initHost;
    private Map<String, String> hostMap;

    public Login(String userName, String password, String initHost, Map<String, String> hostMap) {
        this.userName = userName;
        this.password = password;
        this.initHost = initHost;
        this.hostMap = hostMap;
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

    public String getInitHost() {
        return initHost;
    }

    public void setInitHost(String initHost) {
        this.initHost = initHost;
    }

    public Map<String, String> getHostMap() {
        return hostMap;
    }

    public void setHostMap(Map<String, String> hostMap) {
        this.hostMap = hostMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Login login = (Login) o;
        return Objects.equals(userName, login.userName) &&
                Objects.equals(password, login.password) &&
                Objects.equals(initHost, login.initHost) &&
                Objects.equals(hostMap, login.hostMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password, initHost, hostMap);
    }

    @Override
    public String toString() {
        return "Login{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", initHost='" + initHost + '\'' +
                ", hostMap=" + hostMap +
                '}';
    }
}
