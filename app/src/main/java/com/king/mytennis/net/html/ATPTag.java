package com.king.mytennis.net.html;

public class ATPTag {

	//v5.6.1 change, website has updated
	@Deprecated
	public static final String H2H_URL_PLAYER1_ID = "pId";
	@Deprecated
	public static final String H2H_URL_PLAYER2_ID = "oId";
	@Deprecated
	public static final String H2H_HEAD_MAIN_TABLE = "head2HeadMainTable";
	@Deprecated
	public static final String H2H_HEAD_TABLE = "head2HeadTables";
	@Deprecated
	public static final String H2H_CONTAINER = "module-h2h-container";
	
	//v5.6.1 change, website has updated
	/**
	 * scripttag is not available
	public static final String H2H_PLAYER_TABLE = "h2h-table h2h-table-ytd";
	public static final String H2H_EVENT_TABLE = "modal-event-breakdown-table";
	 */
	public static final String H2H_SCORE_TABLE = "modal-scores-h2h-players";
	public static final String H2H_PLAYER_TABLE = "<table class=\"h2h-table h2h-table-ytd\">";
	public static final String H2H_EVENT_TABLE = "<table class=\"modal-event-breakdown-table\">";
	
	public static final String BASE_URL = "http://www.atpworldtour.com";
	public static final String H2H_URL = BASE_URL + "/players/fedex-head-2-head/";
}
