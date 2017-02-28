/**
 * 配置信息，实现为单例
 */
package com.king.mytennis.model;

import java.io.File;
import java.io.Serializable;
import java.util.Locale;

import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.service.ExternalRecordTool;
import com.king.mytennis.view.settings.AutoFillItem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;

public class Configuration implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Configuration configuration;

	//before android 4.1
	//public static final String DEF_CONTENT = "/sdcard/myparadise/myApp/myTennis/";

	//after android4.1
	//htc /storage/ext_sd/
	//samsung /storage/extSdCard/
	public static final String DATABASE_CONTENT = "/data/data/com.king.mytennis.view/databases/";

	public static final String SDCARD = Environment.getExternalStorageDirectory().getPath();
	public static final String EXTERNAL_SDCARD_HTC = "/storage/ext_sd";
	public static final String EXTERNAL_SDCARD_SAMSUNG = "/storage/extSdCard";

	public static final String DEF_CONTENT = SDCARD + "/mytennis";
	public static final String IMG_PLAYER_BASE = DEF_CONTENT + "/img_player/";
	public static final String IMG_MATCH_BASE = DEF_CONTENT + "/img_match/";
	public static final String IMG_BK_BASE = DEF_CONTENT + "/img_bk/";
	public static final String IMG_DEFAULT_BASE = DEF_CONTENT + "/img_default/";
	public static final String IMG_PLAYER_HEAD = DEF_CONTENT + "/img_player/head/";
	public static final String HISTORY_BASE = DEF_CONTENT + "/history/";
	public static final String HISTORY_PLAYER_BASE = HISTORY_BASE + "/player/";
	public static final String DOWNLOAD_BASE = DEF_CONTENT + "/download";
	public static final String DOWNLOAD_IMAGE = DOWNLOAD_BASE + "/img";

	public static final String CONF_DIR = DEF_CONTENT + "/conf/";
	public static final String TEMP_DIR = DEF_CONTENT + "/temp/";
	public static final String TEMP_HTTP_DIR = TEMP_DIR + "http/";
	public static final String TEMP_IMAGE_DIR = TEMP_DIR + "image/";

	public static final String IMG_DIR = DEF_CONTENT + "/img";
	public static final String IMG_CARDBK_DIR = IMG_DIR + "/card_bk";

	public static final String CONF_FILE = "config.dat";
	public static final String DEF_IMG_PLAYER = "player_other.jpg";
	public static final String DEF_IMG_HARD = "hard.jpg";
	public static final String DEF_IMG_INNERHARD = "innerhard.jpg";
	public static final String DEF_IMG_CLAY = "clay.jpg";
	public static final String DEF_IMG_GRASS = "grass.jpg";

	public static final String GRANDSLAME_FILE = "grandslame.dat";
	public static final String SEASON_SCORE_FILE = "sscore.dat";
	public static final String SEASON_SCORE_FILE_HENRY = "sscore_henr.dat";
	public static final String SEASON_SCORE_FILE_FLAMENCO = "sscore_flam.dat";
	public static final String SEASON_SCORE_FILE_TIANQI = "sscore_tian.dat";
	public static final String RANK_FILE = "rank.dat";
	public static final String RANK_FILE_HENRY = "rank_henr.dat";
	public static final String RANK_FILE_FLAMENCO = "rank_flam.dat";
	public static final String RANK_FILE_TIANQI = "rank_tian.dat";

	public static final String MATCH_ORDER_FILE = "matchorder.dat";

	public static final String AUTOFILL_DIR = CONF_DIR + "autofill/";

	public static final String APP_UPDATE_DIR = CONF_DIR + "app/";

	public static final String PREF_INSERT_MATCH = "pref_insert_match";
	public static final String PREF_INSERT_COUNTRY = "pref_insert_country";
	public static final String PREF_INSERT_CITY = "pref_insert_city";
	public static final String PREF_INSERT_COURT = "pref_insert_court";
	public static final String PREF_INSERT_LEVEL = "pref_insert_level";
	public static final String PREF_INSERT_REGION = "pref_insert_region";
	public static final String PREF_INSERT_ROUND = "pref_insert_round";

	public static final String EXTEND_RES_DIR = CONF_DIR + "res/";
	public static final String EXTEND_RES_COLOR = EXTEND_RES_DIR + "colors.xml";
	public static final String EXTEND_RES_DIMEN = EXTEND_RES_DIR + "dimens.xml";
	public static final String ASSETS_RES_COLOR = "res/colors.xml";
	public static final String ASSETS_RES_DIMEN = "res/dimens.xml";

	public static boolean supportMultiAutoFill;
	public static boolean supportSetConfigDir = false;
	public static boolean supportUpload = false;

	public static String USERAGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 QIHU 360EE";

	public static String USERAGENT_MOBILE;

	public String DEF_BK;

	public int index_year;
	public int index_month;
	public AutoFillItem autoFillItem;

	private Configuration() {
		DEF_BK = "def";
		autoFillItem = new AutoFillItem();
		autoFillItem.setMatch("");
		autoFillItem.setCountry("");
		autoFillItem.setCity("");
	}

	public static Configuration getInstance() {

		if (configuration == null) {
			configuration = new Configuration();
		}
		return configuration;
	}
	/**
	 * 用于序列化中恢复对象
	 * @param conf
	 */
	public static void setInstance(Configuration conf) {
		if (conf == null) {
			configuration = getInstance();
		}
		else {
			configuration = conf;
		}
	}

	public void resetLatestInfor() {
		index_month = 0;
		index_year = 0;
	}

	public static void initialize(Context context) {
		createDir(new File(DEF_CONTENT));
		createDir(new File(IMG_BK_BASE));
		createDir(new File(IMG_DEFAULT_BASE));
		createDir(new File(IMG_MATCH_BASE));
		createDir(new File(IMG_PLAYER_BASE));
		createDir(new File(IMG_PLAYER_HEAD));
		createDir(new File(HISTORY_BASE));
		createDir(new File(HISTORY_PLAYER_BASE));
		createDir(new File(CONF_DIR));
		createDir(new File(AUTOFILL_DIR));
		createDir(new File(TEMP_DIR));
		createDir(new File(TEMP_HTTP_DIR));
		createDir(new File(TEMP_IMAGE_DIR));
		createDir(new File(DOWNLOAD_BASE));
		createDir(new File(DOWNLOAD_IMAGE));
		createDir(new File(EXTEND_RES_DIR));
		createDir(new File(APP_UPDATE_DIR));
		ExternalRecordTool.copyDbFromAssets(context, ExternalRecordTool.DATABASE);
		ExternalRecordTool.copyDbFromAssets(context, ExternalRecordTool.DATABASE_PLAYER);
		ExternalRecordTool.copyDbFromAssets(context, ExternalRecordTool.DATABASE_TIANQI);
		ExternalRecordTool.copyDbFromAssets(context, ExternalRecordTool.DATABASE_FLAMENCO);
		ExternalRecordTool.copyDbFromAssets(context, ExternalRecordTool.DATABASE_HENRY);
		ExternalRecordTool.copyPublicDbFromAssets(context, ExternalRecordTool.DATABASE_PUBLIC);
		ExternalRecordTool.copyResFromAssets(context, ASSETS_RES_COLOR, EXTEND_RES_COLOR);
		ExternalRecordTool.copyResFromAssets(context, ASSETS_RES_DIMEN, EXTEND_RES_DIMEN);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		supportMultiAutoFill = preferences.getBoolean("setting_feature_mult_form", true);
		MultiUserManager.getInstance().loadUsers(context);
		USERAGENT_MOBILE = getLocalUserAgent();
	}

	private static void createDir(File file) {
		if (!file.exists()) {
			file.mkdir();
		}
	}

	public void createPreference(Context context, AutoFillItem item) {
		autoFillItem = item;

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PREF_INSERT_CITY, autoFillItem.getCity());
		editor.putString(PREF_INSERT_COUNTRY, autoFillItem.getCountry());
		editor.putString(PREF_INSERT_MATCH, autoFillItem.getMatch());
		editor.putInt(PREF_INSERT_COURT, autoFillItem.getCourtIndex());
		editor.putInt(PREF_INSERT_LEVEL, autoFillItem.getLevelIndex());
		editor.putInt(PREF_INSERT_REGION, autoFillItem.getRegionIndex());
		editor.putInt(PREF_INSERT_ROUND, autoFillItem.getRoundIndex());
		editor.commit();
	}

	public void loadFromPreference(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

		if (autoFillItem == null) {
			autoFillItem = new AutoFillItem();
			autoFillItem.setMatch("");
			autoFillItem.setCountry("");
			autoFillItem.setCity("");
		}
		autoFillItem.setMatch(preferences.getString(PREF_INSERT_MATCH, ""));
		autoFillItem.setCountry(preferences.getString(PREF_INSERT_COUNTRY, ""));
		autoFillItem.setCity(preferences.getString(PREF_INSERT_CITY, ""));
		autoFillItem.setCourtIndex(preferences.getInt(PREF_INSERT_COURT, 0));
		autoFillItem.setLevelIndex(preferences.getInt(PREF_INSERT_LEVEL, 0));
		autoFillItem.setRegionIndex(preferences.getInt(PREF_INSERT_REGION, 0));
		autoFillItem.setRoundIndex(preferences.getInt(PREF_INSERT_ROUND, 0));
	}

	private static String getLocalUserAgent() {
		StringBuffer buffer = new StringBuffer();

		final String version = android.os.Build.VERSION.RELEASE;
		if (version.length() > 0) {
			if (Character.isDigit(version.charAt(0))) {
				buffer.append(version);
			}
			else {
				buffer.append("4.1.1");
			}
		}
		else {
			buffer.append("1.0");
		}
		buffer.append("; ");
		Locale locale = Locale.getDefault();
		final String language = locale.getLanguage();
		if (language != null) {
			buffer.append(convertObsoleteLanguageCodeToNew(language));
			String country = locale.getCountry();
			if (country != null) {
				buffer.append("-");
				buffer.append(country.toLowerCase());
			}
		}
		else {
			buffer.append("en");
		}
		buffer.append(";");
		if ("REL".equals(android.os.Build.VERSION.CODENAME)) {
			String model = Build.MODEL;
//			String tempModelName = null;
//			if (tempModelName != null) {
//				buffer.append(" ");
//				buffer.append(tempModelName);
//			}
			buffer.append(" ");
			buffer.append(model);
		}
		String id = Build.ID;
		if (id.length() > 0) {
			buffer.append(" Build/");
			buffer.append(id);
		}
		String mobile = "Mobile ";
		String base = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 %s Safara/534.30";
		return String.format(base, buffer, mobile);
	}

	private static String convertObsoleteLanguageCodeToNew(String lan) {
		if (lan == null) {
			return null;
		}
		if ("iw".equals(lan)) {
			return "he";
		}
		else if ("in".equals(lan)) {
			return "id";
		}
		else if ("ji".equals(lan)) {
			return "yi";
		}
		return lan;
	}

}
