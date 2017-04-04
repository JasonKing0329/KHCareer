package com.king.mytennis.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * 像素转换工具
 * 		计算空间位置
 * @author qin
 *
 */
public class DensityUtil {
	
	static DisplayMetrics dm = new DisplayMetrics();

    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    } 

	/**
	 * 设置view的宽高
	 * 
	 * @param c
	 * @param view
	 */
	public static void setViewWH(Context c, View view, int w, int h) {
		android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
		// DisplayMetrics dm = new DisplayMetrics();
		((Activity) c).getWindowManager().getDefaultDisplay().getMetrics(dm);

		// // 窗口的宽度
		params.width = (int) ((dm.widthPixels / 640f) * w);
		params.height = (int) ((dm.heightPixels / 1136f) * h);
		view.setLayoutParams(params);
	}
	/**
	 * 设置宽高以宽度为标准
	 * 
	 * @param c
	 * @param view
	 * @param w
	 */
	public static void setViewWHForW(Context c, View view, int w,int h) {
		android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
//		 DisplayMetrics dm = new DisplayMetrics();
		((Activity) c).getWindowManager().getDefaultDisplay().getMetrics(dm);

		// // 窗口的宽度
		params.width = (int) (dm.widthPixels * w/ 640f);
		params.height = (int) (dm.widthPixels * h / 640f);
		view.setLayoutParams(params);
	}
	/**
	 * 以宽为基准 设置圆形或者正方形
	 * 
	 * @param c
	 * @param view
	 * @param w
	 */
	public static void setViewWH(Context c, View view, int w) {
		android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
		// DisplayMetrics dm = new DisplayMetrics();
		((Activity) c).getWindowManager().getDefaultDisplay().getMetrics(dm);

		// // 窗口的宽度
		params.width = (int) (dm.widthPixels * w/ 640f) ;
		params.height = (int) (dm.widthPixels * w/ 640f) ;
//		view.setPadding(left, top, right, bottom);
		view.setLayoutParams(params);
	}
	/**
	 * 只设置高度
	 * @param c
	 * @param view
	 * @param w
	 * @param h
	 */
	public static void setViewOnlyH(Context c, View view, int h) {
		android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
		// DisplayMetrics dm = new DisplayMetrics();
		((Activity) c).getWindowManager().getDefaultDisplay().getMetrics(dm);

		// // 窗口的宽度
//		params.width = (dm.widthPixels) ;
		params.height = (int) ((dm.heightPixels / 1136f) * h);
		view.setLayoutParams(params);
	}
	/**
	 * 只设置宽度
	 * @param c
	 * @param view
	 * @param w
	 * @param h
	 */
	public static void setViewOnlyW(Context c, View view, int w) {
		android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
		// DisplayMetrics dm = new DisplayMetrics();
		((Activity) c).getWindowManager().getDefaultDisplay().getMetrics(dm);

		// // 窗口的宽度
		params.width = (int) ((dm.widthPixels / 640f) * w);
//		params.height = (dm.heightPixels);
		view.setLayoutParams(params);
	}
	/**
	 * 设置多倍view宽度  为屏幕宽度的倍数
	 * @param c
	 * @param view
	 * @param w
	 */
	public static void setViewMultW(Context c, View view, int w) {
		android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
		// DisplayMetrics dm = new DisplayMetrics();
		((Activity) c).getWindowManager().getDefaultDisplay().getMetrics(dm);

		// // 窗口的宽度
		params.width = (int) ((dm.widthPixels / w));
//		params.height = (dm.heightPixels);
		view.setLayoutParams(params);
	}
	
	/**
	 * 获取屏幕宽度
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context){
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	/**
	 * 获取屏幕高度
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context){
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
}
