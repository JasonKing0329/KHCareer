package com.king.mytennis.score;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.king.mytennis.model.Constants;
import com.king.mytennis.multiuser.MultiUser;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.view.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/21 14:14
 */
public class ScoreFragment extends Fragment implements IScorePageView {

    public static final String KEY_MODE = "key_mode";
    public static final int FLAG_52WEEK = 0;
    public static final int FLAG_YEAR = 1;
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

    private PieChart chartCourt;
    private PieChart chartYear;
    private ChartHelper chartHelper;

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
        tvMatchCount = (TextView) view.findViewById(R.id.score_match_number);
        ivCountryFlag = (ImageView) view.findViewById(R.id.score_flag_bg);
        chartCourt = (PieChart) view.findViewById(R.id.score_chart_court);
        chartYear = (PieChart) view.findViewById(R.id.score_chart_year);

        MultiUser user = MultiUserManager.getInstance().getCurrentUser();
        ivCountryFlag.setImageResource(user.getFlagImageResId());
        tvPlayer.setText(user.getFullName());
        tvCountry.setText(user.getCountry());
        tvBirthday.setText(user.getBirthday());
        tvHeight.setText(user.getHeight() + "  " + user.getWeight());

        int mode = getArguments().getInt(KEY_MODE);
        scoreView.getPresenter().setScorePageView(this);
        if (mode == FLAG_YEAR) {
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

        // 统计ATP规则内的实际积分（4大满贯+年终总决赛+8站强制ATP1000+2站最好ATP500+2站最好ATP250+6站最好剩余赛事）

        // gs
        List<ScoreBean> gsList = map.get(arrLevel[0]);
        tvGs.setText(scoreView.getPresenter().getGroupText(gsList, null, null));
        
        // master cup
        List<ScoreBean> mcList = map.get(arrLevel[1]);
        tvMaster.setText(scoreView.getPresenter().getGroupText(mcList, null, null));

        // atp 1000, 蒙特卡洛算作Replace
        List<ScoreBean> list1000 = map.get(arrLevel[2]);
        tv1000.setText(scoreView.getPresenter().getGroupText(list1000, Constants.MATCH_CONST_MONTECARLO, replaceList));

        // atp 500, 只取最好的两站，其他算作replace
        List<ScoreBean> list500 = map.get(arrLevel[3]);
        tv500.setText(scoreView.getPresenter().getGroupText(list500, 2, replaceList));

        // atp 250, 只取最好的两站，其他算作replace
        List<ScoreBean> list250 = map.get(arrLevel[4]);
        tv250.setText(scoreView.getPresenter().getGroupText(list250, 2, replaceList));

        List<ScoreBean> otherList = new ArrayList<>();
        // 所有replace赛事中，取最好的6项赛事进入积分系统, 其他的进入other（不进入积分系统只显示）
        String text = scoreView.getPresenter().getGroupText(replaceList, 6, otherList);
        tvReplace.setText(text);

        // 不进入积分系统的剩余赛事，计算实际积分和各项比例（将该赛事从统计数据中减去）
        if (otherList.size() > 0) {
            for (int i = 0; i < otherList.size(); i ++) {
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
            otherList.addAll(olyList);
        }
        tvOther.setText(scoreView.getPresenter().getGroupText(otherList, null, null));

        tvScoreTotal.setText(String.valueOf(data.getCountScore()));
        tvMatchCount.setText(String.valueOf("Match count " + data.getScoreList().size()));

        showCourtChart(data);
        showYearChart(data);
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

}
