package com.king.khcareer.pubview.slidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.king.khcareer.utils.ScreenUtils;
import com.king.mytennis.view.R;
import com.nineoldandroids.view.ViewHelper;

public class SlidingMenuRight extends SlidingMenuAbstract
{
	public static final String BK_KEY = "sliding_menu_bk_right";
	private int mScreenWidth;
	/**
	 * dp
	 */
	private int mMenuLefttPadding;
	private int mMenuWidth;
	private int mHalfMenuWidth;

	private boolean isOpen;

	private boolean once;

	private ViewGroup mMenu;
	private ViewGroup mContent;
	
	private boolean enableScroll = true;

	public SlidingMenuRight(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);

	}

	public SlidingMenuRight(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		MENU_BK_KEY = BK_KEY;
		
		mScreenWidth = ScreenUtils.getScreenWidth(context);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenuLeft, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++)
		{
			int attr = a.getIndex(i);
			switch (attr)
			{
			case R.styleable.SlidingMenuLeft_leftPadding:
				mMenuLefttPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));
				break;
			}
		}
		a.recycle();
	}

	public SlidingMenuRight(Context context)
	{
		this(context, null, 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		if (!once)
		{
			LinearLayout wrapper = (LinearLayout) getChildAt(0);
			mContent = (ViewGroup) wrapper.getChildAt(0);
			mMenu = (ViewGroup) wrapper.getChildAt(1);

			mMenuWidth = mScreenWidth - mMenuLefttPadding;
			mHalfMenuWidth = mMenuWidth / 2;
			mMenu.getLayoutParams().width = mMenuWidth;
			mContent.getLayoutParams().width = mScreenWidth;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		super.onLayout(changed, l, t, r, b);
		if (changed)
		{
			Log.d("SlidingMenu", "onLayout changed");
			//this.scrollTo(mMenuWidth, 0);
			once = true;
		}
	}

	@Override
	public void enableScroll(boolean enable) {
		enableScroll = enable;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (enableScroll) {
			return super.onInterceptTouchEvent(ev);
		}
		else {
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (enableScroll) {
			int action = ev.getAction();
			switch (action)
			{
			case MotionEvent.ACTION_UP:
				int scrollX = getScrollX();
				Log.d("SlidingMenu", "scrollX=" + scrollX);
				if (scrollX > mHalfMenuWidth)
				{
					this.smoothScrollTo(mMenuWidth, 0);
					isOpen = true;
				} else
				{
					this.smoothScrollTo(0, 0);
					isOpen = false;
				}
				return true;
			}
			return super.onTouchEvent(ev);
		}
		else {
			return false;
		}
	}

	public void openMenu()
	{
		if (isOpen)
			return;
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen = true;
	}

	public void closeMenu()
	{
		if (isOpen)
		{
			this.smoothScrollTo(0, 0);
			isOpen = false;
		}
	}

	public void toggle()
	{
		if (isOpen)
		{
			closeMenu();
		} else
		{
			openMenu();
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt)
	{
		super.onScrollChanged(l, t, oldl, oldt);
		
		//sliding from left to right, l is decrease
		//sliding from right to left, l is increase
		float scale = (mMenuWidth - l) * 1.0f / mMenuWidth;
		float contentScale = 0.8f + scale * 0.2f;
		float menuScale = 1 - 0.3f * scale;
		//ViewHelper.setScaleX(mContent, contentScale);//x方向的收缩会增大与menu的空白边距，但是不收缩的话效果又不太好
		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);//先设置好轴心，收缩就不会出现轴心为0的情况
		ViewHelper.setScaleY(mContent, contentScale);
		
		ViewHelper.setScaleX(mMenu, menuScale);
		ViewHelper.setScaleY(mMenu, menuScale);
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.7f);
		ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));

	}

}
