package com.king.mytennis.view;

import java.io.File;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.king.mytennis.service.ImageUtil;

public class WallpaperAdapter extends BaseAdapter {

	private Context context;
	private File[] files;
	public WallpaperAdapter(Context context, File[] files) {
		this.context = context;
		this.files = files;
		ImageUtil.initImageLoader(context);
	}
	@Override
	public int getCount() {

		return files == null ? 0:files.length;//这个可不能不写，如果是默认的0那直接就没有任何东西显示了
	}

	@Override
	public Object getItem(int position) {

		return files == null ? null:files[position];
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView view = new ImageView(context);
		view.setScaleType(ImageView.ScaleType.FIT_CENTER);
		view.setLayoutParams(new GridView.LayoutParams(250, 250));
		ImageUtil.load("file://" + files[position].getPath(), view);
		return view;
	}

}
