package com.king.khcareer.utils;

import java.util.Calendar;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/25 11:17
 */
public class SeasonManager {

    public enum SeasonEnum {
        HARD, CLAY, GRASS, INHARD
    }

    public static SeasonEnum getSeasonType() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        // clay season, 蒙卡前一站到法网结束
        if (week >= 15 && week < 24) {
            return SeasonEnum.CLAY;
        }
        // grass season, 斯图加特到温网结束
        else if (week >= 24 && week < 29) {
            return SeasonEnum.GRASS;
        }
        // inner hard season, 上海以后
        else if (week > 41) {
            return SeasonEnum.INHARD;
        }
        // hard season
        else {
            return SeasonEnum.HARD;
        }
    }
}
