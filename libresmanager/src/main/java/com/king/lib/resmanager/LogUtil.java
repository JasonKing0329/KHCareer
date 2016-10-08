package com.king.lib.resmanager;

import android.util.Log;

/**
 * @author JingYang
 * @version create time：2016-1-12 下午5:20:30
 *
 */
public class LogUtil {

	private static final String TAG = "LibResManager";

	public static void logE(String msg) {
		Log.e(TAG, msg);
	}
	public static void logD(String msg) {
		Log.d(TAG, msg);
	}
	public static void logI(String msg) {
		Log.i(TAG, msg);
	}
}
