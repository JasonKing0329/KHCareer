package com.king.mytennis.update_v_6_0;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.king.mytennis.glory.GloryModuleActivity;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.Constants;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.model.MySQLHelper;
import com.king.mytennis.model.Record;
import com.king.mytennis.multiuser.MultiUser;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.res.JResource;
import com.king.mytennis.service.InitService;
import com.king.mytennis.service.MenuService;
import com.king.mytennis.update_v_6_0.DragSideBarViewManager.SideListener;
import com.king.mytennis.update_v_6_0.controller.CardDataUnprepareException;
import com.king.mytennis.update_v_6_0.controller.CardManager;
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.H2hMainActivity;
import com.king.mytennis.view.R;
import com.king.mytennis.view.RankActivity;
import com.king.mytennis.view.SearchDialog.OnSearchListener;
import com.king.mytennis.view.editor.RecordEditorActivity;
import com.king.mytennis.view.publicview.DragSideBarTrigger;
import com.king.mytennis.view.publicview.DragSideBar;
import com.king.mytennis.view.publicview.PageTagView;
import com.king.mytennis.view.settings.SettingActivity;
import com.king.mytennis.view.update.recordlist.RecordListViewController;
import com.king.mytennis.view.update.recordlist.RecordListViewUpdate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class ManagerActivity extends BaseActivity implements CardListener
		, SideListener, OnSearchListener{

	public static final String KEY_INIT_MODE = "key_init_mode";
	public static final String KEY_START_FROM_V7 = "key_start_from_v7";
	public static final int INIT_YEAR = 0;
	public static final int INIT_MATCH = 1;
	public static final int INIT_PLAYER = 2;
	private int initMode = INIT_YEAR;

	/**
	 * 如果是从UI 7.0点击翻滚卡片启动该Activity，则退出时不需要提示，也不注销user
	 * 作为UI 6.0的主Activity才需要上面两个过程
	 */
	private boolean isStartFromV7;

	public static final int INTENT_INSERT = 100;
	public static final int INTENT_SETTING = 101;

	private CardPage cardPage;
	private PageTagView pageTagView;
	private CardManager cardManager;
	private CardBkFileNameFilter cardBkFileNameFilter;

	private DragSideBar dragSideBar;
	private DragSideBarViewManager dragSideBarViewManager;

	private FrameLayout recordContainer;
	private RecordListViewUpdate recordListViewUpdate;

	private ProgressDialog loadRecordDialog;

	private MenuService menuService;

	private SparseArray<Bitmap> bkMap;

	private RecordListViewController recordListViewController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//check default user
		MultiUserManager.getInstance().loadUsers(this);
		MultiUserManager.getInstance().loadFromPreference(this);
		JResource.initializeColors();

		setContentView(R.layout.mainview_v_6_0);

		initMode = getIntent().getIntExtra(KEY_INIT_MODE, INIT_YEAR);
		isStartFromV7 = getIntent().getBooleanExtra(KEY_START_FROM_V7, false);

		cardPage = (CardPage) findViewById(R.id.cardpage);
		pageTagView = (PageTagView) findViewById(R.id.cardpage_side);
		dragSideBar = (DragSideBar) findViewById(R.id.cardpage_dragside);
		recordContainer = (FrameLayout) findViewById(R.id.classic_container);

		dragSideBar.setLayoutRes(R.layout.cardview_sidebar);
		dragSideBarViewManager = new DragSideBarViewManager(this, dragSideBar);
		dragSideBarViewManager.setOnSideListener(this);
		cardPage.setBackgroundColor(Color.BLACK);
		cardPage.addCardListener(this);
		cardPage.setDragSidBarTrigger(new DragSideBarTrigger(this, dragSideBar));

		cardManager = new CardManager(this);
		menuService = new MenuService();
		cardBkFileNameFilter = new CardBkFileNameFilter();

		recordListViewController = new RecordListViewController(this, loadHandler, this);
		bkMap = new SparseArray<Bitmap>();
		startLoadRecord();
		new InitAppThread().start();
	}

	private void startLoadRecord() {
		loadRecordDialog=new ProgressDialog(this);
		loadRecordDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadRecordDialog.setMessage(getString(R.string.dlg_loading_msg));
		loadRecordDialog.show();
		recordListViewController.startLoadRecord();
	}

	/**
	 * 登录后首次进入mainview完成初始化后的处理，主要是加载configuration与classic list的背景
	 */
	private class InitAppThread extends Thread implements Callback {

		private Handler handler;
		private Bitmap background;

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
			}
			recordListViewController.updateBackground(background);
			return true;
		}

	}

	@Override
	public void onCardChanged(int from, int to) {
		//更新侧边当前页指示试图
		if (pageTagView.getVisibility() == View.VISIBLE) {
			pageTagView.setFocusTag(cardPage.getCardNumber() - 1 - to);
		}

		//加载与回收卡片背景
		String filterKey = "year";
		if (cardPage.getCardView(0) != null) {
			if (cardPage.getCardView(0) instanceof PlayerCardView) {
				filterKey = "player";
			}
			else if (cardPage.getCardView(0) instanceof MatchCardView) {
				filterKey = "court";
			}
		}
		switchPageBackground(from, to, filterKey);
	}

	/**
	 * 决定应该新加载的背景和回收的背景，确保最多仅有3个bitmap存在，当前页，上一页与下一页
	 * @param from 切换前的页面
	 * @param to 切换后的页面
	 * @param filterKey 加载背景的关键词
	 */
	public void switchPageBackground(int from, int to, String filterKey) {
		int loadIndex = -1;
		int recycleIndex = -1;
		if (to > from) {
			if (to != cardPage.getChildCount() - 1) {
				loadIndex = to + 1;
			}
			recycleIndex = from - 1;
		}
		else {
			if (from != cardPage.getChildCount() - 1) {
				recycleIndex = from + 1;
			}
			loadIndex = to - 1;
		}
		new SwitchCardTask(loadIndex, recycleIndex, filterKey).execute();
	}

	/**
	 * handler notify
	 * @param i
	 */
	private void recycleBackground(int i) {
		Log.e("ManagerActivity", "recycleBackground " + i);
		if (i >= 0 && i < cardPage.getChildCount()) {
			Bitmap bitmap = null;
			CardView cardView = cardPage.getCardView(i);
			cardView.setBackground(null);
			bitmap = bkMap.get(i);
			if (bitmap != null) {
				bitmap.recycle();
				bkMap.remove(i);
			}
		}
	}

	/**
	 * handler notify
	 * @param i
	 * @param bitmap
	 */
	private void loadBackground(int i, Bitmap bitmap) {
		Log.e("ManagerActivity", "loadBackground " + i);
		if (i >= 0 && i < cardPage.getChildCount()) {
			CardView cardView = cardPage.getCardView(i);
			if (bitmap == null) {
				cardView.setBackgroundResource(R.drawable.card_def_player);
			}
			else {
				bkMap.put(i, bitmap);
				cardView.setBackground(new BitmapDrawable(getResources(), bitmap));
			}
		}

	}

	/**
	 * 后台加载背景图片，加载完成后通知卡片回收背景图与新加载背景图
	 */
	private class SwitchCardTask extends AsyncTask<Void, Void, Void> {

		private ImageFactory factory;
		private Bitmap bitmap;
		private int loadIndex, recycleIndex;
		private String filterKey;

		public SwitchCardTask(int load, int recycle, String filterKey) {
			loadIndex = load;
			recycleIndex = recycle;
			this.filterKey = filterKey;
			factory = new ImageFactory();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			if (loadIndex != -1) {
				File file = new File(Configuration.IMG_CARDBK_DIR);
				String extra = filterKey;
				if (extra.equals("court")) {
					//赛事图片还要细分关键词
					extra = getExtraFromCourt(cardManager.getMatchList().get(loadIndex).get("court"));
				}
				cardBkFileNameFilter.setKey(extra);
				File files[] = file.listFiles(cardBkFileNameFilter);
				if (files.length > 0) {
					int index = Math.abs(new Random().nextInt()) % files.length;
					bitmap = factory.getBackground(files[index].getPath());
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			recycleBackground(recycleIndex);
			if (loadIndex != -1) {
				loadBackground(loadIndex, bitmap);
			}
		}

	}

	/**
	 * image in sdcard directory name follow the rule:
	 * year_match_player_xxx.xx
	 * the order of year, match, player can be different
	 * match value is hard/clay/grass/inhard
	 */
	private class CardBkFileNameFilter implements FilenameFilter {

		private String[] key;
		public void setKey(String... key) {
			this.key = key;
		}
		@Override
		public boolean accept(File file, String name) {

			String[] arr = name.split("_");
			for (int i = 0; i < arr.length && i < 3; i ++) {
				for (int j = 0; j < key.length; j ++) {
					if (arr[i].equals(key[j])) {
						return true;
					}
				}
			}
			return false;
		}

	}

	/**
	 * 初始化player卡片
	 */
	private void initPlayerCards() {
		cardPage.reset();
		cardManager.preparePlayerIndexCard();

		List<String> indexList = cardManager.getPlayerIndexList();
		if (indexList != null && indexList.size() > 0) {
			File file = new File(Configuration.IMG_CARDBK_DIR);
			cardBkFileNameFilter.setKey("player");
			File files[] = file.listFiles(cardBkFileNameFilter);

			ImageFactory factory = new ImageFactory();
			try {
				for (int i = 0; i < indexList.size(); i ++) {
					PlayerCardView cardView = new PlayerCardView(this);
					cardView.setCardManager(cardManager);
					if (i > indexList.size() - 3) {//背景图只初始化头两个
						if (files == null || file.length() == 0) {
							cardView.setBackgroundResource(R.drawable.card_def_player);
						}
						else {
							int index = Math.abs(new Random().nextInt()) % files.length;
							Bitmap bitmap = factory.getBackground(files[index].getPath());
							cardView.setBackground(new BitmapDrawable(getResources(), bitmap));
							bkMap.put(i, bitmap);
						}
					}
					cardView.setIndex(indexList.get(i));
					cardView.setCardData(cardManager.getPlayerIndexCard(indexList.get(i)));
					cardPage.addCard(cardView);
				}
			} catch (CardDataUnprepareException e) {
				e.printStackTrace();
			}

//			final int pages = indexList.size();
//			new Handler().postDelayed(new Runnable() {
//
//				@Override
//				public void run() {
//					pageTagView.setTagNumber(pages);
//				}
//			}, 100);
		}
	}

	/**
	 * 初始化match卡片
	 */
	private void initMatchCards() {
		cardPage.reset();
		cardManager.prepareMatchCard();
		List<HashMap<String, String>> matchList = cardManager.getMatchList();
		if (matchList != null && matchList.size() > 0) {
			File file = new File(Configuration.IMG_CARDBK_DIR);

			ImageFactory factory = new ImageFactory();

			try {
				for (int i = 0; i < matchList.size(); i ++) {
					MatchCardView cardView = new MatchCardView(this);
					if (i > matchList.size() - 3) {//背景图只初始化头两个
						String extra = getExtraFromCourt(matchList.get(i).get("court"));
						cardBkFileNameFilter.setKey(extra);
						File files[] = file.listFiles(cardBkFileNameFilter);

						if (files.length == 0) {
							cardView.setBackgroundResource(R.drawable.card_def_match);
						}
						else {
							int index = Math.abs(new Random().nextInt()) % files.length;
							Bitmap bitmap = factory.getBackground(files[index].getPath());
							cardView.setBackground(new BitmapDrawable(getResources(), bitmap));
							bkMap.put(i, bitmap);
						}
					}
					cardView.setMatch(matchList.get(i));
					cardView.setCardData(cardManager.getMatchCard(matchList.get(i).get("match")));
					cardPage.addCard(cardView);
				}
			} catch (CardDataUnprepareException e) {
				e.printStackTrace();
			}

			final int pages = matchList.size();
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					pageTagView.setTagNumber(pages);
					pageTagView.setFocusTag(0);
				}
			}, 100);
		}

	}

	/**
	 * 根据赛事的场地属性细分背景图的关键词
	 * @param court 硬地/红土/草地/室内硬地
	 * @return
	 */
	private String getExtraFromCourt(String court) {
		String extra = "hard";
		String[] courts = Constants.RECORD_MATCH_COURTS;
		if (court.equals(courts[1])) {
			extra = "clay";
		}
		else if (court.equals(courts[2])) {
			extra = "grass";
		}
		else if (court.equals(courts[3])) {
			extra = "inhard";
		}
		return extra;
	}

	/**
	 * 初始化卡片只初始化简单数据，图片仅仅缓存前两个，在切换时进行新加载与回收
	 */
	private void initYearCards() {

		cardPage.reset();
		cardManager.prepareYearCard();
		List<String> yearList = cardManager.getYearList();
		if (yearList != null && yearList.size() > 0) {
			File file = new File(Configuration.IMG_CARDBK_DIR);
			cardBkFileNameFilter.setKey("year");
			File files[] = file.listFiles(cardBkFileNameFilter);

			ImageFactory factory = new ImageFactory();

			try {
				for (int i = 0; i < yearList.size(); i ++) {
					TimeLineCardView cardView = new TimeLineCardView(this);
					cardView.setCardManager(cardManager);
					if (i > yearList.size() - 3) {//只初始化头两个
						if (files.length == 0) {
							cardView.setBackgroundResource(R.drawable.card_def_year);
						}
						else {
							int index = Math.abs(new Random().nextInt()) % files.length;
							Bitmap bitmap = factory.getBackground(files[index].getPath());
							cardView.setBackground(new BitmapDrawable(getResources(), bitmap));
							bkMap.put(i, bitmap);
						}
					}
					cardView.setYear(yearList.get(i));
					cardView.setCardData(cardManager.getYearCard(yearList.get(i)));
					cardPage.addCard(cardView);
				}
			} catch (CardDataUnprepareException e) {
				e.printStackTrace();
			}

			final int pages = yearList.size();
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					pageTagView.setTagNumber(pages);
					pageTagView.setFocusTag(0);
				}
			}, 100);
		}

	}

	@Override
	public void onBackPressed() {
		//关闭侧边拉出菜单
		if (dragSideBar.isOpen()) {
			dragSideBar.dismiss(true);
			return;
		}

		//从classic list返回到卡片视图
		if (recordContainer.getVisibility() == View.VISIBLE) {
			recordContainer.setVisibility(View.GONE);
			cardPage.setVisibility(View.VISIBLE);
			return;
		}

		//提示退出
		View view = cardPage.getChildAt(cardPage.getForgroundIndex());
		if (view instanceof PlayerCardView) {
			//如果是player卡片，先判断是否从二级页面返回到一级页面
			PlayerCardView playerCardView = (PlayerCardView) view;
			if (!playerCardView.backToIndexView()) {

				if (isStartFromV7) {
					super.onBackPressed();
					return;
				}
				popupDismiss();
			}
		}
		else {

			if (isStartFromV7) {
				super.onBackPressed();
				return;
			}
			popupDismiss();
		}
	}

	private void popupDismiss() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.warning)
				.setMessage(R.string.exit_msg)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								MySQLHelper.closeHelper();

								if (!isStartFromV7) {
									MultiUserManager.destroy();//如果不主动销毁，下一次进入应用程序就不会new该实例了
									//如果是从任务管理器关掉应用则不用主动销毁
								}

								finish();
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
	}

	private Handler loadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				loadRecordDialog.cancel();

				if (recordListViewController.getRecordList() == null
						|| recordListViewController.getRecordList().size() == 0) {
					Toast.makeText(ManagerActivity.this
							, R.string.nullRecord, Toast.LENGTH_LONG).show();
				}
				cardManager.setRecordList(recordListViewController.getRecordList());
			}

			if (initMode == INIT_MATCH) {
				initMatchCards();
			}
			else if (initMode == INIT_PLAYER) {
				initPlayerCards();
			}
			else {
				initYearCards();
			}
		}
	};

	@Override
	public void onMatchCard() {
		pageTagView.setVisibility(View.VISIBLE);
		initMatchCards();
	}

	@Override
	public void onYearCard() {
		pageTagView.setVisibility(View.VISIBLE);
		initYearCards();
	}

	@Override
	public void onPlayerCard() {
		pageTagView.setVisibility(View.GONE);
		initPlayerCards();
	}

	@Override
	public void onChangeUser(MultiUser user) {
		cardManager.reload();
		startLoadRecord();
	}

	@Override
	public void onInsert() {
		Intent intent = new Intent();
		intent.setClass(this, RecordEditorActivity.class);
		startActivityForResult(intent, INTENT_INSERT);
	}

	@Override
	public void onGlory() {
		Intent intent = new Intent();
		intent.setClass(this, GloryModuleActivity.class);
		startActivity(intent);
	}

	@Override
	public void onSetting() {
		startActivityForResult(new Intent().setClass(this, SettingActivity.class), INTENT_SETTING);
	}

	@Override
	public void onMenuItem(int itemId) {
		switch (itemId) {
			case SideListener.MENU_SAVE:
				showSaveResult(menuService.saveDatabases());
				break;
			case SideListener.MENU_LOAD:
				//userActivity.load();
				recordListViewController.startDialog(RecordListViewController.DLG_LOAD);
				break;
			case SideListener.MENU_SAVEAS:
				recordListViewController.startDialog(RecordListViewController.DLG_SAVEAS);
				break;
			case SideListener.MENU_CHANGEBK:
				recordListViewController.startDialog(RecordListViewController.DLG_CHOOSEBK);
				break;
			case SideListener.MENU_CHANGETHEME:
				recordListViewController.startDialog(RecordListViewController.DLG_CHANGE_THEME);
				break;
			case SideListener.MENU_SAVECONF:
				showSaveResult(menuService.saveConfiguration());
				break;
			case SideListener.MENU_EXIT:
				finish();
				break;
			default:
				break;
		}
	}

	private void showSaveResult(String message) {

		if (message.equals("ok")) {
			Toast.makeText(this, R.string.save_ok, Toast.LENGTH_LONG)
					.show();
		} else {
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onRank() {
		startActivity(new Intent().setClass(this, RankActivity.class));
	}

	@Override
	public void onH2H() {
		startActivity(new Intent().setClass(this, H2hMainActivity.class));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == INTENT_INSERT) {
			startLoadRecord();
			Log.i("MyTennis", "onActivityResult INTENT_INSERT");
		}
		if (requestCode == INTENT_SETTING) {

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClassicList() {
		if (recordListViewUpdate == null) {
			View view = LayoutInflater.from(this).inflate(R.layout.view_recordlist_update_l, null);
			recordContainer.addView(view);
			recordListViewUpdate = new RecordListViewUpdate(this, recordListViewController);
			recordListViewUpdate.init();
		}
		cardPage.setVisibility(View.GONE);
		recordContainer.setVisibility(View.VISIBLE);
	}

	protected void notifyRecordListViewUserChanged() {
		recordListViewUpdate.notifyUserChanged();
	}

	public void notifyListViewChanged() {
		//recordListView.reAdapt();
		recordListViewUpdate.reAdapt();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		recordListViewUpdate.onContextItemSelected(item);
		return true;
		//return super.onContextItemSelected(item);不注释掉有时候会引起item没有响应
	}

	public void onColorEdited() {
		JResource.saveColorUpdate(this);
	}

	@Override
	public List<Record> onGetSearchList() {
		return recordListViewController.getRecordList();
	}

	@Override
	public void onSearchResult(List<Record> list) {
		recordListViewController.updateRecordList(list);

		recordListViewUpdate.setNeedReorderData(true);
		recordListViewUpdate.reInit();
	}

}
