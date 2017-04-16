package com.king.khcareer.model.sql.player.interfc;

import android.database.sqlite.SQLiteOpenHelper;

public interface DatabaseAccess {

	public SQLiteOpenHelper getSQLHelper();
	public void setDatabase(String name);
	//public Cursor select(String table, String[] columns, String where, String[] whereArgs);
	public void insert(String table, String[] columns, String[] values);
	public void delete(String table, String where, String whereArgs[]);
	public void update(String table, String where, String whereArgs[], String[] columns, String[] newValues);
	public void createTable(String name, String params);
	public void dropTable(String name);
	public boolean isTableExisted(String name, String[] columns);
	public void closeHelper();
	public void clearTable(String name);
	public long getCurRecordSize();
}
