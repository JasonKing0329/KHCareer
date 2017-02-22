package com.king.mytennis.score;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.R;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 16:21
 */
public class ScoreActivity extends BaseActivity implements View.OnClickListener, IScoreView {

    private TextView tvYear;
    private TextView tvWeeks;

    private View vDividerYear;
    private View vDividerWeeks;

    private ScorePresenter mPresenter;

    private ScoreFragment ftYear;
    private ScoreFragment ft52Week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        tvYear = (TextView) findViewById(R.id.score_actionbar_year);
        tvWeeks = (TextView) findViewById(R.id.score_actionbar_week);
        vDividerYear = findViewById(R.id.score_actionbar_year_divider);
        vDividerWeeks = findViewById(R.id.score_actionbar_week_divider);

        tvYear.setOnClickListener(this);
        tvWeeks.setOnClickListener(this);

        mPresenter = new ScorePresenter(this);
        show52WeekScores();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.score_actionbar_year:
                showYearScores();
                break;
            case R.id.score_actionbar_week:
                show52WeekScores();
                break;
        }
    }

    private void show52WeekScores() {
        setFocusTab(tvWeeks);
        show52WeekFragment();
    }

    private void showYearScores() {
        setFocusTab(tvYear);
        showYearFragment();
    }

    private void show52WeekFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (ft52Week == null) {
            ft52Week = new ScoreFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ScoreFragment.KEY_MODE, ScoreFragment.FLAG_52WEEK);
            ft52Week.setArguments(bundle);
            ft.add(R.id.score_ft_container, ft52Week, "ScoreFragment_52Week");
        }
        else {
            ft.show(ft52Week).hide(ftYear);
        }
        ft.commit();
    }

    private void showYearFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (ftYear == null) {
            ftYear = new ScoreFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ScoreFragment.KEY_MODE, ScoreFragment.FLAG_YEAR);
            ftYear.setArguments(bundle);
            ft.add(R.id.score_ft_container, ftYear, "ScoreFragment_Year").hide(ft52Week);
        }
        else {
            ft.show(ftYear).hide(ft52Week);
        }
        ft.commit();
    }

    public void setFocusTab(TextView focusTab) {
        if (focusTab == tvYear) {
            tvYear.setSelected(true);
            tvWeeks.setSelected(false);
            vDividerYear.setVisibility(View.VISIBLE);
            vDividerWeeks.setVisibility(View.INVISIBLE);
        }
        else {
            tvYear.setSelected(false);
            tvWeeks.setSelected(true);
            vDividerYear.setVisibility(View.INVISIBLE);
            vDividerWeeks.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ScorePresenter getPresenter() {
        return mPresenter;
    }
}
