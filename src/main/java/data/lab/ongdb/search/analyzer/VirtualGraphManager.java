package data.lab.ongdb.search.analyzer;
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

import com.alibaba.fastjson.JSONObject;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.zdr.graph.services.graph.virtual
 * @Description: TODO(虚拟图IDS管理对象)
 * @date 2019/5/20 16:18
 */
public class VirtualGraphManager {

    // 从RESULT中获取NODES - KEY
    public final static String nodesFieldName = "nodes";

    // 从RESULT中获取RELATIONSHIPS - KEY
    public final static String relationshipsFieldName = "relationships";

    /**
     * 虚拟图ID都为负值
     **/

    /**
     * 自增关系ID变量 - 互动网络虚拟关系线ID都为负值
     **/
    private static long relationshipId;

    /**
     * 关系线虚拟ID管理LIST
     **/
    private final static CopyOnWriteArrayList<VirtualRelaId> virtualRelaIdManager = new CopyOnWriteArrayList<>();

    private static void clear(int virtualRelaIdSize) {
        if (virtualRelaIdManager.size() >= virtualRelaIdSize) {
            virtualRelaIdManager.clear();
        }
    }

    private static void add(VirtualRelaId virtualRelaId) {
        if (!virtualRelaIdManager.contains(virtualRelaId)) {
            virtualRelaIdManager.add(virtualRelaId);
        }
    }

    /**
     * @param targetId:开始节点ID
     * @param endId:结束节点ID
     * @return
     * @Description: TODO(从虚拟关系ID列表中获取关系ID)
     */
    private static long getVirtualRelaId(long targetId, long endId, String virRelaName) {
        return virtualRelaIdManager.parallelStream()
                .filter(v -> v.pathEquals(targetId, endId, virRelaName))
                .findAny().orElse(new VirtualRelaId()).getRelationshipId();
    }

    /**
     * @param
     * @param targetId
     * @param endId
     * @return
     * @Description: TODO(关系ID生成 - 获取关系ID)
     */
    // 并发操作可能导致生成相同的ID，加锁操作
    public synchronized static long relaIdProducer(long targetId, long endId, String virRelaName) {

        Optional optional = Optional.ofNullable(getVirtualRelaId(targetId, endId, virRelaName));
        if (optional.isPresent() && (long) optional.get() != 0) {
            return (long) optional.get();
        } else {
            clear(1000);
            add(new VirtualRelaId(targetId, endId, -++relationshipId, virRelaName));
            return -relationshipId;
        }
    }

    /**
     * @param targetId:开始节点ID
     * @param endId:结束节点ID
     * @param virRelaName:虚拟关系名称
     * @param properties:除关系名之外的其它关系属性
     * @return
     * @Description: TODO(关系对象生成)
     */
    public static JSONObject virRelas(long targetId, String virRelaName, long endId, JSONObject properties) {
        if (targetId != endId) {
            JSONObject pro = new JSONObject();
            pro.put("name", virRelaName);
            if (properties != null) {
                pro.putAll(properties);
            }
            JSONObject relationship = new JSONObject();
            relationship.put("startNode", targetId);
            relationship.put("id", relaIdProducer(targetId, endId, virRelaName));
            relationship.put("type", virRelaName);
            relationship.put("endNode", endId);
            relationship.put("properties", pro);
            return relationship;
        }
        return null;
    }

}

