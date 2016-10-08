package com.king.lib.resmanager;

import android.content.Context;

import com.king.lib.resmanager.action.JResManagerCallback;
import com.king.lib.resmanager.action.JXMLAction;
import com.king.lib.resmanager.exception.JResNotFoundException;
import com.king.lib.resmanager.exception.JResParseException;
import com.king.lib.resmanager.impl.JXMLActionFactory;

/**
 * @author JingYang
 * @version create time：2016-1-12 下午3:23:33
 *
 * This library is designed for using resource file on disk file system.
 * Currently, only color and dimen resource are supported.
 * You should use this library with follow by:
 * (using color resource)
 * 1.call JResManager.getInstance().parseColorFile with your color resource .xml
 *   the content style should same with android colors.xml
 * 2.call registCallback if you need apply update operation
 * 3.call getColor with resource name
 * 4.call updateColor with resource name and new color
 * 5.call saveColorUpdate to save modifications into file on disk
 * dimen resource is same with above
 */
public class JResManager {

	private static JResManager manager;
	private JXMLAction xmlColorImpl;
	private JXMLAction xmlDimenImpl;

	private JResManagerCallback mCallback;

	private JResManager() {
		xmlColorImpl = JXMLActionFactory.createColorAction();
		xmlDimenImpl = JXMLActionFactory.createDimenAction();
	}

	/**
	 * not safe, instance may be always existed in system memory if application is not totally killed
	 * better to use createInstance
	 * @return
	 */
	@Deprecated
	public static JResManager getInstance() {
		if (manager == null) {
			manager = new JResManager();
		}
		return manager;
	}

	public static JResManager createInstance() {
		manager = new JResManager();
		return manager;
	}

	/**
	 * register callback for async event
	 * @param callback
	 */
	@Deprecated /** v1.1 deprecated, all operation should not be synchronized **/
	public void registCallback(JResManagerCallback callback) {
		mCallback = callback;
		xmlColorImpl.registCallback(mCallback);
		xmlDimenImpl.registCallback(mCallback);
	}

	/**
	 * color resource xml file on File system
	 * @param filePath
	 * @throws JResParseException
	 */
	public void parseColorFile(String filePath) throws JResParseException {
		xmlColorImpl.loadFile(filePath);
	}

	/**
	 * dimen resource xml file on File system
	 * @param filePath
	 * @throws JResParseException
	 */
	public void parseDimenFile(String filePath) throws JResParseException {
		xmlDimenImpl.loadFile(filePath);
	}

	public void removeColorResource(String resName) {
		xmlColorImpl.removeItem(resName);
	}

	public void removeDimenResource(String resName) {
		xmlDimenImpl.removeItem(resName);
	}

	/**
	 * get color from filePath imported by parseColorFile
	 * eg.<color name="resName">value</color>
	 * @param context
	 * @param resName
	 * @return
	 * @throws JResNotFoundException
	 * @throws JResParseException
	 */
	public int getColor(Context context, String resName) throws JResNotFoundException, JResParseException {
		return xmlColorImpl.getResource(context, resName);
	}

	/**
	 * get dimen from filePath imported by parseDimenFile
	 * eg.<dimen name="resName">value</dimen>
	 * @param context
	 * @param resName
	 * @return
	 * @throws JResNotFoundException
	 * @throws JResParseException
	 */
	public double getDimen(Context context, String resName) throws JResNotFoundException, JResParseException {
		return xmlDimenImpl.getResource(context, resName);
	}

	/**
	 * modify color resource value
	 * new value only saved in Document instance of memory
	 * eg.<color name="resName">value</color>
	 * @param resName
	 * @param value format "#00ff33" or "#ee00ff33"
	 */
	public void updateColor(String resName, String value) {
		xmlColorImpl.updateResource(resName, value);
	}

	/**
	 * modify dimen resource value
	 * new value only saved in Document instance of memory
	 * eg.<dimen name="resName">value</dimen>
	 * @param resName
	 * @param value such as "10dp", only support "dp/sp/px"
	 */
	public void updateDimen(String resName, String value) {
		xmlDimenImpl.updateResource(resName, value);
	}

	/**
	 * save Document instance in memory as filePath imported by parseColorFile
	 */
	public boolean saveColorUpdate() {
		return xmlColorImpl.saveUpdate();
	}

	/**
	 * save Document instance in memory as filePath imported by parseDimenFile
	 */
	public boolean saveDimenUpdate() {
		return xmlDimenImpl.saveUpdate();
	}
}
