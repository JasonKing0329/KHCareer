package com.king.khcareer.glory.bean;

import com.king.khcareer.glory.gs.GloryGsItem;
import com.king.khcareer.glory.gs.GloryMasterItem;
import com.king.khcareer.model.sql.player.bean.Record;

import java.util.List;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class GloryTitle {
    
    private List<Record> championList;
    private List<Record> runnerUpList;
    private List<Record> targetList;
    private List<Record> targetWinList;

    private int careerMatch;
    private int careerWin;
    
    public Title championTitle;
    public Title runnerupTitle;

    private Gs gs;
    private Master1000 master1000;

    private List<GloryGsItem> gsItemList;
    private List<GloryMasterItem> masterItemList;

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

    public List<GloryGsItem> getGsItemList() {
        return gsItemList;
    }

    public void setGsItemList(List<GloryGsItem> gsItemList) {
        this.gsItemList = gsItemList;
    }

    public List<GloryMasterItem> getMasterItemList() {
        return masterItemList;
    }

    public void setMasterItemList(List<GloryMasterItem> masterItemList) {
        this.masterItemList = masterItemList;
    }

    public List<Record> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<Record> targetList) {
        this.targetList = targetList;
    }

    public List<Record> getTargetWinList() {
        return targetWinList;
    }

    public void setTargetWinList(List<Record> targetWinList) {
        this.targetWinList = targetWinList;
    }

    public int getCareerMatch() {
        return careerMatch;
    }

    public void setCareerMatch(int careerMatch) {
        this.careerMatch = careerMatch;
    }

    public int getCareerWin() {
        return careerWin;
    }

    public void setCareerWin(int careerWin) {
        this.careerWin = careerWin;
    }

    public Title getChampionTitle() {
        return championTitle;
    }

    public void setChampionTitle(Title championTitle) {
        this.championTitle = championTitle;
    }

    public Title getRunnerupTitle() {
        return runnerupTitle;
    }

    public void setRunnerupTitle(Title runnerupTitle) {
        this.runnerupTitle = runnerupTitle;
    }

    public Gs getGs() {
        return gs;
    }

    public void setGs(Gs gs) {
        this.gs = gs;
    }

    public Master1000 getMaster1000() {
        return master1000;
    }

    public void setMaster1000(Master1000 master1000) {
        this.master1000 = master1000;
    }

    public class Master1000 {
        private int careerWin;
        private int careerLose;
        private int seasonWin;
        private int seasonLose;

        public int getCareerWin() {
            return careerWin;
        }

        public void setCareerWin(int careerWin) {
            this.careerWin = careerWin;
        }

        public int getCareerLose() {
            return careerLose;
        }

        public void setCareerLose(int careerLose) {
            this.careerLose = careerLose;
        }

        public int getSeasonWin() {
            return seasonWin;
        }

        public void setSeasonWin(int seasonWin) {
            this.seasonWin = seasonWin;
        }

        public int getSeasonLose() {
            return seasonLose;
        }

        public void setSeasonLose(int seasonLose) {
            this.seasonLose = seasonLose;
        }
    }

    public class Gs {
        private int careerWin;
        private int careerLose;
        private int seasonWin;
        private int seasonLose;
        private int aoWin;
        private int aoLose;
        private int foWin;
        private int foLose;
        private int woWin;
        private int woLose;
        private int uoWin;
        private int uoLose;

        public int getCareerWin() {
            return careerWin;
        }

        public void setCareerWin(int careerWin) {
            this.careerWin = careerWin;
        }

        public int getCareerLose() {
            return careerLose;
        }

        public void setCareerLose(int careerLose) {
            this.careerLose = careerLose;
        }

        public int getSeasonWin() {
            return seasonWin;
        }

        public void setSeasonWin(int seasonWin) {
            this.seasonWin = seasonWin;
        }

        public int getSeasonLose() {
            return seasonLose;
        }

        public void setSeasonLose(int seasonLose) {
            this.seasonLose = seasonLose;
        }

        public int getAoWin() {
            return aoWin;
        }

        public void setAoWin(int aoWin) {
            this.aoWin = aoWin;
        }

        public int getAoLose() {
            return aoLose;
        }

        public void setAoLose(int aoLose) {
            this.aoLose = aoLose;
        }

        public int getFoWin() {
            return foWin;
        }

        public void setFoWin(int foWin) {
            this.foWin = foWin;
        }

        public int getFoLose() {
            return foLose;
        }

        public void setFoLose(int foLose) {
            this.foLose = foLose;
        }

        public int getWoWin() {
            return woWin;
        }

        public void setWoWin(int woWin) {
            this.woWin = woWin;
        }

        public int getWoLose() {
            return woLose;
        }

        public void setWoLose(int woLose) {
            this.woLose = woLose;
        }

        public int getUoWin() {
            return uoWin;
        }

        public void setUoWin(int uoWin) {
            this.uoWin = uoWin;
        }

        public int getUoLose() {
            return uoLose;
        }

        public void setUoLose(int uoLose) {
            this.uoLose = uoLose;
        }
    }

    public class Title {
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

        private int careerHard;
        private int careerClay;
        private int careerGrass;
        private int careerInhard;

        private int yearHard;
        private int yearClay;
        private int yearGrass;
        private int yearInhard;

        private int careerTotal;
        private int yearTotal;

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

        public int getCareerHard() {
            return careerHard;
        }

        public void setCareerHard(int careerHard) {
            this.careerHard = careerHard;
        }

        public int getCareerClay() {
            return careerClay;
        }

        public void setCareerClay(int careerClay) {
            this.careerClay = careerClay;
        }

        public int getCareerGrass() {
            return careerGrass;
        }

        public void setCareerGrass(int careerGrass) {
            this.careerGrass = careerGrass;
        }

        public int getCareerInhard() {
            return careerInhard;
        }

        public void setCareerInhard(int careerInhard) {
            this.careerInhard = careerInhard;
        }

        public int getYearHard() {
            return yearHard;
        }

        public void setYearHard(int yearHard) {
            this.yearHard = yearHard;
        }

        public int getYearClay() {
            return yearClay;
        }

        public void setYearClay(int yearClay) {
            this.yearClay = yearClay;
        }

        public int getYearGrass() {
            return yearGrass;
        }

        public void setYearGrass(int yearGrass) {
            this.yearGrass = yearGrass;
        }

        public int getYearInhard() {
            return yearInhard;
        }

        public void setYearInhard(int yearInhard) {
            this.yearInhard = yearInhard;
        }

        public int getCareerTotal() {
            return careerTotal;
        }

        public void setCareerTotal(int careerTotal) {
            this.careerTotal = careerTotal;
        }

        public int getYearTotal() {
            return yearTotal;
        }

        public void setYearTotal(int yearTotal) {
            this.yearTotal = yearTotal;
        }
    }

}
