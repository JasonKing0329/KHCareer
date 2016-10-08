package com.king.mytennis.view.settings;

import com.king.mytennis.view.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingProperty {

	public static final int HTTP_METHOD_APACHE = 201;
	public static final int HTTP_METHOD_URL = 202;

	private static final String DEFAULT_LANGUAGE = "setting_default_language";
	private static final String DEFAULT_HTTP_METHOD = "setting_default_http_method";
	private static final String LOGIN_FINGERPRINT = "setting_login_fingerprint";
	private static final String FEATURE_MULTIFORM = "setting_feature_mult_form";
	private static final String SLIDING_ENABLE = "setting_slidingmenu_enable";
	private static final String SLIDING_MODE = "setting_slidingmenu_mode";
	private static final String DEFAULT_UI = "setting_default_ui";

	private static final String QUICK_ENTER_TIME = "setting_quickenter_time";
	private static final String QUICK_ENTER_MATCH = "setting_quickenter_match";
	private static final String QUICK_ENTER_PLAYER = "setting_quickenter_player";

	public static final int SLIDINGMENU_LEFT = 0;
	public static final int SLIDINGMENU_RIGHT = 1;
	public static final int SLIDINGMENU_TWOWAY = 2;
	
	public static int getHttpMethod(Context context) {
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String method = preferences.getString(DEFAULT_HTTP_METHOD, null);
		if (method != null) {
			String[] array = context.getResources().getStringArray(R.array.httpMethodChoices);
			if (method.equals(array[0])) {
				return HTTP_METHOD_APACHE;
			}
			else {
				return HTTP_METHOD_URL;
			}
		}
		return HTTP_METHOD_APACHE;
	}
	
	public static boolean isFingerPrintLogin(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(LOGIN_FINGERPRINT, false);
	}

	public static boolean isSupportMultiForm(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(FEATURE_MULTIFORM, false);
	}
	public static String getDefaultLanguage(Context context) {
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(DEFAULT_LANGUAGE, context.getResources().getStringArray(R.array.languageChoices)[0]);

	}
	public static boolean isSlidingEnable(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(SLIDING_ENABLE, false);
	}

	public static String getDefaultUiMode(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(DEFAULT_UI, context.getResources().getStringArray(R.array.uiChoices)[0]);
	}

	public static void setDefaultUiMode(Context context, String mode) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(DEFAULT_UI, mode);
		editor.commit();
	}

	public static int getSlidingMenuMode(Context context) {
		String[] modes = context.getResources().getStringArray(R.array.setting_slidingmenu_mode_value);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String mode = preferences.getString(SLIDING_MODE, modes[0]);
		if (mode.equals(modes[0])) {
			return SLIDINGMENU_LEFT;
		}
		else if (mode.equals(modes[1])) {
			return SLIDINGMENU_RIGHT;
		}
		else if (mode.equals(modes[2])) {
			return SLIDINGMENU_TWOWAY;
		}
		else {
			return SLIDINGMENU_LEFT;
		}
	}

	public static String getQuickEnterTimeMode(Context context, String defaultMode) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(QUICK_ENTER_TIME, defaultMode);
	}

	public static String getQuickEnterMatchMode(Context context, String defaultMode) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(QUICK_ENTER_MATCH, defaultMode);
	}

	public static String getQuickEnterPlayerMode(Context context, String defaultMode) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(QUICK_ENTER_PLAYER, defaultMode);
	}

	public static void setQuickEnterTimeMode(Context context,
			String value) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(QUICK_ENTER_TIME, value);
		editor.commit();
	}

	public static void setQuickEnterMatchMode(Context context,
			String value) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(QUICK_ENTER_MATCH, value);
		editor.commit();
	}

	public static void setQuickEnterPlayerMode(Context context,
			String value) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(QUICK_ENTER_PLAYER, value);
		editor.commit();
	}

}
