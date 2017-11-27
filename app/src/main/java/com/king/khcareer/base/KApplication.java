package com.king.khcareer.base;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import com.king.khcareer.model.PubProviderHelper;
import com.king.khcareer.model.sql.player.MySQLHelper;
import com.king.khcareer.utils.DebugLog;

import java.util.ArrayList;
import java.util.List;

public class KApplication extends Application {

	private static KApplication instance;

	private List<Activity> activityList;

	private Activity currentActivity;

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

	public static KApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		instance = this;
		super.onCreate();
		activityList = new ArrayList<>();

		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				DebugLog.e(activity.getClass().getName());
				activityList.add(activity);
			}

			@Override
			public void onActivityStarted(Activity activity) {

			}

			@Override
			public void onActivityResumed(Activity activity) {
				DebugLog.e(activity.getClass().getName());
				currentActivity = activity;
			}

			@Override
			public void onActivityPaused(Activity activity) {

			}

			@Override
			public void onActivityStopped(Activity activity) {

			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

			}

			@Override
			public void onActivityDestroyed(Activity activity) {
				DebugLog.e(activity.getClass().getName());

				// 最后一个activity结束
				if (activityList.size() == 1 && activityList.get(0) == activity) {
					onLastActivityFinished(activity);
				}

				activityList.remove(activity);

				if (activity == currentActivity) {
					currentActivity = null;
				}
				if (activityList.size() == 0) {
					currentActivity = null;
				}
			}
		});
	}

	private void onLastActivityFinished(Activity activity) {
		PubProviderHelper.close();
		stopAllService(activity);
	}

	private void stopAllService(Activity from) {
	}

	public void initSqlHelper() {
		MySQLHelper.initAppContext(this);
	}
}
