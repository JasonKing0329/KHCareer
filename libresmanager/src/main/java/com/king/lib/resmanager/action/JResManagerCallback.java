package com.king.lib.resmanager.action;
/**
 * @author JingYang
 * @version create time：2016-1-13 上午9:31:37
 *
 */
@Deprecated
public interface JResManagerCallback {

	/**
	 * callback of saveColorUpdate or saveDimenUpdate
	 * save success
	 */
	public void onUpdateSuccess();
	/**
	 * callback of saveColorUpdate or saveDimenUpdate
	 * save failed
	 */
	public void onUpdateFailed();
}
