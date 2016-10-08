package com.king.lib.resmanager.action;

import android.content.Context;

import com.king.lib.resmanager.exception.JResNotFoundException;
import com.king.lib.resmanager.exception.JResParseException;

/**
 * @author JingYang
 * @version create time：2016-1-12 下午3:32:18
 *
 */
public interface JXMLAction {

	/**
	 * callback of saveUpdate
	 * @param callback
	 */
	@Deprecated
	public void registCallback(JResManagerCallback callback);
	/**
	 * load XML file
	 * @param filePath XML file on disk
	 * @throws JResParseException
	 */
	public void loadFile(String filePath) throws JResParseException;
	/**
	 * load XML string
	 * @param xmlString
	 * @throws JResParseException
	 */
	public void loadString(String xmlString) throws JResParseException;
	/**
	 * get resource value from filePath imported by loadFile
	 * @param context
	 * @param resName
	 * @return
	 * @throws JResNotFoundException
	 * @throws JResParseException
	 */
	public int getResource(Context context, String resName) throws JResNotFoundException, JResParseException;
	/**
	 * update resource value of Document instance in memory
	 * @param resName
	 * @param value
	 */
	public void updateResource(String resName, String value);
	/**
	 * update resource value of filePath imported by loadFile
	 */
	public boolean saveUpdate();
	/**
	 * remove resource value
	 * @param resName
	 */
	public void removeItem(String resName);
}
