package com.king.mytennis.view.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.R;
import com.king.mytennis.glory.ActionBar;
import com.king.mytennis.glory.ActionBar.ActionBarListener;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.FileIO;
import com.king.mytennis.service.Application;

public class SettingActivity extends BaseActivity implements ActionBarListener {

	public static final int START_DEFAULT = 0;
	public static final int START_AUTOFILL_SELECT = 1;
	public static final String START_MODE = "start_mode";

	private int startMode;

	private final int FRAG_MAIN = 0;
	private final int FRAG_AUTOFILL = 1;
	private int fragMode = FRAG_MAIN;

	private MainFragment mainFragment;
	private AutoFillFragment autoFillFragment;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if (Application.isLollipop()) {
			setContentView(R.layout.settings_l);
		}
		else {
			setContentView(R.layout.settings);
		}
		actionBar = new ActionBar(this, this);
		actionBar.setTitle(getResources().getString(R.string.action_setting));

		startMode = getIntent().getIntExtra(START_MODE, START_DEFAULT);
		switch (startMode) {
			case START_AUTOFILL_SELECT:
				changeToAutoFillFragment();
				break;
			default:
				initMainSetting();
				break;
		}
		super.onCreate(savedInstanceState);
	}

	public int getStartMode() {
		return startMode;
	}

	private void initMainSetting() {
		mainFragment = new MainFragment();
		getFragmentManager().beginTransaction().replace(R.id.setting_container, mainFragment).commit();
	}

	public void changeToAutoFillFragment() {
		actionBar.clearActionIcon();
		actionBar.addAddIcon();
		if (autoFillFragment == null) {
			autoFillFragment = new AutoFillFragment();
		}
		getFragmentManager().beginTransaction().replace(R.id.setting_container, autoFillFragment).commit();
		fragMode = FRAG_AUTOFILL;
	}

	public void reload() {

		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	public void changeToMainFrag() {
		actionBar.clearActionIcon();
		getFragmentManager().beginTransaction().replace(R.id.setting_container, mainFragment).commit();
		fragMode = FRAG_MAIN;

	}

	public void openAutoFillItem(Bundle bundle, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(this, AutoFillFormEditor.class);
		if (bundle != null) {
			bundle.putInt("request_code", requestCode);
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
	}

	public void setAsPreference(AutoFillItem item) {
		Configuration.getInstance().createPreference(this, item);
		if (startMode != START_AUTOFILL_SELECT) {
			mainFragment.updateAutoFillPref(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == AutoFillFormEditor.REQUEST_ADD && resultCode == AutoFillFormEditor.RESULT_OK) {
			Bundle bundle = data.getExtras();
			AutoFillItem autoFillItem = (AutoFillItem) bundle.getSerializable("data");

			if (Configuration.supportMultiAutoFill) {
				AutoFillListItemPreference preference = new AutoFillListItemPreference(this);
				autoFillFragment.addPreference(preference, autoFillItem);
			}
			else {
				setAsPreference(autoFillItem);

				Preference preference = new Preference(this);
				autoFillFragment.addPreference(preference, autoFillItem);
			}
			new FileIO().saveAutoFillForm(autoFillItem);
			Toast.makeText(this, R.string.save_ok, Toast.LENGTH_LONG).show();
		}
		else if (requestCode == AutoFillFormEditor.REQUEST_UPDATE && resultCode == AutoFillFormEditor.RESULT_OK) {
			Bundle bundle = data.getExtras();
			AutoFillItem autoFillItem = (AutoFillItem) bundle.getSerializable("data");

			if (autoFillFragment.getPrefToUpdate() != null) {
				if (!autoFillFragment.getItemToUpdate().getMatch().equals(autoFillItem.getMatch())) {
					new FileIO().deleteAutoFillForm(autoFillFragment.getItemToUpdate());
				}
			}
			new FileIO().saveAutoFillForm(autoFillItem);

			if (Configuration.supportMultiAutoFill) {
				autoFillFragment.updateAutoFillPref(autoFillItem);
				autoFillFragment.reload();
			}
			else {
				setAsPreference(autoFillItem);

				Configuration conf = Configuration.getInstance();
				autoFillFragment.updateAutoFillPref(conf.autoFillItem);
			}
			Toast.makeText(this, R.string.save_ok, Toast.LENGTH_LONG).show();
		}
		else if (requestCode == AutoFillFormEditor.REQUEST_DELETE) {
			if (Configuration.supportMultiAutoFill) {
				autoFillFragment.reload();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBack() {
		if (fragMode == FRAG_MAIN) {
			finish();
		}
		else {
			if (startMode == START_AUTOFILL_SELECT) {
				setResult(RESULT_OK);
				finish();
			}
			else {
				changeToMainFrag();
			}
		}
	}



	@Override
	public void onBackPressed() {
		onBack();
	}

	public void editSelectedPreference(AutoFillItem item) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("data", item);
		openAutoFillItem(bundle, AutoFillFormEditor.REQUEST_UPDATE);
	}

	@Override
	public void onOk() {
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public ListAdapter createMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListAdapter onPrepareMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMenuSelected(int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(String text, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDelete() {
		if (fragMode == FRAG_AUTOFILL) {
			if (Configuration.supportMultiAutoFill) {
				openAutoFillItem(new Bundle(), AutoFillFormEditor.REQUEST_DELETE);
			}
		}
	}

	@Override
	public void onIconClick(View view) {
		if (view.getId() == R.id.actionbar_add) {
			if (fragMode == FRAG_AUTOFILL) {
				boolean canAdd = false;
				if (Configuration.supportMultiAutoFill) {
					canAdd = true;
				}
				else {
					Configuration conf = Configuration.getInstance();
					if (conf.autoFillItem == null || conf.autoFillItem.getMatch().length() == 0) {
						canAdd = true;
					}
				}

				if (canAdd) {
					Bundle bundle = new Bundle();
					bundle.putInt("request_code", AutoFillFormEditor.REQUEST_ADD);
					openAutoFillItem(bundle, AutoFillFormEditor.REQUEST_ADD);
				}
			}
		}
	}

	public void notifyAddDeleteAction(boolean b) {
		actionBar.addDeleteIcon();
	}

}
