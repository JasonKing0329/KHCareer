package com.king.mytennis.model;

import com.king.mytennis.view.R;

import static android.R.attr.name;

public class Constants {

	public static final int SLIDING_MENU_DEFAULT_BK = R.drawable.wall_bk9;

	public static final String SCORE_RETIRE = "W/O";
	
	public static final int[] MainViewMenu = new int[] {
		R.string.menu_save,
		R.string.menu_load,
		R.string.menu_saveas,
		R.string.menu_saveconf,
		R.string.menu_choosebk,
		R.string.menu_change_theme,
		R.string.exit
	};
	public static final int[] RecordListViewMenu = new int[] {
		R.string.menu_exactselect,
		R.string.menu_showall,
		R.string.menu_show_by_player,
		R.string.menu_change_theme,
		R.string.menu_saveas,
		R.string.exit
	};

	public static final String[] RECORD_MATCH_LEVELS = new String[] {

    	"Grand Slam",
    	"Master Cup",
    	"ATP1000",
    	"ATP500",
    	"ATP250",
    	"Davi\'s Cup",
		"Olympic Games"
	};

	public static final String[] RECORD_MATCH_COURTS = new String[] {

			"硬地",
			"红土",
			"草地",
			"室内硬地",
	};

	public static final String[] RECORD_MATCH_ROUNDS = new String[] {

			"Final",
			"Semi Final",
			"1/4 Final",
			"Round 16",
			"Round 32",
			"Round 64",
			"Round 128",
			"Group",
			"Bronze medal"
	};

	public static final String[] RECORD_MATCH_ROUNDS_SHORT = new String[] {

			"F",
			"SF",
			"QF",
			"R16",
			"R32",
			"R64",
			"R128",
			"Group",
			"Bronze"
	};

	public static final String MATCH_CONST_MONTECARLO = "蒙特卡洛大师赛";

}
