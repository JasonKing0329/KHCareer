package com.king.khcareer.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

public class DisplayHelper {

	private static boolean fullscreen = false;
	public static boolean isTabModel(Context context) {
		if (context.getResources().getConfiguration().smallestScreenWidthDp >= 600) {
			return true;
		}
		return false;
	}
	
	public static Point getScreenSize(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Point point = new Point();
		wm.getDefaultDisplay().getSize(point);
		return point;
	}
	
	public static void enableFullScreen() {
		fullscreen = true;
	}
	public static boolean isFullScreen() {
		return fullscreen;
	}
}
