package com.king.khcareer.player.h2hlist;

import com.king.khcareer.model.sql.player.H2hModel;
import com.king.khcareer.model.sql.player.bean.H2hParentBean;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;

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
    private H2hListPageData h2hListPageData;
    private H2hModel h2hModel;

    public H2hPresenter(IH2hListView h2hView) {
        this.h2hView = h2hView;
        h2hModel = new H2hModel();
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
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(H2hListPageData data) {
                        h2hView.onDataLoaded(data);
                    }
                });
    }

    private H2hListPageData createHeaderList() {
        H2hListPageData data = new H2hListPageData();

        // h2h list
        List<H2hParentBean> list = h2hModel.queryH2hList();
        data.setHeaderList(list);

        // chart datas
        data.setChartContents(new String[]{
                "Top10", "Top11-20", "Top21-50", "Top51-100", "OutOf100"
        });
        data.setCareerChartWinValues(h2hModel.getTotalCount(true, false));
        data.setCareerChartLoseValues(h2hModel.getTotalCount(false, false));
        data.setSeasonChartWinValues(h2hModel.getTotalCount(true, true));
        data.setSeasonChartLoseValues(h2hModel.getTotalCount(false, true));

        // load player bean for each player
        Map<String, PlayerBean> playerMap = getPlayerBeanMap();
        for (int i = 0; i < list.size(); i ++) {
            H2hParentBean bean = list.get(i);
            bean.setPlayerBean(playerMap.get(bean.getPlayer()));
        }

        // sort by pinyin
        Collections.sort(list, new PlayerComparator(SortDialog.SORT_ORDER_ASC));

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
        List<H2hParentBean> list = h2hListPageData.getHeaderList();
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

    /**
     * 不过滤，还原所有数据
     */
    public void filterNothing() {
        h2hView.onFiltFinished(h2hListPageData.getHeaderList());
    }

    public void filterCountry(String country) {
        List<H2hParentBean> list = h2hModel.queryH2hListByCountry(country);
        h2hView.onFiltFinished(list);
    }

    public void filterCount(int min, int max) {
        List<H2hParentBean> list = h2hModel.queryH2hListByTotal(min, max);
        h2hView.onFiltFinished(list);
    }

    public void filterWin(int min, int max) {
        List<H2hParentBean> list = h2hModel.queryH2hListByWin(min, max);
        h2hView.onFiltFinished(list);
    }

    public void filterLose(int min, int max) {
        List<H2hParentBean> list = h2hModel.queryH2hListByLose(min, max);
        h2hView.onFiltFinished(list);
    }

    /**
     * 净胜场
     * @param min
     * @param max
     */
    public void filterOdds(int min, int max) {
        List<H2hParentBean> list = h2hModel.queryH2hListByOdds(min, max);
        h2hView.onFiltFinished(list);
    }

    private class PlayerComparator implements Comparator<H2hParentBean> {

        private int order;

        public PlayerComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(H2hParentBean item1, H2hParentBean item2) {
            PlayerBean pb1 = item1.getPlayerBean();
            PlayerBean pb2 = item2.getPlayerBean();
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

    private class TotalComparator implements Comparator<H2hParentBean> {

        private int order;

        public TotalComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(H2hParentBean item1, H2hParentBean item2) {
            int lTotal = item1.getWin() + item1.getLose();
            int rTotal = item2.getWin() + item2.getLose();
            if (order == SortDialog.SORT_ORDER_DESC) {
                return rTotal - lTotal;
            }
            else {
                return lTotal - rTotal;
            }
        }
    }

    private class WinComparator implements Comparator<H2hParentBean> {

        private int order;

        public WinComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(H2hParentBean item1, H2hParentBean item2) {
            int lWin = item1.getWin();
            int rWin = item2.getWin();
            if (order == SortDialog.SORT_ORDER_DESC) {
                return rWin - lWin;
            }
            else {
                return lWin - rWin;
            }
        }
    }

    private class LoseComparator implements Comparator<H2hParentBean> {

        private int order;

        public LoseComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(H2hParentBean item1, H2hParentBean item2) {
            int lWin = item1.getLose();
            int rWin = item2.getLose();
            if (order == SortDialog.SORT_ORDER_DESC) {
                return rWin - lWin;
            }
            else {
                return lWin - rWin;
            }
        }
    }

    private class PureWinComparator implements Comparator<H2hParentBean> {

        private int order;

        public PureWinComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(H2hParentBean item1, H2hParentBean item2) {
            int lTotal = item1.getWin() - item1.getLose();
            int rTotal = item2.getWin() - item2.getLose();
            if (order == SortDialog.SORT_ORDER_DESC) {
                return rTotal - lTotal;
            }
            else {
                return lTotal - rTotal;
            }
        }
    }

    private class PureLoseComparator implements Comparator<H2hParentBean> {

        private int order;

        public PureLoseComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(H2hParentBean item1, H2hParentBean item2) {
            int lTotal = item1.getLose() - item1.getWin();
            int rTotal = item2.getLose() - item2.getWin();
            if (order == SortDialog.SORT_ORDER_DESC) {
                return rTotal - lTotal;
            }
            else {
                return lTotal - rTotal;
            }
        }
    }
}
