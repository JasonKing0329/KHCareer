package com.king.khcareer.match.timeline;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.king.khcareer.match.gallery.UserMatchBean;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.ImageUtil;
import com.king.khcareer.utils.ScreenUtils;
import com.king.khcareer.base.BaseActivity;
import com.king.mytennis.view.R;
import com.king.khcareer.record.detail.DetailGallery;
import com.king.khcareer.common.helper.ObjectCache;

/**
 * @author JingYang
 * @version create time：2016-3-14 下午3:21:54
 */
public class MatchActivity extends BaseActivity implements OnGroupCollapseListener
		, OnChildClickListener{

	public static final String KEY_MATCH_NAME = "key_match_name";
	public static final String KEY_USER_ID = "key_user_id";

	private final String TAG = "MatchActivity";
	private PullToZoomScrollViewEx pullView;
	private ExpandableListView expandableListView;

	private MatchExpanAdapter mAdapter;
	private MatchController mController;

	private UserMatchBean mMatchBean;

	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mController = new MatchController();

		setContentView(R.layout.activity_match);
		pullView = (PullToZoomScrollViewEx) findViewById(R.id.match_pullview);

		View zoomView = LayoutInflater.from(this).inflate(R.layout.layout_match_zoom, null, false);
		View headView = LayoutInflater.from(this).inflate(R.layout.layout_match_head, null, false);
		View contentView = LayoutInflater.from(this).inflate(R.layout.layout_match_content, null, false);

		pullView.setHeaderView(headView);
		pullView.setZoomView(zoomView);
		pullView.setScrollContentView(contentView);

		userId = getIntent().getStringExtra(KEY_USER_ID);
		mController.setUserId(userId);

		// 从swipeCard/gallery中进入，已加载过UserMatchBean
		mMatchBean = ObjectCache.getUserMatchBean();
		// 从其他入口进入，只提供了name
		if (mMatchBean == null) {
			mMatchBean = mController.getUserMatchBean(getIntent().getStringExtra(KEY_MATCH_NAME));
		}

		initHeader(headView, zoomView);

		initContent(contentView);

		//全局背景
		findViewById(R.id.match_content_bk).setBackgroundResource(R.drawable.wall_bk9);
	}

	@Override
	protected void onDestroy() {
		ObjectCache.clear();
		super.onDestroy();
	}

	private void initHeader(View headView, View zoomView) {
		ImageUtil.load("file://" + ImageFactory.getMatchHeadPath(mMatchBean.getNameBean().getName(), mMatchBean.getNameBean().getMatchBean().getCourt())
				, (ImageView) zoomView, R.drawable.default_img);
		((TextView) headView.findViewById(R.id.match_head_court)).setText(mMatchBean.getNameBean().getMatchBean().getCourt());
		((TextView) headView.findViewById(R.id.match_head_level)).setText(mMatchBean.getNameBean().getMatchBean().getLevel());
		((TextView) headView.findViewById(R.id.match_head_name)).setText(mMatchBean.getNameBean().getName());
		((TextView) headView.findViewById(R.id.match_head_place)).setText(
				mMatchBean.getNameBean().getMatchBean().getCountry() + "/" + mMatchBean.getNameBean().getMatchBean().getCity());

		//设置Header所占比例
		int screenWidth = ScreenUtils.getScreenWidth(this);
		LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(
				screenWidth, (int) (9.0F * (screenWidth / 16.0F)));
		pullView.setHeaderLayoutParams(localObject);

	}

	private void initContent(View contentView) {
		mController.loadRecords(mMatchBean.getNameBean().getMatchId());
		mAdapter = new MatchExpanAdapter(this, mController.getExpandList());
		mAdapter.setUserId(userId);
		expandableListView = (ExpandableListView) contentView.findViewById(R.id.match_expanlist);
		expandableListView.setAdapter(mAdapter);
		expandableListView.setOnGroupCollapseListener(this);
		expandableListView.setOnChildClickListener(this);
		for (int i = 0; i < mAdapter.getGroupCount(); i ++) {
			expandableListView.expandGroup(i);
		}

		//content部分的背景，由于要做弧线效果，用layer-list为item增加负数的top padding可以实现
		findViewById(R.id.match_content_parent).setBackgroundResource(R.drawable.pullview_content_bk);

		updateListStatus();
	}

	/**
	 * 1.List如果已达到滑动的条件，content部分会挤压header部分，因此要做scrollToStart的操作
	 * 2.List内容如果太少，背景充满不了富余部分，要调整高度
	 * 3.不知为何，经调试观察，expandableListView的父控件的paddingTop竟然高达72dp,
	 * 有可能是自定义的背景drawable引起的，padding太大视觉效果不好，强制调小
	 */
	private void updateListStatus() {
		expandableListView.post(new Runnable() {

			@Override
			public void run() {
				//调整padding，缩进的padding不会引起height的即时更新，因此要记录下来用于后面使用
				int forceDecrease = 0;
				if (expandableListView.getParent() != null) {
					View parent = (View) expandableListView.getParent();
					Rect padding = new Rect(parent.getPaddingLeft(), parent.getPaddingTop()
							, parent.getPaddingRight(), parent.getPaddingBottom());
					forceDecrease = padding.top / 4 * 3;
					padding.top = padding.top / 4;
					parent.setPadding(padding.left, padding.top, padding.right, padding.bottom);
				}

				//用屏幕最下方到expandableListView底部的距离来填充padding，实现调整expandableListView的高度
				int height = expandableListView.getHeight() - forceDecrease;
				Log.d(TAG, "height = " + height);
				int[] pos = new int[2];
				expandableListView.getLocationOnScreen(pos);
				int bottom = pos[1] + height;
				int paddingBottom = ScreenUtils.getScreenHeight(MatchActivity.this) - bottom;
				Log.d(TAG, "paddingBottom = " + paddingBottom);
				if (paddingBottom > 0) {
					Rect padding = new Rect(expandableListView.getPaddingLeft(), expandableListView.getPaddingTop()
							, expandableListView.getPaddingRight(), expandableListView.getPaddingBottom());
					expandableListView.setPadding(padding.left, padding.top, padding.right
							, paddingBottom);
				}

				//直接调用pullView的scrollTo不管用，因此在里面定义了一个scrollToStart的方法
				//，控制真正代表ScrollView的mRootView的滑动
				pullView.scrollToStart();
			}
		});
	}

	@Override
	public void onGroupCollapse(int groupPosition) {
		//group的收缩会引起expandableListView高度变化，背景也会跟着收缩
		//暂时没有收缩group的需求，因此始终保持为展开状态
		expandableListView.expandGroup(groupPosition);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
								int groupPosition, int childPosition, long id) {

		// 从MatchCommonActivity里跳转过来的，不允许再跳转至其他页面
		if (userId != null) {
			return true;
		}

		Intent intent = new Intent();
		intent.setClass(this, DetailGallery.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("record", mController.getExpandList().get(groupPosition).get(childPosition));
		intent.putExtras(bundle);
		startActivity(intent);
		return true;
	}

}
