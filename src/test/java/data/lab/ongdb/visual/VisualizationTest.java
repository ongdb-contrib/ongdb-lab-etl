package data.lab.ongdb.visual;

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

import data.lab.ongdb.algo.move.GraphTraversalMove;
import data.lab.ongdb.common.CRUD;
import data.lab.ongdb.search.NeoSearcher;
import data.lab.ongdb.search.NeoSeeker;
import data.lab.ongdb.search._plugin.PathPlugin;
import data.lab.ongdb.util.GraphTraversal;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.PropertyConfigurator;
import org.neo4j.driver.v1.Config;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.visual
 * @Description: TODO(Visualization Plugin Test)
 * @date 2019/8/22 10:33
 */
public class VisualizationTest {

    // private final static String ipPorts = "localhost:7687";
    // private final static String ipPorts = "192.168.12.19:7688";
    private final static String ipPorts = "192.168.7.178:7688";

//    private final static long[] startNodeIds = new long[]{3214884, 3185547};
//    private final static long[] dragNodeIds = new long[]{3441235};

    private final static long[] startNodeIds = new long[]{22701,20094};
    private final static long[] dragNodeIds = new long[]{19776};

//    private final static long[] startNodeIds = new long[]{19795, 19903, 20103};
//        private final static long[] dragNodeIds = new long[]{19891};
//    private final static long[] dragNodeIds = new long[]{20181, 20237, 19792, 20381, 19885, 19807, 19807, 2710666, 20219, 20219};

    private NeoSearcher neoSearcher = new NeoSearcher(ipPorts, "neo4j", "123456",
            Config.builder().withMaxTransactionRetryTime(600, TimeUnit.SECONDS).build());

    private void run() throws IOException {
        // 加载多点深度推理的分析结果
        neoSearcher.setDEBUG(true);
        JSONObject result = neoSearcher.execute(NeoSeeker.pathSeeker()
                .setStartNodeId(startNodeIds)
                .setPlugin(PathPlugin.twinNodeAllShortestPaths())
                .setStart(0)
                .setRow(1000).toQuery(), CRUD.RETRIEVE);

//        JSONObject result =JSONObject.parseObject("{\"totalNodeSize\":4,\"totalRelationSize\":2,\"results\":[{\"data\":[{\"graph\":{\"relationships\":[{\"startNode\":3341546,\"id\":6236548,\"type\":\"关注\",\"endNode\":3139228,\"properties\":{\"name\":\"关注\"}},{\"startNode\":3441235,\"id\":6235530,\"type\":\"关注\",\"endNode\":3214884,\"properties\":{\"name\":\"关注\"}}],\"nodes\":[{\"img\":\"./images/img/qt.svg\",\"id\":3139228,\"text\":\"Google Android\",\"properties\":{\"imgurl\":\"http://media.licdn.com/mpr/mpr/shrinknp_100_100/p/1/000/01f/3e7/313a8bb.png\",\"name\":\"Google Android\",\"lnkurl\":\"https://www.linkedin.com/groups/\"},\"labels\":[\"组织\",\"其它组织\"]},{\"img\":\"./images/img/linkedin.svg\",\"id\":3341546,\"text\":\"Marc Graves\",\"properties\":{\"summary\":\"tb_linkedin_people+10324+summary\",\"urlpattern\":\"marc-graves-38a9738\",\"lnkid\":26278234,\"languageSkill\":\"\",\"courseList\":\"\",\"industry\":\"Information Technology & Services\",\"dateCreatedTime\":\"2018-07-18 10:47:09\",\"skills\":\"Active Directory,VMware,VMware ESX,Virtualization,Servers,Windows Server,VMware Infrastructure,Cisco Technologies,Data Center,SAN,Infrastructure,Citrix,Networking,Disaster Recovery,Microsoft SQL Server,Linux,Project Management,Storage,Network Engineering,vSphere,High Availability,Network Security,Network Architecture,DNS,Storage Area Networks,Cloud Computing,Microsoft Azure,\",\"imgurl\":\"http://media.licdn.com/mpr/mpr/shrinknp_400_400/p/4/000/13c/1d0/1071194.jpg\",\"imgcode_id\":549704,\"name\":\"Marc Graves_26278234_linkedin\",\"location\":\"Salt Lake City, Utah\",\"fullname\":\"Marc Graves\",\"headline\":\"tb_linkedin_overview+10283+headline\"},\"labels\":[\"人\",\"虚拟账号ID\",\"LinkedinID\"]},{\"img\":\"./images/img/linkedin.svg\",\"id\":3441235,\"text\":\"Nate Stoddard\",\"properties\":{\"summary\":\"tb_linkedin_people+10313+summary\",\"urlpattern\":\"nstoddar\",\"lnkid\":215098445,\"imgname\":\"/images/linkedinimg/215098445/recommanders/215098445_recommanders1.jpg\",\"languageSkill\":\"English,\",\"courseList\":\"\",\"industry\":\"Computer Software\",\"lnkurl\":\"http://www.linkedin.com/in/nstoddar\",\"dateCreatedTime\":\"2018-07-18 10:46:20\",\"skills\":\"Java Enterprise Edition,Java,Agile Methodologies,XML,Maven,Hibernate,Web Services,Spring,REST,Software Development,Spring Framework,Distributed Systems,Enterprise Software,JMS,Eclipse,MapReduce,Scalability,Hadoop,Ant,SOA,Tomcat,JBoss Application Server,JSP,Design Patterns,Perl,JDBC,Subversion,JUnit,EJB,JSON,Software Design,Weblogic,Scrum,JavaScript,SDLC,Java Database Connectivity (JDBC),Software Development Life Cycle (SDLC),Architecture,Unit Testing,Software Engineering,Open Source,Node.js,SQL,\",\"imgurl\":\"https://media.licdn.com/mpr/mpr/shrinknp_100_100/AAEAAQAAAAAAAAOWAAAAJGNkYjRmYzgzLWEzNWQtNDYzYi04ZTQyLTg3NTJlNTUxNTg0MQ.jpg\",\"twitter\":\"nstoddar74\",\"imgcode_id\":549696,\"name\":\"Nate Stoddard_215098445_linkedin\",\"location\":\"Baltimore, Maryland Area\",\"fullname\":\"Nate Stoddard\",\"headline\":\"tb_linkedin_overview+10257+headline\"},\"labels\":[\"人\",\"虚拟账号ID\",\"LinkedinID\"]},{\"img\":\"./images/img/qt.svg\",\"id\":3214884,\"text\":\"Big Data, Analytics, Hadoop, NoSQL & Cloud Computing\",\"properties\":{\"imgurl\":\"http://media.licdn.com/mpr/mpr/shrinknp_100_100/p/6/005/036/032/0fdbb7b.png\",\"name\":\"Big Data, Analytics, Hadoop, NoSQL & Cloud Computing\",\"lnkurl\":\"https://www.linkedin.com/groups/\"},\"labels\":[\"组织\",\"其它组织\"]}]}}],\"columns\":\"\"}]}\n");

        /**
         * @param dragNodeId:分析的起始节点
         * @param result:深度推理的分析结果
         * @return
         * @Description: TODO(Depth reasoning and then drag the node)
         */
        System.out.println(result);
        // JAVA 7
//        JSONObject reasoningDragResult = GraphTraversalMove.depthReasoning(startNodeIds, dragNodeIds, result);
        // 限制分析的路径数量
        JSONObject reasoningDragResult = GraphTraversalMove.depthReasoning(startNodeIds, dragNodeIds, result,1000);

        // JAVA 8
//        JSONObject reasoningDragResult = GraphTraversal.depthReasoning(startNodeIds, dragNodeIds, result);

        /**
         * 使用可视化插件校验图分析数据-可视化插件的使用方式（一）：
         * 1、将查询结果进行格式转换
         * 2、转换后的数据写入到neo-import-csv/check-graph-traversal.json文件
         * 3、启动HTTP-SERVICE:neo4j-engine-inter\src\main\java\casia\isi\neo4j\http\server\HttpService.java
         * 4、双击neo4j-engine-inter\src\main\resources\static\index.html文件使用浏览器显示
         * **/

        /**
         * 使用可视化插件校验图分析数据-可视化插件的使用方式（二）：
         * 1、直接调用可视化插件类Visualization
         * 2、然后双击static/index.html页面即可
         * **/
        System.out.println(reasoningDragResult);
        new Visualization().run(reasoningDragResult);
    }

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configureAndWatch("config/log4j.properties");
        GraphTraversal.setDEBUG(true);
        new VisualizationTest().run();
    }

}


