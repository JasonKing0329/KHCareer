package com.king.lib.colorpicker;

import android.content.res.Resources;

/**
 * @author JingYang
 * @version create time：2016-1-14 下午5:44:48
 *
 */
public class ResourceController {

	public static int getColor(Resources res, String resName, int color) {
		int resultColor = color;
		if (color == ResourceProvider.FLAG_DEFAULT) {
			if (resName.equals(ColorPickerRes.COLORPICKER_BACKGROUND)) {
				resultColor = res.getColor(R.color.colorpicker_background);
			}
			else if (resName.equals(ColorPickerRes.COLORPICKER_COLOR_TEXT)) {
				resultColor = res.getColor(R.color.colorpicker_color_text);
			}
			else if (resName.equals(ColorPickerRes.COLORPICKER_TITLE)) {
				resultColor = res.getColor(R.color.colorpicker_title);
			}
			else if (resName.equals(ColorPickerRes.COLORPICKER_DIVIDER)) {
				resultColor = res.getColor(R.color.colorpicker_divider);
			}
			else if (resName.equals(ColorPickerRes.COLORPICKER_FRAME_BORDER)) {
				resultColor = res.getColor(R.color.colorpicker_frame_border);
			}
			else if (resName.equals(ColorPickerRes.COLORPICKER_LIST_TEXT)) {
				resultColor = res.getColor(R.color.colorpicker_list_text);
			}
		}
		return resultColor;
	}
}
