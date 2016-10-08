package com.king.mytennis.view;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.Record;
import com.king.mytennis.service.RecordService;

/**
 * 初始化使用时new之后，先另起后台线程执行super.initData方法，前台出现等待对话框
 * 完毕后再执行本类的show()方法。
 * 只初始化一次，以后启动对话框直接调用reshow()方法
 * @author king
 *
 */
@Deprecated
public class InsertDialog extends InsertOrUpdateDialog implements DialogInterface.OnClickListener {

	private DialogInterface dlgToDestory=null;

	public InsertDialog(ManagerActivity userActivity) {
		super(userActivity);
	}

	public void show() {

		super.initView();
		showFromServerFile();

		AlertDialog.Builder dlg=new AlertDialog.Builder(userActivity);
		dlg.setTitle(R.string.insert_title);
		dlg.setView(dialog_view).setPositiveButton(R.string.ok, this);
		dlg.setNeutralButton(R.string.insert_next, this);
		dlg.setNegativeButton(R.string.cancel, this);
		dlg.show();
	}

	public void reshow() {
		show();
	}

	private void showFromServerFile() {

		//String names[] = null;// = rdb.queryColumnNoRepeat("competitor");
		//ArrayAdapter<String> cmpt_adapter=
		//	new ArrayAdapter<String>(userActivity, android.R.layout.simple_dropdown_item_1line, names);
		// actv_compname.setAdapter(cmpt_adapter);

		//names = rdb.queryColumnNoRepeat("match");
		//ArrayAdapter<String> match_adapter=
		//	new ArrayAdapter<String>(userActivity, android.R.layout.simple_dropdown_item_1line, names);
		//actv_matchname.setAdapter(match_adapter);
		Configuration conf = Configuration.getInstance();
		actv_matchname.setText(conf.autoFillItem.getMatch());

		et_matchcountry.setText(conf.autoFillItem.getCountry());
		et_city.setText(conf.autoFillItem.getCity());
		et_winner.setText("king");
		cur_year = conf.index_year;
		sp_year.setSelection(cur_year);
		cur_month = conf.index_month;
		sp_month.setSelection(cur_month);
		cur_round = conf.autoFillItem.getRoundIndex();
		sp_round.setSelection(cur_round);
		cur_court = conf.autoFillItem.getCourtIndex();
		sp_court.setSelection(cur_court);
		cur_level = conf.autoFillItem.getLevelIndex();
		sp_level.setSelection(cur_level);
		cur_region = conf.autoFillItem.getRegionIndex();
		sp_region.setSelection(cur_region);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == AlertDialog.BUTTON_POSITIVE) {
			insert();

			destoryDialog(dialog);
		}
		else if (which == AlertDialog.BUTTON_NEUTRAL) {
			insert();
			//当按下back键时若已按过next，则这时back键也不能撤销dialog
			//因此这里接受DialogInterface参数以销毁
			Configuration conf = Configuration.getInstance();
			dlgToDestory=dialog;
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
			et_winner.setText("king");
			sp_year.setSelection(conf.index_year);
			sp_month.setSelection(conf.index_month);
			sp_round.setSelection(conf.autoFillItem.getRoundIndex());
			sp_court.setSelection(conf.autoFillItem.getCourtIndex());
			sp_level.setSelection(conf.autoFillItem.getLevelIndex());
			sp_region.setSelection(conf.autoFillItem.getRegionIndex());
			keepDialog(dialog);
		}
		if (which == AlertDialog.BUTTON_NEGATIVE) {
			destoryDialog(dialog);
		}
	}

	private void keepDialog(DialogInterface dialog) {

		try {
			Field field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void destory() throws NullPointerException{//当按下back键时若已按过next，则这时back键也不能撤销dialog因此要另写销毁
		if (dlgToDestory==null) {
			throw new NullPointerException("dialog当前可销毁,dlgToDestory为空");
		}
		else
			destoryDialog(dlgToDestory);
	}

	public void onBackPressed() {

		try {
			destory();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 点击确认添加后组装record，修改config的最近添加记录(只是组装，持久化在后面一步)，以及调用recordService完成添加记录
	 */
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
		if (et_winner.getText().toString().toLowerCase().matches("king")) {
			//record.setWinner(true);
		}
		//else record.setWinner(false);
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
//		conf.city = record.getCity();
//		conf.index_court = cur_court;
//		conf.index_level = cur_level;
//		conf.index_month = cur_month;
//		conf.index_region = cur_region;
//		conf.index_round = cur_round;
//		conf.index_year = cur_year;
//		conf.match = record.getMatch();
//		conf.mCountry = record.getMatchCountry();

		RecordService service = new RecordService(userActivity);
		return service.insert(record);
	}

}
