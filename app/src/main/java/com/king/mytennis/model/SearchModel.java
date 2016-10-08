package com.king.mytennis.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.king.mytennis.interfc.DatabaseAccess;
import com.king.mytennis.multiuser.MultiUserManager;

public class SearchModel {

	private boolean courtOn;
	private boolean levelOn;
	private boolean matchOn;
	private boolean roundOn;
	private boolean regionOn;
	private boolean mCountryOn;
	private boolean competitorOn;
	private boolean cptCountryOn;
	private boolean rankOn;
	private boolean dateOn;
	private boolean scoreOn;
	private boolean isWinnerOn;
	private String competitor;
	private String cptCountry;
	private String mCountry;
	private String court;
	private String level;
	private String round;
	private String region;
	private String match;
	private boolean isWinner;
	private int rankMin, rankMax;
	private long date_start, date_end;
	private String score;

	public SearchModel() {

	}

	public boolean isCourtOn() {
		return courtOn;
	}
	public void setCourtOn(boolean courtOn) {
		this.courtOn = courtOn;
	}
	public boolean isLevelOn() {
		return levelOn;
	}
	public void setIsWinnerOn(boolean isWinnerOn) {
		this.isWinnerOn = isWinnerOn;
	}
	public boolean isWinnerOn() {
		return levelOn;
	}
	public void setLevelOn(boolean levelOn) {
		this.levelOn = levelOn;
	}
	public boolean isMatchOn() {
		return matchOn;
	}
	public void setMatchOn(boolean matchOn) {
		this.matchOn = matchOn;
	}
	public boolean isRoundOn() {
		return roundOn;
	}
	public void setRoundOn(boolean roundOn) {
		this.roundOn = roundOn;
	}
	public boolean isRegionOn() {
		return regionOn;
	}
	public void setRegionOn(boolean regionOn) {
		this.regionOn = regionOn;
	}
	public boolean ismCountryOn() {
		return mCountryOn;
	}
	public void setmCountryOn(boolean mCountryOn) {
		this.mCountryOn = mCountryOn;
	}
	public boolean isCompetitorOn() {
		return competitorOn;
	}
	public void setCompetitorOn(boolean competitorOn) {
		this.competitorOn = competitorOn;
	}
	public boolean isCptCountryOn() {
		return cptCountryOn;
	}
	public void setCptCountryOn(boolean cptCountryOn) {
		this.cptCountryOn = cptCountryOn;
	}
	public boolean isRankOn() {
		return rankOn;
	}
	public void setRankOn(boolean rankOn) {
		this.rankOn = rankOn;
	}
	public boolean isDateOn() {
		return dateOn;
	}
	public void setDateOn(boolean dateOn) {
		this.dateOn = dateOn;
	}
	public boolean isScoreOn() {
		return scoreOn;
	}
	public void setScoreOn(boolean scoreOn) {
		this.scoreOn = scoreOn;
	}

	public void setCompetitor(String competitor) {
		this.competitor = competitor;
	}

	public void setCptCountry(String cptCountry) {
		this.cptCountry = cptCountry;
	}

	public void setmCountry(String mCountry) {
		this.mCountry = mCountry;
	}

	public void setCourt(String court) {
		this.court = court;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}

	public void setRankMin(int rankMin) {
		this.rankMin = rankMin;
	}

	public void setRankMax(int rankMax) {
		this.rankMax = rankMax;
	}

	public void setDate_start(long date_start) {
		this.date_start = date_start;
	}

	public void setDate_end(long date_end) {
		this.date_end = date_end;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public void reset() {
		courtOn = false;
		levelOn = false;
		matchOn = false;
		roundOn = false;
		regionOn = false;
		mCountryOn = false;
		competitorOn = false;
		cptCountryOn = false;
		rankOn = false;
		dateOn = false;
		scoreOn = false;
	}

	public List<Record> searchFromList(List<Record> list) {

		if (list == null || list.size() == 0) {
			return null;
		}
		List<Record> newList=new ArrayList<Record>();
		if (competitorOn) {
			String name;
			for (int i=0, n=list.size(); i<n; i++) {
				name=list.get(i).getCompetitor();
				if (name.contains(competitor)) {
					newList.add(list.get(i));
				}
			}
			list.clear();
			for (int i=0,n=newList.size();i<n;i++){
				list.add(newList.get(i));
			}
			newList.clear();
		}
		if (cptCountryOn) {
			String str;
			for (int i=0, n=list.size(); i<n; i++) {
				str=list.get(i).getCptCountry();
				if (str.contains(cptCountry)) {
					newList.add(list.get(i));
				}
			}
			list.clear();
			for (int i=0,n=newList.size();i<n;i++){
				list.add(newList.get(i));
			}
			newList.clear();
		}
		if (mCountryOn) {
			String str;
			for (int i=0, n=list.size(); i<n; i++) {
				str=list.get(i).getMatchCountry();
				if (str.contains(mCountry)) {
					newList.add(list.get(i));
				}
			}
			list.clear();
			for (int i=0,n=newList.size();i<n;i++){
				list.add(newList.get(i));
			}
			newList.clear();
		}
		if (courtOn) {
			String str;
			for (int i=0, n=list.size(); i<n; i++) {
				str=list.get(i).getCourt();
				if (str.contains(court)) {
					newList.add(list.get(i));
				}
			}
			list.clear();
			for (int i=0,n=newList.size();i<n;i++){
				list.add(newList.get(i));
			}
			newList.clear();
		}
		if (levelOn) {
			String str;
			for (int i=0, n=list.size(); i<n; i++) {
				str=list.get(i).getLevel();
				if (str.matches(level)) {
					newList.add(list.get(i));
				}
			}
			list.clear();
			for (int i=0,n=newList.size();i<n;i++){
				list.add(newList.get(i));
			}
			newList.clear();
		}
		if (roundOn) {
			String str;
			for (int i=0, n=list.size(); i<n; i++) {
				str=list.get(i).getRound();
				if (str.matches(round)) {
					newList.add(list.get(i));
				}
			}
			list.clear();
			for (int i=0,n=newList.size();i<n;i++){
				list.add(newList.get(i));
			}
			newList.clear();
		}
		if (regionOn) {
			String str;
			for (int i=0, n=list.size(); i<n; i++) {
				str=list.get(i).getRegion();
				if (str.matches(region)) {
					newList.add(list.get(i));
				}
			}
			list.clear();
			for (int i=0,n=newList.size();i<n;i++){
				list.add(newList.get(i));
			}
			newList.clear();
		}
		if (matchOn) {
			String str;
			for (int i=0, n=list.size(); i<n; i++) {
				str=list.get(i).getMatch();
				if (str.contains(match)) {
					newList.add(list.get(i));
				}
			}
			list.clear();
			for (int i=0,n=newList.size();i<n;i++){
				list.add(newList.get(i));
			}
			newList.clear();
		}
		if (rankOn) {

			int rank;
			for (int i=0, n=list.size(); i<n; i++) {
				rank=list.get(i).getCptRank();
				if (rank>=rankMin && rank<=rankMax) {
					newList.add(list.get(i));
				}
			}

			list.clear();
			for (int i=0,n=newList.size();i<n;i++){
				list.add(newList.get(i));
			}
			newList.clear();
		}
		if (dateOn) {

			long date;
			for (int i=0,n=list.size();i<n;i++){
				date=list.get(i).getLongDate();
				if (date>=date_start && date<=date_end) {
					newList.add(list.get(i));
				}
			}

			list.clear();
			for (int i=0,n=newList.size();i<n;i++){
				list.add(newList.get(i));
			}
			newList.clear();
		}

		if (isWinnerOn) {
			for (int i=0, n=list.size(); i<n; i++) {
				if (isWinner == list.get(i).getWinner().equals(MultiUserManager.USER_DB_FLAG)) {
					newList.add(list.get(i));
				}
			}
			list.clear();
			for (int i=0,n=newList.size();i<n;i++){
				list.add(newList.get(i));
			}
			newList.clear();
		}

		if (scoreOn) {

			searchScore(list, newList);
		}
		return list;
	}

	private void searchScore(List<Record> list, List<Record> newList) {

		String scores[]=null;
		final int BYAND=1;
		final int BYOR=2;
		int flag=0;

		if (score.indexOf('&')>=0) {
			scores=score.split(" & ");
			flag=BYAND;
		}
		else if (score.indexOf("and")>=0){
			scores=score.split(" and ");
			flag=BYAND;
		}
		else if (score.indexOf('|')>=0){
			scores=score.split(" | ");
			flag=BYOR;
		}
		else if (score.indexOf("or")>=0){
			scores=score.split(" or ");
			flag=BYOR;
		}
		String scoreFromList="";
		if (scores==null) {
			if (score!=null && score.length()!=0) {
				for (int i=0,n=list.size();i<n;i++){
					scoreFromList=list.get(i).getScore();
					if (scoreFromList.contains(score)) {
						newList.add(list.get(i));
					}
				}

				list.clear();
				for (int i=0,n=newList.size();i<n;i++){
					list.add(newList.get(i));
				}
				newList.clear();
			}
		}
		else {
			for (int i=0,n=list.size();i<n;i++){
				scoreFromList=list.get(i).getScore();
				boolean cmpTrue=false;
				if (flag==BYAND) {
					for (int ii=0; ii<scores.length; ii++){
						cmpTrue=scoreFromList.contains(scores[ii]);
						if (!cmpTrue) {//and关系中，当有一个为假就应该为假
							break;
						}
					}
				}
				else if (flag==BYOR) {
					for (int ii=0; ii<scores.length; ii++){
						cmpTrue=scoreFromList.contains(scores[ii]);
						if (cmpTrue) {//or关系中，当有一个为真就应该为真
							break;
						}
					}
				}
				if (cmpTrue) {
					newList.add(list.get(i));
				}
			}

			list.clear();
			for (int i=0,n=newList.size();i<n;i++){
				list.add(newList.get(i));
			}
			newList.clear();
		}
	}

	public ArrayList<Record> searchByCompetitor(Context context) {

		DatabaseAccess sqlite = new SQLiteDB(context);
		SQLiteDatabase db = sqlite.getSQLHelper().getReadableDatabase();
		Cursor cursor = db.query(DatabaseStruct.TABLE_RECORD, DatabaseStruct.TABLE_RECORD_COL
				, DatabaseStruct.TABLE_RECORD_COL[DatabaseStruct.COL_competitor] + "=?"
				, new String[]{competitor}, null, null, null);
		ArrayList<Record> list = new ArrayList<Record>();
		while (cursor.moveToNext()) {
			Record record = new Record();
			record.setId(cursor.getInt(DatabaseStruct.COL_ID));
			record.setCompetitor(cursor.getString(DatabaseStruct.COL_competitor));
			record.setCptCountry(cursor.getString(DatabaseStruct.COL_competitor_country));
			record.setCourt(cursor.getString(DatabaseStruct.COL_court));
			record.setLongDate(cursor.getLong(DatabaseStruct.COL_date_long));
			record.setStrDate(cursor.getString(DatabaseStruct.COL_date_str));
			record.setWinner(cursor.getString(DatabaseStruct.COL_iswinner));
			record.setLevel(cursor.getString(DatabaseStruct.COL_level));
			record.setMatch(cursor.getString(DatabaseStruct.COL_match));
			record.setCity(cursor.getString(DatabaseStruct.COL_match_city));
			record.setMatchCountry(cursor.getString(DatabaseStruct.COL_match_country));
			record.setRank(cursor.getInt(DatabaseStruct.COL_rankp1));
			record.setCptRank(cursor.getInt(DatabaseStruct.COL_rank));
			record.setSeed(cursor.getInt(DatabaseStruct.COL_seedp1));
			record.setCptSeed(cursor.getInt(DatabaseStruct.COL_seed));
			record.setRegion(cursor.getString(DatabaseStruct.COL_region));
			record.setRound(cursor.getString(DatabaseStruct.COL_round));
			record.setScore(cursor.getString(DatabaseStruct.COL_score));
			list.add(record);
		}
		return list;
	}
}
