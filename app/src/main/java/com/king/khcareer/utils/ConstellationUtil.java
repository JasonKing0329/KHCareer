package com.king.khcareer.utils;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/9 15:15
 */
public class ConstellationUtil {

    public static String[] starArr = {"魔羯座", "水瓶座", "双鱼座", "白羊座",
            "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座"};
    private static String[] starArrEng = {"Capricornus", "Aquarius", "Pisces", "Aries",
            "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius"};
    private static int[] starIndex = {9, 10, 11, 0,
            1, 2, 3, 4, 5, 6, 7, 8};
    private static int[] DayArr = {22, 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22};  // 两个星座分割日

    /**
     * 获取星座的序号，下标从0开始
     * @param date 符合“yyyy-MM-dd”
     * @return
     */
    public static int getConstellationIndex(String date) throws ConstellationParseException {
        try {
            String[] arrays = date.split("-");
            return getAstroIndex(Integer.parseInt(arrays[1]), Integer.parseInt(arrays[2]));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConstellationParseException();
        }
    }

    /**
     * 获取星座的中文名
     * @param date 符合“yyyy-MM-dd”
     * @return
     */
    public static String getConstellationChn(String date) throws ConstellationParseException {
        try {
            String[] arrays = date.split("-");
            return getAstro(Integer.parseInt(arrays[1]), Integer.parseInt(arrays[2]));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConstellationParseException();
        }
    }

    /**
     * 获取星座的英文名
     * @param date 符合“yyyy-MM-dd”
     * @return
     */
    public static String getConstellationEng(String date) throws ConstellationParseException {
        try {
            String[] arrays = date.split("-");
            return getAstroEng(Integer.parseInt(arrays[1]), Integer.parseInt(arrays[2]));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConstellationParseException();
        }
    }

    public static String getAstroEng(int month, int day) {
        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < DayArr[month - 1]) {
            index = index - 1;
        }
        // 12月摩羯座
        if (index >= starArrEng.length) {
            index = 0;
        }
        // 返回索引指向的星座string
        return starArrEng[index];
    }

    public static String getAstro(int month, int day) {
        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < DayArr[month - 1]) {
            index = index - 1;
        }
        // 12月摩羯座
        if (index >= starArrEng.length) {
            index = 0;
        }
        // 返回索引指向的星座string
        return starArr[index];
    }

    public static int getAstroIndex(int month, int day) {
        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < DayArr[month - 1]) {
            index = index - 1;
        }
        // 12月摩羯座
        if (index >= starArrEng.length) {
            index = 0;
        }
        // 返回索引指向的星座string
        return starIndex[index];
    }

    /**
     * 获取星座序号对应的中文名称
     * @param astroIndex
     * @return
     */
    public static String getConstellationChnByIndex(int astroIndex) {
        for (int i = 0; i < starIndex.length; i ++) {
            if (astroIndex == starIndex[i]) {
                return starArr[i];
            }
        }
        return "Error";
    }

    public static class ConstellationParseException extends Exception {

    }
}
