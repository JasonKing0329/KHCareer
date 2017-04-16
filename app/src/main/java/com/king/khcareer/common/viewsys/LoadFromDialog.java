package com.king.khcareer.common.viewsys;

import java.io.File;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.king.khcareer.base.CustomDialog;
import com.king.mytennis.view.R;

public class LoadFromDialog extends CustomDialog implements OnItemClickListener {

	private ListView listView;
	private ArrayAdapter<String> adapter;
	private String[] historyFiles;
	private View lastChosedItem;
	private String chosedItem;
	private String basePath;

	public LoadFromDialog(Context context,
			OnCustomDialogActionListener actionListener) {
		super(context, actionListener);
		
		setTitle(R.string.menu_load);
		
	}

	@Override
	public void onClick(View view) {
		if (view == saveButton) {
			if (chosedItem != null) {
				final File file = new File(basePath + chosedItem);
				if (file.exists()) {
					new AlertDialog.Builder(context)
						.setTitle(R.string.warning)
						.setMessage(R.string.load_from_warning_msg)
						.setPositiveButton(R.string.ok, new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								actionListener.onSave(file);
								dismiss();
							}
						})
						.setNegativeButton(R.string.cancel, null)
						.show();
				}
			}
			if (listView == null) {
				super.onClick(view);
			}
		}
		else {
			super.onClick(view);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (lastChosedItem != null) {
			lastChosedItem.setBackground(null);
		}
		view.setBackgroundColor(getContext().getResources().getColor(R.color.title_court_clay));
		lastChosedItem = view;
		chosedItem = historyFiles[position];
	}

	@Override
	protected View getCustomView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		actionListener.onLoadData(map);
		
		historyFiles = (String[]) map.get("data");
		basePath = (String) map.get("basePath");
		if (historyFiles == null || historyFiles.length == 0) {
			
			return null;
		}
		listView = new ListView(context);
		adapter = new ArrayAdapter<String>(context
				, android.R.layout.simple_dropdown_item_1line, historyFiles);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		return listView;
	}

	@Override
	protected View getCustomToolbar() {
		// TODO Auto-generated method stub
		return null;
	}

}
