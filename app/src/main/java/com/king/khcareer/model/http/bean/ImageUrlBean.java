package com.king.khcareer.model.http.bean;

import java.util.List;

public class ImageUrlBean {

	private String key;
	private List<ImageItemBean> itemList;

	public List<ImageItemBean> getItemList() {
		return itemList;
	}

	public void setItemList(List<ImageItemBean> itemList) {
		this.itemList = itemList;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
