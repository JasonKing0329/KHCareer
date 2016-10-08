package com.king.mytennis.view_v_7_0;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.king.mytennis.model.Record;
import com.king.mytennis.service.InitService;
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.R;
import com.king.mytennis.view.SearchDialog.OnSearchListener;
import com.king.mytennis.view.update.recordlist.RecordListViewController;
import com.king.mytennis.view.update.recordlist.RecordListViewUpdate;

/**
 * @author JingYang
 * @version create time：2016-3-9 下午2:16:00
 *
 */
public class ClassicActivity extends BaseActivity implements OnSearchListener {

	public static final String KEY_INIT_MODE = "key_init_mode";
	public static final int INIT_MATCH = 0;
	public static final int INIT_PLAYER = 1;
	private int initMode;

	private RecordListViewUpdate recordListViewUpdate;
	private RecordListViewController recordListViewController;

	private ProgressDialog loadRecordDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.view_recordlist_update_l);

		recordListViewController = new RecordListViewController(this, loadHandler, this);

		startLoadRecord();
	}

	private void startLoadRecord() {
		loadRecordDialog=new ProgressDialog(this);
		loadRecordDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadRecordDialog.setMessage(getString(R.string.dlg_loading_msg));
		loadRecordDialog.show();

		new InitAppThread().start();
		recordListViewController.startLoadRecord();
	}

	@SuppressLint("HandlerLeak")
	private Handler loadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				loadRecordDialog.cancel();

				if (recordListViewController.getRecordList() == null
						|| recordListViewController.getRecordList().size() == 0) {
					Toast.makeText(ClassicActivity.this
							, R.string.nullRecord, Toast.LENGTH_LONG).show();
				}
				initRecordListView();
			}
		}
	};

	protected void initRecordListView() {
		initMode = getIntent().getIntExtra(KEY_INIT_MODE, INIT_MATCH);

		recordListViewUpdate = new RecordListViewUpdate(this, recordListViewController);

		recordListViewUpdate.init();
		if (initMode == INIT_PLAYER) {
			recordListViewUpdate.switchToPlayerList();
		}
		recordListViewUpdate.notifyBackgroundUpdated(recordListViewController.getBackground());
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

	@Override
	public void onBackPressed() {
		recordListViewUpdate.onBackPressed();
		super.onBackPressed();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenuInfo menuInfo) {
		recordListViewUpdate.onCreateContextMenu(menu, v, menuInfo);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		recordListViewUpdate.onContextItemSelected(item);
		return super.onContextItemSelected(item);
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
			if (recordListViewUpdate != null) {
				recordListViewUpdate.notifyBackgroundUpdated(background);
			}
			return true;
		}

	}
}
