package com.king.khcareer.model.sql.pubdata;

import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/10 14:22
 */
public class VirtualManager {
    public static List<PlayerBean> getVirtualPlayer() {
        List<PlayerBean> list = new ArrayList<>();
        list.add(getKingHao());
        list.add(getJohnFlamenco());
        list.add(getMichaelHenry());
        list.add(getQiTian());
        return list;
    }

    public static PlayerBean getVirtualPlayer(String name) {
        if ("King Hao".equals(name)) {
            return getKingHao();
        }
        else if ("Qi Tian".equals(name)) {
            return getQiTian();
        }
        else if ("Michael Henry".equals(name)) {
            return getMichaelHenry();
        }
        else if ("John Flamenco".equals(name)) {
            return getJohnFlamenco();
        }
        return null;
    }

    private static PlayerBean getKingHao() {
        PlayerBean bean = new PlayerBean();
        bean.setId(100001);
        bean.setNameChn("King Hao");
        bean.setNameEng("King Hao");
        bean.setNamePinyin("king hao");
        bean.setCountry("中国");
        bean.setCity("上海");
        bean.setBirthday("1991-03-29");
        return bean;
    }

    private static PlayerBean getQiTian() {
        PlayerBean bean = new PlayerBean();
        bean.setId(100004);
        bean.setNameChn("Qi Tian");
        bean.setNameEng("Qi Tian");
        bean.setNamePinyin("qi tian");
        bean.setCountry("中国");
        bean.setCity("成都");
        bean.setBirthday("1991-03-27");
        return bean;
    }

    private static PlayerBean getMichaelHenry() {
        PlayerBean bean = new PlayerBean();
        bean.setId(100003);
        bean.setNameChn("Michael Henry");
        bean.setNameEng("Michael Henry");
        bean.setNamePinyin("michael henry");
        bean.setCountry("美国");
        bean.setCity("芝加哥");
        bean.setBirthday("1998-05-26");
        return bean;
    }

    private static PlayerBean getJohnFlamenco() {
        PlayerBean bean = new PlayerBean();
        bean.setId(100002);
        bean.setNameChn("John Flamenco");
        bean.setNameEng("John Flamenco");
        bean.setNamePinyin("john flamenco");
        bean.setCountry("法国");
        bean.setCity("巴黎");
        bean.setBirthday("1997-05-04");
        return bean;
    }
}
