package com.king.mytennis.view.update.recordlist;

import java.util.List;
import java.util.Map;

import com.king.mytennis.model.Record;

import android.graphics.Bitmap;

public interface RecordListViewService {

	public Map<String, Bitmap> getHeadMap();
	public List<Record> getRecordList();
	public Bitmap getBackground();
	public void updateBackground(Bitmap bitmap);
	public void startSaveAsDialog();
	public void startThemeDialog();
	public void startSearchDialog();
	public void startCompetitorDialog();
	public void exitApp();
	public void onBackPressCallback();
	public void updateRecordList(List<Record> list);
	public void startUpdateDialog(int position);
	public void startDetailDialog(Record record, int which);

	Map<String, String> getNamePinyinMap();
}
