package com.king.mytennis.view;

import com.king.mytennis.service.ThemeManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.WindowManager;

public class BaseActivity extends FragmentActivity {

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
    	setTheme(new ThemeManager(this).getDefaultTheme());

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//prevent from task manager take screenshot
		//also prevent from system screenshot
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

		progressDialog = new ProgressDialog(this);

		super.onCreate(savedInstanceState);
	}

	public void showConfirmMessage(String msg, DialogInterface.OnClickListener listener) {
		new AlertDialog.Builder(this)
				.setTitle(null)
				.setMessage(msg)
				.setPositiveButton(getString(R.string.ok), listener)
				.show();
	}

	public void showProgress(String msg) {
		if (TextUtils.isEmpty(msg)) {
			msg = getResources().getString(R.string.loading);
		}
		progressDialog.setMessage(msg);
		progressDialog.show();
	}

	public void dismissProgress() {
		progressDialog.dismiss();
	}
}
