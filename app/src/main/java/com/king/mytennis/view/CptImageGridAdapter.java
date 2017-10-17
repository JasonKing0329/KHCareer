package com.king.mytennis.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.ImageUtil;

import java.util.HashMap;
import java.util.Map;

public class CptImageGridAdapter  extends BaseAdapter {

	private String[] cptArray;
	private Context context;

	/**
	 * 保存首次从文件夹加载的图片序号
	 */
	private Map<String, Integer> imageIndexMap;

	public CptImageGridAdapter(Context context, String[] array) {
		this.context = context;
		cptArray = array;
		imageIndexMap = new HashMap<>();
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

		String filePath;
		if (imageIndexMap.get(cptArray[position]) == null) {
			filePath = ImageFactory.getPlayerHeadPath(cptArray[position], imageIndexMap);
		}
		else {
			filePath = ImageFactory.getPlayerHeadPath(cptArray[position], imageIndexMap.get(cptArray[position]));
		}
		ImageUtil.load("file://" + filePath, holder.image, R.drawable.icon_list);
		return convertView;
	}

    public void onItemClickRefresh(int position) {
        String name = cptArray[position];
        ImageFactory.getPlayerHeadPath(name, imageIndexMap);
        notifyDataSetChanged();
    }

	private class ViewHolder {
		public ImageView image;
		public TextView name;
	}
}
