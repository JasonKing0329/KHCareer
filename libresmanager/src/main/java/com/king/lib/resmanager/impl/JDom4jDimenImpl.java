package com.king.lib.resmanager.impl;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.king.lib.resmanager.LogUtil;
import com.king.lib.resmanager.exception.JResNotFoundException;
import com.king.lib.resmanager.exception.JResParseException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;

/**
 * @author JingYang
 * @version create time：2016-1-12 下午3:34:31
 *
 */
public class JDom4jDimenImpl extends JDom4jAbstract {

	private Document mDocument;
	private HashMap<String, Element> map;
	private String filePath;

	public JDom4jDimenImpl() {
		map = new HashMap<String, Element>();
	}

	@Override
	public void loadFile(String filePath) throws JResParseException {
		try {
			mDocument = load(filePath);
			this.filePath = filePath;

			Element element = mDocument.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> list = element.elements("dimen");
			for (Element dimen : list) {
				String name = dimen.attribute("name").getValue();
				map.put(name, dimen);
			}
		} catch (DocumentException e) {
			LogUtil.logE("DocumentException occured while load file " + filePath);
			e.printStackTrace();
			throw new JResParseException();
		}
	}

	@Override
	public void loadString(String xmlString) throws JResParseException {

	}

	@Override
	public int getResource(Context context, String resName) throws JResNotFoundException,
			JResParseException {
		int res = -1;

		Element element = map.get(resName);
		if (element == null) {
			LogUtil.logD("resource not found: " + resName);
			throw new JResNotFoundException();
		} else {
			String value = element.getText();
			try {
				if (value.endsWith("dp") || value.endsWith("sp")) {
					float num = Float.parseFloat(value.substring(0, value.length() - 2));
					res = dpToPx(context, num);
				}
				else if (value.endsWith("px")) {
					res = Integer.parseInt(value.substring(0, value.length() - 2));
				}
				else {
					LogUtil.logE("resource parse error: " + resName);
					throw new JResParseException();
				}
			} catch (Exception e) {
				LogUtil.logE("resource parse error: " + resName);
				e.printStackTrace();
				throw new JResParseException();
			}
		}
		return res;
	}

	public static int dpToPx(Context c, float dipValue) {
		DisplayMetrics metrics = c.getResources().getDisplayMetrics();

		float val = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);

		// Round
		int res = (int)(val + 0.5);

		// Ensure at least 1 pixel if val was > 0
		if(res == 0 && val > 0) {
			res = 1;
		}

		return res;
	}

	@Override
	public void updateResource(String resName, String value) {
		Element element = map.get(resName);
		if (element == null) {
			LogUtil.logD("resource not found: " + resName);
			LogUtil.logD("add element: " + resName);
			element = mDocument.getRootElement().addElement("dimen");
			element.addAttribute("name", resName);
			element.setText(value);
		}
		else {
			element.setText(value);
		}
	}

	@Override
	public boolean saveUpdate() {
		return doc2XmlFile(mDocument, filePath);
	}

	@Override
	public void removeItem(String resName) {
		Element element = map.get(resName);
		if (element != null) {
			map.remove(resName);
			mDocument.getRootElement().remove(element);
		}
	}

}
