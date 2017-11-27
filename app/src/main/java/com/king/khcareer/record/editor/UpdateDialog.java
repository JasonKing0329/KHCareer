package com.king.khcareer.record.editor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.record.RecordService;
import com.king.khcareer.home.classic.ManagerActivity;
import com.king.mytennis.view.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 初始化使用时new之后，先另起后台线程执行super.initData方法，前台出现等待对话框
 * 完毕后再执行本类的show()方法。
 * 只初始化一次，以后启动对话框直接调用reshow()方法
 * @author king
 *
 */
public class UpdateDialog extends InsertOrUpdateDialog implements DialogInterface.OnClickListener {

	private Record record;

	private OnUpdateListener onUpdateListener;

	public UpdateDialog(Context userActivity, Record record) {
		super(userActivity);
		this.record = record;
	}

	public void show() {

		super.initView();
		setCurPosInfor();

		AlertDialog.Builder dlg=new AlertDialog.Builder(userActivity);
		dlg.setTitle(R.string.update_title);
		dlg.setView(dialog_view).setPositiveButton(R.string.ok, this);
		dlg.setNegativeButton(R.string.cancel, this);
		dlg.show();
	}

	public void reshow(Record record) {

		this.record = record;
		super.initView();
		setCurPosInfor();
		show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		if (which == AlertDialog.BUTTON_POSITIVE) {
			updateList();
			if (onUpdateListener != null) {
				onUpdateListener.OnRecordUpdated(record);
			}
			destoryDialog(dialog);
		}
		else if (which == AlertDialog.BUTTON_NEGATIVE) {
			destoryDialog(dialog);
		}
	}

	private void updateList(){
		/**
		 * 修改选中条目，lookresult类调用，内部代码基本和insertToList一样
		 * 在dlg中调用
		 */
		int temp;
		try {
			temp = Integer.parseInt(et_rankp1.getText().toString());
		} catch (Exception e) {
			temp = 0;
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

		long lDate = System.currentTimeMillis();
		try {
			lDate=format.parse(record.getStrDate()).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		record.setLongDate(lDate);

		RecordService service = new RecordService();
		service.update(record);
		//MyRecord oldRecord=list.get(update_position);//不能这样，这样也只是得到引用，下面这句就更改了
		//list.get(update_position).updateTo(newRecord);
//		rdb.update(oldRecord, newRecord);
//		lookResult.refreshList();//刷新操作应该在本类对话框按下 确定 后执行
	}

	private void setCurPosInfor(){
		et_city.setText(record.getCity());
		actv_compname.setText(record.getCompetitor());
		et_matchcountry.setText(record.getMatchCountry());
		actv_matchname.setText(record.getMatch());
		et_playercountry.setText(record.getCptCountry());
		et_rank.setText(""+record.getCptRank());
		et_rankp1.setText(""+record.getRank());
		et_seed.setText(""+record.getCptSeed());
		et_seedp1.setText(""+record.getSeed());
		et_score.setText(""+record.getScore());

		String winner = record.getWinner();
		if (winner.equals(MultiUserManager.USER_DB_FLAG)) {
			winner = MultiUserManager.getInstance().getCurrentUser().getDisplayName();
		}
		et_winner.setText(winner);
		int i=0;
		for (;i<arr_court.length;i++){
			if (arr_court[i].matches(record.getCourt()))
				sp_court.setSelection(i);
		}
		for (i=0;i<arr_level.length;i++){
			if (arr_level[i].matches(record.getLevel()))
				sp_level.setSelection(i);
		}
		for (i=0;i<arr_region.length;i++){
			if (arr_region[i].matches(record.getRegion()))
				sp_region.setSelection(i);
		}
		for (i=0;i<arr_round.length;i++){
			if (arr_round[i].matches(record.getRound()))
				sp_round.setSelection(i);
		}
		int pos=record.getStrDate().indexOf("-");
		String year=record.getStrDate().substring(0,pos);
		String month=record.getStrDate().substring(pos+1, record.getStrDate().length());
		System.out.println("year="+year+"  month="+month);
		for (i=0;i<arr_year.length;i++){
			if (arr_year[i].matches(year))
				sp_year.setSelection(i);
		}
		for (i=0;i<arr_month.length;i++){
			if (arr_month[i].matches(month))
				sp_month.setSelection(i);
		}
	}

	public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
		this.onUpdateListener = onUpdateListener;
	}

	public interface OnUpdateListener {
		void OnRecordUpdated(Record record);
	}
}
