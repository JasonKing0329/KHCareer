package com.king.lib.colorpicker;
/**
 * @author JingYang
 * @version create time：2016-1-13 下午2:03:33
 *
 * View page should register this data to ColorPicker
 * key is better to keep same with name attribute value of color in application colors.xml
 * name is the text to show in ColorPicker's list part, ColorPicker will know witch part to edit
 */
public class ColorPickerSelectionData {

	private String key;
	private String name;
	private int color;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
}
