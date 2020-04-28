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

import java.util.Objects;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.zdr.graph.services.graph.interactive
 * @Description: TODO(虚拟图关系ID对象)
 * @date 2019/5/16 11:37
 */
public class VirtualRelaId {

    private long startNodeId;
    private long endNodeId;
    private String virRelaName;
    private long relationshipId;

    public VirtualRelaId() {
    }

    public VirtualRelaId(long startNodeId, long endNodeId, long relationshipId, String virRelaName) {
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        this.relationshipId = relationshipId;
        this.virRelaName = virRelaName;
    }

    @Override
    public String toString() {
        return "VirtualRelaId{" +
                "startNodeId=" + startNodeId +
                ", endNodeId=" + endNodeId +
                ", virRelaName=" + virRelaName +
                ", relationshipId=" + relationshipId +
                '}';
    }

    /**
     * @param
     * @return
     * @Description: TODO(( 开始节点)->(结束节点) (结束节点)->(开始节点) 关系名一样时使用相同的关系ID )
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VirtualRelaId that = (VirtualRelaId) o;
        return ((this.startNodeId == that.startNodeId && this.endNodeId == that.endNodeId) ||
                (this.startNodeId == that.endNodeId && this.endNodeId == that.startNodeId)) &&
                (this.virRelaName.equals(that.virRelaName));
    }

    @Override
    public int hashCode() {
        return Objects.hash(startNodeId, endNodeId, virRelaName);
    }

    /**
     * @param
     * @return
     * @Description: TODO(判断是否是同一虚拟条路径)
     */
    public boolean pathEquals(long startNodeId, long endNodeId, String virRelaName) {
        if (((this.startNodeId == startNodeId && this.endNodeId == endNodeId) ||
                (this.startNodeId == endNodeId && this.endNodeId == startNodeId)) &&
                (this.virRelaName.equals(virRelaName))) {
            return true;
        }
        return false;
    }

    public long getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(long startNodeId) {
        this.startNodeId = startNodeId;
    }

    public long getEndNodeId() {
        return endNodeId;
    }

    public void setEndNodeId(long endNodeId) {
        this.endNodeId = endNodeId;
    }

    public long getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(long relationshipId) {
        this.relationshipId = relationshipId;
    }

    public String getVirRelaName() {
        return virRelaName;
    }

    public void setVirRelaName(String virRelaName) {
        this.virRelaName = virRelaName;
    }

}

