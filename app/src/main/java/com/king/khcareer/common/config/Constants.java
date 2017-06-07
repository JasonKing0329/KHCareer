package com.king.khcareer.common.config;

import com.king.mytennis.view.R;

import static android.R.attr.name;

public class Constants {

	public static final int SLIDING_MENU_DEFAULT_BK = R.drawable.wall_bk9;

	public static final String SCORE_RETIRE = "W/O";

	public static final String SCORE_RETIRE_NORMAL = "(对手退赛)";

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

	public static final String[] RECORD_GS_ROUNDS_GLORY = new String[] {

			"冠军",
			"亚军",
			"四强",
			"八强",
			"第四轮",
			"第三轮",
			"第二轮",
			"第一轮",
			"",
			""
	};

	/**
	 * RECORD_MATCH_ROUNDS对照的RECORD_GS_ROUNDS_GLORY
	 * @param round
	 * @param isWinner
	 * @return
	 */
	public static final String getGsGloryForRound(String round, boolean isWinner) {
		if (round.equals(RECORD_MATCH_ROUNDS[0]) && isWinner) {
			return RECORD_GS_ROUNDS_GLORY[0];
		}

		String glory = "--";
		for (int i = 0; i < RECORD_MATCH_ROUNDS.length; i ++) {
			if (RECORD_MATCH_ROUNDS[i].equals(round)) {
				glory = RECORD_GS_ROUNDS_GLORY[i + 1];
			}
		}
		return glory;
	}

	public static final String MATCH_CONST_MONTECARLO = "蒙特卡洛大师赛";

	public static final String[] MONTH_ENG = new String[] {

			"January",
			"February",
			"March",
			"April",
			"May",
			"June",
			"July",
			"August",
			"September",
			"October",
			"November",
			"December"
	};

	/**
	 * record记录有改动（增删改）标志
	 */
	public static final int FLAG_RECORD_UPDATE = 1030;

}
