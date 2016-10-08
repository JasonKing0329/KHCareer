package com.king.mytennis.net.html;

import java.io.Serializable;
import java.util.List;

import com.king.mytennis.model.Record;

public class H2HEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String player1Id, player2Id;
	private int rank1, rank2;
	private int wins1, wins2;
	private String name1, name2;
	private String imgUrl1, imgUrl2;
	private String age1, age2;
	private String birthPlace1, birthPlace2;
	private String risidence1, risidence2;//atp only
	private String country1, country2;//wta only
	private String height1, height2;
	private String weight1, weight2;
	private String plays1, plays2;
	private String backhand1, backhand2;
	private String turnedPro1, turnedPro2;
	private String ytdWonLost1, ytdWonLost2;
	private String ytdTitles1, ytdTitles2;
	private String careerWonLost1, careerWonLost2;
	private String careerTitles1, careerTitles2;
	private String ytdPrize1, ytdPrize2;//wta only
	private String careerPrize1, careerPrize2;
	private List<Record> recordList;
	
	
	public String getPlayer1Id() {
		return player1Id;
	}
	public void setPlayer1Id(String player1Id) {
		this.player1Id = player1Id;
	}
	public String getPlayer2Id() {
		return player2Id;
	}
	public void setPlayer2Id(String player2Id) {
		this.player2Id = player2Id;
	}
	public int getRank1() {
		return rank1;
	}
	public void setRank1(int rank1) {
		this.rank1 = rank1;
	}
	public int getRank2() {
		return rank2;
	}
	public void setRank2(int rank2) {
		this.rank2 = rank2;
	}
	public int getWins1() {
		return wins1;
	}
	public void setWins1(int wins1) {
		this.wins1 = wins1;
	}
	public int getWins2() {
		return wins2;
	}
	public void setWins2(int wins2) {
		this.wins2 = wins2;
	}
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	public String getImgUrl1() {
		return imgUrl1;
	}
	public void setImgUrl1(String imgUrl1) {
		this.imgUrl1 = imgUrl1;
	}
	public String getImgUrl2() {
		return imgUrl2;
	}
	public void setImgUrl2(String imgUrl2) {
		this.imgUrl2 = imgUrl2;
	}
	public String getAge1() {
		return age1;
	}
	public void setAge1(String age1) {
		this.age1 = age1;
	}
	public String getAge2() {
		return age2;
	}
	public void setAge2(String age2) {
		this.age2 = age2;
	}
	public String getBirthPlace1() {
		return birthPlace1;
	}
	public void setBirthPlace1(String birthPlace1) {
		this.birthPlace1 = birthPlace1;
	}
	public String getBirthPlace2() {
		return birthPlace2;
	}
	public void setBirthPlace2(String birthPlace2) {
		this.birthPlace2 = birthPlace2;
	}
	public String getRisidence1() {
		return risidence1;
	}
	public void setRisidence1(String risidence1) {
		this.risidence1 = risidence1;
	}
	public String getRisidence2() {
		return risidence2;
	}
	public String getCountry1() {
		return country1;
	}
	public void setCountry1(String country1) {
		this.country1 = country1;
	}
	public String getCountry2() {
		return country2;
	}
	public void setCountry2(String country2) {
		this.country2 = country2;
	}
	public void setRisidence2(String risidence2) {
		this.risidence2 = risidence2;
	}
	public String getHeight1() {
		return height1;
	}
	public void setHeight1(String height1) {
		this.height1 = height1;
	}
	public String getHeight2() {
		return height2;
	}
	public void setHeight2(String height2) {
		this.height2 = height2;
	}
	public String getWeight1() {
		return weight1;
	}
	public void setWeight1(String weight1) {
		this.weight1 = weight1;
	}
	public String getWeight2() {
		return weight2;
	}
	public void setWeight2(String weight2) {
		this.weight2 = weight2;
	}
	public String getPlays1() {
		return plays1;
	}
	public void setPlays1(String plays1) {
		this.plays1 = plays1;
	}
	public String getPlays2() {
		return plays2;
	}
	public void setPlays2(String plays2) {
		this.plays2 = plays2;
	}
	public String getBackhand1() {
		return backhand1;
	}
	public void setBackhand1(String backhand1) {
		this.backhand1 = backhand1;
	}
	public String getBackhand2() {
		return backhand2;
	}
	public void setBackhand2(String backhand2) {
		this.backhand2 = backhand2;
	}
	public String getTurnedPro1() {
		return turnedPro1;
	}
	public void setTurnedPro1(String turnedPro1) {
		this.turnedPro1 = turnedPro1;
	}
	public String getTurnedPro2() {
		return turnedPro2;
	}
	public void setTurnedPro2(String turnedPro2) {
		this.turnedPro2 = turnedPro2;
	}
	public String getYtdWonLost1() {
		return ytdWonLost1;
	}
	public void setYtdWonLost1(String ytdWonLost1) {
		this.ytdWonLost1 = ytdWonLost1;
	}
	public String getYtdWonLost2() {
		return ytdWonLost2;
	}
	public void setYtdWonLost2(String ytdWonLost2) {
		this.ytdWonLost2 = ytdWonLost2;
	}
	public String getYtdTitles1() {
		return ytdTitles1;
	}
	public void setYtdTitles1(String ytdTitles1) {
		this.ytdTitles1 = ytdTitles1;
	}
	public String getYtdTitles2() {
		return ytdTitles2;
	}
	public void setYtdTitles2(String ytdTitles2) {
		this.ytdTitles2 = ytdTitles2;
	}
	public String getCareerWonLost1() {
		return careerWonLost1;
	}
	public void setCareerWonLost1(String careerWonLost1) {
		this.careerWonLost1 = careerWonLost1;
	}
	public String getCareerWonLost2() {
		return careerWonLost2;
	}
	public void setCareerWonLost2(String careerWonLost2) {
		this.careerWonLost2 = careerWonLost2;
	}
	public String getCareerTitles1() {
		return careerTitles1;
	}
	public void setCareerTitles1(String careerTitles1) {
		this.careerTitles1 = careerTitles1;
	}
	public String getCareerTitles2() {
		return careerTitles2;
	}
	public void setCareerTitles2(String careerTitles2) {
		this.careerTitles2 = careerTitles2;
	}
	public String getYtdPrize1() {
		return ytdPrize1;
	}
	public void setYtdPrize1(String ytdPrize1) {
		this.ytdPrize1 = ytdPrize1;
	}
	public String getYtdPrize2() {
		return ytdPrize2;
	}
	public void setYtdPrize2(String ytdPrize2) {
		this.ytdPrize2 = ytdPrize2;
	}
	public String getCareerPrize1() {
		return careerPrize1;
	}
	public void setCareerPrize1(String careerPrize1) {
		this.careerPrize1 = careerPrize1;
	}
	public String getCareerPrize2() {
		return careerPrize2;
	}
	public void setCareerPrize2(String careerPrize2) {
		this.careerPrize2 = careerPrize2;
	}

	public List<Record> getRecordList() {
		return recordList;
	}
	public void setRecordList(List<Record> recordList) {
		this.recordList = recordList;
	}
	
}
