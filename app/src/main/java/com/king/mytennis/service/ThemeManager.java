package com.king.mytennis.service;

import com.king.mytennis.view.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ThemeManager {

	public static final String THEME_KEY = "theme_value";
	
	private String[] themeKeys;
	private Context context;
	
	private int[] themeValues = new int[] {R.style.ActionBarBlue, R.style.ActionBarGreen
			, R.style.ActionBarLightGreen, R.style.ActionBarOrange, R.style.ActionBarDeepBlue, R.style.ActionBarPurple};
	private int[] themeDrawables = new int[] {R.drawable.theme_blue, R.drawable.theme_green, R.drawable.theme_lightgreen
			, R.drawable.theme_orange, R.drawable.theme_deepblue, R.drawable.theme_purple};
	
	public ThemeManager(Context context) {
		this.context = context;
		themeKeys = context.getResources().getStringArray(R.array.theme_key);
	}
	
	public String[] getThemes() {
		return themeKeys;
	}

	public int[] getThemesDrawables() {
		return themeDrawables;
	}
	
	public int getTheme(String key) {
		int theme = 0;
		for (int i = 0; i < themeKeys.length; i ++) {
			if (key.equals(themeKeys[i])) {
				theme = themeValues[i];
				break;
			}
		}
		return theme;
	}

	public int getTheme(int keyIndex) {
		return themeValues[keyIndex];
	}
	
	public static void init(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = preferences.getString(THEME_KEY, null);
		if (value == null) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString(THEME_KEY, context.getResources().getStringArray(R.array.theme_key)[0]);
			editor.commit();
		}
	}

	public void saveTheme(String key) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(THEME_KEY, key);
		editor.commit();
	}
	
	public int getDefaultTheme() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = null;
		int theme = 0;
		value = preferences.getString(THEME_KEY, themeKeys[0]);
		for (int i = 0; i < themeKeys.length; i ++) {
			if (value.equals(themeKeys[i])) {
				theme = themeValues[i];
				break;
			}
		}
		return theme;
	}
}
