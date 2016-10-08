package com.king.mytennis.view;

import com.king.mytennis.service.ThemeManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
    	setTheme(new ThemeManager(this).getDefaultTheme());

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//prevent from task manager take screenshot
		//also prevent from system screenshot
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		
		super.onCreate(savedInstanceState);
	}

}
