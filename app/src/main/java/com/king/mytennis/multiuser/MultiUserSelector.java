package com.king.mytennis.multiuser;

import com.king.mytennis.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MultiUserSelector implements OnItemClickListener {

	private Context mContext;
	private PopupWindow popupWindow;
	private MultiUserManager multiUserManager;
	private LinearLayout userLayout;
	private OnUserSelectListener onUserSelectListener;

	public interface OnUserSelectListener {
		public void onSelect(MultiUser user);
	}

	public MultiUserSelector(Context context, OnUserSelectListener listener) {
		mContext = context;
		onUserSelectListener = listener;
		multiUserManager = MultiUserManager.getInstance();
	}

	public void showSelector(View anchor) {

		if (popupWindow == null) {
			createUserSelectPopup();
		}
		popupWindow.showAsDropDown(anchor);
	}

	private void createUserSelectPopup() {
		ListView listView = new ListView(mContext);
		listView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		listView.setDivider(null);
		listView.setSelector(R.drawable.shape_slidingmenu_bk);
		listView.setAdapter(new SelectorAdapter());
		listView.setOnItemClickListener(this);
		popupWindow = new PopupWindow(listView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setWidth(mContext.getResources().getDimensionPixelSize(R.dimen.userselector_popup_width));
		//必须设置setBackgroundDrawable才能使点击外面区域便关闭
		popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_slidingmenuitem_bk_pressed));
		//获取popwindow焦点
		popupWindow.setFocusable(true);
		//设置popwindow如果点击外面区域，便关闭。
		popupWindow.setOutsideTouchable(true);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		MultiUser user = multiUserManager.getUsers()[position];
		multiUserManager.setCurrentUser(user);
		multiUserManager.saveToPreference(mContext, user);
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
		updateUserGuide(userLayout, user);
		if (onUserSelectListener != null) {
			onUserSelectListener.onSelect(user);
		}
	}

	public void updateUserGuide(LinearLayout layout, MultiUser user) {
		((ImageView) layout.getChildAt(0)).setImageResource(multiUserManager.getUserSelectorResId(mContext, user));
		((TextView) layout.getChildAt(1)).setText(user.getId());
	}

	private class SelectorAdapter extends BaseAdapter {

		public SelectorAdapter() {

		}

		@Override
		public int getCount() {
			return multiUserManager.getUsers().length;
		}

		@Override
		public Object getItem(int position) {
			return multiUserManager.getUsers()[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.multiuser_selector_adapter, null);
			MultiUser user = multiUserManager.getUsers()[position];
			((ImageView) convertView.findViewById(R.id.multiuser_selector_item_image)).setImageResource(multiUserManager.getUserSelectorResId(mContext, user));
			((TextView) convertView.findViewById(R.id.multiuser_selector_item_name)).setText(user.getId());
			return convertView;
		}

	}

	public void setUserLayout(LinearLayout layout) {
		this.userLayout = layout;
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSelector(userLayout);
			}
		});
	}
}
