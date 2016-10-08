package com.king.mytennis.view_v_7_0.model;

import java.util.List;

import com.king.mytennis.model.Record;

/**
 * @author JingYang
 *
 */
public class MatchBean {

	private String name;
	private String namePinYin;
	private String country;
	private String city;
	private String region;
	private String level;
	private String court;
	private int win;
	private int lose;
	private String best;
	private String bestYears;
	
	private List<Record> recordList;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNamePinYin() {
		return namePinYin;
	}
	public void setNamePinYin(String namePinYin) {
		this.namePinYin = namePinYin;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getCourt() {
		return court;
	}
	public void setCourt(String court) {
		this.court = court;
	}
	public int getWin() {
		return win;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public int getLose() {
		return lose;
	}
	public void setLose(int lose) {
		this.lose = lose;
	}
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
	public List<Record> getRecordList() {
		return recordList;
	}
	public void setRecordList(List<Record> recordList) {
		this.recordList = recordList;
	}
	
}
