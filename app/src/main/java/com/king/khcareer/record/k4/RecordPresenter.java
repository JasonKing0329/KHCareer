package com.king.khcareer.record.k4;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.player.RecordDAOImp;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.player.interfc.RecordDAO;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
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
 * <p/>创建时间: 2017/4/21 17:05
 */
public class RecordPresenter {

    private IRecordView recordView;

    private ArrayList<Record> recordList;

    public RecordPresenter(IRecordView recordView) {
        this.recordView = recordView;
    }

    /**
     * 按照当前的recordList组装3级数据
     * @param recordList
     */
    public void loadRecordDatas(ArrayList<Record> recordList) {
        this.recordList = recordList;
        executeLoad();
    }

    /**
     * 加载全部的record，并组装3级数据
     */
    public void loadRecordDatas() {
        recordList = null;
        executeLoad();
    }

    private void executeLoad() {
        Observable.create(new ObservableOnSubscribe<RecordPageData>() {
            @Override
            public void subscribe(ObservableEmitter<RecordPageData> e) throws Exception {
                RecordPageData headerList = createHeaderList();
                e.onNext(headerList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RecordPageData>() {
                    @Override
                    public void accept(RecordPageData recordPageData) throws Exception {
                        recordView.onRecordDataLoaded(recordPageData);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private RecordPageData createHeaderList() {
        RecordPageData data = new RecordPageData();
        
        RecordDAO dao = new RecordDAOImp();
        if (recordList == null) {
            recordList = dao.queryAll();
            Collections.reverse(recordList);
        }
        data.setRecordList(recordList);

        List<YearItem> yearList = new ArrayList<>();
        data.setYearList(yearList);
        Map<String, YearItem> yearMap = new HashMap<>();
        Map<String, HeaderItem> headerMap = new HashMap<>();

        int nCareer = 0;
        int nCareerWin = 0;
        int nYear = 0;
        int nYearWin = 0;
        for (int i = 0; i < recordList.size(); i ++) {
            Record record = recordList.get(i);
            // format Year and Header and Item
            String keyYear = record.getStrDate().split("-")[0];
            YearItem yearItem = yearMap.get(keyYear);
            if (yearItem == null) {
                yearItem = new YearItem();
                yearItem.setYear(keyYear);
                yearItem.setChildItemList(new ArrayList<HeaderItem>());
                yearMap.put(keyYear, yearItem);
                yearList.add(yearItem);
            }
            List<HeaderItem> headerList = yearItem.getChildItemList();

            String keyHeader = record.getMatch() + "-" + record.getStrDate();
            HeaderItem item = headerMap.get(keyHeader);
            if (item == null) {
                item = new HeaderItem();
                item.setRecord(record);
                item.setChildItemList(new ArrayList<RecordItem>());
                headerMap.put(keyHeader, item);
                headerList.add(item);
            }
            RecordItem rItem = new RecordItem();
            rItem.setRecord(record);
            item.getChildItemList().add(rItem);

            int year = Calendar.getInstance().get(Calendar.YEAR);
            // count win lose
            // W/0不算作胜负场
            if (!Constants.SCORE_RETIRE.equals(record.getScore())) {
                nCareer ++;
                if (MultiUserManager.USER_DB_FLAG.equals(record.getWinner())) {
                    nCareerWin ++;
                    yearItem.setWin(yearItem.getWin() + 1);
                }
                else {
                    yearItem.setLose(yearItem.getLose() + 1);
                }

                if (year == Integer.parseInt(record.getStrDate().split("-")[0])) {
                    nYear ++;
                    if (MultiUserManager.USER_DB_FLAG.equals(record.getWinner())) {
                        nYearWin ++;
                    }
                }
            }
        }
        data.setCareerWin(nCareerWin);
        data.setCareerLose(nCareer - nCareerWin);
        data.setYearWin(nYearWin);
        data.setYearLose(nYear - nYearWin);
        DecimalFormat format = new DecimalFormat("##0.0");
        data.setCareerRate(format.format((float) nCareerWin/ (float) nCareer * 100) + "%");
        data.setYearRate(format.format((float) nYearWin/ (float) nYear * 100) + "%");
        return  data;
    }
}
