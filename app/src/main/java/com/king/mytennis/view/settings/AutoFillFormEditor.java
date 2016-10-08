package com.king.mytennis.view.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.king.mytennis.glory.ActionBar;
import com.king.mytennis.glory.ActionBar.ActionBarListener;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.R;

public class AutoFillFormEditor extends BaseActivity implements ActionBarListener
		, OnItemClickListener{

	private ActionBar actionBar;
	public static final int RESULT_OK = 1001;
	public static final int REQUEST_ADD = 2001;
	public static final int REQUEST_UPDATE = 2002;
	public static final int REQUEST_DELETE = 2003;

	private String[] arr_round, arr_level, arr_court, arr_region;
	private ScrollView formView;
	private Spinner sp_round, sp_level, sp_court, sp_region;
	protected int cur_round = 0, cur_level = 0, cur_court = 0, cur_region = 0;// 记录当前spinner选项
	private EditText et_match, et_country, et_city;
	private AutoFillItem autoFillItem;

	private ListView delListView;
	private DelListAdapter listAdapter;
	private List<File> delList;
	private List<Boolean> checkList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_auto_fill_item);

		Bundle bundle = getIntent().getExtras();
		int requestCode = bundle.getInt("request_code");

		actionBar = new ActionBar(this, this);
		actionBar.setTitle(getResources().getString(R.string.setting_auto_fill_form_list));
		actionBar.addDeleteIcon();
		if (requestCode != REQUEST_DELETE) {
			actionBar.addSaveIcon();
		}

		initData(requestCode);
		initView(requestCode);

		if (Configuration.supportMultiAutoFill) {
			if (requestCode == REQUEST_UPDATE) {
				autoFillItem = (AutoFillItem) bundle.getSerializable("data");
				showFrom(autoFillItem);
			}
		}
		else {
			if (requestCode == REQUEST_UPDATE) {
				Configuration.getInstance().loadFromPreference(this);
				autoFillItem = Configuration.getInstance().autoFillItem;
				showFrom(autoFillItem);
			}
		}
	}

	private void initData(int requestCode) {

		if (requestCode == REQUEST_DELETE) {
			listAdapter = new DelListAdapter();
			File[] files = new File(Configuration.AUTOFILL_DIR).listFiles();
			if (files != null && files.length > 0) {
				delList = new ArrayList<File>();
				checkList = new ArrayList<Boolean>();
				for (File f:files) {
					delList.add(f);
					checkList.add(false);
				}
			}
		}
		else {
			arr_court = getResources().getStringArray(
					R.array.spinner_court);
			arr_level = getResources().getStringArray(
					R.array.spinner_level);
			arr_region = getResources().getStringArray(
					R.array.spinner_region);
			arr_round = getResources().getStringArray(
					R.array.spinner_round);
		}
	}

	private void initView(int requestCode) {

		if (requestCode == REQUEST_DELETE) {
			initDelListView();
		}
		else {
			initFormView();
		}
	}

	private void initDelListView() {
		delListView = (ListView) findViewById(R.id.setting_autofill_insert_dellist);
		delListView.setVisibility(View.VISIBLE);
		delListView.setAdapter(listAdapter);
		delListView.setOnItemClickListener(this);
	}

	private void initFormView() {
		formView = (ScrollView) findViewById(R.id.setting_autofill_insert_form);
		formView.setVisibility(View.VISIBLE);
		et_match = (EditText) findViewById(R.id.setting_autofill_insert_match);
		et_country = (EditText) findViewById(R.id.setting_autofill_insert_country);
		et_city = (EditText) findViewById(R.id.setting_autofill_insert_city);

		ArrayAdapter<String> spinnerAdapter;
		SpinnerListener spinnerListener = new SpinnerListener();

		sp_court = (Spinner) findViewById(R.id.setting_autofill_insert_court);
		sp_round = (Spinner) findViewById(R.id.setting_autofill_insert_round);
		sp_level = (Spinner) findViewById(R.id.setting_autofill_insert_level);
		sp_region = (Spinner) findViewById(R.id.setting_autofill_insert_region);

		spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr_round);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_round.setAdapter(spinnerAdapter);
		sp_round.setOnItemSelectedListener(spinnerListener);
		spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr_court);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_court.setAdapter(spinnerAdapter);
		sp_court.setOnItemSelectedListener(spinnerListener);
		spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr_level);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_level.setAdapter(spinnerAdapter);
		sp_level.setOnItemSelectedListener(spinnerListener);
		spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr_region);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_region.setAdapter(spinnerAdapter);
		sp_region.setOnItemSelectedListener(spinnerListener);
	}

	private void showFrom(AutoFillItem autoFillItem) {

		et_match.setText(autoFillItem.getMatch());
		et_country.setText(autoFillItem.getCountry());
		et_city.setText(autoFillItem.getCity());
		cur_round = autoFillItem.getRoundIndex();
		sp_round.setSelection(cur_round);
		cur_court = autoFillItem.getCourtIndex();
		sp_court.setSelection(cur_court);
		cur_level = autoFillItem.getLevelIndex();
		sp_level.setSelection(cur_level);
		cur_region = autoFillItem.getRegionIndex();
		sp_region.setSelection(cur_region);
	}

	private class SpinnerListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
								   int position, long id) {
			if (parent == sp_court) {
				cur_court = position;
			} else if (parent == sp_level) {
				cur_level = position;
			} else if (parent == sp_region) {
				cur_region = position;
			} else if (parent == sp_round) {
				cur_round = position;
			} else if (parent == sp_court) {
				cur_court = position;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	@Override
	public void onBack() {

		finish();
	}

	@Override
	public void onDelete() {
		if (formView != null) {
			resetAll();
		}
		else {
			deleteCheckedItems();
		}
	}

	private void deleteCheckedItems() {

		int count = 0;
		for (int i = checkList.size() - 1; i >= 0; i --) {
			if (checkList.get(i)) {
				count ++;
			}
		}
		if (count > 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.delete);
			String msg = getResources().getString(R.string.setting_auto_fill_del_option);
			msg = msg.replace("%d", "" + count);
			builder.setMessage(msg);
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					for (int i = checkList.size() - 1; i >= 0; i --) {
						if (checkList.get(i)) {
							if (delList.get(i).exists()) {
								delList.get(i).delete();
							}
							checkList.remove(i);
							delList.remove(i);
						}
					}
					listAdapter.notifyDataSetChanged();
				}
			});
			builder.setNegativeButton(R.string.cancel, null);
			builder.show();
		}

	}

	private void resetAll() {
		et_city.setText("");
		et_country.setText("");
		et_match.setText("");
		sp_court.setSelection(0);
		sp_level.setSelection(0);
		sp_region.setSelection(0);
		sp_round.setSelection(0);
	}

	@Override
	public void onOk() {

	}

	private void updateAutoFillItem(Context context, String match, String country, String city,
									int court, int level, int region, int round) {
		if (autoFillItem == null) {
			autoFillItem = new AutoFillItem();
		}
		autoFillItem.setMatch(match);
		autoFillItem.setCountry(country);
		autoFillItem.setCity(city);
		autoFillItem.setCourtIndex(court);
		autoFillItem.setLevelIndex(level);
		autoFillItem.setRegionIndex(region);
		autoFillItem.setRoundIndex(round);
	}

	@Override
	public void onCancel() {

	}

	@Override
	public ListAdapter createMenu() {
		return null;
	}

	@Override
	public ListAdapter onPrepareMenu() {
		return null;
	}

	@Override
	public void onMenuSelected(int index) {

	}

	@Override
	public void onTextChanged(String text, int start, int before, int count) {

	}

	@Override
	public void onIconClick(View view) {
		if (view.getId() == R.id.actionbar_save) {
			updateAutoFillItem(this, et_match.getText().toString(), et_country.getText().toString()
					, et_city.getText().toString(), sp_court.getSelectedItemPosition()
					, sp_level.getSelectedItemPosition(), sp_region.getSelectedItemPosition()
					, sp_round.getSelectedItemPosition());
			Bundle bundle = new Bundle();
			bundle.putSerializable("data", autoFillItem);
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		CheckBox box = (CheckBox) view.findViewById(R.id.autofill_formlist_item_check);
		if (box.isChecked()) {
			box.setChecked(false);
			checkList.set(position, false);
		}
		else {
			box.setChecked(true);
			checkList.set(position, true);
		}
	}

	private class DelListAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return delList == null ? 0:delList.size();
		}

		@Override
		public Object getItem(int position) {

			return delList == null ? position:delList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(AutoFillFormEditor.this).inflate(R.layout.setting_autofill_formlist_item, null);
			TextView view = (TextView) convertView.findViewById(R.id.autofill_formlist_item_text);
			convertView.findViewById(R.id.autofill_formlist_item_check).setClickable(false);
			convertView.findViewById(R.id.autofill_formlist_item_check).setFocusable(false);
			view.setText(delList.get(position).getName());
			return convertView;
		}

	}

}
