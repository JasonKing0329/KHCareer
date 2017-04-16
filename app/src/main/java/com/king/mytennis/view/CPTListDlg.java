package com.king.mytennis.view;

import java.util.List;

import com.king.khcareer.record.RecordService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class CPTListDlg implements OnItemClickListener, OnItemLongClickListener, Callback {

	private Context userActivity;
	private String[] nameArray;
	private List<String> pathList;
	private Dialog thisDialog;//用于记录show的对话框的id来在没有cancel按钮情况下关闭对话框
	private ProgressDialog progressDialog;
	private Handler handler;

	private CptImageGridAdapter adapter;

	private int nSelected;

	public CPTListDlg(Context userActivity) {
		this.userActivity = userActivity;
		handler = new Handler(this);
	}

	public void show() {
		if (nameArray == null) {
			progressDialog = new ProgressDialog(userActivity);
			progressDialog.setMessage(userActivity.getResources().getString(R.string.dlg_loading_msg));
			progressDialog.show();
			new Thread() {
				public void run() {
					nameArray = new RecordService().getCptNames();
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
		adapter = new CptImageGridAdapter(userActivity, nameArray);
		initThenShow();
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

//		SearchModel search = new SearchModel();
//		search.setCompetitor(nameArray[position]);
//		search.setCompetitorOn(true);
//		if (userActivity instanceof ManagerActivity) {
//			ManagerActivity activity = (ManagerActivity) userActivity;
//			activity.setRecordList(search.searchByCompetitor(userActivity));
//			activity.changeView(ManagerActivity.VIEW_LIST, true);
//		}
//		thisDialog.dismiss();//要用Dialog类的dismiss方法手动关闭

		nSelected = position;
		AlertDialog.Builder dlg=new AlertDialog.Builder(userActivity);
		dlg.setTitle(nameArray[position]);
		dlg.setItems(userActivity.getResources().getStringArray(R.array.cptdlg_item_oper)
				, itemListener);
		dlg.show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
								   int position, long id) {

		return true;
	}

	DialogInterface.OnClickListener itemListener = new DialogInterface.OnClickListener() {

		private final int DOWNLOAD = 0;
		private final int REFRESH = 1;
		private final int MANAGE = 2;
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == DOWNLOAD) {
				adapter.onItemClickDownload(nSelected);
			}
			else if (which == REFRESH) {
				adapter.onItemClickRefresh(nSelected);
			}
			else if (which == MANAGE) {
				adapter.onItemClickManage(nSelected);
			}
		}
	};

}
