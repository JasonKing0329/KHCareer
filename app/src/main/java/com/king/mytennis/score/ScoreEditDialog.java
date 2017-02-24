package com.king.mytennis.score;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.king.mytennis.model.FileIO;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 13:58
 */
public class ScoreEditDialog extends CustomDialog {

    private EditText edtRank;
    private EditText edtRankHighest;
    private EditText edtTop1Week;
    private ViewGroup groupTop1Week;

    private RankBean rankBean;

    public ScoreEditDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
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

    public RankBean getRankData() {
        RankBean bean = new FileIO().readRankBean();
        return bean;
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_score_manage, null);
        edtRank = (EditText) view.findViewById(R.id.score_manage_rank);
        edtRankHighest = (EditText) view.findViewById(R.id.score_manage_rank_highest);
        edtTop1Week = (EditText) view.findViewById(R.id.score_manage_top1_week);
        groupTop1Week = (ViewGroup) view.findViewById(R.id.score_manage_group_top1_week);
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

    @Override
    public void show() {
        super.show();
    }
}
