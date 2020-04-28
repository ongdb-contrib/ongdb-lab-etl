package data.lab.ongdb.common;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import data.lab.ongdb.model.RelationshipType;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.common.Relationships
 * @Description: TODO(REL TYPE)
 * @date 2020/4/28 14:30
 */
public enum Relationships implements RelationshipType {

    // 分类层级标签关系
    NEXT,

    // 固定层级标签关系
    FIX_NEXT

}

