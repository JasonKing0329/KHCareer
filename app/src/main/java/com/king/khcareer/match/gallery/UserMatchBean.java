package com.king.khcareer.match.gallery;

import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/15 10:17
 */
public class UserMatchBean {
    private MatchNameBean nameBean;
    private int win;
    private int lose;
    private String best;
    private String bestYears;
    private List<Record> recordList;

    public String getBest() {
        return best;
    }

    public void setBest(String best) {
        this.best = best;
    }

    public String getBestYears() {
        return bestYears;
    }

    public void setBestYears(String bestYears) {
        this.bestYears = bestYears;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public MatchNameBean getNameBean() {
        return nameBean;
    }

    public void setNameBean(MatchNameBean nameBean) {
        this.nameBean = nameBean;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }
}
