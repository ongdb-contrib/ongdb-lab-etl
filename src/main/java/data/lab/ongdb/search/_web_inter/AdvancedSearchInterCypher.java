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

import data.lab.zdr.graph.common.label.Labels;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.search._web_inter
 * @Description: TODO(JOINT WEB INTERFACE CYPHER)
 * @date 2020/2/27 16:41
 */
public class AdvancedSearchInterCypher {
    /**
     * // 加载用户可选条件
     * // chineseKey 中文条件（一个）
     * // englishKey 英文字段（多个）
     * // labelRelation 可关联的标签类别
     * <p>
     * // 加载检索条件
     * // value 检索值（为空不添加）
     * // match  EXACT FUZZY
     * // condition 与或非(AND OR NOT)
     *
     * @param
     * @return
     * @Description: TODO(拼接高级检索查询)--【全文索引】
     */
    public static String freeTextSearchNoLabelJoint(JSONArray properties, StoreTimeField start, long startMill, StoreTimeField stop, long stopMill) {
        // 检索条件
        String condition = jointKeyConditon(properties);
        StringBuilder stringBuilder = new StringBuilder();

        // 设置权重字段在解析时对最终结果进行排序
        String reCondition = condition.replace("zdrMarkLabelRepalceLoc.", "");
        stringBuilder.append("CALL db.index.fulltext.queryNodes('nodesProperties', '" + reCondition + "') YIELD node,score SET node.searchEngineWeight=score RETURN node LIMIT 100 ");

        String timeCondition = startMill + "<=node." + start + " AND " + stopMill + "<=node." + stop + "";
        if (StoreTimeField.pub_time_mills.equals(start)) {
            timeCondition = timeCondition.replace("node.", "r.");
            timeCondition = "MATCH (node)-[r]-() WHERE " + timeCondition;
            return stringBuilder.toString().replace("=score", "=score WITH node " + timeCondition + " ");
        } else {
            timeCondition = "MATCH (node) WHERE " + timeCondition;
            return stringBuilder.toString().replace("=score", "=score WITH node " + timeCondition + " ");
        }
    }

    /**
     * // 加载用户可选条件
     * // chineseKey 中文条件（一个）
     * // englishKey 英文字段（多个）
     * // labelRelation 可关联的标签类别
     * <p>
     * // 加载检索条件
     * // value 检索值（为空不添加）
     * // match  EXACT FUZZY
     * // condition 与或非(AND OR NOT)
     *
     * @param
     * @return
     * @Description: TODO(拼接高级检索查询)--【全文索引】--增加时间检索字段
     */
    public static String freeTextSearchNoLabelJoint(JSONArray properties) {
        // 检索条件
        String condition = jointKeyConditon(properties);
        StringBuilder stringBuilder = new StringBuilder();

        // 设置权重字段在解析时对最终结果进行排序
        String reCondition = condition.replace("zdrMarkLabelRepalceLoc.", "");
        stringBuilder.append("CALL db.index.fulltext.queryNodes('nodesProperties', '" + reCondition + "') YIELD node,score SET node.searchEngineWeight=score RETURN node LIMIT 100 ");
        return stringBuilder.toString();
    }

    private static String jointKeyConditon(JSONArray properties) {
        StringBuilder builder = new StringBuilder();

        properties.forEach(v -> {
            LinkedHashMap object = (LinkedHashMap) v;

            JSONArray englishKey = JSONArray.parseArray(JSON.toJSONString(object.get("englishKey")));
            JSONArray labelRelation = JSONArray.parseArray(JSON.toJSONString(object.get("labelRelation")));
            Object value = object.get("value");
            String match = (String) object.get("match");
            String condition = (String) object.get("condition");

            if (englishKey.size() == 1) {
                if ("AND".equals(condition)) {
                    builder.append(" +" + jointEnglishKeyAndMatchFull(englishKey, value, match));
                } else if ("OR".equals(condition)) {
                    builder.append("  " + jointEnglishKeyAndMatchFull(englishKey, value, match));
                } else if ("NOT".equals(condition)) {
                    builder.append(" -" + jointEnglishKeyAndMatchFull(englishKey, value, match));
                }
            } else {
                // 表达式分组
                if ("AND".equals(condition)) {
                    builder.append(" +(" + jointEnglishKeyAndMatchFull(englishKey, value, match) + ")");
                } else if ("OR".equals(condition)) {
                    builder.append("  (" + jointEnglishKeyAndMatchFull(englishKey, value, match) + ")");
                } else if ("NOT".equals(condition)) {
                    builder.append(" -(" + jointEnglishKeyAndMatchFull(englishKey, value, match) + ")");
                }
            }
        });
        if (builder != null && !"".equals(builder.toString())) {
            return builder.substring(1, builder.length());
        } else {
            return "";
        }
    }

    /**
     * @param
     * @return
     * @Description: TODO(拼接单个中文条件下的检索条件)
     */
    private static String jointEnglishKeyAndMatchFull(JSONArray englishKey, Object value, String match) {
        StringBuilder builder = new StringBuilder();

        englishKey.forEach(v -> {
            String key = (String) v;
            if ("EXACT".equals(match)) {
                builder.append("zdrMarkLabelRepalceLoc." + key + ":\"" + value + "\" ");

            } else if ("FUZZY".equals(match)) {
                builder.append("zdrMarkLabelRepalceLoc." + key + ":" + value + "  ");
                // 模糊匹配
//                builder.append("zdrMarkLabelRepalceLoc." + key + ":" + value + "* ");
                // 相似性检索
//                builder.append("zdrMarkLabelRepalceLoc." + key + ":" + value + "~ ");
            }
        });
        if (builder != null && !"".equals(builder.toString())) {
            return builder.toString();
        } else {
            return "";
        }
    }

    /**
     * @param
     * @return
     * @Description: TODO(拼接高级检索查询)--【模式索引】
     */
    public static String universalSearchLabelJoint(JSONArray properties, String timeFilterConditon) {

        // 检索条件
        StringBuilder builder = new StringBuilder();

        // 标签
        JSONArray labelsAdd = new JSONArray();

        properties.forEach(v -> {
            LinkedHashMap object = (LinkedHashMap) v;

            JSONArray englishKey = JSONArray.parseArray(JSON.toJSONString(object.get("englishKey")));
            JSONArray labelRelation = JSONArray.parseArray(JSON.toJSONString(object.get("labelRelation")));
            Object value = object.get("value");
            String match = (String) object.get("match");
            String condition = (String) object.get("condition");

            // 收集所有标签
            labelsAdd.addAll(labelRelation);

            if ("AND".equals(condition)) {
                builder.append(jointEnglishKeyAndMatch(englishKey, value, match) + "AND ");

            } else if ("OR".equals(condition)) {
                builder.append(jointEnglishKeyAndMatch(englishKey, value, match) + "OR  ");

            } else if ("NOT".equals(condition)) {
                builder.append(jointEnglishKeyAndMatch(englishKey, value, "NOT ") + "AND ");
            } else {
                builder.append(jointEnglishKeyAndMatch(englishKey, value, match) + "AND ");
            }

        });

        // 标签去重
        JSONArray labels = labelsAdd.stream().distinct().collect(Collectors.toCollection(JSONArray::new));

        if (builder != null && !"".equals(builder.toString())) {
            String condition = builder.substring(0, builder.length() - 4);

            StringBuilder stringBuilder = new StringBuilder();
            labels.forEach(v -> stringBuilder.append("MATCH (n:" + v + ") WHERE " + condition + " AND " + timeFilterConditon + " RETURN n LIMIT 100 UNION ALL "));
            if (stringBuilder != null && !"".equals(stringBuilder.toString())) {
                return stringBuilder.substring(0, stringBuilder.length() - 10);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * @param
     * @return
     * @Description: TODO(拼接单个中文条件下的检索条件)
     */
    public static String jointEnglishKeyAndMatch(JSONArray englishKey, Object value, String match) {
        StringBuilder builder = new StringBuilder();

        Object reValue;
        if (value instanceof String) {
            reValue = "'" + value + "'";
        } else {
            reValue = value;
        }

        englishKey.forEach(v -> {
            String key = (String) v;
            if ("EXACT".equals(match)) {
//                builder.append("n." + key + " = " + reValue + " OR ");
                builder.append("n." + key + " CONTAINS " + reValue + " OR ");
            } else if ("FUZZY".equals(match)) {
                builder.append("n." + key + " CONTAINS " + reValue + " OR ");
            } else if ("NOT".equals(match)) {
                builder.append("n." + key + " <> " + reValue + " OR ");
            } else {
                builder.append("");
            }
        });
        if (builder != null && !"".equals(builder.toString())) {
            return builder.substring(0, builder.length() - 3);
        } else {
            return "";
        }
    }

    /**
     * @param
     * @return
     * @Description: TODO(高级检索条件数据封装)
     */
    public static JSONObject putCondition(String chineseKey, String[] englishKey, Labels[] labelRelation) {

        // OBJECT start 0 end >0
        // 加载用户可选条件
        // chineseKey 中文条件（一个）
        // englishKey 英文字段（多个）
        // labelRelation 可关联的标签类别

        // 加载检索条件
        // value 检索值（为空不添加）
        // match  EXACT FUZZY
        // condition 与或非(AND OR NOT)

        JSONObject object = new JSONObject();
        object.put("chineseKey", chineseKey);
        object.put("englishKey", englishKey);
        object.put("labelRelation", labelRelation);
        return object;
    }
}

