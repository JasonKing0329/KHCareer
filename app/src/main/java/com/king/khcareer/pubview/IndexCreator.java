package com.king.khcareer.pubview;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class IndexCreator {

	private SideBar sideBar;
	private Map<String, Integer> indexMap;
	
	public IndexCreator(SideBar sideBar) {
		this.sideBar = sideBar;
		indexMap = new HashMap<String, Integer>();
	}

	public void createFrom(List<HashMap<String, String>> list, Map<String, String> namePinyinMap) {
		indexMap.clear();
		sideBar.clear();
		if (list != null && list.size() > 0) {
			TreeSet<String> set = new TreeSet<String>();
			String index = null;
			String name = null;
			for (int i = 0; i < list.size(); i ++) {
				name = list.get(i).get("player");
				index = namePinyinMap.get(name);
				if (index == null) {
					index = name;
				}
				// 没有名称的作为特殊字符处理
				if (TextUtils.isEmpty(index)) {
					index = "#";
				}
				else {
					char chIndex = index.toUpperCase().charAt(0);
					if (chIndex > 'Z' || chIndex < 'A') {
						chIndex = '#';
					}
					index = String.valueOf(chIndex);
				}
				if (set.add(index)) {
					indexMap.put(index, i);
					sideBar.addIndex(index);
				}
			}
		}
	}
	
	public int getIndexPosition(String index) {
		return indexMap.get(index);
	}
}
