package com.king.khcareer.common.viewsys;

import java.io.File;
import java.util.HashMap;

import com.king.khcareer.base.CustomDialog;
import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.pubview.slidingmenu.SlidingMenuLeft;
import com.king.khcareer.pubview.slidingmenu.SlidingMenuRight;
import com.king.mytennis.view.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler.Callback;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListPopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ChooseBkDialog extends CustomDialog implements OnItemClickListener, OnItemLongClickListener, Callback{

	public static final int BK_KIND_MAINVIEW = 0;
	public static final int BK_KIND_MENU = 1;
	public static final String BK_KIND_KEY = "bk_kind";

	private WallpaperAdapter adapter;
	private GridView gridView;
	private ProgressBar progressBar;
	private Handler handler;
	private ListPopupWindow setAsMenuBkPopup;
	
	private File[] files;
	
	public ChooseBkDialog(Context context, OnCustomDialogActionListener actionListener) {
		super(context, actionListener);
		setTitle(context.getResources().getString(R.string.dlg_choosebk));
		initBackground();
	}
	
	private void initBackground() {
		handler = new Handler(this);
		new Thread() {
			public void run() {
				files = new File(Configuration.IMG_BK_BASE).listFiles();
				handler.sendMessage(new Message());
			}
		}.start();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
		String newDEF_BK = files[position].getPath();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(BK_KIND_KEY, BK_KIND_MAINVIEW);
		map.put("path", newDEF_BK);
		actionListener.onSave(map);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		final String imagePath = files[position].getPath();
		setAsMenuBkPopup = new ListPopupWindow(getContext());
		setAsMenuBkPopup.setAnchorView(view);
		setAsMenuBkPopup.setWidth(600);
		setAsMenuBkPopup.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.shape_slidingmenu_bk));
		String[] menuItems = getContext().getResources().getStringArray(R.array.setting_slidingmenu_mode);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext()
				, android.R.layout.simple_dropdown_item_1line, menuItems);
		setAsMenuBkPopup.setAdapter(adapter);
		setAsMenuBkPopup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == 0) {
					SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString(SlidingMenuLeft.BK_KEY, imagePath);
					editor.commit();

					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put(BK_KIND_KEY, BK_KIND_MENU);
					map.put("path", imagePath);
					actionListener.onSave(map);
					Toast.makeText(getContext(), R.string.save_ok, Toast.LENGTH_LONG).show();
				}
				else if (position == 1) {
					SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString(SlidingMenuRight.BK_KEY, imagePath);
					editor.commit();

					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put(BK_KIND_KEY, BK_KIND_MENU);
					map.put("path", imagePath);
					actionListener.onSave(map);

					Toast.makeText(getContext(), R.string.save_ok, Toast.LENGTH_LONG).show();
				}
				setAsMenuBkPopup.dismiss();
			}
		});
		setAsMenuBkPopup.show();
		setAsMenuBkPopup.getListView().setDivider(null);
		return true;
	}

	@Override
	protected View getCustomView() {
		View view=LayoutInflater.from(context).inflate(R.layout.dialog_changebk, null);
		gridView = (GridView) view.findViewById(R.id.changebk_grid);
		progressBar = (ProgressBar) view.findViewById(R.id.changebk_progress);
		gridView.setVisibility(View.GONE);
		gridView.setOnItemClickListener(this);
		gridView.setOnItemLongClickListener(this);
		return view;
	}

	@Override
	protected View getCustomToolbar() {
		hideSaveButton();
		return null;
	}

	@Override
	public boolean handleMessage(Message msg) {
		progressBar.setVisibility(View.GONE);
		adapter = new WallpaperAdapter(context, files);
		gridView.setAdapter(adapter);
		gridView.setVisibility(View.VISIBLE);
		return true;
	}

}
