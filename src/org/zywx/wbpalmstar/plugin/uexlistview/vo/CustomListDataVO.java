package org.zywx.wbpalmstar.plugin.uexlistview.vo;

import org.zywx.wbpalmstar.util.customlayout.vo.ElementVO;

import java.io.Serializable;
import java.util.List;

public class CustomListDataVO implements Serializable{
    private static final long serialVersionUID = 286773405328407501L;
    private ElementVO center;
    private ElementVO left;
    private ElementVO right;

    public ElementVO getCenter() {
        return center;
    }

    public void setCenter(ElementVO center) {
        this.center = center;
    }

    public ElementVO getLeft() {
        return left;
    }

    public void setLeft(ElementVO left) {
        this.left = left;
    }

    public ElementVO getRight() {
        return right;
    }

    public void setRight(ElementVO right) {
        this.right = right;
    }
}
