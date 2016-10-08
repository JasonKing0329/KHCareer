package com.king.mytennis.view_v_7_0.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.king.mytennis.model.Record;
import com.king.mytennis.service.RecordService;

/**
 * @author JingYang
 *
 */
public class MatchController {

	private List<Record> recordList;

	private List<List<Record>> expandList;

	public List<Record> getRecordList() {
		return recordList;
	}

	public List<List<Record>> getExpandList() {
		return expandList;
	}
	
	public void loadRecords(Context context, String matchId) {
		recordList = new RecordService(context).queryByWhere("match=?", new String[]{matchId});
		
		expandList = new ArrayList<List<Record>>();
		Map<String, List<Record>> map = new HashMap<String, List<Record>>();
		Collections.reverse(recordList);
		List<Record> child = null;
		for (int i = 0; i < recordList.size(); i ++) {
			Record record = recordList.get(i);
			child = map.get(record.getStrDate());
			if (child == null) {
				child = new ArrayList<Record>();
				expandList.add(child);
				map.put(record.getStrDate(), child);
			}
			child.add(record);
		}
	}
}
