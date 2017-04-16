package com.king.khcareer.model.sql.player;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.king.khcareer.model.sql.player.bean.SafeInfor;
import com.king.khcareer.model.sql.player.interfc.DatabaseAccess;
import com.king.khcareer.model.sql.player.interfc.SafeInforDAO;

public class SafeInforDAOImp implements SafeInforDAO {

	private DatabaseAccess sqlite;
	
	public SafeInforDAOImp() {
		sqlite = new SQLiteDB();
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
		MySQLHelper helper = MySQLHelper.getInstance();
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
		cursor.close();
		return safeInfor;
	}

}
