package com.king.mytennis.score;

/**
 * 描述: 积分对照表
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/21 9:25
 */
public class ScoreTable {

    /**
     * row refer spinner_level
     * col refer spinner_round, the column 0 means winner, others' index + 1
     */
    private static int[][] tables = new int[][] {
            {2000, 1200, 720, 360, 180, 90, 45, 10, 0, 0}, // Grand Slam
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Master cup，计分方式不参照该表，仅占位
            {1000, 600, 360, 180, 90, 45, 10, 10, 0, 0}, // ATP1000
            {500, 300, 180, 90, 45, 20, 0, 0, 0, 0}, // ATP500
            {250, 150, 90, 45, 20, 5, 0, 0, 0, 0}, // ATP250
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Davis Cup，无积分，仅占位
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0} // Olympics，无积分，仅占位
    };

    public static int getScore(String round, String level, boolean isWinner, String[] arrRound, String[] arrLevel) {
        int indexRound = 0;
        if (arrRound[0].equals(round) && isWinner) {
            indexRound = 0;
        }
        else {
            for (int i = 0; i < arrRound.length; i ++) {
                if (arrRound[i].equals(round)) {
                    indexRound = i + 1;
                    break;
                }
            }
        }
        int indexLevel = 0;
        for (int i = 0; i < arrLevel.length; i ++) {
            if (arrLevel[i].equals(level)) {
                indexLevel = i;
                break;
            }
        }

        return tables[indexLevel][indexRound];
    }
}
