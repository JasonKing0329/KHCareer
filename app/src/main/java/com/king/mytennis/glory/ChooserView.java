package com.king.mytennis.glory;

import com.king.mytennis.view.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ChooserView extends LinearLayout {

	private SPictureChooseListener listener;
	private int childWidth;

	public ChooserView(Context context) {
		super(context);
		childWidth = context.getResources().getDimensionPixelSize(R.dimen.spicture_chooser_item_width);
	}

	public ChooserView(Context context, AttributeSet attr) {
		super(context, attr);
		childWidth = context.getResources().getDimensionPixelSize(R.dimen.spicture_chooser_item_width);
	}

	public void setOnChooseListener(SPictureChooseListener listener) {
		this.listener = listener;
	}

	public void setAdapter(HorizontalChooserAdapter adapter) {
		if (getChildCount() > 0) {
			removeAllViews();
		}
		for (int i = 0; i < adapter.getCount(); i++) {
			View v = adapter.getView(i, null, null);

			// 为视图设定点击监听器
			final int position = i;
			final View view = v;
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (listener != null) {
						listener.onChoose(view, position);
					}
				}
			});
			v.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (listener != null) {
						listener.onLongTouch(view, position);
					}
					return true;
				}
			});
			this.setOrientation(HORIZONTAL);
			this.addView(view, new LinearLayout.LayoutParams(
					childWidth, childWidth));/*LayoutParams.WRAP_CONTENT*/
		}
	}
}