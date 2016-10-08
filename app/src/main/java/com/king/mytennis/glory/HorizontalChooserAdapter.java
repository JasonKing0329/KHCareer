package com.king.mytennis.glory;

import java.util.List;

import com.king.mytennis.view.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class HorizontalChooserAdapter extends BaseAdapter {

	private List<String> fileList;
	private Context context;
	
	public HorizontalChooserAdapter(Context context, List<String> fileList) {
		this.context = context;
		setList(fileList);
	}
	
	public void updateList(List<String> fileList) {
		//setList(imageList);
		this.fileList = fileList;
	}
	
	private void setList(List<String> fileList) {
		if (this.fileList == null) {
			this.fileList = fileList;
		}
		else {
			this.fileList.clear();
			this.fileList = fileList;
		}
	}
	@Override
	public int getCount() {

		return fileList == null ? 0:fileList.size();
	}

	@Override
	public Object getItem(int position) {
		
		return fileList == null ? 0:fileList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView view = new ImageView(context);
		view.setLayoutParams(new LinearLayout.LayoutParams(R.dimen.spicture_chooser_item_width, R.dimen.spicture_chooser_item_width));
		//view.setPadding(15, 15, 15, 15);
		view.setScaleType(ScaleType.FIT_CENTER);
		int drawableId = 0;
		switch (position) {
		case 0:
			drawableId = R.drawable.glory_rank;
			break;
		case 1:
			drawableId = R.drawable.glory_title;
			break;
		case 2:
			drawableId = R.drawable.glory_gs;
			break;
		case 3:
			drawableId = R.drawable.glory_prize;
			break;
		case 4:
			drawableId = R.drawable.glory_runnerup;
			break;
		default:
			drawableId = R.drawable.pic_def;
			break;
		}
		view.setBackgroundResource(drawableId);
		view.setImageResource(R.drawable.spicture_chooser_border_normal);
		//Bitmap bitmap = PictureManager.getInstance().getChooserItem(fileList.get(position));
		//view.setBackground(new BitmapDrawable(context.getResources(), bitmap));
		return view;
	}

}
