package com.king.mytennis.view.publicview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PageTagView extends View {

	private final int HEIGHT_ITEM_MAX = 60;
	private final int EDGE_MIN = 100;

	private int tagNumber;
	private int curTag;
	private Paint paint;

	public PageTagView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setTagNumber(int num) {
		tagNumber = num;

		paint = new Paint();
		invalidate();
	}

	public void setFocusTag(int index) {
		curTag = index;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (paint != null && tagNumber > 0) {

			int itemHeight = (getHeight() - EDGE_MIN * 2) / tagNumber;
			if (itemHeight > HEIGHT_ITEM_MAX) {
				itemHeight = HEIGHT_ITEM_MAX;
			}

			float x = getWidth() - 100;
			float y = (getHeight() - tagNumber * itemHeight) / 2;
			paint.setColor(Color.WHITE);
			for (int i = 0; i < tagNumber; i ++) {
				if (i == curTag) {
					paint.setTextSize(50);
					canvas.drawText("○", x, y, paint);
				}
				else {
					paint.setTextSize(30);
					canvas.drawText("●", x + 10, y, paint);
				}
				y += itemHeight;
			}
		}

		super.onDraw(canvas);
	}


}
