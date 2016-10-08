package com.king.mytennis.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class WallpaperAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Bitmap> bitmaps;
	public WallpaperAdapter(Context context, ArrayList<Bitmap> drawables) {
		this.context = context;
		bitmaps = drawables;
	}
	@Override
	public int getCount() {

		return bitmaps.size();//这个可不能不写，如果是默认的0那直接就没有任何东西显示了
	}

	@Override
	public Object getItem(int position) {

		return bitmaps.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView view = new ImageView(context);
		view.setImageBitmap(bitmaps.get(position));
		view.setScaleType(ImageView.ScaleType.FIT_CENTER);
		view.setLayoutParams(new GridView.LayoutParams(250, 250));
		return view;
	}

}
