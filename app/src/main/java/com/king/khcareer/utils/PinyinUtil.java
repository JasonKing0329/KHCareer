package com.king.khcareer.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by Administrator on 2016/10/8.
 */
public class PinyinUtil {
    public static String getPinyin(String input) {
        if (input == null) {
            return "";
        }
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (i > 0) {
                // 已经是字母的不需要再插入空格
                if (c < 'a' || c > 'Z') {
                    buffer.append(" ");// 字与字直接用空格隔开
                }
            }
            String[] pinyinArray = null;
            try {
                pinyinArray = PinyinHelper.toHanyuPinyinStringArray(
                        c, defaultFormat);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }

            if (pinyinArray == null) {
                buffer.append(c);
            }
            else {
                buffer.append(pinyinArray[0]);
            }
        }
        String pinyin = buffer.toString().toLowerCase();
        DebugLog.e(input + " -> " + pinyin);
        return pinyin;
    }
}
