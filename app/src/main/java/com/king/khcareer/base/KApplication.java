package com.king.khcareer.base;

import android.app.Application;
import android.os.Build;

import com.king.khcareer.model.sql.player.MySQLHelper;

public class KApplication extends Application {

	/**
	 * 行为约束：
	 * 1. 程序的folder目录遵从：与文件夹同级的只能是文件夹不能是image文件
	 * 也即文件夹包含下一级内容要么全是文件及要么全是image文件
	 * 该约束影响Thumbfolder view, wholerandommanager规则
	 */

	public static int getSDKVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * use number 21 to mark, make codes runs well under android L
	 * @return
	 */
	public static boolean isLollipop() {
		return Build.VERSION.SDK_INT >= 21;//Build.VERSION_CODES.L;
	}

	/**
	 * use number 23 to mark, make codes runs well under android L
	 * @return
	 */
	public static boolean isM() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

	public static boolean DEBUG = false;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public void initSqlHelper() {
		MySQLHelper.initAppContext(this);
	}
}
