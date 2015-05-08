package org.zywx.wbpalmstar.plugin.uexlistview;

import java.util.Date;

import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BaseListView extends SwipeListView implements OnScrollListener {

	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;

	private final static int RATIO = 3;
	private LayoutInflater inflater;

	private LinearLayout headerView;
	private TextView lvHeaderTipsTv;
	private TextView lvHeaderLastUpdatedTv;
	private ImageView lvHeaderArrowIv;
	private ProgressBar lvHeaderProgressBar;

	private LinearLayout footerView;
	private TextView lvFooterTipsTv;
	private TextView lvFooterLastUpdatedTv;
	private ImageView lvFooterArrowIv;
	private ProgressBar lvFooterProgressBar;

	private int headerContentHeight;
	private int footerContentHeight;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	private int startY;
	private int state;
	private boolean isBack;

	private boolean isRecored;

	private OnRefreshListener onRefreshListener = null;
	private OnRefreshListener onHeaderRefreshListener = null;
	private OnRefreshListener onFooterRefreshListener = null;
	

	private boolean isHeaderRefreshable;
	private boolean isFooterRefreshable;
	
	private Mode mode;
	
	private static RefreshBean headerViewData, footerViewData;
	
	
	public BaseListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public BaseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BaseListView(Context context, int swipeBackView, int swipeFrontView) {
		super(context, swipeBackView, swipeFrontView);
		init(context);
	}

	public boolean isHeaderRefreshable() {
		return isHeaderRefreshable;
	}

	public boolean isFooterRefreshable() {
		return isFooterRefreshable;
	}

	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	public void setOnHeaderRefreshListener(OnRefreshListener onHeaderRefreshListener) {
		this.onHeaderRefreshListener = onHeaderRefreshListener;
	}

	public void setOnFooterRefreshListener(OnRefreshListener onFooterRefreshListener) {
		this.onFooterRefreshListener = onFooterRefreshListener;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public static void setHeaderViewData(RefreshBean headerViewData) {
		BaseListView.headerViewData = headerViewData;
	}

	public static void setFooterViewData(RefreshBean footerViewData) {
		BaseListView.footerViewData = footerViewData;
	}

	private void init(final Context context) {
		inflater = LayoutInflater.from(context);
		headerView = (LinearLayout) inflater.inflate(EUExUtil.getResLayoutID("plugin_lv_header"), null);
		footerView = (LinearLayout) inflater.inflate(EUExUtil.getResLayoutID("plugin_lv_footer"), null);
		
		lvHeaderArrowIv = (ImageView) headerView.findViewById(EUExUtil.getResIdID("lvHeaderArrowIv"));
		lvFooterArrowIv = (ImageView) footerView.findViewById(EUExUtil.getResIdID("lvFooterArrowIv"));
		
		lvHeaderTipsTv = (TextView) headerView.findViewById(EUExUtil.getResIdID("lvHeaderTipsTv"));
		lvFooterTipsTv = (TextView) footerView.findViewById(EUExUtil.getResIdID("lvFooterTipsTv"));
		
		lvHeaderLastUpdatedTv = (TextView) headerView.findViewById(EUExUtil.getResIdID("lvHeaderLastUpdatedTv"));
		lvFooterLastUpdatedTv = (TextView) footerView.findViewById(EUExUtil.getResIdID("lvFooterLastUpdatedTv"));
		
		if(headerViewData != null) {
			headerView.setBackgroundColor(BUtility.parseColor(headerViewData.getBackGroundColor()));
			lvHeaderArrowIv.setImageBitmap(EUExListViewUtils.getImage(context, headerViewData.getArrowImage()));
			lvHeaderTipsTv.setTextSize(Integer.parseInt(headerViewData.getTextFontSize()));
			lvHeaderTipsTv.setTextColor(BUtility.parseColor(headerViewData.getTextColor()));
			lvHeaderLastUpdatedTv.setTextColor(BUtility.parseColor(headerViewData.getTextColor()));
		}
		if(footerViewData != null) {
			footerView.setBackgroundColor(BUtility.parseColor(footerViewData.getBackGroundColor()));
			lvFooterArrowIv.setImageBitmap(EUExListViewUtils.getImage(context, footerViewData.getArrowImage()));
			lvFooterTipsTv.setTextSize(Integer.parseInt(footerViewData.getTextFontSize()));
			lvFooterTipsTv.setTextColor(BUtility.parseColor(footerViewData.getTextColor()));
			lvFooterLastUpdatedTv.setTextColor(BUtility.parseColor(footerViewData.getTextColor()));
		}
		

		lvHeaderArrowIv.setMinimumWidth(70);
		lvHeaderArrowIv.setMinimumHeight(50);
		lvFooterArrowIv.setMinimumWidth(70);
		lvFooterArrowIv.setMinimumHeight(50);

		lvHeaderProgressBar = (ProgressBar) headerView.findViewById(EUExUtil.getResIdID("lvHeaderProgressBar"));
		lvFooterProgressBar = (ProgressBar) footerView.findViewById(EUExUtil.getResIdID("lvFooterProgressBar"));

		measureView(headerView);
		measureView(footerView);

		headerContentHeight = headerView.getMeasuredHeight();
		footerContentHeight = footerView.getMeasuredHeight();

		headerView.setPadding(0, -1 * headerContentHeight, 0, 0);
		footerView.setPadding(0, 0, 0, -1 * footerContentHeight);

		headerView.invalidate();
		footerView.invalidate();

		addHeaderView(headerView, null, false);
		addFooterView(footerView, null, false);

		setOnScrollListener(this);
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isHeaderRefreshable = false;
		isFooterRefreshable = false;
		
		mode = Mode.DISABLE;
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams params = child.getLayoutParams();
		if (params == null) {
			params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0,params.width);
		int lpHeight = params.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem == 0 && (mode == Mode.HEADER || mode == Mode.BOTH) && onHeaderRefreshListener != null) {
			isHeaderRefreshable = true;
		}
		else if((view.getLastVisiblePosition() == totalItemCount - 1) && (mode == Mode.FOOTER || mode == Mode.BOTH) && onFooterRefreshListener != null) {
			isFooterRefreshable = true;
		}
		else {
			isHeaderRefreshable = false;
			isFooterRefreshable = false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mode != Mode.DISABLE) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!isRecored) {
					isRecored = true;
					startY = (int) ev.getY();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING && state != LOADING) {
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeViewByState();
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeViewByState();
						onLvRefresh();
					}
				}
				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				closeOpenedItems();
				int tempY = (int) ev.getY();
				if (!isRecored) {
					isRecored = true;
					startY = tempY;
				}
				if(isHeaderRefreshable) {
					if (state != REFRESHING && isRecored && state != LOADING) {
						if (state == RELEASE_To_REFRESH) {
							setSelection(0); 
							if (((tempY - startY) / RATIO < headerContentHeight) && (tempY - startY) > 0) { 
								state = PULL_To_REFRESH; 
								changeViewByState(); 
							} else if ((tempY - startY) <= 0) {
								state = DONE;
								changeViewByState();
							}
						}
						if (state == PULL_To_REFRESH) {
							
							setSelection(0); 
							if((tempY - startY) / RATIO >= headerContentHeight) {
								state = RELEASE_To_REFRESH;
								isBack = true;
								changeViewByState(); 
							} else if ((tempY - startY) <= 0) {
								state = DONE; 
								changeViewByState(); 
							}
						}
						if (state == DONE) {
							
							if ((tempY - startY) > 0) { 
								state = PULL_To_REFRESH;
								changeViewByState(); 
							}
						}
						if (state == PULL_To_REFRESH) {
							headerView.setPadding(0, -1 * headerContentHeight + (tempY - startY) / RATIO, 0, 0);
						}
						if (state == RELEASE_To_REFRESH) {
							headerView.setPadding(0, (tempY - startY) / RATIO - headerContentHeight, 0, 0);
						}
					}
				}
				if(isFooterRefreshable) {
					if (state != REFRESHING && isRecored && state != LOADING) {
						if (state == RELEASE_To_REFRESH) {
							setSelection(getCount() - 1);
							if (((startY - tempY) / RATIO < footerContentHeight && (startY - tempY) > 0)) {
								state = PULL_To_REFRESH;
								changeViewByState();
							} else if ((tempY - startY) >= 0) {
								state = DONE;
								changeViewByState();
							}
						}
						if (state == PULL_To_REFRESH) {
							setSelection(getCount() - 1);
							if ((startY - tempY) / RATIO >= footerContentHeight) {
								state = RELEASE_To_REFRESH;
								isBack = true;
								changeViewByState();
							} else if ((tempY - startY) >= 0) {
								state = DONE;
								changeViewByState();
							}
						}
						if (state == DONE) {
							if ((startY - tempY) > 0) {
							state = PULL_To_REFRESH;
							changeViewByState();
						}
						}
						if (state == PULL_To_REFRESH) {
							footerView.setPadding(0, 0, 0, -1 * footerContentHeight + (startY - tempY) / RATIO);
						}
						if (state == RELEASE_To_REFRESH) {
							footerView.setPadding(0, 0, 0, (startY - tempY) / RATIO - footerContentHeight);
						}
					}
				}
				break;
			}
		}
		return super.onTouchEvent(ev);
	}

	private void changeViewByState() {
		if(isHeaderRefreshable) {
			switch (state) {
			case RELEASE_To_REFRESH:
				lvHeaderArrowIv.setVisibility(View.VISIBLE);
				lvHeaderProgressBar.setVisibility(View.GONE);
				lvHeaderTipsTv.setVisibility(View.VISIBLE);
				lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
				lvHeaderArrowIv.clearAnimation();
				lvHeaderArrowIv.startAnimation(animation);
				if(headerViewData != null) {
					lvHeaderTipsTv.setText(headerViewData.getPullRefreshPullingText());
				}else {
					lvHeaderTipsTv.setText("松开刷新");
				}
				break;
			case PULL_To_REFRESH:
				
				lvHeaderProgressBar.setVisibility(View.GONE);
				lvHeaderTipsTv.setVisibility(View.VISIBLE);
				lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
				lvHeaderArrowIv.clearAnimation();
				lvHeaderArrowIv.setVisibility(View.VISIBLE);
				if (isBack) {
					isBack = false; 
					lvHeaderArrowIv.clearAnimation();
					lvHeaderArrowIv.startAnimation(reverseAnimation);
					if(headerViewData != null) {
						lvHeaderTipsTv.setText(headerViewData.getPullRefreshNormalText());
					}else {
						lvHeaderTipsTv.setText("下拉刷新");
					}
				} else {
					if(headerViewData != null) {
						lvHeaderTipsTv.setText(headerViewData.getPullRefreshNormalText());
					}else {
						lvHeaderTipsTv.setText("下拉刷新");
					}
				}
				break;
			case REFRESHING:
				headerView.setPadding(0, 0, 0, 0);
				lvHeaderProgressBar.setVisibility(View.VISIBLE);
				lvHeaderArrowIv.clearAnimation();
				lvHeaderArrowIv.setVisibility(View.GONE);
				if(headerViewData != null) {
					lvHeaderTipsTv.setText(headerViewData.getPullRefreshLoadingText());
				}else {
					lvHeaderTipsTv.setText("正在刷新");
				}
				lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
				break;
			case DONE:
				
				headerView.setPadding(0, -1 * headerContentHeight, 0, 0);
				lvHeaderProgressBar.setVisibility(View.GONE);
				lvHeaderArrowIv.clearAnimation();
				if(headerViewData != null) {
					lvHeaderTipsTv.setText(headerViewData.getPullRefreshNormalText());
				}else {
					lvHeaderTipsTv.setText("下拉刷新");
				}
				lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
				break;
			}
		}
		if(isFooterRefreshable) {		
			switch (state) {
			case RELEASE_To_REFRESH:
				lvFooterArrowIv.setVisibility(View.VISIBLE);
				lvFooterProgressBar.setVisibility(View.GONE);
				lvFooterTipsTv.setVisibility(View.VISIBLE);
				lvFooterLastUpdatedTv.setVisibility(View.VISIBLE);
				lvFooterArrowIv.clearAnimation();
				lvFooterArrowIv.startAnimation(animation);
				if(footerViewData != null) {
					lvFooterTipsTv.setText(footerViewData.getPullRefreshPullingText());
				}else {
					lvFooterTipsTv.setText("松开加载");
				}
				break;
			case PULL_To_REFRESH:
				lvFooterProgressBar.setVisibility(View.GONE);
				lvFooterTipsTv.setVisibility(View.VISIBLE);
				lvFooterLastUpdatedTv.setVisibility(View.VISIBLE);
				lvFooterArrowIv.clearAnimation();
				lvFooterArrowIv.setVisibility(View.VISIBLE);
				if (isBack) {
					lvFooterArrowIv.clearAnimation();
					lvFooterArrowIv.startAnimation(reverseAnimation);
					if(footerViewData != null) {
						lvFooterTipsTv.setText(footerViewData.getPullRefreshNormalText());
					}else {
						lvFooterTipsTv.setText("加载更多");
					}
				} else {
					if(footerViewData != null) {
						lvFooterTipsTv.setText(footerViewData.getPullRefreshNormalText());
					}else {
						lvFooterTipsTv.setText("加载更多");
					}
				}
				break;
			case REFRESHING:
				footerView.setPadding(0, 0, 0, 0);
				lvFooterProgressBar.setVisibility(View.VISIBLE);
				lvFooterArrowIv.clearAnimation();
				lvFooterArrowIv.setVisibility(View.GONE);
				if(footerViewData != null) {
					lvFooterTipsTv.setText(footerViewData.getPullRefreshLoadingText());
				}else {
					lvFooterTipsTv.setText("正在加载");
				}
				lvFooterLastUpdatedTv.setVisibility(View.VISIBLE);
				break;
			case DONE:
				footerView.setPadding(0, 0, 0, -1 * footerContentHeight);
				lvFooterProgressBar.setVisibility(View.GONE);
				lvFooterArrowIv.clearAnimation();
				if(footerViewData != null) {
					lvFooterTipsTv.setText(footerViewData.getPullRefreshNormalText());
				}else {
					lvFooterTipsTv.setText("加载更多");
				}
				lvFooterLastUpdatedTv.setVisibility(View.VISIBLE);
				break;
			}
		}

	}

	public void onRefreshComplete() {
		state = DONE;
		if(headerViewData != null && headerViewData.getIsShowUpdataDate() == 0) {
			lvHeaderLastUpdatedTv.setText("");
		}else {
			lvHeaderLastUpdatedTv.setText("最近更新:" + new Date().toLocaleString());
		}
		if(footerViewData != null && footerViewData.getIsShowUpdataDate() == 0) {
			lvFooterLastUpdatedTv.setText("");
		}else {
			lvFooterLastUpdatedTv.setText("最近加载:" + new Date().toLocaleString());
		}
		changeViewByState();
	}

	private void onLvRefresh() {
		if (onRefreshListener != null && mode == Mode.BOTH && (isHeaderRefreshable || isFooterRefreshable)) {
			onRefreshListener.onRefresh();
			Toast.makeText(getContext(), "both", 0).show();
			return;
		}
		
		if(onHeaderRefreshListener != null && (mode == Mode.HEADER || mode == Mode.BOTH) && isHeaderRefreshable) {
			onHeaderRefreshListener.onRefresh();
		}
		
		if(onFooterRefreshListener != null && (mode == Mode.FOOTER || mode == Mode.BOTH) && isFooterRefreshable) {
			onFooterRefreshListener.onRefresh();
		}
	}

	public void setAdapter(EUExAdapter adapter) {
		if(headerViewData != null && headerViewData.getIsShowUpdataDate() == 0) {
			lvHeaderLastUpdatedTv.setText("");
		}else {
			lvHeaderLastUpdatedTv.setText("最近更新:" + new Date().toLocaleString());
		}
		if(footerViewData != null && footerViewData.getIsShowUpdataDate() == 0) {
			lvFooterLastUpdatedTv.setText("");
		}else {
			lvFooterLastUpdatedTv.setText("最近加载:" + new Date().toLocaleString());
		}
		super.setAdapter(adapter);
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}
	
	public enum Mode {
		BOTH, HEADER, FOOTER, DISABLE
	}
}
