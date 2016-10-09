package com.king.mytennis.model;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.king.mytennis.interfc.H2HDAO;
import com.king.mytennis.interfc.RecordDAO;
import com.king.mytennis.multiuser.MultiUserManager;

public class H2HDAODB implements H2HDAO {

	private Context context;
	private String competitor;
	private int win = 0;
	private int lose = 0;
	private String details;
	private boolean isHandled;

	public H2HDAODB(Context context, String competitor) {
		this.context = context;
		this.competitor = competitor;
	}

	@Override
	public int getWin() {

		if (!isHandled) {
			handleList();
		}
		return win;
	}

	@Override
	public int getLose() {

		if (!isHandled) {
			handleList();
		}
		return lose;
	}

	@Override
	public String getH2HDetail() {

		if (!isHandled) {
			handleList();
		}
		return details;
	}

	private void handleList() {

		MySQLHelper helper = MySQLHelper.getInstance(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		String columns[] = DatabaseStruct.TABLE_RECORD_COL;
		Cursor cursor = db.query(DatabaseStruct.TABLE_RECORD, columns
				, columns[DatabaseStruct.COL_competitor] + "=?"
				, new String[]{competitor}, null, null, null);
		StringBuilder builder = new StringBuilder();
		while (cursor.moveToNext()) {

			String winner = cursor.getString(DatabaseStruct.COL_iswinner);
			String score = cursor.getString(DatabaseStruct.COL_score);
			//如果是赛前退赛不算作h2h
			if (score.equals(Constants.SCORE_RETIRE)) {
				continue;
			}
			if (MultiUserManager.USER_DB_FLAG.equals(winner)) {
				win++;
			}
			else {
				lose++;
			}

			builder.append(cursor.getString(DatabaseStruct.COL_date_str)).append("  ")
					.append(cursor.getString(DatabaseStruct.COL_match)).append("  ")
					.append(cursor.getString(DatabaseStruct.COL_round)).append("  ")
					.append(cursor.getString(DatabaseStruct.COL_iswinner))
					.append("\n").append("               ")
					.append(cursor.getString(DatabaseStruct.COL_court)).append("  ")
					.append(cursor.getString(DatabaseStruct.COL_score)).append("\n");
		}
		db.close();
		details = builder.toString();
		isHandled = true;
	}

	@Override
	public List<Record> getH2HList() {

		RecordDAO dao = new RecordDAOImp(context);
		String where = DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_competitor] + "=?";
		return dao.queryByWhere(where, new String[]{competitor});
	}

}