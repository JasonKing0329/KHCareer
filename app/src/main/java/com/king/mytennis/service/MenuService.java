package com.king.mytennis.service;

import java.util.ArrayList;

import android.content.Context;

import com.king.mytennis.interfc.DatabaseAccess;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.DatabaseStruct;
import com.king.mytennis.model.Record;
import com.king.mytennis.model.FileIO;
import com.king.mytennis.model.SQLiteDB;

public class MenuService {

	public static final String HISTORY_FOLDER_ALREADY_EXIST = "<<alreadyexist>>";
	private FileIO fileIO;

	public MenuService() {
		fileIO = new FileIO();
	}

	public String saveDatabases() {

		//return saveas(Configuration.getInstance().DEF_FILE, list);//文件版本
		//return saveas(Configuration.DEF_CONTENT + DatabaseStruct.DATABASE, null);

		//ExternalRecordTool.saveDbAsHistory();
		//v5.4 change

		return ExternalRecordTool.saveAllDbAsHistory(null);
	}

	public String saveDatabasesToFolder(String folderName) {

		//return fileIO.saveAll(list, path);//文件版本
		String folderPath = Configuration.HISTORY_BASE + folderName;
		return ExternalRecordTool.saveAllDbAsHistory(folderPath);
	}

	public ArrayList<Record> loadRecords(Context context) {

		return new RecordService(context).queryAll();
	}

	public String saveConfiguration() {

		return fileIO.saveConfigInfor(Configuration.getInstance());
	}

	@Deprecated
	public String fileToDB(ArrayList<Record> list) {

		/**文件版本
		 if (list == null) {
		 return fileToDbFail;
		 }
		 DatabaseAccess sqlite = new SQLiteDB();
		 try {
		 sqlite.clearTable(DatabaseStruct.TABLE_RECORD);
		 } catch (Exception e) {
		 return fileToDbFail;
		 }
		 RecordService recordService = new RecordService();
		 recordService.addRecordsToDB(list);
		 return fileToDbSuccess;
		 */
//		boolean result = ExternalRecordTool.forceCopyFromContent(
//				Configuration.getInstance().DEF_FILE);
//		if (result) {
//			return fileToDbSuccess;
//		}
//		else {
//			return fileToDbFail;
//		}
		return null;
	}

	public void alterDefLanguage(Context context, String lang) {

		DatabaseAccess sqlite = new SQLiteDB(context);
		sqlite.update(DatabaseStruct.TABLE_CONF, "id=?", new String[]{"0"}
				, DatabaseStruct.TABLE_CONF_COL, new String[]{"0", lang});
	}
}
