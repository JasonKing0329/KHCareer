package com.king.mytennis.glory.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.king.mytennis.glory.model.GrandSlameItem;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class GrandSlameAdapter extends BaseAdapter {

	private Context context;
	private List<GrandSlameItem> achieveList;
	private boolean isEditMode;
	//private SimpleDateFormat format;

	public boolean isEditMode() {
		return isEditMode;
	}

	public void setEditMode(boolean isEditMode) {
		this.isEditMode = isEditMode;
	}

	public GrandSlameAdapter(Context context, List<GrandSlameItem> list) {
		this.context = context;
		achieveList = list;
	}

	public void setAchieveList(List<GrandSlameItem> list) {
		achieveList = list;
	}
	
	@Override
	public int getCount() {
		int count = 0;
		if (achieveList != null) {
			count = achieveList.size() + 1;
		}
		return count;
	}

	@Override
	public Object getItem(int position) {

		if (achieveList != null) {
			achieveList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder h = null;
		if (convertView == null) {
			h = new Holder();
			convertView = LayoutInflater.from(context).inflate(R.layout.glory_xlistview_item_date_tablecontent,
					null);
			h.date = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.findViewById(R.id.tv_index).setVisibility(View.GONE);
			h.iv = (ImageView) convertView.findViewById(R.id.iv_icon);//feather
			h.ao = (TextView) convertView.findViewById(R.id.tv_ao);
			h.fo = (TextView) convertView.findViewById(R.id.tv_fo);
			h.wo = (TextView) convertView.findViewById(R.id.tv_wo);
			h.uo = (TextView) convertView.findViewById(R.id.tv_uo);
			convertView.setTag(h);
		} else {
			h = (Holder) convertView.getTag();
		}

		if (position == 0) {
			h.date.setText(" ");
			h.iv.setVisibility(View.INVISIBLE);
			h.ao.setText("澳网");
			h.fo.setText("法网");
			h.wo.setText("温网");
			h.uo.setText("美网");
		}
		else {
			GrandSlameItem item = achieveList.get(position - 1);
			h.date.setText(item.date);
			h.ao.setText(item.ao);
			h.fo.setText(item.fo);
			h.wo.setText(item.wo);
			h.uo.setText(item.uo);
		}
		return convertView;
	}

	public void addGS() {
		GSEditor editor = new GSEditor(context, new CustomDialog.OnCustomDialogActionListener() {
			
			@Override
			public boolean onSave(Object object) {
				GrandSlameItem item = (GrandSlameItem) object;
				achieveList.add(item);
				notifyDataSetChanged();
				return true;
			}
			
			@Override
			public void onLoadData(HashMap<String, Object> data) {

			}
			
			@Override
			public boolean onCancel() {
				return false;
			}
		});
		editor.show();
	}
	
	public void editGS(final int index) {
		GSEditor editor = new GSEditor(context, new CustomDialog.OnCustomDialogActionListener() {
			
			@Override
			public boolean onSave(Object object) {
				GrandSlameItem item = (GrandSlameItem) object;
				achieveList.set(index, item);
				notifyDataSetChanged();
				return true;
			}
			
			@Override
			public void onLoadData(HashMap<String, Object> data) {
				data.put("data", index);
			}
			
			@Override
			public boolean onCancel() {
				return false;
			}
		});
		editor.show();
	}
	
	private class Holder {
		public TextView date;
		public ImageView iv;
		public TextView ao, fo, wo, uo;
	}
	
	private class GSEditor extends CustomDialog {

		private EditText dateEdit;
		private Spinner aoSpinner, foSpinner, woSpinner, uoSpinner;
		private int editIndex;
		private String[] roundArrays;
		
		public GSEditor(Context context, OnCustomDialogActionListener actionListener) {
			super(context, actionListener);
			roundArrays = context.getResources().getStringArray(R.array.spinner_achive_gs);
			HashMap<String, Object> data = new HashMap<String, Object>();
			actionListener.onLoadData(data);
			if (data.get("data") != null) {
				editIndex = (Integer) data.get("data");
				GrandSlameItem item = achieveList.get(editIndex);
				dateEdit.setText(item.date);
				for (int i = 0; i < roundArrays.length; i ++) {
					if (roundArrays[i].equals(item.ao)) {
						aoSpinner.setSelection(i);
					}
					if (roundArrays[i].equals(item.fo)) {
						foSpinner.setSelection(i);
					}
					if (roundArrays[i].equals(item.wo)) {
						woSpinner.setSelection(i);
					}
					if (roundArrays[i].equals(item.uo)) {
						uoSpinner.setSelection(i);
					}
				}
			}
		}

		@Override
		protected View getCustomView() {
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_glory_gs_edit, null);
			dateEdit = (EditText) view.findViewById(R.id.glory_gs_edit_date);
			aoSpinner = (Spinner) view.findViewById(R.id.glory_gs_spinner_ao);
			foSpinner = (Spinner) view.findViewById(R.id.glory_gs_spinner_fo);
			woSpinner = (Spinner) view.findViewById(R.id.glory_gs_spinner_wo);
			uoSpinner = (Spinner) view.findViewById(R.id.glory_gs_spinner_uo);
			return view;
		}

		
		
		@Override
		public void onClick(View view) {
			if (view == saveButton) {
				GrandSlameItem item = new GrandSlameItem();
				String str = dateEdit.getText().toString();
				if (str == null || str.length() == 0) {
					dateEdit.setError(context.getResources().getString(R.string.date_not_null));
					return;
				}
				else {
					item.date = str;
				}
				item.ao = (String) aoSpinner.getSelectedItem();
				item.fo = (String) foSpinner.getSelectedItem();
				item.wo = (String) woSpinner.getSelectedItem();
				item.uo = (String) uoSpinner.getSelectedItem();
				
				actionListener.onSave(item);
			}
			super.onClick(view);
		}

		@Override
		protected View getCustomToolbar() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
