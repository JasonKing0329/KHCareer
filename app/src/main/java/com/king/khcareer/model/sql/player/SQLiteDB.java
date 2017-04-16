package com.king.khcareer.model.sql.player;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.king.khcareer.model.sql.player.interfc.DatabaseAccess;
import com.king.khcareer.common.multiuser.MultiUser;

public class SQLiteDB implements DatabaseAccess {

	protected MySQLHelper sqlHelper;

	public SQLiteDB() {
		initSqlHelper();
	}

	public SQLiteDB(MultiUser user) {
		initSqlHelper(user);
	}

	protected void initSqlHelper() {
		sqlHelper = MySQLHelper.getInstance();
	}

	protected void initSqlHelper(MultiUser user) {
		sqlHelper = MySQLHelper.getInstance(user);
	}

	@Deprecated //本程序里暂时考虑全局只能使用mytennis一个数据库，因此这句以后看看再做打算
	@Override
	public void setDatabase(String name) {
		/*
		sqlHelper.close();
		sqlHelper = null;
		sqlHelper = MySQLHelper.getInstance(context, DatabaseStruct.DATABASE);
		*/
	}

	/*
	@Override
	public Cursor select(String table, String[] columns, String where,
			String[] whereArgs) {

		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		Cursor cursor = db.query(table, columns, where, whereArgs, null, null, null);
		return null;
	}
	*/

	@Override
	public void insert(String table, String[] columns, String[] values) {
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		for (int i = 0; i < columns.length; i++) {
			cv.put(columns[i], values[i]);
		}
		db.insert(table, null, cv);
	}

	@Override
	public void delete(String table, String where, String[] whereArgs) {
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		db.delete(table, where
				, whereArgs);
	}

	@Override
	public void update(String table, String where, String[] whereArgs,
					   String[] columns, String[] newValues) {
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		for (int i = 0; i < columns.length; i++) {
			cv.put(columns[i], newValues[i]);
		}
		db.update(table, cv, where
				, whereArgs);
	}

	@Override
	public void createTable(String name, String params) {
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		db.execSQL("CREATE TABLE IF NOT EXISTS " + name + params);
	}

	@Override
	public void dropTable(String name) {
		// TODO Auto-generated method stub

	}
	@Override
	public SQLiteOpenHelper getSQLHelper() {

		return sqlHelper;
	}
	@Override
	public boolean isTableExisted(String name, String[] columns) {

		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		try {
			Cursor cursor;
			cursor = db.query(name, columns, "id=?", new String[]{"0"}, null, null, null);
			int count = cursor.getCount();
			if (count > 0) {
				return true;
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	@Override
	public void closeHelper() {

		if (sqlHelper != null) {
			sqlHelper.close();
		}
	}

	@Override
	public void clearTable(String name) {
		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		//db.execSQL("TRUNCATE TABLE " + name);//sqlite用的是delete from
		db.execSQL("DELETE FROM " + name);
	}

	@Override
	public long getCurRecordSize() {

		long result = System.currentTimeMillis();
		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		Cursor cursor = db.query(DatabaseStruct.TABLE_RECORD
				, new String[]{"COUNT(*)"}, null, null, null, null, null);
		if (cursor.moveToNext()) {
			result = cursor.getInt(0);
		}
		cursor.close();
		return result;
	}

}
