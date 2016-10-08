package com.king.mytennis.service;

import android.os.Build;

public class Application {

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

	public static boolean DEBUG = false;
}
