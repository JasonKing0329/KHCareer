package com.king.mytennis.model;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.king.mytennis.interfc.NamePinyinDAO;

public class NamePinyinPool implements NamePinyinDAO {

	private HashMap<String, String> map;
	private Context mContext;
	
	public NamePinyinPool(Context context) {
		mContext = context;
	}
	
	@Override
	public void add(String name, String pinyin) {

		SQLiteDB sqLite = new SQLiteDB(mContext);
		String[] values = {name, pinyin};
		sqLite.insert(DatabaseStruct.TABLE_NAME_PINYIN, DatabaseStruct.TABLE_NAME_PINYIN_COL_TOINSERT, values);
	}

	@Override
	public HashMap<String, String> getNamePinyinMap() {
		if (map == null) {
			map = new HashMap<String, String>();
			queryMap();
		}
		return map;
	}

	private void queryMap() {
		MySQLHelper helper = MySQLHelper.getInstance(mContext);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(DatabaseStruct.TABLE_NAME_PINYIN, DatabaseStruct.TABLE_NAME_PINYIN_COL
				, null, null, null, null, null);
		String name = null, pinyin = null;
		while (cursor.moveToNext()) {
			name = cursor.getString(DatabaseStruct.COL_NAME_PINYIN_NAME);
			pinyin = cursor.getString(DatabaseStruct.COL_NAME_PINYIN_PINYIN);
			map.put(name, pinyin);
		}
		db.close();
	}

}
