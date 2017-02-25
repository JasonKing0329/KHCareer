package com.king.mytennis.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.king.mytennis.match.MatchManageActivity;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.MySQLHelper;
import com.king.mytennis.model.Record;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.player.PlayerManageActivity;
import com.king.mytennis.pubdata.PubDataProvider;
import com.king.mytennis.service.RecordEditorService;
import com.king.mytennis.service.RecordService;
import com.king.mytennis.view.settings.SettingActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class RecordEditorActivity extends BaseActivity implements OnClickListener {

	private final int REQUEST_CHANGE_MATCH = 101;
	private final int REQUEST_CHANGE_PLAYER = 102;
	private TextView nextPageView, previousPageView, doneView, continueView;
	private RelativeLayout bkLayout;
	private TextView titleView;
	private LinearLayout playerLayout, matchLayout;
	private ProgressDialog progressDialog;
	private ImageView ivChangeMatch, ivChangePlayer;

	private RecordEditorService recordEditorService;

	protected String[] arr_round, arr_level, arr_court, arr_region;
	protected String[] arr_year;
	protected EditText et_rankp1, et_seedp1, et_rank,
			et_seed;
	protected TextView tvCompetitor, tvMatch, tvCompetitorCountry
			, tvMatchCountry, tvMatchLevel, tvMatchRegion, tvMatchCity, tvMatchCourt, tvMatchMonth;
	protected EditText et_winner, et_score;
	protected Spinner sp_year, sp_round;
	protected int cur_year = 2, cur_round = 0;// 记录当前spinner选项

	private SpinnerListener spinnerListener;

	private PubDataProvider pubDataProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.dialog_insert_update);

		spinnerListener = new SpinnerListener();
		recordEditorService = new RecordEditorService();
		pubDataProvider = new PubDataProvider();

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
		else if (v == ivChangeMatch) {
			Intent intent = new Intent().setClass(this, MatchManageActivity.class);
			intent.putExtra(MatchManageActivity.KEY_START_MODE, MatchManageActivity.START_MODE_SELECT);
			startActivityForResult(intent, REQUEST_CHANGE_MATCH);
		}
		else if (v == ivChangePlayer) {
			Intent intent = new Intent().setClass(this, PlayerManageActivity.class);
			intent.putExtra(PlayerManageActivity.KEY_START_MODE, PlayerManageActivity.START_MODE_SELECT);
			startActivityForResult(intent, REQUEST_CHANGE_PLAYER);
		}
	}

	private void continueInsert() {
		Configuration conf = Configuration.getInstance();
		et_rankp1.setText("");
		et_seedp1.setText("");
		tvMatchCity.setText(conf.autoFillItem.getCity());
		tvCompetitor.setText("");
		tvMatchCountry.setText(conf.autoFillItem.getCountry());
		tvMatch.setText(conf.autoFillItem.getMatch());
		tvCompetitorCountry.setText("");
		et_rank.setText("");
		et_score.setText("");
		et_seed.setText("");
		et_winner.setText(MultiUserManager.getInstance().getCurrentUser().getDisplayName());
		sp_year.setSelection(conf.index_year);
		tvMatchMonth.setText(String.valueOf(conf.index_month + 1));
		sp_round.setSelection(conf.autoFillItem.getRoundIndex());
		tvMatchCourt.setText(arr_court[conf.autoFillItem.getCourtIndex()]);
		tvMatchLevel.setText(arr_level[conf.autoFillItem.getLevelIndex()]);
		tvMatchRegion.setText(arr_region[conf.autoFillItem.getRegionIndex()]);
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
		arr_year = new String[20];
		for (int n = 0; n < 20; n++) {
			arr_year[n] = "" + (n + 2010);
		}
	}

	private void initView() {

		ArrayAdapter<String> spinnerAdapter;

		et_rankp1 = (EditText) findViewById(R.id.insert_et_rank1);
		et_seedp1 = (EditText) findViewById(R.id.insert_et_seed1);

		tvCompetitor = (TextView) findViewById(R.id.insert_et_compname);

		tvCompetitorCountry = (TextView) findViewById(R.id.insert_et_country);
		et_rank = (EditText) findViewById(R.id.insert_et_rank);
		et_seed = (EditText) findViewById(R.id.insert_et_seed);

		tvMatch = (TextView) findViewById(R.id.insert_et_matchname);
		tvMatchCountry = (TextView) findViewById(R.id.insert_et_matchcountry);
		tvMatchCity = (TextView) findViewById(R.id.insert_et_city);
		et_winner = (EditText) findViewById(R.id.insert_et_winner);
		et_score = (EditText) findViewById(R.id.insert_et_score);
		ivChangeMatch = (ImageView) findViewById(R.id.insert_iv_change_match);
		ivChangeMatch.setOnClickListener(this);
		ivChangePlayer = (ImageView) findViewById(R.id.insert_iv_change_competitor);
		ivChangePlayer.setOnClickListener(this);
		tvMatchMonth = (TextView) findViewById(R.id.insert_spinner_month);
		tvMatchCourt = (TextView) findViewById(R.id.insert_spinner_court);
		tvMatchLevel = (TextView) findViewById(R.id.insert_spinner_level);
		tvMatchRegion = (TextView) findViewById(R.id.insert_spinner_region);
		sp_year = (Spinner) findViewById(R.id.insert_spinner_year);
		sp_round = (Spinner) findViewById(R.id.insert_spinner_round);

		spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr_year);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sp_year.setAdapter(spinnerAdapter);
		sp_year.setOnItemSelectedListener(spinnerListener);
		spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr_round);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_round.setAdapter(spinnerAdapter);
		sp_round.setOnItemSelectedListener(spinnerListener);
	}

	private void showFromServerFile() {

		Configuration conf = Configuration.getInstance();
		et_winner.setText(MultiUserManager.getInstance().getCurrentUser().getDisplayName());
		cur_year = conf.index_year;
		sp_year.setSelection(cur_year);
		tvMatchMonth.setText(String.valueOf(conf.index_month + 1));

		//v4.7 change: load from preference
		conf.loadFromPreference(this);
		tvMatch.setText(conf.autoFillItem.getMatch());
		tvMatchCountry.setText(conf.autoFillItem.getCountry());
		tvMatchCity.setText(conf.autoFillItem.getCity());
		cur_round = conf.autoFillItem.getRoundIndex();
		sp_round.setSelection(cur_round);
		tvMatchCourt.setText(arr_court[conf.autoFillItem.getCourtIndex()]);
		tvMatchLevel.setText(arr_level[conf.autoFillItem.getLevelIndex()]);
		tvMatchRegion.setText(arr_region[conf.autoFillItem.getRegionIndex()]);
	}
	private class SpinnerListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
								   int position, long id) {
			if (parent == sp_year) {
				cur_year = position;
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
		record.setCity(tvMatchCity.getText().toString());
		record.setCompetitor(tvCompetitor.getText().toString());
		record.setCptCountry(tvCompetitorCountry.getText().toString());
		record.setMatchCountry(tvMatchCountry.getText().toString());
		record.setCourt(tvMatchCourt.getText().toString());
		record.setLevel(tvMatchLevel.getText().toString());
		record.setMatch(tvMatch.getText().toString());
		record.setRegion(tvMatchRegion.getText().toString());
		record.setRound(arr_round[cur_round]);
		record.setScore(et_score.getText().toString());
		int cur_month = 0;
		try {
			cur_month = Integer.parseInt(tvMatchMonth.getText().toString()) - 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
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

		String name = tvCompetitor.getText().toString();
		record.setCompetitor(name);
		RecordService service = new RecordService(this);
		return service.insert(record);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CHANGE_MATCH) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				tvMatch.setText(bundle.getString("name"));
				tvMatchCountry.setText(bundle.getString("country"));
				tvMatchLevel.setText(bundle.getString("level"));
				tvMatchCourt.setText(bundle.getString("court"));
				tvMatchRegion.setText(bundle.getString("region"));
				tvMatchCity.setText(bundle.getString("city"));
				tvMatchMonth.setText(String.valueOf(bundle.getInt("month")));
			}
		}
		if (requestCode == REQUEST_CHANGE_PLAYER) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				tvCompetitor.setText(bundle.getString("name"));
				tvCompetitorCountry.setText(bundle.getString("country"));
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
