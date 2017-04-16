package com.king.mytennis.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.king.app.browser.interaction.MyBrowserConstants;
import com.king.khcareer.base.BaseActivity;
import com.king.mytennis.glory.GloryController;
import com.king.mytennis.model.AtpSinaRank;
import com.king.mytennis.net.html.ATPRankParser;
import com.king.mytennis.net.html.SinaParser;
import com.king.mytennis.net.html.Parser.OnParseListener;
import com.king.khcareer.base.KApplication;
import com.king.khcareer.utils.CalendarService;
import com.king.khcareer.settings.CacheController;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class RankActivity extends BaseActivity implements OnParseListener, OnClickListener {

	private boolean enableSinaParser = false;
	private ListView listView;
	private RankListAdapter rankListAdapter;
	private ProgressDialog progressDialog;
	private SinaParser sinaParser;
	private List<AtpSinaRank> rankList;
	private TextView noDataView;

	private ATPRankParser atpRankParser;

	private Spinner yearSpinner, monthSpinner, daySpinner;
	private ImageView searchButton, refreshButton;
	private boolean hasInsertedkingScore = false;//仅插入到最新排名中

	private URLListener urlListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (KApplication.isLollipop()) {
			setContentView(R.layout.rank_main_l);
		}
		else {
			setContentView(R.layout.rank_main);
		}
		listView = (ListView) findViewById(R.id.rank_main_list);
		noDataView = (TextView) findViewById(R.id.rank_no_data);
		yearSpinner = (Spinner) findViewById(R.id.rank_spinner_date_year);
		monthSpinner = (Spinner) findViewById(R.id.rank_spinner_date_month);
		daySpinner = (Spinner) findViewById(R.id.rank_spinner_date_day);

		searchButton = (ImageView) findViewById(R.id.rank_button_search);
		refreshButton = (ImageView) findViewById(R.id.rank_button_refresh);
		searchButton.setOnClickListener(this);
		refreshButton.setOnClickListener(this);

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getResources().getString(R.string.loading));
		progressDialog.show();

		initDateSpinner();

		if (enableSinaParser) {
			sinaParser = new SinaParser(this, this);
			sinaParser.getRankListFromCache();//get latest
		}
		else {
			atpRankParser = new ATPRankParser(this, this);
			atpRankParser.getRankListFromCache();
		}

		checkIfHasNewRank();
	}

	private void checkIfHasNewRank() {
		String filePath = CacheController.TEMP_RANK_ATP_FILE;
		if (enableSinaParser) {
			filePath = CacheController.TEMP_RANK_SINA_FILE;
		}
		File file = new File(filePath);
		long cacheDate = file.lastModified();
		Date cacheDay = new Date(cacheDate);

		Calendar calendar = Calendar.getInstance();
		int today = calendar.get(Calendar.DAY_OF_YEAR);
		int toyear = calendar.get(Calendar.YEAR);
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);

		calendar.setTime(cacheDay);
		int cacheday = calendar.get(Calendar.DAY_OF_YEAR);
		int cacheyear = calendar.get(Calendar.YEAR);

		boolean hasNewRank = false;
		if (weekday == Calendar.MONDAY) {
			if (today != cacheday) {
				hasNewRank = true;
			}
		}
		else {
			int latestMonday = CalendarService.getLatestMondayOfYear(cacheDate);
			if (cacheyear == toyear) {
				if (cacheday < latestMonday) {
					hasNewRank = true;
				}
			}
			else {
				hasNewRank = true;
			}
		}

		if (hasNewRank) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(null);
			builder.setMessage(getResources().getString(R.string.rank_cache_outofdate));
			builder.setPositiveButton(R.string.ok, null);
			builder.show();
		}
	}

	private class URLListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			String url = (String) view.getTag();
			if (url != null) {
				startBrowser(url);
			}
		}

	}

	private void startBrowser(String url) {
		Intent intent = getPackageManager().getLaunchIntentForPackage("com.king.app.browser");
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.putExtra(MyBrowserConstants.DATA_URL, url);
		intent.putExtra(MyBrowserConstants.DATA_NOCHECK, true);
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

		}
		return super.onKeyDown(keyCode, event);
	}

	private void initDateSpinner() {

		Calendar calendar = Calendar.getInstance();
		List<String> yearList = new ArrayList<String>();
		for (int i = calendar.get(Calendar.YEAR); i > 2006 ; i --) {
			yearList.add("" + i);
		}

		ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this
				, android.R.layout.simple_dropdown_item_1line, yearList);
		yearSpinner.setAdapter(yearAdapter);
		yearSpinner.setOnItemSelectedListener(spinnerListener);
		yearSpinner.setSelection(0);

		monthSpinner.setOnItemSelectedListener(spinnerListener);
		daySpinner.setOnItemSelectedListener(spinnerListener);

		yearSpinner.setDropDownWidth(getResources().getDimensionPixelSize(R.dimen.rank_spinner_dropdown_width));
		monthSpinner.setDropDownWidth(getResources().getDimensionPixelSize(R.dimen.rank_spinner_dropdown_width));
		daySpinner.setDropDownWidth(getResources().getDimensionPixelSize(R.dimen.rank_spinner_dropdown_width));
	}

	OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
								   int position, long id) {
			if (parent == yearSpinner) {
				Calendar calendar = Calendar.getInstance();
				List<String> monthList = new ArrayList<String>();
				if (position == 0) {//current year
					for (int i = calendar.get(Calendar.MONTH) + 1; i > 0; i --) {
						monthList.add("" + i);
					}
				}
				else {
					for (int i = 12; i > 0; i --) {
						monthList.add("" + i);
					}
				}
				ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(RankActivity.this
						, android.R.layout.simple_dropdown_item_1line, monthList);
				monthSpinner.setAdapter(monthAdapter);
			}
			else if (parent == monthSpinner) {
				List<String> dayList = new ArrayList<String>();
				Calendar calendar = Calendar.getInstance();
				int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
				int month = Integer.parseInt(monthSpinner.getSelectedItem().toString());
				if (calendar.get(Calendar.YEAR) == year
						&& calendar.get(Calendar.MONTH) ==  month - 1) {
					dayList = CalendarService.getMondayList(year, month, calendar.get(Calendar.DAY_OF_MONTH));
				}
				else {
					dayList = CalendarService.getMondayList(year, month, -1);
				}
				ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(RankActivity.this
						, android.R.layout.simple_dropdown_item_1line, dayList);
				daySpinner.setAdapter(dayAdapter);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	@SuppressWarnings("unchecked")
	@Override
	public void onParseOk(int requestCode, Object object) {
		if (object != null) {
			rankList = (List<AtpSinaRank>) object;
		}
		if (rankList == null || rankList.size() == 0) {
			noDataView.setVisibility(View.VISIBLE);
		}
		else {
			if (!hasInsertedkingScore) {//仅插入到最新排名中
				insertKingsScore();
				hasInsertedkingScore = true;
			}
			if (rankListAdapter == null) {
				rankListAdapter = new RankListAdapter(this, rankList);
				urlListener = new URLListener();
				rankListAdapter.setOnclickListener(urlListener);
				listView.setAdapter(rankListAdapter);
			}
			else {
				rankListAdapter.updateRankList(rankList);
				rankListAdapter.notifyDataSetChanged();
			}
			noDataView.setVisibility(View.GONE);
		}
		progressDialog.dismiss();
	}

	private void insertKingsScore() {
		GloryController controller = new GloryController();
		HashMap<String, Integer> data = controller.getRankData();
		if (data != null) {
			AtpSinaRank rank = new AtpSinaRank();
			int score = data.get("score");
			if (enableSinaParser) {
				rank.setCountry("中国");
				rank.setPlayer("King");
			}
			else {
				rank.setCountry("(CHN)");
				rank.setPlayer("H.King");
			}
			rank.setMatchNumber(data.get("matchNumber"));
			rank.setScore(formatScore(score));
			rank.setChange(data.get("rank"));

			AtpSinaRank temRank = null;
			for (int i = 0; i < rankList.size(); i ++) {
				temRank = rankList.get(i);
				if (score > parseScore(temRank.getScore())) {
					rank.setRank(i + 1);
					rank.setChange(rank.getChange() - i - 1);
					rankList.add(i, rank);
					notifyRankChanged(i + 1);
					break;
				}
			}
		}
	}

	private void notifyRankChanged(int i) {
		for (int n = i; n < rankList.size(); n ++) {
			rankList.get(n).setRank(n + 1);
		}
	}

	private int parseScore(String score) {
		if (score.contains(",")) {//score over than 1000
			String[] array = score.split(",");
			int t = Integer.parseInt(array[0].trim());
			int n = Integer.parseInt(array[1].trim());
			return t * 1000 + n;
		}
		return Integer.parseInt(score);
	}

	private String formatScore(int score) {
		if (score > 999) {
			StringBuffer buffer = new StringBuffer("" + score);
			if (score > 9999) {
				buffer.insert(2, ',');
			}
			else {
				buffer.insert(1, ',');
			}
			return buffer.toString();
		}
		return "" + score;
	}

	@Override
	public void onParseFail(int requestCode, Object object) {
		Toast.makeText(this, object.toString(), Toast.LENGTH_LONG).show();
		noDataView.setVisibility(View.VISIBLE);
		progressDialog.dismiss();
	}

	@Override
	public void onClick(View v) {
		if (v == searchButton) {
			Object obj = daySpinner.getSelectedItem();
			if (obj != null && obj.toString().length() != 0) {
				progressDialog.show();
				String day = obj.toString();
				String month = monthSpinner.getSelectedItem().toString();
				if (!enableSinaParser) {
					if (day.length() == 1) {
						day = "0" + day;
					}
					if (month.length() == 1) {
						month = "0" + month;
					}
				}
				String date = yearSpinner.getSelectedItem().toString() + "-"
						+ month + "-" + day;
				if (enableSinaParser) {
					sinaParser.getRankList(date);
				}
				else {
					atpRankParser.getRankList(date);
				}
			}
		}
		else if (v == refreshButton) {
			progressDialog.show();
			if (enableSinaParser) {
				sinaParser.getRankList(null);
			}
			else {
				atpRankParser.getRankList(null);
			}
		}
	}

}
