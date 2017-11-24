package com.king.khcareer.home.k4;

import com.king.khcareer.match.gallery.UserMatchBean;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/3 0003.
 */

public class HomeData {

    private List<Record> recordList;

    private String recordMatch;
    private String recordCountry;
    private String recordRound;
    private String recordCourt;

    private List<PlayerBean> playerList;

    private List<UserMatchBean> matchList;

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    public String getRecordMatch() {
        return recordMatch;
    }

    public void setRecordMatch(String recordMatch) {
        this.recordMatch = recordMatch;
    }

    public String getRecordRound() {
        return recordRound;
    }

    public void setRecordRound(String recordRound) {
        this.recordRound = recordRound;
    }

    public String getRecordCourt() {
        return recordCourt;
    }

    public void setRecordCourt(String recordCourt) {
        this.recordCourt = recordCourt;
    }

    public List<UserMatchBean> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<UserMatchBean> matchList) {
        this.matchList = matchList;
    }

    public String getRecordCountry() {
        return recordCountry;
    }

    public void setRecordCountry(String recordCountry) {
        this.recordCountry = recordCountry;
    }

    public List<PlayerBean> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<PlayerBean> playerList) {
        this.playerList = playerList;
    }
}
