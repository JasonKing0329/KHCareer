package com.king.khcareer.common.image;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.king.mytennis.view.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 图片工具
 * 
 * @author zhouby
 */
public class ImageUtil {

	/** 图片加载 **/
	private static ImageLoader imageLoader;
	/** 图片加载设置 **/
	private static DisplayImageOptions options;

	/**
	 * 初始化图片工具类
	 */
	public static void initImageLoader(Context context) {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build());

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	/**
	 * 初始化图片工具类
	 */
	public static void initImageLoader(Context context,int rid) {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build());

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(rid)
				.showImageOnFail(rid)
				.showImageOnLoading(rid)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	public static void initImageLoaderCache(Context context){
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build());

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	public static void initImageLoaderCache(Context context,int rid){
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build());

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(rid)
				.showImageOnFail(rid)
				.showImageOnLoading(rid)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	/**
	 * 加载图片
	 */
	public static void load(String uri, ImageView imageView) {
		imageLoader.displayImage(uri, imageView, options);
	}

	/**
	 * 加载图片
	 */
	public static void load(String uri, ImageView imageView,
			ImageLoadingListener listener) {
		imageLoader.displayImage(uri, imageView, options, listener);
	}

	/**
	 * 加载图片
	 */
	public static void load(String uri, ImageView imageView, int imgRes) {
		imageLoader.displayImage(
				uri,
				imageView,
				new DisplayImageOptions.Builder().cacheInMemory(true)
						.cacheOnDisk(true)
						.showImageOnFail(imgRes)
						.showImageOnLoading(imgRes)
						.showImageForEmptyUri(imgRes)
						.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
						.bitmapConfig(Bitmap.Config.RGB_565).build());
	}

}
