package com.king.mytennis.view;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.model.Record;
import com.king.mytennis.model.RecordListModel;
import com.king.mytennis.service.MenuService;
import com.king.mytennis.service.RecordService;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

@Deprecated
public class RecordListView implements OnItemClickListener
		, OnItemLongClickListener{

	private final int MENU_EXACTSELECT=10;
	private final int MENU_SHOWALL=11;
	private final int MENU_SORT=12;
	private final int MENU_SORT_COMPETITOR=121;
	private final int MENU_SORT_DATE=122;
	private final int MENU_CMPLIST=7;
	private final int MENU_RANKING=8;
	private final int MENU_SAVEAS=2;

	private final int LT_UPDATE=0;
	private final int LT_DELETE=1;
	public static final int LT_DETAILSOFTHIS=2;
	public static final int LT_DETAILSOFALL=3;

	private ManagerActivity userActivity;
	private MenuService menuService;

	private HashMap<String, Bitmap> headMap;
	private List<Record> list;
	private ListView listView;
	private TextView tv_count;
	//private MyListAdapter listAdapter;
	private RecordListAdapter recordAdapter;
	private ProgressDialog reloadDialog;
	private int nSelected;

	public RecordListView(ManagerActivity activity) {
		userActivity = activity;
		menuService = new MenuService();
		headMap = userActivity.getHeadMap();
	}

	public void reInit() {
		list = userActivity.getRecordList();
		RelativeLayout layout=(RelativeLayout)userActivity.findViewById(R.id.layout_lookresult);
		layout.setBackgroundDrawable(new BitmapDrawable(userActivity.getBackground()));

		listView=(ListView)userActivity.findViewById(R.id.listview_result);
		tv_count=(TextView)userActivity.findViewById(R.id.result_tv_count);

//		listView.setAdapter(listAdapter);
//		listAdapter.setRecordList(list);
//		listAdapter.setHeadMap(headMap);
//		listAdapter.notifyDataSetChanged();
		listView.setAdapter(recordAdapter);
		recordAdapter.setRecordList(list);
		recordAdapter.setHeadMap(headMap);
		recordAdapter.notifyDataSetChanged();
		listView.setCacheColorHint(0);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		tv_count.setText(new RecordListModel().getCountInfor(list));
	}
	public void init(){

		list = userActivity.getRecordList();
		RelativeLayout layout=(RelativeLayout)userActivity.findViewById(R.id.layout_lookresult);
		layout.setBackgroundDrawable(new BitmapDrawable(userActivity.getBackground()));

		listView=(ListView)userActivity.findViewById(R.id.listview_result);
		tv_count=(TextView)userActivity.findViewById(R.id.result_tv_count);
//		listAdapter=new MyListAdapter(userActivity, this.list);
//		listView.setAdapter(listAdapter);
		recordAdapter = new RecordListAdapter(userActivity, this.list);
		listView.setAdapter(recordAdapter);
		listView.setCacheColorHint(0);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		tv_count.setText(new RecordListModel().getCountInfor(list));
		//listAdapter.notifyDataSetChanged();
		userActivity.setProgressBarIndeterminate(true);
		if (list != null && list.size() != 0) {
			new ImageThread(0, RecordListAdapter.EACHMAXSIZE).start();
		}
	}

	private class ImageThread extends Thread implements Callback {

		private int start;
		private int size;
		private Handler handler;
		public ImageThread (int start, int size) {
			this.start = start;
			this.size = size;
			handler = new Handler(this);
		}
		public void run() {
			//注意由于设计的缺陷，列表顺序显示是按list的反序的
			start = list.size() - 1 - start;
			int end = start - size;
			if (end < 0) {
				end = list.size();
			}
			for (int i = start; i >= end; i --) {
				String name = list.get(i).getCompetitor();
				if (headMap.get(name) != null) {
					continue;//已经缓存过就不再缓存
				}
				try {
					Bitmap bitmap = ImageFactory.compressFromFile(
							Configuration.IMG_PLAYER_HEAD + name + ".jpg", 200);
					headMap.put(name, bitmap);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			handler.sendMessage(new Message());
		}

		@Override
		public boolean handleMessage(Message msg) {

			recordAdapter.setHeadMap(headMap);
			recordAdapter.notifyDataSetChanged();
			Toast.makeText(userActivity, R.string.head_load_ok, Toast.LENGTH_LONG).show();

			return true;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
							long arg3) {
		if (recordAdapter.isLoadMore(position)) {
			int start = recordAdapter.getCount() - 1;//去掉“加载更多”项
			recordAdapter.loadMore();//执行后size已变化
			if (userActivity.hasCachedAllHead()) {//如果已全部缓存完毕
				recordAdapter.setHeadMap(headMap);
				recordAdapter.notifyDataSetChanged();
			}
			else {//没有缓存完毕则每次点击就执行缓存
				new ImageThread(start, RecordListAdapter.EACHMAXSIZE).start();
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
								   int position, long arg3) {
		nSelected=position;
		AlertDialog.Builder dlg=new AlertDialog.Builder(userActivity);
		dlg.setTitle(R.string.longtouch_title);
		dlg.setItems(userActivity.getResources().getStringArray(R.array.longtouch_menu)
				, lis_longtouch);
		dlg.show();
		return false;
	}

	DialogInterface.OnClickListener lis_longtouch=new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which==LT_UPDATE) {//update
				userActivity.startUpdateDialog(list.size()-nSelected-1);//因为是反向显示
			}
			else if (which==LT_DELETE) {//delete
				RecordService service = new RecordService(userActivity);
				service.delete(list.size()-nSelected-1, list);
				reAdapt();
			}
			else if (which==LT_DETAILSOFTHIS) {//details of this
				userActivity.startDetailDialog(list.get(list.size()-nSelected-1), which);
			}
			else if (which==LT_DETAILSOFALL) {
				userActivity.startDetailDialog(list.get(list.size()-nSelected-1), which);
			}
		}
	};

	public void onBackPressed() {
		userActivity.changeView(ManagerActivity.VIEW_MAIN, false);
	}

	public void onCreateOptionsMenu(Menu menu) {
		menu.add(1, MENU_EXACTSELECT, 0, R.string.menu_exactselect);
		menu.add(1, MENU_SHOWALL, 1, R.string.menu_showall);
		menu.add(1, MENU_SAVEAS, 2, R.string.menu_saveas);
		menu.add(1, MENU_CMPLIST, 3, R.string.menu_cmplist);
		menu.add(1, MENU_RANKING, 4, R.string.menu_ranking);
		SubMenu subMenu=menu.addSubMenu(1, MENU_SORT, 5, R.string.menu_sort);
		subMenu.setIcon(android.R.drawable.ic_menu_more);
		subMenu.add(2, MENU_SORT_COMPETITOR, 0, R.string.menu_sort_comp);
		subMenu.add(2, MENU_SORT_DATE, 1, R.string.menu_sort_date);
	}

	public void onMenuOpened(int featureId, Menu menu) {
		menu.clear();
		menu.add(1, MENU_EXACTSELECT, 0, R.string.menu_exactselect);
		menu.add(1, MENU_SHOWALL, 1, R.string.menu_showall);
		menu.add(1, MENU_SAVEAS, 2, R.string.menu_saveas);
		menu.add(1, MENU_CMPLIST, 3, R.string.menu_cmplist);
		menu.add(1, MENU_RANKING, 4, R.string.menu_ranking);
		SubMenu subMenu=menu.addSubMenu(1, MENU_SORT, 5, R.string.menu_sort);
		subMenu.setIcon(android.R.drawable.ic_menu_more);
		subMenu.add(2, MENU_SORT_COMPETITOR, 0, R.string.menu_sort_comp);
		subMenu.add(2, MENU_SORT_DATE, 1, R.string.menu_sort_date);
	}

	public void onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_SAVEAS:
				userActivity.startDialog(ManagerActivity.DLG_SAVEAS);
				break;
			case MENU_EXACTSELECT:
				userActivity.startDialog(ManagerActivity.DLG_SEARCH);
				break;
			case MENU_SHOWALL:
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
				userActivity.startDialog(ManagerActivity.DLG_CPTLIST);
				break;
			case MENU_RANKING:
				break;
			default:
				break;
		}
	}

	private void showAll() {
		reloadDialog = new ProgressDialog(userActivity);
		reloadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		reloadDialog.setMessage(userActivity.getString(R.string.dlg_loading_msg));
		reloadDialog.show();
		new Thread(){
			public void run() {

				list = menuService.loadRecords(userActivity);
				Message msg = new Message();
				msg.what = 1;
				reloadHandler.sendMessage(msg);
			}
		}.start();
	}

	Handler reloadHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			reloadDialog.cancel();
			if (msg.what == 1) {
				if (list != null) {
					userActivity.setRecordList(list);
					Toast.makeText(userActivity, R.string.loading_ok, Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(userActivity, R.string.loading_fail, Toast.LENGTH_LONG).show();
				}
				reAdapt();
			}
			super.handleMessage(msg);
		}

	};

	public void reAdapt() {

//		listAdapter.setRecordList(list);
//		listAdapter.setHeadMap(headMap);
//		listAdapter.notifyDataSetChanged();
		recordAdapter.setRecordList(list);
		recordAdapter.setHeadMap(headMap);
		recordAdapter.notifyDataSetChanged();

		listView.setCacheColorHint(0);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		tv_count.setText(new RecordListModel().getCountInfor(list));
	}

}
