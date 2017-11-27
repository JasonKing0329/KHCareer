package com.king.khcareer.player.h2hlist;

import com.king.khcareer.model.PubProviderHelper;
import com.king.khcareer.model.sql.player.H2hModel;
import com.king.khcareer.model.sql.player.bean.H2hParentBean;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
 * Created by Administrator on 2017/4/30 0030.
 */

public class H2hPresenter {

    private IH2hListView h2hView;
    private H2hListPageData h2hListPageData;
    private H2hModel h2hModel;

    private int sortType;
    private int sortOrder;
    private Map<String, PlayerBean> playerMap = getPlayerBeanMap();

    public H2hPresenter(IH2hListView h2hView) {
        this.h2hView = h2hView;
        h2hModel = new H2hModel();
        sortType = SortDialog.SORT_TYPE_NAME;
        sortOrder = SortDialog.SORT_ORDER_ASC;
    }

    public void loadDatas() {
        Observable.create(new ObservableOnSubscribe<H2hListPageData>() {
            @Override
            public void subscribe(ObservableEmitter<H2hListPageData> e) throws Exception {
                h2hListPageData = createHeaderList();
                e.onNext(h2hListPageData);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<H2hListPageData>() {
                    @Override
                    public void accept(H2hListPageData data) throws Exception {
                        h2hView.onDataLoaded(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private H2hListPageData createHeaderList() {
        H2hListPageData data = new H2hListPageData();

        // h2h list
        List<H2hParentBean> list = h2hModel.queryH2hList();
        data.setHeaderList(list);
        data.setShowList(new ArrayList<H2hParentBean>());
        data.getShowList().addAll(list);

        // chart datas
        data.setChartContents(new String[]{
                "Top10", "Top11-20", "Top21-50", "Top51-100", "OutOf100"
        });
        data.setCareerChartWinValues(h2hModel.getTotalCount(true, false));
        data.setCareerChartLoseValues(h2hModel.getTotalCount(false, false));
        data.setSeasonChartWinValues(h2hModel.getTotalCount(true, true));
        data.setSeasonChartLoseValues(h2hModel.getTotalCount(false, true));

        // load player bean for each player
        fillPlayerBean(list);

        // sort by pinyin
        Collections.sort(list, new PlayerComparator(SortDialog.SORT_ORDER_ASC));

        return data;
    }

    public Map<String, PlayerBean> getPlayerBeanMap() {
        List<PlayerBean> playerList = PubProviderHelper.getProvider().getPlayerList();
        Map<String, PlayerBean> map = new HashMap<>();
        if (playerList != null) {
            for (PlayerBean bean:playerList) {
                map.put(bean.getNameChn(), bean);
            }
        }
        return map;
    }

    public int getSortType() {
        return sortType;
    }

    public void sortDatas(int sortType, int sortOrder) {
        this.sortType = sortType;
        this.sortOrder = sortOrder;
        List<H2hParentBean> list = h2hListPageData.getShowList();
        handleSort(list, sortType, sortOrder);
        h2hView.onSortFinished();
    }

    private void handleSort(List<H2hParentBean> list, int sortType, int sortOrder) {
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
    }

    /**
     * load player bean for each player
     * @param list
     */
    private void fillPlayerBean(List<H2hParentBean> list) {
        for (int i = 0; i < list.size(); i ++) {
            H2hParentBean bean = list.get(i);
            bean.setPlayerBean(playerMap.get(bean.getPlayer()));
        }
    }

    /**
     * 不过滤，还原所有数据
     */
    public void filterNothing() {
        h2hListPageData.getShowList().clear();
        h2hListPageData.getShowList().addAll(h2hListPageData.getHeaderList());
        handleSort(h2hListPageData.getShowList(), sortType, sortOrder);
        h2hView.onFilterFinished();
    }

    public void filterCountry(String country) {
        List<H2hParentBean> list = h2hModel.queryH2hListByCountry(country);
        fillPlayerBean(list);
        handleSort(list, sortType, sortOrder);
        h2hListPageData.setShowList(list);
        h2hView.onFilterFinished();
    }

    public void filterCount(int min, int max) {
        List<H2hParentBean> list = h2hModel.queryH2hListByTotal(min, max);
        fillPlayerBean(list);
        handleSort(list, sortType, sortOrder);
        h2hListPageData.setShowList(list);
        h2hView.onFilterFinished();
    }

    public void filterWin(int min, int max) {
        List<H2hParentBean> list = h2hModel.queryH2hListByWin(min, max);
        fillPlayerBean(list);
        handleSort(list, sortType, sortOrder);
        h2hListPageData.setShowList(list);
        h2hView.onFilterFinished();
    }

    public void filterLose(int min, int max) {
        List<H2hParentBean> list = h2hModel.queryH2hListByLose(min, max);
        fillPlayerBean(list);
        handleSort(list, sortType, sortOrder);
        h2hListPageData.setShowList(list);
        h2hView.onFilterFinished();
    }

    /**
     * 净胜场
     * @param min
     * @param max
     */
    public void filterOdds(int min, int max) {
        List<H2hParentBean> list = h2hModel.queryH2hListByOdds(min, max);
        fillPlayerBean(list);
        handleSort(list, sortType, sortOrder);
        h2hListPageData.setShowList(list);
        h2hView.onFilterFinished();
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
