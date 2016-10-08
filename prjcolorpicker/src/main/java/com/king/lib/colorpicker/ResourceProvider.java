package com.king.lib.colorpicker;
/**
 * @author JingYang
 * @version create time：2016-1-14 下午5:16:45
 *
 */
public interface ResourceProvider {

	public static final int FLAG_DEFAULT = -1;

	public int getColor(String resName);
	public void updateColor(String resName, int color);
	public void saveColorUpdate();
}
