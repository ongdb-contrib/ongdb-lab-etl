package data.lab.ongdb.search;

import data.lab.ongdb.common.AccessOccurs;
import data.lab.ongdb.common.ResultDataContents;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
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
 * @PACKAGE_NAME: casia.isi.neo4j.search
 * @Description: TODO(Describe the role of this JAVA class)
 * @author YanchaoMa yanchaoma@foxmail.com
 * @date 2019/7/12 14:54
 *
 *
 */
public class NeoSearcherAccessorTest {

    //    private final static String ipPorts = "localhost:7474,192.168.12.19:7474";
    private final static String ipPorts = "192.168.12.19:7474";

    private static NeoSearcher searcher;

    @Before
    public void setUp() throws Exception {

        PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.properties");


        // 返回GRAPH数据
//        searcher = new NeoSearcher(ResultDataContents.GRAPH, AccessOccurs.RESTFUL_API, ipPorts, "neo4j", "123456");

        // 返回ROW数据
//        searcher = new NeoSearcher(ResultDataContents.ROW, AccessOccurs.RESTFUL_API, ipPorts, "neo4j", "123456");
//       // 返回ROW_GRAPH数据
//        searcher = new NeoSearcher(ResultDataContents.ROW_GRAPH, AccessOccurs.RESTFUL_API, ipPorts, "neo4j", "123456");

        // 返回D3_GRAPH数据
        searcher = new NeoSearcher(ResultDataContents.D3_GRAPH, AccessOccurs.RESTFUL_API, ipPorts, "neo4j", "123456");
    }

    /**
     * @param
     * @return
     * @Description: TODO(节点与关系同时过滤 - 检索路径)
     */
    @Test
    public void setStartNodeConditionTest1() {
        /**
         * 检索开始节点ID是0，关系层数是N的关系。(返回PATH包含关系和节点)。N层关系检索
         */
        searcher.addPathStartNodeId(375184813);
//        searcher.addPathLength(1);
        searcher.addPathLength(4);
        searcher.setStart(0);
        searcher.setRow(100);
        System.out.println(searcher.execute());
        searcher.reset();
    }

    // 返回的数据样例
    /**
     * 1、GRAPH
     * {"queryResultList":[{"message":true,"results":[{"data":[{"graph":{"relationships":[{"startNode":"374825589","id":"249991348","type":"命中关键词","endNode":"375184813","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"}}],"nodes":[{"id":"375184813","properties":{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},"labels":["专题事件"]},{"id":"374825589","properties":{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"},"labels":["帖子"]}]}},{"graph":{"relationships":[{"startNode":"374825589","id":"249991348","type":"命中关键词","endNode":"375184813","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"}},{"startNode":"375166807","id":"249990129","type":"发帖","endNode":"374825589","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"发帖"}}],"nodes":[{"id":"375184813","properties":{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},"labels":["专题事件"]},{"id":"374825589","properties":{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"},"labels":["帖子"]},{"id":"375166807","properties":{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"社会热点播报"},"labels":["虚拟账号"]}]}},{"graph":{"relationships":[{"startNode":"374825589","id":"249991348","type":"命中关键词","endNode":"375184813","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"}},{"startNode":"375166807","id":"249989349","type":"参与组织","endNode":"375111286","properties":{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"参与组织"}},{"startNode":"375166807","id":"249990129","type":"发帖","endNode":"374825589","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"发帖"}}],"nodes":[{"id":"375184813","properties":{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},"labels":["专题事件"]},{"id":"374825589","properties":{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"},"labels":["帖子"]},{"id":"375166807","properties":{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"社会热点播报"},"labels":["虚拟账号"]},{"id":"375111286","properties":{"_unique_uid":"2sa234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"热点传播"},"labels":["组织"]}]}}],"columns":["p"]}],"errors":[]}],"consume":"Total consume 0s,average consume 0s/request","failed":0,"totalQuery":1,"successful":1}
     *
     * 2、ROW
     * {"queryResultList":[{"message":true,"results":[{"data":[{"meta":[[{"deleted":false,"id":375184813,"type":"node"},{"deleted":false,"id":249991348,"type":"relationship"},{"deleted":false,"id":374825589,"type":"node"}]],"row":[[{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"},{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"}]]},{"meta":[[{"deleted":false,"id":375184813,"type":"node"},{"deleted":false,"id":249991348,"type":"relationship"},{"deleted":false,"id":374825589,"type":"node"},{"deleted":false,"id":249990129,"type":"relationship"},{"deleted":false,"id":375166807,"type":"node"}]],"row":[[{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"},{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"},{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"发帖"},{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"社会热点播报"}]]},{"meta":[[{"deleted":false,"id":375184813,"type":"node"},{"deleted":false,"id":249991348,"type":"relationship"},{"deleted":false,"id":374825589,"type":"node"},{"deleted":false,"id":249990129,"type":"relationship"},{"deleted":false,"id":375166807,"type":"node"},{"deleted":false,"id":249989349,"type":"relationship"},{"deleted":false,"id":375111286,"type":"node"}]],"row":[[{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"},{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"},{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"发帖"},{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"社会热点播报"},{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"参与组织"},{"_unique_uid":"2sa234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"热点传播"}]]}],"columns":["p"]}],"errors":[]}],"consume":"Total consume 0s,average consume 0s/request","failed":0,"totalQuery":1,"successful":1}
     *
     * 3、ROW_GRAPH
     * {"queryResultList":[{"message":true,"results":[{"data":[{"meta":[[{"deleted":false,"id":375184813,"type":"node"},{"deleted":false,"id":249991348,"type":"relationship"},{"deleted":false,"id":374825589,"type":"node"}]],"row":[[{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"},{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"}]],"graph":{"relationships":[{"startNode":"374825589","id":"249991348","type":"命中关键词","endNode":"375184813","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"}}],"nodes":[{"id":"375184813","properties":{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},"labels":["专题事件"]},{"id":"374825589","properties":{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"},"labels":["帖子"]}]}},{"meta":[[{"deleted":false,"id":375184813,"type":"node"},{"deleted":false,"id":249991348,"type":"relationship"},{"deleted":false,"id":374825589,"type":"node"},{"deleted":false,"id":249990129,"type":"relationship"},{"deleted":false,"id":375166807,"type":"node"}]],"row":[[{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"},{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"},{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"发帖"},{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"社会热点播报"}]],"graph":{"relationships":[{"startNode":"374825589","id":"249991348","type":"命中关键词","endNode":"375184813","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"}},{"startNode":"375166807","id":"249990129","type":"发帖","endNode":"374825589","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"发帖"}}],"nodes":[{"id":"375184813","properties":{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},"labels":["专题事件"]},{"id":"374825589","properties":{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"},"labels":["帖子"]},{"id":"375166807","properties":{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"社会热点播报"},"labels":["虚拟账号"]}]}},{"meta":[[{"deleted":false,"id":375184813,"type":"node"},{"deleted":false,"id":249991348,"type":"relationship"},{"deleted":false,"id":374825589,"type":"node"},{"deleted":false,"id":249990129,"type":"relationship"},{"deleted":false,"id":375166807,"type":"node"},{"deleted":false,"id":249989349,"type":"relationship"},{"deleted":false,"id":375111286,"type":"node"}]],"row":[[{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"},{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"},{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"发帖"},{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"社会热点播报"},{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"参与组织"},{"_unique_uid":"2sa234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"热点传播"}]],"graph":{"relationships":[{"startNode":"374825589","id":"249991348","type":"命中关键词","endNode":"375184813","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"}},{"startNode":"375166807","id":"249989349","type":"参与组织","endNode":"375111286","properties":{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"参与组织"}},{"startNode":"375166807","id":"249990129","type":"发帖","endNode":"374825589","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"发帖"}}],"nodes":[{"id":"375184813","properties":{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},"labels":["专题事件"]},{"id":"374825589","properties":{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"},"labels":["帖子"]},{"id":"375166807","properties":{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"社会热点播报"},"labels":["虚拟账号"]},{"id":"375111286","properties":{"_unique_uid":"2sa234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"热点传播"},"labels":["组织"]}]}}],"columns":["p"]}],"errors":[]}],"consume":"Total consume 3s,average consume 3s/request","failed":0,"totalQuery":1,"successful":1}
     *
     * 4、D3_GRAPH
     * {"consume":"Total consume 0s,average consume 0s/request","failed":0,"totalQuery":1,"message":true,"results":[{"data":[{"graph":{"relationships":[{"startNode":"374825589","id":"249991348","type":"命中关键词","endNode":"375184813","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"}},{"startNode":"374825589","id":"249991348","type":"命中关键词","endNode":"375184813","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"}},{"startNode":"375166807","id":"249990129","type":"发帖","endNode":"374825589","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"发帖"}},{"startNode":"374825589","id":"249991348","type":"命中关键词","endNode":"375184813","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"命中关键词","name":"命中关键词"}},{"startNode":"375166807","id":"249989349","type":"参与组织","endNode":"375111286","properties":{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"参与组织"}},{"startNode":"375166807","id":"249990129","type":"发帖","endNode":"374825589","properties":{"_unique_uid":"2sa23wqwtrtr5qwedcdsfsaas","_entity_name":"发帖"}}],"nodes":[{"id":"375184813","properties":{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},"labels":["专题事件"]},{"id":"374825589","properties":{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"},"labels":["帖子"]},{"id":"375184813","properties":{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},"labels":["专题事件"]},{"id":"374825589","properties":{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"},"labels":["帖子"]},{"id":"375166807","properties":{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"社会热点播报"},"labels":["虚拟账号"]},{"id":"375184813","properties":{"eid":12,"_unique_uid":"asdwqwtrrtr5qwedcdsfsaas","_entity_name":"打砸抢"},"labels":["专题事件"]},{"id":"374825589","properties":{"_unique_uid":"a234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"今天看见一个人打砸抢"},"labels":["帖子"]},{"id":"375166807","properties":{"_unique_uid":"2sa23dwqwtrtr5qwedcdsfsaas","_entity_name":"社会热点播报"},"labels":["虚拟账号"]},{"id":"375111286","properties":{"_unique_uid":"2sa234sdwqwtrrtr5qwedcdsfsaas","_entity_name":"热点传播"},"labels":["组织"]}]}}],"columns":["p"]}],"errors":[],"successful":1}
     *
     * **/
}