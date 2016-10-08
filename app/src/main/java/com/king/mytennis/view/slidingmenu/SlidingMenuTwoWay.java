package com.king.mytennis.view.slidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.king.mytennis.service.ScreenUtils;
import com.king.mytennis.view.R;
import com.nineoldandroids.view.ViewHelper;

public class SlidingMenuTwoWay extends SlidingMenuAbstract
{
	/**
	 * 屏幕宽度
	 */
	private int mScreenWidth;
	/**
	 * dp
	 */
	private int mMenuRightPadding;
	private int mMenuLefttPadding;
	/**
	 * 菜单的宽度
	 */
	private int mMenuWidth;
	private int mHalfMenuWidth;
	private int mRightMenuWidth;
	private int mRightHalfMenuWidth;

	private boolean isOpen;

	private boolean once;

	private ViewGroup mMenu;
	private ViewGroup mContent;
	private ViewGroup mMenuRight;
	
	private boolean enableScroll = true;

	public interface OnSlideChagedListener {
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
		public void onSlideChange(int direction);
	}
	
	private OnSlideChagedListener onSlideChagedListener;
	private int currentDirection;
	
	public SlidingMenuTwoWay(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);

	}

	public SlidingMenuTwoWay(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		mScreenWidth = ScreenUtils.getScreenWidth(context);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenuLeft, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++)
		{
			int attr = a.getIndex(i);
			switch (attr)
			{
			case R.styleable.SlidingMenuLeft_rightPadding:
				// 默认50
				mMenuRightPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));
				break;
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

	public SlidingMenuTwoWay(Context context)
	{
		this(context, null, 0);
	}

	public void addOnSlideChangedListener(OnSlideChagedListener listener) {
		onSlideChagedListener = listener;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		/**
		 * 显示的设置一个宽度
		 */
		if (!once)
		{
			LinearLayout wrapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) wrapper.getChildAt(0);
			mContent = (ViewGroup) wrapper.getChildAt(1);
			mMenuRight = (ViewGroup) wrapper.getChildAt(2);

			mMenuWidth = mScreenWidth - mMenuRightPadding;
			mRightMenuWidth = mScreenWidth - mMenuLefttPadding;
			mHalfMenuWidth = mMenuWidth / 2;
			mRightHalfMenuWidth = mRightMenuWidth / 2;
			mMenu.getLayoutParams().width = mMenuWidth;
			mContent.getLayoutParams().width = mScreenWidth;
			mMenuRight.getLayoutParams().width = mRightMenuWidth;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		super.onLayout(changed, l, t, r, b);
		if (changed)
		{
			// 将菜单隐�?
			this.scrollTo(mMenuWidth, 0);
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
			// Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
			case MotionEvent.ACTION_UP:
				int scrollX = getScrollX();
				if (scrollX > mRightHalfMenuWidth + mMenuWidth) {//show right menu
					this.smoothScrollTo(mMenuWidth + mRightMenuWidth, 0);
					isOpen = true;
				}
				else if (scrollX > mHalfMenuWidth) {//show content
					this.smoothScrollTo(mMenuWidth, 0);
					isOpen = false;
				} 
				else {//show left menu
					this.smoothScrollTo(0, 0);
					isOpen = true;
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
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}

	/**
	 * 关闭菜单
	 */
	public void closeMenu()
	{
		if (isOpen)
		{
			this.smoothScrollTo(mMenuWidth, 0);
			isOpen = false;
		}
	}

	/**
	 * 切换菜单状态
	 */
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
		if (l > mMenuWidth) {
			if (OnSlideChagedListener.RIGHT != currentDirection) {
				onSlideChagedListener.onSlideChange(OnSlideChagedListener.RIGHT);
			}
			currentDirection = OnSlideChagedListener.RIGHT;
			l = l - mMenuWidth;
			float scale = (mRightMenuWidth - l) * 1.0f / mRightMenuWidth;
			float contentScale = 0.8f + scale * 0.2f;
			float menuScale = 1 - 0.3f * scale;
			//ViewHelper.setScaleX(mContent, contentScale);//x方向的收缩会增大与menu的空白边距，但是不收缩的话效果又不太好
			ViewHelper.setPivotX(mContent, 0);
			ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);//先设置好轴心，收缩就不会出现轴心为0的情况
			ViewHelper.setScaleY(mContent, contentScale);
			
			ViewHelper.setScaleX(mMenuRight, menuScale);
			ViewHelper.setScaleY(mMenuRight, menuScale);
			ViewHelper.setTranslationX(mMenuRight, mRightMenuWidth * scale * 0.7f);
			ViewHelper.setAlpha(mMenuRight, 0.6f + 0.4f * (1 - scale));
		}
		else {
			if (OnSlideChagedListener.LEFT != currentDirection) {
				onSlideChagedListener.onSlideChange(OnSlideChagedListener.LEFT);
			}
			currentDirection = OnSlideChagedListener.LEFT;
			float scale = l * 1.0f / mMenuWidth;
			float leftScale = 1 - 0.3f * scale;
			float rightScale = 0.8f + scale * 0.2f;
			
			ViewHelper.setScaleX(mMenu, leftScale);
			ViewHelper.setScaleY(mMenu, leftScale);
			ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
			ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.7f);

			ViewHelper.setPivotX(mContent, 0);
			ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
			ViewHelper.setScaleX(mContent, rightScale);
			ViewHelper.setScaleY(mContent, rightScale);
		}

	}

}
