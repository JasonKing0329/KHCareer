package com.king.mytennis.view.publicview;

import java.util.ArrayList;
import java.util.List;

import com.king.mytennis.view.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View {
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	//		public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
//				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
//				"W", "X", "Y", "Z", "#" };
	private List<String> indexList;
	private int choose = -1;
	private Paint paint = new Paint();

	private int textSize;
	private int textColor;
	private int textColorFocus;
	private Drawable bkRes;
	private int forceHeight = -1;

	private TextView mTextDialog;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}


	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		indexList = new ArrayList<String>();

		TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs
				, R.styleable.SideBarParams, 0, 0);
		textSize = array.getDimensionPixelSize(R.styleable.SideBarParams_indexTextSize, 30);
		textColor = array.getColor(R.styleable.SideBarParams_indexColor, Color.WHITE);
		textColorFocus = array.getColor(R.styleable.SideBarParams_indexColorFocus, Color.BLACK);
		bkRes = array.getDrawable(R.styleable.SideBarParams_sideBackground);
		array.recycle();

		setBackground(bkRes);
	}

	public void addIndex(String index) {
		indexList.add(index);
	}

	public void clear() {
		indexList.clear();
		forceHeight = -1;
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		if (forceHeight != -1) {
			height = forceHeight;
		}
		int width = getWidth();
		int singleHeight = height / (indexList.size() + 1);//留出一定的padding

		for (int i = 0; i < indexList.size(); i++) {
			paint.setColor(textColor);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(textSize);
			if (i == choose) {
				paint.setColor(textColorFocus);
				paint.setFakeBoldText(true);
			}
			float xPos = width / 2 - paint.measureText(indexList.get(i)) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(indexList.get(i), xPos, yPos, paint);
			paint.reset();
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * indexList.size());

		switch (action) {
			case MotionEvent.ACTION_UP:
				choose = -1;//
				invalidate();
				if (mTextDialog != null) {
					mTextDialog.setVisibility(View.GONE);
				}
				break;

			default:
				if (oldChoose != c) {
					if (c >= 0 && c < indexList.size()) {
						if (listener != null) {
							listener.onTouchingLetterChanged(indexList.get(c));
						}
						if (mTextDialog != null) {
							mTextDialog.setText(indexList.get(c));
							mTextDialog.setVisibility(View.VISIBLE);
						}

						choose = c;
						invalidate();
					}
				}

				break;
		}
		return true;
	}

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

	public void forceSetHeight(int height) {
		forceHeight = height;
	}

}