package org.zywx.wbpalmstar.plugin.uexlistview.vo;

import java.io.Serializable;

public class ResultElementClickVO extends ResultItemClickVO implements Serializable{
    private static final long serialVersionUID = 5869073445165598535L;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
