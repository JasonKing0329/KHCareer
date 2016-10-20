package com.king.mytennis.view.settings;

import java.io.Serializable;

public class AutoFillItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int courtIndex;
	private int levelIndex;
	private int regionIndex;
	private int roundIndex;
	private String match;
	private String country;
	private String city;
	private String matchPinyin;
	public int getCourtIndex() {
		return courtIndex;
	}
	public void setCourtIndex(int courtIndex) {
		this.courtIndex = courtIndex;
	}
	public int getLevelIndex() {
		return levelIndex;
	}
	public void setLevelIndex(int levelIndex) {
		this.levelIndex = levelIndex;
	}
	public int getRegionIndex() {
		return regionIndex;
	}
	public void setRegionIndex(int regionIndex) {
		this.regionIndex = regionIndex;
	}
	public int getRoundIndex() {
		return roundIndex;
	}
	public void setRoundIndex(int roundIndex) {
		this.roundIndex = roundIndex;
	}
	public String getMatch() {
		return match;
	}
	public void setMatch(String match) {
		this.match = match;
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

	public String getMatchPinyin() {
		return matchPinyin;
	}

	public void setMatchPinyin(String matchPinyin) {
		this.matchPinyin = matchPinyin;
	}
}
