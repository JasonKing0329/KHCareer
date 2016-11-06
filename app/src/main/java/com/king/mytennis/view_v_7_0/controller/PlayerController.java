package com.king.mytennis.view_v_7_0.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.Constants;
import com.king.mytennis.model.Record;
import com.king.mytennis.service.RecordService;
import com.king.mytennis.view.R;

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

	private String countInfor;

	public List<List<Record>> getExpandList() {
		return expandList;
	}

	public String getCountInfor() {
		return countInfor;
	}

	public void loadRecords(Context context, String playerId) {
		recordList = new RecordService(context).queryByWhere("competitor=?", new String[]{playerId});
		
		expandList = new ArrayList<List<Record>>();
		Map<String, List<Record>> map = new HashMap<String, List<Record>>();
		Collections.reverse(recordList);
		List<Record> child = null;

		int hardWin = 0, hardLose = 0;
		int clayWin = 0, clayLose = 0;
		int grassWin = 0, grassLose = 0;
		int innerhardWin = 0, innerhardLose = 0;
		int gsWin = 0, gsLose = 0;
		int finalWin = 0, finalLose = 0;
		int sfWin = 0, sfLose = 0;
		String[] roundArray = context.getResources().getStringArray(R.array.spinner_round);
		String[] levelArray = context.getResources().getStringArray(R.array.spinner_level);
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

			//如果是赛前退赛不算作h2h
			if (record.getScore().equals(Constants.SCORE_RETIRE)) {
				continue;
			}
			// count h2h by court
			if (record.getCourt().equals("硬地")) {
				if (record.getCompetitor().equals(record.getWinner())) {
					hardLose ++;
				}
				else {
					hardWin ++;
				}
			}
			else if (record.getCourt().equals("红土")) {
				if (record.getCompetitor().equals(record.getWinner())) {
					clayLose ++;
				}
				else {
					clayWin ++;
				}
			}
			else if (record.getCourt().equals("室内硬地")) {
				if (record.getCompetitor().equals(record.getWinner())) {
					innerhardLose ++;
				}
				else {
					innerhardWin ++;
				}
			}
			else if (record.getCourt().equals("草地")) {
				if (record.getCompetitor().equals(record.getWinner())) {
					grassLose ++;
				}
				else {
					grassWin ++;
				}
			}

			// count h2h by gs
			if (record.getLevel().equals(levelArray[0])) {//gs
				if (record.getCompetitor().equals(record.getWinner())) {
					gsLose ++;
				}
				else {
					gsWin ++;
				}
			}

			// count h2h by round
			if (record.getRound().equals(roundArray[0])) {//final
				if (record.getCompetitor().equals(record.getWinner())) {
					finalLose ++;
				}
				else {
					finalWin ++;
				}
			}
			else if (record.getRound().equals(roundArray[0])) {//semi final
				if (record.getCompetitor().equals(record.getWinner())) {
					sfLose ++;
				}
				else {
					sfWin ++;
				}
			}
		}

		StringBuffer buffer = new StringBuffer();
		if (hardLose > 0 && hardWin > 0) {
			buffer.append("硬地 ").append(hardWin).append("-").append(hardLose).append("\n");
		}
		if (clayLose > 0 && clayWin > 0) {
			buffer.append("红土 ").append(clayWin).append("-").append(clayLose).append("\n");
		}
		if (grassLose > 0 && grassWin > 0) {
			buffer.append("草地 ").append(grassWin).append("-").append(grassLose).append("\n");
		}
		if (innerhardLose > 0 && innerhardWin > 0) {
			buffer.append("室内硬地 ").append(innerhardWin).append("-").append(innerhardLose).append("\n");
		}
		if (gsLose > 0 && gsWin > 0) {
			buffer.append("大满贯 ").append(gsWin).append("-").append(gsLose).append("\n");
		}
		if (finalLose > 0 && finalWin > 0) {
			buffer.append("决赛 ").append(finalWin).append("-").append(finalLose).append("\n");
		}
		if (sfLose > 0 && sfWin > 0) {
			buffer.append("半决赛 ").append(sfWin).append("-").append(sfLose).append("\n");
		}

		countInfor = buffer.toString();
		if (countInfor.length() > 0) {
			countInfor = countInfor.substring(0, countInfor.length() - 1);// 去掉末尾的"\n"
		}
	}

}
