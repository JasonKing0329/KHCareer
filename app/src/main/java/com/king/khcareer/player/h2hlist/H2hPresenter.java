package com.king.khcareer.player.h2hlist;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.player.RecordDAOImp;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.player.interfc.RecordDAO;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/30 0030.
 */

public class H2hPresenter {

    private IH2hListView h2hView;
    private ArrayList<Record> recordList;
    private H2hListPageData h2hListPageData;

    public H2hPresenter(IH2hListView h2hView) {
        this.h2hView = h2hView;
    }

    public void loadDatas() {
        Observable.create(new Observable.OnSubscribe<H2hListPageData>() {
            @Override
            public void call(Subscriber<? super H2hListPageData> subscriber) {
                h2hListPageData = createHeaderList();
                subscriber.onNext(h2hListPageData);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<H2hListPageData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(H2hListPageData data) {
                        h2hView.onDataLoaded(data);
                    }
                });
    }

    private H2hListPageData createHeaderList() {
        H2hListPageData data = new H2hListPageData();

        RecordDAO dao = new RecordDAOImp();
        if (recordList == null) {
            recordList = dao.queryAll();
            Collections.reverse(recordList);
        }
        data.setRecordList(recordList);

        int competitors = 0;
        int toTop10Win = 0;
        int toTop10Lose = 0;
        Map<String, PlayerBean> playerMap = getPlayerBeanMap();
        List<HeaderItem> list = new ArrayList<>();
        Map<String, HeaderItem> headerMap = new HashMap<>();
        for (int i = 0; i < recordList.size(); i ++) {
            Record record = recordList.get(i);
            String keyHeader = record.getCompetitor();
            HeaderItem item = headerMap.get(keyHeader);
            if (item == null) {
                competitors ++;
                item = new HeaderItem();
                headerMap.put(keyHeader, item);
                list.add(item);

                H2hHeaderBean bean = new H2hHeaderBean();
                bean.setPlayerBean(playerMap.get(record.getCompetitor()));
                bean.setPlayer(record.getCompetitor());
                bean.setCountry(record.getCptCountry());
                item.setHeader(bean);
                item.setChildItemList(new ArrayList<RecordItem>());
            }
            RecordItem recordItem = new RecordItem();
            recordItem.setRecord(record);
            item.getChildItemList().add(recordItem);

            // count win lose
            // W/0不算作胜负场
            if (!Constants.SCORE_RETIRE.equals(record.getScore())) {

                if (MultiUserManager.USER_DB_FLAG.equals(record.getWinner())) {
                    item.getHeader().setWin(item.getHeader().getWin() + 1);
                    if (record.getCptRank() <= 10 && record.getCptRank() > 0) {
                        toTop10Win ++;
                    }
                }
                else {
                    item.getHeader().setLose(item.getHeader().getLose() + 1);
                    if (record.getCptRank() <= 10 && record.getCptRank() > 0) {
                        toTop10Lose ++;
                    }
                }
            }
        }

        // sort by pinyin
        Collections.sort(list, new PlayerComparator(SortDialog.SORT_ORDER_ASC));

        data.setCompetitors(competitors);
        data.setTop10Lose(toTop10Lose);
        data.setTop10Win(toTop10Win);
        data.setHeaderList(list);
        return data;
    }

    public Map<String, PlayerBean> getPlayerBeanMap() {
        List<PlayerBean> playerList = new PubDataProvider().getPlayerList();
        Map<String, PlayerBean> map = new HashMap<>();
        if (playerList != null) {
            for (PlayerBean bean:playerList) {
                map.put(bean.getNameChn(), bean);
            }
        }
        return map;
    }

    public void sortDatas(int sortType, int sortOrder) {
        List<HeaderItem> list = h2hListPageData.getHeaderList();
        switch (sortType) {
            case SortDialog.SORT_TYPE_NAME:
                Collections.sort(list, new PlayerComparator(sortOrder));
                break;
            case SortDialog.SORT_TYPE_TOTAL:
                Collections.sort(list, new TotalComparator(sortOrder));
                break;
            case SortDialog.SORT_TYPE_WIN:
                Collections.sort(list, new WinComparator(sortOrder));
                break;
            case SortDialog.SORT_TYPE_LOSE:
                Collections.sort(list, new LoseComparator(sortOrder));
                break;
            case SortDialog.SORT_TYPE_PUREWIN:
                Collections.sort(list, new PureWinComparator(sortOrder));
                break;
            case SortDialog.SORT_TYPE_PURELOSE:
                Collections.sort(list, new PureLoseComparator(sortOrder));
                break;
        }
        h2hView.onSortFinished();
    }

    private class PlayerComparator implements Comparator<HeaderItem> {

        private int order;

        public PlayerComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(HeaderItem item1, HeaderItem item2) {
            PlayerBean pb1 = item1.getHeader().getPlayerBean();
            PlayerBean pb2 = item2.getHeader().getPlayerBean();
            String pinyin1 = pb1 == null ? "zzzz":pb1.getNamePinyin();
            String pinyin2 = pb2 == null ? "zzzz":pb2.getNamePinyin();
            if (order == SortDialog.SORT_ORDER_DESC) {
                return pinyin2.compareTo(pinyin1);
            }
            else {
                return pinyin1.compareTo(pinyin2);
            }
        }
    }

    private class TotalComparator implements Comparator<HeaderItem> {

        private int order;

        public TotalComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(HeaderItem item1, HeaderItem item2) {
            int lTotal = item1.getHeader().getWin() + item1.getHeader().getLose();
            int rTotal = item2.getHeader().getWin() + item2.getHeader().getLose();
            if (order == SortDialog.SORT_ORDER_DESC) {
                return rTotal - lTotal;
            }
            else {
                return lTotal - rTotal;
            }
        }
    }

    private class WinComparator implements Comparator<HeaderItem> {

        private int order;

        public WinComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(HeaderItem item1, HeaderItem item2) {
            int lWin = item1.getHeader().getWin();
            int rWin = item2.getHeader().getWin();
            if (order == SortDialog.SORT_ORDER_DESC) {
                return rWin - lWin;
            }
            else {
                return lWin - rWin;
            }
        }
    }

    private class LoseComparator implements Comparator<HeaderItem> {

        private int order;

        public LoseComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(HeaderItem item1, HeaderItem item2) {
            int lWin = item1.getHeader().getLose();
            int rWin = item2.getHeader().getLose();
            if (order == SortDialog.SORT_ORDER_DESC) {
                return rWin - lWin;
            }
            else {
                return lWin - rWin;
            }
        }
    }

    private class PureWinComparator implements Comparator<HeaderItem> {

        private int order;

        public PureWinComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(HeaderItem item1, HeaderItem item2) {
            int lTotal = item1.getHeader().getWin() - item1.getHeader().getLose();
            int rTotal = item2.getHeader().getWin() - item2.getHeader().getLose();
            if (order == SortDialog.SORT_ORDER_DESC) {
                return rTotal - lTotal;
            }
            else {
                return lTotal - rTotal;
            }
        }
    }

    private class PureLoseComparator implements Comparator<HeaderItem> {

        private int order;

        public PureLoseComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(HeaderItem item1, HeaderItem item2) {
            int lTotal = item1.getHeader().getLose() - item1.getHeader().getWin();
            int rTotal = item2.getHeader().getLose() - item2.getHeader().getWin();
            if (order == SortDialog.SORT_ORDER_DESC) {
                return rTotal - lTotal;
            }
            else {
                return lTotal - rTotal;
            }
        }
    }
}
