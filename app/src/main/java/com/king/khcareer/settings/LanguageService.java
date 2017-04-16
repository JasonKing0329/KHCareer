/**
 * 切换中文LanguageService.changeLanguage(userActivity, Locale.CHINESE);
 * 切换中文LanguageService.changeLanguage(userActivity, Locale.ENGLISH);
 */
package com.king.khcareer.settings;

import java.util.Locale;

import com.king.mytennis.view.R;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

public class LanguageService {

	private static String mainActvityLanguage;

	public static void init(Context context) {

		String[] lans = context.getResources().getStringArray(R.array.languageChoices);
		String def_language = LanguageService.getPrefLanguage(context);
		if (def_language.equals(lans[0])) {
			changeLanguage(context, Locale.CHINESE);
		}
		else {
			changeLanguage(context, Locale.ENGLISH);
		}
	}

	public static void changeLanguage(Context context, Locale locale) {

		Resources resources = context.getResources();
		Configuration conf = resources.getConfiguration();
		conf.locale = locale;
		resources.updateConfiguration(conf, resources.getDisplayMetrics());
	}

	public static String getMainActivityLanguage(Context context) {

		if (mainActvityLanguage == null) {
			mainActvityLanguage = SettingProperty.getDefaultLanguage(context);
		}
		return mainActvityLanguage;
	}

	public static void updateMainActivityLanguage(String language) {

		mainActvityLanguage = language;
	}

	public static String getPrefLanguage(Context context) {
		return SettingProperty.getDefaultLanguage(context);
	}
}
