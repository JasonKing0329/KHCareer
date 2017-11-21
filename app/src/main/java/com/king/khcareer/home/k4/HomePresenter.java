package com.king.khcareer.home.k4;

import com.king.khcareer.match.gallery.UserMatchBean;
import com.king.khcareer.match.gallery.UserMatchPresenter;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.record.RecordService;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public HomeData getHomeData() {
        HomeData data = new HomeData();
        List<Record> list = new RecordService().queryAll();
        Collections.reverse(list);
        data.setRecordList(list);
        data.setMatchList(userMatchPresenter.getMatchList(list));

        // 统计最近3个交手的选手（可重复），最近一条交手记录
        if (list.size() > 0) {
            data.setPlayerName1(list.get(0).getCompetitor());
            data.setWinner1(MultiUserManager.USER_DB_FLAG.equals(list.get(0).getWinner()));
        }
        if (list.size() > 1) {
            data.setPlayerName2(list.get(1).getCompetitor());
            data.setWinner2(MultiUserManager.USER_DB_FLAG.equals(list.get(1).getWinner()));
        }
        if (list.size() > 2) {
            data.setPlayerName3(list.get(2).getCompetitor());
            data.setWinner3(MultiUserManager.USER_DB_FLAG.equals(list.get(2).getWinner()));
        }
        data.setRecordMatch(list.get(0).getMatch());
        data.setRecordRound(list.get(0).getRound());
        data.setRecordCourt(list.get(0).getCourt());
        data.setRecordCountry(list.get(0).getMatchCountry());
        return data;
    }

    public int findLatestWeekItem(List<UserMatchBean> matchList) {
        return userMatchPresenter.findLatestWeekItem(matchList);
    }
}
