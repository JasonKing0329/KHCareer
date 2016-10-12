package com.king.mytennis.view_v_7_0.view;

import java.util.HashMap;
import java.util.List;

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
import com.king.mytennis.glory.GloryController;
import com.king.mytennis.glory.GloryMatchDialog;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.model.Record;
import com.king.mytennis.service.ImageUtil;
import com.king.mytennis.service.ScreenUtils;
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;
import com.king.mytennis.view_v_7_0.controller.ObjectCache;
import com.king.mytennis.view_v_7_0.controller.PlayerController;
import com.king.mytennis.view_v_7_0.model.PlayerBean;

/**
 * @author JingYang
 * @version create time：2016-3-14 下午3:21:54
 *
 */
public class PlayerActivity extends BaseActivity implements OnGroupCollapseListener
		, OnChildClickListener{

	private final String TAG = "MatchActivity";
	private PullToZoomScrollViewEx pullView;
	private ExpandableListView expandableListView;

	private PlayerExpanAdapter mAdapter;
	private PlayerController mController;

	private PlayerBean mPlayerBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mController = new PlayerController();

		setContentView(R.layout.activity_player);
		pullView = (PullToZoomScrollViewEx) findViewById(R.id.player_pullview);

		View zoomView = LayoutInflater.from(this).inflate(R.layout.layout_match_zoom, null, false);
		View headView = LayoutInflater.from(this).inflate(R.layout.layout_player_head, null, false);
		View contentView = LayoutInflater.from(this).inflate(R.layout.layout_match_content, null, false);

		pullView.setHeaderView(headView);
		pullView.setZoomView(zoomView);
		pullView.setScrollContentView(contentView);

		mPlayerBean = ObjectCache.gePlayerBean();

		initHeader(headView, zoomView);

		initContent(contentView);

		//全局背景
		findViewById(R.id.player_content_bk).setBackgroundResource(R.drawable.wall_bk9);
	}

	private void initHeader(View headView, View zoomView) {
		ImageUtil.load("file://" + ImageFactory.getDetailPlayerPath(mPlayerBean.getName())
				, (ImageView) zoomView, R.drawable.swipecard_default_img);
		((TextView) headView.findViewById(R.id.player_head_h2h)).setText(
				mPlayerBean.getWin() + "胜" + mPlayerBean.getLose() + "负");
//        ((TextView) headView.findViewById(R.id.match_head_level)).setText(mMatchBean.getLevel());
		((TextView) headView.findViewById(R.id.player_head_name)).setText(mPlayerBean.getName());
		((TextView) headView.findViewById(R.id.player_head_place)).setText(
				mPlayerBean.getCountry());

		//设置Header所占比例
		int screenWidth = ScreenUtils.getScreenWidth(this);
		LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(
				screenWidth, (int) (9.0F * (screenWidth / 16.0F)));
		pullView.setHeaderLayoutParams(localObject);

	}

	private void initContent(View contentView) {
		mController.loadRecords(this, mPlayerBean.getName());
		mAdapter = new PlayerExpanAdapter(this, mController.getExpandList());
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
				int paddingBottom = ScreenUtils.getScreenHeight(PlayerActivity.this) - bottom;
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
								final int groupPosition, final int childPosition, long id) {
		GloryMatchDialog dialog = new GloryMatchDialog(this, new CustomDialog.OnCustomDialogActionListener() {

			@Override
			public boolean onSave(Object object) {
				return false;
			}

			@Override
			public void onLoadData(HashMap<String, Object> data) {
				Record record = mController.getExpandList().get(groupPosition).get(childPosition);
				List<Record> list = new GloryController().loadMatchRecord(PlayerActivity.this,
						record.getMatch(), record.getStrDate());
				data.put(CustomDialog.OnCustomDialogActionListener.DATA_TYPE, list);
			}

			@Override
			public boolean onCancel() {
				return false;
			}
		});
		dialog.enableItemLongClick();
		dialog.show();
		return true;
	}

}
