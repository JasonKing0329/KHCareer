package com.king.mytennis.view;

import com.king.mytennis.glory.ActionBar;
import com.king.mytennis.glory.ActionBar.ActionBarListener;
import com.king.mytennis.http.BaseUrl;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.MySQLHelper;
import com.king.mytennis.model.PermissionUtil;
import com.king.mytennis.service.FingerPrintController;
import com.king.mytennis.service.FingerPrintController.SimpleIdentifyListener;
import com.king.mytennis.service.Application;
import com.king.mytennis.service.ImageUtil;
import com.king.mytennis.service.LanguageService;
import com.king.mytennis.service.ThemeManager;
import com.king.mytennis.service.UserLoginService;
import com.king.mytennis.view.settings.SettingProperty;
import com.king.mytennis.view_v_7_0.V7MainActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements OnClickListener, Callback, ActionBarListener {

	private UserLoginService loginService;

	private EditText edit_pwd, edit_answer;
	private Button button_ok, button_exit, button_fgtpwd, button_submit;
	private TextView tv_qst, tv_qstDetail, tv_ans, tv_showAnswer;
	private String def_language = "zh";
	private boolean clickCount = true;
	private Handler handler = new Handler(this);
	private FingerPrintController fingerPrint;

	private ProgressDialog prepareDialog;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// android6.0及以上需要动态分配权限
		// 先获取读写以及存储权限，不然没法执行init里面的一些初始化操作
		if (Application.isM()) {
			if (PermissionUtil.isStoragePermitted(this)) {
				executeOnCreate();
			}
			else {
				PermissionUtil.requestStoragePermission(this, 1);
				PermissionUtil.requestOtherPermission(this);
			}
		}
		else {
			executeOnCreate();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if (PermissionUtil.isStoragePermitted(this)) {
			executeOnCreate();
		}
	}

	private void executeOnCreate() {
		prepareDialog = new ProgressDialog(this);
		prepareDialog.setTitle(R.string.loading);
		prepareDialog.setMessage(getResources().getString(R.string.preparing_app));
		prepareDialog.show();

		new Thread() {
			public void run() {
				//复制数据库文件，总的来说，该方法只有在程序安装后第一次启动有意义。

				//从sdcard现有文件
				//ExternalRecordTool.copyDbFromContent(LoginActivity.this
				//		, Configuration.getInstance().DEF_FILE);

				//从assets中复制
				Configuration.initialize(LoginActivity.this);
				handler.sendMessage(new Message());
			}
		}.start();

		//to debug
//        checkOK();
		BaseUrl.getInstance().setBaseUrl(SettingProperty.getServerBaseUrl(this));
	}

	@Override
	public boolean handleMessage(Message msg) {

		prepareDialog.cancel();
		init();
		return true;
	}

	private void init() {
		loginService = new UserLoginService(this);
		loginService.init();

		//warning: 这个Activity下，切换语言操作要放到setContentView之前才行。而UserActivity那边就不用，真奇怪！！
		initLanguage();

		ThemeManager.init(this);

		if (SettingProperty.isFingerPrintLogin(this)) {
			fingerPrint = new FingerPrintController(this);
			if (fingerPrint.isSupported() && fingerPrint.hasRegistered()) {
				startFingerPrintDialog();
				return;
			}
		}

		if (Application.isLollipop()) {
			setContentView(R.layout.main_l);
		}
		else {
			setContentView(R.layout.main);
		}
		ActionBar actionBar = new ActionBar(this, this);
		actionBar.setTitle(getResources().getString(R.string.app_name));
		actionBar.addTitleIcon(R.drawable.app_icon);

		/***********调试直接进入userActivity**************
		 Intent intent = new Intent();
		 intent.setClass(this, UserActivity.class);
		 intent.putExtra("def_language", def_language);
		 startActivity(intent);
		 finish();
		 /*************************/

		edit_pwd = (EditText) findViewById(R.id.login_edit_pwd);
		button_ok = (Button) findViewById(R.id.login_button_ok);
		button_exit = (Button) findViewById(R.id.login_button_exit);
		button_exit.setOnClickListener(this);
		button_ok.setOnClickListener(this);

		button_fgtpwd = (Button) findViewById(R.id.login_imagebt_fgtpwd);
		button_fgtpwd.setOnClickListener(this);
		button_submit = (Button) findViewById(R.id.login_button_submit);
		button_submit.setOnClickListener(this);
		edit_answer = (EditText) findViewById(R.id.login_edit_answer);
		tv_ans = (TextView) findViewById(R.id.login_tv_ans);
		tv_qst = (TextView) findViewById(R.id.login_tv_qst);
		tv_qstDetail = (TextView) findViewById(R.id.login_tv_qstDetais);
		tv_qstDetail.setText(loginService.getQuestion());
		tv_showAnswer = (TextView) findViewById(R.id.login_tv_showpwd);

	}

	private void startFingerPrintDialog() {
		if (fingerPrint.hasRegistered()) {
			boolean withPW = false;
			fingerPrint.showIdentifyDialog(withPW, new SimpleIdentifyListener() {

				@Override
				public void onSuccess() {
					checkOK();
				}

				@Override
				public void onFail() {

				}

				@Override
				public void onCancel() {
					finish();
				}
			});
		}
		else {
			Toast.makeText(this, R.string.login_finger_not_register, Toast.LENGTH_LONG).show();
		}
	}

	private void initLanguage() {

		//v4.7 change: read default from preference
		//def_language = loginService.getLanguage();//接受默认语言，以传给UserActivity

		LanguageService.init(this);
	}

	private void checkOK() {

		ImageUtil.initImageLoader(this);

		String uiMode = SettingProperty.getDefaultUiMode(this);
		String[] array = getResources().getStringArray(R.array.uiChoices);
		if (array[0].equals(uiMode)) {
			new UiSelectorManager();
		}
		else if (array[1].equals(uiMode)) {
			initUi(0);
		}
		else if (array[2].equals(uiMode)) {
			initUi(1);
		}
		else if (array[3].equals(uiMode)) {
			initUi(2);
		}

	}

	private void initUi(int flag) {
		MySQLHelper.closeHelper();
		Intent intent = new Intent();
		intent.putExtra("def_language", def_language);
		if (flag == 0) {
			intent.setClass(this, ManagerActivity.class);
		}
		else if (flag == 1) {
			intent.setClass(this, com.king.mytennis.update_v_6_0.ManagerActivity.class);
		}
		else if (flag == 2) {
			intent.setClass(this, V7MainActivity.class);
		}
		startActivity(intent);
		finish();
	}

	private class UiSelectorManager implements OnClickListener {

		private RadioButton classicButton, ui6_0Button, ui7_0Button;
		private CheckBox checkBox;
		private AlertDialog alertDialog;

		public UiSelectorManager() {
			View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_ui_select, null);
			classicButton = (RadioButton) view.findViewById(R.id.login_ui_select_classic);
			ui6_0Button = (RadioButton) view.findViewById(R.id.login_ui_select_6_0);
			ui7_0Button = (RadioButton) view.findViewById(R.id.login_ui_select_7_0);
			checkBox = (CheckBox) view.findViewById(R.id.login_ui_select_check);

			classicButton.setOnClickListener(this);
			ui6_0Button.setOnClickListener(this);
			ui7_0Button.setOnClickListener(this);
			alertDialog = new AlertDialog.Builder(LoginActivity.this)
					.setView(view)
					.show();
		}
		@Override
		public void onClick(View view) {
			String[] array = getResources().getStringArray(R.array.uiChoices);
			if (view == classicButton) {
				if (checkBox.isChecked()) {
					SettingProperty.setDefaultUiMode(LoginActivity.this, array[1]);
				}
				initUi(0);
			}
			else if (view == ui6_0Button) {
				if (checkBox.isChecked()) {
					SettingProperty.setDefaultUiMode(LoginActivity.this, array[2]);
				}
				initUi(1);
			}
			else if (view == ui7_0Button) {
				if (checkBox.isChecked()) {
					SettingProperty.setDefaultUiMode(LoginActivity.this, array[3]);
				}
				initUi(2);
			}
			alertDialog.dismiss();
		}

	}

	@Override
	public void onClick(View v) {

		int choice = v.getId();
		if (choice == R.id.login_button_ok) {

			boolean isright = loginService.checkPassword(edit_pwd.getText().toString());
			if (isright) {
				checkOK();
			}
			else {
				new AlertDialog.Builder(this).setMessage(R.string.error_password).show();
			}
		}
		else if (choice == R.id.login_button_exit) {
			MySQLHelper.closeHelper();
			finish();
		}
		else if (choice == R.id.login_imagebt_fgtpwd) {
			if (clickCount) {
				tv_ans.setVisibility(View.VISIBLE);
				tv_qst.setVisibility(View.VISIBLE);
				tv_qstDetail.setVisibility(View.VISIBLE);
				tv_showAnswer.setVisibility(View.VISIBLE);
				button_submit.setVisibility(View.VISIBLE);
				edit_answer.setVisibility(View.VISIBLE);
			}
			else {
				tv_ans.setVisibility(View.INVISIBLE);
				tv_qst.setVisibility(View.INVISIBLE);
				tv_qstDetail.setVisibility(View.INVISIBLE);
				tv_showAnswer.setVisibility(View.INVISIBLE);
				button_submit.setVisibility(View.INVISIBLE);
				edit_answer.setVisibility(View.INVISIBLE);
			}
			clickCount = !clickCount;
		}
		else if (choice == R.id.login_button_submit) {
			if (loginService.checkAnswer(edit_answer.getText().toString())) {
				tv_showAnswer.setText("Password is " + loginService.getPassword());
			}
			else {
				tv_showAnswer.setText("Wrong answer");
			}
		}
	}

	@Override
	protected void onDestroy() {
		MySQLHelper.closeHelper();
		super.onDestroy();
	}

	@Override
	public void onBack() {
		finish();
	}

	@Override
	public void onOk() {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void onIconClick(View view) {
		// TODO Auto-generated method stub

	}

}