package com.king.mytennis.net;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.widget.Toast;

import com.king.mytennis.view.R;

public class UploadHelper {

	private Context context;
	private ProgressDialog uploadDialog;
	private Handler uploadHandler = new Handler(new UploadHandler());
	
	public UploadHelper(Context context) {
		this.context = context;
	}
	//FIXME this is old version to upload file record, need change to db file
	@Deprecated
	public void uploadRecord() {
		uploadDialog = new ProgressDialog(context);
		uploadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		uploadDialog.setMessage(context.getResources().getString(R.string.uploading));
		uploadDialog.show();
    	new Thread(){
    		public void run(){
    			//String result = new NetInstance().uploadFile(Configuration.getInstance().DEF_FILE);
    			Message msg = new Message();
    			//msg.obj = result;
    			uploadHandler.sendMessage(msg);
    		}
    	}.start();
	}

	private class UploadHandler implements Callback, DialogInterface.OnClickListener {

		@Override
		public boolean handleMessage(Message msg) {
			uploadDialog.cancel();
			String result = (String) msg.obj;
			if (result.equals(Protocal.UPLOAD_CONFIRM)) {
				new AlertDialog.Builder(context)
					.setTitle(R.string.warning)
					.setMessage(context.getResources().getString(R.string.upload_confirm))
					.setPositiveButton(R.string.ok, this)
					.setNegativeButton(R.string.cancel, this)
					.show();
			}
			else if (result.equals(Protocal.RESULT_OK)) {
				Toast.makeText(context, R.string.upload_ok, Toast.LENGTH_LONG).show();
			}
			else if (result.equals(Protocal.RESULT_FAIL)) {
				Toast.makeText(context, R.string.upload_fail, Toast.LENGTH_LONG).show();
			}
			else {
				new AlertDialog.Builder(context)
					.setMessage(result).show();
			}
			return true;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == DialogInterface.BUTTON_POSITIVE) {
				new ConfirmThread(true).start();
			}
			else if (which == DialogInterface.BUTTON_NEGATIVE) {
				new ConfirmThread(false).start();
			}
		}
		
	}

	private class ConfirmThread extends Thread implements Callback {

		private boolean confirm;
		private Handler handler;
		public ConfirmThread(boolean c) {
			confirm = c;
			handler = new Handler(this);
		}
		public void run() {
			String result = new NetInstance().comfirmUpload(confirm);
			Message msg = new Message();
			msg.obj = result;
			handler.sendMessage(msg);
		}
		@Override
		public boolean handleMessage(Message msg) {

			String result = (String) msg.obj;
			if (result.equals(Protocal.RESULT_OK)) {
				Toast.makeText(context, R.string.upload_ok, Toast.LENGTH_LONG).show();
			}
			else if (result.equals(Protocal.RESULT_FAIL)) {
				Toast.makeText(context, R.string.upload_fail, Toast.LENGTH_LONG).show();
			}
			else if (result.equals(Protocal.UPLOAD_OVERTIME)) {
				Toast.makeText(context, R.string.upload_overtime, Toast.LENGTH_LONG).show();
			}
			else {
				new AlertDialog.Builder(context)
					.setMessage(result).show();
			}
			return true;
		}
		
	}
}
