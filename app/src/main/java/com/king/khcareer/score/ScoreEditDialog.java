package com.king.khcareer.score;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.king.khcareer.model.FileIO;
import com.king.khcareer.rank.RankFinalBean;
import com.king.khcareer.base.CustomDialog;
import com.king.mytennis.view.R;

import java.util.HashMap;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 13:58
 */
public class ScoreEditDialog extends CustomDialog {

    public static final String KEY_MODE = "mode";
    public static final String KEY_INIT_RANK_FINAL_BEAN = "init_rank_final_bean";

    /**
     * current rank, top1 week, highest rank
     */
    public static final int MODE_COUNT_RANK = 0;

    /**
     * year rank
     */
    public static final int MODE_YEAR_RANK = 1;

    private int mode = MODE_COUNT_RANK;

    private EditText edtRank;
    private EditText edtRankHighest;
    private EditText edtTop1Week;
    private EditText etYear;
    private EditText etYearRank;
    private ViewGroup groupTop1Week;

    private ViewGroup groupCountRank;
    private ViewGroup groupYearRank;

    private RankBean rankBean;

    private RankFinalBean rankFinalBean;

    public ScoreEditDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);

        HashMap<String, Object> data = new HashMap<>();
        actionListener.onLoadData(data);
        if (data.get(KEY_MODE) != null) {
            mode = (int) data.get(KEY_MODE);
        }

        if (mode == MODE_YEAR_RANK) {
            groupCountRank.setVisibility(View.GONE);
            groupYearRank.setVisibility(View.VISIBLE);
            if (data.get(KEY_INIT_RANK_FINAL_BEAN) != null) {
                rankFinalBean = (RankFinalBean) data.get(KEY_INIT_RANK_FINAL_BEAN);
                if (rankFinalBean.getYear() != 0) {
                    etYear.setText(String.valueOf(rankFinalBean.getYear()));
                }
                if (rankFinalBean.getRank() != 0) {
                    etYearRank.setText(String.valueOf(rankFinalBean.getRank()));
                }
            }
        }
        else {
            rankBean = getRankData();
            if (rankBean == null) {
                rankBean = new RankBean();
            }
            else {
                edtRank.setText(String.valueOf(rankBean.getRank()));
                edtRankHighest.setText(String.valueOf(rankBean.getHighestRank()));
                showTop1Week();
            }
        }
    }

    public RankBean getRankData() {
        RankBean bean = new FileIO().readRankBean();
        return bean;
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_score_manage, null);
        edtRank = (EditText) view.findViewById(R.id.score_manage_rank);
        edtRankHighest = (EditText) view.findViewById(R.id.score_manage_rank_highest);
        etYear = (EditText) view.findViewById(R.id.et_year);
        etYearRank = (EditText) view.findViewById(R.id.et_rank);
        edtTop1Week = (EditText) view.findViewById(R.id.score_manage_top1_week);
        groupTop1Week = (ViewGroup) view.findViewById(R.id.score_manage_group_top1_week);
        groupYearRank = (ViewGroup) view.findViewById(R.id.group_rank_year);
        groupCountRank = (ViewGroup) view.findViewById(R.id.group_rank_count);
        edtRankHighest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    rankBean.setHighestRank(0);
                }
                else {
                    rankBean.setHighestRank(Integer.parseInt(s.toString()));
                    showTop1Week();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void showTop1Week() {
        if (rankBean.getHighestRank() == 1) {
            groupTop1Week.setVisibility(View.VISIBLE);
            edtTop1Week.setText(String.valueOf(rankBean.getTop1Week()));
        }
        else {
            groupTop1Week.setVisibility(View.GONE);
        }
    }

    @Override
    protected View getCustomToolbar() {
        return null;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == saveButton) {
            if (mode == MODE_YEAR_RANK) {
                if (rankFinalBean == null) {
                    rankFinalBean = new RankFinalBean();
                }
                String year = etYear.getText().toString();
                if (TextUtils.isEmpty(year)) {
                    etYear.setError("year can't be null");
                    return;
                }
                rankFinalBean.setYear(Integer.parseInt(year));
                String rank = etYearRank.getText().toString();
                if (TextUtils.isEmpty(rank)) {
                    etYearRank.setError("rank can't be null");
                    return;
                }
                rankFinalBean.setRank(Integer.parseInt(rank));
                actionListener.onSave(rankFinalBean);
            }
            else {
                String rank = edtRank.getText().toString();
                if (TextUtils.isEmpty(rank)) {
                    edtRank.setError("rank can't be null");
                    return;
                }
                rankBean.setRank(Integer.parseInt(rank));
                String highest = edtRankHighest.getText().toString();
                if (TextUtils.isEmpty(rank)) {
                    edtRankHighest.setError("highest rank can't be null");
                    return;
                }
                rankBean.setHighestRank(Integer.parseInt(highest));
                if (groupTop1Week.getVisibility() == View.VISIBLE) {
                    String top1Week = edtTop1Week.getText().toString();
                    if (TextUtils.isEmpty(rank)) {
                        edtTop1Week.setError("top 1 week can't be null");
                        return;
                    }
                    rankBean.setTop1Week(Integer.parseInt(top1Week));
                }

                new FileIO().saveRankBean(rankBean);
                actionListener.onSave(rankBean);
            }
        }
    }

    @Override
    public void show() {
        super.show();
    }
}
