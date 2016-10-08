package com.king.lib.resmanager.impl;

import com.king.lib.resmanager.action.JResManagerCallback;
import com.king.lib.resmanager.action.JXMLAction;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;

/**
 * @author JingYang
 * @version create time：2016-1-12 下午4:23:55
 *
 */
public abstract class JDom4jAbstract implements JXMLAction {

//	protected JResManagerCallback mCallback;

	@Override
	public void registCallback(JResManagerCallback callback) {
//		mCallback = callback;
	}

	/**
	 * load 载入一个xml文档
	 *
	 * @return 成功返回Document对象，失败返回null
	 *            文件路径
	 * @throws DocumentException
	 */
	protected Document load(String filename) throws DocumentException {
		Document document = null;
		SAXReader saxReader = new SAXReader();
		document = saxReader.read(new File(filename));
		return document;
	}

	/**
	 * doc2String 将xml文档内容转为String
	 *
	 * @return 字符串
	 * @param document
	 */
	protected static String doc2String(Document document) {
		String s = "";
		try {
			// 使用输出流来进行转化
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			// 使用GB2312编码
			OutputFormat format = new OutputFormat("  ", true, "GB2312");
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(document);
			s = out.toString("GB2312");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return s;
	}

	/**
	 * doc2XmlFile 将Document对象保存为一个xml文件到本地
	 *
	 * @return true:保存成功 flase:失败
	 * @param filename
	 *            保存的文件名
	 * @param document
	 *            需要保存的document对象
	 */
	protected boolean doc2XmlFile(Document document, String filename) {
		boolean flag = true;
		try {
			/* 将document中的内容写入文件中 */
			// 默认为UTF-8格式，指定为"GB2312"
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GB2312");
			XMLWriter writer = new XMLWriter(
					new FileWriter(new File(filename)), format);
			writer.write(document);
			writer.close();

//			if (mCallback != null) {
//				mCallback.onUpdateSuccess();
//			}
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();

//			if (mCallback != null) {
//				mCallback.onUpdateFailed();
//			}
		}
		return flag;
	}

}
