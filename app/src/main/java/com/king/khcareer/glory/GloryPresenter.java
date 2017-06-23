package com.king.khcareer.glory;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.glory.bean.GloryTitle;
import com.king.khcareer.glory.gs.GloryGsItem;
import com.king.khcareer.glory.gs.GloryMasterItem;
import com.king.khcareer.glory.title.HeaderBean;
import com.king.khcareer.glory.title.HeaderItem;
import com.king.khcareer.glory.title.SubItem;
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
        data.setTargetList(gloryModel.getTargetRecords(Constants.GLORY_TARGET_FACTOR, false));
        data.setTargetWinList(gloryModel.getTargetRecords(Constants.GLORY_TARGET_FACTOR, true));
        data.setCareerMatch(gloryModel.getMatchNumber(false, false));
        data.setCareerWin(gloryModel.getMatchNumber(true, false));
        data.setYearMatch(gloryModel.getMatchNumber(false, true));
        data.setYearWin(gloryModel.getMatchNumber(true, true));

        // count champion params
        data.setChampionTitle(data.new Title());
        boolean isWinner = true;
        List<KeyValueCountBean> titleCounts = gloryModel.getTitleCountByLevel(false, isWinner);
        parseCountDataByLevel(data.getChampionTitle(), titleCounts, false);
        titleCounts = gloryModel.getTitleCountByLevel(true, isWinner);
        parseCountDataByLevel(data.getChampionTitle(), titleCounts, true);
        titleCounts = gloryModel.getTitleCountByCourt(false, isWinner);
        parseCountDataByCourt(data.getChampionTitle(), titleCounts, false);
        titleCounts = gloryModel.getTitleCountByCourt(true, isWinner);
        parseCountDataByCourt(data.getChampionTitle(), titleCounts, true);
        // count runner-up params
        isWinner = false;
        data.setRunnerupTitle(data.new Title());
        titleCounts = gloryModel.getTitleCountByLevel(false, isWinner);
        parseCountDataByLevel(data.getRunnerupTitle(), titleCounts, false);
        titleCounts = gloryModel.getTitleCountByLevel(true, isWinner);
        parseCountDataByLevel(data.getRunnerupTitle(), titleCounts, true);
        titleCounts = gloryModel.getTitleCountByCourt(false, isWinner);
        parseCountDataByCourt(data.getRunnerupTitle(), titleCounts, false);
        titleCounts = gloryModel.getTitleCountByCourt(true, isWinner);
        parseCountDataByCourt(data.getRunnerupTitle(), titleCounts, true);
        // count gs params
        data.setGs(data.new Gs());
        Map<String, Integer[]> map = gloryModel.getGsWinLose();
        parseGsParamData(data, map);
        Integer[] result = gloryModel.getGsCount(false);
        data.getGs().setCareerWin(result[0]);
        data.getGs().setCareerLose(result[1]);
        result = gloryModel.getGsCount(true);
        data.getGs().setSeasonWin(result[0]);
        data.getGs().setSeasonLose(result[1]);
        // count atp1000 params
        data.setMaster1000(data.new Master1000());
        result = gloryModel.getAtp1000Count(false);
        data.getMaster1000().setCareerWin(result[0]);
        data.getMaster1000().setCareerLose(result[1]);
        result = gloryModel.getAtp1000Count(true);
        data.getMaster1000().setSeasonWin(result[0]);
        data.getMaster1000().setSeasonLose(result[1]);

        parseGsData(data);
        parseMasterData(data);
        return data;
    }

    private void parseGsParamData(GloryTitle data, Map<String, Integer[]> map) {
        Integer[] result = map.get(Constants.MATCH_GS[0]);
        if (result != null) {
            data.getGs().setAoWin(result[0]);
            data.getGs().setAoLose(result[1]);
        }
        result = map.get(Constants.MATCH_GS[1]);
        if (result != null) {
            data.getGs().setFoWin(result[0]);
            data.getGs().setFoLose(result[1]);
        }
        result = map.get(Constants.MATCH_GS[2]);
        if (result != null) {
            data.getGs().setWoWin(result[0]);
            data.getGs().setWoLose(result[1]);
        }
        result = map.get(Constants.MATCH_GS[3]);
        if (result != null) {
            data.getGs().setUoWin(result[0]);
            data.getGs().setUoLose(result[1]);
        }
        data.getGs().setCareerWin(data.getGs().getAoWin());
    }

    private void parseGsData(GloryTitle data) {
        List<MatchResultBean> gsList = gloryModel.getGsResultList();
        Map<String, GloryGsItem> gsMap = new HashMap<>();
        List<GloryGsItem> gsGlories = new ArrayList<>();
        for (MatchResultBean bean:gsList) {
            String year = bean.getDate().split("-")[0];
            GloryGsItem item = gsMap.get(year);

            // 只记录name和date
            Record record = new Record();
            record.setMatch(bean.getMatch());
            record.setStrDate(bean.getDate());

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
               item.setRecordAo(record);
            }
            else if (bean.getMatch().equals("法国网球公开赛")) {
                if ("Winner".equals(bean.getResult())) {
                    item.setFo("冠军");
                }
                else {
                    item.setFo(Constants.getGsGloryForRound(bean.getResult(), false));
                }
                item.setRecordFo(record);
            }
            else if (bean.getMatch().equals("温布尔顿网球公开赛")) {
                if ("Winner".equals(bean.getResult())) {
                    item.setWo("冠军");
                }
                else {
                    item.setWo(Constants.getGsGloryForRound(bean.getResult(), false));
                }
                item.setRecordWo(record);
            }
            else if (bean.getMatch().equals("美国网球公开赛")) {
                if ("Winner".equals(bean.getResult())) {
                    item.setUo("冠军");
                }
                else {
                    item.setUo(Constants.getGsGloryForRound(bean.getResult(), false));
                }
                item.setRecordUo(record);
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

            // 只记录name和date
            Record record = new Record();
            record.setMatch(bean.getMatch());
            record.setStrDate(bean.getDate());

            setMasterResult(item, nameMap.get(bean.getMatch()).getMatchId(), bean.getResult(), record);
        }
        Collections.sort(masterGlories, new Comparator<GloryMasterItem>() {
            @Override
            public int compare(GloryMasterItem left, GloryMasterItem right) {
                return left.getYear() - right.getYear();
            }
        });
        data.setMasterItemList(masterGlories);
    }

    private void setMasterResult(GloryMasterItem item, int matchId, String result, Record record) {
        if (matchId == Constants.ATP_1000_PUBLIC_ID[0]) {
            item.setIw(Constants.getMasterGloryForRound(result));
            item.setRecordIW(record);
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[1]) {
            item.setMiami(Constants.getMasterGloryForRound(result));
            item.setRecordMiami(record);
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[2]) {
            item.setMc(Constants.getMasterGloryForRound(result));
            item.setRecordMC(record);
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[3]) {
            item.setMadrid(Constants.getMasterGloryForRound(result));
            item.setRecordMadrid(record);
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[4]) {
            item.setRoma(Constants.getMasterGloryForRound(result));
            item.setRecordRoma(record);
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[5]) {
            item.setRc(Constants.getMasterGloryForRound(result));
            item.setRecordRC(record);
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[6]) {
            item.setCicinati(Constants.getMasterGloryForRound(result));
            item.setRecordCicinati(record);
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[7]) {
            item.setSh(Constants.getMasterGloryForRound(result));
            item.setRecordSH(record);
        }
        else if (matchId == Constants.ATP_1000_PUBLIC_ID[8]) {
            item.setParis(Constants.getMasterGloryForRound(result));
            item.setRecordParis(record);
        }
    }

    private void parseCountDataByLevel(GloryTitle.Title title, List<KeyValueCountBean> countList, boolean isCurrentYear) {
        int sum = 0;
        for (KeyValueCountBean countBean:countList) {
            sum += countBean.getValue();
            if (Constants.RECORD_MATCH_LEVELS[0].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    title.setYearGs(countBean.getValue());
                }
                else {
                    title.setCareerGs(countBean.getValue());
                }
            }
            else if (Constants.RECORD_MATCH_LEVELS[1].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    title.setYearMasterCup(countBean.getValue());
                }
                else {
                    title.setCareerMasterCup(countBean.getValue());
                }
            }
            else if (Constants.RECORD_MATCH_LEVELS[2].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    title.setYearAtp1000(countBean.getValue());
                }
                else {
                    title.setCareerAtp1000(countBean.getValue());
                }
            }
            else if (Constants.RECORD_MATCH_LEVELS[3].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    title.setYearAtp500(countBean.getValue());
                }
                else {
                    title.setCareerAtp500(countBean.getValue());
                }
            }
            else if (Constants.RECORD_MATCH_LEVELS[4].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    title.setYearAtp250(countBean.getValue());
                }
                else {
                    title.setCareerAtp250(countBean.getValue());
                }
            }
            else if (Constants.RECORD_MATCH_LEVELS[6].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    title.setYearOlympics(countBean.getValue());
                }
                else {
                    title.setCareerOlympics(countBean.getValue());
                }
            }
        }
        if (isCurrentYear) {
            title.setYearTotal(sum);
        }
        else {
            title.setCareerTotal(sum);
        }
    }

    private void parseCountDataByCourt(GloryTitle.Title title, List<KeyValueCountBean> countList, boolean isCurrentYear) {
        for (KeyValueCountBean countBean:countList) {
            if (Constants.RECORD_MATCH_COURTS[0].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    title.setYearHard(countBean.getValue());
                }
                else {
                    title.setCareerHard(countBean.getValue());
                }
            }
            else if (Constants.RECORD_MATCH_COURTS[1].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    title.setYearClay(countBean.getValue());
                }
                else {
                    title.setCareerClay(countBean.getValue());
                }
            }
            else if (Constants.RECORD_MATCH_COURTS[2].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    title.setYearGrass(countBean.getValue());
                }
                else {
                    title.setCareerGrass(countBean.getValue());
                }
            }
            else if (Constants.RECORD_MATCH_COURTS[3].equals(countBean.getKey())) {
                if (isCurrentYear) {
                    title.setYearInhard(countBean.getValue());
                }
                else {
                    title.setCareerInhard(countBean.getValue());
                }
            }
        }
    }

    /**
     *
     * @param recordList
     * @param groupMode Constants.GROUP_BY_XX
     * @return
     */
    public List<HeaderItem> getHeaderList(List<Record> recordList, int groupMode) {
        List<HeaderItem> list = new ArrayList<>();
        Map<String, List<SubItem>> keyMap = new HashMap<>();
        for (Record record:recordList) {
            String key;
            if (groupMode == Constants.GROUP_BY_COURT) {
                key = record.getCourt();
            }
            else if (groupMode == Constants.GROUP_BY_LEVEL) {
                key = record.getLevel();
            }
            else {
                key = record.getStrDate().split("-")[0];
            }
            List<SubItem> subList = keyMap.get(key);
            if (subList == null) {
                subList = new ArrayList<>();
                keyMap.put(key, subList);

                HeaderItem header = new HeaderItem();
                header.setHeaderBean(new HeaderBean());
                header.getHeaderBean().setItemList(subList);
                header.getHeaderBean().setKey(key);
                list.add(header);
            }
            SubItem item = new SubItem();
            item.setItemPosition(subList.size());
            item.setRecord(record);
            subList.add(item);
        }

        if (groupMode == Constants.GROUP_BY_COURT) {
            Collections.sort(list, new CourtComparotor());
        }
        else if (groupMode == Constants.GROUP_BY_LEVEL) {
            Collections.sort(list, new LevelComparotor());
        }
        else if (groupMode == Constants.GROUP_BY_YEAR) {
            Collections.sort(list, new YearComparotor());
        }

        for (HeaderItem item:list) {
            item.getHeaderBean().setContent(String.valueOf(item.getHeaderBean().getItemList().size()));
            for (SubItem sub:item.getHeaderBean().getItemList()) {
                sub.setGroupCount(item.getHeaderBean().getItemList().size());
            }
        }
        return list;
    }

    private class LevelComparotor implements Comparator<HeaderItem> {

        @Override
        public int compare(HeaderItem o1, HeaderItem o2) {
            return getLevelValue(o1.getHeaderBean().getKey()) - getLevelValue(o2.getHeaderBean().getKey());
        }

        private int getLevelValue(String level) {
            for (int i = 0; i < Constants.RECORD_MATCH_LEVELS.length; i ++) {
                if (level.equals(Constants.RECORD_MATCH_LEVELS[i])) {
                    return i;
                }
            }
            return Constants.RECORD_MATCH_LEVELS.length;
        }
    }

    private class CourtComparotor implements Comparator<HeaderItem> {

        @Override
        public int compare(HeaderItem o1, HeaderItem o2) {
            return getCourtValue(o1.getHeaderBean().getKey()) - getCourtValue(o2.getHeaderBean().getKey());
        }

        private int getCourtValue(String court) {
            for (int i = 0; i < Constants.RECORD_MATCH_COURTS.length; i ++) {
                if (court.equals(Constants.RECORD_MATCH_COURTS[i])) {
                    return i;
                }
            }
            return Constants.RECORD_MATCH_COURTS.length;
        }
    }

    private class YearComparotor implements Comparator<HeaderItem> {

        @Override
        public int compare(HeaderItem o1, HeaderItem o2) {
            return Integer.parseInt(o2.getHeaderBean().getKey()) - Integer.parseInt(o1.getHeaderBean().getKey());
        }
    }

}
