package com.king.mytennis.model;

public class DatabaseStruct {

	public static final String DATABASE="mytennis";
	public static final String DATABASE_TIANQI="mytennis_TianQi";
	public static final String DATABASE_FLAMENCO="mytennis_Flamenco";
	public static final String DATABASE_HENRY="mytennis_Henry";
	public static final String DATABASE_PUBLIC="mytennis_public";

	/*
	public static final String TABLE_RECORD_ITEM="record_item";
	public static final String[] TABLE_RECORD_ITEM_COL = {
		"id", "rank", "seed", "rank_comp", "seed_comp", "date", "round", "score"
		, "iswinner", "player_id", "match_id"
	};
	public static final String TABLE_RECORD_ITEM_PARAM =
			"CREATE TABLE record_item ("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "rank INTEGER,"
			+ "seed INTEGER,"
			+ "rank_comp INTEGER,"
			+ "seed_comp INTEGER,"
			+ "date INTEGER,"
			+ "round TEXT,"
			+ "score TEXT,"
			+ "iswinner INTEGER,"
			+ "player_id INTEGER,"
			+ "match_id INTEGER"
			+ ")";
	

	public static final String TABLE_RECORD_PLAYER="record_player";
	public static final String[] TABLE_RECORD_PLAYER_COL = {
		"id", "name", "country", "city", "win", "lose", "img_url"
	};
	public static final String TABLE_RECORD_PLAYER_PARAM =
			"CREATE TABLE record_player ("
			+ "id INTEGER PRIMARY KEY NOT NULL,"
			+ "name TEXT,"
			+ "country TEXT,"
			+ "city TEXT,"
			+ "win INTEGER,"
			+ "lose INTEGER,"
			+ "img_url TEXT"
			+ ")";
	

	public static final String TABLE_RECORD_MATCH="record_match";
	public static final String[] TABLE_RECORD_MATCH_COL = {
		"id", "name", "country", "city", "court", "year", "level", "region", "img_url"
	};
	public static final String TABLE_RECORD_MATCH_PARAM =
			"CREATE TABLE record_match ("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ "name TEXT,"
			+ "country TEXT,"
			+ "city TEXT,"
			+ "court TEXT,"
			+ "year INTEGER,"
			+ "level TEXT,"
			+ "region TEXT,"
			+ "img_url TEXT"
			+ ")";
	*/

	public static final String TABLE_NAME_PINYIN="name_pinyin";
	public static final String[] TABLE_NAME_PINYIN_COL={"n_id", "n_name", "n_pinyin"};
	public static final String[] TABLE_NAME_PINYIN_COL_TOINSERT={"n_name", "n_pinyin"};
	public static final String TABLE_NAME_PINYIN_PARAM="(n_id INTEGER primary key AUTOINCREMENT, n_name TEXT, n_pinyin TEXT)";
	public static final int COL_NAME_PINYIN_ID=0;
	public static final int COL_NAME_PINYIN_NAME=1;
	public static final int COL_NAME_PINYIN_PINYIN=2;
	
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
	
	public static final String TABLE_ID="idfactory";
	public static final String[] TABLE_ID_COL={"id", "table_name", "table_new_id"};
	public static final String TABLE_ID_PARAM="(id int primary key, table_name varchar(20), table_new_id int)";
	public static final int COL_TABLE=1;
	public static final int COL_TABLENEWID=2;
	
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

	public static final String TABLE_MATCH_SEQ="match_seq";
	public static final String[] TABLE_MATCH_SEQ_COL = {
			"_id","name","sequence"};

	public static final String TABLE_SEQUENCE="sqlite_sequence";
}
