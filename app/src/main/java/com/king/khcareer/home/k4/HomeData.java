package com.king.khcareer.home.k4;

import com.king.khcareer.match.gallery.UserMatchBean;
import com.king.khcareer.model.sql.player.bean.Record;

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

    private String playerName1;
    private boolean isWinner1;
    private String playerName2;
    private boolean isWinner2;
    private String playerName3;
    private boolean isWinner3;

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

    public String getPlayerName1() {
        return playerName1;
    }

    public void setPlayerName1(String playerName1) {
        this.playerName1 = playerName1;
    }

    public String getPlayerName2() {
        return playerName2;
    }

    public void setPlayerName2(String playerName2) {
        this.playerName2 = playerName2;
    }

    public String getPlayerName3() {
        return playerName3;
    }

    public void setPlayerName3(String playerName3) {
        this.playerName3 = playerName3;
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

    public boolean isWinner1() {
        return isWinner1;
    }

    public void setWinner1(boolean winner1) {
        isWinner1 = winner1;
    }

    public boolean isWinner2() {
        return isWinner2;
    }

    public void setWinner2(boolean winner2) {
        isWinner2 = winner2;
    }

    public boolean isWinner3() {
        return isWinner3;
    }

    public void setWinner3(boolean winner3) {
        isWinner3 = winner3;
    }
}
