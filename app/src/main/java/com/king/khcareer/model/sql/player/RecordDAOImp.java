package com.king.khcareer.model.sql.player;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.player.interfc.DatabaseAccess;
import com.king.khcareer.model.sql.player.interfc.RecordDAO;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.config.Configuration;

public class RecordDAOImp implements RecordDAO {

	private DatabaseAccess sqLite;

	//private MySQLHelper sqlHelper;
	public RecordDAOImp() {
		//sqlHelper = new MySQLHelper(context, DATABASE);
		sqLite = new SQLiteDB();
	}

	//private MySQLHelper sqlHelper;
	public RecordDAOImp(MultiUser user) {
		//sqlHelper = new MySQLHelper(context, DATABASE);
		sqLite = new SQLiteDB(user);
	}

	@Override
	public DatabaseAccess getSqLite() {
		return sqLite;
	}
	@Override
	public void add(Record record) {

		String[] values = new String[DatabaseStruct.TABLE_RECORD_COL.length];
		//已设计为自增
		//values[DatabaseStruct.COL_ID] = "" + getNewId();
		values[DatabaseStruct.COL_competitor] = record.getCompetitor();
		values[DatabaseStruct.COL_competitor_country] = record.getCptCountry();
		values[DatabaseStruct.COL_court] = record.getCourt();
		values[DatabaseStruct.COL_date_long] = String.valueOf(record.getLongDate());
		values[DatabaseStruct.COL_date_str] = record.getStrDate();
		values[DatabaseStruct.COL_iswinner] = record.getWinner();
		values[DatabaseStruct.COL_level] = record.getLevel();
		values[DatabaseStruct.COL_match] = record.getMatch();
		values[DatabaseStruct.COL_match_city] = record.getCity();
		values[DatabaseStruct.COL_match_country] = record.getMatchCountry();
		values[DatabaseStruct.COL_rank] = "" + record.getCptRank();
		values[DatabaseStruct.COL_rankp1] = "" + record.getRank();
		values[DatabaseStruct.COL_seed] = "" + record.getCptSeed();
		values[DatabaseStruct.COL_seedp1] = "" + record.getSeed();
		values[DatabaseStruct.COL_region] = record.getRegion();
		values[DatabaseStruct.COL_round] = record.getRound();
		values[DatabaseStruct.COL_score] = record.getScore();
		sqLite.insert(DatabaseStruct.TABLE_RECORD, DatabaseStruct.TABLE_RECORD_COL, values);
	}

	@Override
	public void update(Record record) {

		String[] values = new String[DatabaseStruct.TABLE_RECORD_COL.length];
		values[DatabaseStruct.COL_ID] = "" + record.getId();
		values[DatabaseStruct.COL_competitor] = record.getCompetitor();
		values[DatabaseStruct.COL_competitor_country] = record.getCptCountry();
		values[DatabaseStruct.COL_court] = record.getCourt();
		values[DatabaseStruct.COL_date_long] = String.valueOf(record.getLongDate());
		values[DatabaseStruct.COL_date_str] = record.getStrDate();
		values[DatabaseStruct.COL_iswinner] = record.getWinner();
		values[DatabaseStruct.COL_level] = record.getLevel();
		values[DatabaseStruct.COL_match] = record.getMatch();
		values[DatabaseStruct.COL_match_city] = record.getCity();
		values[DatabaseStruct.COL_match_country] = record.getMatchCountry();
		values[DatabaseStruct.COL_rank] = "" + record.getCptRank();
		values[DatabaseStruct.COL_rankp1] = "" + record.getRank();
		values[DatabaseStruct.COL_seed] = "" + record.getCptSeed();
		values[DatabaseStruct.COL_seedp1] = "" + record.getSeed();
		values[DatabaseStruct.COL_region] = record.getRegion();
		values[DatabaseStruct.COL_round] = record.getRound();
		values[DatabaseStruct.COL_score] = record.getScore();
		sqLite.update(DatabaseStruct.TABLE_RECORD, "id=?"
				, new String[]{""+record.getId()}, DatabaseStruct.TABLE_RECORD_COL, values);
	}

	@Override
	public void delete(Record record) {

		sqLite.delete(DatabaseStruct.TABLE_RECORD, "id=?"
				, new String[]{""+record.getId()});
	}

	@Override
	public Record get(Record record) {
		//query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		//query里的参数其实就是将select语句分块
		//table表名，colums是查询的列名string[]，selection则是where子句(动态编译)
		//，selectionArgs是对应于?的值，后面的也很熟悉了
		SQLiteDatabase db = sqLite.getSQLHelper().getWritableDatabase();
		Cursor cursor=db.query(
				DatabaseStruct.TABLE_RECORD, DatabaseStruct.TABLE_RECORD_COL
				, "id = ?", new String[]{""+record.getId()}, null, null, null);
		if (cursor.moveToNext()){
			adaptRecord(cursor, record);
		}
        cursor.close();
		return record;
	}

	private void adaptRecord(Cursor cursor, Record record) {
		record.setId(cursor.getInt(DatabaseStruct.COL_ID));
		record.setCompetitor(cursor.getString(DatabaseStruct.COL_competitor));
		record.setCptCountry(cursor.getString(DatabaseStruct.COL_competitor_country));
		record.setCourt(cursor.getString(DatabaseStruct.COL_court));
		record.setLongDate(Long.parseLong(cursor.getString(DatabaseStruct.COL_date_long)));
		record.setStrDate(cursor.getString(DatabaseStruct.COL_date_str));
		record.setWinner(cursor.getString(DatabaseStruct.COL_iswinner));
		record.setLevel(cursor.getString(DatabaseStruct.COL_level));
		record.setMatch(cursor.getString(DatabaseStruct.COL_match));
		record.setCity(cursor.getString(DatabaseStruct.COL_match_city));
		record.setMatchCountry(cursor.getString(DatabaseStruct.COL_match_country));
		record.setRank(Integer.parseInt(cursor.getString(DatabaseStruct.COL_rankp1)));
		record.setCptRank(Integer.parseInt(cursor.getString(DatabaseStruct.COL_rank)));
		record.setSeed(Integer.parseInt(cursor.getString(DatabaseStruct.COL_seedp1)));
		record.setCptSeed(Integer.parseInt(cursor.getString(DatabaseStruct.COL_seed)));
		record.setRegion(cursor.getString(DatabaseStruct.COL_region));
		record.setRound(cursor.getString(DatabaseStruct.COL_round));
		record.setScore(cursor.getString(DatabaseStruct.COL_score));
	}

	@Override
	public ArrayList<Record> queryAll() {

		ArrayList<Record> list = new ArrayList<>();
		SQLiteDatabase db = sqLite.getSQLHelper().getWritableDatabase();
		Cursor cursor=db.query(
				DatabaseStruct.TABLE_RECORD, DatabaseStruct.TABLE_RECORD_COL
				, null, null, null, null, null);
		Record record = null;
		while (cursor.moveToNext()){
			record = new Record();
			adaptRecord(cursor, record);
			list.add(record);
		}
        cursor.close();
		return list;
	}

	@Override
	public String saveAll(ArrayList<Record> list, String path) {

		return null;
	}

	@Override
	public Configuration readConfigInfor() {

		return null;
	}

	@Override
	public String saveConfigInfor(Configuration conf) {

		return null;
	}
	@Override
	public void addRecords(ArrayList<Record> list) {

		SQLiteOpenHelper helper = sqLite.getSQLHelper();
		SQLiteDatabase db = helper.getWritableDatabase();
		for (Record record:list) {
			ContentValues contentValues = new ContentValues();
			//ID已设为自增
			//contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_ID], record.getId());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_competitor] , record.getCompetitor());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_competitor_country] , record.getCptCountry());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_court] , record.getCourt());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_date_long] , String.valueOf(record.getLongDate()));
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_date_str] , record.getStrDate());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_iswinner] , record.getWinner());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_level] , record.getLevel());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_match] , record.getMatch());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_match_city] , record.getCity());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_match_country] , record.getMatchCountry());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_rank] , "" + record.getCptRank());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_rankp1] , "" + record.getRank());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_seed] , "" + record.getCptSeed());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_seedp1] , "" + record.getSeed());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_region] , record.getRegion());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_round] , record.getRound());
			contentValues.put(DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_score] , record.getScore());
			db.insert(DatabaseStruct.TABLE_RECORD, null, contentValues);
		}
	}

	@Override
	public String[] getCptNames() {

		SQLiteOpenHelper helper = sqLite.getSQLHelper();
		SQLiteDatabase db = helper.getWritableDatabase();

		List<String> list = new ArrayList<String>();
		Cursor cursor = db.query(DatabaseStruct.TABLE_RECORD
				, new String[]{DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_competitor]}
				, null, null, DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_competitor], null, null);
		cursor.moveToPosition(-1);
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}
		if (list.size()>0) {
			String[] strings = new String[list.size()];
			for (int i = 0; i < list.size(); i ++) {
				strings[i] = list.get(i);
			}
			return strings;
		}
		cursor.close();
		return null;
	}

	@Override
	public String[] getMatchNames() {
		SQLiteOpenHelper helper = sqLite.getSQLHelper();
		SQLiteDatabase db = helper.getWritableDatabase();

		List<String> list = new ArrayList<String>();
		Cursor cursor = db.query(DatabaseStruct.TABLE_RECORD
				, new String[]{DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_match]}
				, null, null, DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_match], null, null);
		cursor.moveToPosition(-1);
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}
		if (list.size()>0) {
			String[] strings = new String[list.size()];
			for (int i = 0; i < list.size(); i ++) {
				strings[i] = list.get(i);
			}
			return strings;
		}
		return null;
	}

	@Override
	public ArrayList<Record> queryByWhere(String where, String[] values) {

		if (where == null) {
			return null;
		}
		ArrayList<Record> list = null;
		SQLiteOpenHelper helper = sqLite.getSQLHelper();
		SQLiteDatabase db = helper.getReadableDatabase();
		try {
			Cursor cursor=db.query(
					DatabaseStruct.TABLE_RECORD, DatabaseStruct.TABLE_RECORD_COL
					, where, values, null, null, null);
			Record record = null;
			while (cursor.moveToNext()){
				if (list == null) {
					list = new ArrayList<Record>();
				}
				record = new Record();
				adaptRecord(cursor, record);
				list.add(record);
			}
            cursor.close();
		} catch (Exception e) {
			Log.i("mytennis", "queryByWhere抛出异常：" + e.getMessage());
			list = null;
		}
		return list;
	}

	@Override
	public List<Record> queryByMatch(String match) {

		if (match == null) {
			return null;
		}
		List<Record> list = new ArrayList<>();
		SQLiteOpenHelper helper = sqLite.getSQLHelper();
		SQLiteDatabase db = helper.getReadableDatabase();
		try {
			Cursor cursor=db.query(
					DatabaseStruct.TABLE_RECORD, DatabaseStruct.TABLE_RECORD_COL
					, "match=?", new String[]{match}, null, null, null);
			Record record;
			while (cursor.moveToNext()){
				if (list == null) {
					list = new ArrayList<>();
				}
				record = new Record();
				adaptRecord(cursor, record);
				list.add(record);
			}
            cursor.close();
		} catch (Exception e) {
			Log.i("mytennis", "queryByWhere抛出异常：" + e.getMessage());
			list = null;
		}
		return list;
	}

	@Override
	public List<Record> queryByCompetitor(String competitor) {

		if (competitor == null) {
			return null;
		}
		List<Record> list = new ArrayList<>();
		SQLiteOpenHelper helper = sqLite.getSQLHelper();
		SQLiteDatabase db = helper.getReadableDatabase();
		try {
			Cursor cursor=db.query(
					DatabaseStruct.TABLE_RECORD, DatabaseStruct.TABLE_RECORD_COL
					, "competitor=?", new String[]{competitor}, null, null, null);
			Record record;
			while (cursor.moveToNext()){
				if (list == null) {
					list = new ArrayList<>();
				}
				record = new Record();
				adaptRecord(cursor, record);
				list.add(record);
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("mytennis", "queryByWhere抛出异常：" + e.getMessage());
			list = null;
		}
		return list;
	}

}
