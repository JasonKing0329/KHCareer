package com.king.mytennis.view.detail;

import java.util.List;

import com.king.lib.colorpicker.ColorPicker;
import com.king.lib.colorpicker.ColorPicker.OnColorPickerListener;
import com.king.lib.colorpicker.ColorPickerSelectionData;
import com.king.mytennis.interfc.H2HDAO;
import com.king.mytennis.model.H2HDAODB;
import com.king.mytennis.model.MySQLHelper;
import com.king.mytennis.model.Record;
import com.king.mytennis.res.AppResProvider;
import com.king.mytennis.res.ColorRes;
import com.king.mytennis.res.ColorResManager;
import com.king.mytennis.res.JResource;
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.R;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DetailGallery extends BaseActivity implements OnClickListener
		, OnColorPickerListener {

	private FlingGallery mGallery;
	private CheckBox circleBox;
	private DetailViewAdapter viewAdapter;
	private Record record;
	private H2HDAO h2hdao;
	private ViewFactory viewFactory;

	private View bkView;
	private TextView titleView;
	private ImageView mColorButton;
	private ColorPicker colorPicker;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		JResource.initializeColors();
		setContentView(R.layout.detail_activity);

		acceptData();

		mGallery = (FlingGallery) findViewById(R.id.detail_flinggallery);
		circleBox = (CheckBox) findViewById(R.id.detail_bottom);
		mColorButton = (ImageView) findViewById(R.id.actionbar_color);
		mColorButton.setOnClickListener(this);
		bkView = findViewById(R.id.detail_bk);

		viewFactory = new ViewFactory(this, record, h2hdao);
		viewFactory.setOnclickListener(this);
		viewAdapter = new DetailViewAdapter(this, viewFactory);
		mGallery.setAdapter(viewAdapter);
		mGallery.setIsGalleryCircular(circleBox.isChecked());
		circleBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mGallery.setIsGalleryCircular(isChecked);
			}
		});

		resetColor();
		super.onCreate(savedInstanceState);
	}

	public int getCurrentPosition() {
		return mGallery.getCurrentPosition();
	}

	private void acceptData() {
		Bundle bundle = getIntent().getExtras();
		record = (Record) bundle.getSerializable("record");
		h2hdao = new H2HDAODB(this, record.getCompetitor());
	}

	@Override
	protected void onDestroy() {
		MySQLHelper.closeHelper();
		super.onDestroy();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGallery.onGalleryTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.detail_button_left:
			case R.id.detail_button_left1:
			case R.id.detail_button_left2:
				mGallery.movePrevious();
				viewFactory.resetColor();
				break;

			case R.id.detail_button_right:
			case R.id.detail_button_right1:
			case R.id.detail_button_right2:
				mGallery.moveNext();
				viewFactory.resetColor();
				break;
			case R.id.actionbar_color:
				if (colorPicker == null) {
					colorPicker = new ColorPicker(this, this);
					colorPicker.setResourceProvider(new AppResProvider(this));
				}

				if (getCurrentPosition() == ViewFactory.VIEW_MATCH) {
					colorPicker.setSelectionData(new ColorResManager().getDetailGalleryMatchPage(this));
				}
				else {
					colorPicker.setSelectionData(new ColorResManager().getDetailGalleryH2hPage(this));
				}
				colorPicker.show();
				break;
			default:
				break;
		}
	}

	@Override
	public void onColorChanged(String key, int newColor) {
		if (key.equals(ColorRes.DETAIL_GALLERY_BK)) {
			bkView.setBackgroundColor(newColor);
		}
		else {
			if (key.equals(ColorRes.DETAIL_GALLERY_BOTTOM_TEXT)) {
				circleBox.setTextColor(newColor);
			}
			//不能 else
			viewFactory.onColorChanged(key, newColor);
		}
		viewAdapter.notifyDataSetChanged();
	}

	@Override
	public void onColorSelected(int color) {

	}

	@Override
	public void onColorSelected(List<ColorPickerSelectionData> list) {
		for (ColorPickerSelectionData data:list) {
			JResource.updateColor(data.getKey(), data.getColor());
		}
		JResource.saveColorUpdate(this);
	}

	@Override
	public void onColorCancleSelect() {
		resetColor();
	}

	@Override
	public void onApplyDefaultColors() {
		JResource.removeColor(ColorRes.DETAIL_GALLERY_BK);
		JResource.removeColor(ColorRes.DETAIL_GALLERY_BOTTOM_TEXT);
		viewFactory.onApplyDefaultColors();
		JResource.saveColorUpdate(this);
		resetColor();
	}

	public void resetColor() {
		bkView.setBackgroundColor(JResource.getColor(this
				, ColorRes.DETAIL_GALLERY_BK, R.color.detail_gallery_bk));
		circleBox.setTextColor(JResource.getColor(this
				, ColorRes.DETAIL_GALLERY_BOTTOM_TEXT, R.color.detail_gallery_bottom_text));
		viewFactory.resetColor();
	}

}
