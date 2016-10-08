package com.king.mytennis.glory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.king.mytennis.glory.adapter.AchieveLinearAdapter.RankViewData;
import com.king.mytennis.glory.model.GrandSlameItem;
import com.king.mytennis.glory.model.GrandSlame;
import com.king.mytennis.interfc.RecordDAO;
import com.king.mytennis.model.FileIO;
import com.king.mytennis.model.Record;
import com.king.mytennis.model.RecordDAOImp;
import com.king.mytennis.multiuser.MultiUser;
import com.king.mytennis.multiuser.MultiUserManager;

public class GloryController {

	private RecordDAO recordDAO;
	private List<HashMap<String, String>> titleList;
	private GrandSlame grandSlame;

	public GloryController() {
		grandSlame = new GrandSlame();
	}
	public List<HashMap<String, String>> loadTitles(Context context) {
		recordDAO = new RecordDAOImp(context);
		List<Record> records = recordDAO.queryByWhere(
				"round = ? and iswinner = ?", new String[]{"Final", MultiUserManager.USER_DB_FLAG});
		if (records != null && records.size() > 0) {
			titleList = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> map = null;
			Record record = null;
			for (int i = records.size() - 1; i > -1; i --) {
				record = records.get(i);
				map = new HashMap<String, String>();
				map.put("achieve_id", "" + record.getId());
				map.put("achieve_index", "" + (i + 1));
				map.put("achieve_date", record.getStrDate());
				map.put("achieve_glory", record.getMatch());
				map.put("achieve_level", record.getLevel());
				map.put("achieve_court", record.getCourt());
				titleList.add(map);
			}
			return titleList;
		}
		return null;
	}
	public List<HashMap<String, String>> filterTitle(String level, String court) {

		if (titleList != null && titleList.size() > 0) {
			if (level == null && court == null) {
				reArrangeIndex(titleList);
				return titleList;
			}
			boolean levelMatched, courtMatched = false;
			List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();

			int index = 1;
			for (HashMap<String, String> map:titleList) {
				levelMatched = level == null ? true:level.equals(map.get("achieve_level"));
				courtMatched = court == null ? true:court.equals(map.get("achieve_court"));
				if (levelMatched && courtMatched) {
					map.put("achieve_index", "" + index);
					index ++;
					list.add(map);
				}
			}

			reArrangeIndex(list);
			return list;
		}
		return null;
	}

	private void reArrangeIndex(List<HashMap<String, String>> list) {
		HashMap<String, String> map = null;
		for (int i = 0; i < list.size(); i ++) {
			map = list.get(i);
			map.put("achieve_index", "" + (list.size() - i));
		}
	}
	public List<HashMap<String, String>> loadRunnerUps(Context context) {
		recordDAO = new RecordDAOImp(context);
		List<Record> records = recordDAO.queryByWhere(
				"round = ? and iswinner != ?", new String[]{"Final", MultiUserManager.USER_DB_FLAG});
		if (records != null && records.size() > 0) {
			titleList = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> map = null;
			Record record = null;
			for (int i = records.size() - 1; i > -1; i --) {
				record = records.get(i);
				map = new HashMap<String, String>();
				map.put("achieve_id", "" + record.getId());
				map.put("achieve_index", "" + (i + 1));
				map.put("achieve_date", record.getStrDate());
				map.put("achieve_glory", record.getMatch());
				map.put("achieve_level", record.getLevel());
				map.put("achieve_court", record.getCourt());
				map.put("achieve_winner", record.getCompetitor());
				titleList.add(map);
			}
			return titleList;
		}
		return null;
	}
	public List<HashMap<String, String>> getSeasonScoreList() {
		List<HashMap<String, String>> list = new FileIO().readSeasonScoreList();

		if (list == null) {
			list = createSeasonScoreList();
		}
		return list;
	}

	public void saveSeasonScoreList(List<HashMap<String, String>> list) {
		new FileIO().saveSeasonScoreList(list);
	}

	public HashMap<String, Integer> getRankData() {
		HashMap<String, Integer> data = null;
		data = new FileIO().readRankData();
		return data;
	}

	public List<GrandSlameItem> getGSData() {
		MultiUser user = MultiUserManager.getInstance().getCurrentUser();
		return grandSlame.getGrandSlameItems(user);
	}

	public List<GrandSlameItem> loadGloryGrandSlame() {
		List<GrandSlameItem> data = null;
		grandSlame.readData();
		MultiUser user = MultiUserManager.getInstance().getCurrentUser();
		data = grandSlame.getGrandSlameItems(user);
		return data;
	}

	public void saveGloryGrandSlame() {
		grandSlame.saveData();
	}

	public void saveRankData(RankViewData data) {
		new FileIO().saveRankData(data);
	}

	public List<Record> loadMatchRecord(Context context, String match, String date) {
		recordDAO = new RecordDAOImp(context);
		List<Record> records = recordDAO.queryByWhere(
				"date_str = ? and match = ?", new String[]{date, match});
		return records;
	}

	/**
	 * 文件未创建过就先随便加些数据，再编辑就行了
	 * @return
	 */
	public List<HashMap<String, String>> createSeasonScoreList() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("season_score_match", "澳大利亚网球公开赛");
		map.put("season_score_score", "0");
		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "迪拜免税网球冠军赛");
//		map.put("season_score_score", "500");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "印第安维尔斯大师赛");
//		map.put("season_score_score", "180");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "迈阿密大师赛");
//		map.put("season_score_score", "1000");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "蒙特卡洛大师赛");
//		map.put("season_score_score", "600");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "马德里大师赛");
//		map.put("season_score_score", "1000");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "罗马大师赛");
//		map.put("season_score_score", "360");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "法国网球公开赛");
//		map.put("season_score_score", "1200");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "女王杯");
//		map.put("season_score_score", "250");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "温布尔顿网球公开赛");
//		map.put("season_score_score", "180");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "亚特兰大网球冠军赛");
//		map.put("season_score_score", "250");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "罗杰斯杯");
//		map.put("season_score_score", "90");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "西南财团公开赛");
//		map.put("season_score_score", "600");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "美国网球公开赛");
//		map.put("season_score_score", "2000");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "中国网球公开赛");
//		map.put("season_score_score", "500");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "上海大师赛");
//		map.put("season_score_score", "360");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "瓦伦西亚公开赛");
//		map.put("season_score_score", "500");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "巴黎大师赛");
//		map.put("season_score_score", "180");
//		list.add(map);
//		map = new HashMap<String, String>();
//		map.put("season_score_match", "伦敦大师杯");
//		map.put("season_score_score", "400");
//		list.add(map);

		new FileIO().saveSeasonScoreList(list);
		return list;
	}
}
