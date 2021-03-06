package com.king.khcareer.common.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

import com.king.khcareer.common.config.Configuration;

public class ImageFactory {

	public ImageFactory() {

	}

	@Deprecated //放弃该加载方式，全部改用ImageLoader加载
	public Bitmap getDefBackground() {

		return getBackground(Configuration.getInstance().DEF_BK);
	}

	@Deprecated //放弃该加载方式，全部改用ImageLoader加载
	public Bitmap getBackground(String path) {
		Bitmap background;
		if (!path.equals("def")) {
			try {
				FileInputStream stream = new FileInputStream(path);
				background = BitmapFactory.decodeStream(stream);
				stream.close();
			} catch (Exception e) {
				//通过调试，android对activity的使用好像是同步的，不能直接获得了
				//background = userActivity.getResources().getDrawable(R.drawable.bk_mainview);
				background = null;
				e.printStackTrace();
			}
		}
		else {
			//background = userActivity.getResources().getDrawable(R.drawable.bk_mainview);
			background = null;
		}
		return background;
	}

	/*
	@Deprecated //放弃该加载方式，全部改用ImageLoader加载
	public Bitmap getDetailMatch(String match_name, String court) {

		InputStream stream=null;
		Bitmap image_match=null;
		try {
			stream = new FileInputStream(Configuration.IMG_MATCH_BASE + match_name + ".jpg");
			image_match=BitmapFactory.decodeStream(stream);
			stream.close();
		} catch (FileNotFoundException e) {
			try {
				if (court.equals("硬地")) {
					stream = new FileInputStream(Configuration.IMG_DEFAULT_BASE + Configuration.DEF_IMG_HARD);
				}
				else if (court.equals("红土")) {
					stream = new FileInputStream(Configuration.IMG_DEFAULT_BASE + Configuration.DEF_IMG_CLAY);
				}
				else if (court.equals("室内硬地")) {
					stream = new FileInputStream(Configuration.IMG_DEFAULT_BASE + Configuration.DEF_IMG_INNERHARD);
				}
				else if (court.equals("草地")) {
					stream = new FileInputStream(Configuration.IMG_DEFAULT_BASE + Configuration.DEF_IMG_GRASS);
				}
				if (stream != null) {
					image_match=BitmapFactory.decodeStream(stream);
					stream.close();
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return image_match;
	}

	@Deprecated //放弃该加载方式，全部改用ImageLoader加载
	public Bitmap getDetailPlayer(String competitor) {

		InputStream stream=null;
		Bitmap image_player=null;
		try {
			stream = new FileInputStream(Configuration.IMG_PLAYER_BASE + competitor+".jpg");
			//stream = new FileInputStream(IMG_PLAYER_BASE+"murry.jpg");//测试用，模拟器上不能放中文图片
			image_player=BitmapFactory.decodeStream(stream);
			stream.close();
		} catch (FileNotFoundException e) {
			System.out.println("图片不存在");
			//BitmapDrawable bd=(BitmapDrawable)record.image_player;可以将已有drawable对象转换为bitmap对象
			//image_player=bd.getBitmap();
			//image_player=BitmapFactory.decodeResource(act.getResources(), R.drawable.player_other);这个是可以把资源文件转换为bitmap对象
			try {
				stream = new FileInputStream(Configuration.IMG_DEFAULT_BASE + Configuration.DEF_IMG_PLAYER);
				image_player=BitmapFactory.decodeStream(stream);
				stream.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return image_player;
	}
	*/

	/**
	 * 采用开源框架ImageLoader加载文件路径（加载文件夹中指定序号的图片）
	 * 调用该方法表示已有player对应的文件夹
	 * @param name
	 * @param indexInFolder
	 * @return
	 */
	public static String getDetailPlayerPath(String name, int indexInFolder) {
		try {
			File file = new File(Configuration.IMG_PLAYER_BASE + name);
			name = file.listFiles()[indexInFolder].getPath();
		} catch (Exception e) {
			e.printStackTrace();
			name = getDetailPlayerPath(name);
		}
		return name;
	}
	/**
	 * 采用开源框架ImageLoader加载文件路径
	 * @param name
	 * @return
	 */
	public static String getDetailPlayerPath(String name) {
		return getDetailPlayerPath(name, null);
	}
	/**
	 * 采用开源框架ImageLoader加载文件路径
	 * @param name
	 * @param indexMap 如果存在文件夹，保存本次随机的序号
	 * @return
	 */
	public static String getDetailPlayerPath(String name, Map<String, Integer> indexMap) {
		File file = new File(Configuration.IMG_PLAYER_BASE + name);
		// 存在文件夹，则随机显示里面的任何图片
		if (file.exists() && file.isDirectory()) {
			File files[] = file.listFiles();
			// 没有图片
			if (files == null || files.length == 0) {
				name = null;
			}
			else {
				if (files.length == 1) {
					if (indexMap != null) {
						indexMap.put(name, 0);
					}
					name = files[0].getPath();
				}
				else {
					int index = Math.abs(new Random().nextInt()) % files.length;
					if (indexMap != null) {
						indexMap.put(name, index);
					}
					name = files[index].getPath();
				}
			}
		}
		else {
			// 只有单张图的情况
			name = Configuration.IMG_PLAYER_BASE + name+".jpg";
			// 没有图片
			if (!new File(name).exists()) {
				name = null;
			}
		}
		// 没有图片显示默认图片
		if (name == null) {
			name = Configuration.IMG_DEFAULT_BASE + Configuration.DEF_IMG_PLAYER;
		}
		return name;
	}

	@Deprecated //由ImageLoader实现
	public ArrayList<Bitmap> getContentImages(File[] files) {

		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>(files.length);

		FileInputStream stream = null;
		for (File file:files) {
			try {
				Bitmap bitmap = null;
				stream = new FileInputStream(file.getPath());
				bitmap = compressFromFile(file.getPath(), 250);
				bitmaps.add(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return bitmaps;
	}

	/**
	 * @param bitmap 原图片
	 * @return 圆角矩形图片
	 */
	public static Bitmap getCircleBitmap(Bitmap bitmap) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if (width > height) {
			width = height;
		}
		else {
			height = width;
		}
		int radius = width / 2;

		Bitmap output = Bitmap.createBitmap(width,
				height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, height);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(radius, radius, radius, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public Bitmap getPlayerHead(String name, int size) throws FileNotFoundException {
		name = Configuration.IMG_PLAYER_HEAD + name + ".jpg";
		return compressFromFile(name, size);
	}

	/**
	 * 采用开源框架ImageLoader加载文件路径（加载文件夹中指定序号的图片）
	 * 调用该方法表示已有player对应的文件夹
	 * @param name
	 * @param indexInFolder
	 * @return could be null
	 */
	public static String getPlayerHeadPath(String name, int indexInFolder) {
		try {
			File file = new File(Configuration.IMG_PLAYER_HEAD + name);
			name = file.listFiles()[indexInFolder].getPath();
		} catch (Exception e) {
			e.printStackTrace();
			name = getDetailPlayerPath(name);
		}
		return name;
	}
	/**
	 * 采用开源框架ImageLoader加载文件路径
	 * @param name
	 * @return could be null
	 */
	public static String getPlayerHeadPath(String name) {
		return getPlayerHeadPath(name, null);
	}

	/**
	 * 采用开源框架ImageLoader加载文件路径
	 * @param name
	 * @param indexMap 如果存在文件夹，保存本次随机的序号
	 * @return could be null
	 */
	public static String getPlayerHeadPath(String name, Map<String, Integer> indexMap) {
		File file = new File(Configuration.IMG_PLAYER_HEAD + name);
		// 存在文件夹，则随机显示里面的任何图片
		if (file.exists() && file.isDirectory()) {
			File files[] = file.listFiles();
			// 没有图片
			if (files == null || files.length == 0) {
				name = null;
			}
			else {
				if (files.length == 1) {
					if (indexMap != null) {
						indexMap.put(name, 0);
					}
					name = files[0].getPath();
				}
				else {
					int index = Math.abs(new Random().nextInt()) % files.length;
					if (indexMap != null) {
						indexMap.put(name, index);
					}
					name = files[index].getPath();
				}
			}
		}
		else {
			// 只有单张图的情况
			name = Configuration.IMG_PLAYER_HEAD + name+".jpg";
			// 没有图片
			if (!new File(name).exists()) {
				name = null;
			}
		}
		return name;
	}

	@Deprecated
	public Bitmap getMatchHead(String name, String court, int size) throws FileNotFoundException {

		name = Configuration.IMG_MATCH_BASE + name + ".jpg";
		if (!new File(name).exists()) {
			if (court.equals("硬地")) {
				name = Configuration.IMG_DEFAULT_BASE + Configuration.DEF_IMG_HARD;
			}
			else if (court.equals("红土")) {
				name = Configuration.IMG_DEFAULT_BASE + Configuration.DEF_IMG_CLAY;
			}
			else if (court.equals("室内硬地")) {
				name = Configuration.IMG_DEFAULT_BASE + Configuration.DEF_IMG_INNERHARD;
			}
			else if (court.equals("草地")) {
				name = Configuration.IMG_DEFAULT_BASE + Configuration.DEF_IMG_GRASS;
			}
		}
		return compressFromFile(name, size);
	}

	/**
	 * 采用开源框架ImageLoader加载文件路径
	 * @param name
	 * @param court
	 * @return
	 */
	public static String getMatchHeadPath(String name, String court, int indexInFolder) {
		try {
			File file = new File(Configuration.IMG_MATCH_BASE + name);
			name = file.listFiles()[indexInFolder].getPath();
		} catch (Exception e) {
			e.printStackTrace();
			name = getMatchHeadPath(name, court);
		}
		return name;
	}
	/**
	 * 采用开源框架ImageLoader加载文件路径
	 * @param name
	 * @param court
	 * @return
	 */
	public static String getMatchHeadPath(String name, String court) {
		return getMatchHeadPath(name, court, null);
	}
	/**
	 * 采用开源框架ImageLoader加载文件路径
	 * @param name
	 * @param court
	 * @return
	 */
	public static String getMatchHeadPath(String name, String court, Map<String, Integer> indexMap) {
		File file = new File(Configuration.IMG_MATCH_BASE + name);
		// 存在文件夹，则随机显示里面的任何图片
		if (file.exists() && file.isDirectory()) {
			File files[] = file.listFiles();
			// 没有图片
			if (files == null || files.length == 0) {
				name = null;
			}
			else {
				if (files.length == 1) {
					if (indexMap != null) {
						indexMap.put(name, 0);
					}
					name = files[0].getPath();
				}
				else {
					int index = Math.abs(new Random().nextInt()) % files.length;
					if (indexMap != null) {
						indexMap.put(name, index);
					}
					name = files[index].getPath();
				}
			}
		}
		else {
			name = Configuration.IMG_MATCH_BASE + name + ".jpg";
			if (!new File(name).exists()) {
				name = null;
			}
		}
		if (name == null) {
			if (court.equals("硬地")) {
				name = Configuration.IMG_DEFAULT_BASE + Configuration.DEF_IMG_HARD;
			}
			else if (court.equals("红土")) {
				name = Configuration.IMG_DEFAULT_BASE + Configuration.DEF_IMG_CLAY;
			}
			else if (court.equals("室内硬地")) {
				name = Configuration.IMG_DEFAULT_BASE + Configuration.DEF_IMG_INNERHARD;
			}
			else if (court.equals("草地")) {
				name = Configuration.IMG_DEFAULT_BASE + Configuration.DEF_IMG_GRASS;
			}
		}
		return name;
	}

	public static Bitmap compressFromFile(String fileName, int size) throws FileNotFoundException {

		InputStream temp = null;
		temp = new FileInputStream(fileName);
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
		options.inJustDecodeBounds = true;
		// 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
		BitmapFactory.decodeStream(temp, null, options);
		// 关闭流
		try {
			temp.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 生成压缩的图片
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			// 这一步是根据要设置的大小，使宽和高都能满足
			if ((options.outWidth >> i <= size)
					&& (options.outHeight >> i <= size)) {
				// 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
				temp = new FileInputStream(fileName);
				// 这个参数表示 新生成的图片为原始图片的几分之一。
				options.inSampleSize = (int) Math.pow(2.0D, i);
				// 这里之前设置为了true，所以要改为false，否则就创建不出图片
				options.inJustDecodeBounds = false;

				bitmap = BitmapFactory.decodeStream(temp, null, options);
				break;
			}
			i ++;
		}
		return bitmap;
	}
}
