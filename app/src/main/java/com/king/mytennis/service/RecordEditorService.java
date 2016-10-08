package com.king.mytennis.service;

import com.king.mytennis.model.DatabaseStruct;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.model.SQLiteDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

public class RecordEditorService {

	public RecordEditorService() {

	}

	public Bitmap loadBackgound() {
		return new ImageFactory().getDefBackground();
	}

	public boolean isPlayerExisted(Context context, String name) {
		if (name == null || name.trim().length() == 0) {
			return false;
		}
		SQLiteOpenHelper helper = new SQLiteDB(context).getSQLHelper();
		SQLiteDatabase db = helper.getReadableDatabase();

		try {
			Cursor cursor = db.query(DatabaseStruct.TABLE_NAME_PINYIN
					, DatabaseStruct.TABLE_NAME_PINYIN_COL
					, DatabaseStruct.TABLE_NAME_PINYIN_COL[DatabaseStruct.COL_NAME_PINYIN_NAME] + " = ?"
					, new String[]{name}, null, null, null);
			if (cursor.moveToNext()){
				return true;
			}
		} catch (Exception e) {
			Log.i("MyTennis", "isPlayerExisted抛出异常：" + e.getMessage());
		}

		db.close();
		return false;
	}

	public boolean insertNamePinyin(Context context, String name, String pinyin) {

		if (name == null || name.trim().length() == 0) {
			return false;
		}
		if (pinyin == null || pinyin.trim().length() == 0) {
			return false;
		}
		//已设计为自增
		SQLiteDB sqLite = new SQLiteDB(context);
		String[] values = new String[DatabaseStruct.TABLE_NAME_PINYIN_COL.length];
		//values[DatabaseStruct.COL_ID] = "" + getNewId();
		values[DatabaseStruct.COL_NAME_PINYIN_NAME] = name;
		values[DatabaseStruct.COL_NAME_PINYIN_PINYIN] = pinyin;
		sqLite.insert(DatabaseStruct.TABLE_NAME_PINYIN, DatabaseStruct.TABLE_NAME_PINYIN_COL, values);
		return true;
	}
}
