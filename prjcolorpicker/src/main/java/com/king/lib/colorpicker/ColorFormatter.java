package com.king.lib.colorpicker;
/**
 * @author JingYang
 * @version create time：2016-1-15 下午1:31:42
 *
 */
public class ColorFormatter {

	public static String formatColor(int color) {
		String hexColor = "" + Integer.toHexString(color);
		//全不透明的可以去掉ff
		if (hexColor.length() == 8 && hexColor.startsWith("ff")) {
			hexColor = hexColor.substring(2);
		}
		//如果是类似00002233，前面的0全部会被截断
		if (color > 0 && hexColor.length() < 8) {
			StringBuffer buffer = new StringBuffer(hexColor);
			for (int i = 0; i < 8 - hexColor.length(); i ++) {
				buffer.insert(0, "0");
			}
			hexColor = buffer.toString();
		}
		return hexColor;
	}
}
