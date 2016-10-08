package com.king.mytennis.update_v_6_0.controller;

import java.util.HashMap;
import java.util.List;

import com.king.mytennis.model.CountData;
import com.king.mytennis.model.CountDataPlayer;

public interface CardContentProvider {

	public void prepareYearCard();
	public List<String> getYearList();
	/**
	 * key String: year (eg. 2015)
	 * value List<HashMap<String, String>>
	 * 		key -- value : "date" -- eg. 12月
	 *                     "match" -- eg. xxx公开赛
	 *                     "glory" -- eg. Winner
	 *                     "recPos" -- record the position in record list of match items, use '-' to separate 
	 * @return
	 */
	public List<HashMap<String, String>> getYearCard(String year) throws CardDataUnprepareException;

	public void prepareMatchCard();
	/**
	 * key-value:
	 * match-match name
	 * court-court type
	 * @return
	 */
	public List<HashMap<String, String>> getMatchList();
	/**
	 * key -- value : 
	 * "date" -- eg. 2012-08
	 * "line1" -- eg. Final Djokovic
	 * "line2" -- eg. Win 7-5/6-4
	 */
	public List<HashMap<String, String>> getMatchCard(String match) throws CardDataUnprepareException;

	public void preparePlayerIndexCard();
	public List<String> getPlayerIndexList();
	/**
	 * key -- value : 
	 * "player" -- eg. Djokovic
	 * "country" -- eg. China
	 * "h2h" -- eg. 6-0
	 * @return
	 */
	public List<HashMap<String, String>> getPlayerIndexCard(String index) throws CardDataUnprepareException;

	/**
	 *
	 * @param year if -1, then get total count
	 * @return
	 */
	public static final String FLAG_COUNT_DATA_WHOLE = "Career";
	public CountData getYearCountData(String year) throws CardDataUnprepareException;
	public CountDataPlayer getPlayerCountData(String year) throws CardDataUnprepareException;

}
