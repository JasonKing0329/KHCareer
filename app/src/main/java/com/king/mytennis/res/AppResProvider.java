package com.king.mytennis.res;

import android.content.Context;

import com.king.lib.colorpicker.ResourceProvider;

/**
 * @author JingYang
 *
 */
public class AppResProvider implements ResourceProvider {

	private Context mContext;
	
	public AppResProvider(Context context) {
		mContext = context;
	}
	@Override
	public int getColor(String resName) {
		return JResource.getColor(mContext, resName, ResourceProvider.FLAG_DEFAULT);
	}

	@Override
	public void updateColor(String resName, int color) {
		JResource.updateColor(resName, color);
	}

	@Override
	public void saveColorUpdate() {
		JResource.saveColorUpdate(mContext);
	}

}
