package com.king.khcareer.common.multiuser;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.king.khcareer.base.KApplication;
import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.model.sql.player.DatabaseStruct;
import com.king.mytennis.view.R;
import com.king.khcareer.settings.SettingProperty;

/**
 *
 * @author tstcit
 *
 */
public class MultiUserManager {

	private final String TAG = "MultiUserManager";
	private final String PREF_KEY = "target_user";
	private static MultiUserManager multiUserManager;

	/**
	 * 用来判断数据库里的winner字段是否是当前user获胜
	 */
	public static final String USER_DB_FLAG = "_user";

	private MultiUser[] users;
	private MultiUser currentUser;
	private int[] selectorResIds = new int[] {
			R.drawable.logo_ao, R.drawable.logo_fo, R.drawable.logo_wo, R.drawable.logo_uo
	};
	private int[] selectorResIds_view7 = new int[] {
			R.drawable.icon_list, R.drawable.logo_fo, R.drawable.logo_wo, R.drawable.icon_list
	};
	private boolean userChangedFlag;
	/**
	 * 首次排名进入前30的年份，用于积分系统确认是否运用罚分系统
	 */
	private int[] firstTop30Year = new int[] {
			2011, 2015, 2015, 2017
	};

	private MultiUserManager() {
		Log.i(TAG, "construct");
	}

	public static MultiUserManager getInstance() {
		if (multiUserManager == null) {
			multiUserManager = new MultiUserManager();
		}
		return multiUserManager;
	}

	public int getFirstTop30Year(MultiUser user) {
		if (user == null) {
			user = currentUser;
		}
		if (user.getId().equals(users[0].getId())) {
			return firstTop30Year[0];
		}
		else if (user.getId().equals(users[1].getId())) {
			return firstTop30Year[1];
		}
		else if (user.getId().equals(users[2].getId())) {
			return firstTop30Year[2];
		}
		else if (user.getId().equals(users[3].getId())) {
			return firstTop30Year[3];
		}
		return 0;
	}

	public String getTargetDatabase() {
		if (users != null && currentUser != null) {
			if (currentUser.getId().equals(users[0].getId())) {
				return DatabaseStruct.DATABASE;
			}
			else if (currentUser.getId().equals(users[1].getId())) {
				return DatabaseStruct.DATABASE_FLAMENCO;
			}
			else if (currentUser.getId().equals(users[2].getId())) {
				return DatabaseStruct.DATABASE_HENRY;
			}
			else if (currentUser.getId().equals(users[3].getId())) {
				return DatabaseStruct.DATABASE_TIANQI;
			}
		}
		return DatabaseStruct.DATABASE;
	}

	public String getDatabase(MultiUser user) {
		if (user != null) {
			if (user.getId().equals(users[0].getId())) {
				return DatabaseStruct.DATABASE;
			}
			else if (user.getId().equals(users[1].getId())) {
				return DatabaseStruct.DATABASE_FLAMENCO;
			}
			else if (user.getId().equals(users[2].getId())) {
				return DatabaseStruct.DATABASE_HENRY;
			}
			else if (user.getId().equals(users[3].getId())) {
				return DatabaseStruct.DATABASE_TIANQI;
			}
		}
		return DatabaseStruct.DATABASE;
	}

	public String getPublicDatabase() {
		return DatabaseStruct.DATABASE_PUBLIC;
	}

	public MultiUser[] getUsers() {
		return users;
	}

	public MultiUser getUserKing() {
		return users[0];
	}

	public MultiUser getUserFlamenco() {
		return users[1];
	}

	public MultiUser getUserHenry() {
		return users[2];
	}

	public MultiUser getUserQi() {
		return users[3];
	}

	public MultiUser getUser(String userId) {
		for (MultiUser user:users) {
			if (user.getId().equals(userId)) {
				return user;
			}
		}
		return null;
	}

	public void setCurrentUser(MultiUser user) {
		if (!user.equals(currentUser)) {
			userChangedFlag = true;
			currentUser = user;
		}
	}

	/**
	 * only fit for call by once
	 * @return
	 */
	public boolean isUserChanged() {
		boolean result = userChangedFlag;
		if (userChangedFlag) {
			userChangedFlag = false;
		}
		return result;
	}

	public void loadFromPreference() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KApplication.getInstance());
		String id = preferences.getString(PREF_KEY, "King");
		for (MultiUser user:users) {
			if (user.getId().equals(id)) {
				currentUser = user;
				return;
			}
		}

		if (currentUser == null) {
			currentUser = users[0];
		}
	}

	public void saveToPreference(Context context, MultiUser user) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PREF_KEY, user.getId());
		editor.commit();
	}

	public MultiUser getCurrentUser() {
		if (currentUser == null) {
			loadFromPreference();
		}
		return currentUser;
	}
	public int getUserSelectorResId(Context context, MultiUser user) {

		int[] resIds = selectorResIds;
		String viewMode = SettingProperty.getDefaultUiMode(context);
		String[] array = context.getResources().getStringArray(R.array.uiChoices);
		if (array[3].equals(viewMode)) {//UI 7.0
			resIds = selectorResIds_view7;
		}

		if (users != null) {
			for (int i = 0; i < users.length; i ++) {
				if (users[i].getId().equals(user.getId())) {
					return resIds[i];
				}
			}
		}
		return -1;
	}

	public static void destroy() {
		multiUserManager = null;
	}

	public void loadUsers(Context context) {
		String[] array = context.getResources().getStringArray(R.array.multiuser_list);
		String[] array1 = context.getResources().getStringArray(R.array.multiuser_display_list);
		String[] countries = context.getResources().getStringArray(R.array.multiuser_country);
		String[] birthdays = context.getResources().getStringArray(R.array.multiuser_birthday);
		String[] heights = context.getResources().getStringArray(R.array.multiuser_height);
		String[] weights = context.getResources().getStringArray(R.array.multiuser_weight);
		String[] fullNames = context.getResources().getStringArray(R.array.multiuser_fullname);
		int[] resIds = new int[] {
				R.drawable.flag_china,
				R.drawable.flag_france,
				R.drawable.flag_usa,
				R.drawable.flag_china
		};
		users = new MultiUser[array.length];
		for (int i = 0; i < array.length; i ++) {
			users[i] = new MultiUser(array[i], array1[i]);
			users[i].setBirthday(birthdays[i]);
			users[i].setCountry(countries[i]);
			users[i].setFullName(fullNames[i]);
			users[i].setHeight(heights[i]);
			users[i].setWeight(weights[i]);
			users[i].setFlagImageResId(resIds[i]);
		}
	}

	public String getTargetScoreFile() {
		if (users != null && currentUser != null) {
			if (currentUser.getId().equals(users[0].getId())) {
				return Configuration.SEASON_SCORE_FILE;
			}
			else if (currentUser.getId().equals(users[1].getId())) {
				return Configuration.SEASON_SCORE_FILE_FLAMENCO;
			}
			else if (currentUser.getId().equals(users[2].getId())) {
				return Configuration.SEASON_SCORE_FILE_HENRY;
			}
			else if (currentUser.getId().equals(users[3].getId())) {
				return Configuration.SEASON_SCORE_FILE_TIANQI;
			}
		}
		return DatabaseStruct.DATABASE;
	}

	public String getTargetRankFile(MultiUser user) {
		if (user == null) {
			user = currentUser;
		}
		if (users != null && user != null) {
			if (user.getId().equals(users[0].getId())) {
				return Configuration.RANK_FILE;
			}
			else if (user.getId().equals(users[1].getId())) {
				return Configuration.RANK_FILE_FLAMENCO;
			}
			else if (user.getId().equals(users[2].getId())) {
				return Configuration.RANK_FILE_HENRY;
			}
			else if (user.getId().equals(users[3].getId())) {
				return Configuration.RANK_FILE_TIANQI;
			}
		}
		return DatabaseStruct.DATABASE;
	}


}
	
