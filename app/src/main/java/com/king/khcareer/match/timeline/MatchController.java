package com.king.khcareer.match.timeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.match.gallery.UserMatchBean;
import com.king.khcareer.model.PubProviderHelper;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.record.RecordService;

/**
 * @author JingYang
 *
 */
public class MatchController {

	private List<Record> recordList;

	private List<List<Record>> expandList;

	private PubDataProvider pubDataProvider;

	private String userId;

	public MatchController() {
		pubDataProvider = PubProviderHelper.getProvider();
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserMatchBean getUserMatchBean(String matchName) {
		MatchNameBean nameBean = pubDataProvider.getMatchByName(matchName);
		UserMatchBean bean = new UserMatchBean();
		if (nameBean != null) {
			bean.setNameBean(nameBean);
			loadRecords(nameBean.getMatchId());
			bean.setRecordList(recordList);
			countMatch(bean);
		}
		return bean;
	}

	/**
	 * MatchActivity目前只需要统计胜负场
	 * @param bean
	 */
	private void countMatch(UserMatchBean bean) {
		if (recordList == null) {
			return;
		}
		for (int i = 0; i < recordList.size(); i ++) {
			// W/0不算作胜场
			if (!Constants.SCORE_RETIRE.equals(recordList.get(i).getScore())) {
				if (MultiUserManager.USER_DB_FLAG.equals(recordList.get(i).getWinner())) {
					bean.setWin(bean.getWin() + 1);
				}
				else {
					bean.setLose(bean.getLose() + 1);
				}
			}
		}
	}

	public List<Record> getRecordList() {
		return recordList;
	}

	public List<List<Record>> getExpandList() {
		return expandList;
	}

	/**
	 * 数据库系统更新后（v3.0），需要按照matchId来查询
	 * 解决不同赛事名称但是是一站赛事的问题
	 * @param matchId
     */
	public void loadRecords(int matchId) {
		List<MatchNameBean> matches = pubDataProvider.getMatchNameList(matchId);
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

		RecordService service;
		if (userId == null) {
			service = new RecordService();
		}
		else {
			service = new RecordService(MultiUserManager.getInstance().getUser(userId));
		}
		recordList = service.queryByWhere(where, args);

		if (recordList == null) {
			return;
		}

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
