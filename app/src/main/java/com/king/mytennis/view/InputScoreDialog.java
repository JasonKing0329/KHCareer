package com.king.mytennis.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.mytennis.model.Constants;
import com.king.mytennis.multiuser.MultiUserManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/3 11:45
 */
public class InputScoreDialog extends CustomDialog {

    private ViewGroup[] groupSets;
    private EditText[] etPoints1, etPoints2;
    private TextView tvPlayer1, tvPlayer2;
    private ImageView[] ivDeletes;
    private EditText[] etTies;
    private TextView[] tvTies;
    private ViewGroup groupAdd;

    private CheckBox cbRetire1, cbRetire2;
    private CheckBox cbWO1, cbWO2;
    // 退赛情况只能勾选一种，也可以全都不勾选
    private CheckBox[] checkBoxes;

    private int nSetCount;
    private String competitor;
    private String winner;

    public InputScoreDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        HashMap<String, Object> map = new HashMap<>();
        actionListener.onLoadData(map);

        String score = (String) map.get("score");
        competitor = (String) map.get("competitor");
        winner = (String) map.get("winner");
        tvPlayer1.setText(MultiUserManager.getInstance().getCurrentUser().getDisplayName());
        tvPlayer2.setText(competitor);
        initScore(score);
    }

    private void initScore(String score) {
        if (TextUtils.isEmpty(score)) {
            nSetCount = 1;
        }
        else {
            if (score.equals(Constants.SCORE_RETIRE)) {
                if (winner.equals(competitor)) {
                    cbWO1.setChecked(true);
                }
                return;
            }

            String scores[] = score.split("/");
            nSetCount = scores.length;
            for (int i = 0; i < nSetCount; i ++) {
                groupSets[i].setVisibility(View.VISIBLE);
                // 只显示最末一盘的删除按钮
                if (i > 1) {
                    ivDeletes[i - 1].setVisibility(View.GONE);
                }
                ivDeletes[i].setVisibility(i == 0 ? View.GONE : View.VISIBLE);

                String item[] = scores[i].split("-");
                if (winner.equals(competitor)) {
                    if (item[0].contains(Constants.SCORE_RETIRE_NORMAL)) {
                        cbRetire1.setChecked(true);
                        etPoints1[i].setText(item[1]);
                        etPoints2[i].setText(String.valueOf(item[0].charAt(0)));
                    }
                    else if (item[1].contains(Constants.SCORE_RETIRE_NORMAL)) {
                        cbRetire1.setChecked(true);
                        etPoints1[i].setText(String.valueOf(item[1].charAt(0)));
                        etPoints2[i].setText(item[0]);
                    }
                    else {
                        etPoints1[i].setText(item[1]);
                        etPoints2[i].setText(item[0]);
                    }
                }
                else {
                    if (item[0].contains(Constants.SCORE_RETIRE_NORMAL)) {
                        cbRetire2.setChecked(true);
                        etPoints1[i].setText(String.valueOf(item[1].charAt(0)));
                        etPoints2[i].setText(item[1]);
                    }
                    else if (item[1].contains(Constants.SCORE_RETIRE_NORMAL)) {
                        cbRetire2.setChecked(true);
                        etPoints1[i].setText(item[0]);
                        etPoints2[i].setText(String.valueOf(item[1].charAt(0)));
                    }
                    else {
                        etPoints1[i].setText(item[0]);
                        etPoints2[i].setText(item[1]);
                    }
                }
            }
        }
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dlg_input_score, null);
        groupAdd = (ViewGroup) view.findViewById(R.id.input_add_set);
        groupAdd.setOnClickListener(this);
        tvPlayer1 = (TextView) view.findViewById(R.id.input_title_player1);
        tvPlayer2 = (TextView) view.findViewById(R.id.input_title_player2);
        cbRetire1 = (CheckBox) view.findViewById(R.id.input_retire1);
        cbRetire2 = (CheckBox) view.findViewById(R.id.input_retire2);
        cbWO1 = (CheckBox) view.findViewById(R.id.input_wo1);
        cbWO2 = (CheckBox) view.findViewById(R.id.input_wo2);
        checkBoxes = new CheckBox[4];
        checkBoxes[0] = cbRetire1;
        checkBoxes[1] = cbRetire2;
        checkBoxes[2] = cbWO1;
        checkBoxes[3] = cbWO2;
        for (int i = 0 ; i < 4; i ++) {
            checkBoxes[i].setOnClickListener(this);
        }
        
        groupSets = new ViewGroup[5];
        groupSets[0] = (ViewGroup) view.findViewById(R.id.input_group_set1);
        groupSets[1] = (ViewGroup) view.findViewById(R.id.input_group_set2);
        groupSets[2] = (ViewGroup) view.findViewById(R.id.input_group_set3);
        groupSets[3] = (ViewGroup) view.findViewById(R.id.input_group_set4);
        groupSets[4] = (ViewGroup) view.findViewById(R.id.input_group_set5);
        etPoints1 = new EditText[5];
        etPoints1[0] = (EditText) view.findViewById(R.id.input_set1_score1);
        etPoints1[1] = (EditText) view.findViewById(R.id.input_set2_score1);
        etPoints1[2] = (EditText) view.findViewById(R.id.input_set3_score1);
        etPoints1[3] = (EditText) view.findViewById(R.id.input_set4_score1);
        etPoints1[4] = (EditText) view.findViewById(R.id.input_set5_score1);
        etPoints2 = new EditText[5];
        etPoints2[0] = (EditText) view.findViewById(R.id.input_set1_score2);
        etPoints2[1] = (EditText) view.findViewById(R.id.input_set2_score2);
        etPoints2[2] = (EditText) view.findViewById(R.id.input_set3_score2);
        etPoints2[3] = (EditText) view.findViewById(R.id.input_set4_score2);
        etPoints2[4] = (EditText) view.findViewById(R.id.input_set5_score2);
        etTies = new EditText[5];
        etTies[0] = (EditText) view.findViewById(R.id.input_set1_tie_edit);
        etTies[1] = (EditText) view.findViewById(R.id.input_set2_tie_edit);
        etTies[2] = (EditText) view.findViewById(R.id.input_set3_tie_edit);
        etTies[3] = (EditText) view.findViewById(R.id.input_set4_tie_edit);
        etTies[4] = (EditText) view.findViewById(R.id.input_set5_tie_edit);
        ivDeletes = new ImageView[5];
        ivDeletes[0] = (ImageView) view.findViewById(R.id.input_set1_delete);
        ivDeletes[0].setVisibility(View.GONE);
        ivDeletes[1] = (ImageView) view.findViewById(R.id.input_set2_delete);
        ivDeletes[2] = (ImageView) view.findViewById(R.id.input_set3_delete);
        ivDeletes[3] = (ImageView) view.findViewById(R.id.input_set4_delete);
        ivDeletes[4] = (ImageView) view.findViewById(R.id.input_set5_delete);
        for (int i = 0; i < 5; i ++) {
            ivDeletes[i].setOnClickListener(this);
        }
        tvTies = new TextView[5];
        tvTies[0] = (TextView) view.findViewById(R.id.input_set1_tie);
        tvTies[1] = (TextView) view.findViewById(R.id.input_set2_tie);
        tvTies[2] = (TextView) view.findViewById(R.id.input_set3_tie);
        tvTies[3] = (TextView) view.findViewById(R.id.input_set4_tie);
        tvTies[4] = (TextView) view.findViewById(R.id.input_set5_tie);
        for (int i = 0; i < 5; i ++) {
            tvTies[i].setOnClickListener(this);
        }
        return view;
    }

    @Override
    protected View getCustomToolbar() {
        return null;
    }

    /**
     * 只支持删除当前最末一盘（set1不允许删除），只能逐盘删除
     * @param view
     */
    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == saveButton) {
            saveInput();
        }
        else if (view.getId() == R.id.input_add_set) {
            // 增加一盘，隐藏上一盘的删除按钮。达到5盘后隐藏add set
            groupSets[nSetCount].setVisibility(View.VISIBLE);
            ivDeletes[nSetCount].setVisibility(View.VISIBLE);
            if (nSetCount != 1) {
                ivDeletes[nSetCount - 1].setVisibility(View.GONE);
            }
            nSetCount ++;
            if (nSetCount == 5) {
                groupAdd.setVisibility(View.GONE);
            }
        } else if (view instanceof CheckBox) {// 各种退赛情况只能有一种，或者都没有
            boolean targetCheck = false;
            CheckBox cb = (CheckBox) view;
            // onClickListener中check状态是已经完成check的状态
            if (cb.isChecked()) {
                targetCheck = true;
            }
            if (targetCheck) {
                for (int i = 0; i < checkBoxes.length; i++) {
                    if (checkBoxes[i] != view) {
                        checkBoxes[i].setChecked(false);
                    }
                }
            }
        } else {
            // 删除最末一盘，恢复上一盘的删除按钮，重新显示add set
            for (int i = 0; i < ivDeletes.length; i++) {
                if (view == ivDeletes[i]) {
                    groupAdd.setVisibility(View.VISIBLE);
                    nSetCount--;
                    if (nSetCount > 1) {
                        ivDeletes[nSetCount - 1].setVisibility(View.VISIBLE);
                    }
                    groupSets[nSetCount].setVisibility(View.GONE);
                    etPoints1[nSetCount].setText("");
                    etPoints2[nSetCount].setText("");
                    return;
                }
            }

            for (int i = 0; i < tvTies.length; i++) {
                if (view == tvTies[i]) {
                    if (etTies[i].getVisibility() == View.VISIBLE) {
                        etTies[i].setText("");
                        etTies[i].setVisibility(View.GONE);
                    } else {
                        etTies[i].setVisibility(View.VISIBLE);
                    }
                    return;
                }
            }
        }
    }

    private void saveInput() {
        String score;
        EditText[] winnerPoint;
        EditText[] loserPoint;
        boolean isWinner;// current user
        boolean isNormalRetire;
        boolean isWO;
        if (cbRetire1.isChecked()) {
            winnerPoint = etPoints2;
            loserPoint = etPoints1;
            isWinner = false;
            isNormalRetire = true;
            isWO = false;
        }
        else if (cbRetire2.isChecked()) {
            winnerPoint = etPoints1;
            loserPoint = etPoints2;
            isWinner = true;
            isNormalRetire = true;
            isWO = false;
        }
        else if (cbWO1.isChecked()) {
            winnerPoint = etPoints2;
            loserPoint = etPoints1;
            isWinner = false;
            isNormalRetire = false;
            isWO = true;
        }
        else if (cbWO2.isChecked()) {
            winnerPoint = etPoints1;
            loserPoint = etPoints2;
            isWinner = true;
            isNormalRetire = false;
            isWO = true;
        }
        else {
            // 根据比分判定winner
            int setWin = 0;
            int setLose = 0;
            for (int i = 0; i < groupSets.length; i ++) {
                if (groupSets[i].getVisibility() == View.VISIBLE) {
                    if (Integer.parseInt(etPoints1[i].getText().toString())
                            > Integer.parseInt(etPoints2[i].getText().toString())) {
                        setWin ++;
                    }
                    else {
                        setLose ++;
                    }
                }
            }

            isWinner = setWin > setLose;
            winnerPoint = isWinner ? etPoints1:etPoints2;
            loserPoint = isWinner ? etPoints2:etPoints1;
            isNormalRetire = false;
            isWO = false;

        }
        score = formatScore(winnerPoint, loserPoint, isNormalRetire, isWO);

        Map<String, String> results = new HashMap<>();
        results.put("winner", isWinner ? MultiUserManager.getInstance().getCurrentUser().getDisplayName():competitor);
        results.put("score", score);
        actionListener.onSave(results);
        dismiss();
    }

    /**
     *
     * @param winnerPoint
     * @param loserPoint
     * @param isNormalRetire 中途退赛（已有比分）
     * @param isWO 赛前退赛（显示为W/O）
     * @return
     */
    private String formatScore(EditText[] winnerPoint, EditText[] loserPoint, boolean isNormalRetire, boolean isWO) {
        if (isWO) {
            return Constants.SCORE_RETIRE;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < groupSets.length; i ++) {
            if (groupSets[i].getVisibility() == View.VISIBLE) {
                if (i > 0) {
                    buffer.append("/");
                }
                buffer.append(winnerPoint[i].getText().toString()).append("-").append(loserPoint[i].getText().toString());
                if (etTies[i].getVisibility() == View.VISIBLE) {// 抢七
                    buffer.append("(").append(etTies[i].getText().toString()).append(")");
                }
            }
        }
        if (isNormalRetire) {
            buffer.append(Constants.SCORE_RETIRE_NORMAL);
        }
        return buffer.toString();
    }
}
