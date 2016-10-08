package com.king.mytennis.update_v_6_0;

import java.util.HashMap;
import java.util.List;

import com.king.lib.colorpicker.ColorPicker;
import com.king.lib.colorpicker.ColorPicker.OnColorPickerListener;
import com.king.lib.colorpicker.ColorPickerSelectionData;
import com.king.mytennis.glory.GloryController;
import com.king.mytennis.glory.GloryMatchDialog;
import com.king.mytennis.model.Record;
import com.king.mytennis.res.AppResProvider;
import com.king.mytennis.res.ColorRes;
import com.king.mytennis.res.ColorResManager;
import com.king.mytennis.res.JResource;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MatchCardView extends CardView implements OnItemClickListener
		, OnClickListener, OnColorPickerListener{

	private TextView titleView, titleLevelView;
	private ListView listView;
	private MatchCardAdapter mAdapter;

	private String match;
	private String level;
	private List<HashMap<String, String>> matchData;

	private ImageView colorButton;
	private ColorPicker colorPicker;
	
	public MatchCardView(Context context) {
		super(context);
		View view = LayoutInflater.from(context).inflate(R.layout.cardview_match, null);
		titleView = (TextView) view.findViewById(R.id.cardview_match_title);
		titleLevelView = (TextView) view.findViewById(R.id.cardview_match_title_level);
		listView = (ListView) view.findViewById(R.id.cardview_match_list);
		colorButton = (ImageView) view.findViewById(R.id.cardview_match_color);
		listView.setOnItemClickListener(this);
		colorButton.setOnClickListener(this);
		
		resetColor();
		
		addView(view);
	}

	@Override
	public void initData() {
		
	}

	public void setMatch(HashMap<String, String> map) {
		match = map.get("match");
		level = map.get("level");
		titleView.setText(match);
		titleLevelView.setText(level);
	}

	@Override
	public void setCardData(List<HashMap<String, String>> matchData) {
		this.matchData = matchData;
		mAdapter = new MatchCardAdapter(getContext(), matchData);
		listView.setAdapter(mAdapter);
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
				HashMap<String, String> map = matchData.get(position);
				List<Record> list = new GloryController().loadMatchRecord(getContext(), 
						match, map.get("date"));
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
	public void resetColor() {
		titleView.setTextColor(JResource.getColor(getContext()
				, ColorRes.CARDVIEW_MATCH_TITLE, R.color.cardview_match_title));
		titleLevelView.setTextColor(JResource.getColor(getContext()
				, ColorRes.CARDVIEW_MATCH_TITLE_LEVEL, R.color.cardview_match_title_level));
		listView.setBackgroundColor(JResource.getColor(getContext()
				, ColorRes.CARDVIEW_MATCH_LIST_BK, R.color.cardview_match_list_bk));
		if (mAdapter != null) {
			mAdapter.resetColor();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == colorButton) {
			if (colorPicker == null) {
				colorPicker = new ColorPicker(getContext(), this);
				colorPicker.setResourceProvider(new AppResProvider(getContext()));
			}
			colorPicker.setSelectionData(new ColorResManager().getCardViewMatchPage(getContext()));
			colorPicker.show();
		}
	}

	@Override
	public void onColorChanged(String key, int newColor) {
		if (key.equals(ColorRes.CARDVIEW_MATCH_TITLE)) {
			titleView.setTextColor(newColor);
		}
		else if (key.equals(ColorRes.CARDVIEW_MATCH_TITLE_LEVEL)) {
			titleLevelView.setTextColor(newColor);
		}
		else if (key.equals(ColorRes.CARDVIEW_MATCH_LIST_BK)) {
			listView.setBackgroundColor(newColor);
		}
		else if (key.equals(ColorRes.CARDVIEW_MATCH_LIST_DATE)) {
			mAdapter.updateDateColor(newColor);
		}
		else if (key.equals(ColorRes.CARDVIEW_MATCH_LIST_WINNER)) {
			mAdapter.updateWinnerColor(newColor);
		}
		else if (key.equals(ColorRes.CARDVIEW_MATCH_LIST_RUNNERUP)) {
			mAdapter.updateRunnerupColor(newColor);
		}
		else if (key.equals(ColorRes.CARDVIEW_MATCH_LIST_SF)) {
			mAdapter.updateSfColor(newColor);
		}
		else if (key.equals(ColorRes.CARDVIEW_MATCH_LIST_NORMAL)) {
			mAdapter.updateNormalColor(newColor);
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
	public void onColorCancleSelect() {
		resetColor();
	}

	@Override
	public void onColorSelected(int color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onApplyDefaultColors() {
		JResource.updateColor(ColorRes.CARDVIEW_MATCH_LIST_BK
				, getContext().getResources().getColor(R.color.cardview_match_list_bk));
		JResource.updateColor(ColorRes.CARDVIEW_MATCH_LIST_DATE
				, getContext().getResources().getColor(R.color.cardview_match_list_date));
		JResource.updateColor(ColorRes.CARDVIEW_MATCH_LIST_NORMAL
				, getContext().getResources().getColor(R.color.cardview_match_list_normal));
		JResource.updateColor(ColorRes.CARDVIEW_MATCH_LIST_RUNNERUP
				, getContext().getResources().getColor(R.color.cardview_match_list_runnerup));
		JResource.updateColor(ColorRes.CARDVIEW_MATCH_LIST_SF
				, getContext().getResources().getColor(R.color.cardview_match_list_sf));
		JResource.updateColor(ColorRes.CARDVIEW_MATCH_TITLE
				, getContext().getResources().getColor(R.color.cardview_match_title));
		JResource.updateColor(ColorRes.CARDVIEW_MATCH_TITLE_LEVEL
				, getContext().getResources().getColor(R.color.cardview_match_title_level));
		JResource.saveColorUpdate(getContext());
	}

}
