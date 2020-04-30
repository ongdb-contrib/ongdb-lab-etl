package data.lab.ongdb.etl.register;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.register
 * @Description: TODO(URIS)
 * @date 2020/4/29 14:41
 */
public class Uris {

    private static final Logger LOGGER = LogManager.getLogger(Uris.class);

    private List<Address> urisSet;

    private final Map<Role, List<Address>> map = new HashMap<>();

    public Uris() {
        this.urisSet = new ArrayList<>();
    }

    public static Uris pack(List<String> uri) {
        List<Address> addressList = uri.stream()
                .map(Address::pack)
                .collect(Collectors.toCollection(ArrayList::new));
        return new Uris().addAll(addressList);
    }

    public Uris addAll(List<Address> addressList) {
        this.urisSet = addressList;
        return this;
    }

    public String randomSingleNode() {
        if (!urisSet.isEmpty())
            return AccessPrefix.SINGLE_NODE.getSymbol() + urisSet.get(0).getHost() + ":" + urisSet.get(0).getPort();
        LOGGER.error("No valid-only ongdb-node!!!");
        return new Address().toString();
    }

    public List<Address> all() {
        return urisSet;
    }

    public void add(Address address) {
        urisSet.add(address);
    }

    private Map<Role, List<Address>> categoryAddress() {
        if (this.urisSet.size() > 1) this.urisSet.forEach(server -> {
            if (Role.READ_REPLICA.equals(server.getRole())) {
                if (map.containsKey(Role.READ_REPLICA)) {
                    List<Address> addressList = map.get(Role.READ_REPLICA);
                    if (!addressList.contains(server)) {
                        addressList.add(server);
                    }
                } else {
                    List<Address> addresses = new ArrayList<>();
                    addresses.add(server);
                    map.put(Role.READ_REPLICA, addresses);
                }
            } else {
                if (map.containsKey(Role.CORE)) {
                    List<Address> addressList = map.get(Role.CORE);
                    if (!addressList.contains(server)) {
                        addressList.add(server);
                    }
                } else {
                    List<Address> addresses = new ArrayList<>();
                    addresses.add(server);
                    map.put(Role.CORE, addresses);
                }
            }
        });
        return map;
    }

    public String readSingleNode() {
        if (!readSingleNodes().isEmpty()) {
            Address address = readSingleNodes().get(0);
            return AccessPrefix.SINGLE_NODE.getSymbol() + address.getHost() + ":" + address.getPort();
        }
        LOGGER.error("No read-only ongdb-node!!!");
        return new Address().toString();
    }

    public List<Address> readSingleNodes() {
        categoryAddress();
        if (!urisSet.isEmpty() && !map.isEmpty()) {
            return map.get(Role.READ_REPLICA);
        }
        return new ArrayList<>();
    }

    public String coreSingleNode() {
        if (!coreSingleNodes().isEmpty()) {
            Address address = coreSingleNodes().get(0);
            return AccessPrefix.SINGLE_NODE.getSymbol() + address.getHost() + ":" + address.getPort();
        } else {
            return randomSingleNode();
        }
    }

    public List<Address> coreSingleNodes() {
        categoryAddress();
        if (!urisSet.isEmpty() && !map.isEmpty()) {
            return map.get(Role.CORE);
        }
        LOGGER.error("No core-only ongdb-node!!!");
        return new ArrayList<>();
    }

    public int size() {
        return this.urisSet.size();
    }

    public Map<Role, List<Address>> getRoleMap() {
        return map;
    }
}

