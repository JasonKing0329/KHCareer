package com.king.mytennis.view.settings;

import java.util.HashMap;

import com.king.mytennis.view.R;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class AutoFillListItemPreference extends Preference {

	private OnClickListener onClickListener;
	private AutoFillItem item;
	private CheckBox checkBox;
	private TextView textView;
	private View view;
	
	public AutoFillListItemPreference(Context context) {
		super(context);
		initView();
	}

	public AutoFillListItemPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		view = LayoutInflater.from(getContext()).inflate(R.layout.setting_autofill_formlist_item, null);
		checkBox = (CheckBox) view.findViewById(R.id.autofill_formlist_item_check);
		textView = (TextView) view.findViewById(R.id.autofill_formlist_item_text);
	}

	public void addOnClickListener(OnClickListener listener) {
		this.onClickListener = listener;
		textView.setOnClickListener(onClickListener);
		checkBox.setOnClickListener(onClickListener);
	}

	public void setValue(AutoFillItem item) {
		this.item = item;
		textView.setText(this.item.getMatch());
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("item", item);
		map.put("preference", this);
		textView.setTag(map);
		checkBox.setTag(map);
	}
	
	public AutoFillItem getValue() {
		return item;
	}
	
	public void setChecked(boolean checked) {
		checkBox.setChecked(checked);
	}
	
	public boolean isChecked() {
		return checkBox.isChecked();
	}

	public CompoundButton getCompoundButton() {
		return checkBox;
	}
	
	@Override
	public View getView(View convertView, ViewGroup parent) {

		return view;
	}

}
