package com.king.mytennis.view;

import com.king.mytennis.model.Configuration;
import com.king.mytennis.service.MenuService;
import com.king.mytennis.view_v_7_0.controller.BasicOperation.DialogCallback;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SaveAsDialog extends AlertDialog.Builder 
		implements DialogInterface.OnClickListener {

	private Context context;
	
	private EditText et_rename;
	private TextView tv_dir;
	private DialogCallback mCallback;
	
	public SaveAsDialog(Context context, DialogCallback callback) {
		super(context);
		this.context = context;
		mCallback = callback;
		init();
	}

	private void init() {

		View view = LayoutInflater.from(context).inflate(R.layout.dialog_saveasorset, null);
		et_rename = (EditText) view.findViewById(R.id.edit_saveas_path);
		tv_dir = (TextView) view.findViewById(R.id.tv_saveas_dir);

		setTitle(R.string.saveas_title);
		setView(view);
		setPositiveButton(R.string.ok, this);
		setNegativeButton(R.string.cancel, this);
		
		tv_dir.setText(context.getResources().getString(R.string.default_directory)
				+ Configuration.HISTORY_BASE);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		if (which == DialogInterface.BUTTON_POSITIVE) {
			MenuService menuService = new MenuService();
			String folder = menuService.saveDatabasesToFolder(et_rename.getText().toString());
			if(folder.equals(MenuService.HISTORY_FOLDER_ALREADY_EXIST)){
				if (mCallback != null) {
					mCallback.onCancel(folder);
				}
				Toast.makeText(context, R.string.save_folder_already_exist, Toast.LENGTH_LONG).show();
			}
			else {
				if (mCallback != null) {
					mCallback.onOk(null);
				}
				String message = getContext().getResources().getString(R.string.save_db_success);
				message = message.replace("%s", folder);
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			}
		}
		else if (which == DialogInterface.BUTTON_NEGATIVE) {
			
		}
	}

}
