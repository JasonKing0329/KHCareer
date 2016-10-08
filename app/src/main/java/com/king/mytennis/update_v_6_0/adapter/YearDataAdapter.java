package com.king.mytennis.update_v_6_0.adapter;

import java.util.Collections;
import java.util.List;

import android.content.Context;

import com.king.mytennis.model.CountData;
import com.king.mytennis.update_v_6_0.controller.CardDataUnprepareException;
import com.king.mytennis.update_v_6_0.controller.CardManager;
import com.king.mytennis.view.R;

public class YearDataAdapter extends AbstractDataCountAdapter {

	private final int NUM_ITEM = 6;
	private CardManager cardManager;
	private String[] contents;
	private String[] titles;
	private Context mContext;

	private List<String> years;
	private int yearIndex;

	public YearDataAdapter(Context context, CardManager cardManager) {
		this.cardManager = cardManager;
		mContext = context;
		years = cardManager.getYearList();
		Collections.reverse(years);
		years.add(CardManager.FLAG_COUNT_DATA_WHOLE);
	}

	@Override
	public int getCount() {
		return NUM_ITEM;
	}

	@Override
	public String getTitle(int index) {
		if (titles == null) {
			titles = mContext.getResources().getStringArray(R.array.data_count_year);
		}
		return titles[index];
	}

	@Override
	public String getContent(int index) {
		return contents[index];
	}

	@Override
	public void onYearChanged(String year) {
		if (contents == null) {
			contents = new String[6];
		}

		CountData data = null;
		try {
			data = cardManager.getYearCountData(year);
		} catch (CardDataUnprepareException e) {
			e.printStackTrace();
		}

		if (data == null) {
			return;
		}

		StringBuffer buffer = new StringBuffer();
		double rate = (double) data.getWin()/ (double) (data.getWin() + data.getLose());
		rate = (double) ((int)(rate*10000))/(double) 100;
		buffer.append(data.getWin()).append("胜").append(data.getLose()).append("负")
				.append("      胜率：").append(rate).append("%");
		contents[0] = buffer.toString();

		buffer = new StringBuffer();
		rate = (double) data.getGsWin()/ (double) (data.getGsWin() + data.getGsLose());
		rate = (double) ((int)(rate*10000))/(double) 100;
		buffer.append(data.getGsWin()).append("胜").append(data.getGsLose()).append("负")
				.append("      胜率：").append(rate).append("%");
		contents[1] = buffer.toString();

		buffer = new StringBuffer();
		rate = (double) data.getMasterCupWin()/ (double) (data.getMasterCupWin() + data.getMasterCupLose());
		rate = (double) ((int)(rate*10000))/(double) 100;
		buffer.append(data.getMasterCupWin()).append("胜").append(data.getMasterCupLose()).append("负")
				.append("      胜率：").append(rate).append("%");
		contents[2] = buffer.toString();

		buffer = new StringBuffer();
		rate = (double) data.getMaster1000Win()/ (double) (data.getMaster1000Win() + data.getMaster1000Lose());
		rate = (double) ((int)(rate*10000))/(double) 100;
		buffer.append(data.getMaster1000Win()).append("胜").append(data.getMaster1000Lose()).append("负")
				.append("      胜率：").append(rate).append("%");
		contents[3] = buffer.toString();

		buffer = new StringBuffer();
		rate = (double) data.getAtp500Win()/ (double) (data.getAtp500Win() + data.getAtp500Lose());
		rate = (double) ((int)(rate*10000))/(double) 100;
		buffer.append(data.getAtp500Win()).append("胜").append(data.getAtp500Lose()).append("负")
				.append("      胜率：").append(rate).append("%");
		contents[4] = buffer.toString();

		buffer = new StringBuffer();
		rate = (double) data.getAtp250Win()/ (double) (data.getAtp250Win() + data.getAtp250Lose());
		rate = (double) ((int)(rate*10000))/(double) 100;
		buffer.append(data.getAtp250Win()).append("胜").append(data.getAtp250Lose()).append("负")
				.append("      胜率：").append(rate).append("%");
		contents[5] = buffer.toString();
	}

	@Override
	public String previousYear() {

		if (yearIndex == 0) {
			yearIndex = years.size() - 1;
		}
		else {
			yearIndex --;
		}
		return years.get(yearIndex);
	}
	@Override
	public String nextYear() {

		if (yearIndex == years.size() - 1) {
			yearIndex = 0;
		}
		else {
			yearIndex ++;
		}
		return years.get(yearIndex);
	}

	@Override
	public void setCurrentYear(String year) {
		for (int i = 0; i < years.size(); i ++) {
			if (years.get(i).equals(year)) {
				yearIndex = i;
			}
		}
	}
}
