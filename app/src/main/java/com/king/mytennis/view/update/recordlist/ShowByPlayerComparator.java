package com.king.mytennis.view.update.recordlist;

import java.util.Comparator;
import java.util.HashMap;

import android.content.Context;

public class ShowByPlayerComparator implements Comparator<HashMap<String, String>> {

	private HashMap<String, String> namePinyinMap;
	public ShowByPlayerComparator(Context context, HashMap<String, String> namePinyinMap) {
		this.namePinyinMap = namePinyinMap;
	}
	@Override
	public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {
		
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
