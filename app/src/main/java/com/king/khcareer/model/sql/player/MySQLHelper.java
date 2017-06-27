package com.king.khcareer.model.sql.player;

import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.utils.DebugLog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLHelper extends SQLiteOpenHelper{

	private static final String TAG = "MySQLHelper";
	private static MySQLHelper sqlHelper = null;
	private static String currentDatabase = DatabaseStruct.DATABASE;
	private static int version = 5;

	private static Context appContext;

	private MySQLHelper(Context context, String database, CursorFactory factory,
						int version) {
		super(context, database, factory, version);

	}

	private MySQLHelper(Context context, String database) {
		this(context, database, null, version);
	}

	public static void initAppContext(Context context) {
		appContext = context;
	}
	/**
	 * v5.4 add multiuser module, need create new connection to different database, need close previous
	 * @return
	 */
	public static MySQLHelper getInstance() {
		String targetDb = MultiUserManager.getInstance().getTargetDatabase();
		Log.d(TAG, "getInstance " + targetDb);
		if (targetDb.equals(currentDatabase)) {
			if (sqlHelper == null) {
				Log.d(TAG, "getInstance new " + targetDb);
				sqlHelper = new MySQLHelper(appContext, targetDb);
				currentDatabase = targetDb;
			}
		}
		else {
			if (sqlHelper != null) {
				Log.d(TAG, "getInstance close " + currentDatabase);
				sqlHelper.close();
			}
			Log.d(TAG, "getInstance new " + targetDb);
			sqlHelper = new MySQLHelper(appContext, targetDb);
			currentDatabase = targetDb;
		}
		return sqlHelper;
	}

	public static MySQLHelper getInstance(MultiUser user) {
		return new MySQLHelper(appContext, MultiUserManager.getInstance().getDatabase(user));
	}

	public static void closeHelper() {
		if (sqlHelper != null) {
			sqlHelper.close();
			sqlHelper = null;
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		DebugLog.e("");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		DebugLog.e("oldVersion=" + oldVersion + ", newVersion=" + newVersion);
		if (oldVersion <= 2 && newVersion == 3) {
			db.execSQL("CREATE TABLE IF NOT EXISTS rank_final(_id INTEGER PRIMARY KEY AUTOINCREMENT, _year INTEGER, _rank INTEGER)");
		}
		if (oldVersion < 5 && newVersion == 5) {
			db.execSQL("DROP VIEW h2hview");
			db.execSQL("CREATE VIEW h2hview as " +
					"select id, competitor, competitor_country, count(*) as total" +
					", sum(case when iswinner='_user' and score!='W/O' then 1 else 0 end) as win " +
					", sum(case when iswinner='_user' then 0 else 1 end) as lose " +
					"from record group by competitor order by total desc");
		}
	}
}
