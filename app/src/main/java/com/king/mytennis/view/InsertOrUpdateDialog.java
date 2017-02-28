package com.king.mytennis.view;

import java.lang.reflect.Field;

import com.king.mytennis.interfc.RecordDAO;
import com.king.mytennis.model.Constants;
import com.king.mytennis.model.RecordDAOImp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public abstract class InsertOrUpdateDialog {

	protected Context userActivity;
	protected View dialog_view;
	protected String[] arr_round, arr_level, arr_court, arr_region;
	protected String[] arr_year, arr_month;
	private String[] arr_names, arr_matches;
	protected EditText et_rankp1, et_seedp1, et_playercountry, et_rank,
			et_seed;
	protected AutoCompleteTextView actv_compname, actv_matchname;
	protected EditText et_matchcountry, et_city, et_winner, et_score;
	protected Spinner sp_year, sp_month, sp_round, sp_level, sp_court,
			sp_region;
	protected int cur_year = 2, cur_month = 0, cur_round = 0, cur_level = 0,
			cur_court = 0, cur_region = 0;// 记录当前spinner选项

	private SpinnerListener spinnerListener;

	public InsertOrUpdateDialog(Context userActivity) {
		this.userActivity = userActivity;
		spinnerListener = new SpinnerListener();
	}

	public void initData() {

		arr_court = Constants.RECORD_MATCH_COURTS;
		arr_level = Constants.RECORD_MATCH_LEVELS;
		arr_region = userActivity.getResources().getStringArray(
				R.array.spinner_region);
		arr_round = Constants.RECORD_MATCH_ROUNDS;
		arr_month = new String[12];
		for (int i = 0; i < 12;) {
			if (i < 9)
				arr_month[i] = "0" + (++i);
			else
				arr_month[i] = "" + (++i);
		}
		arr_year = new String[20];
		for (int n = 0; n < 20; n++) {
			arr_year[n] = "" + (n + 2010);
		}

		RecordDAO dao = new RecordDAOImp(userActivity);
		arr_names = dao.getCptNames();
		arr_matches = dao.getMatchNames();
	}

	protected void initView() {

		ArrayAdapter<String> spinnerAdapter;

		LayoutInflater factory = LayoutInflater.from(userActivity);
		dialog_view = factory.inflate(R.layout.dialog_insert, null);

		et_rankp1 = (EditText) dialog_view.findViewById(R.id.insert_et_rank1);
		et_seedp1 = (EditText) dialog_view.findViewById(R.id.insert_et_seed1);

		actv_compname = (AutoCompleteTextView) dialog_view
				.findViewById(R.id.insert_et_compname);

		actv_compname.setAdapter(new ArrayAdapter<String>(userActivity,
				android.R.layout.simple_dropdown_item_1line, arr_names));

		et_playercountry = (EditText) dialog_view
				.findViewById(R.id.insert_et_country);
		et_rank = (EditText) dialog_view.findViewById(R.id.insert_et_rank);
		et_seed = (EditText) dialog_view.findViewById(R.id.insert_et_seed);

		actv_matchname = (AutoCompleteTextView) dialog_view
				.findViewById(R.id.insert_et_matchname);

		actv_matchname.setAdapter(new ArrayAdapter<String>(userActivity,
				android.R.layout.simple_dropdown_item_1line, arr_matches));

		et_matchcountry = (EditText) dialog_view
				.findViewById(R.id.insert_et_matchcountry);
		et_city = (EditText) dialog_view.findViewById(R.id.insert_et_city);
		et_winner = (EditText) dialog_view.findViewById(R.id.insert_et_winner);
		et_score = (EditText) dialog_view.findViewById(R.id.insert_et_score);

		sp_court = (Spinner) dialog_view
				.findViewById(R.id.insert_spinner_court);
		sp_month = (Spinner) dialog_view
				.findViewById(R.id.insert_spinner_month);
		sp_year = (Spinner) dialog_view.findViewById(R.id.insert_spinner_year);
		sp_round = (Spinner) dialog_view
				.findViewById(R.id.insert_spinner_round);
		sp_level = (Spinner) dialog_view
				.findViewById(R.id.insert_spinner_level);
		sp_region = (Spinner) dialog_view
				.findViewById(R.id.insert_spinner_region);

		spinnerAdapter = new ArrayAdapter<String>(userActivity,
				android.R.layout.simple_spinner_item, arr_year);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sp_year.setAdapter(spinnerAdapter);
		sp_year.setOnItemSelectedListener(spinnerListener);
		spinnerAdapter = new ArrayAdapter<String>(userActivity,
				android.R.layout.simple_spinner_item, arr_month);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sp_month.setAdapter(spinnerAdapter);
		sp_month.setOnItemSelectedListener(spinnerListener);
		spinnerAdapter = new ArrayAdapter<String>(userActivity,
				android.R.layout.simple_spinner_item, arr_round);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_round.setAdapter(spinnerAdapter);
		sp_round.setOnItemSelectedListener(spinnerListener);
		spinnerAdapter = new ArrayAdapter<String>(userActivity,
				android.R.layout.simple_spinner_item, arr_court);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_court.setAdapter(spinnerAdapter);
		sp_court.setOnItemSelectedListener(spinnerListener);
		spinnerAdapter = new ArrayAdapter<String>(userActivity,
				android.R.layout.simple_spinner_item, arr_level);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_level.setAdapter(spinnerAdapter);
		sp_level.setOnItemSelectedListener(spinnerListener);
		spinnerAdapter = new ArrayAdapter<String>(userActivity,
				android.R.layout.simple_spinner_item, arr_region);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_region.setAdapter(spinnerAdapter);
		sp_region.setOnItemSelectedListener(spinnerListener);
	}

	private class SpinnerListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
								   int position, long id) {
			if (parent == sp_year) {
				cur_year = position;
			} else if (parent == sp_month) {
				cur_month = position;
			} else if (parent == sp_court) {
				cur_court = position;
			} else if (parent == sp_level) {
				cur_level = position;
			} else if (parent == sp_region) {
				cur_region = position;
			} else if (parent == sp_month) {
				cur_round = position;
			} else if (parent == sp_round) {
				cur_round = position;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	protected void destoryDialog(DialogInterface dialog) {

		try {
			Field field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
