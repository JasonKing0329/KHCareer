package com.king.mytennis.view_v_7_0.swipecard;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.lib.tool.ui.RippleFactory;
import com.king.mytennis.service.Application;
import com.king.mytennis.service.ImageUtil;
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.R;
import com.king.mytennis.view.publicview.SideBar;
import com.king.mytennis.view.publicview.SideBar.OnTouchingLetterChangedListener;
import com.king.mytennis.view_v_7_0.controller.RecordProvider;
import com.king.mytennis.view_v_7_0.swipecard.adapter.AbstractSwipeAdapter;
import com.king.mytennis.view_v_7_0.swipecard.adapter.MatchSwipeCardAdapter;
import com.king.mytennis.view_v_7_0.swipecard.adapter.PlayerSwipeCardAdapter;
import com.king.mytennis.view_v_7_0.swipecard.controller.SwipeCardController;
import com.king.mytennis.view_v_7_0.swipecard.view.SwipeFlingAdapterView;
import com.king.mytennis.view_v_7_0.swipecard.view.SwipeFlingAdapterView.OnItemClickListener;
import com.king.mytennis.view_v_7_0.swipecard.view.SwipeFlingAdapterView.onFlingListener;

/**
 * @author JingYang
 * @version create time：2016-3-11 上午10:58:52
 *
 */
public class SwipeCardActivity extends BaseActivity implements onFlingListener
		, OnItemClickListener, OnClickListener, OnTouchingLetterChangedListener {

	private final String TAG = "SwipeCardActivity";

	public static final String KEY_INIT_MODE = "key_init_mode";
	public static final int INIT_MATCH = 1;
	public static final int INIT_PLAYER = 2;
	private int initMode;

	private SwipeFlingAdapterView swipeView;
	private TextView cardIndexView;
	private View bottomBar;
	private TextView btnSort;
	private View btnRestore;
	private TextView cardIndexViewOfBottombar;

	private SideBar indexSideBar;
	private TextView indexPopupView;

	private RecordProvider recordProvider;
	private SwipeCardController mController;
	private AbstractSwipeAdapter mAdapter;

	private boolean mSortByName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ImageUtil.initImageLoader(this);

		setContentView(R.layout.activity_swipecard);

		initViewParams();

		initMode = getIntent().getIntExtra(KEY_INIT_MODE, INIT_MATCH);
		mController = new SwipeCardController(this, initMode);
		recordProvider = new RecordProvider(this);

		mController.loadRecordList();

		switch (initMode) {
			case INIT_MATCH:
				initMatchCards();
				break;
			case INIT_PLAYER:
				initPlayerCards();
				break;

			default:
				break;
		}
	}

	private void initViewParams() {
		ImageView backView = (ImageView) findViewById(R.id.view7_actionbar_back);
		backView.setVisibility(View.VISIBLE);
		backView.setOnClickListener(this);
		swipeView = (SwipeFlingAdapterView) findViewById(R.id.swipe_view);
		swipeView.setFlingListener(this);
		swipeView.setOnItemClickListener(this);
		TextView refreshView = (TextView) findViewById(R.id.swipe_refresh);
		Drawable drawable = getResources().getDrawable(R.drawable.swipcard_ic_refresh);
		int size = getResources().getDimensionPixelSize(R.dimen.swipecard_refresh_size);
		drawable.setBounds(0, 0, size, size);
		refreshView.setCompoundDrawables(null, drawable, null, null);
		refreshView.setOnClickListener(this);
		findViewById(R.id.view7_actionbar_menu).setOnClickListener(this);
		cardIndexView = (TextView) findViewById(R.id.swipe_card_index);
		cardIndexView.setOnClickListener(this);
		cardIndexViewOfBottombar = (TextView) findViewById(R.id.swipe_card_index1);
		cardIndexViewOfBottombar.setOnClickListener(this);
		bottomBar = findViewById(R.id.swipe_bottombar);
		btnSort = (TextView) findViewById(R.id.swipe_btn_sort);
		btnSort.setOnClickListener(this);
		btnRestore = findViewById(R.id.swipe_btn_restore);
		btnRestore.setOnClickListener(this);

		indexSideBar = (SideBar) findViewById(R.id.swipe_sidebar);
		indexPopupView = (TextView) findViewById(R.id.swipe_indexview_popup);
		indexSideBar.setOnTouchingLetterChangedListener(this);
		indexSideBar.setTextView(indexPopupView);

		if (Application.isLollipop()) {
			backView.setBackground(RippleFactory.getRippleBackground(Color.TRANSPARENT,
					getResources().getColor(R.color.ripple_material_light)));
			refreshView.setBackground(RippleFactory.getRippleBackground(Color.TRANSPARENT,
					getResources().getColor(R.color.ripple_material_light)));
			findViewById(R.id.view7_actionbar_menu).setBackground(
					RippleFactory.getRippleBackground(Color.TRANSPARENT,
							getResources().getColor(R.color.ripple_material_light)));
			btnSort.setBackground(RippleFactory.getRippleBackground(Color.WHITE,
					getResources().getColor(R.color.ripple_material_light), true));
			btnRestore.setBackground(RippleFactory.getRippleBackground(Color.WHITE,
					getResources().getColor(R.color.ripple_material_light), true));
		}
	}

	private void initPlayerCards() {
		recordProvider.createPlayerSwipeCardData(mController.getRecordList());
		findViewById(R.id.layout_init).setVisibility(View.GONE);

		mAdapter = new PlayerSwipeCardAdapter(this, recordProvider.getPlayerList(mSortByName));
		mAdapter.setIndexSideBar(indexSideBar);
		swipeView.setAdapter(mAdapter);
		updateCardIndex();
	}

	private void initMatchCards() {
		recordProvider.createMatchSwipeCardData(mController.getRecordList());
		findViewById(R.id.layout_init).setVisibility(View.GONE);

		mAdapter = new MatchSwipeCardAdapter(this, recordProvider.getMatchList(mSortByName));
		mAdapter.setIndexSideBar(indexSideBar);
		swipeView.setAdapter(mAdapter);
		updateCardIndex();
	}

	private Drawable getIndexDrawable() {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.OVAL);
		drawable.setColor(mAdapter.getCardIndexColor(0));
		return drawable;
	}

	/**
	 * 更新底部的index显示
	 */
	private void updateCardIndex() {
		cardIndexView.setText("" + mAdapter.getCount());
		cardIndexView.setBackground(getIndexDrawable());
		cardIndexViewOfBottombar.setText("" + mAdapter.getCount());
		cardIndexViewOfBottombar.setBackground(getIndexDrawable());
	}

	@Override
	public void removeFirstObjectInAdapter() {
		mController.onCardRemoved(mAdapter.getItemData(0));
		mAdapter.remove(0);
		if (btnRestore.getVisibility() == View.GONE) {
			btnRestore.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onLeftCardExit(Object dataObject) {
		updateCardIndex();
	}

	@Override
	public void onRightCardExit(Object dataObject) {
		updateCardIndex();
	}

	@Override
	public void onAdapterAboutToEmpty(int itemsInAdapter) {
		Log.d(TAG, "onAdapterAboutToEmpty itemsInAdapter=" + itemsInAdapter);
	}

	@Override
	public void onScroll(float progress, float scrollXProgress) {

	}

	@Override
	public void onItemClicked(MotionEvent event, View v, Object dataObject) {
//		if (v.getTag() instanceof ViewHolder) {
//            int x = (int) event.getRawX();
//            int y = (int) event.getRawY();
//            ViewHolder vh = (ViewHolder) v.getTag();
//            View child = vh.portraitView;
//            Rect outRect = new Rect();
//            child.getGlobalVisibleRect(outRect);
//            if (outRect.contains(x, y)) {
//                AppToast.show(this, "click 大图");
//            } else {
//                outRect.setEmpty();
//                child = vh.collectView;
//                child.getGlobalVisibleRect(outRect);
//                if (outRect.contains(x, y)) {
//                    AppToast.show(this, "click 关注");
//                }
//            }
//        }
		int itemPosition = (Integer) dataObject;
		mAdapter.onItemClicked(itemPosition);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.view7_actionbar_back:
				finish();
				break;
			case R.id.swipe_refresh://重新加载
				mController.clearStack();
				refreshCards();
				//隐藏恢复上张卡片
				btnRestore.setVisibility(View.GONE);
				break;
			case R.id.swipe_btn_restore://恢复上张卡片
				Object bean = mController.getLastCard();
				if (bean != null) {
					mAdapter.addItem(0, bean);
					mAdapter.notifyDataSetChanged();
					updateCardIndex();
					mAdapter.refreshFirstItem(mController.getRestoreCardAnim(this));

					if (mController.isStackEmpty()) {
						btnRestore.setVisibility(View.GONE);
					}
				}
				break;
			case R.id.swipe_btn_sort:
				mSortByName = !mSortByName;
				refreshCards();
				//swipecard的一个bug，在卡片没有全部消除的情况下，重新适配内容并通过notifyDataSetChanged刷新
				//position=0的位置没有触发getView，所以第0个item需要强制刷新一下
				mAdapter.refreshFirstItem(null);

				if (mSortByName) {
					indexSideBar.setVisibility(View.VISIBLE);
					btnSort.setText(getString(R.string.swipecard_sortby_date));
				}
				else {
					indexSideBar.setVisibility(View.GONE);
					btnSort.setText(getString(R.string.swipecard_sortby_name));
				}
				//清空历史栈
				mController.clearStack();
				//隐藏恢复上张卡片
				btnRestore.setVisibility(View.GONE);
				break;
			case R.id.swipe_card_index:
				bottomBar.setVisibility(View.VISIBLE);
				bottomBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.swipecard_bottombar_appear));
				break;
			case R.id.swipe_card_index1:
				bottomBar.setVisibility(View.GONE);
				bottomBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.swipecard_bottombar_disappear));
				break;

			default:
				break;
		}
	}

	private void refreshCards() {
		mAdapter.updateDatas(requestFullDatas());
		swipeView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.swipecard_appear));
		updateCardIndex();
	}

	private Object requestFullDatas() {
		if (initMode == INIT_PLAYER) {
			return recordProvider.getPlayerList(mSortByName);
		}
		else {
			return recordProvider.getMatchList(mSortByName);
		}
	}

	@Override
	public void onTouchingLetterChanged(String s) {
		mAdapter.onIndexLetter(s);
		updateCardIndex();

		//进入字母选择后应该清除历史栈，避免恢复上张卡片
		mController.clearStack();
		btnRestore.setVisibility(View.GONE);
	}

}
