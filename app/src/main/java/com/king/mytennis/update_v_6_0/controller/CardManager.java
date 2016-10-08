package com.king.mytennis.update_v_6_0.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.content.Context;

import com.king.mytennis.model.Constants;
import com.king.mytennis.model.CountData;
import com.king.mytennis.model.CountDataPlayer;
import com.king.mytennis.model.FileIO;
import com.king.mytennis.model.NamePinyinPool;
import com.king.mytennis.model.Record;

public class CardManager implements CardContentProvider {

	private Context mContext;
	private List<Record> recordList;

	/*********************************************/
	private List<String> yearList;
	private HashMap<String, List<HashMap<String, String>>> yearCardData;
	private boolean preparedYearCardData;
	private HashMap<String, CountData> countDataMap;
	/*********************************************/

	/*********************************************/
	private List<HashMap<String, String>> matchList;
	private HashMap<String, List<HashMap<String, String>>> matchCardData;
	private boolean preparedMatchCardData;
	//Match card初始化页面序号，相对于matchCardData
	public static int MATCH_DATE_BASE_UNIT = 1000;
	private static int[] dateBaseArray = new int[] {//月份乘以MATCH_DATE_BASE_UNIT
			1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 11000, 12000
	};
	/*********************************************/

	/*********************************************/
	private List<String> playerIndexList;
	private HashMap<String, List<HashMap<String, String>>> playerIndexData;
	private boolean preparedPlayerIndexCardData;
	private HashMap<String, CountDataPlayer> playerCountDataMap;
	/*********************************************/
	private HashMap<String, String> namePinyinMap;

	public CardManager(Context context) {
		mContext = context;
		/**
		 * 当有新赛事或某项赛事发生时间变化时，手动开启该代码重新创建赛事时间文档
		 */
		saveMatchOrderFile();
	}

	public void setRecordList(List<Record> list) {
		recordList = list;
	}

	public void reload() {
		preparedMatchCardData = false;
		preparedYearCardData = false;
		preparedPlayerIndexCardData = false;
		namePinyinMap = null;
	}
	@Override
	public void prepareYearCard() {

		if (recordList != null) {
			yearCardData = new HashMap<String, List<HashMap<String,String>>>();
			yearList = new ArrayList<String>();
			List<HashMap<String,String>> list = null;
			HashMap<String,String> itemMap = null;
			StringBuffer recPosBuffer = null;

			countDataMap = new HashMap<String, CountData>();
			for (int i = 0; i < recordList.size(); i ++) {
				Record record = recordList.get(i);
				String date = record.getStrDate().substring(0, 4);

				if (recPosBuffer == null) {
					recPosBuffer = new StringBuffer(i);
				}
				else {
					recPosBuffer.append("-").append(i);
				}

				/************************year card data*************************/
				//确定match的最终轮次以及match在year下的唯一性
				if (i + 1 == recordList.size() ||
						!record.getMatch().equals(recordList.get(i + 1).getMatch())) {
					if (yearCardData.get(date) == null) {
						list = new ArrayList<HashMap<String,String>>();
						yearCardData.put(date, list);
						yearList.add(date);
					}
					else {
						list = yearCardData.get(date);
					}
					itemMap = new HashMap<String, String>();
					itemMap.put("date", record.getStrDate().substring(5));
					itemMap.put("match", record.getMatch());
					if (record.getRound().equals("Final")) {
						itemMap.put("glory", record.getWinner().equals(record.getCompetitor())
								? "Runner-up" : "Winner");
					}
					else {
						itemMap.put("glory", record.getRound());
					}
					list.add(itemMap);

					itemMap.put("recPos", recPosBuffer.toString());
					recPosBuffer = null;
				}
				/*************************************************/


				/************************year count data*************************/
				countYearData(record);
			}

			concludeYearData();
		}
		preparedYearCardData = true;
	}

	private void concludeYearData() {
		CountData tcd = new CountData();
		countDataMap.put(FLAG_COUNT_DATA_WHOLE, tcd);
		Set<String> set = countDataMap.keySet();
		CountData data = null;
		for (String key:set) {
			data = countDataMap.get(key);
			tcd.setAtp250Lose(tcd.getAtp250Lose() + data.getAtp250Lose());
			tcd.setAtp250Win(tcd.getAtp250Win() + data.getAtp250Win());
			tcd.setAtp500Lose(tcd.getAtp500Lose() + data.getAtp500Lose());
			tcd.setAtp500Win(tcd.getAtp500Win() + data.getAtp500Win());
			tcd.setGsLose(tcd.getGsLose() + data.getGsLose());
			tcd.setGsWin(tcd.getGsWin() + data.getGsWin());
			tcd.setLose(tcd.getLose() + data.getLose());
			tcd.setWin(tcd.getWin() + data.getWin());
			tcd.setMaster1000Lose(tcd.getMaster1000Lose() + data.getMaster1000Lose());
			tcd.setMaster1000Win(tcd.getMaster1000Win() + data.getMaster1000Win());
			tcd.setMasterCupLose(tcd.getMasterCupLose() + data.getMasterCupLose());
			tcd.setMasterCupWin(tcd.getMasterCupWin() + data.getMasterCupWin());
		}
	}

	/**
	 * 统计total/gs/mastercup/master1000/atp500/atp250
	 * @param record
	 */
	private void countYearData(Record record) {
		String year = record.getStrDate().substring(0, 4);
		CountData countData = countDataMap.get(year);
		if (countData == null) {
			countData = new CountData();
			countDataMap.put(year, countData);
		}

		if (record.getWinner().equals(record.getCompetitor())) {
			countData.setLose(countData.getLose() + 1);
			if (Constants.RECORD_MATCH_LEVELS[0].equals(record.getLevel())) {
				countData.setGsLose(countData.getGsLose() + 1);
			}
			else if (Constants.RECORD_MATCH_LEVELS[1].equals(record.getLevel())) {
				countData.setMasterCupLose(countData.getMasterCupLose() + 1);
			}
			else if (Constants.RECORD_MATCH_LEVELS[2].equals(record.getLevel())) {
				countData.setMaster1000Lose(countData.getMaster1000Lose() + 1);
			}
			else if (Constants.RECORD_MATCH_LEVELS[3].equals(record.getLevel())) {
				countData.setAtp500Lose(countData.getAtp500Lose() + 1);
			}
			else if (Constants.RECORD_MATCH_LEVELS[4].equals(record.getLevel())) {
				countData.setAtp250Lose(countData.getAtp250Lose() + 1);
			}
			else {
				countData.setOtherLose(countData.getOtherLose() + 1);
			}
		}
		else {
			countData.setWin(countData.getWin() + 1);
			if (Constants.RECORD_MATCH_LEVELS[0].equals(record.getLevel())) {
				countData.setGsWin(countData.getGsWin() + 1);
			}
			else if (Constants.RECORD_MATCH_LEVELS[1].equals(record.getLevel())) {
				countData.setMasterCupWin(countData.getMasterCupWin() + 1);
			}
			else if (Constants.RECORD_MATCH_LEVELS[2].equals(record.getLevel())) {
				countData.setMaster1000Win(countData.getMaster1000Win() + 1);
			}
			else if (Constants.RECORD_MATCH_LEVELS[3].equals(record.getLevel())) {
				countData.setAtp500Win(countData.getAtp500Win() + 1);
			}
			else if (Constants.RECORD_MATCH_LEVELS[4].equals(record.getLevel())) {
				countData.setAtp250Win(countData.getAtp250Win() + 1);
			}
			else {
				countData.setOtherWin(countData.getOtherWin() + 1);
			}
		}
	}

	@Override
	public List<HashMap<String, String>> getYearCard(String year) throws CardDataUnprepareException {
		if (!preparedYearCardData) {
			throw new CardDataUnprepareException();
		}

		if (yearCardData != null) {
			List<HashMap<String, String>> list = yearCardData.get("" + year);
			Collections.reverse(list);
			return list;
		}
		return null;
	}

	@Override
	public List<String> getYearList() {
		return yearList;
	}

	@Override
	public void prepareMatchCard() {
		if (recordList != null) {
			matchCardData = new HashMap<String, List<HashMap<String,String>>>();
			matchList = new ArrayList<HashMap<String,String>>();
			List<HashMap<String,String>> list = null;
			HashMap<String,String> itemMap = null;
			HashMap<String,String> matchMap = null;

			for (int i = 0; i < recordList.size(); i ++) {
				Record record = recordList.get(i);
				String match = record.getMatch();
				if (i + 1 == recordList.size() ||
						!match.equals(recordList.get(i + 1).getMatch())) {
					if (matchCardData.get(match) == null) {
						list = new ArrayList<HashMap<String,String>>();
						matchCardData.put(match, list);

						matchMap = new HashMap<String, String>();
						matchMap.put("match", match);
						matchMap.put("level", record.getLevel());
						matchMap.put("court", record.getCourt());
						matchList.add(matchMap);
					}
					else {
						list = matchCardData.get(match);
					}

					itemMap = new HashMap<String, String>();
					itemMap.put("date", record.getStrDate());
					String line1 = record.getRound() + "  " + record.getCompetitor() + "(" + record.getCptRank()
							+ "/" + record.getCptSeed() + ")";
					itemMap.put("line1", line1);
					String line2 = null;
					if (record.getWinner().equals(record.getCompetitor())) {
						line2 = "Lose  " + record.getScore();
					}
					else {
						line2 = "Win  " + record.getScore();
					}
					itemMap.put("line2", line2);
					list.add(itemMap);
				}
			}
		}
		preparedMatchCardData = true;
	}

	@Override
	public List<HashMap<String, String>> getMatchList() {
		MatchComparator comparator = new MatchComparator();
		Collections.sort(matchList, comparator);
		return matchList;
	}

	private class MatchComparator implements Comparator<HashMap<String, String>> {

		private HashMap<String, Integer> map;

		public MatchComparator() {
			map = new HashMap<String, Integer>();
			List<HashMap<String, String>> list = new FileIO().readMatchOrderList();
			for (int i = 0; i < list.size(); i ++) {
				map.put(list.get(i).get("match_name"),
						Integer.parseInt(list.get(i).get("order_flag")));
			}
		}

		@Override
		public int compare(HashMap<String, String> map1,
						   HashMap<String, String> map2) {

			int value1 = 0;
			if (map.get(map1.get("match")) != null) {
				value1 = map.get(map1.get("match"));
			}
			int value2 = 0;
			if (map.get(map2.get("match")) != null) {
				value2 = map.get(map2.get("match"));
			}
			if (value1 - value2 > 0) {
				return 1;
			}
			else if (value1 - value2 < 0) {
				return -1;
			}
			else {
				return 0;
			}
		}

	}

	@Override
	public List<HashMap<String, String>> getMatchCard(String match)
			throws CardDataUnprepareException {

		if (!preparedMatchCardData) {
			throw new CardDataUnprepareException();
		}

		if (matchCardData != null) {
			List<HashMap<String, String>> list = matchCardData.get(match);
			Collections.reverse(list);
			return list;
		}
		return null;
	}

	private class PlayerRecord {
		public String country;
		public List<Boolean> isWinnerList;//在遍历recordlist每一个记录时记录胜负关系，遍历完毕后对该player进行合计产生h2h的value
		public String name;
		public String h2h;//decide by isWinnerList
		public String pinyinIndex;//first pinyin character
	}
	@Override
	public void preparePlayerIndexCard() {

		if (recordList != null && recordList.size() > 0) {
			playerIndexData = new HashMap<String, List<HashMap<String,String>>>();
			playerIndexList = new ArrayList<String>();
			createNamePinyinPool();

			//先统计出所有的player
			HashMap<String,PlayerRecord> playerMap = new HashMap<String, CardManager.PlayerRecord>();
			PlayerRecord pRecord = null;

			playerCountDataMap = new HashMap<String, CountDataPlayer>();
			for (int i = 0; i < recordList.size(); i ++) {
				Record record = recordList.get(i);
				if (playerMap.get(record.getCompetitor()) == null) {
					pRecord = new PlayerRecord();
					pRecord.name = record.getCompetitor();
					pRecord.country = record.getCptCountry();
					if (namePinyinMap.get(record.getCompetitor()) == null) {
						pRecord.pinyinIndex = "#";
					}
					else {
						pRecord.pinyinIndex = "" + namePinyinMap.get(record.getCompetitor()).charAt(0);
					}
					pRecord.isWinnerList = new ArrayList<Boolean>();
					pRecord.isWinnerList.add(!record.getWinner().equals(record.getCompetitor()));
					playerMap.put(record.getCompetitor(), pRecord);
				}
				else {
					pRecord = playerMap.get(record.getCompetitor());
					pRecord.isWinnerList.add(!record.getWinner().equals(record.getCompetitor()));
				}

				/**************player year count data*****************/
				countPlayerData(record);
			}
			concludePlayerData();

			//计算与所有player的h2h记录，并将对象放入list中用于之后的排序
			List<PlayerRecord> list = new ArrayList<CardManager.PlayerRecord>();
			for (PlayerRecord pr:playerMap.values()) {
				int win = 0;
				for (boolean isWinner:pr.isWinnerList) {
					if (isWinner) {
						win ++;
					}
				}
				pr.h2h = win + "-" + (pr.isWinnerList.size() - win);
				list.add(pr);
			}

			//先将所有的player按照第一个字母的音序排列，这样便于统计单个字母对应的player list
			Collections.sort(list, new Comparator<PlayerRecord>() {

				@Override
				public int compare(PlayerRecord record0, PlayerRecord record1) {

					return record1.pinyinIndex.compareTo(record0.pinyinIndex);
				}
			});

			//最终装配index-player数据以及index列表
			String curIndex = null;
			List<HashMap<String,String>> indexList = null;
			for (int i = 0; i < list.size(); i ++) {
				String index = list.get(i).pinyinIndex;
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("player", list.get(i).name);
				map.put("country", list.get(i).country);
				map.put("h2h", list.get(i).h2h);
				if (index.equals(curIndex)) {
					indexList = playerIndexData.get(index);
					indexList.add(map);
				}
				else {
					playerIndexList.add(index);
					indexList = new ArrayList<HashMap<String,String>>();
					indexList.add(map);
					playerIndexData.put(index, indexList);
					curIndex = index;
				}
			}
		}
		preparedPlayerIndexCardData = true;
	}

	private void concludePlayerData() {
		CountDataPlayer tcd = new CountDataPlayer();
		playerCountDataMap.put(FLAG_COUNT_DATA_WHOLE, tcd);
		Set<String> set = playerCountDataMap.keySet();
		CountDataPlayer data = null;
		for (String key:set) {
			data = playerCountDataMap.get(key);
			tcd.setTop1Lose(tcd.getTop1Lose() + data.getTop1Lose());
			tcd.setTop1Win(tcd.getTop1Win() + data.getTop1Win());
			tcd.setTop10Lose(tcd.getTop10Lose() + data.getTop10Lose());
			tcd.setTop10Win(tcd.getTop10Win() + data.getTop10Win());
			tcd.setTop1020Lose(tcd.getTop1020Lose() + data.getTop1020Lose());
			tcd.setTop1020Win(tcd.getTop1020Win() + data.getTop1020Win());
			tcd.setTop2050Lose(tcd.getTop2050Lose() + data.getTop2050Lose());
			tcd.setTop2050Win(tcd.getTop2050Win() + data.getTop2050Win());
			tcd.setTop50100Lose(tcd.getTop50100Lose() + data.getTop50100Lose());
			tcd.setTop50100Win(tcd.getTop50100Win() + data.getTop50100Win());
			tcd.setOutof100Lose(tcd.getOutof100Lose() + data.getOutof100Lose());
			tcd.setOutof100Win(tcd.getOutof100Win() + data.getOutof100Win());
		}
	}

	/**
	 * 统计 top1/10/10-20/20-50/50-100/out of 100
	 * @param record
	 */
	private void countPlayerData(Record record) {
		String year = record.getStrDate().substring(0, 4);
		CountDataPlayer countData = playerCountDataMap.get(year);
		if (countData == null) {
			countData = new CountDataPlayer();
			playerCountDataMap.put(year, countData);
		}

		int rank = record.getCptRank();
		if (record.getWinner().equals(record.getCompetitor())) {
			if (rank == 1) {
				countData.setTop1Lose(countData.getTop1Lose() + 1);
				countData.setTop10Lose(countData.getTop10Lose() + 1);
			}
			else if (rank == 0) {
				countData.setOutof100Lose(countData.getOutof100Lose() + 1);
			}
			else if (rank < 11) {
				countData.setTop10Lose(countData.getTop10Lose() + 1);
			}
			else if (rank > 10 && rank < 21) {
				countData.setTop1020Lose(countData.getTop1020Lose() + 1);
			}
			else if (rank > 20 && rank < 51) {
				countData.setTop2050Lose(countData.getTop2050Lose() + 1);
			}
			else if (rank > 50 && rank < 101) {
				countData.setTop50100Lose(countData.getTop50100Lose() + 1);
			}
			else {
				countData.setOutof100Lose(countData.getOutof100Lose() + 1);
			}
		}
		else {
			if (rank == 1) {
				countData.setTop1Win(countData.getTop1Win() + 1);
				countData.setTop10Win(countData.getTop10Win() + 1);
			}
			else if (rank == 0) {
				countData.setOutof100Win(countData.getOutof100Win() + 1);
			}
			else if (rank < 11) {
				countData.setTop10Win(countData.getTop10Win() + 1);
			}
			else if (rank > 10 && rank < 21) {
				countData.setTop1020Win(countData.getTop1020Win() + 1);
			}
			else if (rank > 20 && rank < 51) {
				countData.setTop2050Win(countData.getTop2050Win() + 1);
			}
			else if (rank > 50 && rank < 101) {
				countData.setTop50100Win(countData.getTop50100Win() + 1);
			}
			else {
				countData.setOutof100Win(countData.getOutof100Win() + 1);
			}
		}
	}

	private void createNamePinyinPool() {
		if (namePinyinMap == null) {
			namePinyinMap = new NamePinyinPool(mContext).getNamePinyinMap();
		}
	}

	@Override
	public List<String> getPlayerIndexList() {
		return playerIndexList;
	}

	@Override
	public List<HashMap<String, String>> getPlayerIndexCard(String index)
			throws CardDataUnprepareException {

		if (!preparedPlayerIndexCardData) {
			throw new CardDataUnprepareException();
		}

		if (playerIndexData != null) {
			return playerIndexData.get(index);
		}
		return null;
	}

	@Override
	public CountData getYearCountData(String year) throws CardDataUnprepareException {

		if (!preparedYearCardData) {
			throw new CardDataUnprepareException();
		}

		if (countDataMap != null) {
			return countDataMap.get(year);
		}
		return null;
	}

	@Override
	public CountDataPlayer getPlayerCountData(String year)
			throws CardDataUnprepareException {
		if (!preparedPlayerIndexCardData) {
			throw new CardDataUnprepareException();
		}

		if (countDataMap != null) {
			return playerCountDataMap.get(year);
		}
		return null;
	}

	/**
	 * 定义赛事的时间顺序，为方便插入新赛事，间隔留有余地较大。
	 */
	public static void saveMatchOrderFile() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[0] + 100));
		map.put("match_name", "布里斯班国际赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[0] + 200));
		map.put("match_name", "澳大利亚保险公司国际赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[0] + 200));
		map.put("match_name", "悉尼国际赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[0] + 300));
		map.put("match_name", "澳大利亚网球公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[1] + 100));
		map.put("match_name", "法国南部公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[1] + 200));
		map.put("match_name", "荷兰网球锦标赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[1] + 300));
		map.put("match_name", "孟菲斯公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[1] + 400));
		map.put("match_name", "美国国家室内冠军赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[1] + 500));
		map.put("match_name", "里约公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[1] + 600));
		map.put("match_name", "德尔雷海滩国际网球冠军赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[1] + 700));
		map.put("match_name", "马赛公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[1] + 800));
		map.put("match_name", "迪拜免税网球冠军赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[1] + 820));
		map.put("match_name", "墨西哥电信赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[1] + 900));
		map.put("match_name", "戴维斯杯");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[2] + 100));
		map.put("match_name", "印第安维尔斯大师赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[2] + 200));
		map.put("match_name", "迈阿密大师赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[3] + 100));
		map.put("match_name", "蒙特卡洛大师赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[3] + 200));
		map.put("match_name", "巴塞罗那公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[3] + 300));
		map.put("match_name", "慕尼黑公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[3] + 320));
		map.put("match_name", "埃斯托利尔公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[4] + 100));
		map.put("match_name", "马德里大师赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[4] + 200));
		map.put("match_name", "罗马大师赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[4] + 300));
		map.put("match_name", "尼斯公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[4] + 400));
		map.put("match_name", "法国网球公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[5] + 100));
		map.put("match_name", "荷兰公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[5] + 200));
		map.put("match_name", "女王杯");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[5] + 300));
		map.put("match_name", "哈雷公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[5] + 400));
		map.put("match_name", "诺丁汉公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[5] + 500));
		map.put("match_name", "温布尔顿网球公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[6] + 100));
		map.put("match_name", "诺丁汉公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[6] + 150));
		map.put("match_name", "纽波特公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[6] + 200));
		map.put("match_name", "瑞典公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[6] + 250));
		map.put("match_name", "洛杉矶乡村经典赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[6] + 300));
		map.put("match_name", "汉堡公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[6] + 350));
		map.put("match_name", "亚特兰大网球冠军赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[6] + 350));
		map.put("match_name", "亚特兰大网球公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[7] + 400));
		map.put("match_name", "雷格梅森精英赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[7] + 400));
		map.put("match_name", "花旗银行公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[7] + 400));
		map.put("match_name", "华盛顿公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[7] + 500));
		map.put("match_name", "罗杰斯杯");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[7] + 600));
		map.put("match_name", "西南财团公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[7] + 600));
		map.put("match_name", "辛辛那提大师赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[7] + 700));
		map.put("match_name", "温斯顿塞勒姆公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[7] + 800));
		map.put("match_name", "美国网球公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[8] + 100));
		map.put("match_name", "戴维斯杯世界组半决赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[8] + 200));
		map.put("match_name", "梅兹公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[8] + 250));
		map.put("match_name", "马来西亚公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[8] + 300));
		map.put("match_name", "深圳公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[9] + 100));
		map.put("match_name", "中国网球公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[9] + 150));
		map.put("match_name", "东京公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[9] + 200));
		map.put("match_name", "上海大师赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[9] + 300));
		map.put("match_name", "莫斯科公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[9] + 400));
		map.put("match_name", "瓦伦西亚公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[9] + 450));
		map.put("match_name", "巴塞尔公开赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[10] + 100));
		map.put("match_name", "巴黎大师赛");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("order_flag", "" + (dateBaseArray[10] + 200));
		map.put("match_name", "伦敦大师杯");
		list.add(map);

		new FileIO().saveMatchOrderList(list);
	}

}
