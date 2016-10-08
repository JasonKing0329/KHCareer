package com.king.lib.resmanager.impl;

import com.king.lib.resmanager.action.JXMLAction;

/**
 * @author JingYang
 * @version create time：2016-1-12 下午3:34:53
 *
 */
public class JXMLActionFactory {

	public static JXMLAction createColorAction() {
		return new JDom4jColorImpl();
	}
	public static JXMLAction createDimenAction() {
		return new JDom4jDimenImpl();
	}
}
