package com.king.khcareer.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;

import com.king.khcareer.base.BaseActivity;
import com.king.mytennis.view.R;
import com.king.mytennis.glory.ActionBar;
import com.king.mytennis.glory.ActionBar.ActionBarListener;
import com.king.khcareer.base.KApplication;

public class SettingActivity extends BaseActivity implements ActionBarListener {

	private MainFragment mainFragment;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if (KApplication.isLollipop()) {
			setContentView(R.layout.settings_l);
		}
		else {
			setContentView(R.layout.settings);
		}
		actionBar = new ActionBar(this, this);
		actionBar.setTitle(getResources().getString(R.string.action_setting));

		initMainSetting();
		super.onCreate(savedInstanceState);
	}

	private void initMainSetting() {
		mainFragment = new MainFragment();
		getFragmentManager().beginTransaction().replace(R.id.setting_container, mainFragment).commit();
	}

	public void reload() {

		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBack() {
		finish();
	}



	@Override
	public void onBackPressed() {
		onBack();
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
	}

	@Override
	public void onIconClick(View view) {
	}

	public void notifyAddDeleteAction(boolean b) {
		actionBar.addDeleteIcon();
	}

}
