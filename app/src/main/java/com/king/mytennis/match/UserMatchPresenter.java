package com.king.mytennis.match;

import android.content.Context;

import com.king.mytennis.interfc.RecordDAO;
import com.king.mytennis.model.Constants;
import com.king.mytennis.model.Record;
import com.king.mytennis.model.RecordDAOImp;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.pubdata.PubDataProvider;
import com.king.mytennis.pubdata.bean.MatchNameBean;
import com.king.mytennis.view.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/15 10:12
 */
public class UserMatchPresenter {

    private PubDataProvider pubDataProvider;
    private RecordDAO recordDao;

    private String strChampion;
    private String strRunnerup;

    public UserMatchPresenter(Context context) {
        pubDataProvider = new PubDataProvider();
        recordDao = new RecordDAOImp(context);
        strChampion = context.getString(R.string.champion);
        strRunnerup = context.getString(R.string.runnerup);
    }

    public List<UserMatchBean> getMatchList() {
        return getMatchList(null);
    }
    /**
     * 确立对应关系：
     * 1. matchId-UserMatchBean
     *    最终的list是按照matchId的唯一性组成的
     * 2. matchId-names
     *    matchId唯一，names不唯一，为1对多的关系
     * 3. name-MatchNameBean
     *    对于Record，唯一的关联key是name
     * @return
     */
    public List<UserMatchBean> getMatchList(List<Record> recordList) {
        List<UserMatchBean> list = new ArrayList<>();

        Map<Integer, UserMatchBean> idBeanMap = new HashMap<>();
        Map<Integer, List<String>> idNamesMap = new HashMap<>();
        Map<String, MatchNameBean> nameBeanMap = new HashMap<>();

        if (recordList == null) {
            recordList = recordDao.queryAll();
        }
        // 查询出所有记录，按照matchId进行分类
        for (int i = 0; i < recordList.size(); i ++) {
            // Record中的唯一外键
            String name = recordList.get(i).getMatch();

            // 获取/生成MatchNameBean
            MatchNameBean nameBean = nameBeanMap.get(name);
            if (nameBean == null) {// 首次查询MatchNameBean
                nameBean = pubDataProvider.getMatchByName(name);
                nameBeanMap.put(name, nameBean);
            }

            // 记录idNamesMap
            List<String> names = idNamesMap.get(nameBean.getMatchId());
            if (names == null) {
                names = new ArrayList<>();
                idNamesMap.put(nameBean.getMatchId(), names);
            }
            if (!names.contains(name)) {
                names.add(name);
            }

            // 获取/生成UserMatchBean
            UserMatchBean bean = idBeanMap.get(nameBean.getMatchId());
            if (bean == null) {
                bean = new UserMatchBean();
                idBeanMap.put(nameBean.getMatchId(), bean);
                if (bean.getRecordList() == null) {
                    bean.setRecordList(new ArrayList<Record>());
                }
                list.add(bean);
            }
            bean.getRecordList().add(recordList.get(i));
            // 以最新的赛事名称为主，所以永远记录最新的
            bean.setNameBean(nameBean);
        }

        // 统计胜负场
        for (UserMatchBean bean:list) {
            calculateMatch(bean);
        }

        // 按照week进行排序
        Collections.sort(list, new WeekComparator());

        return list;
    }

    /**
     * 统计总胜负和最佳成绩
     * @param bean
     */
    private void calculateMatch(UserMatchBean bean) {
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

    /**
     * 找到与当前周数最靠近的赛事，前后出现等间隔的已前者优先（跨周数赛事）
     * @param matchList 已经是按周数升序排列了
     * @return
     */
    public int findLatestWeekItem(List<UserMatchBean> matchList) {

        int position = 0;
        // 最小间隔
        int minSpace = Integer.MAX_VALUE;

        // 当前周
        int curWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        for (int i = 0; i < matchList.size(); i ++) {
            int week = matchList.get(i).getNameBean().getMatchBean().getWeek();
            int space = Math.abs(week - curWeek);
            if (space < minSpace) {
                minSpace = space;
                position = i;
            }
            else if (space == minSpace) {
                // 等于的情况以前者优先
                continue;
            }
            // 由于已经是按周数的升序排列了，所以当出现大于等于最小间隔的情况时，可以终止遍历
            else {
                break;
            }
        }
        return position;
    }

    private class WeekComparator implements Comparator<UserMatchBean> {

        @Override
        public int compare(UserMatchBean beanL, UserMatchBean beanR) {
            return beanL.getNameBean().getMatchBean().getWeek() - beanR.getNameBean().getMatchBean().getWeek();
        }
    }
}
