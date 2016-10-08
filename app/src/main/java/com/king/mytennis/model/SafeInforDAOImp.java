package com.king.mytennis.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.king.mytennis.interfc.DatabaseAccess;
import com.king.mytennis.interfc.SafeInforDAO;

public class SafeInforDAOImp implements SafeInforDAO {

	private DatabaseAccess sqlite;
	private Context mContext;
	
	public SafeInforDAOImp(Context context) {
		mContext = context;
		sqlite = new SQLiteDB(context);
	}
	@Override
	public void insert(SafeInfor inf) {

		String[] values = {""+inf.getId(), inf.getPassword()
				, inf.getQuestion(), inf.getAnswer(), inf.getOther()};
		sqlite.insert(DatabaseStruct.TABLE_PWD, DatabaseStruct.TABLE_PWD_COL, values);
	}

	@Override
	public void delete(SafeInfor inf) {
		
		sqlite.delete(DatabaseStruct.TABLE_PWD, "id=?", new String[]{""+inf.getId()});
	}

	@Override
	public void update(SafeInfor inf) {
		
		String[] values = {""+inf.getId(), inf.getPassword()
				, inf.getQuestion(), inf.getAnswer(), inf.getOther()};
		sqlite.update(DatabaseStruct.TABLE_PWD, "id=?", new String[]{""+inf.getId()}
			, DatabaseStruct.TABLE_PWD_COL, values);
	}

	@Override
	public SafeInfor get(int id) {
		
		SafeInfor safeInfor = SafeInfor.getInstance();
		MySQLHelper helper = MySQLHelper.getInstance(mContext);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(DatabaseStruct.TABLE_PWD, DatabaseStruct.TABLE_PWD_COL
				, "id=?", new String[]{""+id}, null, null, null);
		if (cursor.moveToNext()) {
			safeInfor.setId(cursor.getInt(DatabaseStruct.COL_ID));
			safeInfor.setPassword(cursor.getString(DatabaseStruct.COL_pwd));
			safeInfor.setQuestion(cursor.getString(DatabaseStruct.COL_question));
			safeInfor.setAnswer(cursor.getString(DatabaseStruct.COL_answer));
			safeInfor.setOther(cursor.getString(DatabaseStruct.COL_other));
		}
		db.close();
		return safeInfor;
	}

}
