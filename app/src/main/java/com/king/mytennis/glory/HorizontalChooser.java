package com.king.mytennis.glory;

import java.util.List;

import com.king.mytennis.view.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class HorizontalChooser implements Chooser {

	private ChooserView chooser;
	private HorizontalChooserAdapter adapter;
	private Context context;
	private List<String> gloryList;
	
	public HorizontalChooser (Context context, List<String> gloryList) {
		this.context = context;
		Activity view = (Activity) context;
		this.gloryList = gloryList;
		chooser = (ChooserView) view.findViewById(R.id.glory_chooser);
		adapter = new HorizontalChooserAdapter(context, gloryList);
		chooser.setAdapter(adapter);
	}

	@Override
	public void reInit() {
		Activity view = (Activity) context;
		chooser = (ChooserView) view.findViewById(R.id.glory_chooser);
		notifyAdapterRefresh();
	}

	@Override
	public void notifyAdapterRefresh() {
		if (gloryList != null) {
			//Log.i(TAG, "image number = " + imageList.size());
			if (adapter == null) {
				adapter = new HorizontalChooserAdapter(context, gloryList);
			}
			else {
				adapter.updateList(gloryList);
			}
			chooser.setAdapter(adapter);
		}
	}

	@Override
	public void setVisibility(int visibility) {
		chooser.setVisibility(visibility);
	}

	@Override
	public void updateList(List<String> fileList) {
		this.gloryList = fileList;
	}

	@Override
	public View getChildAt(int index) {
		return chooser.getChildAt(index);
	}

	@Override
	public void setOnChooseListener(Object listener) {
		chooser.setOnChooseListener((SPictureChooseListener) listener);
		
	}
}
