package org.zywx.wbpalmstar.plugin.uexlistview.vo;

import org.zywx.wbpalmstar.util.customlayout.vo.LayoutItemVO;

import java.io.Serializable;
import java.util.List;

public class CustomListLayoutVO implements Serializable{
    private static final long serialVersionUID = -4682801859535102699L;
    private List<LayoutItemVO> left;
    private List<LayoutItemVO> right;
    private List<LayoutItemVO> center;

    public List<LayoutItemVO> getLeft() {
        return left;
    }

    public void setLeft(List<LayoutItemVO> left) {
        this.left = left;
    }

    public List<LayoutItemVO> getRight() {
        return right;
    }

    public void setRight(List<LayoutItemVO> right) {
        this.right = right;
    }

    public List<LayoutItemVO> getCenter() {
        return center;
    }

    public void setCenter(List<LayoutItemVO> center) {
        this.center = center;
    }
}
