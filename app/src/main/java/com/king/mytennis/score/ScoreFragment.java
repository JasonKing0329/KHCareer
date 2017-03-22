package com.king.mytennis.score;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.king.mytennis.glory.GloryController;
import com.king.mytennis.glory.GloryMatchDialog;
import com.king.mytennis.model.Constants;
import com.king.mytennis.model.FileIO;
import com.king.mytennis.model.Record;
import com.king.mytennis.multiuser.MultiUser;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.pubdata.bean.MatchNameBean;
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.endYear;
import static android.R.attr.startYear;
import static android.R.id.list;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/21 14:14
 */
public class ScoreFragment extends Fragment implements IScorePageView, View.OnClickListener {

    public static final String KEY_MODE = "key_mode";
    public static final int FLAG_52WEEK = 0;
    public static final int FLAG_YEAR = 1;

    private int pageMode;

    private IScoreView scoreView;

    private RecyclerView rvGs;
    private RecyclerView rvMaster;
    private RecyclerView rv1000;
    private RecyclerView rv500;
    private RecyclerView rv250;
    private RecyclerView rvReplace;
    private RecyclerView rvOther;

    private ScoreItemAdapter adapterGs;
    private ScoreItemAdapter adapterMaster;
    private ScoreItemAdapter adapter1000;
    private ScoreItemAdapter adapter500;
    private ScoreItemAdapter adapter250;
    private ScoreItemAdapter adapterReplace;
    private ScoreItemAdapter adapterOther;

    private TextView tvPlayer;
    private TextView tvCountry;
    private TextView tvBirthday;
    private TextView tvScoreTotal;
    private TextView tvMatchCount;
    private TextView tvHeight;
    private ImageView ivCountryFlag;
    private TextView tvRank;

    private PieChart chartCourt;
    private PieChart chartYear;
    private ChartHelper chartHelper;

    private ImageView ivDateLast;
    private ImageView ivDateNext;
    private TextView tvYearSelect;
    private ViewGroup groupDate;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        scoreView = (IScoreView) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        chartHelper = new ChartHelper(getActivity());

        View view = inflater.inflate(R.layout.fragment_score, container, false);

        tvPlayer = (TextView) view.findViewById(R.id.score_player);
        tvScoreTotal = (TextView) view.findViewById(R.id.score_total);
        tvBirthday = (TextView) view.findViewById(R.id.score_birthday);
        tvCountry = (TextView) view.findViewById(R.id.score_country);
        tvHeight = (TextView) view.findViewById(R.id.score_height);
        tvRank = (TextView) view.findViewById(R.id.score_rank);
        tvMatchCount = (TextView) view.findViewById(R.id.score_match_number);
        ivCountryFlag = (ImageView) view.findViewById(R.id.score_flag_bg);
        chartCourt = (PieChart) view.findViewById(R.id.score_chart_court);
        chartYear = (PieChart) view.findViewById(R.id.score_chart_year);

        tvYearSelect = (TextView) view.findViewById(R.id.score_datebar_year);
        ivDateLast = (ImageView) view.findViewById(R.id.score_datebar_last);
        ivDateNext = (ImageView) view.findViewById(R.id.score_datebar_next);
        groupDate = (ViewGroup) view.findViewById(R.id.score_datebar);
        ivDateLast.setOnClickListener(this);
        ivDateNext.setOnClickListener(this);

        rvGs = (RecyclerView) view.findViewById(R.id.score_gs);
        initRecyclerView(rvGs);
        rvMaster = (RecyclerView) view.findViewById(R.id.score_mc);
        initRecyclerView(rvMaster);
        rv1000 = (RecyclerView) view.findViewById(R.id.score_1000);
        initRecyclerView(rv1000);
        rv500 = (RecyclerView) view.findViewById(R.id.score_500);
        initRecyclerView(rv500);
        rv250 = (RecyclerView) view.findViewById(R.id.score_250);
        initRecyclerView(rv250);
        rvReplace = (RecyclerView) view.findViewById(R.id.score_replace);
        initRecyclerView(rvReplace);
        rvOther = (RecyclerView) view.findViewById(R.id.score_other);
        initRecyclerView(rvOther);
        
        MultiUser user = MultiUserManager.getInstance().getCurrentUser();
        ivCountryFlag.setImageResource(user.getFlagImageResId());
        tvPlayer.setText(user.getFullName());
        tvCountry.setText(user.getCountry());
        tvBirthday.setText(user.getBirthday());
        tvHeight.setText(user.getHeight() + "  " + user.getWeight());

        pageMode = getArguments().getInt(KEY_MODE);
        scoreView.getPresenter().setScorePageView(this);
        if (pageMode == FLAG_YEAR) {
            scoreView.getPresenter().queryYearRecords();
        }
        else {
            scoreView.getPresenter().query52WeekRecords();
        }
        return view;
    }

    private void initRecyclerView(RecyclerView rv) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    private void onMatchClicked(ScoreBean bean) {
        if (bean != null && bean.getMatchBean() != null) {
            String date = bean.getYear() + "-";
            int month = bean.getMatchBean().getMatchBean().getMonth();
            if (month < 10) {
                date = date + "0" + month;
            }
            else {
                date = date + month;
            }
            final List<Record> list = new GloryController().loadMatchRecord(getActivity(), bean.getMatchBean().getName(), date);
            if (list == null) {
                return;
            }
            GloryMatchDialog dialog = new GloryMatchDialog(getActivity(), new CustomDialog.OnCustomDialogActionListener() {
                @Override
                public boolean onSave(Object object) {
                    return false;
                }

                @Override
                public boolean onCancel() {
                    return false;
                }

                @Override
                public void onLoadData(HashMap<String, Object> data) {
                    data.put(CustomDialog.OnCustomDialogActionListener.DATA_TYPE, list);
                }
            });
            dialog.enableItemLongClick();
            dialog.show();
        }
    }

    @Override
    public void onPageDataLoaded(ScorePageData data) {

        // 获取排名
        RankBean bean = scoreView.getPresenter().loadRank();
        if (bean == null) {
            tvRank.setText("--");
        }
        else {
            tvRank.setText(String.valueOf(bean.getRank()));
        }

        // 积分、图表
        // gs
        if (adapterGs == null) {
            adapterGs = new ScoreItemAdapter(data.getGsList());
            adapterGs.setOnScoreItemClickListener(new ScoreItemAdapter.OnScoreItemClickListener() {
                @Override
                public void onScoreItemClick(ScoreBean bean) {
                    onMatchClicked(bean);
                }
            });
            rvGs.setAdapter(adapterGs);
        }
        else {
            adapterGs.setList(data.getGsList());
            adapterGs.notifyDataSetChanged();
        }

        // master cup
        if (adapterMaster == null) {
            adapterMaster = new ScoreItemAdapter(data.getMasterCupList());
            adapterMaster.setOnScoreItemClickListener(new ScoreItemAdapter.OnScoreItemClickListener() {
                @Override
                public void onScoreItemClick(ScoreBean bean) {
                    onMatchClicked(bean);
                }
            });
            rvMaster.setAdapter(adapterMaster);
        }
        else {
            adapterMaster.setList(data.getMasterCupList());
            adapterMaster.notifyDataSetChanged();
        }

        // 1000
        if (adapter1000 == null) {
            adapter1000 = new ScoreItemAdapter(data.getAtp1000List());
            adapter1000.setOnScoreItemClickListener(new ScoreItemAdapter.OnScoreItemClickListener() {
                @Override
                public void onScoreItemClick(ScoreBean bean) {
                    onMatchClicked(bean);
                }
            });
            rv1000.setAdapter(adapter1000);
        }
        else {
            adapter1000.setList(data.getAtp1000List());
            adapter1000.notifyDataSetChanged();
        }

        // 500
        if (adapter500 == null) {
            adapter500 = new ScoreItemAdapter(data.getAtp500List());
            adapter500.setOnScoreItemClickListener(new ScoreItemAdapter.OnScoreItemClickListener() {
                @Override
                public void onScoreItemClick(ScoreBean bean) {
                    onMatchClicked(bean);
                }
            });
            rv500.setAdapter(adapter500);
        }
        else {
            adapter500.setList(data.getAtp500List());
            adapter500.notifyDataSetChanged();
        }

        // 250
        if (adapter250 == null) {
            adapter250 = new ScoreItemAdapter(data.getAtp250List());
            adapter250.setOnScoreItemClickListener(new ScoreItemAdapter.OnScoreItemClickListener() {
                @Override
                public void onScoreItemClick(ScoreBean bean) {
                    onMatchClicked(bean);
                }
            });
            rv250.setAdapter(adapter250);
        }
        else {
            adapter250.setList(data.getAtp250List());
            adapter250.notifyDataSetChanged();
        }

        // replace
        if (adapterReplace == null) {
            adapterReplace = new ScoreItemAdapter(data.getReplaceList());
            adapterReplace.setOnScoreItemClickListener(new ScoreItemAdapter.OnScoreItemClickListener() {
                @Override
                public void onScoreItemClick(ScoreBean bean) {
                    onMatchClicked(bean);
                }
            });
            rvReplace.setAdapter(adapterReplace);
        }
        else {
            adapterReplace.setList(data.getReplaceList());
            adapterReplace.notifyDataSetChanged();
        }

        // replace
        if (adapterOther == null) {
            adapterOther = new ScoreItemAdapter(data.getOtherList());
            adapterOther.setOnScoreItemClickListener(new ScoreItemAdapter.OnScoreItemClickListener() {
                @Override
                public void onScoreItemClick(ScoreBean bean) {
                    onMatchClicked(bean);
                }
            });
            rvOther.setAdapter(adapterOther);
        }
        else {
            adapterOther.setList(data.getOtherList());
            adapterOther.notifyDataSetChanged();
        }

        tvScoreTotal.setText(String.valueOf(data.getCountScore()));
        tvMatchCount.setText("Match count " + String.valueOf(data.getScoreList().size()));
        
        // 显示场地胜率统计
        showCourtChart(data);
        // 52 week记录才显示去年占比和今年占比
        if (pageMode == FLAG_52WEEK) {
            showYearChart(data);
        }
        else {
            chartYear.setVisibility(View.GONE);
        }

        // 有match没有在public数据库中，提示
        if (data.getNonExistMatchList().size() > 0) {
            StringBuffer buffer = new StringBuffer("Match not found: ");
            for (int i = 0; i < data.getNonExistMatchList().size(); i ++) {
                if (i == 0) {
                    buffer.append(data.getNonExistMatchList().get(i));
                }
                else {
                    buffer.append(",").append(data.getNonExistMatchList().get(i));
                }
            }
            ((BaseActivity) getActivity()).showConfirmMessage(buffer.toString(), null);
        }
    }

    private void showYearChart(ScorePageData data) {
        // 块对应的颜色
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorAccent));
        colors.add(getResources().getColor(R.color.grey));
        int year = scoreView.getPresenter().getThisYear();
        String[] contents = new String[] {
                String.valueOf(year), String.valueOf(year - 1)
        };
        float[] percents = new float[] {
                (float) data.getCountScoreYear()/ (float) data.getCountScore() * 100,
                (float) data.getCountScoreLastYear()/ (float) data.getCountScore() * 100,
        };

        ChartStyle style = new ChartStyle();
        chartHelper.showPieChart(chartYear, contents, percents, colors, style);
    }

    private void showCourtChart(ScorePageData data) {
        // 块对应的颜色
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.actionbar_bk_blue));
        colors.add(getResources().getColor(R.color.actionbar_bk_orange));
        colors.add(getResources().getColor(R.color.actionbar_bk_green));
        colors.add(getResources().getColor(R.color.actionbar_bk_deepblue));

        String[] contents = new String[] {
                "硬地", "红土", "草地", "室内硬地"
        };
        float[] percents = new float[] {
                (float) data.getCountScoreHard()/ (float) data.getCountScore() * 100,
                (float) data.getCountScoreClay()/ (float) data.getCountScore() * 100,
                (float) data.getCountScoreGrass()/ (float) data.getCountScore() * 100,
                (float) data.getCountScoreInHard()/ (float) data.getCountScore() * 100,
        };

        ChartStyle style = new ChartStyle();
        style.setShowCenterHole(true);
        style.setCenterText("Court");
        style.setShowLegend(true);
        style.setHideEntries(true);
        chartHelper.showPieChart(chartCourt, contents, percents, colors, style);
    }

    public void onRankChanged(int rank) {
        tvRank.setText(String.valueOf(rank));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.score_datebar_last:
                showLastYear();
                break;
            case R.id.score_datebar_next:
                showNextYear();
                break;
        }
    }

    public void showDateGroup() {
        if (groupDate.getVisibility() == View.VISIBLE) {
            groupDate.startAnimation(getDisappearAnim());
            groupDate.setVisibility(View.GONE);
        }
        else {
            groupDate.setVisibility(View.VISIBLE);
            groupDate.startAnimation(getAppearAnim());
        }
    }

    private void showLastYear() {
        int year = scoreView.getPresenter().getCurrentYear() - 1;
        scoreView.getPresenter().setCurrentYear(year);
        tvYearSelect.setText(String.valueOf(year));
        scoreView.getPresenter().queryYearRecords();
    }

    private void showNextYear() {
        int year = scoreView.getPresenter().getCurrentYear() + 1;
        scoreView.getPresenter().setCurrentYear(year);
        tvYearSelect.setText(String.valueOf(year));
        scoreView.getPresenter().queryYearRecords();
    }

    public Animation getDisappearAnim() {
        Animation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, -1);
        anim.setDuration(500);
        return anim;
    }
    public Animation getAppearAnim() {
        Animation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, -1
                , Animation.RELATIVE_TO_SELF, 0);
        anim.setDuration(500);
        return anim;
    }
}
