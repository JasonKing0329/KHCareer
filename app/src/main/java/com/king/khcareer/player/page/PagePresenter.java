package com.king.khcareer.player.page;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.player.RecordDAOImp;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.utils.ConstellationUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述: handle operations of player page activity and fragment
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/20 15:48
 */
public class PagePresenter {

    private final String TAB_ALL = "全部";

    private PubDataProvider pubDataProvider;

    private IPageView view;

    private PlayerBean mPlayerBean;

    private List<Record> recordList;

    public PagePresenter(IPageView view) {
        this.view = view;
        pubDataProvider = new PubDataProvider();
    }


    public PlayerBean loadPlayerByChnName(String name) {
        mPlayerBean = pubDataProvider.getPlayerByChnName(name);
        return mPlayerBean;
    }

    public void loadPlayerInfor() {
        String constel = null;
        try {
            constel = ConstellationUtil.getConstellationChn(mPlayerBean.getBirthday());
        } catch (ConstellationUtil.ConstellationParseException e) {
            e.printStackTrace();
        }
        String info = mPlayerBean.getNameChn() + "，" + mPlayerBean.getBirthday() + "，" + constel;
        view.showPlayerInfo(mPlayerBean.getNameEng(), info
                , ImageFactory.getPlayerHeadPath(mPlayerBean.getNameChn())
                , mPlayerBean.getCountry());
    }

    public void loadRecords(final String userId) {
        Observable.create(new ObservableOnSubscribe<List<TabBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<TabBean>> e) throws Exception {

                MultiUser user;
                if (userId == null) {
                    user = MultiUserManager.getInstance().getCurrentUser();
                }
                else {
                    user = MultiUserManager.getInstance().getUser(userId);
                }
                recordList = new RecordDAOImp(user).queryByCompetitor(mPlayerBean.getNameChn());

                // 查出来的是时间升序，按时间降序排列
                Collections.reverse(recordList);

                List<TabBean> tabList = new ArrayList<>();

                for (int i = 0; i < Constants.RECORD_MATCH_COURTS.length; i ++) {
                    TabBean tab = new TabBean();
                    tab.name = Constants.RECORD_MATCH_COURTS[i];
                    tabList.add(tab);
                }

                for (int i = 0; i < recordList.size(); i ++) {
                    Record record = recordList.get(i);
                    // count h2h by court
                    if (record.getCourt().equals(Constants.RECORD_MATCH_COURTS[0])) {
                        tabList.get(0).total ++;
                        //如果是赛前退赛不算作h2h
                        if (record.getScore().equals(Constants.SCORE_RETIRE)) {
                            continue;
                        }
                        else {
                            if (record.getCompetitor().equals(record.getWinner())) {
                                tabList.get(0).lose ++;
                            }
                            else {
                                tabList.get(0).win ++;
                            }
                        }
                    }
                    else if (record.getCourt().equals(Constants.RECORD_MATCH_COURTS[1])) {
                        tabList.get(1).total ++;
                        //如果是赛前退赛不算作h2h
                        if (record.getScore().equals(Constants.SCORE_RETIRE)) {
                            continue;
                        }
                        else {
                            if (record.getCompetitor().equals(record.getWinner())) {
                                tabList.get(1).lose ++;
                            }
                            else {
                                tabList.get(1).win ++;
                            }
                        }
                    }
                    else if (record.getCourt().equals(Constants.RECORD_MATCH_COURTS[3])) {
                        tabList.get(3).total ++;
                        //如果是赛前退赛不算作h2h
                        if (record.getScore().equals(Constants.SCORE_RETIRE)) {
                            continue;
                        }
                        else {
                            if (record.getCompetitor().equals(record.getWinner())) {
                                tabList.get(3).lose ++;
                            }
                            else {
                                tabList.get(3).win ++;
                            }
                        }
                    }
                    else if (record.getCourt().equals(Constants.RECORD_MATCH_COURTS[2])) {
                        tabList.get(2).total ++;
                        //如果是赛前退赛不算作h2h
                        if (record.getScore().equals(Constants.SCORE_RETIRE)) {
                            continue;
                        }
                        else {
                            if (record.getCompetitor().equals(record.getWinner())) {
                                tabList.get(2).lose ++;
                            }
                            else {
                                tabList.get(2).win ++;
                            }
                        }
                    }
                }

                TabBean tabAll = new TabBean();
                tabAll.name = TAB_ALL;
                // 如果没有记录就不显示这个tab
                for (int i = tabList.size() - 1; i >= 0; i --) {
                    tabAll.win += tabList.get(i).win;
                    tabAll.lose += tabList.get(i).lose;
                    tabAll.total += tabList.get(i).total;
                    if (tabList.get(i).total == 0) {
                        tabList.remove(i);
                    }
                }
                tabList.add(0, tabAll);

                e.onNext(tabList);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<TabBean>>() {
                    @Override
                    public void accept(List<TabBean> list) throws Exception {
                        view.onTabLoaded(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        view.showError("loadRecords error: " + throwable.getMessage());
                    }
                });
    }

    public void createRecords(final String tabName, final IPageCallback callback) {
        Observable.create(new ObservableOnSubscribe<List<Object>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Object>> e) throws Exception {
                List<Object> list = new ArrayList<>();
                Map<Integer, List<Record>> map = new HashMap<>();
                for (int i = 0; i < recordList.size(); i ++) {
                    Record record = recordList.get(i);
                    if (tabName.equals(TAB_ALL) || tabName.equals(record.getCourt())) {
                        String strYear = record.getStrDate().split("-")[0];
                        int year = Integer.parseInt(strYear);
                        List<Record> child = map.get(year);
                        if (child == null) {
                            child = new ArrayList<>();
                            map.put(year, child);
                        }
                        child.add(record);
                    }
                }

                // 按year降序
                Iterator<Integer> it = map.keySet().iterator();
                List<Integer> yearList = new ArrayList<>();
                while (it.hasNext()) {
                    yearList.add(it.next());
                }
                Collections.sort(yearList);
                Collections.reverse(yearList);

                for (Integer year:yearList) {
                    List<Record> records = map.get(year);
                    PageTitleBean title = countTitle(year, records);
                    list.add(title);
                    list.addAll(records);
                }

                e.onNext(list);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Object>>() {
                    @Override
                    public void accept(List<Object> list) throws Exception {
                        callback.onDataLoaded(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private PageTitleBean countTitle(int year, List<Record> records) {
        PageTitleBean bean = new PageTitleBean();
        int win = 0, lose = 0;
        for (int i = 0; i < records.size(); i ++) {
            Record record = records.get(i);
            //如果是赛前退赛不算作h2h
            if (record.getScore().equals(Constants.SCORE_RETIRE)) {
                continue;
            }
            else {
                if (record.getCompetitor().equals(record.getWinner())) {
                    lose ++;
                } else {
                    win ++;
                }
            }
        }
        bean.setYear(year);
        bean.setWin(win);
        bean.setLose(lose);
        return bean;
    }
}
