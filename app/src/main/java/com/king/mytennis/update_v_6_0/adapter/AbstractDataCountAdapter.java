package com.king.mytennis.update_v_6_0.adapter;

public abstract class AbstractDataCountAdapter {

	public abstract int getCount();
	public abstract String getTitle(int index);
	public abstract String getContent(int index);
	public abstract void onYearChanged(String year);
	public abstract String previousYear();
	public abstract String nextYear();
	public abstract void setCurrentYear(String year);
}
