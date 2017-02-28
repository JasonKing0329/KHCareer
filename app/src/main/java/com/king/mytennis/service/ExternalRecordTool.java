package com.king.mytennis.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.DatabaseStruct;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 用于程序安装后第一次启动复制assets目录下的“省份——城市”数据库到安装包的databases目录下
 * @author king
 *
 */
public class ExternalRecordTool {

	//private static final String ASSETS_FILE = "mytennis";
	public static final String DATABASE = "mytennis";
	public static final String DATABASE_PLAYER = "mytennis_player.db";
	public static final String DATABASE_TIANQI = "mytennis_TianQi";
	public static final String DATABASE_FLAMENCO = "mytennis_Flamenco";
	public static final String DATABASE_HENRY = "mytennis_Henry";
	public static final String DATABASE_PUBLIC = "mytennis_public.db";

	private static final String[] targetDatabase = new String[] {
			DATABASE, DATABASE_TIANQI, DATABASE_FLAMENCO, DATABASE_HENRY, DATABASE_PUBLIC
	};

	/**
	 * 从assets目录复制的方法
	 * @param context
	 */
	public static void copyDbFromAssets(Context context, String dbFile) {

		SQLiteDatabase db = null;
		//先检查是否存在，不存在才复制
		try {
			db = SQLiteDatabase.openDatabase(Configuration.DATABASE_CONTENT + dbFile
					, null, SQLiteDatabase.OPEN_READONLY);
		} catch (Exception e) {
			db = null;
		}
		if (db == null) {
			try {
				InputStream assetsIn = context.getAssets().open(dbFile);
				File file = new File(Configuration.DATABASE_CONTENT);
				if (!file.exists()) {
					file.mkdir();
				}
				OutputStream fileOut = new FileOutputStream(Configuration.DATABASE_CONTENT + dbFile);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = assetsIn.read(buffer))>0){
					fileOut.write(buffer, 0, length);
				}

				fileOut.flush();
				fileOut.close();
				assetsIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (db != null) {
			db.close();
		}
	}

	/**
	 * 从assets目录复制的方法
	 * @param context
	 */
	public static void copyPublicDbFromAssets(Context context, String dbFile) {

		//先检查是否存在，不存在才复制
		File file = new File(Configuration.CONF_DIR + DatabaseStruct.DATABASE_PUBLIC);
		if (file.exists()) {
			return;
		}
		try {
			InputStream assetsIn = context.getAssets().open(dbFile);
			OutputStream fileOut = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = assetsIn.read(buffer))>0){
				fileOut.write(buffer, 0, length);
			}

			fileOut.flush();
			fileOut.close();
			assetsIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从assets目录复制res文件
	 * @param context
	 */
	public static void copyResFromAssets(Context context, String resFile, String targetPath) {

		File file = new File(targetPath);
		if (!file.exists()) {
			try {
				InputStream assetsIn = context.getAssets().open(resFile);
				OutputStream fileOut = new FileOutputStream(targetPath);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = assetsIn.read(buffer))>0){
					fileOut.write(buffer, 0, length);
				}

				fileOut.flush();
				fileOut.close();
				assetsIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * @param folderPath
	 * @return 保存后的文件目录
	 */
	public static String saveAllDbAsHistory(String folderPath) {

		File file = null;
		if (folderPath != null) {
			file = new File(folderPath);
			if (file.exists()) {
				return MenuService.HISTORY_FOLDER_ALREADY_EXIST;
			}
			else {
				file.mkdir();
			}
		}

		if (file == null) {
			Calendar calendar = Calendar.getInstance();
			StringBuffer target = new StringBuffer();
			target.append(calendar.get(Calendar.YEAR)).append("_");
			target.append(calendar.get(Calendar.MONTH) + 1).append("_");
			target.append(calendar.get(Calendar.DAY_OF_MONTH)).append("_");
			target.append(calendar.get(Calendar.HOUR)).append("_");
			target.append(calendar.get(Calendar.MINUTE)).append("_");
			target.append(calendar.get(Calendar.SECOND));
			file = new File(Configuration.HISTORY_BASE + target);
			file.mkdir();
		}

		saveDbTo(DATABASE, file);
		//saveDbTo(DATABASE_PLAYER, file);//saveDbPlayerAsHistory is called in H2hMainActivity
		saveDbTo(DATABASE_FLAMENCO, file);
		saveDbTo(DATABASE_TIANQI, file);
		saveDbTo(DATABASE_HENRY, file);

		return file.getPath();
	}
	private static void saveDbTo(String database, File directory) {
		SQLiteDatabase db = null;
		//先检查是否存在，存在才复制
		try {
			db = SQLiteDatabase.openDatabase(Configuration.DATABASE_CONTENT + database
					, null, SQLiteDatabase.OPEN_READONLY);
		} catch (Exception e) {
			db = null;
		}
		if (db != null) {
			try {
				InputStream in = new FileInputStream(Configuration.DATABASE_CONTENT + database);

				File file = new File(directory.getPath() + "/" + database);
				OutputStream fileOut = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = in.read(buffer))>0){
					fileOut.write(buffer, 0, length);
				}

				fileOut.flush();
				fileOut.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (db != null) {
			db.close();
		}
	}

	public static void saveDbAsHistory() {

		SQLiteDatabase db = null;
		//先检查是否存在，存在才复制
		try {
			db = SQLiteDatabase.openDatabase(Configuration.DATABASE_CONTENT + DATABASE
					, null, SQLiteDatabase.OPEN_READONLY);
		} catch (Exception e) {
			db = null;
		}
		if (db != null) {
			try {
				InputStream in = new FileInputStream(Configuration.DATABASE_CONTENT + DATABASE);
				File file = new File(Configuration.HISTORY_BASE);
				if (!file.exists()) {
					file.mkdir();
				}
				Calendar calendar = Calendar.getInstance();
				StringBuffer target = new StringBuffer();
				target.append(calendar.get(Calendar.YEAR)).append("_");
				target.append(calendar.get(Calendar.MONTH) + 1).append("_");
				target.append(calendar.get(Calendar.DAY_OF_MONTH)).append("_");
				target.append(calendar.get(Calendar.HOUR)).append("_");
				target.append(calendar.get(Calendar.MINUTE)).append("_");
				target.append(calendar.get(Calendar.SECOND)).append(".db");
				file = new File(Configuration.HISTORY_BASE + target);
				OutputStream fileOut = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = in.read(buffer))>0){
					fileOut.write(buffer, 0, length);
				}

				fileOut.flush();
				fileOut.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (db != null) {
			db.close();
		}
	}

	public static void saveDbPlayerAsHistory() {

		SQLiteDatabase db = null;
		//先检查是否存在，存在才复制
		try {
			db = SQLiteDatabase.openDatabase(Configuration.DATABASE_CONTENT + DATABASE_PLAYER
					, null, SQLiteDatabase.OPEN_READONLY);
		} catch (Exception e) {
			db = null;
		}
		if (db != null) {
			try {
				InputStream in = new FileInputStream(Configuration.DATABASE_CONTENT + DATABASE_PLAYER);


				Calendar calendar = Calendar.getInstance();
				StringBuffer target = new StringBuffer();
				target.append(calendar.get(Calendar.YEAR)).append("_");
				target.append(calendar.get(Calendar.MONTH) + 1).append("_");
				target.append(calendar.get(Calendar.DAY_OF_MONTH)).append("_");
				target.append(calendar.get(Calendar.HOUR)).append("_");
				target.append(calendar.get(Calendar.MINUTE)).append("_");
				target.append(calendar.get(Calendar.SECOND)).append(".db");

				File file = new File(Configuration.HISTORY_PLAYER_BASE + "player_" + target);
				OutputStream fileOut = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = in.read(buffer))>0){
					fileOut.write(buffer, 0, length);
				}

				fileOut.flush();
				fileOut.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (db != null) {
			db.close();
		}
	}
	/**
	 *
	 * @param context
	 */
	public static boolean replaceDatabase(Context context, String target, String dbFile) {

		if (target == null || !new File(target).exists()) {
			return false;
		}
		//先检查是否存在，存在则删除
		File defaultDb = new File(Configuration.DATABASE_CONTENT + dbFile);
		if (defaultDb.exists()) {
			defaultDb.delete();
		}
		try {
			InputStream in = new FileInputStream(target);
			File file = new File(Configuration.DATABASE_CONTENT + dbFile);
			OutputStream fileOut = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer))>0){
				fileOut.write(buffer, 0, length);
			}

			fileOut.flush();
			fileOut.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 从project content复制的方法
	 * @param context
	 */
	public static void copyDbFromContent(Context context, String externalFile) {

		SQLiteDatabase db = null;
		//先检查是否存在，不存在才复制
		try {
			db = SQLiteDatabase.openDatabase(Configuration.DATABASE_CONTENT + DATABASE
					, null, SQLiteDatabase.OPEN_READONLY);
		} catch (Exception e) {
			db = null;
		}
		if (db == null) {
			forceCopyFromContent(externalFile);
		}
		if (db != null) {
			db.close();
		}
	}

	public static boolean forceCopyFromContent(String externalFile) {

		try {
			InputStream in = new FileInputStream(externalFile);
			File file = new File(Configuration.DATABASE_CONTENT);
			if (!file.exists()) {
				file.mkdir();
			}
			OutputStream fileOut = new FileOutputStream(Configuration.DATABASE_CONTENT + DATABASE);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer))>0){
				fileOut.write(buffer, 0, length);
			}

			fileOut.flush();
			fileOut.close();
			in.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String copyDbToContent(String targetName) {

		String msg = null;
		SQLiteDatabase db = null;
		try {
			db = SQLiteDatabase.openDatabase(Configuration.DATABASE_CONTENT + DATABASE
					, null, SQLiteDatabase.OPEN_READONLY);
		} catch (Exception e) {
			db = null;
		}
		if (db != null) {//与反向不同，这里需要存在才执行复制
			try {
				InputStream in = new FileInputStream(Configuration.DATABASE_CONTENT + DATABASE);
				File file = new File(Configuration.DATABASE_CONTENT);
				if (!file.exists()) {
					file.mkdir();
				}
				OutputStream fileOut = new FileOutputStream(targetName);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = in.read(buffer))>0){
					fileOut.write(buffer, 0, length);
				}

				fileOut.flush();
				fileOut.close();
				in.close();
				msg = "ok";
			} catch (IOException e) {
				e.printStackTrace();
				msg = e.getMessage();
			}
		}
		if (db != null) {
			db.close();
		}
		return msg;
	}

	public static boolean replaceAllDatabase(Context context,
											 String from) {
		if (from == null || !new File(from).exists()) {
			return false;
		}

		for (int i = 0; i < targetDatabase.length; i ++) {
			//先检查是否存在，存在则删除
			File defaultDb = new File(Configuration.DATABASE_CONTENT + targetDatabase[i]);
			if (defaultDb.exists()) {
				defaultDb.delete();
			}
			try {
				InputStream in = new FileInputStream(from + "/" + targetDatabase[i]);
				File file = new File(Configuration.DATABASE_CONTENT + targetDatabase[i]);
				OutputStream fileOut = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = in.read(buffer))>0){
					fileOut.write(buffer, 0, length);
				}

				fileOut.flush();
				fileOut.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
