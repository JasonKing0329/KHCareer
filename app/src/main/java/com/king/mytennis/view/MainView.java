package com.king.mytennis.view;

import com.king.mytennis.model.Constants;
import com.king.mytennis.model.MySQLHelper;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.service.MenuService;
import com.king.mytennis.view.settings.SettingProperty;
import com.king.mytennis.view.slidingmenu.SlidingMenuAbstract;
import com.king.mytennis.view.slidingmenu.SlidingMenuCreator;
import com.king.mytennis.view.slidingmenu.SlidingMenuLeft;
import com.king.mytennis.view.slidingmenu.SlidingMenuService;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainView implements OnClickListener, SlidingMenuService {

	private final int MENU_SAVE = 0;
	private final int MENU_LOAD = 1;
	private final int MENU_SAVEAS = 2;
	private final int MENU_SAVECONF = 3;
	private final int MENU_CHANGEBK = 4;
	private final int MENU_CHANGETHEME = 5;
	private final int MENU_EXIT = 6;

//	private final int LGGSET_CN = 0;
//	private final int LGGSET_EN = 1;

	private MenuService menuService;

	private ManagerActivity userActivity;
	private ProgressDialog turningDialog;
	private String turnResult;
	private RelativeLayout layout;
	private ImageButton button_lookAll;
	private LinearLayout button_insert, button_search, button_h2h, button_rank
			, button_glory, button_setting;

	private ImageView menuButton;
	private ListPopupWindow menuWindow;
	private ListAdapter adapter;
	private String[] menuItems;

	public MainView(ManagerActivity activity) {
		this.userActivity = activity;
		init();
		menuService = new MenuService();
	}

	/**
	 * 在同一个Activity下切换view，每一次都需要重新find控件
	 */
	public void reInit() {
		init();
	}

	private void init() {

		layout = (RelativeLayout) userActivity.findViewById(R.id.layout_mainview);
		layout.setBackground(new BitmapDrawable(userActivity.getBackground()));
		button_lookAll = (ImageButton) userActivity
				.findViewById(R.id.mview_look);
		button_insert = (LinearLayout) userActivity
				.findViewById(R.id.mview_insert_layout);
		button_search = (LinearLayout) userActivity
				.findViewById(R.id.mview_search_layout);
		button_h2h = (LinearLayout) userActivity
				.findViewById(R.id.mview_h2h_layout);
		button_rank = (LinearLayout) userActivity
				.findViewById(R.id.mview_rank_layout);
		button_glory = (LinearLayout) userActivity
				.findViewById(R.id.mview_glory_layout);
		button_setting = (LinearLayout) userActivity
				.findViewById(R.id.mview_setting_layout);
		menuButton = (ImageView) userActivity.findViewById(R.id.mview_menu);
		button_lookAll.setOnClickListener(this);
		button_insert.setOnClickListener(this);
		button_search.setOnClickListener(this);
		button_h2h.setOnClickListener(this);
		button_rank.setOnClickListener(this);
		button_glory.setOnClickListener(this);
		button_setting.setOnClickListener(this);
		menuButton.setOnClickListener(this);

		menuWindow = new ListPopupWindow(userActivity);
		menuWindow.setAnchorView(menuButton);
		int width = userActivity.getResources().getDimensionPixelSize(R.dimen.actionbar_menu_width);
		int iconWidth = userActivity.getResources().getDimensionPixelSize(R.dimen.actionbar_icon_width);
		int offset = iconWidth - width;//in sliding menu mode, use this to control not show on menu view
		menuWindow.setWidth(width);
		menuWindow.setHorizontalOffset(offset);
		menuWindow.setHeight(userActivity.getResources().getDimensionPixelSize(R.dimen.mainview_menu_height));
		menuWindow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
									long arg3) {
				switch (position) {
					case MENU_SAVE:
						showSaveResult(menuService.saveDatabases());
						break;
					case MENU_LOAD:
						//userActivity.load();
						userActivity.startDialog(ManagerActivity.DLG_LOAD);
						break;
					case MENU_SAVEAS:
						userActivity.startDialog(ManagerActivity.DLG_SAVEAS);
						break;
					/*
				case MENU_SET:// 修改数据源
					userActivity.startDialog(ManagerActivity.DLG_SETRESFILE);
					break;
				case MENU_DBTOFILE:
					// dbToFile();//从文件版本改为数据库版本后，该菜单项无用
					break;
				case MENU_FILETODB:
					if (userActivity.getRecordList() == null
							|| userActivity.getRecordList().size() == 0) {
						Toast.makeText(userActivity, R.string.nullRecord,
								Toast.LENGTH_LONG).show();
						return;
					}
					turningDialog = new ProgressDialog(userActivity);
					turningDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					turningDialog.setMessage(userActivity
							.getString(R.string.dlg_loading_msg));
					turningDialog.show();
					new Thread() {
						public void run() {
							turnResult = menuService.fileToDB(userActivity
									.getRecordList());
							Message msg = new Message();
							msg.what = 1;
							turnHandler.sendMessage(msg);
						}
					}.start();
					break;
					*/
					case MENU_CHANGEBK:
						userActivity.startDialog(ManagerActivity.DLG_CHOOSEBK);
						break;
					case MENU_CHANGETHEME:
						userActivity.startDialog(ManagerActivity.DLG_CHANGE_THEME);
						break;
					case MENU_SAVECONF:
						showSaveResult(menuService.saveConfiguration());
						break;
//				case MENU_SETTING:
//					AlertDialog.Builder dlg = new AlertDialog.Builder(userActivity);
//					dlg.setTitle(R.string.menu_setting);
//					dlg.setItems(userActivity.getResources().getStringArray(
//									R.array.languageChoices), lisLggSet);
//					dlg.show();
//					break;
					case MENU_EXIT:
						userActivity.finish();
						break;
					default:
						break;
				}
				menuWindow.dismiss();
			}

		});
		menuItems = userActivity.getResources().getStringArray(R.array.mainview_menu);
		adapter = new ArrayAdapter<String>(userActivity
				, android.R.layout.simple_dropdown_item_1line, menuItems);
		menuWindow.setAdapter(adapter);
	}

	public void changeBackground(Bitmap bk) {
		layout.setBackground(new BitmapDrawable(bk));
	}

	@Override
	public void onClick(View v) {
		if (v == button_insert) {
			userActivity.startInsertActivity();
		}
		else if (v == button_search) {
			userActivity.startDialog(ManagerActivity.DLG_SEARCH);
		}
		else if (v == button_lookAll) {
			userActivity.changeView(ManagerActivity.VIEW_LIST, false);
		}
		else if (v == menuButton) {
			menuWindow.show();
		}
		else if (v == button_h2h) {
			userActivity.startH2hView();
		}
		else if (v == button_rank) {
			userActivity.startRankView();
		}
		else if (v == button_glory) {
			userActivity.startGloryView();
		}
		else if (v == button_setting) {
			userActivity.startSetting();
		}
	}

	public void onBackPressed() {
		if (menuWindow.isShowing()) {
			menuWindow.dismiss();
			return;
		}
		new AlertDialog.Builder(userActivity)
				.setTitle(R.string.warning)
				.setMessage(R.string.exit_msg)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								MySQLHelper.closeHelper();

								MultiUserManager.destroy();//如果不主动销毁，下一次进入应用程序就不会new该实例了
								//如果是从任务管理器关掉应用则不用主动销毁

								userActivity.finish();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								return;
							}
						}).show();
		// super.onBackPressed();不实现父类方法，父类方法是按下back键后就退回到上一个activity
	}

	/*
	 * 监听back键的第二种方法
	 *
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
	 *
	 * } return super.onKeyDown(keyCode, event); }
	 */

	public void onCreateOptionsMenu(Menu menu) {
		/*
		menu.add(0, MENU_SAVE, MENU_SAVE, R.string.menu_save);
		menu.add(0, MENU_LOAD, MENU_LOAD, R.string.menu_load);
		menu.add(0, MENU_SAVEAS, MENU_SAVEAS, R.string.menu_saveas);
		menu.add(0, MENU_SAVECONF, MENU_SAVECONF, R.string.menu_saveconf);
		menu.add(0, MENU_SET, MENU_SET, R.string.menu_set);
		menu.add(0, MENU_FILETODB, MENU_FILETODB, R.string.menu_filetodb);
		menu.add(0, MENU_DBTOFILE, MENU_DBTOFILE, R.string.menu_dbtofile);
		menu.add(0, MENU_CHANGEBK, MENU_CHANGEBK, R.string.menu_choosebk);
		menu.add(0, MENU_SAFESET, MENU_SAFESET, R.string.menu_safeset);
		menu.add(0, MENU_LANGUAGESET, MENU_LANGUAGESET, R.string.menu_lggset);
		menu.add(0, MENU_UPLOADREC, MENU_UPLOADREC, R.string.menu_uploadrecord);
		*/
	}

	public void onMenuOpened(int featureId, Menu menu) {
		/*
		menu.clear();
		menu.add(0, MENU_SAVE, MENU_SAVE, R.string.menu_save);
		menu.add(0, MENU_LOAD, MENU_LOAD, R.string.menu_load);
		menu.add(0, MENU_SAVEAS, MENU_SAVEAS, R.string.menu_saveas);
		menu.add(0, MENU_SAVECONF, MENU_SAVECONF, R.string.menu_saveconf);
		menu.add(0, MENU_SET, MENU_SET, R.string.menu_set);
		menu.add(0, MENU_FILETODB, MENU_FILETODB, R.string.menu_filetodb);
		menu.add(0, MENU_DBTOFILE, MENU_DBTOFILE, R.string.menu_dbtofile);
		menu.add(0, MENU_CHANGEBK, MENU_CHANGEBK, R.string.menu_choosebk);
		menu.add(0, MENU_SAFESET, MENU_SAFESET, R.string.menu_safeset);
		menu.add(0, MENU_LANGUAGESET, MENU_LANGUAGESET, R.string.menu_lggset);
		menu.add(0, MENU_UPLOADREC, MENU_UPLOADREC, R.string.menu_uploadrecord);
		*/
	}

	public void onOptionsItemSelected(MenuItem item) {
		/*
		switch (item.getItemId()) {

		case MENU_SAVE:
			showSaveResult(menuService.save(userActivity.getRecordList()));
			break;
		case MENU_LOAD:
			userActivity.load();
			break;
		case MENU_SAVEAS:
			userActivity.startDialog(ManagerActivity.DLG_SAVEAS);
			break;
		case MENU_SET:// 修改数据源
			userActivity.startDialog(ManagerActivity.DLG_SETRESFILE);
			break;
		case MENU_DBTOFILE:
			// dbToFile();//从文件版本改为数据库版本后，该菜单项无用
			break;
		case MENU_FILETODB:
			if (userActivity.getRecordList() == null
					|| userActivity.getRecordList().size() == 0) {
				Toast.makeText(userActivity, R.string.nullRecord,
						Toast.LENGTH_LONG).show();
				return;
			}
			turningDialog = new ProgressDialog(userActivity);
			turningDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			turningDialog.setMessage(userActivity
					.getString(R.string.dlg_loading_msg));
			turningDialog.show();
			new Thread() {
				public void run() {
					turnResult = menuService.fileToDB(userActivity
							.getRecordList());
					Message msg = new Message();
					msg.what = 1;
					turnHandler.sendMessage(msg);
				}
			}.start();
			break;
		case MENU_CHANGEBK:
			userActivity.startDialog(ManagerActivity.DLG_CHOOSEBK);
			break;
		case MENU_SAVECONF:
			showSaveResult(menuService.saveConfiguration());
			break;
		case MENU_SAFESET:
			userActivity.startDialog(ManagerActivity.DLG_SAFESET);
			break;
		case MENU_LANGUAGESET:
			AlertDialog.Builder dlg = new AlertDialog.Builder(userActivity);
			dlg.setTitle(R.string.menu_lggset);
			dlg.setItems(userActivity.getResources().getStringArray(
							R.array.languageChoices), lisLggSet);
			dlg.show();
			break;
		case MENU_UPLOADREC:
			uploadRecord();
			break;
		default:
			break;
		}
		*/
	}

	/**
	 * Deprecated
	 */
//	DialogInterface.OnClickListener lisLggSet = new DialogInterface.OnClickListener() {
//
//		@Override
//		public void onClick(DialogInterface dialog, int which) {
//			if (which == LGGSET_CN) {
//				LanguageService.changeLanguage(userActivity, Locale.CHINESE);
//				menuService.alterDefLanguage("zh");
//			}
//			else if (which == LGGSET_EN) {
//				LanguageService.changeLanguage(userActivity, Locale.ENGLISH);
//				menuService.alterDefLanguage("en");
//			}
//		}
//	};

	Handler turnHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			turningDialog.cancel();
			if (msg.what == 1) {
				Toast.makeText(userActivity, turnResult, Toast.LENGTH_LONG)
						.show();
			}
			super.handleMessage(msg);
		}

	};

	private void showSaveResult(String message) {

		if (message.equals("ok")) {
			Toast.makeText(userActivity, R.string.save_ok, Toast.LENGTH_LONG)
					.show();
		} else {
			Toast.makeText(userActivity, message, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void loadMenu(SlidingMenuAbstract slidingMenu,
						 LinearLayout menuLayout) {
		menuLayout.removeAllViews();
		SlidingMenuCreator creator = new SlidingMenuCreator(userActivity, slidingMenuListener);
		creator.loadMenu(Constants.MainViewMenu, menuLayout, SettingProperty.getSlidingMenuMode(userActivity));
		Bitmap bitmap = creator.loadBackground(SlidingMenuAbstract.MENU_BK_KEY);
		userActivity.updateBackground(bitmap);
	}

	@Override
	public SlidingMenuCreator loadTwoWayMenu(LinearLayout menuLayout,
											 LinearLayout menuLayoutRight) {
		menuLayout.removeAllViews();
		menuLayoutRight.removeAllViews();

		SlidingMenuCreator slidingMenuCreator = new SlidingMenuCreator(userActivity, slidingMenuListener);
		slidingMenuCreator.loadMenu(Constants.MainViewMenu, menuLayout, SettingProperty.SLIDINGMENU_LEFT);
		slidingMenuCreator.loadMenu(Constants.MainViewMenu, menuLayoutRight, SettingProperty.SLIDINGMENU_RIGHT);
		Bitmap bitmap = slidingMenuCreator.loadBackground(SlidingMenuLeft.BK_KEY);
		userActivity.updateBackground(bitmap);
		return slidingMenuCreator;
	}

	OnClickListener slidingMenuListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.string.menu_save:
					showSaveResult(menuService.saveDatabases());
					break;
				case R.string.menu_load:
					userActivity.startDialog(ManagerActivity.DLG_LOAD);
					break;
				case R.string.menu_saveas:
					userActivity.startDialog(ManagerActivity.DLG_SAVEAS);
					break;
				case R.string.menu_saveconf:
					showSaveResult(menuService.saveConfiguration());
					break;
				case R.string.menu_choosebk:
					userActivity.startDialog(ManagerActivity.DLG_CHOOSEBK);
					break;
				case R.string.menu_change_theme:
					userActivity.startDialog(ManagerActivity.DLG_CHANGE_THEME);
					break;
				case R.string.exit:
					userActivity.finish();
					break;
			}
		}
	};
}
