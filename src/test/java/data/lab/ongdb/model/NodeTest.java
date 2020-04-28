package data.lab.ongdb.model;

import data.lab.ongdb.common.Labels;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.model
 * @Description: TODO(Describe the role of this JAVA class)
 * @date 2019/7/9 17:36
 */
public class NodeTest {

    @Test
    public void setLables() {
        Node node = new Node();
        List<Label> labels = new ArrayList<>();
        labels.add(Labels.LabelsTree);

        // 定义枚举类实现Label接口
        node.setLables(Labels.Facebook发帖, Labels.InstagramID);

        node.setLables(Label.label("POST"));

        List<Label> labels1 = node.getLables();
        for (int i = 0; i < labels1.size(); i++) {
            Label label = labels1.get(i);
            System.out.println(label.name());
        }
    }

}

