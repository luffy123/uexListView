package org.zywx.wbpalmstar.plugin.uexlistview.vo;

import java.io.Serializable;

public class InsertCustomDataVO extends ResultItemClickVO implements Serializable{
    private static final long serialVersionUID = -2291572501136711375L;
    private CustomListDataVO data;

    public CustomListDataVO getData() {
        return data;
    }

    public void setData(CustomListDataVO data) {
        this.data = data;
    }
}
