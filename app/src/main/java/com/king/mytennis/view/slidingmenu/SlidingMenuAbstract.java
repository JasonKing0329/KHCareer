package com.king.mytennis.view.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public abstract class SlidingMenuAbstract extends HorizontalScrollView {

	public static String MENU_BK_KEY;
	
	public SlidingMenuAbstract(Context context) {
		super(context);
	}
	public SlidingMenuAbstract(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public SlidingMenuAbstract(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public abstract void openMenu();
	public abstract void closeMenu();
	public abstract void enableScroll(boolean enable);

}
