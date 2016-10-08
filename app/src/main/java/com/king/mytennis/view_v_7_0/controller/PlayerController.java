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
public class PlayerController {

	private List<Record> recordList;

	private List<List<Record>> expandList;

	public List<Record> getRecordList() {
		return recordList;
	}

	public List<List<Record>> getExpandList() {
		return expandList;
	}
	
	public void loadRecords(Context context, String playerId) {
		recordList = new RecordService(context).queryByWhere("competitor=?", new String[]{playerId});
		
		expandList = new ArrayList<List<Record>>();
		Map<String, List<Record>> map = new HashMap<String, List<Record>>();
		Collections.reverse(recordList);
		List<Record> child = null;
		for (int i = 0; i < recordList.size(); i ++) {
			Record record = recordList.get(i);
			String year = record.getStrDate().split("-")[0];
			child = map.get(year);
			if (child == null) {
				child = new ArrayList<Record>();
				expandList.add(child);
				map.put(year, child);
			}
			child.add(record);
		}
	}
}
