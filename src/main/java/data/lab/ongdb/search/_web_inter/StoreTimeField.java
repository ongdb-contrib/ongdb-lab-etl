package data.lab.ongdb.search._web_inter;
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
 * @PACKAGE_NAME: casia.isi.neo4j.search._web_inter
 * @Description: TODO(TIME FIELD)
 * @author YanchaoMa yanchaoma@foxmail.com
 * @date 2020/2/28 15:01
 *
 *
 */
public enum StoreTimeField {
    //        默认全部不限【下拉框中选择过滤项】
//        发布时间  开始 结束 pub_time_mills       <=n.pub_time_mills<=
//        转发时间  开始 结束 pub_time_mills       <=n.pub_time_mills<=
//        更新时间  开始 结束 update_time_mills    <=n.update_time_mills<=
//        事件时间  开始 结束 start_time_mills end_time_mills   <=start_time_mills AND end_time_mills<=
    pub_time_mills,update_time_mills,start_time_mills,end_time_mills
}
