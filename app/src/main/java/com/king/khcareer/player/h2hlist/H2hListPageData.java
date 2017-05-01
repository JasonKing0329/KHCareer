package com.king.khcareer.player.h2hlist;

import com.king.khcareer.model.sql.player.bean.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/30 0030.
 */

public class H2hListPageData {

    private int competitors;
    private int top10Win;
    private int top10Lose;
    private List<HeaderItem> headerList;
    private ArrayList<Record> recordList;

    public ArrayList<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(ArrayList<Record> recordList) {
        this.recordList = recordList;
    }

    public int getCompetitors() {
        return competitors;
    }

    public void setCompetitors(int competitors) {
        this.competitors = competitors;
    }

    public int getTop10Win() {
        return top10Win;
    }

    public void setTop10Win(int top10Win) {
        this.top10Win = top10Win;
    }

    public int getTop10Lose() {
        return top10Lose;
    }

    public void setTop10Lose(int top10Lose) {
        this.top10Lose = top10Lose;
    }

    public List<HeaderItem> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List<HeaderItem> headerList) {
        this.headerList = headerList;
    }
}
