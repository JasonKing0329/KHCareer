package com.king.mytennis.model;

import java.util.Comparator;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 1.自制方法对汉字的处理仅仅是获取每个汉字的首字母
 * 如（“德约科维奇” 与 “德尔波特罗” 比较，其实比的是"dykwq" 和 "debtl"）
 * 2.另一种方法采用pinyin4j开源组件
 * @author king
 *
 */
public class StringMixSort implements Comparator<String> {

	/**
	 * 自制方法
	 // 国标码和区位码转换常量
	 //存放国标一级汉字不同读音的起始区位码
	 private final int GB_SP_DIFF = 0xa0;
	 private final int[] LETTER_GB = {
	 1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787,
	 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086,
	 4390, 4558, 4684, 4925, 5249, 5600};
	 //存放国标一级汉字不同读音的起始区位码对应读音
	 private final char[] LETTER_PINYIN = {
	 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j',
	 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
	 't', 'w', 'x', 'y', 'z'};

	 //获取一个字符串的拼音码
	 private String getLowerWord(String name) {
	 String nn = name.toLowerCase(Locale.CHINESE);
	 String nameToLower = name.toLowerCase();
	 StringBuffer buffer = new StringBuffer();
	 char ch;
	 char[] temp;
	 for (int i = 0,n=nameToLower.length(); i < n; i++) { //依次处理str中每个字符
	 ch = nameToLower.charAt(i);
	 temp = new char[] {ch};
	 byte[] uniCode;
	 try {
	 //由于后面是按照国标码进行转换，因此这里一定要是gb2312,不是空或者utf-8
	 uniCode = new String(temp).getBytes("gb2312");
	 if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
	 buffer.append(temp);
	 }
	 else {
	 buffer.append(convert(uniCode));
	 }
	 } catch (UnsupportedEncodingException e) {
	 e.printStackTrace();
	 }
	 }
	 return buffer.toString();
	 }

	 /** 获取一个汉字的拼音首字母。
	 * GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
	 * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
	 * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
	 *
	 private char convert(byte[] bytes) {
	 char result = '-';
	 int secPosValue = 0;
	 int i;
	 for (i = 0; i < bytes.length; i++) {
	 bytes[i] -= GB_SP_DIFF;
	 }
	 secPosValue = bytes[0] * 100 + bytes[1];
	 for (i = 0; i < 23; i++) {
	 if (secPosValue >= LETTER_GB[i] && secPosValue < LETTER_GB[i + 1]) {
	 result = LETTER_PINYIN[i];
	 break;
	 }
	 }
	 return result;
	 }

	 //* 由于只是去汉字每个字的首字母进行比较因此可能出现本来不同的但是被判断成相同的
	 //* 因此还要结合equal作细致判断
	 @Override
	 public int compare(String str1, String str2) {

	 boolean isEqual = str1.equals(str2);
	 try {
	 byte[] unicode1 = str1.getBytes("gb2312");
	 byte[] unicode2 = str2.getBytes("gb2312");
	 if (unicode1[0] > 128 || unicode1[0] < 0) { // 汉字
	 str1 = getLowerWord(str1);
	 }
	 else if (unicode1[0] > 64 && unicode1[0] < 91) {//大写字母
	 str1 = str1.toLowerCase();
	 }
	 if (unicode2[0] > 128 || unicode2[0] < 0) { // 汉字
	 str2 = getLowerWord(str2);
	 }
	 else if (unicode2[0] > 64 && unicode2[0] < 91) {//大写字母
	 str2 = str2.toLowerCase();
	 }
	 } catch (UnsupportedEncodingException e) {
	 e.printStackTrace();
	 }
	 int result = str1.compareTo(str2);
	 if (result == 0 && !isEqual) {
	 result = 1;
	 }
	 return result;
	 }
	 */


	/**
	 * 以下全部是用pinyin4j开源组件的方法，上面的代码无关
	 */
	@Override
	public int compare(String object1, String object2) {

		return getPinyin(object1).compareTo(getPinyin(object2));
	}

	private String getPinyin(String input){
		String result = "";
		for (int i = 0; i < input.length(); i++) {
			HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
			defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
			char c = input.charAt(i);
			String[] pinyinArray = null;
			try {
				pinyinArray = PinyinHelper.toHanyuPinyinStringArray(
						c, defaultFormat);
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
			if(pinyinArray!=null)
				result += pinyinArray[0];
			else
			if(c!=' ')
				result += input.charAt(i);
		}
		return result.trim().toLowerCase();
	}
}
