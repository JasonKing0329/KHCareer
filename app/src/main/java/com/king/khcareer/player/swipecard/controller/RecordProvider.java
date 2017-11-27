package com.king.khcareer.player.swipecard.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.model.PubProviderHelper;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.utils.CharacterParser;
import com.king.mytennis.view.R;
//import com.king.mytennis.view_v_7_0.model.H2HBean;
import com.king.khcareer.match.swipecard.MatchBean;
import com.king.khcareer.player.timeline.PlayerBean;

/**
 * @author JingYang
 * @version create time：2016-3-10 下午4:50:55
 *
 */
public class RecordProvider {

	private final String TAG = "RecordProvider";

	private Context mContext;
	private String strChampion;
	private String strRunnerup;

	private List<MatchBean> matchList;
	private List<PlayerBean> playerList;

	public RecordProvider(Context context) {
		mContext = context;
		strChampion = mContext.getString(R.string.champion);
		strRunnerup = mContext.getString(R.string.runnerup);
	}

	public List<MatchBean> getMatchList(boolean sortByName) {
		//由于swipecard要对list进行删除操作，因此提供拷贝给外部，保证matchList不被影响
		List<MatchBean> sortedList = new ArrayList<MatchBean>(matchList);
		if (sortByName) {
			Collections.sort(sortedList, new MatchBeanComparator());
		}
		return sortedList;
	}

	private class MatchBeanComparator implements Comparator<MatchBean> {

		@Override
		public int compare(MatchBean lhs, MatchBean rhs) {

			return lhs.getNamePinYin().compareTo(rhs.getNamePinYin());
		}

	}

	/**
	 * Match card include name, place, level, court, total win/lose and best achievement.
	 * There is need to count total win/lose and best achievement
	 * @param list list should be ordered by time DESC
	 * @return
	 */
	public List<MatchBean> createMatchSwipeCardData(List<Record> list) {

		if (list == null || list.size() == 0) {
			return null;
		}

		matchList = new ArrayList<>();

		// 数据库系统更新后（v3.0），可以合并改名的赛事，不同名称但是matchId相同
		Map<String, Integer> idMap = new HashMap<>();
		PubDataProvider pubDataProvider = PubProviderHelper.getProvider();
		Map<Integer, MatchBean> map = new HashMap<>();
		CharacterParser parser = CharacterParser.getInstance();
		for (int i = 0; i < list.size(); i ++) {
			Record record = list.get(i);
			Integer matchId = idMap.get(record.getMatch());
			MatchBean bean;
			if (matchId == null) {
				MatchNameBean mnb = pubDataProvider.getMatchByName(record.getMatch());
				bean = map.get(mnb.getMatchId());
				if (bean == null) {
					bean = new MatchBean();
					bean.setPubId(mnb.getMatchId());
					bean.setCity(record.getCity());
					bean.setCountry(record.getMatchCountry());
					bean.setRegion(record.getRegion());
					bean.setName(record.getMatch());
					bean.setNamePinYin(parser.getSelling(record.getMatch()).toLowerCase(Locale.CHINA));
					bean.setLevel(record.getLevel());
					bean.setCourt(record.getCourt());
					bean.setRecordList(new ArrayList<Record>());

					map.put(mnb.getMatchId(), bean);
					idMap.put(record.getMatch(), mnb.getMatchId());
					matchList.add(bean);
				}
				else {
					idMap.put(record.getMatch(), mnb.getMatchId());
					// 出现改名的赛事，名称用最新的
					// 由于对record的查询已经是按日期降序排列了，所以第一次出现的名称就是最新的名称
//					bean.setName(record.getMatch());
				}
			}
			else {
				bean = map.get(matchId);
			}
			bean.getRecordList().add(record);
		}

		for (MatchBean bean:matchList) {
			calculateMatchBean(bean);
		}
		return matchList;
	}

	/**
	 * 统计总胜负和最佳成绩
	 * @param bean
	 */
	private void calculateMatchBean(MatchBean bean) {
		Log.d(TAG, "calculateMatchBean " + bean.getName());
		if (bean.getRecordList() != null) {
			int win = 0, lose = 0;
			String[] roundArray = Constants.RECORD_MATCH_ROUNDS;
			String best = null;
			StringBuffer bestYears = null;
			for (int i = 0; i < bean.getRecordList().size(); i ++) {
				Record record = bean.getRecordList().get(i);

				if (MultiUserManager.USER_DB_FLAG.equals(record.getWinner())) {
					win ++;
					//Final winner is champion，肯定是最佳
					if (roundArray[0].equals(record.getRound())) {
						if (!strChampion.equals(best)) {
							best = strChampion;
							bestYears = new StringBuffer(record.getStrDate().split("-")[0]);
						}
						else {
							bestYears.append(", ").append(record.getStrDate().split("-")[0]);
						}
					}
				}
				else {
					lose ++;

					//在某一轮输了，可能是最佳
					String round = record.getRound();
					if (round.equals(roundArray[0])) {//Final
						round = strRunnerup;
					}
					int result = compareMatchBest(roundArray, round, best);
					if (result > 0) {
						best = round;
						bestYears = new StringBuffer(record.getStrDate().split("-")[0]);
					}
					else if (result == 0) {
						bestYears.append(", ").append(record.getStrDate().split("-")[0]);
					}
				}
			}
			bean.setWin(win);
			bean.setLose(lose);

			if (best == null) {//戴维斯杯
				bean.setBest(roundArray[roundArray.length - 1]);//group
				bean.setBestYears("");
			}
			else {
				bean.setBest(best);
				bean.setBestYears(bestYears.toString());
			}
		}
	}

	/**
	 * 按照比赛轮次，比较更好的成绩
	 * @param roundArray 轮次数组Final/Semi Final...
	 * @param target 当前轮次
	 * @param best 累计统计中暂时最好的轮次
	 * @return 大于0则target为当前最好的轮次，等于0则并列为当前最好轮次
	 */
	private int compareMatchBest(String[] roundArray, String target, String best) {
		if (best == null) {
			return 1;
		}
		else {
			return getRoundLevel(roundArray, target) - getRoundLevel(roundArray, best);
		}
	}

	private int getRoundLevel(String[] roundArray, String round) {
		int level = 0;
		int max = roundArray.length;
		//Final区分冠亚军
		if (round.equals(strRunnerup)) {
			level = max - 1;
		}
		else if (round.equals(strChampion)) {
			level = max;
		}

		for (int i = 0; i < roundArray.length - 1; i ++) {//Final不计算
			if (round.equals(roundArray[i])) {
				level = max - 1 - i;
			}
		}
		return level;
	}

	public List<PlayerBean> getPlayerList(boolean sortByName) {
		//由于swipecard要对list进行删除操作，因此提供拷贝给外部，保证playerList不被影响
		List<PlayerBean> sortedList = new ArrayList<PlayerBean>(playerList);
		if (sortByName) {
			Collections.sort(sortedList, new PlayerBeanComparator());
		}
		return sortedList;
	}

	private class PlayerBeanComparator implements Comparator<PlayerBean> {

		@Override
		public int compare(PlayerBean lhs, PlayerBean rhs) {

			return lhs.getNamePinYin().compareTo(rhs.getNamePinYin());
		}

	}

	/**
	 * Player card include image, name, country, win/lose record and last record information
	 * There is need to count the win/lose record
	 * @param list as PlayerBean need set lastRecord parameter, be sure that list is ordered by time DESC
	 * @return
	 */
	public List<PlayerBean> createPlayerSwipeCardData(List<Record> list) {

		if (list == null || list.size() == 0) {
			return null;
		}

		playerList = new ArrayList<PlayerBean>();

		Map<String, PlayerBean> map = new HashMap<String, PlayerBean>();
		CharacterParser parser = CharacterParser.getInstance();
		for (int i = 0; i < list.size(); i ++) {
			Record record = list.get(i);
			PlayerBean bean = map.get(record.getCompetitor());
			if (bean == null) {
				bean = new PlayerBean();
				bean.setName(record.getCompetitor());
				bean.setNamePinYin(parser.getSelling(bean.getName()).toLowerCase(Locale.CHINA));
				bean.setCountry(record.getCptCountry());
				bean.setLastRecord(record);
				playerList.add(bean);
				map.put(bean.getName(), bean);
			}

			//如果是赛前退赛不算作h2h
			if (!record.getScore().equals(Constants.SCORE_RETIRE)) {
				if (record.getWinner().equals(MultiUserManager.USER_DB_FLAG)) {
					bean.setWin(bean.getWin() + 1);
				}
				else {
					bean.setLose(bean.getLose() + 1);
				}
			}
		}
		return playerList;
	}
	/*
	private List<Record> headList;
	private List<List<Record>> headChildList;
	private List<H2HBean> h2hList;
	
	public List<Record> getHeadList() {
		return headList;
	}

	public List<List<Record>> getHeadChildList() {
		return headChildList;
	}

	public List<H2HBean> getH2hList() {
		return h2hList;
	}
	
	public void getView7TimeLineTimeList() {
		
	}

	public void groupListByDate(List<Record> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		
		headList = new ArrayList<Record>();
		headChildList = new ArrayList<List<Record>>();
		
		Record titleMap = null;
		Record record = null;
		List<Record> childList = null;
		Record child = null;
		
		for (int i = 0; i < list.size(); i++) {
			record = list.get(i);
			if (i == 0) {
				titleMap = record;
				headList.add(record);
				childList = new ArrayList<Record>();
			}
			if (!record.getMatch().equals(titleMap.getMatch()) || !record.getStrDate().equals(titleMap.getStrDate())) {
				titleMap = record;
				headList.add(titleMap);
				headChildList.add(childList);
				childList = new ArrayList<Record>();
			}
			child = record;
			childList.add(0, child);
		}
		headChildList.add(childList);
		Collections.reverse(headChildList);
		Collections.reverse(headList);
		
	}

	public void groupListByPlayer(List<Record> list) {
		if (list == null || list.size() == 0) {
			return;
		}

		h2hList = new ArrayList<H2HBean>();

		Record record = null;
		Map<String, H2HBean> childMap = new HashMap<String, H2HBean>();
		H2HBean bean = null;
		
		CharacterParser parser = CharacterParser.getInstance();
		
		for (int i = 0; i < list.size(); i++) {
			record = list.get(i);
			bean = childMap.get(record.getCompetitor());
			if (bean == null) {
				bean = new H2HBean();
				bean.setCompetitor(record.getCompetitor());
				bean.setPinyin(parser.getSelling(record.getCompetitor()).toLowerCase(Locale.CHINA));
				bean.setRecordList(new ArrayList<Record>());
				childMap.put(record.getCompetitor(), bean);
				h2hList.add(bean);
			}
			bean.getRecordList().add(record);
		}
		
		for (H2HBean h2h:h2hList) {
			h2h.calculate();
		}
		
		Collections.sort(h2hList, new H2hBeanComparator());
	}
	
	private class H2hBeanComparator implements Comparator<H2HBean> {

		@Override
		public int compare(H2HBean lhs, H2HBean rhs) {
			
			return lhs.getPinyin().compareTo(rhs.getPinyin());
		}
		
	}
	*/

}
