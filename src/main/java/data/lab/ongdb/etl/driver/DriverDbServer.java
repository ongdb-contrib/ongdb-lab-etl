package data.lab.ongdb.etl.driver;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import com.alibaba.fastjson.JSONArray;
import data.lab.ongdb.http.register.Address;
import data.lab.ongdb.http.register.DbServer;
import data.lab.ongdb.http.register.Role;
import org.neo4j.driver.Driver;

import java.util.List;
import java.util.Objects;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.driver
 * @Description: TODO
 * @date 2020/5/8 10:59
 */
public class DriverDbServer extends DbServer {
    /**
     * BLOT驱动器-本地访问驱动
     **/
    private Driver driverServerAddressMappingLocal;

    /**
     * BLOT驱动器-远程映射驱动
     **/
    private Driver driverServerAddress;

    public DriverDbServer(String id, List<Address> addressList, Role role, JSONArray groups, String database, boolean status, Driver driverServerAddressMappingLocal, Driver driverServerAddress) {
        super(id, addressList, role, groups, database, status);
        this.driverServerAddressMappingLocal = driverServerAddressMappingLocal;
        this.driverServerAddress = driverServerAddress;
    }

    public Driver getDriverServerAddressMappingLocal() {
        return driverServerAddressMappingLocal;
    }

    public void setDriverServerAddressMappingLocal(Driver driverServerAddressMappingLocal) {
        this.driverServerAddressMappingLocal = driverServerAddressMappingLocal;
    }

    public Driver getDriverServerAddress() {
        return driverServerAddress;
    }

    public void setDriverServerAddress(Driver driverServerAddress) {
        this.driverServerAddress = driverServerAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        DriverDbServer that = (DriverDbServer) o;
        return Objects.equals(driverServerAddressMappingLocal, that.driverServerAddressMappingLocal) &&
                Objects.equals(driverServerAddress, that.driverServerAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), driverServerAddressMappingLocal, driverServerAddress);
    }

    @Override
    public String toString() {
        return "DriverDbServer{" +
                "driverServerAddressMappingLocal=" + driverServerAddressMappingLocal +
                ", driverServerAddress=" + driverServerAddress +
                "} " + super.toString();
    }

}
