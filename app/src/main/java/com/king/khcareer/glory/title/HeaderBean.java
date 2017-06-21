package com.king.khcareer.glory.title;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/21 10:33
 */
public class HeaderBean {
    private String key;
    private String content;
    private List<SubItem> itemList;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<SubItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SubItem> itemList) {
        this.itemList = itemList;
    }
}
