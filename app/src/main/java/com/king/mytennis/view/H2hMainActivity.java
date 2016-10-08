package com.king.mytennis.view;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

import com.king.mytennis.model.Configuration;
import com.king.mytennis.service.Application;
import com.king.mytennis.service.ExternalRecordTool;
import com.king.mytennis.view.player.Controller;
import com.king.mytennis.view.player.H2HSearcher;
import com.king.mytennis.view.player.PlayerUpdateView;
import com.king.mytennis.view.settings.SettingActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

public class H2hMainActivity extends BaseActivity implements OnClickListener, OnMenuItemClickListener {

	private TextView addATPPlayerButton, addWTAPlayerButton;
	private ImageView atpButton, wtaButton;
	private ImageView menuButton;
	private PopupMenu popupMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Application.isLollipop()) {
			setContentView(R.layout.activity_atpwta_h2h_l);
		}
		else {
			setContentView(R.layout.activity_atpwta_h2h);
		}
		addATPPlayerButton = (TextView) findViewById(R.id.h2h_atp_add_player);
		addWTAPlayerButton = (TextView) findViewById(R.id.h2h_wta_add_player);
		atpButton = (ImageView) findViewById(R.id.h2h_button_atp);
		wtaButton = (ImageView) findViewById(R.id.h2h_button_wta);
		menuButton = (ImageView) findViewById(R.id.h2h_button_menu);
		
		addATPPlayerButton.setOnClickListener(this);
		addWTAPlayerButton.setOnClickListener(this);
		atpButton.setOnClickListener(this);
		wtaButton.setOnClickListener(this);
		menuButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onClick(View view) {
		if (view == addATPPlayerButton) {
			new PlayerUpdateView(this, Controller.ATP).show();
		}
		else if (view == addWTAPlayerButton) {
			new PlayerUpdateView(this, Controller.WTA).show();
		}
		else if (view == atpButton) {
			startH2hSearcher(Controller.ATP);
		}
		else if (view == wtaButton) {
			startH2hSearcher(Controller.WTA);
		}
		else if (view == menuButton) {
			showMenu();
		}
	}
	
	private void showMenu() {
		if (popupMenu == null) {
			popupMenu = new PopupMenu(this, menuButton);
			popupMenu.getMenuInflater().inflate(R.menu.h2h_atpwta, popupMenu.getMenu());
			//menuWindow.setWidth(context.getResources().getDimensionPixelSize(R.dimen.actionbar_menu_width));
			popupMenu.setOnMenuItemClickListener(this);
		}
		popupMenu.show();
	}

	public void startH2hSearcher(int atpOrWta) {
		Bundle bundle = new Bundle();
		bundle.putInt("mode", atpOrWta);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(this, H2HSearcher.class);
		startActivity(intent);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_h2h_atpwta_save:
			ExternalRecordTool.saveDbPlayerAsHistory();
			break;
		case R.id.menu_h2h_atpwta_load:
			new LoadFromDialog(this, new CustomDialog.OnCustomDialogActionListener() {
				
				@Override
				public boolean onSave(Object object) {
					if (object != null) {
						File file = (File) object;
						ExternalRecordTool.replaceDatabase(H2hMainActivity.this
								, file.getPath(), ExternalRecordTool.DATABASE_PLAYER);
					}
					return true;
				}
				
				@Override
				public void onLoadData(HashMap<String, Object> data) {
					File file = new File(Configuration.HISTORY_PLAYER_BASE);
					String[] names = file.list(new FilenameFilter() {
						
						@Override
						public boolean accept(File dir, String filename) {

							return filename.endsWith(".db");
						}
					});
					data.put("data", names);
					data.put("basePath", Configuration.HISTORY_PLAYER_BASE);
				}
				
				@Override
				public boolean onCancel() {
					// TODO Auto-generated method stub
					return false;
				}
			}).show();
			break;
		case R.id.menu_h2h_atpwta_setting:
			startSetting();
			break;

		default:
			break;
		}
		return true;
	}
	
	public void startSetting() {
		startActivity(new Intent().setClass(this, SettingActivity.class));
	}

}
