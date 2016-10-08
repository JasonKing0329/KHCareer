package com.king.mytennis.model;

import com.king.mytennis.multiuser.MultiUserManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLHelper extends SQLiteOpenHelper{

	private static final String TAG = "MySQLHelper";
	private static MySQLHelper sqlHelper = null;
	private static String currentDatabase = DatabaseStruct.DATABASE;

	private MySQLHelper(Context context, String database, CursorFactory factory,
						int version) {
		super(context, database, factory, version);

	}

	private MySQLHelper(Context context, String database) {
		this(context, database, null, 1);//version版本这里命名为1
	}

	/**
	 * v5.4 add multiuser module, need create new connection to different database, need close previous
	 * @param context
	 * @return
	 */
	public static MySQLHelper getInstance(Context context) {
		String targetDb = MultiUserManager.getInstance().getTargetDatabase();
		Log.d(TAG, "getInstance " + targetDb);
		if (targetDb.equals(currentDatabase)) {
			if (sqlHelper == null) {
				Log.d(TAG, "getInstance new " + targetDb);
				sqlHelper = new MySQLHelper(context, targetDb);
				currentDatabase = targetDb;
			}
		}
		else {
			if (sqlHelper != null) {
				Log.d(TAG, "getInstance close " + currentDatabase);
				sqlHelper.close();
			}
			Log.d(TAG, "getInstance new " + targetDb);
			sqlHelper = new MySQLHelper(context, targetDb);
			currentDatabase = targetDb;
		}
		return sqlHelper;
	}

	public static void closeHelper() {
		if (sqlHelper != null) {
			sqlHelper.close();
		}
	}
	////只有在SQLiteDatabase对象调用getR/W...时才会调用
	////并且前面的new MySQLHelper(this, "nameT")里面是一个全新的名字才会执行
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("helper oncreate");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
