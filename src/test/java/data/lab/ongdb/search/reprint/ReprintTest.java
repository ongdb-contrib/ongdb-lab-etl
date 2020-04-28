package data.lab.ongdb.search.reprint;

import data.lab.ongdb.common.SortOrder;
import data.lab.ongdb.search.NeoSearcher;
import data.lab.ongdb.util.FileUtil;
import data.lab.ongdb.util.JSONTool;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;

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
 * @PACKAGE_NAME: casia.isi.neo4j.search.reprint
 * @Description: TODO(REPRINT TEST)
 * @date 2019/10/15 10:21
 */
public class ReprintTest {

    private final static String ipPorts = "localhost:7687";

    private NeoSearcher neoSearcher;

    // 查询到某个事件下站点之间的转载网络
    private JSONObject result;

    @Before
    public void setUp() throws Exception {
//        neoSearcher = new NeoSearcher(ipPorts, "neo4j", "123456",
//                Config.builder().withMaxTransactionRetryTime(600, TimeUnit.SECONDS).build());
        /**
         * 转载网络预设的统计量属性储存格式：
         * statistics: [{"reprintCount":2,"start":"立场新闻","end":"中国禁闻网"},{"reprintCount":1,"start":"中国禁闻网","end":"立场新闻"}]
         */
        // 查询到某个事件下站点之间的转载网络
//        String cypher = NeoSeeker.pathSeeker()
//                .setStartNodeLabel(Label.label("SITES_REPRINT_1"))
//                .setRelationType(RelationshipType.withName("转载"))
//                .setEndNodeLabel(Label.label("SITES_REPRINT_1"))
//                .setStart(0)
//                .setRow(2000)
//                .toQuery();
//        String cypher = "match p=(n)--(m) where id(n)=1026 return p";
//        result = neoSearcher.execute(cypher, CRUD.RETRIEVE);

        String data = FileUtil.getFileContent("data", "check.json");
        result = JSONObject.parseObject(data);
        System.out.println(result.toJSONString());
    }

    @Test
    public void wasRepublished() {
        /**
         * @param result:某事件下站点之间的转载网络
         * @param sort:排序方式
         * @return
         * @Description: TODO(被转载站点 - 按照被转载数进行排序)
         */
        JSONArray newsSites = Reprint.wasRepublished(result, SortOrder.DESC);
        System.out.println(newsSites);

        // 将排序后的节点重新放入图数据
        System.out.println(JSONTool.putNodeOrRelaList(result, newsSites, "nodes"));
    }

    @Test
    public void republished() {
        /**
         * @param result:某事件下站点之间的转载网络
         * @param sort:排序方式
         * @return
         * @Description: TODO(转载站点 - 按照转载数进行排序)
         */
        JSONArray newsSites = Reprint.republished(result, SortOrder.DESC);
        System.out.println(newsSites);

        // 将排序后的节点重新放入图数据
        System.out.println(JSONTool.putNodeOrRelaList(result, newsSites, "nodes"));
    }


    @Test
    public void allRepublished() {
        /**
         * @param result:某事件下站点之间的转载网络
         * @param sort:排序方式
         *
         * @return
         * @Description: TODO(转载站点 - 按照所有转载数进行排序 - 包括转载和被转载)
         */
        JSONArray newsSites = Reprint.allRepublished(result, SortOrder.DESC);
        System.out.println(newsSites);

        // 将排序后的节点重新放入图数据
        System.out.println(JSONTool.putNodeOrRelaList(result, newsSites, "nodes"));
    }
}

