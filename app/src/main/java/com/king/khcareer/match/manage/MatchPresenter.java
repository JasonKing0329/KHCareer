package com.king.khcareer.match.manage;

import android.content.Context;
import android.os.AsyncTask;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.model.PubProviderHelper;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.utils.PinyinUtil;
import com.king.khcareer.settings.SettingProperty;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/9 11:20
 */
public class MatchPresenter {
    private IMatchView matchView;
    private PubDataProvider pubDataProvider;
    private List<MatchNameBean> matchList;

    public MatchPresenter(IMatchView matchView) {
        this.matchView = matchView;
        pubDataProvider = PubProviderHelper.getProvider();
    }

    public void loadMatchList(Context context) {
        int sortMode = SettingProperty.getMatchSortMode(context);
        new LoadTask().execute(sortMode);
    }

    /**
     *
     * @param context
     * @param mode see SettingProperty.VALUE_SORT_MATCH_XX
     */
    public void sortMatch(Context context, int mode) {
        SettingProperty.setMatchSortMode(context, mode);
        new SortTask().execute(mode);
    }

    public void insertMatch(MatchNameBean bean) {
        pubDataProvider.insertMatch(bean);
    }

    public void updateMatch(MatchNameBean bean) {
        pubDataProvider.updateMatch(bean);
    }

    public void deleteMatchName(MatchNameBean bean) {
        pubDataProvider.deleteMatchName(bean);
    }

    private static class MatchComparator implements Comparator<MatchNameBean> {

        private int sortMode;

        private MatchComparator(int sortMode) {
            this.sortMode = sortMode;
        }

        @Override
        public int compare(MatchNameBean lhs, MatchNameBean rhs) {
            if (sortMode == SettingProperty.VALUE_SORT_MATCH_NAME) {
                return compareByName(lhs, rhs);
            }
            else if (sortMode == SettingProperty.VALUE_SORT_MATCH_LEVEL) {
                return compareByLevel(lhs, rhs);
            }
            else {
                return lhs.getMatchBean().getWeek() - rhs.getMatchBean().getWeek();
            }
        }

        private int compareByName(MatchNameBean lhs, MatchNameBean rhs) {
            return PinyinUtil.getPinyin(lhs.getName()).compareTo(PinyinUtil.getPinyin(rhs.getName()));
        }

        private int compareByLevel(MatchNameBean lhs, MatchNameBean rhs) {
            int valueL = getLevelValue(lhs.getMatchBean().getLevel());
            int valueR = getLevelValue(rhs.getMatchBean().getLevel());
            return valueL - valueR;
        }

        private int getLevelValue(String level) {
            for (int i = 0; i < Constants.RECORD_MATCH_LEVELS.length; i ++) {
                if (level.equals(Constants.RECORD_MATCH_LEVELS[i])) {
                    return i;
                }
            }
            return 0;
        }
    }

    private class LoadTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int sortMode = params[0];
            matchList = pubDataProvider.getMatchList();
            // 从数据库里查询就是按照week排序的
            if (sortMode != SettingProperty.VALUE_SORT_MATCH_WEEK) {
                Collections.sort(matchList, new MatchComparator(sortMode));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            matchView.onLoadMatchList(matchList);
            super.onPostExecute(aVoid);
        }
    }

    private class SortTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int sortMode = params[0];
            Collections.sort(matchList, new MatchComparator(sortMode));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            matchView.onSortFinished();
            super.onPostExecute(aVoid);
        }
    }
}
