package com.king.khcareer.player.manage;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.VirtualManager;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.utils.ConstellationUtil;
import com.king.khcareer.utils.PinyinUtil;
import com.king.khcareer.settings.SettingProperty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/9 11:19
 */
public class PlayerPresenter {
    private PubDataProvider pubDataProvider;
    /**
     * virtual player 不参与排序，也不可编辑，始终保持在list的最顶上
     */
    private List<PlayerBean> virtualPlayerList;
    private List<PlayerBean> realPlayerList;
    /**
     * 最后显示的player list
     */
    private List<PlayerBean> playerList;
    private IPlayerView playerView;

    public PlayerPresenter(IPlayerView playerView) {
        this.playerView = playerView;
        pubDataProvider = new PubDataProvider();
    }

    public void loadPlayerList(Context context) {

        int sortMode = SettingProperty.getPlayerSortMode(context);
        new LoadTask().execute(sortMode);
    }

    /**
     *
     * @param context
     * @param mode see SettingProperty.VALUE_SORT_PLAYER_XX
     */
    public void sortPlayer(Context context, int mode) {
        SettingProperty.setPlayerSortMode(context, mode);
        new SortTask().execute(mode);
    }

    public void insertPlayer(PlayerBean bean) {
        pubDataProvider.insertPlayer(bean);
    }

    public void updatePlayer(PlayerBean bean) {
        pubDataProvider.updatePlayer(bean);
    }

    public void deletePlayer(PlayerBean bean) {
        pubDataProvider.deletePlayer(bean);
    }

    private static class PlayerComparator implements Comparator<PlayerBean> {

        private int sortMode;
        private Map<String, String> countryPinyinMap;
        private SimpleDateFormat sdf;

        private PlayerComparator(int sortMode) {
            this.sortMode = sortMode;
            if (sortMode == SettingProperty.VALUE_SORT_PLAYER_COUNTRY) {
                countryPinyinMap = new HashMap<>();
            }
            else if (sortMode == SettingProperty.VALUE_SORT_PLAYER_AGE) {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
            else if (sortMode == SettingProperty.VALUE_SORT_PLAYER_CONSTELLATION) {
                sdf = new SimpleDateFormat("MM-dd");
            }
        }

        @Override
        public int compare(PlayerBean lhs, PlayerBean rhs) {
            if (sortMode == SettingProperty.VALUE_SORT_PLAYER_AGE) {
                return compareByAge(lhs, rhs);
            }
            else if (sortMode == SettingProperty.VALUE_SORT_PLAYER_CONSTELLATION) {
                return compareByConstellation(lhs, rhs);
            }
            else if (sortMode == SettingProperty.VALUE_SORT_PLAYER_COUNTRY) {
                return compareByCoutry(lhs, rhs);
            }
            else if (sortMode == SettingProperty.VALUE_SORT_PLAYER_NAME_ENG) {
                return compareByNameEng(lhs, rhs);
            }
            else {
                return lhs.getNamePinyin().compareTo(rhs.getNamePinyin());
            }
        }

        private int compareByCoutry(PlayerBean lhs, PlayerBean rhs) {
            String pinyinL = countryPinyinMap.get(lhs.getCountry());
            if (pinyinL == null) {
                pinyinL = PinyinUtil.getPinyin(lhs.getCountry());
            }
            String pinyinR = countryPinyinMap.get(rhs.getCountry());
            if (pinyinR == null) {
                pinyinR = PinyinUtil.getPinyin(rhs.getCountry());
            }
            return pinyinL.compareTo(pinyinR);
        }

        private int compareByAge(PlayerBean lhs, PlayerBean rhs) {
            // 未知的放在最后
            long dateL;
            try {
                dateL = sdf.parse(lhs.getBirthday()).getTime();
            } catch (Exception e) {
                e.printStackTrace();
                dateL = Long.MAX_VALUE;
            }
            long dateR;
            try {
                dateR = sdf.parse(rhs.getBirthday()).getTime();
            } catch (Exception e) {
                e.printStackTrace();
                dateR = Long.MAX_VALUE;
            }
            if (dateL - dateR < 0) {
                return -1;
            }
            else if (dateL - dateR > 0) {
                return 1;
            }
            else {
                return 0;
            }
        }

        private int compareByConstellation(PlayerBean lhs, PlayerBean rhs) {
            // 未知的放在最后
            int indexL;
            try {
                indexL = ConstellationUtil.getConstellationIndex(lhs.getBirthday());
            } catch (ConstellationUtil.ConstellationParseException e) {
                e.printStackTrace();
                indexL = 999;
            }
            int indexR;
            try {
                indexR = ConstellationUtil.getConstellationIndex(rhs.getBirthday());
            } catch (ConstellationUtil.ConstellationParseException e) {
                e.printStackTrace();
                indexR = 999;
            }
            return indexL - indexR;
        }

        private int compareByNameEng(PlayerBean lhs, PlayerBean rhs) {
            // 未知的放在最后
            String strLeft = lhs.getNameEng();
            if (TextUtils.isEmpty(strLeft)) {
                strLeft = "zzzzzzzzzzzzz";
            }
            String strRight = rhs.getNameEng();
            if (TextUtils.isEmpty(strRight)) {
                strRight = "zzzzzzzzzzzzz";
            }
            return strLeft.toLowerCase().compareTo(strRight.toLowerCase());
        }

    }

    private class LoadTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int sortMode = params[0];
            playerList = new ArrayList<>();
            realPlayerList = pubDataProvider.getRealPlayerList();
            virtualPlayerList = VirtualManager.getVirtualPlayer();
            // 从数据库里查询就是按照name排序的
            if (sortMode != SettingProperty.VALUE_SORT_PLAYER_NAME) {
                Collections.sort(realPlayerList, new PlayerComparator(sortMode));
            }
            playerList.addAll(virtualPlayerList);
            playerList.addAll(realPlayerList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            playerView.onLoadPlayerList(playerList);
            super.onPostExecute(aVoid);
        }
    }

    /**
     * 只排序real player
     */
    private class SortTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int sortMode = params[0];
            Collections.sort(realPlayerList, new PlayerComparator(sortMode));
            playerList.clear();
            playerList.addAll(virtualPlayerList);
            playerList.addAll(realPlayerList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            playerView.onSortFinished();
            super.onPostExecute(aVoid);
        }
    }
}
