package com.king.khcareer.glory;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.glory.gs.GloryGsItem;
import com.king.khcareer.model.sql.player.RecordDAOImp;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.player.interfc.RecordDAO;

import java.util.ArrayList;
import java.util.Calendar;
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
    private RecordDAO recordDAO;
    
    public GloryPresenter(IGloryView view) {
        gloryView = view;
        recordDAO = new RecordDAOImp();
    }
    
    public void loadDatas() {
        Observable.create(new Observable.OnSubscribe<GloryTitle>() {
            @Override
            public void call(Subscriber<? super GloryTitle> subscriber) {
                subscriber.onNext(loadGloryTitle());
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

    private GloryTitle loadGloryTitle() {
        GloryTitle bean = new GloryTitle();
        List<Record> championList = new ArrayList<>();
        List<Record> runnerUpList = new ArrayList<>();
        List<GloryGsItem> gsList = new ArrayList<>();
        bean.setGsItemList(gsList);
        bean.setChampionList(championList);
        bean.setRunnerUpList(runnerUpList);

        Map<String, GloryGsItem> gsMap = new HashMap<>();

        ArrayList<Record> list = recordDAO.queryAll();
        for (Record record:list) {
            // Final
            if (Constants.RECORD_MATCH_ROUNDS[0].equals(record.getRound())) {
                // is winner
                if (MultiUserManager.USER_DB_FLAG.equals(record.getWinner())) {
                    championList.add(record);
                    countChampion(bean, record);
                }
                else {
                    runnerUpList.add(record);
                }
            }
            
            // gs
            if (Constants.RECORD_MATCH_LEVELS[0].equals(record.getLevel())) {
                boolean isGsItem = false;
                // lose
                if (!MultiUserManager.USER_DB_FLAG.equals(record.getWinner())) {
                    isGsItem = true;
                }
                // Final winner
                else if (Constants.RECORD_MATCH_ROUNDS[0].equals(record.getRound())) {
                    isGsItem = true;
                }
                
                if (isGsItem) {
                    String year = record.getStrDate().split("-")[0];
                    GloryGsItem item = gsMap.get(year);
                    if (item == null) {
                        item = new GloryGsItem();
                        gsMap.put(year, item);
                        item.setYear(Integer.parseInt(year));
                    }
                    if (record.getMatchCountry().equals("澳大利亚")) {
                        item.setRecordAo(record);
                        item.setAo(Constants.getGsGloryForRound(record.getRound(), MultiUserManager.USER_DB_FLAG.equals(record.getWinner())));
                    }
                    else if (record.getMatchCountry().equals("法国")) {
                        item.setRecordFo(record);
                        item.setFo(Constants.getGsGloryForRound(record.getRound(), MultiUserManager.USER_DB_FLAG.equals(record.getWinner())));
                    }
                    else if (record.getMatchCountry().equals("英国")) {
                        item.setRecordWo(record);
                        item.setWo(Constants.getGsGloryForRound(record.getRound(), MultiUserManager.USER_DB_FLAG.equals(record.getWinner())));
                    }
                    else if (record.getMatchCountry().equals("美国")) {
                        item.setRecordUo(record);
                        item.setUo(Constants.getGsGloryForRound(record.getRound(), MultiUserManager.USER_DB_FLAG.equals(record.getWinner())));
                    }
                }
            }
        }
        
        for (GloryGsItem item:gsMap.values()) {
            gsList.add(item);
        }
        Collections.sort(gsList, new Comparator<GloryGsItem>() {
            @Override
            public int compare(GloryGsItem item1, GloryGsItem item2) {
                return item1.getYear() - item2.getYear();
            }
        });
        return bean;
    }

    private void countChampion(GloryTitle bean, Record record) {
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int year = Integer.parseInt(record.getStrDate().split("-")[0]);

        if (year == thisYear) {
            bean.setYearTitle(bean.getYearTitle() + 1);
        }
        bean.setCareerTitle(bean.getCareerTitle() + 1);

        if (Constants.RECORD_MATCH_LEVELS[0].equals(record.getLevel())) {
            bean.setCareerGs(bean.getCareerGs() + 1);
            if (year == thisYear) {
                bean.setYearGs(bean.getYearGs() + 1);
            }
        }
        else if (Constants.RECORD_MATCH_LEVELS[1].equals(record.getLevel())) {
            bean.setCareerMasterCup(bean.getCareerMasterCup() + 1);
            if (year == thisYear) {
                bean.setYearMasterCup(bean.getYearMasterCup() + 1);
            }
        }
        else if (Constants.RECORD_MATCH_LEVELS[2].equals(record.getLevel())) {
            bean.setCareerAtp1000(bean.getCareerAtp1000() + 1);
            if (year == thisYear) {
                bean.setYearAtp1000(bean.getYearAtp1000() + 1);
            }
        }
        else if (Constants.RECORD_MATCH_LEVELS[3].equals(record.getLevel())) {
            bean.setCareerAtp500(bean.getCareerAtp500() + 1);
            if (year == thisYear) {
                bean.setYearAtp500(bean.getYearAtp500() + 1);
            }
        }
        else if (Constants.RECORD_MATCH_LEVELS[4].equals(record.getLevel())) {
            bean.setCareerAtp250(bean.getCareerAtp250() + 1);
            if (year == thisYear) {
                bean.setYearAtp250(bean.getYearAtp250() + 1);
            }
        }
        else if (Constants.RECORD_MATCH_LEVELS[6].equals(record.getLevel())) {
            bean.setCareerOlympics(bean.getCareerOlympics() + 1);
            if (year == thisYear) {
                bean.setYearOlympics(bean.getYearOlympics() + 1);
            }
        }
    }

    public List<Record> loadMatchRecord(String match, String date) {
        List<Record> records = recordDAO.queryByWhere(
                "date_str = ? and match = ?", new String[]{date, match});
        return records;
    }

}
