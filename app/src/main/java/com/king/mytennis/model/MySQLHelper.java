package com.king.mytennis.model;

import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.utils.DebugLog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLHelper extends SQLiteOpenHelper{

	private static final String TAG = "MySQLHelper";
	private static MySQLHelper sqlHelper = null;
	private static MySQLHelper publicHelper = null;
	private static String currentDatabase = DatabaseStruct.DATABASE;
	private static int version = 2;

	private MySQLHelper(Context context, String database, CursorFactory factory,
						int version) {
		super(context, database, factory, version);

	}

	private MySQLHelper(Context context, String database) {
		this(context, database, null, version);
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

	public static MySQLHelper getPublicInstance(Context context) {
		if (publicHelper == null) {
			publicHelper = new MySQLHelper(context, MultiUserManager.getInstance().getPublicDatabase());
		}
		return publicHelper;
	}

	public static void closeHelper() {
		if (sqlHelper != null) {
			sqlHelper.close();
			sqlHelper = null;
		}
		if (publicHelper != null) {
			publicHelper.close();
			publicHelper = null;
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		DebugLog.e("");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		DebugLog.e("");
	}
}
