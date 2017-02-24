package com.king.mytennis.view.update.recordlist;

import java.util.Comparator;
import java.util.Map;

import android.content.Context;

public class ShowByPlayerComparator implements Comparator<Map<String, String>> {

	private Map<String, String> namePinyinMap;
	public ShowByPlayerComparator(Context context, Map<String, String> namePinyinMap) {
		this.namePinyinMap = namePinyinMap;
	}
	@Override
	public int compare(Map<String, String> lhs, Map<String, String> rhs) {
		
		String left = lhs.get("player");
		String right = rhs.get("player");
		left = namePinyinMap.get(left);
		right = namePinyinMap.get(right);
		if (left == null) {
			left = "0";
		}
		if (right == null) {
			right = "0";
		}
		return left.compareTo(right);
	}

}
