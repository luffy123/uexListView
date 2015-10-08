package org.zywx.wbpalmstar.plugin.uexlistview.vo;

import java.io.Serializable;

public class ResultRefreshVO implements Serializable{
    private static final long serialVersionUID = 7201134644334074914L;
    private int status;

    public ResultRefreshVO(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
