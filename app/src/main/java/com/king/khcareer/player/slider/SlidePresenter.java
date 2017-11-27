package com.king.khcareer.player.slider;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.model.PubProviderHelper;
import com.king.khcareer.model.sql.player.H2hModel;
import com.king.khcareer.model.sql.player.RecordDAOImp;
import com.king.khcareer.model.sql.player.bean.H2hParentBean;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.player.page.PageTitleBean;
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
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/24 14:56
 */
public class SlidePresenter {

    private ISlideView view;
    private PubDataProvider pubDataProvider;
    private H2hModel h2hModel;

    private Disposable recordDisposable;

    public SlidePresenter(ISlideView view) {
        this.view = view;
        pubDataProvider = PubProviderHelper.getProvider();
        h2hModel = new H2hModel();
    }

    public void loadPlayers() {
        Observable.create(new ObservableOnSubscribe<List<PlayerBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<PlayerBean>> e) throws Exception {
                List<Record> list = new RecordService().queryAll();
                Collections.reverse(list);
                Map<String, PlayerBean> map = new HashMap<>();
                List<PlayerBean> playerList = new ArrayList<>();

                for (int i = 0; i < list.size(); i ++) {
                    Record record = list.get(i);
                    PlayerBean bean = pubDataProvider.getPlayerByChnName(record.getCompetitor());
                    if (bean == null) {
                        continue;
                    }
                    if (map.get(bean.getNameChn()) == null) {
                        map.put(bean.getNameChn(), bean);
                        playerList.add(bean);
                    }

                    // 全部加载完耗时比较长，先通知10个已加载完成
                    if (i == 10) {
                        e.onNext(playerList);
                    }
                }
                e.onNext(playerList);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<PlayerBean>>() {
                    @Override
                    public void accept(List<PlayerBean> playerList) throws Exception {
                        view.onPlayerLoaded(playerList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        view.onPlayerLoadFailed(throwable.getMessage());
                    }
                });
    }

    public H2hParentBean queryH2h(PlayerBean bean) {
        return h2hModel.queryH2hBean(bean.getNameChn());
    }

    public void loadRecords(final String name) {
        if (recordDisposable != null) {
            recordDisposable.dispose();
        }
        recordDisposable = Observable.create(new ObservableOnSubscribe<List<Object>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Object>> e) throws Exception {
                List<Object> list = new ArrayList<>();
                List<Record> recordList = new RecordDAOImp().queryByCompetitor(name);
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
                        view.onRecordLoaded(list);
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
