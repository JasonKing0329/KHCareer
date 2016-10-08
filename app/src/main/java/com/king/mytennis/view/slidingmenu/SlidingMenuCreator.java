package com.king.mytennis.view.slidingmenu;

import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.service.Application;
import com.king.mytennis.service.DisplayHelper;
import com.king.mytennis.view.R;
import com.king.mytennis.view.settings.SettingProperty;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SlidingMenuCreator {

	private Context mContext;
	private OnClickListener onClickListener;
	private boolean isTabletModel;
	public SlidingMenuCreator(Context context, OnClickListener listener) {
		mContext = context;
		onClickListener = listener;
		isTabletModel = DisplayHelper.isTabModel(context);
	}
	
	public void loadMenu(int[] menuRes, ViewGroup group, int leftOrRight) {
		if (leftOrRight == SettingProperty.SLIDINGMENU_LEFT) {
			if (Application.isLollipop()) {
				group.setBackgroundResource(R.drawable.shape_slidingmenu_bk_l);
			}
			else {
				group.setBackgroundResource(R.drawable.shape_slidingmenu_bk);
			}
		}
		else {
			if (Application.isLollipop()) {
				group.setBackgroundResource(R.drawable.shape_slidingmenu_bk_right_l);
			}
			else {
				group.setBackgroundResource(R.drawable.shape_slidingmenu_bk_right);
			}
			if (group instanceof LinearLayout) {
				((LinearLayout) group).setGravity(Gravity.RIGHT);
			}
		}
		
		for (int i = 0; i < menuRes.length; i ++) {
			addMenuItem(menuRes, i, group, leftOrRight);
		}
	}
	private void addMenuItem(int[] menuRes, int index, ViewGroup group, int leftOrRight) {
		TextView view = new TextView(mContext);
		view.setText(menuRes[index]);
		view.setId(menuRes[index]);
		view.setOnClickListener(onClickListener);
		view.setTextColor(mContext.getResources().getColor(R.color.white));
		view.setTextSize(mContext.getResources().getDimensionPixelOffset(R.dimen.slidingmenu_text_size));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		MarginLayoutParams params2 = params;
		if (index == menuRes.length - 1) {
			params2.bottomMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.slidingmenu_margin_top);
		}
		if (index == 0) {
			params2.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.slidingmenu_margin_top);
		}
		else {
			if (isTabletModel) {
				params2.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.slidingmenu_margin_top);
			}
			else {
				if (menuRes.length < 7) {
					params2.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.slidingmenu_margin_top);
				}
				else {
					params2.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.slidingmenu_margin_top_small);
				}
			}
		}
		
		if (leftOrRight == SettingProperty.SLIDINGMENU_RIGHT) {
			view.setBackgroundResource(R.drawable.selector_slidingmenuitem_bk_right);
			view.setGravity(Gravity.RIGHT);
			params2.rightMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.slidingmenu_margin_left);
		}
		else {
			view.setBackgroundResource(R.drawable.selector_slidingmenuitem_bk);
			params2.leftMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.slidingmenu_margin_left);
		}
		group.addView(view, params);
	}

	public Bitmap loadBackground(String key) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		String file = preferences.getString(key, null);
		if (file == null) {
			return null;
		}
		else {
//			int width = ScreenUtils.getScreenWidth(mContext) + 200;
//			int height = ScreenUtils.getScreenHeight(mContext) + 200;
			Bitmap bitmap = new ImageFactory().getBackground(file);
			if (bitmap == null) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.remove(key);
				editor.commit();
				return null;
			}
			else {
				return bitmap;
			}
		}
	}
	
}
