package data.lab.ongdb.etl.model;/*
*
 * Data Lab - graph database organization.
*
 */


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.util.Objects;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.etl.model
 * @Description: TODO(构造HTTP参数)
 * @date 2019/7/10 11:01
 */
public class Para {

    private Logger logger = Logger.getLogger(this.getClass());

    private String paraName;

    private JSONObject parameters;

    public Para(String paraName, Object... paras) {
        if (paras != null) {
            if (paras.clone().length % 2 == 0) {

                this.paraName = paraName;

                this.parameters = new JSONObject();
                for (int i = 0; i < paras.length; i++) {
                    Object para = paras[i];
                    try {
                        this.parameters.put(String.valueOf(para), paras[i + 1]);
                    } catch (Exception e) {

                    }
                    i++;
                }
            } else {
                if (this.logger.isDebugEnabled()) {
                    this.logger.error("Statement parameters:" + paras.toString(), new IllegalArgumentException());
                }
            }
        }
    }

    public String getParaName() {
        return paraName;
    }

    public JSONObject getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "Para{" +
                "parameters=" + parameters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Para paras = (Para) o;
        return Objects.equals(parameters, paras.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameters);
    }
}

