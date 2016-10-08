package com.king.mytennis.service;

import java.util.List;

import com.king.mytennis.model.RankingDao;
import com.king.mytennis.model.RankingItem;

public class RankingService {

	public List<RankingItem> getLatestRankingList() {
		
		return getRankingList(new RankingDao().queryLatestDate());
	}
	
	public List<RankingItem> getRankingList(String date) {
		
		List<RankingItem> list = null;
		
		return list;
	}
}
