package com.king.khcareer.model.sql.player.observe;

import com.king.khcareer.model.sql.player.RecordDAOImp;
import com.king.khcareer.model.sql.player.bean.Record;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述: record表的响应式监听
 * Home/Record list 界面涉及注册Observer
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/27 15:42
 */
public class RecordObserver {

    private static List<Observer<List<Record>>> observers = new ArrayList<>();

    public static void onRecordUpdate() {
        Observable<List<Record>> queryObs = getQuery();
        for (Observer<List<Record>> observer:observers) {
            queryObs.subscribe(observer);
        }
    }

    public static void addObserver(Observer<List<Record>> observer) {
        observers.add(observer);
    }

    public static void removeObserver(Observer<List<Record>> observer) {
        observers.remove(observer);
    }

    /**
     * 查询全部记录
     * @return
     */
    public static Observable<List<Record>> getQuery() {
        return Observable.create(new ObservableOnSubscribe<List<Record>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Record>> e) throws Exception {
                e.onNext(new RecordDAOImp().queryAll());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }
}
