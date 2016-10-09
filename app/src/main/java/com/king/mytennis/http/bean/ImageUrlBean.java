package com.king.mytennis.http.bean;

import java.util.List;

public class ImageUrlBean {

	private String key;
	private List<String> urlList;
	private List<Long> sizeList;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<String> getUrlList() {
		return urlList;
	}
	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}
	public List<Long> getSizeList() {
		return sizeList;
	}
	public void setSizeList(List<Long> sizeList) {
		this.sizeList = sizeList;
	}
}
