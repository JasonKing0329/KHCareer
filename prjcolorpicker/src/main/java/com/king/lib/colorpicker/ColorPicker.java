package com.king.lib.colorpicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.danielnilsson9.colorpickerview.view.ColorPanelView;
import com.github.danielnilsson9.colorpickerview.view.ColorPickerView;
import com.github.danielnilsson9.colorpickerview.view.ColorPickerView.OnColorChangedListener;

import java.util.List;

/**
 * @author JingYang
 * @version create time：2016-1-13 上午10:00:54
 *
 */
public class ColorPicker extends Dialog implements OnColorChangedListener
		, DialogInterface.OnDismissListener, TextWatcher
		, OnTouchListener, View.OnClickListener {

	/**
	 * action listener
	 * @author JingYang
	 *
	 */
	public interface OnColorPickerListener {
		/**
		 * 实时选择颜色
		 * @param key 要修改颜色部分的标志，可为null，表示只有唯一部分需要修改
		 * @param newColor
		 */
		public void onColorChanged(String key, int newColor);
		/**
		 * 按下确认件后
		 * @param color
		 */
		public void onColorSelected(int color);
		/**
		 * 按下确认件后
		 * @param list 批量编辑的颜色值
		 */
		public void onColorSelected(List<ColorPickerSelectionData> list);
		/**
		 * 由于onColorChanged可能产生了实时修改颜色操作，隐藏如果取消了保存则需要通知还原
		 */
		public void onColorCancleSelect();
		/**
		 * 还原为应用程序默认样式
		 */
		public void onApplyDefaultColors();
	}

	/**
	 * used for touch event
	 * @author JingYang
	 *
	 */
	private class Point {
		float x;
		float y;
	}

	/** action listener **/
	private OnColorPickerListener mListener;
	/** res provider **/
	private ResourceProvider mResourceProvider;

	/** color picker **/
	private ColorPickerView mColorPickerView;
	/** panel to show picked color **/
	private ColorPanelView mColorPanelView;
	/** hex format text of picked color, support edit **/
	private EditText mColorHexEdit;

	/** ok button **/
	private ImageView mOkView;
	/** close button **/
	private ImageView mCloseView;
	/** dialog title **/
	private TextView mTitleView;
	/** divider below top bar **/
	private View mDividerView;
	/** more operation **/
	private View mMoreLayout;
	/** more operation **/
	private ImageView mMoreIconView;
	/** edit ColorPicker style **/
	private TextView mEditSelfView;
	/** dialog title **/
	private TextView mApplyDefaultView;

	/** selection part **/
	private ColorPickerList colorPickerList;

	/** current key of view section to change color **/
	private String mKey;

	/** edit self mode **/
	private boolean isEditSelf;

	/** if colorpicker_ok is clicked, avoid execute onDismiss **/
	private boolean isSaved;

	/** used for change dialog background **/
	private View rootView;

	/*****************for drag/move operation**********************/
	private Point startPoint, touchPoint;
	private LayoutParams windowParams;

	public ColorPicker(Context context, OnColorPickerListener listener) {
		super(context, R.style.ColorPicker);
		mListener = listener;
		setContentView(R.layout.dlg_color_picker);
		mColorHexEdit = (EditText) findViewById(R.id.colorpicker_text_edit);
		mColorPickerView = (ColorPickerView) findViewById(R.id.colorpicker_picker);
		mColorPanelView = (ColorPanelView) findViewById(R.id.colorpicker_panel);
		mEditSelfView = (TextView) findViewById(R.id.colorpicker_editself);
		mApplyDefaultView = (TextView) findViewById(R.id.colorpicker_restore);
		mMoreIconView = (ImageView) findViewById(R.id.colorpicker_more_icon);
		mTitleView = (TextView) findViewById(R.id.colorpicker_title);
		mDividerView = findViewById(R.id.colorpicker_divider);

		mColorPickerView.setOnColorChangedListener(this);
		mColorHexEdit.addTextChangedListener(this);
		mCloseView = (ImageView) findViewById(R.id.colorpicker_close);
		mCloseView.setOnClickListener(this);
		mOkView = (ImageView) findViewById(R.id.colorpicker_ok);
		mOkView.setOnClickListener(this);
		mMoreLayout = findViewById(R.id.colorpicker_layout_more);
		mMoreLayout.setOnClickListener(this);
		mEditSelfView.setOnClickListener(this);
		mApplyDefaultView.setOnClickListener(this);

		colorPickerList = new ColorPickerList(context, this, findViewById(R.id.colorpicker_part_list));

		windowParams = getWindow().getAttributes();
		touchPoint = new Point();
		startPoint = new Point();

		rootView = mColorPickerView.getRootView();
		setOnDismissListener(this);
	}

	/**
	 * register ResourceProvider if you want to modify ColorPicker's color style
	 * @param provider
	 */
	public void setResourceProvider(ResourceProvider provider) {
		this.mResourceProvider = provider;
		colorPickerList.setmResourceProvider(mResourceProvider);
	}

	/**
	 * initialize colors from JResource which support load resource from disk file
	 */
	private void initPickerColors() {
		if (mResourceProvider != null) {
			rootView.setBackgroundColor(ResourceController.getColor(getContext().getResources()
					, ColorPickerRes.COLORPICKER_BACKGROUND
					, mResourceProvider.getColor(ColorPickerRes.COLORPICKER_BACKGROUND)));
			mDividerView.setBackgroundColor(ResourceController.getColor(getContext().getResources()
					, ColorPickerRes.COLORPICKER_DIVIDER
					, mResourceProvider.getColor(ColorPickerRes.COLORPICKER_DIVIDER)));
			mTitleView.setTextColor(ResourceController.getColor(getContext().getResources()
					, ColorPickerRes.COLORPICKER_TITLE
					, mResourceProvider.getColor(ColorPickerRes.COLORPICKER_TITLE)));
			mEditSelfView.setTextColor(ResourceController.getColor(getContext().getResources()
					, ColorPickerRes.COLORPICKER_COLOR_TEXT
					, mResourceProvider.getColor(ColorPickerRes.COLORPICKER_COLOR_TEXT)));
		}
	}

	@Override
	public void show() {
		isSaved = false;
		initPickerColors();
		colorPickerList.onShow();
		super.show();
	}

	/**
	 * Initialize color before show()
	 * @param color
	 */
	public void initColor(int color) {
		mColorPickerView.setColor(color);
		mColorPanelView.setColor(color);
		updateColorText(color);
	}

	@Override
	public void onClick(View v) {
		if (v == mCloseView) {
			dismiss();
		}
		else if (v == mOkView) {
			if (isEditSelf) {
				colorPickerList.saveUpdate();
			}
			else {
				isSaved = true;
				if (!colorPickerList.onPickDone(mListener)) {
					if (mListener != null) {
						mListener.onColorSelected(mColorPickerView.getColor());
					}
				}
				dismiss();
			}
		}
		else if (v == mMoreLayout) {//more operation
			if (mApplyDefaultView.getVisibility() == View.VISIBLE) {
				mEditSelfView.setVisibility(View.GONE);
				mApplyDefaultView.setVisibility(View.GONE);
				mMoreIconView.setImageResource(R.drawable.colorpicker_more_drop);
			}
			else {
				if (!isEditSelf) {
					mEditSelfView.setVisibility(View.VISIBLE);
				}
				mApplyDefaultView.setVisibility(View.VISIBLE);
				mMoreIconView.setImageResource(R.drawable.colorpicker_more_pull);
			}
		}
		else if (v == mEditSelfView) {//edit ColorPicker
			showEditSelfWarning();
		}
		else if (v == mApplyDefaultView) {//apply default style
			showApplyDefaultWarning();
		}
	}

	/**
	 * Warning while click apply default style
	 */
	private void showApplyDefaultWarning() {
		new AlertDialog.Builder(getContext())
				.setTitle(R.string.colorpicker_tip_warning)
				.setMessage(R.string.colorpicker_warning_applydefault)
				.setPositiveButton(R.string.colorpicker_tip_ok,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								if (isEditSelf) {
									if (mResourceProvider != null) {
										mResourceProvider.updateColor(ColorPickerRes.COLORPICKER_BACKGROUND
												, getContext().getResources().getColor(R.color.colorpicker_background));
										mResourceProvider.updateColor(ColorPickerRes.COLORPICKER_TITLE
												, getContext().getResources().getColor(R.color.colorpicker_title));
										mResourceProvider.updateColor(ColorPickerRes.COLORPICKER_DIVIDER
												, getContext().getResources().getColor(R.color.colorpicker_divider));
										mResourceProvider.updateColor(ColorPickerRes.COLORPICKER_LIST_TEXT
												, getContext().getResources().getColor(R.color.colorpicker_list_text));
										mResourceProvider.updateColor(ColorPickerRes.COLORPICKER_COLOR_TEXT
												, getContext().getResources().getColor(R.color.colorpicker_color_text));
										mResourceProvider.updateColor(ColorPickerRes.COLORPICKER_FRAME_BORDER
												, getContext().getResources().getColor(R.color.colorpicker_frame_border));
										mResourceProvider.saveColorUpdate();
									}
								}
								else {
									if (mListener != null) {
										mListener.onApplyDefaultColors();
									}
								}
							}
						})
				.setNegativeButton(R.string.colorpicker_tip_cancel, null)
				.show();
	}

	/**
	 * Warning while click edit ColorPicker
	 * out side call ColorPicker with registered data for its views,
	 * once clicking edit ColorPicker, need give up effect on outside
	 */
	private void showEditSelfWarning() {
		new AlertDialog.Builder(getContext())
				.setTitle(R.string.colorpicker_tip_warning)
				.setMessage(R.string.colorpicker_warning_giveup)
				.setPositiveButton(R.string.colorpicker_tip_ok,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								colorPickerList.loadSelfEditData();
								mEditSelfView.setVisibility(View.GONE);
								isEditSelf = true;
							}
						})
				.setNegativeButton(R.string.colorpicker_tip_cancel, null)
				.show();
	}

	/**
	 * public part of onColorChanged event from ColorPickerView and onTextChanged event from EditText
	 * @param newColor
	 */
	public void notifyColorChanged(int newColor) {
		mColorPanelView.setColor(newColor);
		colorPickerList.updateColor(newColor);

		if (isEditSelf) {
			String key = colorPickerList.getSelectionKey();
			if (key.equals(ColorPickerRes.COLORPICKER_BACKGROUND)) {
				rootView.setBackgroundColor(newColor);
			}
			else if (key.equals(ColorPickerRes.COLORPICKER_TITLE)) {
				mTitleView.setTextColor(newColor);
			}
			else if (key.equals(ColorPickerRes.COLORPICKER_DIVIDER)) {
				mDividerView.setBackgroundColor(newColor);
			}
			else if (key.equals(ColorPickerRes.COLORPICKER_LIST_TEXT)) {
				colorPickerList.updateListTextColor(newColor);
			}
			else if (key.equals(ColorPickerRes.COLORPICKER_COLOR_TEXT)) {
				mEditSelfView.setTextColor(newColor);
			}
			else if (key.equals(ColorPickerRes.COLORPICKER_FRAME_BORDER)) {

			}
		}
		else {
			if (mListener != null) {
				mListener.onColorChanged(mKey, newColor);
			}
		}
	}

	/**
	 * callback of ColorPickerView
	 */
	@Override
	public void onColorChanged(int newColor) {
		updateColorText(newColor);
		notifyColorChanged(newColor);
	}

	/**
	 * show new color's hex string every time color changed
	 * @param color
	 */
	private void updateColorText(int color) {
		mColorHexEdit.setText(ColorFormatter.formatColor(color));
	}

	/**
	 * ColorPickerList selection changed
	 * @param key
	 */
	public void onKeyChanged(String key, int color) {
		if (!key.equals(mKey)) {
			mKey = key;
			mColorPickerView.setColor(color);
			mColorPanelView.setColor(color);
			updateColorText(color);
		}
	}

	/**
	 * called by outside, register view parts to edit
	 * @param list
	 */
	public void setSelectionData(List<ColorPickerSelectionData> list) {
		colorPickerList.setSelectionData(list);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
								  int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() == 6 || s.length() == 8) {
			try {
				int color =  Color.parseColor("#" + s);
				//不能直接调用onColorChanged，其调用了updateTextColor，会触发onTextChanged，形成死循环
//				onColorChanged(color);
				mColorPickerView.setColor(color);
				notifyColorChanged(color);
			} catch (Exception e) {
				e.printStackTrace();
				mColorHexEdit.setError(getContext().getString(R.string.colorpicker_text_error));
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

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

	private void move(int x, int y) {

		windowParams.x += x;
		windowParams.y += y;
		getWindow().setAttributes(windowParams);//must have
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

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (!isSaved && mListener != null) {
			mListener.onColorCancleSelect();
		}
	}

}
