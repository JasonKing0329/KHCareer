package com.king.mytennis.view;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.king.mytennis.glory.GloryModuleActivity;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.Constants;
import com.king.mytennis.model.H2HDAOList;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.model.MySQLHelper;
import com.king.mytennis.model.Record;
import com.king.mytennis.model.FileIO;
import com.king.mytennis.multiuser.MultiUser;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.multiuser.MultiUserSelector;
import com.king.mytennis.service.Application;
import com.king.mytennis.service.ExternalRecordTool;
import com.king.mytennis.service.InitService;
import com.king.mytennis.service.LanguageService;
import com.king.mytennis.service.RecordService;
import com.king.mytennis.view.SearchDialog.OnSearchListener;
import com.king.mytennis.view.detail.DetailGallery;
import com.king.mytennis.view.settings.SettingActivity;
import com.king.mytennis.view.settings.SettingProperty;
import com.king.mytennis.view.slidingmenu.SlidingMenuAbstract;
import com.king.mytennis.view.slidingmenu.SlidingMenuCreator;
import com.king.mytennis.view.slidingmenu.SlidingMenuLeft;
import com.king.mytennis.view.slidingmenu.SlidingMenuRight;
import com.king.mytennis.view.slidingmenu.SlidingMenuService;
import com.king.mytennis.view.slidingmenu.SlidingMenuTwoWay;
import com.king.mytennis.view.slidingmenu.SlidingMenuTwoWay.OnSlideChagedListener;
import com.king.mytennis.view.update.recordlist.RecordListViewService;
import com.king.mytennis.view.update.recordlist.RecordListViewUpdate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ManagerActivity extends BaseActivity implements OnSlideChagedListener
		, RecordListViewService, OnSearchListener {

	public static final int VIEW_MAIN=0;
	public static final int VIEW_LIST=1;
	public static final int DLG_SAVEAS=2;
	public static final int DLG_CHOOSEBK=3;
	public static final int DLG_SETRESFILE=4;
	public static final int DLG_CPTLIST=5;
	public static final int DLG_INSERT=6;
	public static final int DLG_SEARCH=8;
	public static final int DLG_UPDATE=9;
	public static final int DLG_LOAD=10;
	public static final int DLG_CHANGE_THEME = 11;

	public static final int INTENT_INSERT = 100;
	public static final int INTENT_SETTING = 101;

	private int curView = VIEW_MAIN;

	private MainView mainView;
	//private RecordListView recordListView;
	private RecordListViewUpdate recordListViewUpdate;
	private UpdateDialog updateDialog;
	private CPTListDlg cptListDlg;

	private ProgressDialog loadingDlg;
	private ProgressDialog loadRecordDialog;
	private Bitmap background;
	private List<Record> recordList;

	private HashMap<String, Bitmap> headMap;
	private boolean hasCachedAllHead;

	private LinearLayout contentView;
	private View mainViewLayout;
	private View recordListViewLayout;
	private LinearLayout menuLayout;
	private LinearLayout menuLayoutRight;
	private SlidingMenuAbstract slidingMenu;
	private int currentSlidingMode;
	private boolean isSlidingEnable;
	private Bitmap slidingBackground;
	private boolean isTwoWayMenu;
	private View backgroundView;
	private SlidingMenuCreator slidingMenuCreator;
	private SlidingMenuService slidingMenuService;
	private LinearLayout userLayout;
	private MultiUserSelector multiUserSelector;
	private MultiUserSelector multiUserRightSelector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);//必须在setContentView之前

		//check default user
		MultiUserManager.getInstance().loadFromPreference(this);

		int mainviewRes = R.layout.mainview;
		if (Application.isLollipop()) {
			mainviewRes = R.layout.mainview_l;
		}

		//sliding feature check
		isSlidingEnable = SettingProperty.isSlidingEnable(this);
		if (isSlidingEnable) {
			currentSlidingMode = SettingProperty.getSlidingMenuMode(this);
			if (currentSlidingMode == SettingProperty.SLIDINGMENU_LEFT) {
				setContentView(R.layout.home_menu_left);
				slidingMenu = (SlidingMenuAbstract) findViewById(R.id.home_slidingmenu);
				backgroundView = slidingMenu;
			}
			else if (currentSlidingMode == SettingProperty.SLIDINGMENU_RIGHT) {
				setContentView(R.layout.home_menu_right);
				slidingMenu = (SlidingMenuAbstract) findViewById(R.id.home_slidingmenu);
				backgroundView = slidingMenu;
			}
			else if (currentSlidingMode == SettingProperty.SLIDINGMENU_TWOWAY) {
				setContentView(R.layout.home_menu_twoway);
				slidingMenu = (SlidingMenuAbstract) findViewById(R.id.home_slidingmenu);
				backgroundView = findViewById(R.id.home_bk_layout);
				isTwoWayMenu = true;
			}

			menuLayout = (LinearLayout) findViewById(R.id.home_menu);
			if (isTwoWayMenu) {
				menuLayoutRight = (LinearLayout) findViewById(R.id.home_menu_right);
				((SlidingMenuTwoWay) slidingMenu).addOnSlideChangedListener(this);
				multiUserRightSelector = new MultiUserSelector(this, new MultiUserSelector.OnUserSelectListener() {

					@Override
					public void onSelect(MultiUser user) {
						multiUserSelector.updateUserGuide(userLayout, user);
						notifyRecordListViewUserChanged();
					}
				});
				LinearLayout layout = (LinearLayout) findViewById(R.id.home_menu_user_layout_right);
				multiUserRightSelector.setUserLayout(layout);
				multiUserRightSelector.updateUserGuide(layout, MultiUserManager.getInstance().getCurrentUser());
			}

			contentView = (LinearLayout) findViewById(R.id.home_content);

			mainViewLayout = LayoutInflater.from(this).inflate(mainviewRes, null);
			contentView.addView(mainViewLayout);

			multiUserSelector = new MultiUserSelector(this, new MultiUserSelector.OnUserSelectListener() {

				@Override
				public void onSelect(MultiUser user) {
					if (isTwoWayMenu) {
						multiUserRightSelector.updateUserGuide((LinearLayout) findViewById(R.id.home_menu_user_layout_right), user);
					}
					notifyRecordListViewUserChanged();
				}
			});
			userLayout = (LinearLayout) findViewById(R.id.home_menu_user_layout);
			multiUserSelector.setUserLayout(userLayout);
			multiUserSelector.updateUserGuide(userLayout, MultiUserManager.getInstance().getCurrentUser());
		}
		else {
			setContentView(mainviewRes);
		}

		//这个Activity下不用在setContentView前就行，真怪！
		initLanguage(getIntent().getExtras().getString("def_language"));

		//初始化工作，加载配置文件，初始化配置信息与背景文件。初始化SqliteOpenHelper实例
		loadingDlg=new ProgressDialog(this);
		loadingDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadingDlg.setMessage(getString(R.string.dlg_loading_msg));
		loadingDlg.show();
		new InitAppThread().start();

		headMap = new HashMap<String, Bitmap>();
	}

	protected void notifyRecordListViewUserChanged() {
		if (curView == VIEW_LIST) {
			recordListViewUpdate.notifyUserChanged();
		}
	}

	@Override
	protected void onDestroy() {
		MySQLHelper.closeHelper();
		super.onDestroy();
	}

	/**
	 * 登录后首次进入mainview完成初始化后的处理
	 */
	private class InitAppThread extends Thread implements Callback {

		private Handler handler;

		public InitAppThread () {
			handler = new Handler(this);
		}

		public void run() {
			InitService initService = new InitService();
			initService.loadConfiguration();
			background = initService.loadBackgound();

			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
		}

		@Override
		public boolean handleMessage(Message msg) {

			if (msg.what == 1) {
				if (background == null) {
					background = BitmapFactory.decodeResource(getResources(), R.drawable.bk_mainview);
				}
				curView = VIEW_MAIN;
				mainView = new MainView(ManagerActivity.this);
				//recordListView = new RecordListView(ManagerActivity.this);
				recordListViewUpdate = new RecordListViewUpdate(ManagerActivity.this, ManagerActivity.this);

				refreshSlidingMenu(mainView);
				loadingDlg.cancel();
			}
			return true;
		}

	}

	private void refreshSlidingMenu(SlidingMenuService service) {
		if (isSlidingEnable) {
			slidingMenuService = service;
			slidingMenu.enableScroll(true);
			if (isTwoWayMenu) {
				slidingMenuCreator = slidingMenuService.loadTwoWayMenu(menuLayout, menuLayoutRight);
			}
			else {
				slidingMenuService.loadMenu(slidingMenu, menuLayout);
			}
		}
	}

	private void initLanguage(String def_language) {

		LanguageService.init(this);
	}

	@Override
	public HashMap<String, Bitmap> getHeadMap() {
		return headMap;
	}

	public void setHeadMap(HashMap<String, Bitmap> headMap) {
		this.headMap = headMap;
	}

	public void setHasCachedAllHead(boolean is) {
		hasCachedAllHead = is;
	}

	public boolean hasCachedAllHead() {
		return hasCachedAllHead;
	}

	public void clearHeadMap() {
		headMap.clear();
	}

	/**
	 * 将background共享给mainview和recordlistview使用
	 * @return 背景对象
	 */
	public Bitmap getBackground() {

		return background;
	}
	/**
	 * chooseBK功能中选择图片立马更改
	 * @param bitmap
	 * @param newDEF_BK
	 */
	private void notifyBackgroundChanged(Bitmap bitmap, String newDEF_BK) {
		background = bitmap;
		mainView.changeBackground(bitmap);
		Configuration.getInstance().DEF_BK = newDEF_BK;
		//configuration.setModified(true);
		FileIO dao = new FileIO();
		dao.saveConfigInfor(Configuration.getInstance());
	}

	public void notifyListViewChanged() {
		//recordListView.reAdapt();
		recordListViewUpdate.reAdapt();
	}

	/**
	 * 在mainview和recordlistview中跳转view与启动dialog
	 * 注意：只要使用了setContentView，那么无论如何都要重新find该View(包括dialog)下的布局与控件
	 * @param viewID
	 * @param reorderData recordlistview下判断是否需要重新排列数据
	 *            当 viewID == VIEW_LIST 有效
	 */
	public void changeView(int viewID, boolean reorderData) {

		if (viewID == VIEW_MAIN) {
			curView = VIEW_MAIN;
			if (isSlidingEnable) {
				contentView.removeAllViews();
				contentView.addView(mainViewLayout);
				refreshSlidingMenu(mainView);
			}
			else {
				setContentView(R.layout.mainview);
			}
			mainView.reInit();
		}
		else if (viewID == VIEW_LIST) {
			curView = VIEW_LIST;
			/**
			 * 第一次访问加载全部记录，线程处理
			 */
			if (recordList == null || MultiUserManager.getInstance().isUserChanged()) {
				load();
			}
			else {
				int resId = R.layout.view_recordlist_update;
				if (Application.isLollipop()) {
					resId = R.layout.view_recordlist_update_l;
				}

				if (isSlidingEnable) {
					if (recordListViewLayout == null) {
						recordListViewLayout = LayoutInflater.from(ManagerActivity.this).inflate(resId, null);
					}
					contentView.removeAllViews();
					contentView.addView(recordListViewLayout);
					refreshSlidingMenu(recordListViewUpdate);

				}
				else {
					setContentView(resId);
				}

				recordListViewUpdate.setNeedReorderData(reorderData);
				recordListViewUpdate.reInit();
			}
		}
	}

	private class LoadRecordThread extends Thread implements Callback {

		private Handler handler;

		public LoadRecordThread () {
			handler = new Handler(this);
		}

		public void run() {
			recordList = new RecordService(ManagerActivity.this).queryAll();

			/**
			 * 已经采用新的复制数据库方法，不再使用下列方法
			 //initService.loadDatabase(recordList);//内置只在第一次创建表有数据操作
			 **/
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
		}
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == 1) {
				loadRecordDialog.cancel();

				if (recordList == null || recordList.size() == 0) {
					Toast.makeText(ManagerActivity.this
							, R.string.nullRecord, Toast.LENGTH_LONG).show();
				}
				else {
					curView = VIEW_LIST;
					//recordListView.init();
					//recordListViewUpdate.init();
					changeView(VIEW_LIST, true);
				}
			}
			return true;
		}

	}
	/**
	 * 两个时刻调用：1.第一次点击进入recordListView的按钮时
	 * 2.菜单项的load record选项
	 */
	public void load() {
		loadRecordDialog=new ProgressDialog(this);
		loadRecordDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadRecordDialog.setMessage(getString(R.string.dlg_loading_msg));
		loadRecordDialog.show();
		new LoadRecordThread().start();
	}

	/**
	 * 启动dialog
	 * @param dialogID
	 */
	public void startDialog(int dialogID) {

		if (dialogID == DLG_SEARCH) {
			new SearchDialog(this, this).show();
		}
		else if (dialogID == DLG_INSERT) {
			/*************version 4.0.4 change***************/
			/*
			if (recordList == null || recordList.size() == 0) {
				Toast.makeText(this, R.string.nullRecord, Toast.LENGTH_LONG).show();
				return;
			}
			if (recordList.size() != new SQLiteDB().getCurRecordSize()) {
				new AlertDialog.Builder(this)
					.setMessage(R.string.insert_warning).show();
				return;
			}
			curView = DLG_INSERT;
			if (insertDialog == null) {
				insertDialog = new InsertDialog(this);
				new InitInsertOrUpdateDialogThread(DLG_INSERT).start();
			}
			else {
				insertDialog.reshow();
			}
			*/
		}
		else if (dialogID == DLG_SAVEAS) {
			new SaveAsDialog(this, null).show();
		}
		else if (dialogID == DLG_SETRESFILE) {
			new SetRecordFileDialog(this).show();
		}
		else if (dialogID == DLG_CHOOSEBK) {
			ChooseBkDialog bkDialog = new ChooseBkDialog(this, new CustomDialog.OnCustomDialogActionListener() {

				@Override
				public boolean onSave(Object object) {
					HashMap<String, Object> map = (HashMap<String, Object>) object;
					int kind = (Integer) map.get(ChooseBkDialog.BK_KIND_KEY);
					if (kind == ChooseBkDialog.BK_KIND_MAINVIEW) {
						String path = (String) map.get("path");
						Bitmap bitmap = new ImageFactory().getBackground(path);
						notifyBackgroundChanged(bitmap, path);
					}
					else if (kind == ChooseBkDialog.BK_KIND_MENU) {
						if (isSlidingEnable) {
							String path = (String) map.get("path");
							Bitmap bitmap = new ImageFactory().getBackground(path);
							if (!isTwoWayMenu) {
								updateBackground(bitmap);
							}
							//in TwoWayMenu mode, every time scroll, it will reload background from preference, no need to handle
						}
					}
					return true;
				}

				@Override
				public void onLoadData(HashMap<String, Object> data) {

				}

				@Override
				public boolean onCancel() {
					return false;
				}
			});
			bkDialog.show();
		}
		else if (dialogID == DLG_CHANGE_THEME) {
			new ChangeThemeDialog(this, new CustomDialog.OnCustomDialogActionListener() {

				@Override
				public boolean onSave(Object object) {
					reload();
					return true;
				}

				@Override
				public void onLoadData(HashMap<String, Object> data) {

				}

				@Override
				public boolean onCancel() {
					return false;
				}
			}).show();
		}
		else if (dialogID == DLG_CPTLIST) {
			if (cptListDlg == null) {
				cptListDlg = new CPTListDlg(this);
			}
			cptListDlg.show(headMap);
			//由于该对话框是listview通过点击某一项后自动关闭对话框，而show返回dialogid用于关闭
			//所有show直接在内部初始化调用
		}
		else if (dialogID == DLG_LOAD) {
			new LoadFromDialog(this, new CustomDialog.OnCustomDialogActionListener() {

				@Override
				public boolean onSave(Object object) {
					if (object != null) {
						File file = (File) object;

						//v5.7.2 update from single database to database directory
//						ExternalRecordTool.replaceDatabase(ManagerActivity.this
//								, file.getPath(), ExternalRecordTool.DATABASE);
						ExternalRecordTool.replaceAllDatabase(ManagerActivity.this
								, file.getPath());

						load();
					}
					return true;
				}

				@Override
				public void onLoadData(HashMap<String, Object> data) {

					File file = new File(Configuration.HISTORY_BASE);

					//v5.7.2 update from single database to database directory
//					String[] names = file.list(new FilenameFilter() {
//
//						@Override
//						public boolean accept(File dir, String filename) {
//
//							return filename.endsWith(".db");
//						}
//					});
					File[] files = file.listFiles(new FileFilter() {

						@Override
						public boolean accept(File file) {

							return file.isDirectory();
						}
					});
					String[] names = new String[files.length];
					for (int i = 0; i < files.length; i ++) {
						names[i] = files[i].getName();
					}

					data.put("data", names);
					data.put("basePath", Configuration.HISTORY_BASE);
				}

				@Override
				public boolean onCancel() {
					// TODO Auto-generated method stub
					return false;
				}
			}).show();
		}
	}

	private void reload() {

		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	@Override
	protected void onResume() {

		String lan = LanguageService.getPrefLanguage(this);
		if (!LanguageService.getMainActivityLanguage(this).equals(lan)) {
			LanguageService.updateMainActivityLanguage(lan);
			reload();
		}
		super.onResume();
	}

	public void startInsertActivity() {

		Intent intent = new Intent();
		intent.setClass(this, RecordEditorActivity.class);
		startActivityForResult(intent, INTENT_INSERT);
	}

	public void startGloryView() {
		Intent intent = new Intent();
		intent.setClass(this, GloryModuleActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == INTENT_INSERT) {
			load();
			Log.i("MyTennis", "onActivityResult INTENT_INSERT");
		}
		if (requestCode == INTENT_SETTING) {
			if (isSlidingEnable != SettingProperty.isSlidingEnable(this)) {
				reload();
			}
			else {
				if (isSlidingEnable) {
					int newMode = SettingProperty.getSlidingMenuMode(this);
					if (currentSlidingMode != newMode) {
						reload();
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 由于涉及其他参数，update对话框单独调用。在recordListView中调用
	 * @param position 目标对象在list中的位置
	 */
	@Override
	public void startUpdateDialog(int position) {
		if (updateDialog == null) {
			updateDialog = new UpdateDialog(this, recordList, position);
			new InitInsertOrUpdateDialogThread(DLG_UPDATE).start();
		}
		else {
			updateDialog.reshow(position);
		}
	}

	/**
	 * 由于涉及其他参数，detail对话框单独调用。在recordListView中调用
	 * @param record 目标record
	 * @param which RecordListView.LT_DETAILSOFTHIS指定通过DAOList在当前list中查询;
	 * 				RecordListView.LT_DETAILSOFALL指定通过DAODB在数据库中查全部

	public void startDetailDialog(Record record, int which) {
	DetailsDialog dlg = null;
	if (which == RecordListView.LT_DETAILSOFTHIS) {
	dlg = new DetailsDialog(this, record, new H2HDAOList(recordList, record.getCompetitor()));
	dlg.show();
	}
	else if (which == RecordListView.LT_DETAILSOFALL) {
	dlg = new DetailsDialog(this, record, new H2HDAODB(this, record.getCompetitor()));
	dlg.show();
	}
	}
	 */
	/************update 140804**************/
	@Override
	public void startDetailDialog(Record record, int which) {
		DetailsDialog dlg = null;
		if (which == RecordListViewUpdate.LT_DETAILSOFTHIS) {
			dlg = new DetailsDialog(this, record, new H2HDAOList(recordList, record.getCompetitor()));
			dlg.show();

		}
		else if (which == RecordListViewUpdate.LT_DETAILSOFALL) {
			//dlg = new DetailsDialog(this, record, new H2HDAODB(this, record.getCompetitor()));
			//dlg.show();

			Intent intent = new Intent();
			intent.setClass(this, DetailGallery.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("record", record);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}


	/**
	 * 将list共享，SearchModel中调用
	 * @return
	 */
	@Override
	public List<Record> getRecordList() {
		return recordList;
	}
	public void setRecordList(List<Record> list) {
		this.recordList = list;
	}

	@Override
	@Deprecated
	public boolean onCreateOptionsMenu(Menu menu) {
		if (curView == VIEW_MAIN) {
			if (mainView != null) {
				mainView.onCreateOptionsMenu(menu);
			}
		}
		else if (curView == VIEW_LIST) {
			//recordListView.onCreateOptionsMenu(menu);
			recordListViewUpdate.onCreateOptionsMenu(menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	@Deprecated
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (curView == VIEW_MAIN) {
			if (mainView != null) {
				mainView.onCreateOptionsMenu(menu);
			}
		}
		else if (curView == VIEW_LIST) {
			//recordListView.onCreateOptionsMenu(menu);
			recordListViewUpdate.onCreateOptionsMenu(menu);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	@Deprecated
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (curView == VIEW_MAIN) {
			mainView.onMenuOpened(featureId, menu);
		}
		else if (curView == VIEW_LIST) {
			//recordListView.onMenuOpened(featureId, menu);
			recordListViewUpdate.onMenuOpened(menu);
		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	@Deprecated
	public boolean onOptionsItemSelected(MenuItem item) {
		if (curView == VIEW_MAIN) {
			mainView.onOptionsItemSelected(item);
		}
		else if (curView == VIEW_LIST) {
			//recordListView.onOptionsItemSelected(item);
			recordListViewUpdate.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		recordListViewUpdate.onContextItemSelected(item);
		return true;
		//return super.onContextItemSelected(item);不注释掉有时候会引起item没有响应
	}

	@Override
	public void onBackPressed() {

		if (curView==VIEW_LIST) {
			//recordListView.onBackPressed();
			recordListViewUpdate.onBackPressed();
		}
		else if (curView==VIEW_MAIN){
			mainView.onBackPressed();
		}
	}

	private class InitInsertOrUpdateDialogThread extends Thread implements Callback{

		private ProgressDialog dialog;
		private Handler handler;
		private int flag;

		public InitInsertOrUpdateDialogThread(int flag) {
			this.flag = flag;
			handler = new Handler(this);
			dialog = new ProgressDialog(ManagerActivity.this);
			dialog.setMessage(getResources().getString(R.string.loading));
			dialog.show();
		}
		public void run() {
			if (flag == DLG_UPDATE) {
				updateDialog.initData();
			}
			handler.sendMessage(new Message());
		}

		@Override
		public boolean handleMessage(Message msg) {

			dialog.cancel();
			if (flag == DLG_UPDATE) {
				updateDialog.show();
			}
			return true;
		}

	}

	public void startSetting() {
		startActivityForResult(new Intent().setClass(this, SettingActivity.class), INTENT_SETTING);
	}

	public void startH2hView() {
		startActivity(new Intent().setClass(this, H2hMainActivity.class));
	}

	public void startRankView() {
		startActivity(new Intent().setClass(this, RankActivity.class));
	}

	@Override
	/**
	 * only called in two-way sliding menu mode
	 */
	public void onSlideChange(final int direction) {

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Bitmap newBackBitmap = null;
				if (direction == OnSlideChagedListener.LEFT) {
					newBackBitmap = slidingMenuCreator.loadBackground(SlidingMenuLeft.BK_KEY);
				}
				else {
					newBackBitmap = slidingMenuCreator.loadBackground(SlidingMenuRight.BK_KEY);
				}

				updateBackground(newBackBitmap);

				if (slidingBackground != null) {
					slidingBackground.recycle();
				}
				slidingBackground = newBackBitmap;
			}
		}, 0);
	}

	@Override
	public void updateBackground(Bitmap bitmap) {
		if (bitmap == null) {
			backgroundView.setBackgroundResource(Constants.SLIDING_MENU_DEFAULT_BK);
		}
		else {
			Drawable drawable = new BitmapDrawable(getResources(), bitmap);
			backgroundView.setBackground(drawable);
		}
		Animation animation = new AlphaAnimation(0.5f, 1);
		animation.setDuration(1000);
		backgroundView.setAnimation(animation);
	}

	@Override
	public void startSaveAsDialog() {
		startDialog(ManagerActivity.DLG_SAVEAS);
	}

	@Override
	public void startThemeDialog() {
		startDialog(ManagerActivity.DLG_CHANGE_THEME);
	}

	@Override
	public void startSearchDialog() {
		startDialog(ManagerActivity.DLG_SEARCH);
	}

	@Override
	public void exitApp() {
		finish();
	}

	@Override
	public void onBackPressCallback() {
		changeView(ManagerActivity.VIEW_MAIN, false);
	}

	@Override
	public void updateRecordList(List<Record> list) {
		setRecordList((ArrayList<Record>)list);
	}

	@Override
	public void startCompetitorDialog() {
		startDialog(DLG_CPTLIST);
	}

	@Override
	public List<Record> onGetSearchList() {
		return recordList;
	}

	@Override
	public void onSearchResult(List<Record> list) {
		this.recordList = list;
		if (recordList == null || recordList.size() == 0) {
			Toast.makeText(this, R.string.nullRecord, Toast.LENGTH_LONG).show();
		}
		else {
			changeView(ManagerActivity.VIEW_LIST, true);
		}
	}

}
