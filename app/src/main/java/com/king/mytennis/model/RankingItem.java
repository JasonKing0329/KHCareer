package com.king.mytennis.model;

public class RankingItem {

	private int ranking;
	private int update;
	private String name;
	private String country;
	private int matchNumber;
	private int score;
	
	public RankingItem() {
		
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public int getUpdate() {
		return update;
	}

	public void setUpdate(int update) {
		this.update = update;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getMatchNumber() {
		return matchNumber;
	}

	public void setMatchNumber(int matchNumber) {
		this.matchNumber = matchNumber;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
}
