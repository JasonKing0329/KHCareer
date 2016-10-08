package com.king.mytennis.view;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CptImageGridAdapter  extends BaseAdapter {

	private String[] cptArray;
	private HashMap<String, Bitmap> map;
	private Context context;

	public CptImageGridAdapter(Context context, String[] array, HashMap<String, Bitmap> map) {
		this.context = context;
		cptArray = array;
		this.map = map;
	}

	public void setHeadMap(HashMap<String, Bitmap> map) {
		this.map = map;
	}

	@Override
	public int getCount() {

		return cptArray.length;
	}

	@Override
	public Object getItem(int position) {

		return cptArray[position];
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_cptgrid, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.cptgrid_image);
			holder.name = (TextView) convertView.findViewById(R.id.cptgrid_name);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(cptArray[position]);
		Bitmap bitmap = map.get(cptArray[position]);
		if (bitmap == null) {
			holder.image.setBackgroundResource(R.drawable.icon_list);
		}
		else {
			holder.image.setBackgroundDrawable(new BitmapDrawable(bitmap));
		}
    	/*
        ImageView imageView;
        if (convertView == null)
        {
            // 给ImageView设置资源
            imageView = new ImageView(mContext);
            // 设置布局 图片120×120显示
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            // 设置显示比例类型
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mImageIds[position]);
        return imageView;
        */
		return convertView;
	}
	private class ViewHolder {
		public ImageView image;
		public TextView name;
	}
}
