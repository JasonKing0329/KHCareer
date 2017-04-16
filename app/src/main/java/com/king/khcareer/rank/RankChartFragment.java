package com.king.khcareer.rank;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.king.khcareer.base.BaseFragment;
import com.king.khcareer.utils.DebugLog;
import com.king.mytennis.view.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/5 10:29
 */
public class RankChartFragment extends BaseFragment {

    private View fakeView;
    private RankPresenter rankPresenter;
    private BarChart barChart;
    private List<RankFinalBean> rankList;
    private List<BarRank> barRankList;

    private int startYear;

    private View.OnClickListener onChartClickListener;

    /**
     * 转换rank在图标中的权值，数越小bar越长
     */
    private Map<Integer, Integer> valueMap = new HashMap<>();

    private int[] colorBars = {
            Color.rgb(0x33, 0x99, 0xff), Color.rgb(0, 0xa5, 0xc4)
    };

    @Override
    protected void onAttachParent(Context context) {

    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_rank_chart;
    }

    @Override
    protected void onCreate(View view) {
        fakeView = view.findViewById(R.id.bar_fake);
        barChart = (BarChart) view.findViewById(R.id.bar_chart_rank);
        rankPresenter = new RankPresenter();
        rankList = rankPresenter.getRankList();
        if (onChartClickListener != null) {
            // 实测当BarChart有数据时，直接设置barChart有效。但是没有数据时，onClick就不管用了
            // 因此，只能设置在fake view上，这样会拦截BarChart的触摸事件
            // 整个group的点击功能只用于主页，因此目前没有别的影响
//            barChart.setOnClickListener(onChartClickListener);
            fakeView.setVisibility(View.VISIBLE);
            fakeView.setOnClickListener(onChartClickListener);
        }

        initChart();
    }

    /**
     * 发生User变化后presenter持有的dao还保留对上个user的数据库访问
     * 需要重新初始化
     */
    public void onUserChanged() {
        rankPresenter = new RankPresenter();
        rankList = rankPresenter.getRankList();
        initChart();
    }

    /**
     * 重新加载图标数据（注：访问的数据库不会改变）
     * @param rankList
     */
    public void refreshRanks(List<RankFinalBean> rankList) {
        if (rankList == null) {
            this.rankList = rankPresenter.getRankList();
        }
        else {
            this.rankList = rankList;
        }
        initChart();
    }

    private void initChart() {
        if (rankList == null || rankList.size() == 0) {
            return;
        }

        startYear = rankList.get(0).getYear();
        BarData barData = formatBarData();

        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                DebugLog.e("xAxis value=" + value);
                return String.valueOf(startYear + (int) value);
            }
        });
        xAxis.setLabelCount(rankList.size());

        YAxis leftAxis = barChart.getAxisLeft();
//        leftAxis.setLabelCount(5, false);
//        leftAxis.setSpaceTop(0);
////        leftAxis.setSpaceTop(15f);
//        leftAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                DebugLog.e("YAxis value=" + value);
//                return String.valueOf((int) value);
//            }
//        });
        leftAxis.setEnabled(false);

        YAxis rightAxis = barChart.getAxisRight();
//        rightAxis.setLabelCount(5, false);
//        rightAxis.setSpaceTop(15f);
        rightAxis.setEnabled(false);

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);

        // set data
        barChart.setData(barData);
        barChart.setFitBars(true);

        // do not forget to refresh the chart
//            barChart.invalidate();
        barChart.animateY(700);
    }

    private BarData formatBarData() {

        createValueMap();
        formatBarRanks();

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < barRankList.size(); i++) {
            entries.add(new BarEntry(i, (float) barRankList.get(i).barValue, barRankList.get(i).bean));
        }

        BarDataSet d = new BarDataSet(entries, "Year-Rank ");
        d.setColors(colorBars);
        d.setValueTextSize(16f);
        d.setHighlightEnabled(false);
//        d.setBarShadowColor(Color.rgb(203, 203, 203));
        d.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                RankFinalBean bean = (RankFinalBean) entry.getData();
                return String.valueOf(bean.getRank());
            }
        });

        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
        sets.add(d);

        BarData cd = new BarData(sets);
        if (barRankList.size() < 3) {
            cd.setBarWidth(0.5f);
        }
        else if (barRankList.size() >2 && barRankList.size() < 4) {
            cd.setBarWidth(0.7f);
        }
        else {
            cd.setBarWidth(0.9f);
        }
        return cd;
    }

    private void formatBarRanks() {
        barRankList = new ArrayList<>();
        if (rankList != null) {
            for (int i = 0; i < rankList.size(); i ++) {
                BarRank rank = new BarRank();
                rank.bean = rankList.get(i);
                rank.barValue = valueMap.get(rankList.get(i).getRank());
                barRankList.add(rank);
            }
        }
    }

    private void createValueMap() {
        if (rankList != null && rankList.size() > 0) {
            List<Integer> values = new ArrayList<>();
            for (RankFinalBean bean:rankList) {
                if (!values.contains(bean.getRank())) {
                    values.add(bean.getRank());
                }
            }
            // 从小到大排列
            Collections.sort(values);
            // rank-value的对应规则
            // Max = values.get(length - 1)
            // Vn = Max - (Rn - R1)  V为barValue，R为实际rank
            int max = values.get(values.size() - 1);
            for (int n = 0; n < values.size(); n ++) {
                valueMap.put(values.get(n), max - (values.get(n) - values.get(0)));
            }
        }
    }

    /**
     * 设置整个group的点击事件
     * @param onChartClickListener
     */
    public void setOnChartGroupClickListener(View.OnClickListener onChartClickListener) {
        this.onChartClickListener = onChartClickListener;
    }

    private class BarRank {
        RankFinalBean bean;
        int barValue;
    }
}
