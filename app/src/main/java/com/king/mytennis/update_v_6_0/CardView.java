package com.king.mytennis.update_v_6_0;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public abstract class CardView extends RelativeLayout {

	public CardView(Context context) {
		super(context);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
	}

	public abstract void initData();
	public abstract void setCardData(List<HashMap<String, String>> yearCard);

	/**
	 * 在某个CardView上编辑颜色后，由于CardView都是预加载的，所以颜色改变后，
	 * 需要通知每个CardView颜色改变
	 */
	public abstract void resetColor();

}
