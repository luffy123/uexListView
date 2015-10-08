package org.zywx.wbpalmstar.plugin.uexlistview.vo;

import org.zywx.wbpalmstar.plugin.uexlistview.EUExListViewUtils;
import org.zywx.wbpalmstar.plugin.uexlistview.JsConst;
import org.zywx.wbpalmstar.plugin.uexlistview.custom.CustomListView;
import org.zywx.wbpalmstar.util.customlayout.vo.ElementVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OpenCustomVO implements Serializable{
    private static final long serialVersionUID = -1999133011524472519L;
    private double left;
    private double top;
    private double width;
    private double height;
    private float offsetLeft;
    private float offsetRight;
    private CustomListLayoutVO layout;
    private List<CustomListDataVO> data = new ArrayList<CustomListDataVO>();
    private int swipeMode = CustomListView.SWIPE_MODE_NONE;
    private int refreshMode = EUExListViewUtils.REFRESH_MODE_BOTH;
    public int getLeft() {
        return (int) left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public int getTop() {
        return (int) top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public int getWidth() {
        return (int) width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int getHeight() {
        return (int) height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public float getOffsetLeft() {
        return offsetLeft;
    }

    public void setOffsetLeft(float offsetLeft) {
        this.offsetLeft = offsetLeft;
    }

    public float getOffsetRight() {
        return offsetRight;
    }

    public void setOffsetRight(float offsetRight) {
        this.offsetRight = offsetRight;
    }

    public CustomListLayoutVO getLayout() {
        return layout;
    }

    public void setLayout(CustomListLayoutVO layout) {
        this.layout = layout;
    }

    public List<CustomListDataVO> getData() {
        return data;
    }

    public void setData(List<CustomListDataVO> data) {
        this.data = data;
    }

    public int getSwipeMode() {
        return swipeMode;
    }

    public void setSwipeMode(int swipeMode) {
        this.swipeMode = swipeMode;
    }

    public int getRefreshMode() {
        return refreshMode;
    }

    public void setRefreshMode(int refreshMode) {
        this.refreshMode = refreshMode;
    }
}
