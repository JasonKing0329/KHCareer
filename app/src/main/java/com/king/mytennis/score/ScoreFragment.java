package com.king.mytennis.score;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.king.mytennis.model.Constants;
import com.king.mytennis.model.FileIO;
import com.king.mytennis.multiuser.MultiUser;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    private TextView tvGs;
    private TextView tvMaster;
    private TextView tv1000;
    private TextView tv500;
    private TextView tv250;
    private TextView tvReplace;
    private TextView tvOther;

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

        tvGs = (TextView) view.findViewById(R.id.score_gs);
        tvMaster = (TextView) view.findViewById(R.id.score_mc);
        tv1000 = (TextView) view.findViewById(R.id.score_1000);
        tv500 = (TextView) view.findViewById(R.id.score_500);
        tv250 = (TextView) view.findViewById(R.id.score_250);
        tvReplace = (TextView) view.findViewById(R.id.score_replace);
        tvOther = (TextView) view.findViewById(R.id.score_other);
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

    @Override
    public void onPageDataLoaded(ScorePageData data) {
        Map<String, List<ScoreBean>> map = data.getLevelMap();
        String[] arrLevel = getResources().getStringArray(R.array.spinner_level);
        String[] arrCourt = getResources().getStringArray(R.array.spinner_court);

        List<ScoreBean> replaceList = new ArrayList<>();

        // @Deprecated
        // 统计ATP规则内的实际积分（4大满贯+年终总决赛+8站强制ATP1000+2站最好ATP500+2站最好ATP250+6站最好剩余赛事）

        // TODO 统计ATP规则内的实际积分
        // 4大满贯+年终总决赛+8站强制ATP1000+6站最好
        // 上一年年终top30的有500赛强制罚分，必须参加4项500赛，且有一项是美网后（蒙卡算500赛），只要参加即刻，可以不计入6站最好（比如6个250夺冠，就不计算4个500赛首轮游）
        // 非top30 按照取18站最好成绩的做法，若参加了大满贯和1000赛需要强制计入
        // 目前的数据库系统待完善，对于top30仅按照废弃规则将6站最好剩余改为 6-已计入的500-已计入的250 站最好

        // gs
        List<ScoreBean> gsList = map.get(arrLevel[0]);
        tvGs.setText(scoreView.getPresenter().getGroupText(gsList, null, null));
        
        // master cup
        List<ScoreBean> mcList = map.get(arrLevel[1]);
        tvMaster.setText(scoreView.getPresenter().getGroupText(mcList, null, null));

        // atp 1000, 蒙特卡洛算作Replace
        List<ScoreBean> list1000 = map.get(arrLevel[2]);
        String text1000 = scoreView.getPresenter().getGroupText(list1000, Constants.MATCH_CONST_MONTECARLO, replaceList);
        tv1000.setText(text1000);

        // 进入积分系统的剩余赛事数量
        int leftbest;
        List<ScoreBean> list500Final = new ArrayList<>();
        // 上一年非top30的情况
        // 除了参加的gs,1000强计，剩下的均按积分高低取(18 - gsCount - 1000Count)
        if (scoreView.getPresenter().getCurrentYear() <= MultiUserManager.getInstance().getFirstTop30Year()) {
            int matchCount = 0;
            if (gsList != null) {
                matchCount += gsList.size();
            }
            if (list1000 != null) {
                matchCount += list1000.size();
                if (replaceList.size() == 1) {// 先剔除蒙卡，作为剩余的赛事统一计算
                    matchCount --;
                }
            }
            leftbest = 18 - matchCount;
        }
        // top30的情况，6站最好（罚分要强计）
        // 计算500赛的罚分情况，需够4站，且一站是美网后的
        else {
            int force500 = 0;
            if (replaceList.size() > 0) {// 参加了蒙卡
                force500 ++;
            }
            List<ScoreBean> list500 = map.get(arrLevel[3]);
            if (list500 != null) {
                boolean hasAfterUsOpen = false;
                for (ScoreBean bean:list500) {
                    if (bean.getWeek() > 35) {// 35是美网的周数
                        hasAfterUsOpen = true;
                    }
                    if (force500 == 4) {
                        // 已够4站，不再累计
                        continue;
                    }
                    force500 ++;
                }
                // 如果未参加美网后的一站500，要强制罚分一站
                if (!hasAfterUsOpen) {
                    force500 --;
                }
            }
            int punish = 4 - force500;//
            for (int i = 0; i < punish; i ++) {
                ScoreBean scoreBean = new ScoreBean();
                scoreBean.setName("500赛罚分");
                scoreBean.setScore(0);
                list500Final.add(scoreBean);
            }
            leftbest = 6 - punish;
        }

        // 先将剩余的赛事按积分从高到低排序
        List<ScoreBean> leftMatches = new ArrayList<>();
        leftMatches.addAll(replaceList);// 可能包含的蒙卡
        if (map.get(arrLevel[3]) != null) {// 500
            leftMatches.addAll(map.get(arrLevel[3]));
        }
        if (map.get(arrLevel[4]) != null) {
            leftMatches.addAll(map.get(arrLevel[4]));// 250
        }
        Collections.sort(leftMatches, scoreView.getPresenter().getScoreComparator());

        List<ScoreBean> list250 = new ArrayList<>();
        replaceList.clear();
        // 将这几站重新分配到对应的level下，其他的进入replace list
        for (int i = 0; i < leftMatches.size(); i ++) {
            ScoreBean bean = leftMatches.get(i);
            if (i < leftbest) {// 进入积分系统的赛事
                if (bean.getLevel() == null) {// 罚分
                    list500Final.add(bean);
                }
                else if (bean.getLevel().equals(arrLevel[2])) {// 蒙特卡洛
                    if (!TextUtils.isEmpty(text1000)) {
                        text1000 = text1000 + "\n";
                    }
                    text1000 = text1000 + bean.getName() + "  " + bean.getScore();
                    tv1000.setText(text1000);
                }
                else if (bean.getLevel().equals(arrLevel[3])) {
                    list500Final.add(bean);
                }
                else if (bean.getLevel().equals(arrLevel[4])) {
                    list250.add(bean);
                }
            }
            else {// 作为替补的赛事
                replaceList.add(bean);
            }
        }

        tv500.setText(scoreView.getPresenter().getGroupText(list500Final, 10, replaceList, true));

        tv250.setText(scoreView.getPresenter().getGroupText(list250, 10, replaceList, true));

        // 替补赛事按照积分进行降序排序
        Collections.sort(replaceList, scoreView.getPresenter().getScoreComparator());
        List<ScoreBean> otherList = new ArrayList<>();
        String text = scoreView.getPresenter().getGroupText(replaceList, 10, otherList, false);
        tvReplace.setText(text);

        // 不进入积分系统的替补赛事，计算实际积分和各项比例（将该赛事从统计数据中减去）
        if (replaceList.size() > 0) {
            for (int i = 0; i < replaceList.size(); i ++) {
                // 减去积分
                data.setCountScore(data.getCountScore() - replaceList.get(i).getScore());

                // 减去this year, last year数量统计
                if (replaceList.get(i).getYear() == scoreView.getPresenter().getThisYear()) {
                    data.setCountScoreYear(data.getCountScoreYear() - 1);
                }
                else {
                    data.setCountScoreLastYear(data.getCountScoreLastYear() - 1);
                }

                // 减去场地类型的数量统计
                if (replaceList.get(i).getCourt().equals(arrCourt[1])) {
                    data.setCountScoreClay(data.getCountScoreClay() - 1);
                }
                else if (replaceList.get(i).getCourt().equals(arrCourt[2])) {
                    data.setCountScoreGrass(data.getCountScoreGrass() - 1);
                }
                else if (replaceList.get(i).getCourt().equals(arrCourt[3])) {
                    data.setCountScoreInHard(data.getCountScoreInHard() - 1);
                }
                else {
                    data.setCountScoreHard(data.getCountScoreHard() - 1);
                }
            }
        }

        // 2016奥运会无积分，进入其他项显示
        List<ScoreBean> olyList = map.get(arrLevel[6]);
        if (olyList != null && olyList.size() > 0) {
            // 如果即打了半决赛又打了铜牌赛，会出现两条记录，只取第一条
            otherList.add(olyList.get(0));
        }
        tvOther.setText(scoreView.getPresenter().getGroupText(otherList, null, null));

        tvScoreTotal.setText(String.valueOf(data.getCountScore()));
        tvMatchCount.setText(String.valueOf("Match count " + data.getScoreList().size()));
        tvRank.setText(loadPlayerRank());

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

    private String loadPlayerRank() {
        RankBean bean = new FileIO().readRankBean();
        if (bean == null) {
            return "0";
        }
        else {
            return String.valueOf(bean.getRank());
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
