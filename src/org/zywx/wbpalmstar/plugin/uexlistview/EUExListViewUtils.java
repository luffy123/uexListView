package org.zywx.wbpalmstar.plugin.uexlistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.webkit.URLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.ACEImageLoader;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.plugin.uexlistview.DataBean.DateItem;
import org.zywx.wbpalmstar.plugin.uexlistview.DataBean.SwipeOption;
import org.zywx.wbpalmstar.plugin.uexlistview.DataBean.SwipeOption.ButtonItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EUExListViewUtils {

    public final static int SWIPE_MODE_DEFAULT = -1;
    public final static int SWIPE_MODE_RIGHT = 0;
    public final static int SWIPE_MODE_LEFT = 1;
    public final static int SWIPE_MODE_BOTH = 2;
    public final static int SWIPE_MODE_NONE = 3;

    public final static int REFRESH_MODE_DEFAULT = 0;
    public final static int REFRESH_MODE_PULL = 1;
    public final static int REFRESH_MODE_UP = 2;
    public final static int REFRESH_MODE_BOTH = 3;

    private static int buttonWidthMax = 260;
    private static int buttonWidthDefalut = 90;

    public final static String LISTVIEW_PARAMS_JSON_KEY_X = "x";
    public final static String LISTVIEW_PARAMS_JSON_KEY_Y = "y";
    public final static String LISTVIEW_PARAMS_JSON_KEY_W = "w";
    public final static String LISTVIEW_PARAMS_JSON_KEY_H = "h";
    private final static String LISTVIEW_PARAMS_JSON_KEY_RIGHTSWIPEOPTIONITEM = "rightSwipeOptionItem";
    private final static String LISTVIEW_PARAMS_JSON_KEY_LEFTSWIPEOPTIONITEM = "leftSwipeOptionItem";
    public final static String LISTVIEW_PARAMS_JSON_KEY_LISTITEMS = "listItems";
    public final static String LISTVIEW_PARAMS_JSON_KEY_LISTITEM = "listItem";

    public final static String LISTVIEW_PARAMS_JSON_KEY_SWIPEOPTIONITEM_BACKGROUNDCOLOR = "backgroundColor";
    public final static String LISTVIEW_PARAMS_JSON_KEY_SWIPEOPTIONITEM_OPTIONBTN = "optionBtn";

    public final static String LISTVIEW_PARAMS_JSON_KEY_ITEMINDEX = "itemIndex";

    public final static String LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_BACKGROUNDCOLOR = "backgroundColor";
    public final static String LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_HEIGHT = "height";
    public final static String LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_IMAGE = "image";
    public final static String LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_PLACEHOLDERIMG = "placeholderImg";
    public final static String LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_RIGHTBTNIMG = "rightBtnImg";
    public final static String LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SELECTEDBACKGROUNDCOLOR = "selectedBackgroundColor";
    public final static String LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SUBTITLE = "subtitle";
    public final static String LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SUBTITLECOLOR = "subtitleColor";
    public final static String LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SUBTITLESIZE = "subtitleSize";
    public final static String LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_TITLE = "title";
    public final static String LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_TITLECOLOR = "titleColor";
    public final static String LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_TITLESIZE = "titleSize";

    public final static String LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_BGCOLOR = "bgColor";
    public final static String LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_BTNINDEX = "btnIndex";
    public final static String LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_TEXT = "text";
    public final static String LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_TEXTCOLOR = "textColor";
    public final static String LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_TEXTSIZE = "textSize";


    public static final String SCRIPT_HEADER = "javascript:";
    public static final String F_CALLBACK_NAME_ONITEMCLICK = "uexListView.onItemClick";
    public static final String F_CALLBACK_NAME_ONLEFTOPTIONBUTTONINITEM = "uexListView.onLeftOptionButtonInItem";
    public static final String F_CALLBACK_NAME_ONRIGHTOPTIONBUTTONINITEM = "uexListView.onRightOptionButtonInItem";
    public static final String F_CALLBACK_NAME_ONTPULLREFRESHHEADERLISTENER = "uexListView.ontPullRefreshHeaderListener";
    public static final String F_CALLBACK_NAME_ONTPULLREFRESHFOOTERLISTENER = "uexListView.ontPullRefreshFooterListener";


    private static final String LISTVIEW_PARAMS_JSON_KEY_PULLREFRESHHEADERSTYLE = "PullRefreshHeaderStyle";
    private static final String LISTVIEW_PARAMS_JSON_KEY_PULLREFRESHFOOTERSTYLE = "PullRefreshFooterStyle";
    private static final String LISTVIEW_PARAMS_JSON_KEY_REFRESH_ARROWIMAGE = "arrowImage";
    private static final String LISTVIEW_PARAMS_JSON_KEY_REFRESH_BACKGROUNDCOLOR = "backGroundColor";
    private static final String LISTVIEW_PARAMS_JSON_KEY_REFRESH_ISSHOWUPDATEDATE = "isShowUpdateDate";
    private static final String LISTVIEW_PARAMS_JSON_KEY_REFRESH_PULLREFRESHLOADINGTEXT = "pullRefreshLoadingText";
    private static final String LISTVIEW_PARAMS_JSON_KEY_REFRESH_PULLREFRESHNORMALTEXT = "pullRefreshNormalText";
    private static final String LISTVIEW_PARAMS_JSON_KEY_REFRESH_PULLREFRESHPULLINGTEXT = "pullRefreshPullingText";
    private static final String LISTVIEW_PARAMS_JSON_KEY_REFRESH_TEXTCOLOR = "textColor";
    private static final String LISTVIEW_PARAMS_JSON_KEY_REFRESH_TEXTFONTSIZE = "textFontSize";

    public static DataBean parseDataBean(String string) {
        DataBean bean = null;
        try {
            bean = new DataBean();
            JSONObject json = new JSONObject(string);

            if (json.has(LISTVIEW_PARAMS_JSON_KEY_LEFTSWIPEOPTIONITEM)) {
                SwipeOption sopLeft = new DataBean().new SwipeOption();
                JSONObject leftSwipOption = json.getJSONObject(LISTVIEW_PARAMS_JSON_KEY_LEFTSWIPEOPTIONITEM);
                if (leftSwipOption.has(LISTVIEW_PARAMS_JSON_KEY_SWIPEOPTIONITEM_BACKGROUNDCOLOR)) {
                    sopLeft.setBackgroundColor(leftSwipOption.getString(LISTVIEW_PARAMS_JSON_KEY_SWIPEOPTIONITEM_BACKGROUNDCOLOR));
                }
                JSONArray LeftBtn = leftSwipOption.getJSONArray(LISTVIEW_PARAMS_JSON_KEY_SWIPEOPTIONITEM_OPTIONBTN);
                List<ButtonItem> leftBts = new ArrayList<ButtonItem>();
                for (int i = 0; i < LeftBtn.length(); i++) {
                    JSONObject item = LeftBtn.getJSONObject(i);
                    ButtonItem bt = new DataBean().new SwipeOption().new ButtonItem();
                    bt.setText(item.getString(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_TEXT));
                    if (item.has(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_TEXTCOLOR)) {
                        bt.setTextColor(item.getString(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_TEXTCOLOR));
                    }
                    if (item.has(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_TEXTSIZE)) {
                        bt.setTextSize(item.getString(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_TEXTSIZE));
                    }
                    if (item.has(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_BGCOLOR)) {
                        bt.setBgColor(item.getString(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_BGCOLOR));
                    }
                    bt.setBtIndex(item.getString(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_BTNINDEX));
                    leftBts.add(bt);
                }
                sopLeft.setBts(leftBts);
                bean.setSopLeft(sopLeft);
            }
            if (json.has(LISTVIEW_PARAMS_JSON_KEY_RIGHTSWIPEOPTIONITEM)) {
                SwipeOption sopRight = new DataBean().new SwipeOption();
                JSONObject rightSwipOption = json.getJSONObject(LISTVIEW_PARAMS_JSON_KEY_RIGHTSWIPEOPTIONITEM);
                if (rightSwipOption.has(LISTVIEW_PARAMS_JSON_KEY_SWIPEOPTIONITEM_BACKGROUNDCOLOR)) {
                    sopRight.setBackgroundColor(rightSwipOption.getString(LISTVIEW_PARAMS_JSON_KEY_SWIPEOPTIONITEM_BACKGROUNDCOLOR));
                }
                JSONArray rightBtn = rightSwipOption.getJSONArray(LISTVIEW_PARAMS_JSON_KEY_SWIPEOPTIONITEM_OPTIONBTN);
                List<ButtonItem> rightBts = new ArrayList<ButtonItem>();
                for (int i = 0; i < rightBtn.length(); i++) {
                    JSONObject item = rightBtn.getJSONObject(i);
                    ButtonItem bt = new DataBean().new SwipeOption().new ButtonItem();
                    bt.setText(item.getString(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_TEXT));
                    if (item.has(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_TEXTCOLOR)) {
                        bt.setTextColor(item.getString(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_TEXTCOLOR));
                    }
                    if (item.has(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_TEXTSIZE)) {
                        bt.setTextSize(item.getString(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_TEXTSIZE));
                    }
                    if (item.has(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_BGCOLOR)) {
                        bt.setBgColor(item.getString(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_BGCOLOR));
                    }
                    bt.setBtIndex(item.getString(LISTVIEW_PARAMS_JSON_KEY_OPTIONBTN_BTNINDEX));
                    rightBts.add(bt);
                }
                sopRight.setBts(rightBts);
                bean.setSopRight(sopRight);
            }

            JSONArray dataItems = json.getJSONArray(LISTVIEW_PARAMS_JSON_KEY_LISTITEMS);
            List<DateItem> datas = parseDataItems(dataItems);
            bean.setDates(datas);
        } catch (Exception e) {
            e.printStackTrace();
            bean = null;
        }
        return bean;
    }

    public static DateItem parseDataItem(JSONObject item) {
        DateItem data = new DataBean().new DateItem();
        try {
            data.setTitle(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_TITLE));
            if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_TITLECOLOR)) {
                data.setTitleColor(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_TITLECOLOR));
            }
            if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_TITLESIZE)) {
                data.setTitleSize(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_TITLESIZE));
            }
            data.setSubtitle(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SUBTITLE));
            if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SUBTITLECOLOR)) {
                data.setSubtitleColor(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SUBTITLECOLOR));
            }
            if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SUBTITLESIZE)) {
                data.setSubtitleSize(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SUBTITLESIZE));
            }
            if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_HEIGHT)) {
                data.setHeight(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_HEIGHT));
            }
            data.setImage(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_IMAGE));
            if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_BACKGROUNDCOLOR)) {
                data.setBackgroundColor(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_BACKGROUNDCOLOR));
            }
            if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SELECTEDBACKGROUNDCOLOR)) {
                data.setSelectedBackgroundColor(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SELECTEDBACKGROUNDCOLOR));
            }
            if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_RIGHTBTNIMG)) {
                data.setRightBtnImg(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_RIGHTBTNIMG));
            }
            if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_PLACEHOLDERIMG)) {
                data.setPlaceholderImg(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_PLACEHOLDERIMG));
            }
        } catch (Exception e) {
            e.printStackTrace();
            data = null;
        }
        return data;
    }

    public static List<DateItem> parseDataItems(JSONArray dataItems) {
        List<DateItem> datas = new ArrayList<DateItem>();
        try {
            for (int i = 0; i < dataItems.length(); i++) {
                JSONObject item = dataItems.getJSONObject(i);
                DateItem data = new DataBean().new DateItem();
                data.setTitle(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_TITLE));
                if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_TITLECOLOR)) {
                    data.setTitleColor(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_TITLECOLOR));
                }
                if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_TITLESIZE)) {
                    data.setTitleSize(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_TITLESIZE));
                }
                data.setSubtitle(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SUBTITLE));
                if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SUBTITLECOLOR)) {
                    data.setSubtitleColor(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SUBTITLECOLOR));
                }
                if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SUBTITLESIZE)) {
                    data.setSubtitleSize(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SUBTITLESIZE));
                }
                if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_HEIGHT)) {
                    data.setHeight(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_HEIGHT));
                }
                data.setImage(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_IMAGE));
                if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_BACKGROUNDCOLOR)) {
                    data.setBackgroundColor(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_BACKGROUNDCOLOR));
                }
                if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SELECTEDBACKGROUNDCOLOR)) {
                    data.setSelectedBackgroundColor(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_SELECTEDBACKGROUNDCOLOR));
                }
                if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_RIGHTBTNIMG)) {
                    data.setRightBtnImg(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_RIGHTBTNIMG));
                }
                if (item.has(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_PLACEHOLDERIMG)) {
                    data.setPlaceholderImg(item.getString(LISTVIEW_PARAMS_JSON_KEY_DATAITEMS_PLACEHOLDERIMG));
                }
                datas.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static RefreshBean parseRefreshData(String string) {
        RefreshBean data = null;
        try {
            data = new RefreshBean();
            JSONObject obj = new JSONObject(string);
            if (obj.has(LISTVIEW_PARAMS_JSON_KEY_PULLREFRESHHEADERSTYLE)) {
                JSONObject json = obj.getJSONObject(LISTVIEW_PARAMS_JSON_KEY_PULLREFRESHHEADERSTYLE);
                data.setIsShowUpdataDate(json.getInt(LISTVIEW_PARAMS_JSON_KEY_REFRESH_ISSHOWUPDATEDATE));
                data.setPullRefreshLoadingText(json.getString(LISTVIEW_PARAMS_JSON_KEY_REFRESH_PULLREFRESHLOADINGTEXT));
                data.setPullRefreshNormalText(json.getString(LISTVIEW_PARAMS_JSON_KEY_REFRESH_PULLREFRESHNORMALTEXT));
                data.setPullRefreshPullingText(json.getString(LISTVIEW_PARAMS_JSON_KEY_REFRESH_PULLREFRESHPULLINGTEXT));
                if (json.has(LISTVIEW_PARAMS_JSON_KEY_REFRESH_ARROWIMAGE)) {
                    data.setArrowImage(json.getString(LISTVIEW_PARAMS_JSON_KEY_REFRESH_ARROWIMAGE));
                }
                if (json.has(LISTVIEW_PARAMS_JSON_KEY_REFRESH_BACKGROUNDCOLOR)) {
                    data.setBackGroundColor(json.getString(LISTVIEW_PARAMS_JSON_KEY_REFRESH_BACKGROUNDCOLOR));
                }
                if (json.has(LISTVIEW_PARAMS_JSON_KEY_REFRESH_TEXTCOLOR)) {
                    data.setTextColor(json.getString(LISTVIEW_PARAMS_JSON_KEY_REFRESH_TEXTCOLOR));
                }
                if (json.has(LISTVIEW_PARAMS_JSON_KEY_REFRESH_TEXTFONTSIZE)) {
                    data.setTextFontSize(json.getString(LISTVIEW_PARAMS_JSON_KEY_REFRESH_TEXTFONTSIZE));
                }
            }
            if (obj.has(LISTVIEW_PARAMS_JSON_KEY_PULLREFRESHFOOTERSTYLE)) {
                JSONObject json = obj.getJSONObject(LISTVIEW_PARAMS_JSON_KEY_PULLREFRESHFOOTERSTYLE);
                data.setIsShowUpdataDate(json.getInt(LISTVIEW_PARAMS_JSON_KEY_REFRESH_ISSHOWUPDATEDATE));
                data.setPullRefreshLoadingText(json.getString(LISTVIEW_PARAMS_JSON_KEY_REFRESH_PULLREFRESHLOADINGTEXT));
                data.setPullRefreshNormalText(json.getString(LISTVIEW_PARAMS_JSON_KEY_REFRESH_PULLREFRESHNORMALTEXT));
                data.setPullRefreshPullingText(json.getString(LISTVIEW_PARAMS_JSON_KEY_REFRESH_PULLREFRESHPULLINGTEXT));
                if (json.has(LISTVIEW_PARAMS_JSON_KEY_REFRESH_ARROWIMAGE)) {
                    data.setArrowImage(json.getString(LISTVIEW_PARAMS_JSON_KEY_REFRESH_ARROWIMAGE));
                }
                if (json.has(LISTVIEW_PARAMS_JSON_KEY_REFRESH_BACKGROUNDCOLOR)) {
                    data.setBackGroundColor(json.getString(LISTVIEW_PARAMS_JSON_KEY_REFRESH_BACKGROUNDCOLOR));
                }
                if (json.has(LISTVIEW_PARAMS_JSON_KEY_REFRESH_TEXTCOLOR)) {
                    data.setTextColor(json.getString(LISTVIEW_PARAMS_JSON_KEY_REFRESH_TEXTCOLOR));
                }
                if (json.has(LISTVIEW_PARAMS_JSON_KEY_REFRESH_TEXTFONTSIZE)) {
                    data.setTextFontSize(json.getString(LISTVIEW_PARAMS_JSON_KEY_REFRESH_TEXTFONTSIZE));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            data = null;
        }
        return data;
    }

    public static Bitmap getImage(Context ctx, String imgUrl) {
        if (imgUrl == null || imgUrl.length() == 0) {
            return null;
        }
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            if (URLUtil.isNetworkUrl(imgUrl)) {
                bitmap = ACEImageLoader.getInstance().getBitmapSync(imgUrl);
            } else {
                if (imgUrl.startsWith(BUtility.F_Widget_RES_SCHEMA)) {
                    is = BUtility.getInputStreamByResPath(ctx, imgUrl);
                    bitmap = BitmapFactory.decodeStream(is);
                } else if (imgUrl.startsWith(BUtility.F_FILE_SCHEMA)) {
                    imgUrl = imgUrl.replace(BUtility.F_FILE_SCHEMA, "");
                    bitmap = BitmapFactory.decodeFile(imgUrl);
                } else if (imgUrl.startsWith(BUtility.F_Widget_RES_path)) {
                    try {
                        is = ctx.getAssets().open(imgUrl);
                        if (is != null) {
                            bitmap = BitmapFactory.decodeStream(is);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    bitmap = BitmapFactory.decodeFile(imgUrl);
                }
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    public static byte[] transStreamToBytes(InputStream is, int buffSize) {
        if (is == null) {
            return null;
        }
        if (buffSize <= 0) {
            throw new IllegalArgumentException(
                    "buffSize can not less than zero.....");
        }
        byte[] data = null;
        byte[] buffer = new byte[buffSize];
        int actualSize = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while ((actualSize = is.read(buffer)) != -1) {
                baos.write(buffer, 0, actualSize);
            }
            data = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static float calculateButtonWidth(int count) {
        float buttonWidth = buttonWidthDefalut;
        if (buttonWidth * count > buttonWidthMax) {
            float buffer = (buttonWidth * count) - buttonWidthMax;
            buttonWidth -= (buffer / count);
        }
        return buttonWidth;
    }

    public void saxParser(Context context) {
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
