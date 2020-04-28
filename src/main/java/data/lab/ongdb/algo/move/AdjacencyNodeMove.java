package data.lab.ongdb.algo.move;/**
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

import java.util.ArrayList;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.algo.structure
 * @Description: TODO(表示一个节点以及和这个节点相连的所有节点)
 * @date 2019/8/23 15:24
 */
public class AdjacencyNodeMove {
    public String name = null;
    public ArrayList<AdjacencyNodeMove> relationNodes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<AdjacencyNodeMove> getRelationNodes() {
        return relationNodes;
    }

    public void setRelationNodes(ArrayList<AdjacencyNodeMove> relationNodes) {
        this.relationNodes = relationNodes;
    }
}
