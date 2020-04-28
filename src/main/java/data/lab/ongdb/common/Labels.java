package data.lab.ongdb.common;
/*
*
 * Data Lab - graph database organization.
*
 */


import data.lab.ongdb.model.Label;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: casia.isiteam.weibo.labels_relationship
 * @Description: TODO(Entities labels)
 * @date 2018/3/23 15:26
 */
public enum Labels implements Label {

    // 标签树本身的标签
    LabelsTree,

    // 存储搜索记录的标签
    SearchRecord,

    人, 现实人员, QQ号, WeChat号, 设备号, 资金账户, 邮箱, 手机号, M方向选民,

    虚拟账号ID, 新浪微博ID, 网易博客ID, 新浪博客ID, 腾讯微博ID, TwitterID, 百度贴吧ID, 天涯论坛ID, 微信公众号ID,
    FacebookID, LinkedinID, YouTubeID, InstagramID, 虚拟账号, BlogID, 论坛ID,

    地, 国家, 省, 市, 区县,

    事, 专题事件, 专利发明, 个人项目, 事件,

    物, 出版作品, 基站, APP, 网址, 发帖, Facebook发帖, Twitter发帖, Linkin发帖, YouTube发帖, Instagram发帖, 帖子,
    新浪微博发帖, 腾讯微博发帖, WeChat发帖, Blog发帖, 论坛发帖,

    组织, 行业, 认证机构, 学校, 公司, 颁发机构, 志愿机构, QQ群, WeChat群, 其它组织, 重点人组织

    // 其它图谱库中标签都是用户自定义标签

}

