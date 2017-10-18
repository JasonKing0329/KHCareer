package com.king.khcareer.settings;

import com.king.khcareer.base.KApplication;
import com.king.khcareer.common.config.Constants;
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
	private static final String PREF_HTTP_SERVER = "pref_http_server";

	private static final String QUICK_ENTER_TIME = "setting_quickenter_time";
	private static final String QUICK_ENTER_MATCH = "setting_quickenter_match";
	private static final String QUICK_ENTER_PLAYER = "setting_quickenter_player";

	public static final int SLIDINGMENU_LEFT = 0;
	public static final int SLIDINGMENU_RIGHT = 1;
	public static final int SLIDINGMENU_TWOWAY = 2;

	public static final String KEY_SORT_MATCH = "key_sort_match";
	public static final int VALUE_SORT_MATCH_WEEK = 0;
	public static final int VALUE_SORT_MATCH_NAME = 1;
	public static final int VALUE_SORT_MATCH_LEVEL = 2;

	public static final String KEY_SORT_PLAYER = "key_sort_player";
	public static final int VALUE_SORT_PLAYER_NAME = 0;
	public static final int VALUE_SORT_PLAYER_NAME_ENG = 1;
	public static final int VALUE_SORT_PLAYER_COUNTRY = 2;
	public static final int VALUE_SORT_PLAYER_AGE = 3;
	public static final int VALUE_SORT_PLAYER_CONSTELLATION = 4;

	public static final String KEY_GLORY_PAGE_INDEX = "key_glory_page_index";
	public static final String KEY_GLORY_TARGET_WIN = "key_glory_target_win";
	public static final String KEY_GLORY_CHAMPION_GROUP_MODE = "key_glory_champion_group_mode";
	public static final String KEY_GLORY_RUNNERUP_GROUP_MODE = "key_glory_runnerup_group_mode";

	public static final String KEY_PLAYER_MANAGE_CARD = "key_player_manage_card";
	public static final String KEY_MATCH_MANAGE_GRID = "key_match_manage_grid";

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

	/**
	 * http服务器站点
	 */
	public static String getServerBaseUrl(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String url = preferences.getString(PREF_HTTP_SERVER, "");
		return url;
	}

	/**
	 * match 默认排序模式
	 * @param context
	 * @param mode
     */
	public static void setMatchSortMode(Context context, int mode) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(KEY_SORT_MATCH, mode);
		editor.commit();
	}

	/**
	 * match 默认排序模式
	 * @param context
	 */
	public static int getMatchSortMode(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		int mode = preferences.getInt(KEY_SORT_MATCH, VALUE_SORT_MATCH_WEEK);
		return mode;
	}

	/**
	 * player 默认排序模式
	 * @param context
	 * @param mode
	 */
	public static void setPlayerSortMode(Context context, int mode) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(KEY_SORT_PLAYER, mode);
		editor.commit();
	}

	/**
	 * player 默认排序模式
	 * @param context
	 */
	public static int getPlayerSortMode(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		int mode = preferences.getInt(KEY_SORT_PLAYER, VALUE_SORT_PLAYER_NAME);
		return mode;
	}

	/**
	 * glory page
	 * @param context
	 */
	public static int getGloryPageIndex(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		int mode = preferences.getInt(KEY_GLORY_PAGE_INDEX, 0);
		return mode;
	}

	/**
	 * glory page
	 * @param context
	 * @param index
	 */
	public static void setGloryPageIndex(Context context, int index) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(KEY_GLORY_PAGE_INDEX, index);
		editor.commit();
	}

	/**
	 * glory target: select win
	 * @param context
	 */
	public static boolean isGloryTargetWin(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean mode = preferences.getBoolean(KEY_GLORY_TARGET_WIN, false);
		return mode;
	}

	/**
	 * glory target: select win
	 * @param context
	 * @param check
	 */
	public static void setGloryTargetWin(Context context, boolean check) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(KEY_GLORY_TARGET_WIN, check);
		editor.commit();
	}

	/**
	 * glory champion group mode
	 * @param context
	 */
	public static int getGloryChampionGroupMode(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		int mode = preferences.getInt(KEY_GLORY_CHAMPION_GROUP_MODE, Constants.GROUP_BY_ALL);
		return mode;
	}

	/**
	 * glory champion group mode
	 * @param context
	 * @param mode
	 */
	public static void setGloryChampionGroupMode(Context context, int mode) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(KEY_GLORY_CHAMPION_GROUP_MODE, mode);
		editor.commit();
	}

	/**
	 * glory runnerup group mode
	 * @param context
	 */
	public static int getGloryRunnerupGroupMode(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		int mode = preferences.getInt(KEY_GLORY_RUNNERUP_GROUP_MODE, Constants.GROUP_BY_ALL);
		return mode;
	}

	/**
	 * glory runnerup group mode
	 * @param context
	 * @param mode
	 */
	public static void setGloryRunnerupGroupMode(Context context, int mode) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(KEY_GLORY_RUNNERUP_GROUP_MODE, mode);
		editor.commit();
	}

	/**
	 * card type of player manage list
	 * @param cardMode
	 */
	public static void setPlayerManageCardMode(boolean cardMode) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KApplication.getInstance());
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(KEY_PLAYER_MANAGE_CARD, cardMode);
		editor.commit();
	}

	/**
	 * card type of player manage list
	 */
	public static boolean isPlayerManageCardMode() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KApplication.getInstance());
		boolean mode = preferences.getBoolean(KEY_PLAYER_MANAGE_CARD, false);
		return mode;
	}

	/**
	 * grid type of match manage list
	 * @param cardMode
	 */
	public static void setMatchManageGridMode(boolean cardMode) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KApplication.getInstance());
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(KEY_MATCH_MANAGE_GRID, cardMode);
		editor.commit();
	}

	/**
	 * grid type of match manage list
	 */
	public static boolean isMatchManageGridMode() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KApplication.getInstance());
		boolean mode = preferences.getBoolean(KEY_MATCH_MANAGE_GRID, false);
		return mode;
	}

}
