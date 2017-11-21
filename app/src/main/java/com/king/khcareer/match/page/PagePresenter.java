package com.king.khcareer.match.page;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.record.RecordService;

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
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/21 14:14
 */
public class PagePresenter {

    private IPageView view;

    private MatchNameBean matchBean;

    private PubDataProvider pubDataProvider;

    private int win;

    private int lose;

    public PagePresenter(IPageView view) {
        this.view = view;
        pubDataProvider = new PubDataProvider();
    }

    public void loadMatchInfo(String matchName) {
        matchBean = pubDataProvider.getMatchByName(matchName);
        view.showMatchInfo(matchBean.getName(), matchBean.getMatchBean().getCountry(), matchBean.getMatchBean().getCity()
            , matchBean.getMatchBean().getLevel(), matchBean.getMatchBean().getCourt());
    }

    public void loadRecords(final String userId) {
        Observable.create(new ObservableOnSubscribe<List<Object>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Object>> e) throws Exception {
                /**
                 * 按照matchId来查询, 解决不同赛事名称但是是一站赛事的问题
                 */
                List<MatchNameBean> matches = pubDataProvider.getMatchNameList(matchBean.getMatchId());
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
                List<Record> recordList = service.queryByWhere(where, args);

                // 统计胜负场
                countWinLose(recordList);

                // 查出来的是时间升序，按时间降序排列
                Collections.reverse(recordList);

                List<Object> list = new ArrayList<>();
                Map<Integer, List<Record>> map = new HashMap<>();

                for (int i = 0; i < recordList.size(); i ++) {
                    Record record = recordList.get(i);
                    String strYear = record.getStrDate().split("-")[0];
                    int year = Integer.parseInt(strYear);
                    List<Record> child = map.get(year);
                    if (child == null) {
                        child = new ArrayList<>();
                        map.put(year, child);
                    }
                    child.add(record);
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
                        view.onRecordsLoaded(list, win, lose);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        view.showError("loadRecords error: " + throwable.getMessage());
                    }
                });
    }

    private PageTitleBean countTitle(int year, List<Record> list) {
        PageTitleBean bean = new PageTitleBean();
        for (Record record:list) {
            if (record.getRound().equals(Constants.RECORD_MATCH_ROUNDS[0])
                    && MultiUserManager.USER_DB_FLAG.equals(record.getWinner())) {
                bean.setWinner(true);
            }
        }
        bean.setYear(year);
        return bean;
    }

    private void countWinLose(List<Record> list) {
        win = 0;
        lose = 0;
        for (Record record:list) {
            //如果是赛前退赛不算胜负场
            if (record.getScore().equals(Constants.SCORE_RETIRE)) {
                continue;
            }
            if (MultiUserManager.USER_DB_FLAG.equals(record.getWinner())) {
                win ++;
            }
            else {
                lose ++;
            }
        }
    }
}
