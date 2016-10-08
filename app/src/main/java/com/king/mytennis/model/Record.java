package com.king.mytennis.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Record implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String competitor, cptCountry, match, matchCountry;
	private String level, court, region, city, round, score, strDate;
	private String winner;
	private int cptRank, cptSeed;
	private int rank, seed;
	private long longDate;

	public Record() {
		competitor = "";
		cptCountry = "";
		level = "";
		court = "";
		region = "";
		matchCountry = "";
		city = "";
		match = "";
		round = "";
		score = "";
		strDate = "";
	}

	public Record(Record record) {
		id = record.getId();
		competitor = record.getCompetitor();
		cptCountry = record.getCptCountry();
		cptRank = record.getCptRank();
		cptSeed = record.getCptSeed();
		winner = record.getWinner();
		level = record.getLevel();
		court = record.getCourt();
		region = record.getRegion();
		matchCountry = record.getMatchCountry();
		city = record.getCity();
		match = record.getMatch();
		round = record.getRound();
		score = record.getScore();
		strDate = record.getStrDate();
		rank = record.getRank();
		seed = record.getSeed();
		longDate = record.getLongDate();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompetitor() {
		return competitor;
	}

	public void setCompetitor(String competitor) {
		this.competitor = competitor;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getCptCountry() {
		return cptCountry;
	}

	public void setCptCountry(String cptCountry) {
		this.cptCountry = cptCountry;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public String getMatchCountry() {
		return matchCountry;
	}

	public void setMatchCountry(String matchCountry) {
		this.matchCountry = matchCountry;
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

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	public int getCptRank() {
		return cptRank;
	}

	public void setCptRank(int cptRank) {
		this.cptRank = cptRank;
	}

	public int getCptSeed() {
		return cptSeed;
	}

	public void setCptSeed(int cptSeed) {
		this.cptSeed = cptSeed;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public long getLongDate() {
		return longDate;
	}

	public void setLongDate(long longDate) {
		this.longDate = longDate;
	}

	/**
	 * warning:该方法要在setStrDate之后调用
	 */
	public void setLongDateFromStr() {
		this.longDate = formatDate(strDate);
	}

	private long formatDate(String strdate){

		long date;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		strdate+="-01";
		try {
			date=sdf.parse(strdate).getTime();
		} catch (ParseException e) {
			date=System.currentTimeMillis();
			e.printStackTrace();
		}
		return date;
	}
}
