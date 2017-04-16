package com.king.khcareer.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.utils.FormatFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CacheController {

	public static String TEMP_H2H_FILE = Configuration.TEMP_HTTP_DIR + "h2h.html";
	public static String TEMP_RANK_SINA_FILE = Configuration.TEMP_HTTP_DIR + "sinarank.html";
	public static String TEMP_RANK_ATP_FILE = Configuration.TEMP_HTTP_DIR + "atprank.html";
	public static String TEMP_UNKNOWN = Configuration.TEMP_HTTP_DIR + "unknown.html";

	public static final int CACHE_TYPE_H2H = 0;
	public static final int CACHE_TYPE_RANK_SINA = 1;
	public static final int CACHE_TYPE_RANK_ATP = 2;

	public static final String CACHE_IMAGE_EXTRA = ".jpg";

	public CacheController() {

	}

	public void clearCache() {

		File file = new File(Configuration.TEMP_HTTP_DIR);
		File[] files = file.listFiles();
		for (File f:files) {
			f.delete();
		}
		files = new File(Configuration.TEMP_IMAGE_DIR).listFiles();
		for (File f:files) {
			f.delete();
		}
	}

	public Bitmap getPlayerImage(String engName) {

		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(Configuration.TEMP_IMAGE_DIR + engName + CACHE_IMAGE_EXTRA);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * apply for HttpClient method
	 * @param datas
	 * @param type
	 * @throws IOException
	 */
	public void saveTempFileByte(byte[] datas, int type) throws IOException {
		String path = null;
		if (type == CACHE_TYPE_H2H) {
			path = TEMP_H2H_FILE;
		}
		else if (type == CACHE_TYPE_RANK_SINA) {
			path = TEMP_RANK_SINA_FILE;
		}
		else if (type == CACHE_TYPE_RANK_ATP) {
			path = TEMP_RANK_ATP_FILE;
		}
		else {
			path = TEMP_UNKNOWN;
		}

		FileOutputStream stream = new FileOutputStream(path);

		int times = datas.length/1024;
		for (int i = 0; i < times; i ++) {
			stream.write(datas, 1024 * i, 1024);
		}
		stream.write(datas, 1024 * times, datas.length - 1024 * times);

		stream.close();
	}

	/**
	 * apply for URLConnection method
	 * @param inputStream
	 * @param type
	 * @throws IOException
	 */
	public void saveTempFile(InputStream inputStream, int type) throws IOException {
		String path = null;
		if (type == CACHE_TYPE_H2H) {
			path = TEMP_H2H_FILE;
		}
		else if (type == CACHE_TYPE_RANK_SINA) {
			path = TEMP_RANK_SINA_FILE;
		}
		else if (type == CACHE_TYPE_RANK_ATP) {
			path = TEMP_RANK_ATP_FILE;
		}
		else {
			path = TEMP_UNKNOWN;
		}

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		FileWriter writer = new FileWriter(path);
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		writer.write(buffer.toString());
		writer.close();
		reader.close();
		inputStream.close();
	}

	public void cacheImage(Bitmap bitmap, String engName) {

		try {
			File file = new File(Configuration.TEMP_IMAGE_DIR + engName + CACHE_IMAGE_EXTRA);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fileOutputStream=new FileOutputStream(file);
			int quality = 100;
			if (fileOutputStream!=null) {
				//imageBitmap.compress(format, quality, stream); 
				//把位图的压缩信息写入到一个指定的输出流中
				//第一个参数format为压缩的格式
				//第二个参数quality为图像压缩比的值,0-100.0 意味着小尺寸压缩,100意味着高质量压缩
				//第三个参数stream为输出流
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
			}
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getCacheSize() {

		String result = null;
		int sum = 0;
		File file = new File(Configuration.TEMP_HTTP_DIR);
		File[] files = file.listFiles();
		for (File f:files) {
			sum += f.length();
		}
		files = new File(Configuration.TEMP_IMAGE_DIR).listFiles();
		for (File f:files) {
			sum += f.length();
		}

		result = FormatFactory.formatFileSize(sum);
		return result;
	}

}
