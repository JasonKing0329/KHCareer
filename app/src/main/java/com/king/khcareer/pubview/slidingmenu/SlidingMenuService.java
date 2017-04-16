package com.king.khcareer.pubview.slidingmenu;


import android.widget.LinearLayout;

public interface SlidingMenuService {

	public void loadMenu(SlidingMenuAbstract slidingMenu, LinearLayout menuLayout);
	public SlidingMenuCreator loadTwoWayMenu(LinearLayout menuLayout, LinearLayout menuLayoutRight);
}
