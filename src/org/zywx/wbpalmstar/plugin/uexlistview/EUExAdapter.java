package org.zywx.wbpalmstar.plugin.uexlistview;

import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexlistview.DataBean.DateItem;
import org.zywx.wbpalmstar.plugin.uexlistview.DataBean.SwipeOption.ButtonItem;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class EUExAdapter extends BaseAdapter {
	
	private EUExListView lv;
	private Context context;
	private DataBean datas;
	private ViewHolder holder;
	private boolean editable = false;
	private ImageLoader loader;
	private DisplayImageOptions options;
	public EUExAdapter(EUExListView base, Context context, DataBean dataBean) {
		super();
		this.lv = base;
		this.datas = dataBean;
		this.context = context;
		
		options = new DisplayImageOptions.Builder()
		.cacheInMemory(true) 
		.cacheOnDisk(true)
		.displayer(new RoundedBitmapDisplayer(20))  
		.build();  
		
		loader = ImageLoader.getInstance();
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public int getCount() {
		return datas.getDates().size();
	}

	@Override
	public Object getItem(int position) {
		return datas.getDates().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final DateItem data = datas.getDates().get(position);
		ButtonItem bt = null;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, EUExUtil.getResLayoutID("plugin_back_row"), null);
			holder.fl = (FrameLayout) convertView.findViewById(EUExUtil.getResIdID("plugin_item"));
			holder.cb = (CheckBox) convertView.findViewById(EUExUtil.getResIdID("plugin_item_cb"));
			holder.iv = (ImageView) convertView.findViewById(EUExUtil.getResIdID("plugin_item_iv"));
			holder.title = (TextView) convertView.findViewById(EUExUtil.getResIdID("plugin_item_title"));
			holder.subTitle = (TextView) convertView.findViewById(EUExUtil.getResIdID("plugin_item_sbTitle"));
			holder.back = (LinearLayout) convertView.findViewById(EUExUtil.getResIdID("plugin_item_back"));
			holder.front = (RelativeLayout) convertView.findViewById(EUExUtil.getResIdID("plugin_item_front"));
			holder.lbt1 = (TextView) convertView.findViewById(EUExUtil.getResIdID("plugin_item_lbt1"));
			holder.lbt2 = (TextView) convertView.findViewById(EUExUtil.getResIdID("plugin_item_lbt2"));
			holder.rbt1 = (TextView) convertView.findViewById(EUExUtil.getResIdID("plugin_item_rbt1"));
			holder.rbt2 = (TextView) convertView.findViewById(EUExUtil.getResIdID("plugin_item_rbt2"));
			holder.sort = (ImageView) convertView.findViewById(EUExUtil.getResIdID("plugin_item_sort"));
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.iv.setTag(data.getImage());
		
		if(data.getPlaceholderImg() != null) {
			holder.iv.setImageBitmap(EUExListViewUtils.getImage(context, data.getPlaceholderImg()));
		}
		if(holder.iv.getTag() != null && holder.iv.getTag().equals(data.getImage())) {
			if(data.getImage().startsWith("http:") || data.getImage().startsWith("https:")) {
				loader.displayImage(data.getImage(), holder.iv, options);
			}else {
				holder.iv.setImageBitmap(EUExListViewUtils.getImage(context, data.getImage()));
			}
		}
		if(data.getBackgroundColor() != null) {
			holder.front.setBackgroundColor(BUtility.parseColor(data.getBackgroundColor()));
		}
		holder.title.setText(data.getTitle());
		if(data.getTitleColor() != null) {
			holder.title.setTextColor(BUtility.parseColor(data.getTitleColor()));
		}
		if(data.getTitleSize() != null) {
//			holder.title.setTextSize(Integer.parseInt(data.getTitleSize()));
			holder.title.setTextSize(EUExListViewUtils.px2sp(context, Float.parseFloat(data.getTitleSize())));
		}
		holder.subTitle.setText(data.getSubtitle());
		if(data.getSubtitleColor() != null) {
			holder.subTitle.setTextColor(BUtility.parseColor(data.getSubtitleColor()));
		}
		if(data.getSubtitleSize() != null) {
//			holder.subTitle.setTextSize(Integer.parseInt(data.getSubtitleSize()));
			holder.subTitle.setTextSize(EUExListViewUtils.px2sp(context, Float.parseFloat(data.getSubtitleSize())));
		}
		
		if(datas.getSopLeft() != null) {
			if(datas.getSopLeft().getBts().size() > 0) {
				bt = datas.getSopLeft().getBts().get(0);
				holder.lbt1.setText(bt.getText());
				if(bt.getTextColor() != null) {
					holder.lbt1.setTextColor(BUtility.parseColor(bt.getTextColor()));
				}
				if(bt.getTextSize() != null) {
//					holder.lbt1.setTextSize(Integer.parseInt(bt.getTextSize()));
					holder.lbt1.setTextSize(EUExListViewUtils.px2sp(context, Float.parseFloat(bt.getTextSize())));
				}
				if(bt.getBgColor() != null) {
					holder.lbt1.setBackgroundColor(BUtility.parseColor(bt.getBgColor()));
				}
				holder.lbt1.setId(Integer.parseInt(bt.getBtIndex()));
				holder.lbt1.setVisibility(View.VISIBLE);
				holder.lbt1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						lv.getLv().closeOpenedItems();
						String js = EUExListViewUtils.SCRIPT_HEADER + "if("+EUExListViewUtils.F_CALLBACK_NAME_ONLEFTOPTIONBUTTONINITEM+"){"+EUExListViewUtils.F_CALLBACK_NAME_ONLEFTOPTIONBUTTONINITEM+"("+position+","+holder.lbt1.getId()+")}";
						lv.onCallback(js);
					}
				});
			}
			if(datas.getSopLeft().getBts().size() > 1) {
				bt = datas.getSopLeft().getBts().get(1);
				holder.lbt2.setText(bt.getText());
				if(bt.getTextColor() != null) {
					holder.lbt2.setTextColor(BUtility.parseColor(bt.getTextColor()));
				}
				if(bt.getTextSize() != null) {
//					holder.lbt2.setTextSize(Integer.parseInt(bt.getTextSize()));
					holder.lbt2.setTextSize(EUExListViewUtils.px2sp(context, Float.parseFloat(bt.getTextSize())));
				}
				if(bt.getBgColor() != null) {
					holder.lbt2.setBackgroundColor(BUtility.parseColor(bt.getBgColor()));
				}
				holder.lbt2.setId(Integer.parseInt(bt.getBtIndex()));
				holder.lbt2.setVisibility(View.VISIBLE);
				holder.lbt2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						lv.getLv().closeOpenedItems();
						String js = EUExListViewUtils.SCRIPT_HEADER + "if("+EUExListViewUtils.F_CALLBACK_NAME_ONLEFTOPTIONBUTTONINITEM+"){"+EUExListViewUtils.F_CALLBACK_NAME_ONLEFTOPTIONBUTTONINITEM+"("+position+","+holder.lbt2.getId()+")}";
						lv.onCallback(js);
					}
				});
			}
			holder.back.setBackgroundColor(BUtility.parseColor(datas.getSopLeft().getBackgroundColor()));
		}
		
		if(datas.getSopRight() != null) {
			if(datas.getSopRight().getBts().size() > 0) {
				bt = datas.getSopRight().getBts().get(0);
				holder.rbt2.setText(bt.getText());
				if(bt.getTextColor() != null) {
					holder.rbt2.setTextColor(BUtility.parseColor(bt.getTextColor()));
				}
				if(bt.getTextSize() != null) {
//					holder.rbt2.setTextSize(Integer.parseInt(bt.getTextSize()));
					holder.rbt2.setTextSize(EUExListViewUtils.px2sp(context, Float.parseFloat(bt.getTextSize())));
				}
				if(bt.getBgColor() != null) {
					holder.rbt2.setBackgroundColor(BUtility.parseColor(bt.getBgColor()));
				}
				holder.rbt2.setId(Integer.parseInt(bt.getBtIndex()));
				holder.rbt2.setVisibility(View.VISIBLE);
				holder.rbt2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						lv.getLv().closeOpenedItems();
						String js = EUExListViewUtils.SCRIPT_HEADER + "if("+EUExListViewUtils.F_CALLBACK_NAME_ONRIGHTOPTIONBUTTONINITEM+"){"+EUExListViewUtils.F_CALLBACK_NAME_ONRIGHTOPTIONBUTTONINITEM+"("+position+","+holder.rbt2.getId()+")}";
						lv.onCallback(js);
					}
				});
			}
			if(datas.getSopRight().getBts().size() > 1) {
				bt = datas.getSopRight().getBts().get(1);
				holder.rbt1.setText(bt.getText());
				if(bt.getTextColor() != null) {
					holder.rbt1.setTextColor(BUtility.parseColor(bt.getTextColor()));
				}
				if(bt.getTextSize() != null) {
//					holder.rbt1.setTextSize(Integer.parseInt(bt.getTextSize()));
					holder.rbt1.setTextSize(EUExListViewUtils.px2sp(context, Float.parseFloat(bt.getTextSize())));
				}
				if(bt.getBgColor() != null) {
					holder.rbt1.setBackgroundColor(BUtility.parseColor(bt.getBgColor()));
				}
				holder.rbt1.setId(Integer.parseInt(bt.getBtIndex()));
				holder.rbt1.setVisibility(View.VISIBLE);
				holder.rbt1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						lv.getLv().closeOpenedItems();
						String js = EUExListViewUtils.SCRIPT_HEADER + "if("+EUExListViewUtils.F_CALLBACK_NAME_ONRIGHTOPTIONBUTTONINITEM+"){"+EUExListViewUtils.F_CALLBACK_NAME_ONRIGHTOPTIONBUTTONINITEM+"("+position+","+holder.rbt1.getId()+")}";
						lv.onCallback(js);
					}
				});
			}
			holder.back.setBackgroundColor(BUtility.parseColor(datas.getSopRight().getBackgroundColor()));
		}
		
		if(editable) {
			holder.cb.setVisibility(View.VISIBLE);
			holder.sort.setVisibility(View.VISIBLE);
			holder.back.setVisibility(View.INVISIBLE);
		}else {
			holder.cb.setVisibility(View.GONE);
			holder.sort.setVisibility(View.GONE);
			holder.back.setVisibility(View.VISIBLE);
		}
		
		holder.sort.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				lv.setEditingEnabled(new String[]{"false"});
			}
		});
		if(data.getHeight() != null) {
//			int inch = Integer.parseInt(data.getHeight());
			int inch = EUExListViewUtils.px2dip(context, Float.parseFloat(data.getHeight()));
			LayoutParams params = (LayoutParams) holder.iv.getLayoutParams();
			params.width = inch;
			params.height = inch;
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			holder.iv.setLayoutParams(params);
			convertView.setMinimumHeight(inch);
		}
		return convertView;
	}
	
}
