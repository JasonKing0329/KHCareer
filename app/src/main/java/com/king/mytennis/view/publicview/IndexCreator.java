package com.king.mytennis.view.publicview;

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
				index = "" + index.toUpperCase().charAt(0);
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
