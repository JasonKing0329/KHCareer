package com.king.khcareer.pubview;

import com.king.khcareer.utils.ScreenUtils;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.view.MotionEvent;

public class DragSideBarTrigger {

	private DragSideBar dragSideBar;

	private float offsetX, startX;
	private boolean isDragSide;
	private int screenWidth;

	public DragSideBarTrigger(Context context, DragSideBar dragSideBar) {
		this.dragSideBar = dragSideBar;
		screenWidth = ScreenUtils.getScreenWidth(context);
	}

	public boolean onTriggerTouch(MotionEvent event) {

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startX = event.getRawX();
				if (startX > screenWidth - 50) {
					isDragSide = true;
				}

				break;
			case MotionEvent.ACTION_MOVE:
				if (isDragSide) {
					offsetX = event.getRawX() - dragSideBar.getPaddingLeft();
					if (offsetX < 0) {
						offsetX = 0;
					}
					ViewHelper.setTranslationX(dragSideBar, offsetX);
					dragSideBar.setBackgroundAlpha(offsetX);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (isDragSide) {
					dragSideBarOver();
					isDragSide = false;
					return true;//这里要单独return true否则触发view会触发ACTION_UP
				}
				break;
			case MotionEvent.ACTION_OUTSIDE:
				break;
		}
		return isDragSide;
	}

	private void dragSideBarOver() {
		int width = dragSideBar.getWidth();
		if (offsetX < width - (width - dragSideBar.getPaddingLeft()) / 2) {
			ViewHelper.setTranslationX(dragSideBar, 0);
			dragSideBar.setBackgroundAlpha(0);
		}
		else {
			dragSideBar.dismiss(false);
		}
	}

}
