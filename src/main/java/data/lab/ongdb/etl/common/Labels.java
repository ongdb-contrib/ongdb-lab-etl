package data.lab.ongdb.etl.common;
/*
 *
 * Data Lab - graph database organization.
 *
 */


import data.lab.ongdb.etl.model.Label;

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

    // 任务ID标签
    GraphTaskIDRecord

}

