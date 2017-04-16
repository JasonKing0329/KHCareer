package com.king.khcareer.common.viewsys;

import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.utils.ExternalRecordTool;
import com.king.mytennis.view.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SetRecordFileDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener {

	private Context context;
	private EditText et_rename;
	public SetRecordFileDialog(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private void init() {
		View view;
		LayoutInflater factory = LayoutInflater.from(context);
		view = factory.inflate(R.layout.dialog_saveasorset, null);
		et_rename = (EditText) view.findViewById(R.id.edit_saveas_path);
		et_rename.setText(Configuration.HISTORY_BASE);
		
		setTitle(R.string.menu_set);
		setMessage(R.string.update_datafrom);
		setView(view);
		setPositiveButton(R.string.ok, this);
		setNegativeButton(R.string.exit, this);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		if (which == DialogInterface.BUTTON_POSITIVE) {

			saveCurrentAsHistory();
			String target = et_rename.getText().toString();
			if (target != null && target.trim().length() > 0) {
				if(ExternalRecordTool.replaceDatabase(context, target, ExternalRecordTool.DATABASE)) {
					Toast.makeText(context, R.string.save_ok, Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(context, R.string.save_fail, Toast.LENGTH_LONG).show();
				}
			}
		}
		else if (which == DialogInterface.BUTTON_NEGATIVE) {
			
		}
	}

	private void saveCurrentAsHistory() {
		ExternalRecordTool.saveDbAsHistory();
	}

}
