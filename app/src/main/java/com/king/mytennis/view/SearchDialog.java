package com.king.mytennis.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.king.mytennis.model.Constants;
import com.king.mytennis.model.DatabaseStruct;
import com.king.mytennis.model.Record;
import com.king.mytennis.model.SearchModel;
import com.king.mytennis.service.RecordService;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SearchDialog implements Button.OnClickListener
		, DialogInterface.OnClickListener, CompoundButton.OnCheckedChangeListener {

	public interface OnSearchListener {
		public List<Record> onGetSearchList();
		public void onSearchResult(List<Record> list);
	}

	private Context userActivity;
	private SearchModel searchModel;
	private OnSearchListener onSearchListener;

	private View view;
	private String[] arr_round, arr_level, arr_court, arr_region;
	private String[] arr_year, arr_month;
	private CheckBox cbox_court, cbox_level, cbox_match, cbox_round
			, cbox_region, cbox_matchcountry, cbox_comp, cbox_compcountry
			, cbox_comprank, cbox_date, cbox_score, cbox_winlose, cbox_sql;
	private EditText et_match, et_comp, et_compcountry, et_comprank_min
			, et_comprank_max, et_matchcountry, et_score, et_where, et_wherevalue;
	private TextView hintTableText;
	private RadioGroup rgroup_winlose;
	private RadioButton radio_win, radio_lose;
	private Button button_date_start, button_date_end;
	private Spinner sp_court, sp_level, sp_region, sp_round;
	private ArrayAdapter<String> spinnerAdapter;
	private int nYear_start, nMonth_start, nDay_start;
	private int nYear_end, nMonth_end, nDay_end;
	private boolean isDateStartChanged=false, isDateEndChanged=false;
	private int cur_round=0, cur_level=0, cur_court=0, cur_region=0;

	public SearchDialog(Context activity, OnSearchListener listener) {

		userActivity = activity;
		onSearchListener = listener;
		searchModel = new SearchModel();
		loadSpinnerArray();
		init();
	}

	private void init(){

		view=LayoutInflater.from(userActivity).inflate(R.layout.dialog_search, null);

		sp_court=(Spinner)view.findViewById(R.id.insert_spinner_court);
		sp_level=(Spinner)view.findViewById(R.id.insert_spinner_level);
		sp_region=(Spinner)view.findViewById(R.id.insert_spinner_region);
		sp_round=(Spinner)view.findViewById(R.id.insert_spinner_round);
		spinnerAdapter=new ArrayAdapter<String>(userActivity, android.R.layout.simple_spinner_item
				, arr_round);
		//或spinnerAdapter = ArrayAdapter.createFromResource(userActivity, R.array.spinner_round, android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_round.setAdapter(spinnerAdapter);
		sp_round.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int pos, long arg3) {
				cur_round=pos;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		spinnerAdapter=new ArrayAdapter<String>(userActivity, android.R.layout.simple_spinner_item
				, arr_court);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_court.setAdapter(spinnerAdapter);
		sp_court.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int pos, long arg3) {
				cur_court=pos;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		spinnerAdapter=new ArrayAdapter<String>(userActivity, android.R.layout.simple_spinner_item
				, arr_level);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_level.setAdapter(spinnerAdapter);
		sp_level.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int pos, long arg3) {
				cur_level=pos;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		spinnerAdapter=new ArrayAdapter<String>(userActivity, android.R.layout.simple_spinner_item
				, arr_region);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_region.setAdapter(spinnerAdapter);
		sp_region.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int pos, long arg3) {
				cur_region=pos;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		et_match=(EditText)view.findViewById(R.id.search_et_match);
		et_comp=(EditText)view.findViewById(R.id.search_et_comp);
		et_compcountry=(EditText)view.findViewById(R.id.search_et_compcountry);
		et_comprank_min=(EditText)view.findViewById(R.id.search_rank_min);
		et_comprank_max=(EditText)view.findViewById(R.id.search_rank_max);
		et_matchcountry=(EditText)view.findViewById(R.id.search_et_matchcountry);
		et_score=(EditText)view.findViewById(R.id.search_et_score);
		et_where=(EditText)view.findViewById(R.id.search_et_sql1);
		et_wherevalue=(EditText)view.findViewById(R.id.search_et_sql2);
		button_date_start=(Button)view.findViewById(R.id.button_date_start);
		button_date_end=(Button)view.findViewById(R.id.button_date_end);
		button_date_start.setOnClickListener(this);
		button_date_end.setOnClickListener(this);

		hintTableText = (TextView) view.findViewById(R.id.search_tv_hint_table);

		rgroup_winlose=(RadioGroup)view.findViewById(R.id.radioGroup_winlose);
		rgroup_winlose.setOnCheckedChangeListener(rgoutListener);
		radio_win=(RadioButton)view.findViewById(R.id.radio_win);
		radio_lose=(RadioButton)view.findViewById(R.id.radio_lose);

		cbox_court=(CheckBox)view.findViewById(R.id.cbox_court);
		cbox_court.setOnCheckedChangeListener(this);
		cbox_level=(CheckBox)view.findViewById(R.id.cbox_level);
		cbox_level.setOnCheckedChangeListener(this);
		cbox_match=(CheckBox)view.findViewById(R.id.cbox_match);
		cbox_match.setOnCheckedChangeListener(this);
		cbox_round=(CheckBox)view.findViewById(R.id.cbox_round);
		cbox_round.setOnCheckedChangeListener(this);
		cbox_region=(CheckBox)view.findViewById(R.id.cbox_region);
		cbox_region.setOnCheckedChangeListener(this);
		cbox_matchcountry=(CheckBox)view.findViewById(R.id.cbox_matchcountry);
		cbox_matchcountry.setOnCheckedChangeListener(this);
		cbox_comp=(CheckBox)view.findViewById(R.id.cbox_compname);
		cbox_comp.setOnCheckedChangeListener(this);
		cbox_compcountry=(CheckBox)view.findViewById(R.id.cbox_compcountry);
		cbox_compcountry.setOnCheckedChangeListener(this);
		cbox_comprank=(CheckBox)view.findViewById(R.id.cbox_comprank);
		cbox_comprank.setOnCheckedChangeListener(this);
		cbox_date=(CheckBox)view.findViewById(R.id.cbox_date);
		cbox_date.setOnCheckedChangeListener(this);
		cbox_score=(CheckBox)view.findViewById(R.id.cbox_score);
		cbox_score.setOnCheckedChangeListener(this);
		cbox_winlose=(CheckBox)view.findViewById(R.id.cbox_winorlose);
		cbox_winlose.setOnCheckedChangeListener(this);
		cbox_sql=(CheckBox)view.findViewById(R.id.cbox_sql);
		cbox_sql.setOnCheckedChangeListener(this);

	}

	public void addSearchListener(OnSearchListener listener) {
		onSearchListener = listener;
	}

	RadioGroup.OnCheckedChangeListener rgoutListener = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

//			int radioID = group.getCheckedRadioButtonId();
//			RadioButton radio = (RadioButton) userActivity.findViewById(radioID);
			if (checkedId == radio_win.getId()) {
				searchModel.setWinner(true);
			}
			else {
				searchModel.setWinner(false);
			}
		}

	};

	public void show() {
		new AlertDialog.Builder(userActivity).setTitle(R.string.search)
				.setView(view)
				.setPositiveButton(R.string.ok, this)
				.setNegativeButton(R.string.cancel, this)
				.show();
	}

	/**
	 * 此方法还需写一系列重置关键词参数代码，弃用
	 */
	@Deprecated
	public void reshow() {

		init();//调试证明，重启必须也同时重新find所有的控件与相关设置
		show();
	}

	private void loadSpinnerArray() {

		arr_court=Constants.RECORD_MATCH_COURTS;
		arr_level= Constants.RECORD_MATCH_LEVELS;
		arr_region=userActivity.getResources().getStringArray(R.array.spinner_region);
		arr_round=Constants.RECORD_MATCH_ROUNDS;
		arr_month=new String[12];
		for (int i=0;i<12;){
			if (i<9)
				arr_month[i]="0"+(++i);
			else
				arr_month[i]=""+(++i);
		}
		arr_year=new String[20];
		for (int n=0;n<20;n++){
			arr_year[n]=""+(n+2010);
		}
	}

	@Override
	public void onClick(View v) {

		if (v.getId()==R.id.button_date_start) {

			Calendar calen=Calendar.getInstance();
			nYear_start=calen.get(Calendar.YEAR);
			nMonth_start=calen.get(Calendar.MONTH);

			DatePickerDialog startDlg=new DatePickerDialog(userActivity,
					startDateLis, nYear_start, nMonth_start, 1);
			startDlg.show();
		}
		else if (v.getId()==R.id.button_date_end) {

			Calendar calen=Calendar.getInstance();
			nYear_end=calen.get(Calendar.YEAR);
			nMonth_end=calen.get(Calendar.MONTH);

			DatePickerDialog endDlg=new DatePickerDialog(userActivity,
					endDateLis, nYear_end, nMonth_end, 28);
			endDlg.show();
		}
	}

	DatePickerDialog.OnDateSetListener startDateLis=new DatePickerDialog.OnDateSetListener(){

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			nYear_start=year;
			nMonth_start=monthOfYear+1;//日期控件的月份是从0开始编号的
			nDay_start=dayOfMonth;
			button_date_start.setText(new StringBuilder().append(nYear_start)
					.append("/").append(nMonth_start).append("/").append(nDay_start));
			isDateStartChanged=true;

		}

	};
	DatePickerDialog.OnDateSetListener endDateLis=new DatePickerDialog.OnDateSetListener(){

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			nYear_end=year;
			nMonth_end=monthOfYear+1;//日期控件的月份是从0开始编号的
			nDay_end=dayOfMonth;
			button_date_end.setText(new StringBuilder().append(nYear_end)
					.append("/").append(nMonth_end).append("/").append(nDay_end));
			isDateEndChanged=true;
		}

	};

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView==cbox_court) {
			if (isChecked){
				sp_court.setVisibility(View.VISIBLE);
				searchModel.setCourtOn(true);
			}
			else{
				sp_court.setVisibility(View.INVISIBLE);
				searchModel.setCourtOn(false);
			}
		}
		else if (buttonView==cbox_level) {
			if (isChecked){
				sp_level.setVisibility(View.VISIBLE);
				searchModel.setLevelOn(true);
			}
			else{
				sp_level.setVisibility(View.INVISIBLE);
				searchModel.setLevelOn(false);
			}
		}
		else if (buttonView==cbox_match) {
			if (isChecked){
				et_match.setVisibility(View.VISIBLE);
				searchModel.setMatchOn(true);
			}
			else{
				et_match.setVisibility(View.INVISIBLE);
				searchModel.setMatchOn(false);
			}
		}
		else if (buttonView==cbox_round) {
			if (isChecked){
				sp_round.setVisibility(View.VISIBLE);
				searchModel.setRoundOn(true);
			}
			else{
				sp_round.setVisibility(View.INVISIBLE);
				searchModel.setRoundOn(false);
			}
		}
		else if (buttonView==cbox_region) {
			if (isChecked){
				sp_region.setVisibility(View.VISIBLE);
				searchModel.setRegionOn(true);
			}
			else{
				sp_region.setVisibility(View.INVISIBLE);
				searchModel.setRegionOn(false);
			}
		}
		else if (buttonView==cbox_matchcountry) {
			if (isChecked){
				et_matchcountry.setVisibility(View.VISIBLE);
				searchModel.setmCountryOn(true);
			}
			else{
				et_matchcountry.setVisibility(View.INVISIBLE);
				searchModel.setmCountryOn(false);
			}
		}
		else if (buttonView==cbox_comp) {
			if (isChecked){
				et_comp.setVisibility(View.VISIBLE);
				searchModel.setCompetitorOn(true);
			}
			else{
				et_comp.setVisibility(View.INVISIBLE);
				searchModel.setCompetitorOn(false);
			}
		}
		else if (buttonView==cbox_compcountry) {
			if (isChecked){
				et_compcountry.setVisibility(View.VISIBLE);
				searchModel.setCptCountryOn(true);
			}
			else{
				et_compcountry.setVisibility(View.INVISIBLE);
				searchModel.setCptCountryOn(false);
			}
		}
		else if (buttonView==cbox_comprank) {
			if (isChecked){
				et_comprank_min.setVisibility(View.VISIBLE);
				et_comprank_max.setVisibility(View.VISIBLE);
				searchModel.setRankOn(true);
			}
			else{
				et_comprank_min.setVisibility(View.INVISIBLE);
				et_comprank_max.setVisibility(View.INVISIBLE);
				searchModel.setRankOn(false);
			}
		}
		else if (buttonView==cbox_date) {
			if (isChecked){
				button_date_start.setVisibility(View.VISIBLE);
				button_date_end.setVisibility(View.VISIBLE);
				searchModel.setDateOn(true);
			}
			else{
				button_date_start.setVisibility(View.INVISIBLE);
				button_date_end.setVisibility(View.INVISIBLE);
				searchModel.setDateOn(false);
			}
		}
		else if (buttonView==cbox_score) {
			if (isChecked){
				et_score.setVisibility(View.VISIBLE);
				searchModel.setScoreOn(true);
			}
			else {
				et_score.setVisibility(View.INVISIBLE);
				searchModel.setScoreOn(false);
			}
		}
		else if (buttonView == cbox_winlose) {
			if (isChecked) {
				searchModel.setIsWinnerOn(true);
				radio_lose.setVisibility(View.VISIBLE);
				radio_win.setVisibility(View.VISIBLE);
				radio_win.setChecked(true);
				//由于监听器里调用setWinner，如果不调用该句以及下一句的话，而且用户只是在
				//checkbox打上勾但是没有点win的话，就一直没有执行setWinner(true).
				searchModel.setWinner(true);
			}
			else {
				searchModel.setIsWinnerOn(false);
				radio_lose.setVisibility(View.INVISIBLE);
				radio_win.setVisibility(View.INVISIBLE);
			}
		}
		else if (buttonView == cbox_sql) {
			if (isChecked) {
				et_where.setVisibility(View.VISIBLE);
				et_wherevalue.setVisibility(View.VISIBLE);
				hintTableText.setVisibility(View.VISIBLE);
				if (hintTableText.getText() == null || hintTableText.getText().length() == 0) {
					StringBuffer buffer = new StringBuffer();
					for (String col:DatabaseStruct.TABLE_RECORD_COL) {
						buffer.append(",").append(col);
					}
					hintTableText.setText("Table record:\n" + buffer.toString().substring(1));
				}
				setStateEnable(false);
			}
			else {
				et_where.setVisibility(View.GONE);
				et_wherevalue.setVisibility(View.GONE);
				hintTableText.setVisibility(View.GONE);
				setStateEnable(true);
			}
		}

	}

	private void setStateEnable(boolean b) {
		if (!b) {
			cbox_comp.setChecked(false);
			cbox_compcountry.setChecked(false);
			cbox_comprank.setChecked(false);
			cbox_court.setChecked(false);
			cbox_date.setChecked(false);
			cbox_level.setChecked(false);
			cbox_match.setChecked(false);
			cbox_matchcountry.setChecked(false);
			cbox_region.setChecked(false);
			cbox_round.setChecked(false);
			cbox_score.setChecked(false);
			cbox_winlose.setChecked(false);
		}
		cbox_comp.setEnabled(b);
		cbox_compcountry.setEnabled(b);
		cbox_comprank.setEnabled(b);
		cbox_court.setEnabled(b);
		cbox_date.setEnabled(b);
		cbox_level.setEnabled(b);
		cbox_match.setEnabled(b);
		cbox_matchcountry.setEnabled(b);
		cbox_region.setEnabled(b);
		cbox_round.setEnabled(b);
		cbox_score.setEnabled(b);
		cbox_winlose.setEnabled(b);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which==AlertDialog.BUTTON_POSITIVE) {
			if (cbox_sql.isChecked()) {
				String where = et_where.getText().toString();
				String valueString = et_wherevalue.getText().toString();
				String[] values = null;
				if (valueString != null) {
					values = valueString.split(",");
				}
				if (onSearchListener != null) {
					onSearchListener.onSearchResult(new RecordService(userActivity).queryByWhere(where, values));
				}
			}
			else {
				orgnizeData();
				if (onSearchListener != null) {
					onSearchListener.onSearchResult(searchModel.searchFromList(onSearchListener.onGetSearchList()));
				}
			}
		}
		else {

		}
	}

	//除了radioGroup决定的win/lose在其监听接口里组装，其他都在这里统一组装
	private void orgnizeData(){
		if (cbox_comp.isChecked()) {
			String competitor=et_comp.getText().toString();
			searchModel.setCompetitor(competitor);
		}
		if (cbox_compcountry.isChecked()) {
			String country=et_compcountry.getText().toString();
			searchModel.setCptCountry(country);
		}
		if (cbox_matchcountry.isChecked()) {
			String country=et_matchcountry.getText().toString();
			searchModel.setmCountry(country);
		}
		if (cbox_court.isChecked()) {
			String court = arr_court[cur_court];
			searchModel.setCourt(court);
		}
		if (cbox_level.isChecked()) {
			String level=arr_level[cur_level];
			searchModel.setLevel(level);
		}
		if (cbox_round.isChecked()) {
			String round=arr_round[cur_round];
			searchModel.setRound(round);
		}
		if (cbox_region.isChecked()) {
			String region=arr_region[cur_region];
			searchModel.setRegion(region);
		}
		if (cbox_match.isChecked()) {
			String match=et_match.getText().toString();
			searchModel.setMatch(match);
		}
		if (cbox_comprank.isChecked()) {

			String str=et_comprank_min.getText().toString();
			int min=0,max=100000;
			try {
				min=Integer.parseInt(str);
			} catch (Exception e) {
				min=0;
			}

			str=et_comprank_max.getText().toString();
			try {
				max=Integer.parseInt(str);
			} catch (Exception e) {
				max=100000;
			}

			searchModel.setRankMin(min);
			searchModel.setRankMax(max);
		}
		if (cbox_date.isChecked()) {

			long date_start=0, date_end=System.currentTimeMillis();
			if (isDateStartChanged) {
				date_start=formatDate(nYear_start, nMonth_start, nDay_start);
			}
			if (isDateEndChanged) {
				date_end=formatDate(nYear_end, nMonth_end, nDay_end);
			}
			searchModel.setDate_start(date_start);
			searchModel.setDate_end(date_end);
		}

		if (cbox_score.isChecked()) {

			searchModel.setScore(et_score.getText().toString());
		}
	}

	/**
	 * 按比分搜索可以对输入比分按照and、or、&、|四个字符及字符串进行拆分，可以实现一个条件
	 * 多个关键字
	 * @param newList
	 */
	private long formatDate(int year, int month, int day){
		long date;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");//天啊！MM一定要大写！！！
		String date_str;
		if (month<10)
			date_str=""+year+"0"+month;
		else
			date_str=""+year+month;
		if (day<10)
			date_str+=("0"+day);
		else
			date_str+=day;
		try {
			date=sdf.parse(date_str).getTime();
		} catch (ParseException e) {
			date=System.currentTimeMillis();
		}
		return date;
	}
}
