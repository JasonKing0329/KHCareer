package com.king.khcareer.login;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.king.khcareer.model.sql.player.interfc.DatabaseAccess;
import com.king.khcareer.model.sql.player.DatabaseStruct;
import com.king.khcareer.model.sql.player.MySQLHelper;
import com.king.khcareer.model.sql.player.SQLiteDB;
import com.king.khcareer.model.sql.player.bean.SafeInfor;
import com.king.khcareer.model.sql.player.SafeInforDAOImp;

public class UserLoginService {

	private SafeInfor safeInfor;
	private boolean isQueried = false;
	
	public UserLoginService() {
		safeInfor = SafeInfor.getInstance();
	}
	
	public String getPassword() {
		if (!isQueried) {
			query();
		}
		return safeInfor.getPassword();
	}

	public String getQuestion() {
		if (!isQueried) {
			query();
		}
		return safeInfor.getQuestion();
	}

	public String getAnswer() {
		if (!isQueried) {
			query();
		}
		return safeInfor.getAnswer();
	}

	public boolean checkPassword(String pwd) {
		
		return getPassword().equals(pwd);
	}
	
	public boolean checkAnswer(String answer) {
		
		return getAnswer().equals(answer);
	}

	public void init() {

		DatabaseAccess sqlite = new SQLiteDB();
		if (!sqlite.isTableExisted(DatabaseStruct.TABLE_PWD, DatabaseStruct.TABLE_PWD_COL)) {
			sqlite.createTable(DatabaseStruct.TABLE_PWD, DatabaseStruct.TABLE_PWD_PARAM);
			String[] values = {"0", "123", "i turn to u", "christina", ""};
			sqlite.insert(DatabaseStruct.TABLE_PWD, DatabaseStruct.TABLE_PWD_COL
					, values);
			isQueried = true;
			safeInfor.setId(0);
			safeInfor.setPassword(values[1]);
			safeInfor.setQuestion(values[2]);
			safeInfor.setAnswer(values[3]);
			safeInfor.setOther("");
		}
		if (!sqlite.isTableExisted(DatabaseStruct.TABLE_CONF, DatabaseStruct.TABLE_CONF_COL)) {
			sqlite.createTable(DatabaseStruct.TABLE_CONF, DatabaseStruct.TABLE_CONF_PARAM);
			String[] values = {"0", "zh"};
			sqlite.insert(DatabaseStruct.TABLE_CONF, DatabaseStruct.TABLE_CONF_COL, values);
		}
	}
	
	public String getLanguage() {
		
		String result = "zh";
		MySQLHelper helper = MySQLHelper.getInstance();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(DatabaseStruct.TABLE_CONF, DatabaseStruct.TABLE_CONF_COL
				, "id=?", new String[]{"0"}, null, null, null);
		if (cursor.moveToNext()) {
			result = cursor.getString(DatabaseStruct.COL_LANGUAGE);
		}
		cursor.close();
		return result;
	}
	
	private void query() {

		safeInfor = new SafeInforDAOImp().get(0);
	}
}
