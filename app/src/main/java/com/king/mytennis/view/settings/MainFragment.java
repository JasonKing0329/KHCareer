package com.king.mytennis.view.settings;

import java.util.Locale;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.TextUtils;
import android.widget.Toast;

import com.king.mytennis.http.BaseUrl;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.net.UploadHelper;
import com.king.mytennis.service.FingerPrintController;
import com.king.mytennis.service.LanguageService;
import com.king.mytennis.update.UpdateManager;
import com.king.mytennis.view.R;
import com.king.mytennis.view.SafeSetDialog;

public class MainFragment extends PreferenceFragment implements OnPreferenceChangeListener
		, OnPreferenceClickListener{

	private CheckBoxPreference loginPref;
	private ListPreference lanPref, httpPref, uiPref;
	private EditTextPreference dirConfigPref, dirTempPref, dirDBPref, httpServerPref;
	private Preference safePref, uploadPref, cachePref, checkUpdatePref;
	private String currentLanguage;
	
	private static Toast fpNotSupportToast;
	private CacheController cacheController;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.setting_main);
		
		loginPref = (CheckBoxPreference) findPreference("setting_login_fingerprint");
		lanPref = (ListPreference) findPreference("setting_default_language");
		httpPref = (ListPreference) findPreference("setting_default_http_method");
		uiPref = (ListPreference) findPreference("setting_default_ui");
		dirConfigPref = (EditTextPreference) findPreference("setting_default_dir_config");
		dirTempPref = (EditTextPreference) findPreference("setting_default_dir_temp");
		dirDBPref = (EditTextPreference) findPreference("setting_default_dir_db");
		httpServerPref = (EditTextPreference) findPreference("pref_http_server");
		safePref = findPreference("setting_safeset");
		uploadPref = findPreference("setting_upload");
		cachePref = findPreference("setting_cache");
		checkUpdatePref = findPreference("pref_http_update");
		loginPref.setOnPreferenceChangeListener(this);
		lanPref.setOnPreferenceChangeListener(this);
		uiPref.setOnPreferenceChangeListener(this);
		httpPref.setOnPreferenceChangeListener(this);
		dirConfigPref.setOnPreferenceChangeListener(this);
		dirTempPref.setOnPreferenceChangeListener(this);
		dirDBPref.setOnPreferenceChangeListener(this);

		if (!new FingerPrintController(getActivity()).isSupported()) {
			if (fpNotSupportToast == null) {
				fpNotSupportToast = Toast.makeText(getActivity(), R.string.login_finger_not_support, Toast.LENGTH_LONG);
				fpNotSupportToast.show();
			}
			loginPref.setEnabled(false);
			
		}
		if (!Configuration.supportSetConfigDir) {
			dirConfigPref.setEnabled(false);
			dirTempPref.setEnabled(false);
			dirDBPref.setEnabled(false);
		}
		if (!Configuration.supportUpload) {
			uploadPref.setEnabled(false);
		}
		
		String str = lanPref.getSharedPreferences().getString("setting_default_language", "English");
		lanPref.setSummary(str);
		currentLanguage = str;
		str = httpPref.getSharedPreferences().getString("setting_default_http_method"
				, getActivity().getResources().getString(R.string.setting_default_http_method_apache));
		httpPref.setSummary(str);
		str =  dirConfigPref.getSharedPreferences().getString("setting_default_dir_config", Configuration.CONF_DIR);
		dirConfigPref.setSummary(str);
		str = dirDBPref.getSharedPreferences().getString("setting_default_dir_db", Configuration.HISTORY_BASE);
		dirDBPref.setSummary(str);
		str = dirTempPref.getSharedPreferences().getString("setting_default_dir_temp", Configuration.TEMP_DIR);
		dirTempPref.setSummary(str);

		httpServerPref.setSummary(SettingProperty.getServerBaseUrl(getActivity()));
		httpServerPref.setOnPreferenceChangeListener(this);
		
		loginPref.setOnPreferenceClickListener(this);
		safePref.setOnPreferenceClickListener(this);
		uploadPref.setOnPreferenceClickListener(this);
		cachePref.setOnPreferenceClickListener(this);
		checkUpdatePref.setOnPreferenceClickListener(this);

		cacheController = new CacheController();
		cachePref.setSummary(cacheController.getCacheSize());
		
		uiPref.setSummary(SettingProperty.getDefaultUiMode(getActivity()));
	}
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == lanPref) {
			String[] lans = getActivity().getResources().getStringArray(R.array.languageChoices);
			if (!currentLanguage.equals(newValue)) {
				lanPref.setSummary(newValue.toString());
				if (newValue.toString().equals(lans[0])) {//zh
					LanguageService.changeLanguage(getActivity(), Locale.CHINESE);
				}
				else {
					LanguageService.changeLanguage(getActivity(), Locale.ENGLISH);
				}
				((SettingActivity) getActivity()).reload();
			}
		}
		else if (preference == dirTempPref) {
			dirTempPref.setSummary(newValue.toString());
		}
		else if (preference == dirConfigPref) {
			dirConfigPref.setSummary(newValue.toString());
		}
		else if (preference == dirDBPref) {
			dirDBPref.setSummary(newValue.toString());
		}
		else if (preference == httpPref) {
			httpPref.setSummary(newValue.toString());
		}
		else if (preference == uiPref) {
			uiPref.setSummary(newValue.toString());
		}
		else if (preference == httpServerPref) {
			httpServerPref.setSummary((String) newValue);
			BaseUrl.getInstance().setBaseUrl((String) newValue);
		}
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {

		if (preference == safePref) {
			if (loginPref.isChecked()) {
				new FingerPrintController(getActivity()).showIdentifyDialog(false, new FingerPrintController.SimpleIdentifyListener() {
					
					@Override
					public void onSuccess() {
						showPasswordEditor(false);
					}
					
					@Override
					public void onFail() {

					}
					
					@Override
					public void onCancel() {

					}
				});
			}
			else {
				showPasswordEditor(true);
			}
		}
		else if (preference == uploadPref) {
			new UploadHelper(getActivity()).uploadRecord();
		}
		else if (preference == cachePref) {
			if (cacheController == null) {
				cacheController = new CacheController();
			}
			cacheController.clearCache();
			cachePref.setSummary("0KB");
		}
		else if (preference == checkUpdatePref) {
			if (TextUtils.isEmpty(SettingProperty.getServerBaseUrl(getActivity()))) {
				Toast.makeText(getActivity(), R.string.server_url_empty, Toast.LENGTH_LONG).show();
			}
			else {
				UpdateManager manager = new UpdateManager(getActivity());
				manager.showMessageWarning();
				manager.startCheck();
			}
		}
		return true;
	}

	private void showPasswordEditor(boolean needOldPWD) {
		new SafeSetDialog(getActivity(), needOldPWD);
	}
}
