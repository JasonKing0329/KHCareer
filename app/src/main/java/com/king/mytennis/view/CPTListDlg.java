package com.king.mytennis.view;

import java.io.FileNotFoundException;
import java.util.HashMap;

import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.model.SearchModel;
import com.king.mytennis.service.RecordService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler.Callback;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class CPTListDlg implements OnItemClickListener, OnItemLongClickListener, Callback {

	private Context userActivity;
	private String[] nameArray;
	private HashMap<String, Bitmap> headMap;
	private Dialog thisDialog;//用于记录show的对话框的id来在没有cancel按钮情况下关闭对话框
	private ProgressDialog progressDialog;
	private Handler handler;

	private CptImageGridAdapter adapter;

	private int nSelected;

	public CPTListDlg(Context userActivity) {
		this.userActivity = userActivity;
		handler = new Handler(this);
	}

	/**
	 * 有汉字排序，速率较慢，因此设计为只初始化1次
	 */
	private void initNames() {
		if (nameArray == null) {
			progressDialog = new ProgressDialog(userActivity);
			progressDialog.setMessage(userActivity.getResources().getString(R.string.dlg_loading_msg));
			progressDialog.show();
			new Thread() {
				public void run() {
					nameArray = new RecordService(userActivity).getCptNames();
					handler.sendMessage(new Message());
				}
			}.start();
		}
		else {
			initThenShow();
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		progressDialog.cancel();
		adapter = new CptImageGridAdapter(userActivity, nameArray, headMap);
		initThenShow();
		new ImageThread().start();
		return true;
	}

	private void initThenShow() {
		View view;
		LayoutInflater factory = LayoutInflater.from(userActivity);
		view = factory.inflate(R.layout.dialog_cptlist, null);

		TextView textView = (TextView)view.findViewById(R.id.countcpt);
		textView.setText("total numbers: "+nameArray.length);

		GridView gridView = (GridView) view.findViewById(R.id.gridview_cptlist);
		gridView.setOnItemClickListener(this);
		gridView.setOnItemLongClickListener(this);
		gridView.setAdapter(adapter);

		AlertDialog.Builder dlg = new AlertDialog.Builder(userActivity);
		dlg.setTitle(R.string.menu_cmplist);
		dlg.setView(view);
		thisDialog = dlg.show();
	}

	public void show(HashMap<String, Bitmap> map) {
		this.headMap = map;
		initNames();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

		SearchModel search = new SearchModel();
		search.setCompetitor(nameArray[pos]);
		search.setCompetitorOn(true);
		if (userActivity instanceof ManagerActivity) {
			ManagerActivity activity = (ManagerActivity) userActivity;
			activity.setRecordList(search.searchByCompetitor(userActivity));
			activity.changeView(ManagerActivity.VIEW_LIST, true);
		}
		thisDialog.dismiss();//要用Dialog类的dismiss方法手动关闭
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
								   int position, long id) {

		nSelected = position;
		AlertDialog.Builder dlg=new AlertDialog.Builder(userActivity);
		dlg.setTitle(R.string.longtouch_title);
		dlg.setItems(userActivity.getResources().getStringArray(R.array.cptdlg_item_oper)
				, itemListener);
		dlg.show();
		return true;
	}

	DialogInterface.OnClickListener itemListener = new DialogInterface.OnClickListener() {

		private final int IMAGE_LOCAL = 0;
		private final int IMAGE_SERVER = 1;
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == IMAGE_LOCAL) {

			}
			else if (which == IMAGE_SERVER) {

			}
		}
	};

	/**
	 * 线程后台更新头像
	 * 采用每更新PER_NUM条就像主线程发送通知更新
	 * @author king
	 *
	 */
	private class ImageThread extends Thread implements Callback {

		private final int PER_NUM = 20;
		private final int FLAG_ALLOK = 10000;

		private Handler handler;
		//private HashMap<Integer, Bitmap> headMap;

		public ImageThread() {
			handler = new Handler(this);
			//headMap = new HashMap<Integer, Bitmap>();
		}
		public void run() {
			int count = 0;
			// 注意listview是按倒序显示list的
			for (int i = 0; i < nameArray.length; i++) {
				String name = nameArray[i];
				if (headMap.get(name) != null) {
					continue;//已经缓存过就不再缓存
				}
				try {
					Bitmap bitmap = ImageFactory.compressFromFile(
							Configuration.IMG_PLAYER_HEAD + name + ".jpg", 200);
					headMap.put(name, bitmap);
					count ++;
					if (count % PER_NUM == 0) {
						handler.sendMessage(new Message());
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			Message msg = new Message();
			msg.what = FLAG_ALLOK;
			handler.sendMessage(msg);
		}

		@Override
		public boolean handleMessage(Message msg) {

			adapter.setHeadMap(headMap);
			adapter.notifyDataSetChanged();
			if (msg.what == FLAG_ALLOK) {
				if (userActivity instanceof ManagerActivity) {
					ManagerActivity activity = (ManagerActivity) userActivity;
					activity.setHasCachedAllHead(true);//打开全部player list对话框则缓存全部头像，其他界面就不必再新启线程缓存
				}
				Toast.makeText(userActivity, R.string.head_load_ok, Toast.LENGTH_LONG).show();
			}
			return true;
		}
	}

}
