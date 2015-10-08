package org.zywx.wbpalmstar.plugin.uexlistview.custom;

import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.plugin.uexlistview.EUExListView;
import org.zywx.wbpalmstar.plugin.uexlistview.vo.ResultElementClickVO;
import org.zywx.wbpalmstar.util.customlayout.OnLayoutClickListener;

public class OnListViewLayoutClickListener implements OnLayoutClickListener {

    private int index;
    private EUExListView.StateChangeListener mListener;
    private boolean isCloseSwipeItem;

    public OnListViewLayoutClickListener(int index,
                                         EUExListView.StateChangeListener listener,
                                         boolean flag) {
        this.mListener = listener;
        this.index = index;
        this.isCloseSwipeItem = flag;
    }

    @Override
    public void onClick(String id, String funName) {
        ResultElementClickVO resultVO = new ResultElementClickVO();
        resultVO.setIndex(index);
        resultVO.setId(id);
        if (mListener != null){
            if (isCloseSwipeItem) mListener.closeSwipeItem();
            mListener.onListViewLayoutClick(funName, DataHelper.gson.toJson(resultVO));
        }
    }
}