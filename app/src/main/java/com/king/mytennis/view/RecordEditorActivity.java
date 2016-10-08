package com.king.mytennis.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.king.mytennis.interfc.RecordDAO;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.MySQLHelper;
import com.king.mytennis.model.Record;
import com.king.mytennis.model.RecordDAOImp;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.service.RecordEditorService;
import com.king.mytennis.service.RecordService;
import com.king.mytennis.utils.PinyinUtil;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class RecordEditorActivity extends BaseActivity implements OnClickListener {

	private TextView nextPageView, previousPageView, doneView, continueView;
	private RelativeLayout bkLayout;
	private TextView titleView;
	private LinearLayout playerLayout, matchLayout;
	private ProgressDialog progressDialog;

	private RecordEditorService recordEditorService;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.dialog_insert_update);

		spinnerListener = new SpinnerListener();
		recordEditorService = new RecordEditorService();

		initDirectView();
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getResources().getString(R.string.loading));
		progressDialog.show();
		new DataLoader().start();

		super.onCreate(savedInstanceState);
	}

	private class DataLoader extends Thread implements Callback {

		private Handler handler = new Handler(this);
		@Override
		public void run() {
			initData();
			handler.sendMessage(new Message());
		}

		@Override
		public boolean handleMessage(Message msg) {
			initView();
			showFromServerFile();
			progressDialog.cancel();
			return true;
		}

	}

	private void initDirectView() {
		titleView = (TextView) findViewById(R.id.editor_title);
		nextPageView = (TextView) findViewById(R.id.next_indicator);
		previousPageView = (TextView) findViewById(R.id.previous_indicator);
		doneView = (TextView) findViewById(R.id.done_indicator);
		continueView = (TextView) findViewById(R.id.continue_indicator);
		playerLayout = (LinearLayout) findViewById(R.id.editor_layout_player);
		matchLayout = (LinearLayout) findViewById(R.id.editor_layout_match);
		bkLayout = (RelativeLayout) findViewById(R.id.layout_editor);

		Bitmap bitmap = recordEditorService.loadBackgound();
		if (bitmap == null) {
			bkLayout.setBackground(null);
		}
		else {
			bkLayout.setBackground(new BitmapDrawable(bitmap));
		}

		nextPageView.setOnClickListener(this);
		previousPageView.setOnClickListener(this);
		doneView.setOnClickListener(this);
		continueView.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		MySQLHelper.closeHelper();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (v == nextPageView) {
			playerLayout.setVisibility(View.GONE);
			matchLayout.setVisibility(View.VISIBLE);
			titleView.setText(getResources().getString(R.string.match_infor));
			previousPageView.setVisibility(View.VISIBLE);
			nextPageView.setVisibility(View.GONE);
			doneView.setVisibility(View.VISIBLE);
		}
		else if (v == previousPageView) {
			matchLayout.setVisibility(View.GONE);
			playerLayout.setVisibility(View.VISIBLE);
			titleView.setText(getResources().getString(R.string.player_infor));
			previousPageView.setVisibility(View.GONE);
			nextPageView.setVisibility(View.VISIBLE);
			doneView.setVisibility(View.GONE);
		}
		else if (v == doneView) {
			if (continueView.getVisibility() == View.GONE) {
				if (insert()) {
					Toast.makeText(this, R.string.insert_done, Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(this, R.string.exception_save, Toast.LENGTH_LONG).show();
				}

				continueView.setVisibility(View.VISIBLE);
			}
			else {
				finish();
			}
		}
		else if (v == continueView) {
			continueView.setVisibility(View.GONE);
			matchLayout.setVisibility(View.GONE);
			playerLayout.setVisibility(View.VISIBLE);
			titleView.setText(getResources().getString(R.string.player_infor));
			previousPageView.setVisibility(View.GONE);
			nextPageView.setVisibility(View.VISIBLE);
			doneView.setVisibility(View.GONE);

			continueInsert();
		}
	}

	private void continueInsert() {
		Configuration conf = Configuration.getInstance();
		et_rankp1.setText("");
		et_seedp1.setText("");
		et_city.setText(conf.autoFillItem.getCity());
		actv_compname.setText("");
		et_matchcountry.setText(conf.autoFillItem.getCountry());
		actv_matchname.setText(conf.autoFillItem.getMatch());
		et_playercountry.setText("");
		et_rank.setText("");
		et_score.setText("");
		et_seed.setText("");
		et_winner.setText(MultiUserManager.getInstance().getCurrentUser().getDisplayName());
		sp_year.setSelection(conf.index_year);
		sp_month.setSelection(conf.index_month);
		sp_round.setSelection(conf.autoFillItem.getRoundIndex());
		sp_court.setSelection(conf.autoFillItem.getCourtIndex());
		sp_level.setSelection(conf.autoFillItem.getLevelIndex());
		sp_region.setSelection(conf.autoFillItem.getRegionIndex());
	}

	private void initData() {

		arr_court = getResources().getStringArray(
				R.array.spinner_court);
		arr_level = getResources().getStringArray(
				R.array.spinner_level);
		arr_region = getResources().getStringArray(
				R.array.spinner_region);
		arr_round = getResources().getStringArray(
				R.array.spinner_round);
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

		RecordDAO dao = new RecordDAOImp(this);
		arr_names = dao.getCptNames();
		arr_matches = dao.getMatchNames();

		//v5.4 after add multiuser module, this error found
		if (arr_names == null) {
			arr_names = new String[]{};
		}
		if (arr_matches == null) {
			arr_matches = new String[]{};
		}
	}

	private void initView() {

		ArrayAdapter<String> spinnerAdapter;

		et_rankp1 = (EditText) findViewById(R.id.insert_et_rank1);
		et_seedp1 = (EditText) findViewById(R.id.insert_et_seed1);

		actv_compname = (AutoCompleteTextView) findViewById(R.id.insert_et_compname);

		actv_compname.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, arr_names));

		et_playercountry = (EditText) findViewById(R.id.insert_et_country);
		et_rank = (EditText) findViewById(R.id.insert_et_rank);
		et_seed = (EditText) findViewById(R.id.insert_et_seed);

		actv_matchname = (AutoCompleteTextView) findViewById(R.id.insert_et_matchname);

		actv_matchname.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, arr_matches));

		et_matchcountry = (EditText) findViewById(R.id.insert_et_matchcountry);
		et_city = (EditText) findViewById(R.id.insert_et_city);
		et_winner = (EditText) findViewById(R.id.insert_et_winner);
		et_score = (EditText) findViewById(R.id.insert_et_score);

		sp_month = (Spinner) findViewById(R.id.insert_spinner_month);
		sp_year = (Spinner) findViewById(R.id.insert_spinner_year);
		sp_court = (Spinner) findViewById(R.id.insert_spinner_court);
		sp_round = (Spinner) findViewById(R.id.insert_spinner_round);
		sp_level = (Spinner) findViewById(R.id.insert_spinner_level);
		sp_region = (Spinner) findViewById(R.id.insert_spinner_region);

		spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr_year);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sp_year.setAdapter(spinnerAdapter);
		sp_year.setOnItemSelectedListener(spinnerListener);
		spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr_month);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sp_month.setAdapter(spinnerAdapter);
		sp_month.setOnItemSelectedListener(spinnerListener);
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

	private void showFromServerFile() {

		Configuration conf = Configuration.getInstance();
		et_winner.setText(MultiUserManager.getInstance().getCurrentUser().getDisplayName());
		cur_year = conf.index_year;
		sp_year.setSelection(cur_year);
		cur_month = conf.index_month;
		sp_month.setSelection(cur_month);

		//v4.7 change: load from preference
		conf.loadFromPreference(this);
		actv_matchname.setText(conf.autoFillItem.getMatch());
		et_matchcountry.setText(conf.autoFillItem.getCountry());
		et_city.setText(conf.autoFillItem.getCity());
		cur_round = conf.autoFillItem.getRoundIndex();
		sp_round.setSelection(cur_round);
		cur_court = conf.autoFillItem.getCourtIndex();
		sp_court.setSelection(cur_court);
		cur_level = conf.autoFillItem.getLevelIndex();
		sp_level.setSelection(cur_level);
		cur_region = conf.autoFillItem.getRegionIndex();
		sp_region.setSelection(cur_region);
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
			} else if (parent == sp_round) {
				cur_round = position;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	private boolean insert() {

		Record record=new Record();
		int temp;
		try {
			temp=Integer.parseInt(et_rankp1.getText().toString());
		} catch (Exception e) {
			temp=0;
		}
		record.setRank(temp);
		try {
			temp=Integer.parseInt(et_seedp1.getText().toString());
		} catch (Exception e) {
			temp=0;
		}
		record.setSeed(temp);
		try {
			temp=Integer.parseInt(et_rank.getText().toString());
		} catch (Exception e) {
			temp=0;
		}
		record.setCptRank(temp);
		try {
			temp=Integer.parseInt(et_seed.getText().toString());
		} catch (Exception e) {
			temp=0;
		}
		record.setCptSeed(temp);
		record.setCity(et_city.getText().toString());
		record.setCompetitor(actv_compname.getText().toString());
		record.setCptCountry(et_playercountry.getText().toString());
		record.setMatchCountry(et_matchcountry.getText().toString());
		record.setCourt(arr_court[cur_court]);
		record.setLevel(arr_level[cur_level]);
		record.setMatch(actv_matchname.getText().toString());
		record.setRegion(arr_region[cur_region]);
		record.setRound(arr_round[cur_round]);
		record.setScore(et_score.getText().toString());
		String month=(1+cur_month)<10 ? ("0"+(1+cur_month)):(""+(1+cur_month));
		record.setStrDate(""+(2010+cur_year)+"-"+month);
		if (et_winner.getText().toString().matches(MultiUserManager.getInstance().getCurrentUser().getDisplayName())) {
			record.setWinner(MultiUserManager.USER_DB_FLAG);
		}
		else record.setWinner(record.getCompetitor());
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM");
		long lDate;
		try {
			lDate=format.parse(record.getStrDate()).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			lDate=System.currentTimeMillis();
		}
		record.setLongDate(lDate);

		Configuration conf = Configuration.getInstance();
		conf.index_month = cur_month;
		conf.index_year = cur_year;

		String name = actv_compname.getText().toString();
		if (!recordEditorService.isPlayerExisted(this, name)) {
			String pinyin = PinyinUtil.getPinyin(name);
			recordEditorService.insertNamePinyin(this, name, pinyin);
		}
		RecordService service = new RecordService(this);
		return service.insert(record);
	}
}
