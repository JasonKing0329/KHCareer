package com.king.mytennis.update_v_6_0;

import java.util.HashMap;
import java.util.List;

import com.king.mytennis.glory.GloryController;
import com.king.mytennis.glory.GloryMatchDialog;
import com.king.mytennis.model.Record;
import com.king.mytennis.res.AppResProvider;
import com.king.mytennis.res.ColorRes;
import com.king.mytennis.res.ColorResManager;
import com.king.mytennis.res.JResource;
import com.king.mytennis.service.Application;
import com.king.mytennis.service.ScreenUtils;
import com.king.mytennis.update_v_6_0.adapter.AbstractDataCountAdapter;
import com.king.mytennis.update_v_6_0.adapter.YearDataAdapter;
import com.king.mytennis.update_v_6_0.controller.CardManager;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;
import com.king.lib.colorpicker.ColorPicker;
import com.king.lib.colorpicker.ColorPicker.OnColorPickerListener;
import com.king.lib.colorpicker.ColorPickerSelectionData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TimeLineCardView extends CardView implements OnItemClickListener
		, OnClickListener, OnColorPickerListener{

	private TextView titleView, countView;
	private ListView listView;
	private TimeLineAdapter timeLineAdapter;
	private CardManager cardManager;

	private String year;
	private List<HashMap<String, String>> yearCardData;
	
	private ImageView colorButton;
	private ColorPicker colorPicker;
	
	public TimeLineCardView(Context context) {
		super(context);
		View view = LayoutInflater.from(context).inflate(R.layout.cardview_timeline, null);
		titleView = (TextView) view.findViewById(R.id.cardview_tline_title);
		countView = (TextView) view.findViewById(R.id.cardview_tline_datacount);
		listView = (ListView) view.findViewById(R.id.cardview_tline_list);
		colorButton = (ImageView) view.findViewById(R.id.cardview_tline_color);
		listView.setOnItemClickListener(this);
		countView.setOnClickListener(this);
		colorButton.setOnClickListener(this);
		if (Application.isLollipop()) {
			countView.setBackgroundResource(R.drawable.ripple_white);
		}
		
		resetColor();
		
		addView(view);
	}

	@Override
	public void initData() {
	}
	
	public void setYear(String year) {
		this.year = year;
		titleView.setText(year);
	}
	
	public void setCardManager(CardManager manager) {
		cardManager = manager;
	}

	@Override
	public void setCardData(List<HashMap<String, String>> yearCard) {
		yearCardData = yearCard;
		timeLineAdapter = new TimeLineAdapter(getContext(), yearCardData);
		listView.setAdapter(timeLineAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view
			, final int position, long arg3) {

		GloryMatchDialog dialog = new GloryMatchDialog(getContext(), new CustomDialog.OnCustomDialogActionListener() {
			
			@Override
			public boolean onSave(Object object) {
				return false;
			}
			
			@Override
			public void onLoadData(HashMap<String, Object> data) {
				HashMap<String, String> map = yearCardData.get(position);
				List<Record> list = new GloryController().loadMatchRecord(getContext(), 
						map.get("match"), year + "-" + map.get("date"));
				data.put(CustomDialog.OnCustomDialogActionListener.DATA_TYPE, list);
			}
			
			@Override
			public boolean onCancel() {
				return false;
			}
		});
		dialog.enableItemLongClick();
		dialog.show();
	}

	@Override
	public void onClick(View view) {
		if (view == countView) {
			AbstractDataCountAdapter adapter = new YearDataAdapter(getContext(), cardManager);
			DataCountDialog dialog = new DataCountDialog(getContext(), year, adapter);
			int width = ScreenUtils.getScreenWidth(getContext());
			int height = getResources().getDimensionPixelSize(R.dimen.dialog_data_count_year_height);
			dialog.setDialogSize(width, height);
			dialog.show();
		}
		else if (view == colorButton) {
			if (colorPicker == null) {
				colorPicker = new ColorPicker(getContext(), this);
				colorPicker.setResourceProvider(new AppResProvider(getContext()));
			}
			colorPicker.setSelectionData(new ColorResManager().getCardViewYearPage(getContext()));
			colorPicker.show();
		}
	}

	@Override
	public void onColorChanged(String key, int newColor) {
		if (key != null) {
			if (key.equals(ColorRes.CARDVIEW_YEAR_YEAR)) {
				titleView.setTextColor(newColor);
			}
			else if (key.equals(ColorRes.CARDVIEW_YEAR_COUNT)) {
				countView.setTextColor(newColor);
			}
			else if (key.equals(ColorRes.CARDVIEW_YEAR_FRAMEBK)) {
				listView.setBackgroundColor(newColor);
			}
			else if (key.equals(ColorRes.CARDVIEW_YEAR_ITEM_MONTH)) {
				timeLineAdapter.updateMonthColor(newColor);
			}
			else if (key.equals(ColorRes.CARDVIEW_YEAR_ITEM_WINNER)) {
				timeLineAdapter.updateWinnerColor(newColor);
			}
			else if (key.equals(ColorRes.CARDVIEW_YEAR_ITEM_NORMAL)) {
				timeLineAdapter.updateNormalColor(newColor);
			}
		}
	}

	@Override
	public void onColorSelected(List<ColorPickerSelectionData> list) {
		for (ColorPickerSelectionData data:list) {
			JResource.updateColor(data.getKey(), data.getColor());
		}
		((ManagerActivity) getContext()).onColorEdited();
	}

	@Override
	public void onColorSelected(int color) {
		
	}

	@Override
	public void resetColor() {
		titleView.setTextColor(JResource.getColor(getContext()
				, ColorRes.CARDVIEW_YEAR_YEAR, R.color.cardview_year_year));
		countView.setTextColor(JResource.getColor(getContext()
				, ColorRes.CARDVIEW_YEAR_COUNT, R.color.cardview_year_count));
		listView.setBackgroundColor(JResource.getColor(getContext()
				, ColorRes.CARDVIEW_YEAR_FRAMEBK, R.color.cardview_year_framebk));
		
		if (timeLineAdapter != null) {
			timeLineAdapter.resetColor();
			timeLineAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onColorCancleSelect() {
		resetColor();
	}

	@Override
	public void onApplyDefaultColors() {
		JResource.updateColor(ColorRes.CARDVIEW_YEAR_YEAR
				, getContext().getResources().getColor(R.color.cardview_year_year));
		JResource.updateColor(ColorRes.CARDVIEW_YEAR_COUNT
				, getContext().getResources().getColor(R.color.cardview_year_count));
		JResource.updateColor(ColorRes.CARDVIEW_YEAR_FRAMEBK
				, getContext().getResources().getColor(R.color.cardview_year_framebk));
		JResource.updateColor(ColorRes.CARDVIEW_YEAR_ITEM_MONTH
				, getContext().getResources().getColor(R.color.cardview_year_item_month));
		JResource.updateColor(ColorRes.CARDVIEW_YEAR_ITEM_NORMAL
				, getContext().getResources().getColor(R.color.cardview_year_item_normal));
		JResource.updateColor(ColorRes.CARDVIEW_YEAR_ITEM_WINNER
				, getContext().getResources().getColor(R.color.cardview_year_item_winner));
		JResource.saveColorUpdate(getContext());
	}

}
