package com.king.khcareer.glory;

import com.king.khcareer.model.sql.player.bean.Record;

import java.util.List;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class GloryTitle {
    
    private List<Record> championList;
    private List<Record> runnerUpList;

    private int careerTitle;
    private int yearTitle;

    private int careerGs;
    private int careerAtp1000;
    private int careerAtp500;
    private int careerAtp250;
    private int careerMasterCup;
    private int careerOlympics;

    private int yearGs;
    private int yearAtp1000;
    private int yearAtp500;
    private int yearAtp250;
    private int yearMasterCup;
    private int yearOlympics;

    private List<GloryGsItem> gsItemList;

    public List<Record> getChampionList() {
        return championList;
    }

    public void setChampionList(List<Record> championList) {
        this.championList = championList;
    }

    public List<Record> getRunnerUpList() {
        return runnerUpList;
    }

    public void setRunnerUpList(List<Record> runnerUpList) {
        this.runnerUpList = runnerUpList;
    }

    public int getCareerTitle() {
        return careerTitle;
    }

    public void setCareerTitle(int careerTitle) {
        this.careerTitle = careerTitle;
    }

    public int getYearTitle() {
        return yearTitle;
    }

    public void setYearTitle(int yearTitle) {
        this.yearTitle = yearTitle;
    }

    public int getCareerGs() {
        return careerGs;
    }

    public void setCareerGs(int careerGs) {
        this.careerGs = careerGs;
    }

    public int getCareerAtp1000() {
        return careerAtp1000;
    }

    public void setCareerAtp1000(int careerAtp1000) {
        this.careerAtp1000 = careerAtp1000;
    }

    public int getCareerAtp500() {
        return careerAtp500;
    }

    public void setCareerAtp500(int careerAtp500) {
        this.careerAtp500 = careerAtp500;
    }

    public int getCareerAtp250() {
        return careerAtp250;
    }

    public void setCareerAtp250(int careerAtp250) {
        this.careerAtp250 = careerAtp250;
    }

    public int getCareerMasterCup() {
        return careerMasterCup;
    }

    public void setCareerMasterCup(int careerMasterCup) {
        this.careerMasterCup = careerMasterCup;
    }

    public int getCareerOlympics() {
        return careerOlympics;
    }

    public void setCareerOlympics(int careerOlympics) {
        this.careerOlympics = careerOlympics;
    }

    public int getYearGs() {
        return yearGs;
    }

    public void setYearGs(int yearGs) {
        this.yearGs = yearGs;
    }

    public int getYearAtp1000() {
        return yearAtp1000;
    }

    public void setYearAtp1000(int yearAtp1000) {
        this.yearAtp1000 = yearAtp1000;
    }

    public int getYearAtp500() {
        return yearAtp500;
    }

    public void setYearAtp500(int yearAtp500) {
        this.yearAtp500 = yearAtp500;
    }

    public int getYearAtp250() {
        return yearAtp250;
    }

    public void setYearAtp250(int yearAtp250) {
        this.yearAtp250 = yearAtp250;
    }

    public int getYearMasterCup() {
        return yearMasterCup;
    }

    public void setYearMasterCup(int yearMasterCup) {
        this.yearMasterCup = yearMasterCup;
    }

    public int getYearOlympics() {
        return yearOlympics;
    }

    public void setYearOlympics(int yearOlympics) {
        this.yearOlympics = yearOlympics;
    }

    public List<GloryGsItem> getGsItemList() {
        return gsItemList;
    }

    public void setGsItemList(List<GloryGsItem> gsItemList) {
        this.gsItemList = gsItemList;
    }
}
