package com.king.mytennis.view.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.FileIO;
import com.king.mytennis.utils.PinyinUtil;
import com.king.mytennis.view.R;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;

public class AutoFillFragment extends PreferenceFragment implements OnPreferenceClickListener
		, OnClickListener{

	private PreferenceScreen prefScreen;
	private Preference uniquePref;
	private List<AutoFillItem> formList;
	private List<AutoFillListItemPreference> prefList;
	private CompoundButton lastCheckedItem;
	
	private Preference prefToUpdate;
	private AutoFillItem itemToUpdate;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		load();
		
	}
	
	private void load() {
		addPreferencesFromResource(R.xml.setting_auto_fill);
		
		prefScreen = getPreferenceScreen();

		prefList = new ArrayList<AutoFillListItemPreference>();
		formList = new ArrayList<AutoFillItem>();

		loadExistedForms();
	}

	private void loadExistedForms() {
		if (Configuration.supportMultiAutoFill) {
			formList = new FileIO().getAllAutoFillForms();
			if (formList != null && formList.size() > 0) {
				Collections.sort(formList, new ItemComparator());
				AutoFillListItemPreference preference = null;
				for (AutoFillItem item:formList) {
					preference = new AutoFillListItemPreference(getActivity());
					addPreferenceView(preference, item);
				}
				((SettingActivity) getActivity()).notifyAddDeleteAction(true);
			}
		}
		else {
			Configuration conf = Configuration.getInstance();
			conf.loadFromPreference(getActivity());
			if (conf.autoFillItem.getMatch().length() > 0) {
				uniquePref = new Preference(getActivity());
				uniquePref.setTitle(conf.autoFillItem.getMatch());
				addPreference(uniquePref, conf.autoFillItem);
				uniquePref.setOnPreferenceClickListener(this);
			}
		}
	}

	private void addPreferenceView(Preference preference, AutoFillItem item) {
		if (preference instanceof AutoFillListItemPreference) {
			AutoFillListItemPreference pref = (AutoFillListItemPreference) preference;
			pref.setValue(item);
			Configuration.getInstance().loadFromPreference(getActivity());
			if (item.getMatch().equals(Configuration.getInstance().autoFillItem.getMatch())) {
				pref.setChecked(true);
				lastCheckedItem = pref.getCompoundButton();
				uniquePref = preference;
			}
			pref.addOnClickListener(this);
			prefList.add(pref);
			prefScreen.addPreference(pref);
		}
		else {
			preference.setTitle(item.getMatch());
			preference.setOnPreferenceClickListener(this);
			prefScreen.addPreference(preference);
			Bundle bundle = preference.getExtras();
			bundle.putSerializable("data", item);
		}
	}
	
	public void addPreference(Preference preference, AutoFillItem item) {
		addPreferenceView(preference, item);
		if (Configuration.supportMultiAutoFill) {
			if (formList == null) {
				formList = new ArrayList<AutoFillItem>();
			}
			prefList.add((AutoFillListItemPreference) preference);
			formList.add(item);
		}
	}

	/**
	 * 按match拼音升序排列
	 */
	private class ItemComparator implements Comparator<AutoFillItem> {

		@Override
		public int compare(AutoFillItem lhs, AutoFillItem rhs) {
			if (lhs.getMatchPinyin() == null) {
				lhs.setMatchPinyin(PinyinUtil.getPinyin(lhs.getMatch()));
			}
			if (rhs.getMatchPinyin() == null) {
				rhs.setMatchPinyin(PinyinUtil.getPinyin(rhs.getMatch()));
			}
			return lhs.getMatchPinyin().compareTo(rhs.getMatchPinyin());
		}
	}

	@Override
	/**
	 * only invoked in non-supportMultiAutoFill mode
	 */
	public boolean onPreferenceClick(Preference preference) {
		if (preference == uniquePref) {
			prefToUpdate = uniquePref;
			itemToUpdate = Configuration.getInstance().autoFillItem;
			((SettingActivity) getActivity()).editSelectedPreference(itemToUpdate);
		}
		return true;
	}

	public void updateAutoFillPref(AutoFillItem item) {
		prefToUpdate.setTitle(item.getMatch());
		if (prefToUpdate == uniquePref) {
			((SettingActivity) getActivity()).setAsPreference(item);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * only invoked in supportMultiAutoFill mode
	 */
	public void onClick(View v) {
		if (v instanceof CompoundButton) {
			CompoundButton buttonView = (CompoundButton) v;
			HashMap<String, Object> map = (HashMap<String, Object>) buttonView.getTag();
			if (!buttonView.isChecked()) {
				buttonView.setChecked(true);
				uniquePref = (Preference) map.get("preference");
			}
			else if (buttonView.isChecked()) {
				if (lastCheckedItem != null) {
					lastCheckedItem.setChecked(false);
				}
				lastCheckedItem = buttonView;
				uniquePref = (Preference) map.get("preference");
				AutoFillItem item = (AutoFillItem) map.get("item");
				((SettingActivity) getActivity()).setAsPreference(item);
			}
		}
		else {
			HashMap<String, Object> map = (HashMap<String, Object>) v.getTag();
			prefToUpdate = (Preference) map.get("preference");
			itemToUpdate = (AutoFillItem) map.get("item");
			((SettingActivity) getActivity()).editSelectedPreference(itemToUpdate);
		}
	}

	public Preference getPrefToUpdate() {
		return prefToUpdate;
	}
	public AutoFillItem getItemToUpdate() {
		return itemToUpdate;
	}

	public void reload() {
		prefScreen.removeAll();
		load();
	}
}
