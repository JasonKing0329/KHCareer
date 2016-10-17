package com.king.mytennis.view.update.recordlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.king.mytennis.glory.ActionBar;
import com.king.mytennis.glory.GloryController;
import com.king.mytennis.glory.ActionBar.ActionBarListener;
import com.king.mytennis.glory.GloryMatchDialog;
import com.king.mytennis.http.RequestCallback;
import com.king.mytennis.http.bean.ImageUrlBean;
import com.king.mytennis.model.Constants;
import com.king.mytennis.model.NamePinyinPool;
import com.king.mytennis.model.Record;
import com.king.mytennis.model.RecordListModel;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.service.MenuService;
import com.king.mytennis.service.RecordService;
import com.king.mytennis.view.ArcButtonView;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;
import com.king.lib.colorpicker.ColorPicker;
import com.king.lib.colorpicker.ColorPickerSelectionData;
import com.king.mytennis.view.publicview.IndexCreator;
import com.king.mytennis.view.publicview.SideBar;
import com.king.mytennis.view.publicview.SideBar.OnTouchingLetterChangedListener;
import com.king.mytennis.view.settings.SettingProperty;
import com.king.mytennis.view.slidingmenu.SlidingMenuAbstract;
import com.king.mytennis.view.slidingmenu.SlidingMenuCreator;
import com.king.mytennis.view.slidingmenu.SlidingMenuLeft;
import com.king.mytennis.view.slidingmenu.SlidingMenuService;
import com.king.mytennis.view_v_7_0.interaction.controller.InteractionController;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;

public class RecordListViewUpdate implements
		OnGroupClickListener, OnGroupExpandListener, OnGroupCollapseListener, OnScrollListener, SlidingMenuService
		, OnChildClickListener, OnCreateContextMenuListener, OnLongClickListener, Runnable
		, OnTouchingLetterChangedListener {

	/**********arc button view part***********/
	private ImageButton buttonExpand, buttonExit, buttonShowall, buttonMusic, buttonShowByDate, buttonShowByPlayer, buttonCptList;
	private View chosedButton;
	private RelativeLayout arcButtonLayout;
	private ArcButtonView arcButtonView;
	private boolean arcHasExpanded;
	/*****************************************/

	/**********actionbar part***********/
	private ActionBar actionBar;
	/*****************************************/

	/**********indexview part***********/
	private SideBar sideBar;
	private IndexCreator indexCreator;
	private TextView indexPopup;
	/*****************************************/
	
	/*
	private final int MENU_EXACTSELECT=10;
	private final int MENU_SHOWALL=11;
	private final int MENU_SORT=12;
	private final int MENU_SORT_COMPETITOR=121;
	private final int MENU_SORT_DATE=122;
	private final int MENU_CMPLIST=7;
	private final int MENU_RANKING=8;
	private final int MENU_GLORY=9;
	private final int MENU_SAVEAS=2;
	private final int MENU_SHOW_BY_DATE=201;
	private final int MENU_SHOW_BY_PLAYER=202;
	*/
	private final int MENU_EXACTSELECT=0;
	private final int MENU_SHOWALL=1;
	private final int MENU_SHOW_BY=2;
	private final int MENU_CMPLIST=3;
	private final int MENU_CHANGE_THEME=4;
	private final int MENU_SAVEAS=5;
	private final int MENU_EXIT=6;

	public static final int LT_DETAILSOFTHIS=2;
	public static final int LT_DETAILSOFALL=3;

	private final int SHOW_BY_DATE=0;
	private final int SHOW_BY_PLAYER=1;
	private int currentShowMode = SHOW_BY_DATE;

	public static int GROUP_BY_DATE = 0;
	public static int GROUP_BY_PLAYER = 1;

	private Context userActivity;
	private MenuService menuService;

	private List<Record> tempList;
	private List<Record> originList;

	private ProgressDialog reloadDialog;
	private TextView winRateView;
	private CheckBox showActionBarBox;
	private boolean showActionBarBoxStatus;
	private ExpandableListView expandableListView;
	private RecordListDateAdapter dateAdapter;
	private RecordListPlayerAdapter playerAdapter;
	private List<List<HashMap<String, String>>> recordList;
	private List<HashMap<String, String>> titleList;
	private TextView noContentView;

	private HashMap<Integer, Boolean> expandStateMap;
	private boolean isResetExpandStateMap;

	private int scrollPositionY;
	private int arcButtonVisibility;
	private boolean needReorderData;

	private HashMap<String, String> namePinyinMap;

	private RecordListViewService recordListViewService;

	public RecordListViewUpdate(Context context, RecordListViewService service) {
		userActivity = context;
		recordListViewService = service;
		menuService = new MenuService();
		expandStateMap = new HashMap<Integer, Boolean>();
		setNeedReorderData(true);
	}

	public void notifyBackgroundUpdated(Bitmap bitmap) {
		Activity view = (Activity) userActivity;
		RelativeLayout layout=(RelativeLayout)view.findViewById(R.id.layout_lookresult);
		layout.setBackground(new BitmapDrawable(view.getResources(), bitmap));
	}

	public void init(){

		Log.i("MyTennis", "init");
		originList = recordListViewService.getRecordList();
		fillTempList();
		Activity view = (Activity) userActivity;
		RelativeLayout layout=(RelativeLayout)view.findViewById(R.id.layout_lookresult);
		layout.setBackground(new BitmapDrawable(view.getResources(), recordListViewService.getBackground()));

		winRateView = (TextView) view.findViewById(R.id.result_tv_count);
		expandableListView = (ExpandableListView) view.findViewById(R.id.recordList);

		sideBar = (SideBar) view.findViewById(R.id.indexview_sidebar);
		indexPopup = (TextView) view.findViewById(R.id.indexview_popup);
		sideBar.setOnTouchingLetterChangedListener(this);
		sideBar.setTextView(indexPopup);

		/**********arc button view part***********/
		buttonExit = (ImageButton) view.findViewById(R.id.button_composer_exit);
		buttonShowall = (ImageButton) view.findViewById(R.id.button_composer_showall);
		buttonMusic = (ImageButton) view.findViewById(R.id.button_composer_search);
		buttonShowByDate = (ImageButton) view.findViewById(R.id.button_composer_show_by_date);
		buttonShowByPlayer = (ImageButton) view.findViewById(R.id.button_composer_show_by_player);
		buttonCptList = (ImageButton) view.findViewById(R.id.button_composer_cptlist);
		buttonExpand = (ImageButton) view.findViewById(R.id.arcbutton_expand);
		arcButtonLayout = (RelativeLayout) view.findViewById(R.id.arcbutton);
		arcButtonLayout.setVisibility(View.VISIBLE);
		noContentView = (TextView) view.findViewById(R.id.no_content_view);
		showActionBarBox = (CheckBox) view.findViewById(R.id.recordlistview_showactionbar);
		buttonExit.setOnLongClickListener(this);
		buttonShowall.setOnLongClickListener(this);
		buttonMusic.setOnLongClickListener(this);
		buttonShowByDate.setOnLongClickListener(this);
		buttonShowByPlayer.setOnLongClickListener(this);
		buttonCptList.setOnLongClickListener(this);
		OnClickListener btlistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				chosedButton = v;
				new Handler().postDelayed(RecordListViewUpdate.this, 500);
			}
		};
		List<ImageButton> list = new ArrayList<ImageButton>();
		list.add(buttonExpand);
		list.add(buttonExit);
		list.add(buttonMusic);
		list.add(buttonCptList);
		list.add(buttonShowall);
		list.add(buttonShowByDate);
		list.add(buttonShowByPlayer);

		//半径和按钮大小需要适应不同屏幕
		int radius = userActivity.getResources().getDimensionPixelSize(R.dimen.arc_radius);
		int buttonSize = userActivity.getResources().getDimensionPixelSize(R.dimen.arc_button_size);
		arcButtonView = new ArcButtonView(userActivity, radius, buttonSize, list, btlistener);
		winRateView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (arcButtonLayout.getVisibility() == View.GONE) {
					arcButtonLayout.setVisibility(View.VISIBLE);
					arcButtonLayout.startAnimation(AnimationUtils.loadAnimation(userActivity, R.anim.appear));
				}
				else {
					arcButtonLayout.setVisibility(View.GONE);
					arcButtonLayout.startAnimation(AnimationUtils.loadAnimation(userActivity, R.anim.disappear));
				}

			}
		});
		showActionBarBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					actionBar.show();
				}
				else {
					actionBar.hide();
				}
			}
		});
		/*****************************************/

		reAdapt();
		//userActivity.setProgressBarIndeterminate(true);

		/**********actionbar view part***********/
		actionBar = new ActionBar(userActivity, new ActionBarController());
		actionBar.addMenuIcon();
		actionBar.addSearchIcon();
		actionBar.addColorIcon();
		actionBar.setTitle(userActivity.getResources().getString(R.string.record_listview_allrecords));
		actionBar.hide();
		/*****************************************/

//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				indexView.setMaxTotalHeight(expandableListView.getHeight());
//			}
//		}, 300);
	}

	private void showNoContent(boolean show) {
		expandableListView.setVisibility(show ? View.GONE:View.VISIBLE);
		noContentView.setVisibility(show ? View.VISIBLE:View.GONE);
	}

	private class ActionBarController implements ActionBarListener {

		private ListAdapter adapter;
		private String[] menuItems;
		@Override
		public void onOk() {
			// TODO Auto-generated method stub

		}

		@Override
		public ListAdapter createMenu() {
			menuItems = userActivity.getResources().getStringArray(R.array.recordlistview_menu);
			adapter = new ArrayAdapter<String>(userActivity
					, android.R.layout.simple_dropdown_item_1line, menuItems);
			return adapter;
		}

		@Override
		public ListAdapter onPrepareMenu() {
			String target = null;
			if (currentShowMode == SHOW_BY_DATE) {
				target = userActivity.getResources().getString(R.string.menu_show_by_player);
			}
			else {
				target = userActivity.getResources().getString(R.string.menu_show_by_date);
			}
			if (target.equals(menuItems[MENU_SHOW_BY])) {
				return null;
			}
			else {
				menuItems[MENU_SHOW_BY] = target;
				adapter = new ArrayAdapter<String>(userActivity
						, android.R.layout.simple_dropdown_item_1line, menuItems);
				return adapter;
			}
		}

		@Override
		public void onMenuSelected(int index) {
			switch (index) {
				case MENU_EXACTSELECT:
					setNeedReorderData(true);
					search();
					break;
				case MENU_SHOWALL:
					setNeedReorderData(true);
					showAll();
					break;
				case MENU_SHOW_BY:
					if (currentShowMode == SHOW_BY_DATE) {
						setNeedReorderData(true);
						showByPlayer();
						resetExpandStateMap();
					}
					else {
						setNeedReorderData(true);
						showByDate();
						resetExpandStateMap();
					}
					break;
				case MENU_CMPLIST:
					showCompetitors();
					break;
				case MENU_SAVEAS:
					recordListViewService.startSaveAsDialog();
					break;
				case MENU_CHANGE_THEME:
					recordListViewService.startThemeDialog();
					break;
				case MENU_EXIT:
					recordListViewService.exitApp();
					break;
				default:
					break;
			}
		}

		@Override
		public void onCancel() {

		}

		@Override
		public void onBack() {
			onBackPressed();
		}

		@Override
		public void onTextChanged(String text, int start, int before, int count) {

			tempList.clear();

			if (text.toString().trim().length() == 0) {
				fillTempList();
				reAdapt();
				actionBar.setTitle(userActivity.getResources().getString(R.string.record_listview_allrecords));
				return;
			}

			//startWith排在前面，contains排在后面
			actionBar.setTitle(text);
			String target = null, prefix = text.toString().toLowerCase();
			for (int i = 0; i < originList.size(); i ++) {
				if (currentShowMode == SHOW_BY_DATE) {
					target = originList.get(i).getMatch().toLowerCase();
				}
				else {
					target = originList.get(i).getCompetitor().toLowerCase();
				}
				if (target.contains(prefix)) {
					tempList.add(originList.get(i));
				}
			}
			reAdapt();

		}

		@Override
		public void onDelete() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onIconClick(View view) {
			if (view.getId() == R.id.actionbar_index) {
				if (currentShowMode == SHOW_BY_PLAYER) {
					if (sideBar.getVisibility() == View.VISIBLE) {
						sideBar.setVisibility(View.GONE);
					}
					else {
						sideBar.setVisibility(View.VISIBLE);
					}
				}
			}
			else if (view.getId() == R.id.actionbar_color) {
				ColorPicker colorPicker = new ColorPicker(userActivity
						, new ColorPicker.OnColorPickerListener() {

					@Override
					public void onColorChanged(String key, int newColor) {
						actionBar.setBackgroundColor(newColor);
					}

					@Override
					public void onColorSelected(int color) {
						actionBar.setBackgroundColor(color);
					}

					@Override
					public void onColorSelected(
							List<ColorPickerSelectionData> list) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onColorCancleSelect() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onApplyDefaultColors() {
						// TODO Auto-generated method stub

					}
				});
				colorPicker.initColor(userActivity.getResources().getColor(R.color.actionbar_bk_blue));
				colorPicker.show();
			}
		}

	};

	/**
	 * 在init()之后才能调用，用于初始化界面为Player list
	 */
	public void switchToPlayerList() {
		setNeedReorderData(true);
		showByPlayer();
		resetExpandStateMap();
	}

	public void reInit() {
		Log.i("MyTennis", "reInit");
		init();
		showActionBarBox.setChecked(showActionBarBoxStatus);
		if (showActionBarBox.isChecked()) {
			actionBar.show();
		}
		restoreExpandState();
	}

	public void onBackPressed() {
		Log.i("MyTennis", "onBackPressed");
		if (!actionBar.dismissMenu()) {
			saveExpandState();
			recordListViewService.onBackPressCallback();
		}
	}

	public void onCreateOptionsMenu(Menu menu) {
		/*
		menu.add(1, MENU_EXACTSELECT, 0, R.string.menu_exactselect);
		menu.add(1, MENU_SHOWALL, 1, R.string.menu_showall);
		menu.add(1, MENU_SAVEAS, 2, R.string.menu_saveas);
		menu.add(1, MENU_CMPLIST, 3, R.string.menu_cmplist);
		menu.add(1, MENU_RANKING, 4, R.string.menu_ranking);
		menu.add(1, MENU_GLORY, 5, R.string.menu_glory);
		SubMenu subMenu=menu.addSubMenu(1, MENU_SORT, 6, R.string.menu_sort);
		subMenu.setIcon(android.R.drawable.ic_menu_more);
		subMenu.add(2, MENU_SORT_COMPETITOR, 0, R.string.menu_sort_comp);
		subMenu.add(2, MENU_SORT_DATE, 1, R.string.menu_sort_date);
*/
	}

	public void onMenuOpened(Menu menu) {
		/*
		menu.clear();
		if (currentShowMode == SHOW_BY_DATE) {
			menu.add(1, MENU_SHOW_BY_PLAYER, 0, R.string.menu_show_by_player);
		}
		else {
			menu.add(1, MENU_SHOW_BY_DATE, 0, R.string.menu_show_by_date);
		}
		menu.add(1, MENU_EXACTSELECT, 1, R.string.menu_exactselect);
		menu.add(1, MENU_SHOWALL, 2, R.string.menu_showall);
		menu.add(1, MENU_SAVEAS, 3, R.string.menu_saveas);
		menu.add(1, MENU_CMPLIST, 4, R.string.menu_cmplist);
		menu.add(1, MENU_RANKING, 5, R.string.menu_ranking);
		menu.add(1, MENU_GLORY, 6, R.string.menu_glory);
		SubMenu subMenu=menu.addSubMenu(1, MENU_SORT, 7, R.string.menu_sort);
		subMenu.setIcon(android.R.drawable.ic_menu_more);
		subMenu.add(2, MENU_SORT_COMPETITOR, 0, R.string.menu_sort_comp);
		subMenu.add(2, MENU_SORT_DATE, 1, R.string.menu_sort_date);
*/
	}

	public void onOptionsItemSelected(MenuItem item) {
		/*
		switch (item.getItemId()) {
		case MENU_SAVEAS:
			userActivity.startDialog(ManagerActivity.DLG_SAVEAS);
			break;
		case MENU_EXACTSELECT:
			setNeedReorderData(true);
			search();
			break;
		case MENU_SHOWALL:
			setNeedReorderData(true);
			showAll();
			break;
		case MENU_SORT_COMPETITOR:
//			loadingDlg=new ProgressDialog(MainView.this);
//	    	loadingDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//	    	loadingDlg.setMessage(getString(R.string.dlg_sorting_msg));
//	    	loadingDlg.show();
//			new Thread(){
//				public void run(){
//					sortList.sortByCompetitor(list);
//					Message msg=new Message();
//					msg.what=SORT_OK;
//					sortHandler.sendMessage(msg);
//				}
//			}.start();
			break;
		case MENU_SORT_DATE:
//			loadingDlg=new ProgressDialog(MainView.this);
//	    	loadingDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//	    	loadingDlg.setMessage(getString(R.string.dlg_sorting_msg));
//	    	loadingDlg.show();
//			new Thread(){
//				public void run(){
//					sortList.sortByDate(list);
//					Message msg=new Message();
//					msg.what=SORT_OK;
//					sortHandler.sendMessage(msg);
//				}
//			}.start();
			break;
		case MENU_CMPLIST:
			showCompetitors();
			break;
		case MENU_RANKING:
			break;
		case MENU_SHOW_BY_DATE:
			showByDate();
			break;
		case MENU_SHOW_BY_PLAYER:
			showByPlayer();
			break;
		case MENU_GLORY:
			userActivity.startGloryView();
			break;
		default:
			break;
		}
*/
	}
	private void showByPlayer() {
		Log.i("MyTennis", "showByPlayer");
		actionBar.addIndexIcon(true);
		currentShowMode = SHOW_BY_PLAYER;
		reAdapt();
	}

	private void showByDate() {
		Log.i("MyTennis", "showByDate");
		actionBar.addIndexIcon(false);
		currentShowMode = SHOW_BY_DATE;
		reAdapt();
	}

	private void showCompetitors() {
		saveExpandState();
		recordListViewService.startCompetitorDialog();
	}

	private void search() {
		saveExpandState();
		recordListViewService.startSearchDialog();
	}

	private void showAll() {
		Log.i("MyTennis", "showAll");
		reloadDialog = new ProgressDialog(userActivity);
		reloadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		reloadDialog.setMessage(userActivity.getString(R.string.dlg_loading_msg));
		reloadDialog.show();
		new Thread(){
			public void run() {

				originList = menuService.loadRecords(userActivity);
				Message msg = new Message();
				msg.what = 1;
				reloadHandler.sendMessage(msg);
			}
		}.start();
	}

	Handler reloadHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				if (originList != null) {
					recordListViewService.updateRecordList(originList);
					fillTempList();
					Toast.makeText(userActivity, R.string.loading_ok, Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(userActivity, R.string.loading_fail, Toast.LENGTH_LONG).show();
				}
				reAdapt();
			}
			actionBar.resetSearchLayout();
			actionBar.setTitle(userActivity.getResources().getString(R.string.record_listview_allrecords));
			reloadDialog.cancel();
			super.handleMessage(msg);
		}

	};

	private void fillTempList() {
		if (originList != null) {
			if (tempList == null) {
				tempList = new ArrayList<Record>();
			}
			else {
				tempList.clear();
			}
			for (Record record:originList) {
				tempList.add(record);
			}
		}
	}

	/*
	@Deprecated // 由ImageUtil完成
	ImageLoader imageLoader = new ImageLoader() {

		@Override
		public Bitmap loadPlayerHead(String name) {

			Bitmap bitmap = headMap.get(name);
			if (bitmap == null && hasLoadHeadMap.get(name) == null) {
				try {
					bitmap = new ImageFactory().getPlayerHead(name, 200);
					headMap.put(name, bitmap);
					hasLoadHeadMap.put(name, true);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					bitmap = null;
				}
			}
			return bitmap;
		}

		@Override
		public Bitmap loadMatchImage(String name, String court) {
			Bitmap bitmap = matchMap.get(name);
			if (bitmap == null && hasLoadMatchMap.get(name) == null) {
				try {
					bitmap = new ImageFactory().getMatchHead(name, court, 200);
					matchMap.put(name, bitmap);
					hasLoadMatchMap.put(name, true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					bitmap = null;
				}
			}
			return bitmap;
		}

	};
	*/

	public void reAdapt() {

		Log.i("MyTennis", "reAdapt");
		if (tempList == null || tempList.size() == 0) {
			showNoContent(true);
		}
		else {
			showNoContent(false);
		}

		winRateView.setText(new RecordListModel().getCountInfor(tempList));

		if (currentShowMode == SHOW_BY_DATE) {
			hideIndexView();
			if (needReorderData()) {//design for restore state
				adjustListDataByDate();
			}
			dateAdapter = new RecordListDateAdapter(userActivity, titleList, recordList);
			expandableListView.setAdapter(dateAdapter);

			if (titleList.size() > 0 && expandStateMap.size() == 0) {
				expandableListView.expandGroup(0);
				expandStateMap.put(0, true);
			}
		}
		else if (currentShowMode == SHOW_BY_PLAYER) {
			if (needReorderData()) {//design for restore state
				adjustListDataByPlayer();
			}
			playerAdapter = new RecordListPlayerAdapter(userActivity, titleList, recordList);
			expandableListView.setAdapter(playerAdapter);
			createIndexView();

			if (titleList.size() == 1 && expandStateMap.size() == 0) {
				expandableListView.expandGroup(0);
				expandStateMap.put(0, true);
			}
		}
		expandableListView.setOnGroupClickListener(this);
		expandableListView.setOnCreateContextMenuListener(this);
		expandableListView.setOnGroupExpandListener(this);
		expandableListView.setOnGroupCollapseListener(this);
		expandableListView.setOnChildClickListener(this);
		expandableListView.setOnScrollListener(this);
	}

	private void hideIndexView() {
		sideBar.setVisibility(View.GONE);
	}

	//only called in reAdapt->currentShowMode == SHOW_BY_PLAYER
	private void createIndexView() {
		//write in here because actionbar can choose to show or hide, so expandableListView height not steady
		//somehow, the height is still a little more than reality
		sideBar.forceSetHeight(expandableListView.getHeight());

		createNamePinyinPool();
		if (indexCreator == null) {
			indexCreator = new IndexCreator(sideBar);
		}
		indexCreator.createFrom(titleList, namePinyinMap);
		sideBar.setVisibility(View.VISIBLE);
	}

	private void adjustListDataByPlayer() {
		Log.i("MyTennis", "adjustListDataByPlayer");
		if (tempList == null || tempList.size() == 0) {
			return;
		}
		if (titleList == null) {
			titleList = new ArrayList<HashMap<String, String>>();
		}
		else {
			titleList.clear();
		}
		if (recordList == null) {
			recordList = new ArrayList<List<HashMap<String,String>>>();
		}
		else {
			recordList.clear();
		}

		HashMap<String, String> titleMap = null;
		Record record = null;
		List<HashMap<String, String>> childList = null;
		HashMap<String, String> map = null;
		StringBuffer buffer = null;

		HashMap<String, Integer> winMap = new HashMap<String, Integer>();
		HashMap<String, Integer> loseMap = new HashMap<String, Integer>();
		int win = 0, lose = 0;

		HashMap<String, List<HashMap<String, String>>> playerListMap = new HashMap<String, List<HashMap<String,String>>>();

		for (int i = 0; i < tempList.size(); i++) {
			record = tempList.get(i);
			childList = playerListMap.get(record.getCompetitor());
			if (childList == null) {
				titleMap = new HashMap<String, String>();
				titleMap.put("player", record.getCompetitor());
				titleMap.put("country", record.getCptCountry());
				//titleMap.put("h2h", "11 - 10");
				//titleMap.put("bkFlag", "" + titleList.size()%2);
				titleMap.put("indexBeforeSort", "" + titleList.size());
				titleList.add(titleMap);

				childList = new ArrayList<HashMap<String,String>>();
				playerListMap.put(record.getCompetitor(), childList);
				recordList.add(childList);
			}
			map = new HashMap<String, String>();
			buffer = new StringBuffer("(");buffer.append(record.getCptRank()).append("/").append(record.getCptSeed());
			buffer.append(")  ").append(record.getCourt()).append("  ").append(record.getStrDate());
			map.put("player", buffer.toString());
			buffer = new StringBuffer(record.getLevel());
			buffer.append("  ").append(record.getMatch()).append("  ").append(record.getRound());
			map.put("match", buffer.toString());
			map.put("match_name", record.getMatch());
			map.put("court_name", record.getCourt());
			buffer = new StringBuffer();

			String winner = record.getWinner();
			if (winner.equals(MultiUserManager.USER_DB_FLAG)) {
				winner = MultiUserManager.getInstance().getCurrentUser().getDisplayName();
			}
			buffer.append(winner);
			buffer.append("  ").append(record.getScore());
			map.put("score", buffer.toString());
			map.put("indexInRecordList", "" + i);

			if (MultiUserManager.USER_DB_FLAG.equals(record.getWinner())) {
				if (winMap.get(record.getCompetitor()) == null) {
					win = 1;
					winMap.put(record.getCompetitor(), 1);
				}
				else {
					win = winMap.get(record.getCompetitor());
					winMap.put(record.getCompetitor(), ++ win);
				}
			}
			else {
				if (loseMap.get(record.getCompetitor()) == null) {
					lose = 1;
					loseMap.put(record.getCompetitor(), 1);
				}
				else {
					lose = loseMap.get(record.getCompetitor());
					loseMap.put(record.getCompetitor(), ++ lose);
				}
			}
			childList.add(0, map);
		}
		recordList.add(childList);

		String key = null;
		for (HashMap<String, String> tMap : titleList) {
			key = tMap.get("player");
			if (winMap.get(key) == null) {
				win = 0;
			}
			else {
				win = winMap.get(key);
			}
			if (loseMap.get(key) == null) {
				lose = 0;
			}
			else {
				lose = loseMap.get(key);
			}
			tMap.put("h2h", win + " - " + lose);
		}

		/* 耗时太长
		Collections.sort(titleList, new StringMixSort());
		int indexBeforeSort = 0;
		List<List<HashMap<String, String>>> tempRecordList = new ArrayList<List<HashMap<String,String>>>(recordList.size());
		for (int i = 0; i < titleList.size(); i ++) {
			indexBeforeSort = Integer.parseInt(titleList.get(i).get("indexBeforeSort"));
			tempRecordList.add(recordList.get(indexBeforeSort));
		}
		recordList = tempRecordList;
		*/
		createNamePinyinPool();
		Collections.sort(titleList, new ShowByPlayerComparator(userActivity, namePinyinMap));
		int indexBeforeSort = 0;
		List<List<HashMap<String, String>>> tempRecordList = new ArrayList<List<HashMap<String,String>>>(recordList.size());
		for (int i = 0; i < titleList.size(); i ++) {
			titleMap = titleList.get(i);
			titleMap.put("bkFlag", "" + i%2);
			indexBeforeSort = Integer.parseInt(titleMap.get("indexBeforeSort"));
			tempRecordList.add(recordList.get(indexBeforeSort));
		}
		recordList = tempRecordList;

	}

	private void createNamePinyinPool() {
		if (namePinyinMap == null) {
			namePinyinMap = new NamePinyinPool(userActivity).getNamePinyinMap();
		}
	}

	private void adjustListDataByDate() {
		Log.i("MyTennis", "adjustListDataByDate");
		if (tempList == null || tempList.size() == 0) {
			return;
		}
		if (titleList == null) {
			titleList = new ArrayList<HashMap<String, String>>();
		}
		else {
			titleList.clear();
		}
		if (recordList == null) {
			recordList = new ArrayList<List<HashMap<String,String>>>();
		}
		else {
			recordList.clear();
		}

		HashMap<String, String> titleMap = null;
		Record record = null;
		List<HashMap<String, String>> childList = null;
		HashMap<String, String> map = null;
		StringBuffer buffer = null;

		for (int i = 0; i < tempList.size(); i++) {
			record = tempList.get(i);
			if (i == 0) {
				titleMap = new HashMap<String, String>();
				titleMap.put("date", record.getStrDate());
				titleMap.put("match", record.getMatch());
				titleMap.put("level", record.getLevel());
				titleMap.put("courtFlag", record.getCourt());
				titleList.add(titleMap);
				childList = new ArrayList<HashMap<String,String>>();
			}
			if (!record.getMatch().equals(titleMap.get("match")) || !record.getStrDate().equals(titleMap.get("date"))) {
				titleMap = new HashMap<String, String>();
				titleMap.put("date", record.getStrDate());
				titleMap.put("match", record.getMatch());
				titleMap.put("level", record.getLevel());
				titleMap.put("courtFlag", record.getCourt());
				titleList.add(titleMap);
				recordList.add(childList);
				childList = new ArrayList<HashMap<String,String>>();
			}
			map = new HashMap<String, String>();
			map.put("head", "" + R.drawable.icon_list);
			map.put("player", record.getCompetitor());
			buffer = new StringBuffer("(");
			buffer.append(record.getCptRank()).append("/").append(record.getCptSeed());
			buffer.append(")  ").append(record.getCourt()).append("  ").append(record.getRound());
			map.put("line1", buffer.toString());
			buffer = new StringBuffer();
			String winner = record.getWinner();

			if (winner.equals(MultiUserManager.USER_DB_FLAG)) {
				winner = MultiUserManager.getInstance().getCurrentUser().getDisplayName();
			}
			buffer.append(winner);
			buffer.append("  ").append(record.getScore());
			map.put("score", buffer.toString());
			map.put("indexInRecordList", "" + i);
			childList.add(0, map);
		}
		recordList.add(childList);
		Collections.reverse(recordList);
		Collections.reverse(titleList);

	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
								int groupPosition, long id) {
		//动画不起作用，把return改成true后起作用但是子列表又不展开了
		//ImageView image = (ImageView) v.findViewById(R.id.recordlist_title_image);
		//image.startAnimation(AnimationUtils.loadAnimation(context, R.anim.arrowrote));
		Log.i("MyTennis", "" + groupPosition);
		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
								int groupPosition, int childPosition, long id) {

		if (currentShowMode == SHOW_BY_PLAYER) {
			int index = Integer.parseInt(recordList.get(groupPosition).get(childPosition).get("indexInRecordList"));
			final Record record = tempList.get(index);

			new GloryMatchDialog(userActivity, new CustomDialog.OnCustomDialogActionListener() {

				@Override
				public boolean onSave(Object object) {
					return false;
				}

				@Override
				public void onLoadData(HashMap<String, Object> data) {
					List<Record> list = new GloryController().loadMatchRecord(userActivity, record.getMatch(), record.getStrDate());
					data.put(CustomDialog.OnCustomDialogActionListener.DATA_TYPE, list);
				}

				@Override
				public boolean onCancel() {
					return false;
				}
			}).show();
		}
		return true;
	}

	//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//
//		Activity act = (Activity) context;
//		act.getMenuInflater().inflate(R.menu.contextmenu_recorditem_longclick, menu);
//	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
//		nGroupSelected = ExpandableListView.getPackedPositionGroup(info.packedPosition);
		if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
//			int child = ExpandableListView.getPackedPositionChild(info.packedPosition);
			((Activity) userActivity).getMenuInflater().inflate(R.menu.contextmenu_recorditem_longclick, menu);
		}
		else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			if (currentShowMode == SHOW_BY_PLAYER) {

				// 移到单击头像
//				AlertDialog.Builder dlg = new AlertDialog.Builder(userActivity);
//				dlg.setTitle(titleList.get(nGroupSelected).get("player"));
//				dlg.setItems(userActivity.getResources().getStringArray(R.array.cptdlg_item_oper)
//						, itemListener);
//				dlg.show();
			}
		}
	}

	public void onContextItemSelected(MenuItem item) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
		int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		int child = 0;
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			child = ExpandableListView.getPackedPositionChild(info.packedPosition);
		}

		HashMap<String, String> recordMap = recordList.get(group).get(child);
		int realIndex = Integer.parseInt(recordMap.get("indexInRecordList"));
		switch (item.getItemId()) {
			case R.id.record_item_update:
				setNeedReorderData(true);
				recordListViewService.startUpdateDialog(realIndex);
				break;
			case R.id.record_item_delete:
				setNeedReorderData(true);
				RecordService service = new RecordService(userActivity);
				service.delete(realIndex, tempList);
				reAdapt();
				break;
			case R.id.record_item_detailcurrent:
				recordListViewService.startDetailDialog(tempList.get(realIndex), LT_DETAILSOFTHIS);
				break;
			case R.id.record_item_detailall:
				recordListViewService.startDetailDialog(tempList.get(realIndex), LT_DETAILSOFALL);

				break;
			default:
				break;
		}
	}

	/**********arc button view part***********/
	@Override
	public boolean onLongClick(View v) {
		Toast.makeText(userActivity, v.getContentDescription(), Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public void run() {
		if (chosedButton != null) {
			switch (chosedButton.getId()) {
				case R.id.button_composer_cptlist:
					showCompetitors();
					break;

				case R.id.button_composer_show_by_date:
					if (currentShowMode != SHOW_BY_DATE) {
						setNeedReorderData(true);
						showByDate();
						resetExpandStateMap();
					}
					break;

				case R.id.button_composer_show_by_player:
					if (currentShowMode != SHOW_BY_PLAYER) {
						setNeedReorderData(true);
						showByPlayer();
						resetExpandStateMap();
					}
					break;

				case R.id.button_composer_showall:
					setNeedReorderData(true);
					resetExpandStateMap();
					showAll();
					break;

				case R.id.button_composer_search:
					search();
					break;
				case R.id.button_composer_exit:
					recordListViewService.exitApp();
					break;
				default:
					break;
			}
		}
	}

	public void resetExpandStateMap() {
		Log.i("MyTennis", "resetExpandStateMap");
		expandStateMap.clear();
	}

	public void setNeedReorderData(boolean value) {
		Log.i("MyTennis", "setNeedReloadData " + value);
		needReorderData = value;
	}

	private boolean needReorderData() {
		Log.i("MyTennis", "needReloadData " + needReorderData);
		return needReorderData;
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		expandStateMap.put(groupPosition, true);
	}

	@Override
	public void onGroupCollapse(int groupPosition) {
		if (!isResetExpandStateMap) {
			expandStateMap.remove(groupPosition);
		}
	}

	public void saveExpandState() {
		Log.i("MyTennis", "saveExpandState");
		scrollPositionY = expandableListView.getFirstVisiblePosition();
		Log.i("MyTennis", "pos:" + scrollPositionY);

		arcButtonVisibility = arcButtonLayout.getVisibility();
		arcHasExpanded = arcButtonView.isExpand();
		showActionBarBoxStatus = showActionBarBox.isChecked();
	}

	public void restoreExpandState() {

		Log.i("MyTennis", "restoreExpandState");
		arcButtonLayout.setVisibility(arcButtonVisibility);
		if (arcHasExpanded && arcButtonVisibility == View.VISIBLE) {
			arcButtonView.onClick(buttonExpand);
		}

		if (needReorderData()) {
			expandStateMap.clear();
		}
		else {
			Iterator<Integer> iterator = expandStateMap.keySet().iterator();
			Integer key = null;
			while (iterator.hasNext()) {
				key = iterator.next();
				expandableListView.expandGroup(key);
			}
			expandableListView.setSelection(scrollPositionY);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		Log.i("MyTennis", "pos getScrollY:" + expandableListView.getScrollY());
//		Log.i("MyTennis", "pos getSelectedItemPosition:" + expandableListView.getSelectedItemPosition());
//		Log.i("MyTennis", "pos getSelectedPosition:" + expandableListView.getSelectedPosition());
//		Log.i("MyTennis", "pos getFirstVisiblePosition:" + expandableListView.getFirstVisiblePosition());
//		Log.i("MyTennis", "pos getLastVisiblePosition:" + expandableListView.getLastVisiblePosition());
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadMenu(SlidingMenuAbstract slidingMenu,
						 LinearLayout menuLayout) {
		menuLayout.removeAllViews();
		SlidingMenuCreator creator = new SlidingMenuCreator(userActivity, slidingMenuListener);
		creator.loadMenu(Constants.RecordListViewMenu, menuLayout, SettingProperty.getSlidingMenuMode(userActivity));
		Bitmap bitmap = creator.loadBackground(SlidingMenuAbstract.MENU_BK_KEY);
		recordListViewService.updateBackground(bitmap);
	}

	@Override
	public SlidingMenuCreator loadTwoWayMenu(LinearLayout menuLayout,
											 LinearLayout menuLayoutRight) {
		menuLayout.removeAllViews();
		menuLayoutRight.removeAllViews();

		SlidingMenuCreator slidingMenuCreator = new SlidingMenuCreator(userActivity, slidingMenuListener);
		slidingMenuCreator.loadMenu(Constants.RecordListViewMenu, menuLayout, SettingProperty.SLIDINGMENU_LEFT);
		slidingMenuCreator.loadMenu(Constants.RecordListViewMenu, menuLayoutRight, SettingProperty.SLIDINGMENU_RIGHT);
		Bitmap bitmap = slidingMenuCreator.loadBackground(SlidingMenuLeft.BK_KEY);
		recordListViewService.updateBackground(bitmap);
		return slidingMenuCreator;
	}

	OnClickListener slidingMenuListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.string.menu_exactselect:
					setNeedReorderData(true);
					search();
					break;
				case R.string.menu_showall:
					setNeedReorderData(true);
					showAll();
					break;
				case R.string.menu_show_by_player:
					setNeedReorderData(true);
					showByPlayer();
					resetExpandStateMap();

					TextView menuItem = (TextView) v;
					menuItem.setText(R.string.menu_show_by_date);
//					menuItem.setId(R.string.menu_show_by_date);
					break;
				case R.string.menu_show_by_date:
					setNeedReorderData(true);
					showByDate();
					resetExpandStateMap();

					menuItem = (TextView) v;
					menuItem.setText(R.string.menu_show_by_player);
//					menuItem.setId(R.string.menu_show_by_player);
					break;
				case R.string.menu_saveas:
					recordListViewService.startSaveAsDialog();
					break;
				case R.string.menu_change_theme:
					recordListViewService.startThemeDialog();
					break;
				case R.string.exit:
					recordListViewService.exitApp();
					break;
			}
		}
	};

	private void unExpandAllGroup() {
		//由于在onGroupCollapse里执行了expandStateMap.remove操作，所以需要判定以防CurrentModifiedException发生
		isResetExpandStateMap = true;
		Iterator<Integer> iterator = expandStateMap.keySet().iterator();
		while (iterator.hasNext()) {
			expandableListView.collapseGroup(iterator.next());
		}
		isResetExpandStateMap = false;
		resetExpandStateMap();
	}

	public void notifyUserChanged() {
		setNeedReorderData(true);
		namePinyinMap = null;
		showAll();
	}

	@Override
	public void onTouchingLetterChanged(String index) {
		if (currentShowMode == SHOW_BY_PLAYER) {

			unExpandAllGroup();//如果处于展开状态，setSelection的位置则不对
			int position = indexCreator.getIndexPosition(index);
			expandableListView.setSelection(position);
		}
	}
}
