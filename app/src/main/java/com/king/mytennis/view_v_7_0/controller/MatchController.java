package com.king.mytennis.view_v_7_0.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.king.mytennis.model.Record;
import com.king.mytennis.pubdata.PubDataProvider;
import com.king.mytennis.pubdata.bean.MatchNameBean;
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

	/**
	 * 数据库系统更新后（v3.0），需要按照matchId来查询
	 * 解决不同赛事名称但是是一站赛事的问题
	 * @param context
	 * @param matchId
     */
	public void loadRecords(Context context, int matchId) {
		List<MatchNameBean> matches = new PubDataProvider().getMatchNameList(matchId);
		String where = "match=?";
		String[] args;
		if (matches.size() > 1) {
			args = new String[matches.size()];
			args[0] = matches.get(0).getName();
			for (int i = 1; i < matches.size(); i ++) {
				where = where.concat(" OR match=?");
				args[i] = matches.get(i).getName();
			}
		}
		else {
			args = new String[]{matches.get(0).getName()};
		}
		recordList = new RecordService(context).queryByWhere(where, args);
		
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
