package com.king.mytennis.interfc;

import java.util.ArrayList;

import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.Record;

public interface RecordDAO {

	public void add(Record record);
	public void update(Record record);
	public void delete(Record record);
	public Record get(Record record);
	public DatabaseAccess getSqLite();
	public void addRecords(ArrayList<Record> list);
	public ArrayList<Record> queryAll();
	public ArrayList<Record> queryByWhere(String where, String[] values);
	public String saveAll(ArrayList<Record> list, String path);
	public Configuration readConfigInfor();
	public String saveConfigInfor(Configuration conf);
	public String[] getCptNames();
	public String[] getMatchNames();
	
}
