package com.king.khcareer.home.classic;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.king.khcareer.common.helper.BackgroundProvider;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.base.BaseActivity;
import com.king.mytennis.view.R;
import com.king.khcareer.record.SearchDialog.OnSearchListener;
import com.king.khcareer.record.classic.RecordListViewController;
import com.king.khcareer.record.classic.RecordListViewUpdate;

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

		Bitmap background = new BackgroundProvider().loadBackgound();
		if (background == null) {
			background = BitmapFactory.decodeResource(getResources(), R.drawable.bk_mainview);
		}
		recordListViewController.updateBackground(background);
		if (recordListViewUpdate != null) {
			recordListViewUpdate.notifyBackgroundUpdated(background);
		}

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

}
