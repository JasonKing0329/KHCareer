package com.king.mytennis.model;

import java.util.ArrayList;
import java.util.List;

import com.king.mytennis.interfc.H2HDAO;
import com.king.mytennis.multiuser.MultiUserManager;

public class H2HDAOList implements H2HDAO {

	private List<Record> list;
	private String competitor;
	private int win;
	private int lose;
	private String details;
	private boolean isHandled;
	
	public H2HDAOList(List<Record> list, String cpt) {
		this.list = list;
		competitor = cpt;
		isHandled = false;
	}
	
	@Override
	public int getWin() {

		if (!isHandled) {
			handleList();
		}
		return win;
	}

	@Override
	public int getLose() {
		
		if (!isHandled) {
			handleList();
		}
		return lose;
	}

	@Override
	public String getH2HDetail() {
		
		if (!isHandled) {
			handleList();
		}
		return details;
	}

	private void handleList() {
		
		StringBuilder builder = new StringBuilder();
		boolean isWinner = false;
		String winner = null;
		for (Record record:list){
			if (competitor.matches(record.getCompetitor())) {
				isWinner = MultiUserManager.USER_DB_FLAG.equals(record.getWinner());
				winner = record.getWinner();
				if (isWinner) {
					win++;
					winner = MultiUserManager.getInstance().getCurrentUser().getDisplayName();
				}
				else {
					lose++;
				}
				
				builder.append(record.getStrDate()).append("  ").append(record.getMatch())
					.append("  ").append(record.getRound()).append("  ")
					.append(winner).append("\n")
					.append("               ").append(record.getCourt())
					.append("  ").append(record.getScore()).append("\n");
			}
		}
		details = builder.toString();
		isHandled = true;
	}

	@Override
	public List<Record> getH2HList() {
		List<Record> rList = new ArrayList<Record>();
		for (Record record:list){
			if (competitor.matches(record.getCompetitor())) {
				if (MultiUserManager.USER_DB_FLAG.equals(record.getWinner())) {
					win++;
				}
				else {
					lose++;
				}

				rList.add(record);
			}
		}
		return rList;
	}
}
