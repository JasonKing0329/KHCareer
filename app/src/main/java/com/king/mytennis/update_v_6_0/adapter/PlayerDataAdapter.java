package com.king.mytennis.update_v_6_0.adapter;

import java.util.List;

import com.king.mytennis.model.CountDataPlayer;
import com.king.mytennis.update_v_6_0.controller.CardDataUnprepareException;
import com.king.mytennis.update_v_6_0.controller.CardManager;
import com.king.mytennis.view.R;

import android.content.Context;

public class PlayerDataAdapter extends AbstractDataCountAdapter {

	private final int NUM_ITEM = 6;
	private CardManager cardManager;
	private String[] contents;
	private String[] titles;
	private List<String> years;
	private Context mContext;

	private int yearIndex;

	public PlayerDataAdapter(Context context, CardManager cardManager) {
		this.cardManager = cardManager;
		mContext = context;
		years = cardManager.getYearList();
		//Collections.reverse(years);//different from year adapter
		years.add(CardManager.FLAG_COUNT_DATA_WHOLE);
	}
	@Override
	public int getCount() {
		return NUM_ITEM;
	}

	@Override
	public String getTitle(int index) {
		if (titles == null) {
			titles = mContext.getResources().getStringArray(R.array.data_count_player);
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

		CountDataPlayer data = null;
		try {
			data = cardManager.getPlayerCountData(year);
		} catch (CardDataUnprepareException e) {
			e.printStackTrace();
		}

		if (data == null) {
			return;
		}

		StringBuffer buffer = new StringBuffer();
		double rate = (double) data.getTop1Win()/ (double) (data.getTop1Win() + data.getTop1Lose());
		rate = (double) ((int)(rate*10000))/(double) 100;
		buffer.append(data.getTop1Win()).append("胜").append(data.getTop1Lose()).append("负")
				.append("      胜率：").append(rate).append("%");
		contents[0] = buffer.toString();

		buffer = new StringBuffer();
		rate = (double) data.getTop10Win()/ (double) (data.getTop10Win() + data.getTop10Lose());
		rate = (double) ((int)(rate*10000))/(double) 100;
		buffer.append(data.getTop10Win()).append("胜").append(data.getTop10Lose()).append("负")
				.append("      胜率：").append(rate).append("%");
		contents[1] = buffer.toString();

		buffer = new StringBuffer();
		rate = (double) data.getTop1020Win()/ (double) (data.getTop1020Win() + data.getTop1020Lose());
		rate = (double) ((int)(rate*10000))/(double) 100;
		buffer.append(data.getTop1020Win()).append("胜").append(data.getTop1020Lose()).append("负")
				.append("      胜率：").append(rate).append("%");
		contents[2] = buffer.toString();

		buffer = new StringBuffer();
		rate = (double) data.getTop2050Win()/ (double) (data.getTop2050Win() + data.getTop2050Lose());
		rate = (double) ((int)(rate*10000))/(double) 100;
		buffer.append(data.getTop2050Win()).append("胜").append(data.getTop2050Lose()).append("负")
				.append("      胜率：").append(rate).append("%");
		contents[3] = buffer.toString();

		buffer = new StringBuffer();
		rate = (double) data.getTop50100Win()/ (double) (data.getTop50100Win() + data.getTop50100Lose());
		rate = (double) ((int)(rate*10000))/(double) 100;
		buffer.append(data.getTop50100Win()).append("胜").append(data.getTop50100Lose()).append("负")
				.append("      胜率：").append(rate).append("%");
		contents[4] = buffer.toString();

		buffer = new StringBuffer();
		rate = (double) data.getOutof100Win()/ (double) (data.getOutof100Win() + data.getOutof100Lose());
		rate = (double) ((int)(rate*10000))/(double) 100;
		buffer.append(data.getOutof100Win()).append("胜").append(data.getOutof100Lose()).append("负")
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
