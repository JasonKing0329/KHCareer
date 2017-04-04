package com.king.khcareer;

import com.king.mytennis.match.UserMatchBean;
import com.king.mytennis.match.UserMatchPresenter;
import com.king.mytennis.model.Record;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.score.IScorePageView;
import com.king.mytennis.score.ScorePageData;
import com.king.mytennis.score.ScorePresenter;
import com.king.mytennis.service.RecordService;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/3 0003.
 */

public class HomePresenter {

    private IHomeView homeView;
    private UserMatchPresenter userMatchPresenter;
    private ScorePresenter scorePrensenter;

    public HomePresenter(IHomeView homeView) {
        this.homeView = homeView;
        userMatchPresenter = new UserMatchPresenter(homeView.getContext());
        scorePrensenter = new ScorePresenter(homeView.getContext());
    }

    public void loadHomeDatas() {
        Observable.create(new Observable.OnSubscribe<HomeData>() {
            @Override
            public void call(Subscriber<? super HomeData> subscriber) {

                HomeData data = getHomeData();

                subscriber.onNext(data);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HomeData data) {
                        homeView.onHomeDataLoaded(data);
                    }
                });

        load52WeekScore();
    }

    private void load52WeekScore() {
        scorePrensenter = new ScorePresenter(homeView.getContext());
        scorePrensenter.setScorePageView(new IScorePageView() {
            @Override
            public void onPageDataLoaded(ScorePageData data) {
                homeView.onScoreLoaded(data.getCountScore(), scorePrensenter.loadRank().getRank());
            }
        });
        scorePrensenter.query52WeekRecords();
    }

    public HomeData getHomeData() {
        HomeData data = new HomeData();
        List<Record> list = new RecordService(homeView.getContext()).queryAll();
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
