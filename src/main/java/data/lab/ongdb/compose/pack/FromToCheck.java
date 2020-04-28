package data.lab.ongdb.compose.pack;
/*
 *
 * Data Lab - graph database organization.
 *
 */import data.lab.ongdb.common.Symbol;
import data.lab.ongdb.model.Label;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Objects;

/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.compose.pack
 * @Description: TODO(起始节点)
 * @date 2019/7/10 14:23
 */
public class FromToCheck {

    private Label label;

    @JSONField(name="_unique_uuid")
    private String _unique_uuid;

    public FromToCheck(Label label, String _unique_uuid) {
        this.label = label;
        this._unique_uuid = _unique_uuid;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public String get_unique_uuid() {
        return _unique_uuid;
    }

    public void set_unique_uuid(String _unique_uuid) {
        this._unique_uuid = _unique_uuid;
    }

    @Override
    public String toString() {
        return label + Symbol.SPECIAL_SPLIT.getSymbolValue() + _unique_uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FromToCheck from = (FromToCheck) o;
        return Objects.equals(label, from.label) &&
                Objects.equals(_unique_uuid, from._unique_uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, _unique_uuid);
    }
}
