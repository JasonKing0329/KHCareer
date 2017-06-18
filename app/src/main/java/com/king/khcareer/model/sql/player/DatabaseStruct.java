package com.king.khcareer.model.sql.player;

public class DatabaseStruct {

	public static final String DATABASE="mytennis";
	public static final String DATABASE_TIANQI="mytennis_TianQi";
	public static final String DATABASE_FLAMENCO="mytennis_Flamenco";
	public static final String DATABASE_HENRY="mytennis_Henry";
	public static final String DATABASE_PUBLIC="mytennis_public.db";

	public static final String TABLE_RECORD="record";
	public static final String[] TABLE_RECORD_COL = {
		"id","rankp1","seedp1","competitor","competitor_country","rank"
		,"seed","date_str","date_long","level","court","region"
		,"match_country","match_city","match","round","iswinner","score","index_date"};
	public static final String TABLE_RECORD_PARAM
		="(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,rankp1 int,seedp1 int,competitor varchar(20)" +
				",competitor_country varchar(20),rank int,seed int" +
				",date_str varchar(10),date_long varchar(15),level varchar(15)" +
				",court varchar(10),region varchar(15),match_country varchar(15)" +
				",match_city varchar(15),match varchar(25),round varchar(12)" +
				",iswinner varchar(6),score varchar(25)" +
				",index_date float)";
	public static final int COL_ID=0;
	public static final int COL_rankp1=1;
	public static final int COL_seedp1=2;
	public static final int COL_competitor=3;
	public static final int COL_competitor_country=4;
	public static final int COL_rank=5;
	public static final int COL_seed=6;
	public static final int COL_date_str=7;
	public static final int COL_date_long=8;
	public static final int COL_level=9;
	public static final int COL_court=10;
	public static final int COL_region=11;
	public static final int COL_match_country=12;
	public static final int COL_match_city=13;
	public static final int COL_match=14;
	public static final int COL_round=15;
	public static final int COL_iswinner=16;
	public static final int COL_score=17;
	public static final int COL_index_date=18;

	public static final String TABLE_PWD="password";
	public static final String[] TABLE_PWD_COL={"id", "password", "question", "answer", "other"};
	public static final String TABLE_PWD_PARAM="(id int primary key, password varchar(20), question varchar(20), answer varchar(20), other varchar(40))";
	public static final int COL_pwd = 1;
	public static final int COL_question = 2;
	public static final int COL_answer = 3;
	public static final int COL_other = 4;

	public static final String TABLE_CONF="configuration";
	public static final String[] TABLE_CONF_COL={"id", "def_language"};
	public static final String TABLE_CONF_PARAM="(id int primary key, def_language varchar(10))";
	public static final int COL_LANGUAGE=1;

	public static final String TABLE_SEQUENCE="sqlite_sequence";

	public static final String TABLE_RANK_FINAL="rank_final";
	public static final String[] TTABLE_RANK_FINAL_COL={"_id", "_year", "_rank"};

	public static final String VIEW_GS_RESULT= "gs_result";
	public static final String VIEW_MASTER_RESULT= "master_result";
	public static final String VIEW_H2H= "h2hview";

	/**
	 * public database
	 */
	public static final String TABLE_MATCH = "_match";
	public static final String TABLE_MATCH_NAME = "_match_name";
	public static final String TABLE_PLAYER= "_player";
}
