package com.king.lib.resmanager.impl;

import android.content.SharedPreferences;

import com.king.lib.resmanager.LogUtil;
import com.king.lib.resmanager.action.JPrefAction;
import com.king.lib.resmanager.exception.JResParseException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;

/**
 * Created by JingYang on 2016/7/20 0020.
 * Description:
 */
public class JDom4jPrefImpl implements JPrefAction {

    private Document mDocument;

    /**
     * load 载入一个xml文档
     *
     * @return 成功返回Document对象，失败返回null
     *            文件路径
     * @throws DocumentException
     */
    protected Document load(String filename) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        return saxReader.read(new File(filename));
    }

    @Override
    public void loadExtendPreference(String filePath, SharedPreferences preferences) throws JResParseException {
        try {
            mDocument = load(filePath);

            Element element = mDocument.getRootElement();
            List<Element> list = element.elements();
            SharedPreferences.Editor editor = preferences.edit();
            for (Element item : list) {
                String type = item.getName();
                String key = item.attributeValue("name");
                if (type.equals("string")) {
                    String value = item.getTextTrim();
                    editor.putString(key, value);
                }
                else if (type.equals("boolean")) {
                    boolean value = Boolean.parseBoolean(item.attributeValue("value"));
                    editor.putBoolean(key, value);
                }
                else if (type.equals("int")) {
                    int value = Integer.parseInt(item.getTextTrim());
                    editor.putInt(key, value);
                }
                else if (type.equals("long")) {
                    long value = Long.parseLong(item.getTextTrim());
                    editor.putLong(key, value);
                }
                else if (type.equals("float")) {
                    float value = Float.parseFloat(item.getTextTrim());
                    editor.putFloat(key, value);
                }
            }
            editor.commit();
        } catch (DocumentException e) {
            LogUtil.logE("DocumentException occured while load file " + filePath);
            e.printStackTrace();
            throw new JResParseException();
        }
    }
}
