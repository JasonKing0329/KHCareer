package com.king.khcareer.common.helper;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.utils.ExternalRecordTool;
import com.king.khcareer.base.CustomDialog;
import com.king.khcareer.common.viewsys.LoadFromDialog;
import com.king.khcareer.common.viewsys.SaveAsDialog;

import android.content.Context;

/**
 * @author JingYang
 *
 */
public class BasicOperation {

	public interface DialogCallback {
		public void onOk(Object result);
		public void onCancel(Object result);
	}
	
	/**
	 * 
	 * @param context
	 * @param callback can be null
	 */
	public static void showLoadFromDialog(final Context context, final DialogCallback callback) {

		new LoadFromDialog(context, new CustomDialog.OnCustomDialogActionListener() {
			
			@Override
			public boolean onSave(Object object) {
				if (object != null) {
					File file = (File) object;
					
					//v5.7.2 update from single database to database directory
//					ExternalRecordTool.replaceDatabase(ManagerActivity.this
//							, file.getPath(), ExternalRecordTool.DATABASE);
					ExternalRecordTool.replaceAllDatabase(context
							, file.getPath());

					if (callback != null) {
						callback.onOk(null);
					}
				}
				return true;
			}
			
			@Override
			public void onLoadData(HashMap<String, Object> data) {
				
				File file = new File(Configuration.HISTORY_BASE);
				
				//v5.7.2 update from single database to database directory
//				String[] names = file.list(new FilenameFilter() {
//					
//					@Override
//					public boolean accept(File dir, String filename) {
//
//						return filename.endsWith(".db");
//					}
//				});
				File[] files = file.listFiles(new FileFilter() {
					
					@Override
					public boolean accept(File file) {

						return file.isDirectory();
					}
				});
				String[] names = new String[files.length];
				for (int i = 0; i < files.length; i ++) {
					names[i] = files[i].getName();
				}
				
				data.put("data", names);
				data.put("basePath", Configuration.HISTORY_BASE);
			}
			
			@Override
			public boolean onCancel() {
				// TODO Auto-generated method stub
				return false;
			}
		}).show();
	}
	
	/**
	 * 
	 * @param mContext
	 * @param callback can be null
	 */
	public static void showSaveAsDialog(final Context mContext, final DialogCallback callback) {
		new SaveAsDialog(mContext, callback).show();
	}
}
