package com.king.mytennis.view;

import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class CustomDialog extends Dialog implements android.view.View.OnClickListener, OnTouchListener {

	private TextView titleView;
	private TextView nullContentView;;
	private LinearLayout customView;
	private LinearLayout customToolbar;

	protected ImageView saveButton, cancelButton, deleteButton;
	protected Context context;
	protected OnCustomDialogActionListener actionListener;
	private Point startPoint, touchPoint;
	private LayoutParams windowParams;

	public CustomDialog(Context context, OnCustomDialogActionListener actionListener) {
		super(context, R.style.DrawsCustomDialog);
		this.context = context;
		this.actionListener = actionListener;
		setContentView(R.layout.dialog_custom);
		titleView = (TextView) findViewById(R.id.dialog_custom_title);
		nullContentView = (TextView) findViewById(R.id.dialog_custom_view_null);
		saveButton = (ImageView) findViewById(R.id.dialog_custom_save);
		cancelButton = (ImageView) findViewById(R.id.dialog_custom_cancle);
		deleteButton = (ImageView) findViewById(R.id.dialog_custom_delete);
		customView = (LinearLayout) findViewById(R.id.dialog_custom_view);
		customToolbar = (LinearLayout) findViewById(R.id.dialog_custom_toolbar);
		saveButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);

		windowParams = getWindow().getAttributes();
		touchPoint = new Point();
		startPoint = new Point();

		View view = getCustomView();
		if (view != null) {
			customView.addView(view);
		}
		else {
			customView.setVisibility(View.GONE);
			nullContentView.setVisibility(View.VISIBLE);
		}
		view = getCustomToolbar();
		if (view != null) {
			customToolbar.addView(view);
		}
	}

	private class Point {
		float x;
		float y;
	}

	@Override
	/**
	 * notice: getRawX/Y是相对屏幕的坐标，getX/Y是相对控件的
	 * 要实现拖动效果，只能用getRawX/Y，用getX/Y会出现拖动不流畅并且抖动的效果
	 * (from internet: getX getY获取的是相对于child 左上角点的 x y 当第一次获取的时候通过layout设置了child一个新的位置 马上 再次获取x y时就会变了 变成了 新的x y
	 * 然后马上layout 然后又获取了新的x y又。。。。所以会看到 一个view不断地在屏幕上闪来闪去)
	 */
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				float x = event.getRawX();//
				float y = event.getRawY();
				startPoint.x = x;
				startPoint.y = y;
				Log.d("CustomDialog", "ACTION_DOWN x=" + x + ", y=" + y);
				break;
			case MotionEvent.ACTION_MOVE:
				x = event.getRawX();
				y = event.getRawY();
				touchPoint.x = x;
				touchPoint.y = y;
				float dx = touchPoint.x - startPoint.x;
				float dy = touchPoint.y - startPoint.y;

				move((int)dx, (int)dy);

				startPoint.x = x;
				startPoint.y = y;
				Log.d("CustomDialog", "ACTION_MOVE x=" + x + ", y=" + y);
				break;
			case MotionEvent.ACTION_UP:
				break;

			default:
				break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				startPoint.x = event.getX();
				startPoint.y = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				touchPoint.x = event.getX();
				touchPoint.y = event.getY();
				float dx = touchPoint.x - startPoint.x;
				float dy = touchPoint.y - startPoint.y;

				move((int)dx, (int)dy);

				startPoint.x = touchPoint.x;
				startPoint.y = touchPoint.y;
				break;
			case MotionEvent.ACTION_UP:
				break;

			default:
				break;
		}
		return false;
	}

	public void hideSaveButton() {
		saveButton.setVisibility(View.GONE);
	}

	public void showDeleteButton() {
		deleteButton.setVisibility(View.VISIBLE);
	}

	public void hideCancelButton() {
		cancelButton.setVisibility(View.GONE);
	}

	protected abstract View getCustomView();

	protected abstract View getCustomToolbar();

	public void setTitle(String text) {
		titleView.setText(text);
	}
	public void setTitle(int resId) {
		titleView.setText(context.getResources().getString(resId));
	}

	/**
	 * 设置dialog的偏移位置
	 * @param x 负数向左，正数向右
	 * @param y 负数向上，正数向下
	 */
	public void setPositionOffset(int x, int y) {

		windowParams = getWindow().getAttributes();
		windowParams.x = x;
		windowParams.y = y;
		getWindow().setAttributes(windowParams);
	}

	private void move(int x, int y) {

		windowParams.x += x;
		windowParams.y += y;
		getWindow().setAttributes(windowParams);//must have
	}

	@Override
	public void onClick(View view) {
		if (view == cancelButton) {
			dismiss();
		}
		else if (view == saveButton) {
			dismiss();
		}
		else if (view == deleteButton) {
			dismiss();
		}
	}

	public interface OnCustomDialogActionListener {
		public static final String DATA_TYPE = "data_type";
		/**
		 *
		 * @param object
		 * @return if false, then don't dismiss dialog
		 */
		public boolean onSave(Object object);
		/**
		 *
		 * @return if false, then don't dismiss dialog
		 */
		public boolean onCancel();
		public void onLoadData(HashMap<String, Object> data);
	}

}
