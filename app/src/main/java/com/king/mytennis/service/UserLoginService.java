package com.king.mytennis.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.king.mytennis.interfc.DatabaseAccess;
import com.king.mytennis.model.DatabaseStruct;
import com.king.mytennis.model.MySQLHelper;
import com.king.mytennis.model.SQLiteDB;
import com.king.mytennis.model.SafeInfor;
import com.king.mytennis.model.SafeInforDAOImp;

public class UserLoginService {

	private SafeInfor safeInfor;
	private boolean isQueried = false;
	private Context context;
	
	public UserLoginService(Context context) {
		this.context = context;
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

		DatabaseAccess sqlite = new SQLiteDB(context);
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
		MySQLHelper helper = MySQLHelper.getInstance(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(DatabaseStruct.TABLE_CONF, DatabaseStruct.TABLE_CONF_COL
				, "id=?", new String[]{"0"}, null, null, null);
		if (cursor.moveToNext()) {
			result = cursor.getString(DatabaseStruct.COL_LANGUAGE);
		}
		return result;
	}
	
	private void query() {

		safeInfor = new SafeInforDAOImp(context).get(0);
	}
}
