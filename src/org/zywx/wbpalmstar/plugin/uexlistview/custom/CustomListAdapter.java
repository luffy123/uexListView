package org.zywx.wbpalmstar.plugin.uexlistview.custom;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.universalimageloader.core.DisplayImageOptions;
import com.ace.universalimageloader.core.ImageLoader;
import com.ace.universalimageloader.core.display.RoundedBitmapDisplayer;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexlistview.EUExListView.StateChangeListener;
import org.zywx.wbpalmstar.plugin.uexlistview.EUExListViewUtils;
import org.zywx.wbpalmstar.plugin.uexlistview.vo.CustomListDataVO;
import org.zywx.wbpalmstar.plugin.uexlistview.vo.CustomListLayoutVO;
import org.zywx.wbpalmstar.util.customlayout.AttriLayoutBean;
import org.zywx.wbpalmstar.util.customlayout.Constants;
import org.zywx.wbpalmstar.util.customlayout.CustomLayoutView;
import org.zywx.wbpalmstar.util.customlayout.vo.ElementItemVO;
import org.zywx.wbpalmstar.util.customlayout.vo.ElementVO;
import org.zywx.wbpalmstar.util.customlayout.vo.LayoutItemVO;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends BaseAdapter{
    private Context mContext;
    private List<LayoutItemVO> mCenterLayouts;
    private List<LayoutItemVO> mLeftLayouts;
    private List<LayoutItemVO> mRightLayouts;
    private List<CustomListDataVO> mList;
    private CustomViewHolder holder;
    private StateChangeListener mListener;
    private ImageLoader loader;
    private DisplayImageOptions options;
    private boolean isSupportLeft = false;
    private boolean isSupportRight = false;

    public CustomListAdapter(Context mContext,
                             CustomListLayoutVO layout,
                             List<CustomListDataVO> data,
                             StateChangeListener listener,
                             boolean isSupportLeft,
                             boolean isSupportRight) {
        this.mContext = mContext;
        this.mCenterLayouts = layout.getCenter();
        this.mLeftLayouts = layout.getLeft();
        this.mRightLayouts = layout.getRight();
        this.mList = data;
        this.mListener = listener;
        if (this.mList == null){
            this.mList = new ArrayList<CustomListDataVO>();
            if (mListener != null){
                mListener.setListViewVisible(View.GONE);
            }
        }
        this.isSupportLeft = isSupportLeft;
        this.isSupportRight = isSupportRight;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();

        loader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CustomListDataVO getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null){
            holder = new CustomViewHolder();
            view = View.inflate(mContext,
                    EUExUtil.getResLayoutID("plugin_listview_item_back_row"), null);
            holder.item_back = (RelativeLayout)view.findViewById(
                    EUExUtil.getResIdID("plugin_item_back"));
            holder.item_front = (RelativeLayout) view.findViewById(
                    EUExUtil.getResIdID("plugin_item_front"));
            holder.item_back_left = (LinearLayout) view.findViewById(
                    EUExUtil.getResIdID("plugin_item_back_left"));
            holder.item_back_right = (LinearLayout) view.findViewById(
                    EUExUtil.getResIdID("plugin_item_back_right"));

            view.setTag(holder);
        }else{
            holder = (CustomViewHolder) view.getTag();
        }
        final int index = position;
        final CustomListDataVO dataVO = getItem(index);
        OnListViewLayoutClickListener swipeListener = new OnListViewLayoutClickListener(index,
                mListener, true);
        OnListViewLayoutClickListener listener = new OnListViewLayoutClickListener(index,
                mListener, false);
        if (isSupportLeft){
            //left
            final ElementVO leftDataList = dataVO.getLeft();
            List<AttriLayoutBean> leftList = getLayoutFormList(leftDataList, mLeftLayouts);
            if (leftList != null){
                CustomLayoutView leftView = new CustomLayoutView(mContext, leftList);
                leftView.setOnClickListener(swipeListener);
                if (leftDataList != null) {
                    List<ElementItemVO> data = leftDataList.getElementData();
                    try {
                        if (data != null && data.size() > 0){
                            for (int i = 0; i < data.size(); i++){
                                final ElementItemVO itemVO = data.get(i);
                                setElementData(leftView, itemVO);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                holder.item_back_left.removeAllViews();
                holder.item_back_left.addView(leftView);
            }
        }else{
            holder.item_back_left.setVisibility(View.GONE);
        }

        if (isSupportRight) {
            //right
            final ElementVO rightDataList = dataVO.getRight();
            List<AttriLayoutBean> rightList = getLayoutFormList(rightDataList, mRightLayouts);
            if (rightList != null) {
                CustomLayoutView rightView = new CustomLayoutView(mContext, rightList);
                rightView.setOnClickListener(swipeListener);
                holder.item_back_right.removeAllViews();
                holder.item_back_right.addView(rightView);
                if (rightDataList != null){
                    List<ElementItemVO> data = rightDataList.getElementData();
                    try {
                        if (data != null && data.size() > 0){
                            for (int i = 0; i < data.size(); i++){
                                final ElementItemVO itemVO = data.get(i);
                                setElementData(rightView, itemVO);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            holder.item_back_right.setVisibility(View.GONE);
        }
        //center
        final ElementVO centerDataList = dataVO.getCenter();
        List<AttriLayoutBean> attriList = getLayoutFormList(centerDataList, mCenterLayouts);
        if (attriList != null){
            CustomLayoutView convertView = new CustomLayoutView(mContext, attriList);
            convertView.setOnClickListener(listener);
            if (centerDataList != null) {
                List<ElementItemVO> data = centerDataList.getElementData();
                try {
                    if (data != null && data.size() > 0){
                        for (int i = 0; i < data.size(); i++){
                            final ElementItemVO itemVO = data.get(i);
                            setElementData(convertView, itemVO);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            holder.item_front.removeAllViewsInLayout();
            holder.item_front.addView(convertView);
        }
        return view;
    }

    private void setElementData(View view, ElementItemVO itemVO){
        final String type = itemVO.getElementType();
        if (type.equals(Constants.TAG_ATTRI_TEXT)){
            if (view.findViewWithTag(itemVO.getId()) != null){
                TextView textView = (TextView)
                        view.findViewWithTag(itemVO.getId());
                if (!TextUtils.isEmpty(itemVO.getText())){
                    textView.setText(itemVO.getText());
                }
            }
        }else if(type.equals(Constants.TAG_LABEL_BUTTON)){
            if (view.findViewWithTag(itemVO.getId()) != null) {
                Button button = (Button)
                        view.findViewWithTag(itemVO.getId());
                if (!TextUtils.isEmpty(itemVO.getText())){
                    button.setText(itemVO.getText());
                }
            }
        }else if (type.equals(Constants.TAG_LABEL_IMG)){
            if (view.findViewWithTag(itemVO.getId()) != null) {
                ImageView img = (ImageView)
                        view.findViewWithTag(itemVO.getId());
                //ACEImageLoader.getInstance().displayImage(img, itemVO.getImgSrc());
                if (!TextUtils.isEmpty(itemVO.getImgSrc())){
                    if(itemVO.getImgSrc().startsWith("http:") ||
                            itemVO.getImgSrc().startsWith("https:")) {
                        loader.displayImage(itemVO.getImgSrc(), img, options);
                    }else {
                        img.setImageBitmap(EUExListViewUtils.getImage(mContext,
                                itemVO.getImgSrc()));
                    }
                }
            }
        }
    }

    private List<AttriLayoutBean> getLayoutFormList(ElementVO data, List<LayoutItemVO> layout) {
        if (layout != null && layout.size() > 0){
            if (layout.size() == 1 ||
                    (data != null && TextUtils.isEmpty(data.getType()))){
                return layout.get(0).getList();
            }
            if (data != null){
                String type = data.getType();
                for (int i = 0; i < layout.size(); i++){
                    if (layout.get(i).getType().equals(type)){
                        return layout.get(i).getList();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (mListener != null){
            if (mList != null && mList.size() > 0){
                mListener.setListViewVisible(View.VISIBLE);
            }else {
                mListener.setListViewVisible(View.GONE);
            }
        }
    }

}
