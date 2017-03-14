package com.king.mytennis.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.king.mytennis.interfc.DatabaseAccess;
import com.king.mytennis.interfc.RecordDAO;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.DatabaseStruct;
import com.king.mytennis.model.Record;
import com.king.mytennis.model.RecordDAOImp;
import com.king.mytennis.model.FileIO;
import com.king.mytennis.multiuser.MultiUser;

public class RecordService {

	private RecordDAO recordDao;

	public RecordService(Context context) {

		recordDao = new RecordDAOImp(context);
	}

	public RecordService(Context context, MultiUser user) {

		recordDao = new RecordDAOImp(context, user);
	}

	public DatabaseAccess getSqlite() {

		return recordDao.getSqLite();
	}

	public void addRecordsToDB(ArrayList<Record> list) {
		recordDao.addRecords(list);
	}

	public void initTableRecord(ArrayList<Record> list) {
		if (!recordDao.getSqLite().isTableExisted(DatabaseStruct.TABLE_RECORD, DatabaseStruct.TABLE_RECORD_COL)) {
			recordDao.getSqLite().createTable(DatabaseStruct.TABLE_RECORD, DatabaseStruct.TABLE_RECORD_PARAM);
			addRecordsToDB(list);
		}
	}

	/**
	 * insert后四个动作：list添加新记录，数据库添加新纪录，修改config的最近添加记录
	 * 其中最后一个由于涉及spinner的index,因此在调用该方法之前的insertDialog里面组装，在这里进行持久化
	 * @param record
	 */
	public boolean insert(Record record) {
		if (record != null) {
			//record.setId((int) new SQLiteDB().getNewRecordId());
			try {
				recordDao.add(record);
				FileIO fileRecord = new FileIO();
				fileRecord.saveConfigInfor(Configuration.getInstance());
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * delete后两个动作：数据库删除，list删除
	 * @param index
	 */
	public void delete(int index, List<Record> list) {
		recordDao.delete(list.get(index));
		list.remove(index);
	}

	/**
	 * update后两个动作：数据库修改，list修改(在调用该方法之前已做)
	 * @param record
	 */
	public void update(Record record) {
		recordDao.update(record);
	}

	public String[] getCptNames() {
		return recordDao.getCptNames();
	}

	public ArrayList<Record> queryAll() {

		/***从文件获取***/
		//return daoFile.queryAllFrom(Configuration.getInstance().DEF_FILE);
		/***从数据库获取***/
		return recordDao.queryAll();
	}

	public ArrayList<Record> queryByWhere(String where, String[] values) {

		return recordDao.queryByWhere(where, values);
	}
}
