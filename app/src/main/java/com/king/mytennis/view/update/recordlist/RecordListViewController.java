package com.king.mytennis.view.update.recordlist;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;

import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.FileIO;
import com.king.mytennis.model.H2HDAOList;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.model.Record;
import com.king.mytennis.service.ExternalRecordTool;
import com.king.mytennis.service.RecordService;
import com.king.mytennis.view.CPTListDlg;
import com.king.mytennis.view.ChangeThemeDialog;
import com.king.mytennis.view.ChooseBkDialog;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.DetailsDialog;
import com.king.mytennis.view.LoadFromDialog;
import com.king.mytennis.view.R;
import com.king.mytennis.view.SaveAsDialog;
import com.king.mytennis.view.SearchDialog;
import com.king.mytennis.view.UpdateDialog;
import com.king.mytennis.view.SearchDialog.OnSearchListener;
import com.king.mytennis.view.detail.DetailGallery;
import com.king.mytennis.view_v_7_0.controller.BasicOperation;
import com.king.mytennis.view.SetRecordFileDialog;

/**
 * @author JingYang
 * @version create time：2016-3-9 下午2:22:39
 *
 */
public class RecordListViewController implements RecordListViewService {

	public static final int DLG_SAVEAS=2;
	public static final int DLG_CHOOSEBK=3;
	public static final int DLG_SETRESFILE=4;
	public static final int DLG_CPTLIST=5;
	public static final int DLG_INSERT=6;
	public static final int DLG_SEARCH=8;
	public static final int DLG_UPDATE=9;
	public static final int DLG_LOAD=10;
	public static final int DLG_CHANGE_THEME = 11;

	private Context mContext;
	private Handler loadHandler;
	private OnSearchListener onSearchListener;

	private HashMap<String, Bitmap> headMap;
	private List<Record> recordList;
	private Bitmap background;

	private CPTListDlg cptListDlg;
	private UpdateDialog updateDialog;

	public RecordListViewController(Context context, Handler loadHandler
			, OnSearchListener searchListener) {
		mContext = context;
		this.loadHandler = loadHandler;
		onSearchListener = searchListener;
		headMap = new HashMap<String, Bitmap>();
	}

	public void setLoadHandler() {
	}

	@Override
	public HashMap<String, Bitmap> getHeadMap() {

		return headMap;
	}

	@Override
	public List<Record> getRecordList() {

		return recordList;
	}

	@Override
	public Bitmap getBackground() {
		return background;
	}

	@Override
	public void updateBackground(Bitmap bitmap) {
		background = bitmap;
	}

	@Override
	public void startSaveAsDialog() {
		startDialog(DLG_SAVEAS);
	}

	@Override
	public void startThemeDialog() {
		startDialog(DLG_CHANGE_THEME);
	}

	@Override
	public void startSearchDialog() {
		startDialog(DLG_SEARCH);
	}

	@Override
	public void startCompetitorDialog() {
		startDialog(DLG_CPTLIST);
	}

	@Override
	public void exitApp() {
		((Activity) mContext).finish();
	}

	@Override
	public void onBackPressCallback() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRecordList(List<Record> list) {
		recordList = list;
	}

	@Override
	public void startUpdateDialog(int position) {
		if (updateDialog == null) {
			updateDialog = new UpdateDialog(mContext, recordList, position);
			new InitInsertOrUpdateDialogThread(DLG_UPDATE).start();
		}
		else {
			updateDialog.reshow(position);
		}
	}

	@Override
	public void startDetailDialog(Record record, int which) {
		DetailsDialog dlg = null;
		if (which == RecordListViewUpdate.LT_DETAILSOFTHIS) {
			dlg = new DetailsDialog(mContext, record, new H2HDAOList(recordList, record.getCompetitor()));
			dlg.show();

		}
		else if (which == RecordListViewUpdate.LT_DETAILSOFALL) {
			//dlg = new DetailsDialog(this, record, new H2HDAODB(this, record.getCompetitor()));
			//dlg.show();

			Intent intent = new Intent();
			intent.setClass(mContext, DetailGallery.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("record", record);
			intent.putExtras(bundle);
			((Activity) mContext).startActivity(intent);
		}
	}

	public void startLoadRecord() {
		new LoadRecordThread(loadHandler).start();
	}

	/**
	 * 后台加载record list，加载完成后初始化year卡片
	 */
	private class LoadRecordThread extends Thread {

		private Handler handler;

		public LoadRecordThread (Handler handler) {
			this.handler = handler;
		}

		public void run() {
			recordList = new RecordService(mContext).queryAll();

			/**
			 * 已经采用新的复制数据库方法，不再使用下列方法
			 //initService.loadDatabase(recordList);//内置只在第一次创建表有数据操作
			 **/
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
		}

	}

	/**
	 * 启动dialog
	 * @param dialogID
	 */
	public void startDialog(int dialogID) {

		if (dialogID == DLG_SEARCH) {
			new SearchDialog(mContext, onSearchListener).show();
		}
		else if (dialogID == DLG_INSERT) {
		}
		else if (dialogID == DLG_SAVEAS) {
			BasicOperation.showSaveAsDialog(mContext, null);
		}
		else if (dialogID == DLG_SETRESFILE) {
			new SetRecordFileDialog(mContext).show();
		}
		else if (dialogID == DLG_CHOOSEBK) {
			ChooseBkDialog bkDialog = new ChooseBkDialog(mContext, new CustomDialog.OnCustomDialogActionListener() {

				@Override
				public boolean onSave(Object object) {
					HashMap<String, Object> map = (HashMap<String, Object>) object;
					int kind = (Integer) map.get(ChooseBkDialog.BK_KIND_KEY);
					if (kind == ChooseBkDialog.BK_KIND_MAINVIEW) {
						String path = (String) map.get("path");
						Bitmap bitmap = new ImageFactory().getBackground(path);
						notifyBackgroundChanged(bitmap, path);
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
			new ChangeThemeDialog(mContext, new CustomDialog.OnCustomDialogActionListener() {

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
			//new CPTListDlg(this, headMap);
			if (cptListDlg == null) {
				cptListDlg = new CPTListDlg(mContext);
			}
			cptListDlg.show(headMap);
			//由于该对话框是listview通过点击某一项后自动关闭对话框，而show返回dialogid用于关闭
			//所有show直接在内部初始化调用
		}
		else if (dialogID == DLG_LOAD) {
			BasicOperation.showLoadFromDialog(mContext, new BasicOperation.DialogCallback() {

				@Override
				public void onOk(Object result) {
					startLoadRecord();
				}

				@Override
				public void onCancel(Object result) {

				}
			});
		}
	}

	/**
	 * chooseBK功能中选择图片立马更改
	 * @param bitmap
	 * @param newDEF_BK
	 */
	private void notifyBackgroundChanged(Bitmap bitmap, String newDEF_BK) {
		updateBackground(bitmap);
		Configuration.getInstance().DEF_BK = newDEF_BK;
		//configuration.setModified(true);
		FileIO dao = new FileIO();
		dao.saveConfigInfor(Configuration.getInstance());
	}
	private void reload() {

		Activity activity = (Activity) mContext;
		Intent intent = activity.getIntent();
		activity.overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		activity.finish();
		activity.overridePendingTransition(0, 0);
		activity.startActivity(intent);
	}

	private class InitInsertOrUpdateDialogThread extends Thread implements Callback{

		private ProgressDialog dialog;
		private Handler handler;
		private int flag;

		public InitInsertOrUpdateDialogThread(int flag) {
			this.flag = flag;
			handler = new Handler(this);
			dialog = new ProgressDialog(mContext);
			dialog.setMessage(mContext.getResources().getString(R.string.loading));
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
}
