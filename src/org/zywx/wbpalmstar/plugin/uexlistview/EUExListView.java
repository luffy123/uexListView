package org.zywx.wbpalmstar.plugin.uexlistview;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.ace.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.ace.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.ace.universalimageloader.core.ImageLoader;
import com.ace.universalimageloader.core.ImageLoaderConfiguration;
import com.ace.universalimageloader.core.assist.QueueProcessingType;
import com.ace.universalimageloader.core.download.BaseImageDownloader;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexlistview.BaseListView.Mode;
import org.zywx.wbpalmstar.plugin.uexlistview.BaseListView.OnRefreshListener;
import org.zywx.wbpalmstar.plugin.uexlistview.DataBean.DateItem;
import org.zywx.wbpalmstar.plugin.uexlistview.custom.CustomListAdapter;
import org.zywx.wbpalmstar.plugin.uexlistview.custom.CustomListView;
import org.zywx.wbpalmstar.plugin.uexlistview.custom.CustomViewHolder;
import org.zywx.wbpalmstar.plugin.uexlistview.vo.CustomListDataVO;
import org.zywx.wbpalmstar.plugin.uexlistview.vo.CustomListLayoutVO;
import org.zywx.wbpalmstar.plugin.uexlistview.vo.InsertCustomDataVO;
import org.zywx.wbpalmstar.plugin.uexlistview.vo.OpenCustomVO;
import org.zywx.wbpalmstar.plugin.uexlistview.vo.ResultItemClickVO;
import org.zywx.wbpalmstar.plugin.uexlistview.vo.ResultRefreshVO;
import org.zywx.wbpalmstar.util.customlayout.AttriLayoutBean;
import org.zywx.wbpalmstar.util.customlayout.LayoutUtils;
import org.zywx.wbpalmstar.util.customlayout.vo.LayoutItemVO;

import java.util.ArrayList;
import java.util.List;

public class EUExListView extends EUExBase {
    private static final String BUNDLE_DATA = "data";
    private static final int MSG_OPEN_CUSTOM = 2;
    private static final int MSG_SET_CUSTOM_ITEMS = 3;
    private static final int MSG_CLOSE_CUSTOM = 4;
    private static final String TAG = "EUExListView";
    private static final String TAG_PLUGIN_NAME = "uexListView";
    private static final int MSG_INSERT_CUSTOM_ITEM = 5;
    private static final int MSG_DELETE_CUSTOM_ITEM = 6;
    private static final int MSG_UPDATE_CUSTOM_ITEM = 7;
    private BaseListView lv;
    private EUExAdapter adapter;
    private boolean isOpen = false;
    private int swipeMode = BaseListView.SWIPE_MODE_RIGHT;
    private Mode refreshMode = Mode.BOTH;
    private View view;
    private DataBean datas;
    private List<DateItem> loadDatas, refreshDatas;
    private float leftOffset, rightOffset;

    private View mCustomView;
    private CustomListView mCustomListView;
    private boolean mCustomOpen = false;
    private CustomListAdapter mCustomAdapter;
    private List<CustomListDataVO> mCustomListViewData;

    public EUExListView(Context context, EBrowserView eBrowserView) {
        super(context, eBrowserView);
    }


    private void addView2CurrentWindow(View child,
                                       LayoutParams parms) {
        int l = (int) (parms.leftMargin);
        int t = (int) (parms.topMargin);
        int w = parms.width;
        int h = parms.height;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
        lp.gravity = Gravity.NO_GRAVITY;
        lp.leftMargin = l;
        lp.topMargin = t;
        adptLayoutParams(parms, lp);
        mBrwView.addViewToCurrentWindow(child, lp);
    }

    public BaseListView getLv() {
        return lv;
    }

    public void setItems(String[] params) {
        if (params != null && params.length == 1) {
            datas = EUExListViewUtils.parseDataBean(params[0]);
        }
    }

    public void open(String[] params) {
        if (params != null && params.length == 1) {
            try {
                if (datas.getDates() == null || isOpen) {
                    return;
                }
                JSONObject json = new JSONObject(params[0]);
                final float x = Float.parseFloat(json.getString(EUExListViewUtils.LISTVIEW_PARAMS_JSON_KEY_X));
                final float y = Float.parseFloat(json.getString(EUExListViewUtils.LISTVIEW_PARAMS_JSON_KEY_Y));
                final float w = Float.parseFloat(json.getString(EUExListViewUtils.LISTVIEW_PARAMS_JSON_KEY_W));
                final float h = Float.parseFloat(json.getString(EUExListViewUtils.LISTVIEW_PARAMS_JSON_KEY_H));
                ((Activity) mContext).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        view = View.inflate(mContext, EUExUtil.getResLayoutID("activity_main"), null);
                        lv = (BaseListView) view.findViewById(EUExUtil.getResIdID("plugin_listview_lv"));
                        lv.setSwipeMode(swipeMode);
                        lv.setMode(refreshMode);
                        if (datas.getSopLeft() != null) {
                            float lScale = datas.getSopLeft().getBts().size() > 1 ? 0.5f : 0.75f;
                            leftOffset = w * lScale;
                            lv.setOffsetRight(leftOffset);
                        }
                        if (datas.getSopRight() != null) {
                            float rScale = datas.getSopRight().getBts().size() > 1 ? 0.5f : 0.75f;
                            rightOffset = w * rScale;
                            lv.setOffsetLeft(rightOffset);
                        }
                        adapter = new EUExAdapter(EUExListView.this, mContext, datas);
                        lv.setOnHeaderRefreshListener(onRefresh);
                        lv.setOnFooterRefreshListener(onRefresh);
                        lv.setSwipeListViewListener(new MyListViewListener());
                        lv.setAdapter(adapter);

                        LayoutParams param = new LayoutParams((int) w, (int) h);
                        param.leftMargin = (int) x;
                        param.topMargin = (int) y;
                        addView2CurrentWindow(view, param);
                        isOpen = true;
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void close(String[] params) {

        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (lv != null) {
                        lv.removeAllViewsInLayout();
                        adapter = null;
                        lv = null;
                        removeViewFromCurrentWindow(view);
                    }
                    if (datas != null) {
                        if (datas.getDates() != null) {
                            datas.getDates().clear();
                        }
                        if (datas.getSopLeft() != null && datas.getSopLeft().getBts() != null) {
                            datas.getSopLeft().getBts().clear();
                        }
                        if (datas.getSopRight() != null && datas.getSopRight().getBts() != null) {
                            datas.getSopRight().getBts().clear();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    datas = null;
                    isOpen = false;
                }
            }
        });
    }

    @Override
    protected boolean clean() {
        close(null);
        return false;
    }

    private OnRefreshListener onRefresh = new OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (lv.isHeaderRefreshable()) {
                String js = EUExListViewUtils.SCRIPT_HEADER + "if(" + EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHHEADERLISTENER + "){" + EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHHEADERLISTENER + "(0)}";
                onCallback(js);
            }
            if (lv.isFooterRefreshable()) {
                String js = EUExListViewUtils.SCRIPT_HEADER + "if(" + EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHFOOTERLISTENER + "){" + EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHFOOTERLISTENER + "(0)}";
                onCallback(js);
            }
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... params) {
                    try {
                        if (lv.isHeaderRefreshable()) {
                            String js = EUExListViewUtils.SCRIPT_HEADER + "if(" + EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHHEADERLISTENER + "){" + EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHHEADERLISTENER + "(1)}";
                            onCallback(js);
                        }
                        if (lv.isFooterRefreshable()) {
                            String js = EUExListViewUtils.SCRIPT_HEADER + "if(" + EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHFOOTERLISTENER + "){" + EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHFOOTERLISTENER + "(1)}";
                            onCallback(js);
                        }
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    adapter.notifyDataSetChanged();
                    lv.onRefreshComplete();
                    if (lv.isHeaderRefreshable()) {
                        String js = EUExListViewUtils.SCRIPT_HEADER + "if(" + EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHHEADERLISTENER + "){" + EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHHEADERLISTENER + "(2)}";
                        onCallback(js);
                        lv.setSelectionAfterHeaderView();
                    }
                    if (lv.isFooterRefreshable()) {
                        String js = EUExListViewUtils.SCRIPT_HEADER + "if(" + EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHFOOTERLISTENER + "){" + EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHFOOTERLISTENER + "(2)}";
                        onCallback(js);
                    }
                }
            }.execute(null, null, null);
        }
    };

    private CustomListView.OnRefreshListener onCustomRefresh = new CustomListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (mCustomListView.isHeaderRefreshable()) {
                ResultRefreshVO resultVO = new ResultRefreshVO(0);
                callBackPluginJs(JsConst.ON_CUSTOM_PULL_REFRESH_HEADER,
                        DataHelper.gson.toJson(resultVO));
                //String js = EUExListViewUtils.SCRIPT_HEADER + "if("+EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHHEADERLISTENER+"){"+EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHHEADERLISTENER+"(0)}";
                //onCallback(js);
            }
            if (mCustomListView.isFooterRefreshable()) {
                ResultRefreshVO resultVO = new ResultRefreshVO(0);
                callBackPluginJs(JsConst.ON_CUSTOM_PULL_REFRESH_FOOTER,
                        DataHelper.gson.toJson(resultVO));
                // String js = EUExListViewUtils.SCRIPT_HEADER + "if("+EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHFOOTERLISTENER+"){"+EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHFOOTERLISTENER+"(0)}";
                //onCallback(js);
            }
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... params) {
                    try {
                        if (mCustomListView.isHeaderRefreshable()) {
                            ResultRefreshVO resultVO = new ResultRefreshVO(1);
                            callBackPluginJs(JsConst.ON_CUSTOM_PULL_REFRESH_HEADER,
                                    DataHelper.gson.toJson(resultVO));
                            //String js = EUExListViewUtils.SCRIPT_HEADER + "if("+EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHHEADERLISTENER+"){"+EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHHEADERLISTENER+"(1)}";
                            //onCallback(js);
                        }
                        if (mCustomListView.isFooterRefreshable()) {
                            ResultRefreshVO resultVO = new ResultRefreshVO(1);
                            callBackPluginJs(JsConst.ON_CUSTOM_PULL_REFRESH_FOOTER,
                                    DataHelper.gson.toJson(resultVO));
                            //String js = EUExListViewUtils.SCRIPT_HEADER + "if("+EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHFOOTERLISTENER+"){"+EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHFOOTERLISTENER+"(1)}";
                            //onCallback(js);
                        }
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    mCustomAdapter.notifyDataSetChanged();
                    mCustomListView.onRefreshComplete();
                    if (mCustomListView.isHeaderRefreshable()) {
                        ResultRefreshVO resultVO = new ResultRefreshVO(2);
                        callBackPluginJs(JsConst.ON_CUSTOM_PULL_REFRESH_HEADER,
                                DataHelper.gson.toJson(resultVO));
                        //String js = EUExListViewUtils.SCRIPT_HEADER + "if("+EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHHEADERLISTENER+"){"+EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHHEADERLISTENER+"(2)}";
                        //onCallback(js);
                        mCustomListView.setSelectionAfterHeaderView();
                    }
                    if (mCustomListView.isFooterRefreshable()) {
                        ResultRefreshVO resultVO = new ResultRefreshVO(2);
                        callBackPluginJs(JsConst.ON_CUSTOM_PULL_REFRESH_FOOTER,
                                DataHelper.gson.toJson(resultVO));
                        //String js = EUExListViewUtils.SCRIPT_HEADER + "if("+EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHFOOTERLISTENER+"){"+EUExListViewUtils.F_CALLBACK_NAME_ONTPULLREFRESHFOOTERLISTENER+"(2)}";
                        //onCallback(js);
                    }
                }
            }.execute(null, null, null);
        }
    };

    public void setItemSwipeType(String[] params) {
        if (params != null && params.length == 1) {
            try {
                int swipeType = Integer.parseInt(params[0]);
                switch (swipeType) {
                    case EUExListViewUtils.SWIPE_MODE_DEFAULT:
                    case EUExListViewUtils.SWIPE_MODE_RIGHT:
                        swipeMode = BaseListView.SWIPE_MODE_RIGHT;
                        break;
                    case EUExListViewUtils.SWIPE_MODE_LEFT:
                        swipeMode = BaseListView.SWIPE_MODE_LEFT;
                        break;
                    case EUExListViewUtils.SWIPE_MODE_BOTH:
                        swipeMode = BaseListView.SWIPE_MODE_BOTH;
                        break;
                    case EUExListViewUtils.SWIPE_MODE_NONE:
                        swipeMode = BaseListView.SWIPE_MODE_NONE;
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setRefreshEnabled(String[] params) {
        if (params != null && params.length == 1) {
            try {
                int RefreshType = Integer.parseInt(params[0]);
                switch (RefreshType) {
                    case EUExListViewUtils.REFRESH_MODE_DEFAULT:
                        refreshMode = Mode.DISABLE;
                        break;
                    case EUExListViewUtils.REFRESH_MODE_PULL:
                        refreshMode = Mode.HEADER;
                        break;
                    case EUExListViewUtils.REFRESH_MODE_UP:
                        refreshMode = Mode.FOOTER;
                        break;
                    case EUExListViewUtils.REFRESH_MODE_BOTH:
                        refreshMode = Mode.BOTH;
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setEditingEnabled(final String[] params) {
        if (params != null && params.length == 1) {
            ((Activity) mContext).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (lv != null && adapter != null) {
                            boolean isEditable = Boolean.parseBoolean(params[0]);
                            if (isEditable) {
                                lv.closeOpenedItems();
                                lv.setSwipeMode(BaseListView.SWIPE_MODE_NONE);
                                lv.setMode(Mode.DISABLE);
                            } else {
                                lv.setSwipeMode(swipeMode);
                                lv.setMode(refreshMode);
                            }
                            adapter.setEditable(isEditable);
                            adapter.notifyDataSetInvalidated();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void setPullRefreshHeader(String[] params) {
        if (params != null && params.length == 1) {
            try {
                RefreshBean data = EUExListViewUtils.parseRefreshData(params[0]);
                BaseListView.setHeaderViewData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setPullRefreshFooter(String[] params) {
        if (params != null && params.length == 1) {
            try {
                RefreshBean data = EUExListViewUtils.parseRefreshData(params[0]);
                BaseListView.setFooterViewData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void appendItems(String[] params) {
        if (params != null && params.length == 1) {
            try {
                JSONObject json = new JSONObject(params[0]);
                JSONArray array = json.getJSONArray(EUExListViewUtils.LISTVIEW_PARAMS_JSON_KEY_LISTITEMS);
                loadDatas = EUExListViewUtils.parseDataItems(array);
                ((Activity) mContext).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (loadDatas != null) {
                            datas.getDates().addAll(loadDatas);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reloadItems(String[] params) {
        if (params != null && params.length == 1) {
            try {
                JSONObject json = new JSONObject(params[0]);
                JSONArray array = json.getJSONArray(EUExListViewUtils.LISTVIEW_PARAMS_JSON_KEY_LISTITEMS);
                refreshDatas = EUExListViewUtils.parseDataItems(array);
                ((Activity) mContext).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (refreshDatas != null) {
                            datas.getDates().clear();
                            adapter.notifyDataSetChanged();
                            datas.getDates().addAll(refreshDatas);
                            refreshDatas.clear();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteItemsAt(final String[] params) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                List<DateItem> temp = new ArrayList<DateItem>();
                try {
                    JSONObject obj = new JSONObject(params[0]);
                    JSONArray array = obj.getJSONArray(EUExListViewUtils.LISTVIEW_PARAMS_JSON_KEY_ITEMINDEX);
                    for (int i = 0; i < array.length(); i++) {
                        int index = Integer.parseInt(array.getString(i));
                        if (datas == null || index > (datas.getDates().size() - 1)) {
                            return;
                        }
                        if (!temp.contains(datas.getDates().get(index))) {
                            temp.add(datas.getDates().get(index));
                        }
                    }
                    datas.getDates().removeAll(temp);
                    temp.clear();
                    adapter.notifyDataSetChanged();
                    if (datas.getDates().size() == 0) {
                        close(new String[]{});
                        isOpen = false;
                    } else {
                        lv.setSelection(Integer.parseInt(array.getString(0)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void insertItemAt(final String[] params) {
        if (params != null && params.length == 1) {
            ((Activity) mContext).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        JSONObject json = new JSONObject(params[0]);
                        int index = Integer.parseInt(json.getString(EUExListViewUtils.LISTVIEW_PARAMS_JSON_KEY_ITEMINDEX));
                        DateItem item = EUExListViewUtils.parseDataItem(json.getJSONObject(EUExListViewUtils.LISTVIEW_PARAMS_JSON_KEY_LISTITEM));
                        if (datas == null || index > (datas.getDates().size() - 1)) {
                            return;
                        }
                        datas.getDates().add(index, item);
                        adapter.notifyDataSetChanged();
                        lv.setSelection(index);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void replaceItemsAt(String[] params) {
    }

    public void updateItemAt(String[] params) {
    }

    public void setUserCanEditItems(String[] params) {
    }

    public class MyListViewListener extends BaseSwipeListViewListener {

        @Override
        public void onClickFrontView(int position) {
            lv.closeOpenedItems();
            String js = EUExListViewUtils.SCRIPT_HEADER + "if(" + EUExListViewUtils.F_CALLBACK_NAME_ONITEMCLICK + "){" + EUExListViewUtils.F_CALLBACK_NAME_ONITEMCLICK + "(" + (position - 1) + ")}";
            onCallback(js);
            super.onClickFrontView(position);
        }

        @Override
        public void onClickBackView(int position) {
            super.onClickBackView(position);
        }

        @Override
        public void onMove(int position, float x) {
            View view = lv.getChildAt(position - lv.getFirstVisiblePosition());
            if (view != null) {
                ViewHolder holder = (ViewHolder) view.getTag();
                if (holder != null) {/*
                    if(Math.abs(x) < leftOffset || Math.abs(x) < rightOffset) {
						holder.lbt1.setVisibility(View.VISIBLE);
						holder.lbt2.setVisibility(View.VISIBLE);
						holder.rbt1.setVisibility(View.VISIBLE);
						holder.rbt2.setVisibility(View.VISIBLE);
					}
					if(Math.abs(x) > Math.max(leftOffset, rightOffset)) {
						if(x < Math.min(leftOffset, rightOffset)) {
							holder.lbt1.setVisibility(View.INVISIBLE);
							holder.lbt2.setVisibility(View.INVISIBLE);
						}
						if(x > Math.min(leftOffset, rightOffset)) {
							holder.rbt1.setVisibility(View.INVISIBLE);
							holder.rbt2.setVisibility(View.INVISIBLE);
						}
					}
				*/
                }
            }
        }

        @Override
        public void onStartOpen(int position, int action, boolean right) {
            View view = lv.getChildAt(position - lv.getFirstVisiblePosition());
            if (view != null) {
                ViewHolder holder = (ViewHolder) view.getTag();
                if (holder != null) {
                    if (right) {
                        if (datas != null && datas.getSopLeft() != null && datas.getSopLeft().getBts() != null) {
                            if (datas.getSopLeft().getBts().size() < 2) {
                                holder.lbt1.setVisibility(View.INVISIBLE);
                            }
                            holder.rbt1.setVisibility(View.INVISIBLE);
                            holder.rbt2.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (datas != null && datas.getSopRight() != null && datas.getSopRight().getBts() != null) {
                            if (datas.getSopRight().getBts().size() < 2) {
                                holder.rbt1.setVisibility(View.INVISIBLE);
                            }
                            holder.lbt1.setVisibility(View.INVISIBLE);
                            holder.lbt2.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        }

        @Override
        public void onClosed(int position, boolean fromRight) {
            View view = lv.getChildAt(position - lv.getFirstVisiblePosition());
            if (view != null) {
                ViewHolder holder = (ViewHolder) view.getTag();
                if (holder != null) {
                    holder.lbt1.setVisibility(View.VISIBLE);
                    holder.lbt2.setVisibility(View.VISIBLE);
                    holder.rbt1.setVisibility(View.VISIBLE);
                    holder.rbt2.setVisibility(View.VISIBLE);
                }
            }
        }


    }

    public void openCustom(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        if (mCustomOpen) {
            errorCallback(0, 0, "already opened!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_OPEN_CUSTOM;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void openCustomMsg(String[] params) {
        String json = params[0];
        OpenCustomVO dataVO = DataHelper.gson.fromJson(json, OpenCustomVO.class);
        CustomListLayoutVO layoutVO = dataVO.getLayout();
        if (layoutVO == null || layoutVO.getCenter() == null
                || layoutVO.getCenter().size() < 1) {
            errorCallback(0, 0, "please set layout!");
            return;
        }
        for (int i = 0; i < layoutVO.getCenter().size(); i++) {
            LayoutItemVO item = layoutVO.getCenter().get(i);
            List<AttriLayoutBean> list = LayoutUtils.parseXmlData(mContext,
                    mBrwView, item.getSrc());
            item.setList(list);
        }
        if (layoutVO.getLeft() != null && layoutVO.getLeft().size() > 0) {
            for (int i = 0; i < layoutVO.getLeft().size(); i++) {
                LayoutItemVO item = layoutVO.getLeft().get(i);
                List<AttriLayoutBean> list = LayoutUtils.parseXmlData(mContext,
                        mBrwView, item.getSrc());
                item.setList(list);
            }
        }
        if (layoutVO.getRight() != null && layoutVO.getRight().size() > 0) {
            for (int i = 0; i < layoutVO.getRight().size(); i++) {
                LayoutItemVO item = layoutVO.getRight().get(i);
                List<AttriLayoutBean> list = LayoutUtils.parseXmlData(mContext,
                        mBrwView, item.getSrc());
                item.setList(list);
            }
        }
        mCustomView = View.inflate(mContext,
                EUExUtil.getResLayoutID("plugin_listview_custom_listview"), null);
        mCustomListView = (CustomListView) mCustomView.findViewById(
                EUExUtil.getResIdID("plugin_listview_lv"));
        int swipeMode = getSwipeMode(dataVO.getSwipeMode());
        mCustomListView.setSwipeMode(swipeMode);
        mCustomListView.setMode(getRefreshMode(dataVO.getRefreshMode()));
        boolean isSupportLeft = false;
        boolean isSupportRight = false;
        switch (swipeMode) {
            case CustomListView.SWIPE_MODE_BOTH:
                isSupportLeft = true;
                isSupportRight = true;
                break;
            case CustomListView.SWIPE_MODE_LEFT:
                isSupportLeft = false;
                isSupportRight = true;
                break;
            case CustomListView.SWIPE_MODE_RIGHT:
                isSupportLeft = true;
                isSupportRight = false;
                break;
            case CustomListView.SWIPE_MODE_NONE:
                isSupportLeft = false;
                isSupportRight = false;
                break;
        }
        if (isSupportLeft) mCustomListView.setOffsetRight(dataVO.getWidth()
                - dataVO.getOffsetRight());
        if (isSupportRight) mCustomListView.setOffsetLeft(dataVO.getWidth()
                - dataVO.getOffsetLeft());
        mCustomListViewData = dataVO.getData();
        mCustomAdapter = new CustomListAdapter(mContext, dataVO.getLayout(),
                mCustomListViewData, mStateChangeListener, isSupportLeft, isSupportRight);
        mCustomListView.setOnFooterRefreshListener(onCustomRefresh);
        mCustomListView.setOnHeaderRefreshListener(onCustomRefresh);
        mCustomListView.setSwipeListViewListener(new CustomListViewListener());
        mCustomListView.setAdapter(mCustomAdapter);
        mCustomAdapter.notifyDataSetChanged();
        LayoutParams param = new LayoutParams(dataVO.getWidth(), dataVO.getHeight());
        param.leftMargin = dataVO.getLeft();
        param.topMargin = dataVO.getTop();
        addView2CurrentWindow(mCustomView, param);
        mCustomOpen = true;
    }

    private CustomListView.Mode getRefreshMode(int refreshMode) {
        CustomListView.Mode mode;
        switch (refreshMode) {
            case EUExListViewUtils.REFRESH_MODE_PULL:
                mode = CustomListView.Mode.HEADER;
                break;
            case EUExListViewUtils.REFRESH_MODE_UP:
                mode = CustomListView.Mode.FOOTER;
                break;
            case EUExListViewUtils.REFRESH_MODE_DEFAULT:
                mode = CustomListView.Mode.DISABLE;
                break;
            case EUExListViewUtils.REFRESH_MODE_BOTH:
            default:
                mode = CustomListView.Mode.BOTH;
                break;
        }
        return mode;
    }

    private int getSwipeMode(int swipeMode) {
        int swipe;
        switch (swipeMode) {
            case EUExListViewUtils.SWIPE_MODE_RIGHT:
                swipe = CustomListView.SWIPE_MODE_RIGHT;
                break;
            case EUExListViewUtils.SWIPE_MODE_LEFT:
                swipe = CustomListView.SWIPE_MODE_LEFT;
                break;
            case EUExListViewUtils.SWIPE_MODE_BOTH:
                swipe = CustomListView.SWIPE_MODE_BOTH;
                break;
            case EUExListViewUtils.SWIPE_MODE_NONE:
            case EUExListViewUtils.SWIPE_MODE_DEFAULT:
            default:
                swipe = CustomListView.SWIPE_MODE_NONE;
                break;
        }
        return swipe;
    }

    public void setCustomItems(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SET_CUSTOM_ITEMS;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void setCustomItemsMsg(String[] params) {
        String json = params[0];
        List<CustomListDataVO> dataVO = DataHelper.gson.fromJson(json,
                new TypeToken<List<CustomListDataVO>>() {
                }.getType());
        if (dataVO != null && dataVO.size() > 0
                && mCustomListViewData != null && mCustomAdapter != null) {
            mCustomListViewData.clear();
            mCustomListViewData.addAll(dataVO);
            mCustomAdapter.notifyDataSetChanged();
        }
    }

    public void closeCustom(String[] params) {
        if (!mCustomOpen) {
            errorCallback(0, 0, "no custom listview!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_CLOSE_CUSTOM;
        mHandler.sendMessage(msg);
    }

    private void closeCustomMsg() {
        if (mCustomOpen) {
            removeViewFromCurrentWindow(mCustomView);
            mCustomListViewData.clear();
            mCustomAdapter = null;
            mCustomListView.removeAllViewsInLayout();
            mCustomListView = null;
            mCustomOpen = false;
        }
    }

    public class CustomListViewListener extends BaseSwipeListViewListener {

        @Override
        public void onClickFrontView(int position) {
            mCustomListView.closeOpenedItems();
            ResultItemClickVO resultVO = new ResultItemClickVO();
            resultVO.setIndex(position - 1);
            callBackPluginJs(JsConst.ON_CUSTOM_ITEM_CLICK, DataHelper.gson.toJson(resultVO));
            //String js = EUExListViewUtils.SCRIPT_HEADER + "if("+EUExListViewUtils.F_CALLBACK_NAME_ONITEMCLICK+"){"+EUExListViewUtils.F_CALLBACK_NAME_ONITEMCLICK+"("+(position - 1)+")}";
            //onCallback(js);
            super.onClickFrontView(position);
        }

        @Override
        public void onClickBackView(int position) {
            super.onClickBackView(position);
        }

        @Override
        public void onMove(int position, float x) {
            View view = mCustomListView.getChildAt(position -
                    mCustomListView.getFirstVisiblePosition());
            if (view != null) {
                CustomViewHolder holder = (CustomViewHolder) view.getTag();
                if (holder != null) {/*
					if(Math.abs(x) < leftOffset || Math.abs(x) < rightOffset) {
						holder.lbt1.setVisibility(View.VISIBLE);
						holder.lbt2.setVisibility(View.VISIBLE);
						holder.rbt1.setVisibility(View.VISIBLE);
						holder.rbt2.setVisibility(View.VISIBLE);
					}
					if(Math.abs(x) > Math.max(leftOffset, rightOffset)) {
						if(x < Math.min(leftOffset, rightOffset)) {
							holder.lbt1.setVisibility(View.INVISIBLE);
							holder.lbt2.setVisibility(View.INVISIBLE);
						}
						if(x > Math.min(leftOffset, rightOffset)) {
							holder.rbt1.setVisibility(View.INVISIBLE);
							holder.rbt2.setVisibility(View.INVISIBLE);
						}
					}
				*/
                }
            }
        }

        @Override
        public void onStartOpen(int position, int action, boolean right) {
            View view = mCustomListView.getChildAt(position -
                    mCustomListView.getFirstVisiblePosition());
            if (view != null) {
                CustomViewHolder holder = (CustomViewHolder) view.getTag();
                if (holder != null) {
                    if (right) {
                        holder.item_back_left.setVisibility(View.VISIBLE);
                        holder.item_back_right.setVisibility(View.INVISIBLE);
                    } else {
                        holder.item_back_left.setVisibility(View.INVISIBLE);
                        holder.item_back_right.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        @Override
        public void onClosed(int position, boolean fromRight) {
            View view = mCustomListView.getChildAt(position -
                    mCustomListView.getFirstVisiblePosition());
            if (view != null) {
                CustomViewHolder holder = (CustomViewHolder) view.getTag();
                if (holder != null) {
                    holder.item_back_left.setVisibility(View.VISIBLE);
                    holder.item_back_right.setVisibility(View.VISIBLE);
                }
            }
        }


    }

    public void insertCustomItem(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_INSERT_CUSTOM_ITEM;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void insertCustomItemMsg(String[] params) {
        String json = params[0];
        InsertCustomDataVO dataVO = DataHelper.gson.fromJson(json,
                InsertCustomDataVO.class);
        int index = dataVO.getIndex();
        if (mCustomListViewData != null && mCustomAdapter != null) {
            //index不传或者传入的index大于等于列表长度时，添加到列表项末尾
            if (index < 0 || index >= mCustomListViewData.size()) {
                mCustomListViewData.add(dataVO.getData());
            } else {
                mCustomListViewData.add(index, dataVO.getData());
            }
            mCustomAdapter.notifyDataSetChanged();
        }
    }

    public void deleteCustomItem(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_DELETE_CUSTOM_ITEM;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void deleteCustomItemMsg(String[] params) {
        String json = params[0];
        List<Integer> indexs = DataHelper.gson.fromJson(json,
                new TypeToken<List<Integer>>() {
                }.getType());
        if (mCustomListViewData != null && mCustomAdapter != null) {
            if (mCustomListViewData.size() > 0) {
                List<CustomListDataVO> temp = new ArrayList<CustomListDataVO>();
                for (int i = 0; i < indexs.size(); i++) {
                    if (indexs.get(i) < mCustomListViewData.size() &&
                            indexs.get(i) >= 0) {
                        temp.add(mCustomListViewData.get(indexs.get(i)));
                    } else {
                        errorCallback(0, 0, "index " + indexs.get(i) + " is not exist!");
                    }
                }
                mCustomListViewData.removeAll(temp);
                temp.clear();
                mCustomAdapter.notifyDataSetChanged();
            } else {
                errorCallback(0, 0, "listview has no item!");
            }
        } else {
            errorCallback(0, 0, "please open listview first!");
        }
    }

    public void updateCustomItem(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_UPDATE_CUSTOM_ITEM;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void updateCustomItemMsg(String[] params) {
        String json = params[0];
        InsertCustomDataVO dataVO = DataHelper.gson.fromJson(json,
                InsertCustomDataVO.class);
        int index = dataVO.getIndex();
        if (index < 0 || index >= mCustomListViewData.size()) {
            errorCallback(0, 0, "error index!");
            return;
        }
        if (mCustomListViewData != null && mCustomAdapter != null) {
            mCustomListViewData.remove(index);
            mCustomListViewData.add(index, dataVO.getData());
            mCustomAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onHandleMessage(Message message) {
        if (message == null) {
            return;
        }
        Bundle bundle = message.getData();
        switch (message.what) {
            case MSG_OPEN_CUSTOM:
                openCustomMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_SET_CUSTOM_ITEMS:
                setCustomItemsMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_CLOSE_CUSTOM:
                closeCustomMsg();
                break;
            case MSG_INSERT_CUSTOM_ITEM:
                insertCustomItemMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_DELETE_CUSTOM_ITEM:
                deleteCustomItemMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_UPDATE_CUSTOM_ITEM:
                updateCustomItemMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            default:
                super.onHandleMessage(message);
        }
    }

    private void callBackPluginJs(String methodName, String jsonData) {
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "('" + jsonData + "');}";
        Log.i(TAG, "callBackPluginJs:" + js);
        onCallback(js);
    }

    public interface StateChangeListener {
        public void setListViewVisible(int visible);

        public void closeSwipeItem();

        public void onListViewLayoutClick(String funName, String js);
    }

    private StateChangeListener mStateChangeListener = new StateChangeListener() {
        @Override
        public void setListViewVisible(int visible) {
            if (mCustomView != null && mCustomView.getVisibility() != visible) {
                mCustomView.setVisibility(visible);
            }
        }

        @Override
        public void closeSwipeItem() {
            if (mCustomListView != null) {
                mCustomListView.closeOpenedItems();
            }
        }

        @Override
        public void onListViewLayoutClick(String funName, String js) {
            callBackPluginJs(TAG_PLUGIN_NAME + "." + funName, js);
        }
    };
}
