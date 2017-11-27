package com.king.khcareer.home.k4;

import com.king.khcareer.match.gallery.UserMatchBean;
import com.king.khcareer.match.gallery.UserMatchPresenter;
import com.king.khcareer.model.PubProviderHelper;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.player.observe.RecordObserver;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.record.RecordService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/3 0003.
 */

public class HomePresenter {

    private IHomeView homeView;
    private UserMatchPresenter userMatchPresenter;

    public HomePresenter(IHomeView homeView) {
        this.homeView = homeView;
        userMatchPresenter = new UserMatchPresenter(homeView.getContext());
    }

    public void loadHomeDatas() {
        Observable.create(new ObservableOnSubscribe<HomeData>() {
            @Override
            public void subscribe(ObservableEmitter<HomeData> e) throws Exception {
                HomeData data = getHomeData();

                e.onNext(data);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HomeData>() {
                    @Override
                    public void accept(HomeData homeData) throws Exception {
                        homeView.onHomeDataLoaded(homeData);
                        createHomeObserver();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private void createHomeObserver() {
        RecordObserver.addObserver(recordObserver);
    }

    private Observer<List<Record>> recordObserver = new Observer<List<Record>>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(List<Record> records) {
            updateRecordsRelated(records);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    private void updateRecordsRelated(final List<Record> list) {
        Observable.create(new ObservableOnSubscribe<List<?>>() {

            @Override
            public void subscribe(ObservableEmitter<List<?>> e) throws Exception {

                Collections.reverse(list);

                // 通知record list变化
                e.onNext(list);

                // 通知最近10个交手的选手变化
                List<PlayerBean> playerList = getLatestPlayers(list);
                e.onNext(playerList);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<?>>() {
                    @Override
                    public void accept(List<?> list) throws Exception {
//                        Method method = list.getClass().getMethod("get", Integer.TYPE);
//                        Class cls = method.getReturnType();
//                        if (cls.equals(Record.class)) {
//                            homeView.onRecordUpdated((List<Record>) list);
//                        }
//                        else if (cls.equals(PlayerBean.class)) {
//                            homeView.onPlayerUpdated((List<PlayerBean>) list);
//                        }
                        if (list.size() > 0) {
                            if (list.get(0) instanceof Record) {
                                homeView.onRecordUpdated((List<Record>) list);
                            }
                            else if (list.get(0) instanceof PlayerBean) {
                                homeView.onPlayerUpdated((List<PlayerBean>) list);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private List<PlayerBean> getLatestPlayers(List<Record> list) {
        int count = 0;
        List<PlayerBean> playerList = new ArrayList<>();
        Map<String, PlayerBean> map = new HashMap<>();
        for (Record record:list) {
            PlayerBean bean = PubProviderHelper.getProvider().getPlayerByChnName(record.getCompetitor());
            if (map.get(bean.getNameChn()) == null) {
                map.put(bean.getNameChn(), bean);
                playerList.add(bean);
                count ++;
            }
            if (count == 10) {
                break;
            }
        }
        return playerList;
    }

    public HomeData getHomeData() {
        HomeData data = new HomeData();
        List<Record> list = new RecordService().queryAll();
        Collections.reverse(list);
        data.setRecordList(list);
        data.setMatchList(userMatchPresenter.getMatchList(list));

        // 获取最近10个交手的选手
        data.setPlayerList(getLatestPlayers(list));

        data.setRecordMatch(list.get(0).getMatch());
        data.setRecordRound(list.get(0).getRound());
        data.setRecordCourt(list.get(0).getCourt());
        data.setRecordCountry(list.get(0).getMatchCountry());
        return data;
    }

    public int findLatestWeekItem(List<UserMatchBean> matchList) {
        return userMatchPresenter.findLatestWeekItem(matchList);
    }

    public void destroy() {
        RecordObserver.removeObserver(recordObserver);
    }
}
