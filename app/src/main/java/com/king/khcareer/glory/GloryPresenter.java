package com.king.khcareer.glory;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.glory.gs.GloryGsItem;
import com.king.khcareer.glory.gs.GloryMasterItem;
import com.king.khcareer.model.sql.player.GloryModel;
import com.king.khcareer.model.sql.player.bean.KeyValueCountBean;
import com.king.khcareer.model.sql.player.bean.MatchResultBean;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class GloryPresenter {
    
    private IGloryView gloryView;
    private GloryModel gloryModel;
    private PubDataProvider pubProvider;
    
    public GloryPresenter(IGloryView view) {
        gloryView = view;
        gloryModel = new GloryModel();
        pubProvider = new PubDataProvider();
    }
    
    public void loadDatas() {
        Observable.create(new Observable.OnSubscribe<GloryTitle>() {
            @Override
            public void call(Subscriber<? super GloryTitle> subscriber) {
                subscriber.onNext(loadGloryDatas());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GloryTitle>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(GloryTitle gloryTitle) {
                        gloryView.onGloryTitleLoaded(gloryTitle);
                    }
                });
    }

    public List<Record> loadMatchRecord(String match, String date) {
        return gloryModel.loadMatchRecord(match, date);
    }

    private GloryTitle loadGloryDatas() {
        GloryTitle data = new GloryTitle();
        data.setChampionList(gloryModel.getChampionRecords());
        data.setRunnerUpList(gloryModel.getRunnerupRecords());
        data.setTargetList(gloryModel.getTargetRecords(100, false));
        data.setTargetWinList(gloryModel.getTargetRecords(100, true));
        List<KeyValueCountBean> titleCounts = gloryModel.getTitleCountByLevel(false);
        parseCountData(data, titleCounts, false);
        titleCounts = gloryModel.getTitleCountByLevel(true);
        parseCountData(data, titleCounts, true);

        parseGsData(data);
        parseMasterData(data);
        return data;
    }

    private void parseGsData(GloryTitle data) {
        List<MatchResultBean> gsList = gloryModel.getGsResultList();
        Map<String, GloryGsItem> gsMap = new HashMap<>();
        List<GloryGsItem> gsGlories = new ArrayList<>();
        for (MatchResultBean bean:gsList) {
            String year = bean.getDate().split("-")[0];
            GloryGsItem item = gsMap.get(year);
            if (item == null) {
                item = new GloryGsItem();
                gsMap.put(year, item);
                item.setYear(Integer.parseInt(year));
                gsGlories.add(item);
            }
            if (bean.getMatch().equals("澳大利亚网球公开赛")) {
                if ("Winner".equals(bean.getResult())) {
                    item.setAo("冠军");
                }
                else {
                    item.setAo(Constants.getGsGloryForRound(bean.getResult(), false));
                }
            }
            else if (bean.getMatch().equals("法国网球公开赛")) {
                if ("Winner".equals(bean.getResult())) {
                    item.setFo("冠军");
                }
                else {
                    item.setFo(Constants.getGsGloryForRound(bean.getResult(), false));
                }
            }
            else if (bean.getMatch().equals("温布尔顿网球公开赛")) {
                if ("Winner".equals(bean.getResult())) {
                    item.setWo("冠军");
                }
                else {
                    item.setWo(Constants.getGsGloryForRound(bean.getResult(), false));
                }
            }
            else if (bean.getMatch().equals("美国网球公开赛")) {
                if ("Winner".equals(bean.getResult())) {
                    item.setUo("冠军");
                }
                else {
                    item.setUo(Constants.getGsGloryForRound(bean.getResult(), false));
                }
            }
        }
        Collections.sort(gsGlories, new Comparator<GloryGsItem>() {
            @Override
            public int compare(GloryGsItem left, GloryGsItem right) {
                return left.getYear() - right.getYear();
            }
        });
        data.setGsItemList(gsGlories);
    }

    private void parseMasterData(GloryTitle data) {
        List<MatchResultBean> gsList = gloryModel.getMasterResultList();
        Map<String, MatchNameBean> nameMap = pubProvider.getMatchMap();
        Map<String, GloryMasterItem> masterMap = new HashMap<>();
        List<GloryMasterItem> masterGlories = new ArrayList<>();
        for (MatchResultBean bean:gsList) {
            String year = bean.getDate().split("-")[0];
            GloryMasterItem item = masterMap.get(year);
            if (item == null) {
                item = new GloryMasterItem();
                item.setYear(Integer.parseInt(year));
                masterMap.put(year, item);
                masterGlories.add(item);
            }
            setMasterResult(item, nameMap.get(bean.getMatch()).getMatchId(), bean.getResult());
        }
        Collections.sort(masterGlories, new Comparator<GloryMasterItem>() {
            @Override
            public int compare(GloryMasterItem left, GloryMasterItem right) {
                return left.getYear() - right.getYear();
            }
        });
        data.setMasterItemList(masterGlories);
    }

    private void setMasterResult(GloryMasterItem item, int matchId, String result) {
        if (matchId == Constants.ATP_1000_PUBLIC_ID[0]) {
            item.setIw(Constants.getMasterGloryForRound(result));
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[1]) {
            item.setMiami(Constants.getMasterGloryForRound(result));
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[2]) {
            item.setMc(Constants.getMasterGloryForRound(result));
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[3]) {
            item.setMadrid(Constants.getMasterGloryForRound(result));
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[4]) {
            item.setRoma(Constants.getMasterGloryForRound(result));
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[5]) {
            item.setRc(Constants.getMasterGloryForRound(result));
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[6]) {
            item.setCicinati(Constants.getMasterGloryForRound(result));
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[7]) {
            item.setSh(Constants.getMasterGloryForRound(result));
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[8]) {
            item.setParis(Constants.getMasterGloryForRound(result));
        }
    }

    private void parseCountData(GloryTitle bean, List<KeyValueCountBean> countList, boolean isCurrentYear) {
        int sum = 0;
        for (KeyValueCountBean countBean:countList) {
            sum += countBean.getValue();
            if (Constants.RECORD_MATCH_LEVELS[0].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    bean.setYearGs(countBean.getValue());
                }
                else {
                    bean.setCareerGs(countBean.getValue());
                }
            }
            else if (Constants.RECORD_MATCH_LEVELS[1].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    bean.setYearMasterCup(countBean.getValue());
                }
                else {
                    bean.setCareerMasterCup(countBean.getValue());
                }
            }
            else if (Constants.RECORD_MATCH_LEVELS[2].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    bean.setYearAtp1000(countBean.getValue());
                }
                else {
                    bean.setCareerAtp1000(countBean.getValue());
                }
            }
            else if (Constants.RECORD_MATCH_LEVELS[3].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    bean.setYearAtp500(countBean.getValue());
                }
                else {
                    bean.setCareerAtp500(countBean.getValue());
                }
            }
            else if (Constants.RECORD_MATCH_LEVELS[4].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    bean.setYearAtp250(countBean.getValue());
                }
                else {
                    bean.setCareerAtp250(countBean.getValue());
                }
            }
            else if (Constants.RECORD_MATCH_LEVELS[6].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    bean.setYearOlympics(countBean.getValue());
                }
                else {
                    bean.setCareerOlympics(countBean.getValue());
                }
            }
        }
        if (isCurrentYear) {
            bean.setYearTitle(sum);
        }
        else {
            bean.setCareerTitle(sum);
        }
    }
}
