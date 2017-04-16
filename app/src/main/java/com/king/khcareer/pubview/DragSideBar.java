package com.king.khcareer.pubview;

import com.king.khcareer.utils.ScreenUtils;
import com.king.mytennis.view.R;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;

public class DragSideBar extends FrameLayout implements AnimationListener {

	public interface DragSideBarListener {
		public void onCloseDragSideBar();
	}

	private final int BK_ALPHA_MAX = 0xaa;

	private float offset;
	private float space;

	private boolean isDrag;

	private DragSideBarListener dragSideBarListener;
	private View shadowView;

	public DragSideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(getResources().getColor(R.color.transparent));
		offset = ScreenUtils.getScreenWidth(getContext()) -
				getResources().getDimensionPixelSize(R.dimen.dragsidebar_padding_left);
		ViewHelper.setTranslationX(this, offset);
	}

	public void setLayoutRes(int resId) {
		View view = LayoutInflater.from(getContext()).inflate(resId, null);
		addView(view);
	}

	public void setDragSideBarListener(DragSideBarListener listener) {
		dragSideBarListener = listener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		boolean handle = false;

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				offset = event.getRawX();
				if (offset < getLeft() + getPaddingLeft()) {
					handle = true;
					isDrag = true;
					space = offset;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (isDrag) {
					offset = event.getRawX();
					ViewHelper.setTranslationX(this, offset - space);
					setBackgroundAlpha(offset);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (isDrag) {
					dragSideBarOver();
				}
				break;
			case MotionEvent.ACTION_OUTSIDE:
				if (isDrag) {
					dragSideBarOver();
				}
				break;

			default:
				break;
		}

		if (handle) {
			return true;
		}
		else {
			return super.onTouchEvent(event);
		}
	}

	private void dragSideBarOver() {
		int width = getWidth();
		if (offset < width / 2) {
			ViewHelper.setTranslationX(this, 0);
			setBackgroundAlpha(0);
		}
		else {
			dismiss();
		}
		isDrag = false;
	}

	public boolean isOpen() {
		return offset == 0;
	}

	public void dismiss(boolean withAnimation) {

		if (withAnimation) {
			TranslateAnimation animation = new TranslateAnimation(0, getWidth(), 0, 0);
			animation.setDuration(1000);
			animation.setAnimationListener(this);
			startAnimation(animation);
		}
		else {
			dismiss();
		}
	}

	private void dismiss() {
		ViewHelper.setTranslationX(this, getWidth());
		setBackgroundAlpha(getWidth());
		if (dragSideBarListener != null) {
			dragSideBarListener.onCloseDragSideBar();
		}
	}

	/**
	 * 打开后以及打开过程中整个屏幕背景透明度变化
	 * @param view
	 */
	public void setShadowView(View view) {
		shadowView = view;
	}

	public void setBackgroundAlpha(float offsetX) {
		offset = offsetX;
		if (shadowView != null) {
			float f = offsetX / getWidth();
			int alpha = (int) (BK_ALPHA_MAX - BK_ALPHA_MAX * f);
			int color = Color.argb(alpha, 0, 0, 0);
			shadowView.setBackgroundColor(color);
			if (alpha != 0 && shadowView.getVisibility() == View.GONE) {
				shadowView.setVisibility(View.VISIBLE);
			}
			else {
				if (alpha == 0 && shadowView.getVisibility() == View.VISIBLE) {
					shadowView.setVisibility(View.GONE);
				}
			}
		}
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		dismiss();
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {

	}

	@Override
	public void onAnimationStart(Animation arg0) {

	}

}
