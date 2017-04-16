package com.king.khcareer.record.detail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DetailViewAdapter extends BaseAdapter {

	private ViewFactory viewFactory;
	
	public DetailViewAdapter(Context context, ViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}
	
	@Override
	public int getCount() {

		return viewFactory.getViewNumber();
	}

	@Override
	public Object getItem(int position) {
		
		return viewFactory.getViewId(position);
	}

	@Override
	public long getItemId(int position) {

		return (Long) viewFactory.getViewId(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		return viewFactory.getView(position, convertView);
	}

}
