package data.lab.ongdb.search;
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

import data.lab.ongdb.common.Accessor;
import data.lab.ongdb.common.LabelOccurs;
import data.lab.ongdb.common.RelationOccurs;
import data.lab.ongdb.common.SortOrder;
import data.lab.ongdb.model.Label;
import data.lab.ongdb.model.RelationshipType;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.search
 * @Description: TODO(检索接口)
 * @date 2019/7/9 9:40
 */
public interface Searcher extends Accessor {

    /**
     * @param skip:默认0
     * @return
     * @Description: TODO(设置分页开始的数据)
     */
    void setStart(int skip);

    /**
     * @param limit:默认10
     * @return
     * @Description: TODO(设置分页开始的数据)
     */
    void setRow(int limit);

    /**
     * =================================节点检索接口=================================
     *
     * **/

    /**
     * @param id:节点ID-NEO4J自动生成的ID
     * @return
     * @Description: TODO(通过节点ID查找节点)
     */
    void addNodeId(long id);

    /**
     * @param label:节点标签
     * @return
     * @Description: TODO(节点标签)
     */
    void addNodeLabel(Label label);

    /**
     * @param key:属性KEY
     * @param value:属性VALUE
     * @return
     * @Description: TODO(通过节点属性检索节点 - 多次设置此条件默认是与检索)
     */
    void addNodeProperties(String key, Object value);

    /**
     * =================================关系检索接口=================================
     *
     * **/

    /**
     * @param id:关系ID-NEO4J自动生成的ID
     * @return
     * @Description: TODO(通过关系ID查找关系)
     */
    void addRelationId(long id);

    /**
     * @param relationshipType:关系类型名
     * @return
     * @Description: TODO(通过关系类型检索关系)
     */
    void addRelationType(RelationshipType relationshipType);

    /**
     * @param key:属性KEY
     * @param value:属性VALUE
     * @return
     * @Description: TODO(通过关系属性检索关系 - 多次设置此条件默认是与检索)
     */
    @Deprecated
    void addRelationProperties(String key, Object value);


    /**
     * =================================节点关系同时检索-PATH检索接口=================================
     *
     * **/
    /**
     * @param id:节点ID
     * @return
     * @Description: TODO(设置路径开始节点ID)
     */
    void addPathStartNodeId(long id);

    /**
     * @param
     * @return
     * @Description: TODO(设置路径开始节点IDS)
     */
    void addPathStartNodeId(long[] ids);

    /**
     * @param
     * @return
     * @Description: TODO(设置路径开始节点标签)
     */
    void addPathStartNodeLabel(Label label);

    /**
     * @param
     * @return
     * @Description: TODO(设置路径开始节点属性 - 多次设置此条件默认是与检索)
     */
    void addPathStartNodeProperties(String key, Object value);

    /**
     * @param id:节点ID
     * @return
     * @Description: TODO(设置路径结束节点ID)
     */
    void addPathEndNodeId(long id);

    /**
     * @param
     * @return
     * @Description: TODO(设置路径结束节点标签)
     */
    void addPathEndNodeLabel(Label label);

    /**
     * @param
     * @return
     * @Description: TODO(设置路径结束节点属性 - 多次设置此条件默认是与检索)
     */
    void addPathEndNodeProperties(String key, Object value);

    /**
     * @param id:节点ID
     * @return
     * @Description: TODO(设置路径中间节点)
     */
    @Deprecated
    void addPathMidSideNodeId(long id);

    /**
     * @param label:设置标签
     * @param labelOccurs:标签出现情况
     * @return
     * @Description: TODO(设置路径中间节点标签)
     */
    void addPathMidSideNodeLabel(Label label, LabelOccurs labelOccurs);

    /**
     * @param
     * @return
     * @Description: TODO(设置路径结束节点属性 - 多次设置此条件默认是与检索)
     */
    @Deprecated
    void addPathMidSideNodeProperties(String key, Object value);

    /**
     * @param relationshipType:关系类型
     * @param relationOccurs:关系在路径中的出现情况(路径中全部是这种关系/路径中包含这种关系/路径中没有这种关系)
     * @return
     * @Description: TODO(路径扩展的关系设置)
     */
    void addPathMidSideRelationType(RelationshipType relationshipType, RelationOccurs relationOccurs);

    /**
     * @param
     * @return
     * @Description: TODO(设置关系路径长度)
     */
    void addPathLength(int length);

    /**
     * @param fieldName:排序字段名称
     * @param sort:排序方式
     * @return
     * @Description: TODO(根据路径结束节点属性排序)
     */
    void setPathEndNodeSort(String fieldName, SortOrder sort);

    /**
     * @param isVirtualGraph:是否返回虚拟图
     * @param startMergeNodeLabel:虚拟图开始的标签类型
     * @param endMergeNodeLabel:虚拟图结束的标签类型
     * @param virtualRelationshipType:生成虚拟图的关系名称
     * @param filterLabel:路径中间被过滤掉的节点标签
     * @return 返回的数据中会将被虚拟的关系路径移除掉
     * @Description: TODO(是否返回虚拟图 - 虚拟图的生成从开始结束类型节点忽略中间所有的关系和路径 ， 直接根据上一次匹配的数据进行生成新的路径)
     * <p>
     * P1=(n:虚拟账号)-[]-(f:发帖)-[]-(m:专题事件)-[]-(h:帖子)
     * <p>
     * addPathGraphIsVirtual(true, new Label[](Label.label("虚拟账号")), new Label[](Label.label("专题事件")))
     * addVirtualP2=(n:虚拟账号)-[]-(m:专题事件)
     */
    void addPathGraphIsVirtual(boolean isVirtualGraph, Label startMergeNodeLabel, Label endMergeNodeLabel, RelationshipType virtualRelationshipType, Label filterLabel);


    /**
     * =================================统计接口=================================
     **/
    /**
     * @param currentId:当前节点ID
     * @param pathLength:路径长度
     * @param targetNodeLabel:目标节点标签
     * @return
     * @Description: TODO(路径统计接口)
     */
//    JSONObject statisticsPathByCurrentNode(long currentId, int pathLength, Label targetNodeLabel);
}


