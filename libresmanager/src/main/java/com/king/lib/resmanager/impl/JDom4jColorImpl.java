package com.king.lib.resmanager.impl;

import android.content.Context;
import android.graphics.Color;

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
public class JDom4jColorImpl extends JDom4jAbstract {

	private Document mDocument;
	private HashMap<String, Element> map;
	private String filePath;

	public JDom4jColorImpl() {
		map = new HashMap<String, Element>();
	}

	@Override
	public void loadFile(String filePath) throws JResParseException {
		try {
			mDocument = load(filePath);
			this.filePath = filePath;

			Element element = mDocument.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> list = element.elements("color");
			for (Element color : list) {
				String name = color.attribute("name").getValue();
				map.put(name, color);
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

		Element value = map.get(resName);
		if (value == null) {
			LogUtil.logD("resource not found: " + resName);
			throw new JResNotFoundException();
		} else {
			try {
				res = Color.parseColor(value.getText());
			} catch (Exception e) {
				LogUtil.logE("resource parse error: " + resName + " value:" + value.getText());
				e.printStackTrace();
				throw new JResParseException();
			}
		}
		return res;
	}

	@Override
	public void updateResource(String resName, String value) {
		Element element = map.get(resName);
		if (element == null) {
			LogUtil.logD("resource not found: " + resName);
			LogUtil.logD("add element: " + resName);
			element = mDocument.getRootElement().addElement("color");
			element.addAttribute("name", resName);
			element.setText(value);
			//必须同时加入到map，否则下次getColor没有值
			map.put(resName, element);
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
			mDocument.getRootElement().remove(element);
			//从map中必须删除，否则下次getColor还是能从map中找出来
			map.remove(resName);
		}
	}

}
