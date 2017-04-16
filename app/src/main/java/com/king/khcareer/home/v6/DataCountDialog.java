package com.king.khcareer.home.v6;

import com.king.khcareer.base.KApplication;
import com.king.khcareer.home.v6.adapter.AbstractDataCountAdapter;
import com.king.mytennis.view.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class DataCountDialog extends Dialog implements OnClickListener {

	private TextView contentView[];
	private TextView titleView[];
	private TextView yearTextView;
	private ImageView previousButton, nextButton;
	private View rootView;
	private LayoutParams windowParams;
	
	private AbstractDataCountAdapter adapter;
	
	private String year;
	
	public DataCountDialog(Context context, String year, AbstractDataCountAdapter adapter) {
		super(context, R.style.DrawsCustomDialog);
		setContentView(R.layout.dialog_data_count);
		this.year = year;
		this.adapter = adapter;

		contentView = new TextView[6];
		titleView = new TextView[6];
		contentView[0] = (TextView) findViewById(R.id.data_count_content1);
		contentView[1] = (TextView) findViewById(R.id.data_count_content2);
		contentView[2] = (TextView) findViewById(R.id.data_count_content3);
		contentView[3] = (TextView) findViewById(R.id.data_count_content4);
		contentView[4] = (TextView) findViewById(R.id.data_count_content5);
		contentView[5] = (TextView) findViewById(R.id.data_count_content6);
		titleView[0] = (TextView) findViewById(R.id.data_count_title1);
		titleView[1] = (TextView) findViewById(R.id.data_count_title2);
		titleView[2] = (TextView) findViewById(R.id.data_count_title3);
		titleView[3] = (TextView) findViewById(R.id.data_count_title4);
		titleView[4] = (TextView) findViewById(R.id.data_count_title5);
		titleView[5] = (TextView) findViewById(R.id.data_count_title6);
		
		yearTextView = (TextView) findViewById(R.id.data_count_year_text);
		yearTextView.setText(year);
		previousButton = (ImageView) findViewById(R.id.data_count_year_previous);
		nextButton = (ImageView) findViewById(R.id.data_count_year_next);
		if (KApplication.isLollipop()) {
			previousButton.setBackgroundResource(R.drawable.ripple_mview_insert);
			nextButton.setBackgroundResource(R.drawable.ripple_mview_insert);
		}
		previousButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		
		rootView = contentView[0].getRootView();
		rootView.setBackgroundResource(R.drawable.glory_count_bk);
		
		windowParams = getWindow().getAttributes();

		if (adapter != null) {
			adapter.setCurrentYear(year);
			adapter.onYearChanged(year);
			refreshData();
		}
	}
	
	public void setAdapter(AbstractDataCountAdapter adapter) {
		this.adapter = adapter;
		if (adapter != null) {
			adapter.onYearChanged(year);
			refreshData();
		}
	}

	public void setDialogSize(int width, int height) {
		windowParams.width = width;
		windowParams.height = height;
	}
	

	private void refreshData() {
		for (int i = 0; i < adapter.getCount(); i ++) {
			titleView[i].setText(adapter.getTitle(i));
			contentView[i].setText(adapter.getContent(i));
		}
	}

	@Override
	public void onClick(View view) {
		if (view == previousButton || view ==  nextButton) {
			if (view == previousButton) {
				year = adapter.previousYear();
			}
			else if (view == nextButton) {
				year = adapter.nextYear();
			}

			if (adapter != null) {
				adapter.onYearChanged(year);
				refreshData();
				yearTextView.setText(year);
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
}
