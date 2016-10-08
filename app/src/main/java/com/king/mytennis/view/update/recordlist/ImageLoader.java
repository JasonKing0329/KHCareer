package com.king.mytennis.view.update.recordlist;

import android.graphics.Bitmap;

public interface ImageLoader {
	public Bitmap loadPlayerHead(String name);

	public Bitmap loadMatchImage(String name, String court);
}
