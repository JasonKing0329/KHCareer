package com.king.khcareer.glory;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.king.khcareer.common.config.Constants;
import com.king.khcareer.glory.bean.GloryTitle;
import com.king.khcareer.score.ChartStyle;
import com.king.mytennis.view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/22 10:21
 */
public class ChartManager {
    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    public ChartManager(Context context) {
        mTfRegular = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
    }

    public void showLevelChart(PieChart chart, GloryTitle gloryTitle, boolean isWinner, boolean isCurrentYear) {
        
        float[] values = new float[6];
        if (isWinner) {
            if (isCurrentYear) {
                values[0] = gloryTitle.getChampionTitle().getYearGs();
                values[1] = gloryTitle.getChampionTitle().getYearMasterCup();
                values[2] = gloryTitle.getChampionTitle().getYearAtp1000();
                values[3] = gloryTitle.getChampionTitle().getYearAtp500();
                values[4] = gloryTitle.getChampionTitle().getYearAtp250();
                values[5] = gloryTitle.getChampionTitle().getYearOlympics();
            }
            else {
                values[0] = gloryTitle.getChampionTitle().getCareerGs();
                values[1] = gloryTitle.getChampionTitle().getCareerMasterCup();
                values[2] = gloryTitle.getChampionTitle().getCareerAtp1000();
                values[3] = gloryTitle.getChampionTitle().getCareerAtp500();
                values[4] = gloryTitle.getChampionTitle().getCareerAtp250();
                values[5] = gloryTitle.getChampionTitle().getCareerOlympics();
            }
        }
        else {
            if (isCurrentYear) {
                values[0] = gloryTitle.getRunnerupTitle().getYearGs();
                values[1] = gloryTitle.getRunnerupTitle().getYearMasterCup();
                values[2] = gloryTitle.getRunnerupTitle().getYearAtp1000();
                values[3] = gloryTitle.getRunnerupTitle().getYearAtp500();
                values[4] = gloryTitle.getRunnerupTitle().getYearAtp250();
                values[5] = gloryTitle.getRunnerupTitle().getYearOlympics();
            }
            else {
                values[0] = gloryTitle.getRunnerupTitle().getCareerGs();
                values[1] = gloryTitle.getRunnerupTitle().getCareerMasterCup();
                values[2] = gloryTitle.getRunnerupTitle().getCareerAtp1000();
                values[3] = gloryTitle.getRunnerupTitle().getCareerAtp500();
                values[4] = gloryTitle.getRunnerupTitle().getCareerAtp250();
                values[5] = gloryTitle.getRunnerupTitle().getCareerOlympics();
            }
        }
        
        String[] contents = new String[6];
        contents[0] = Constants.RECORD_MATCH_LEVELS[0];
        contents[1] = Constants.RECORD_MATCH_LEVELS[1];
        contents[2] = Constants.RECORD_MATCH_LEVELS[2];
        contents[3] = Constants.RECORD_MATCH_LEVELS[3];
        contents[4] = Constants.RECORD_MATCH_LEVELS[4];
        contents[5] = Constants.RECORD_MATCH_LEVELS[6];

        ChartStyle style = new ChartStyle();
        setPieChartParams(chart, style);

        // 饼图内色块content以及其percent value
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < contents.length; i ++) {
            // 第一个参数对应getFormattedValue里的value值，直接定义一个myvalue直接拼成最后显示的内容
            PieEntry entry = new GloryPieEntry(values[i], contents[i], contents[i] + "(" + (int) values[i] + ")");
            entries.add(entry);
        }

        int[] colors = new int[6];
        colors[0] = chart.getContext().getResources().getColor(R.color.pie_level_gs);
        colors[1] = chart.getContext().getResources().getColor(R.color.pie_level_mc);
        colors[2] = chart.getContext().getResources().getColor(R.color.pie_level_1000);
        colors[3] = chart.getContext().getResources().getColor(R.color.pie_level_500);
        colors[4] = chart.getContext().getResources().getColor(R.color.pie_level_250);
        colors[5] = chart.getContext().getResources().getColor(R.color.pie_level_oly);
        setChartData(chart, entries, colors, new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (value == 0) {
                    return "";
                }
                GloryPieEntry gpe = (GloryPieEntry) entry;
                return gpe.getMyValue();
            }
        });
    }

    public void showCourtChart(PieChart chart, GloryTitle gloryTitle, boolean isWinner, boolean isCurrentYear) {
        
        float[] values = new float[4];
        if (isWinner) {
            if (isCurrentYear) {
                values[0] = gloryTitle.getChampionTitle().getYearHard();
                values[1] = gloryTitle.getChampionTitle().getYearClay();
                values[2] = gloryTitle.getChampionTitle().getYearGrass();
                values[3] = gloryTitle.getChampionTitle().getYearInhard();
            } else {
                values[0] = gloryTitle.getChampionTitle().getCareerHard();
                values[1] = gloryTitle.getChampionTitle().getCareerClay();
                values[2] = gloryTitle.getChampionTitle().getCareerGrass();
                values[3] = gloryTitle.getChampionTitle().getCareerInhard();
            }
        }
        else {
            if (isCurrentYear) {
                values[0] = gloryTitle.getRunnerupTitle().getYearHard();
                values[1] = gloryTitle.getRunnerupTitle().getYearClay();
                values[2] = gloryTitle.getRunnerupTitle().getYearGrass();
                values[3] = gloryTitle.getRunnerupTitle().getYearInhard();
            } else {
                values[0] = gloryTitle.getRunnerupTitle().getCareerHard();
                values[1] = gloryTitle.getRunnerupTitle().getCareerClay();
                values[2] = gloryTitle.getRunnerupTitle().getCareerGrass();
                values[3] = gloryTitle.getRunnerupTitle().getCareerInhard();
            }
        }
        
        String[] contents = new String[4];
        contents[0] = Constants.RECORD_MATCH_COURTS[0];
        contents[1] = Constants.RECORD_MATCH_COURTS[1];
        contents[2] = Constants.RECORD_MATCH_COURTS[2];
        contents[3] = Constants.RECORD_MATCH_COURTS[3];

        ChartStyle style = new ChartStyle();
        setPieChartParams(chart, style);

        // 饼图内色块content
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < contents.length; i ++) {
            // 第一个参数对应getFormattedValue里的value值，直接定义一个myvalue直接拼成最后显示的内容
            PieEntry entry = new GloryPieEntry(values[i], contents[i], contents[i] + "(" + (int) values[i] + ")");
            entries.add(entry);
        }

        int[] colors = new int[4];
        colors[0] = chart.getContext().getResources().getColor(R.color.pie_court_hard);
        colors[1] = chart.getContext().getResources().getColor(R.color.pie_court_clay);
        colors[2] = chart.getContext().getResources().getColor(R.color.pie_court_grass);
        colors[3] = chart.getContext().getResources().getColor(R.color.pie_court_inhard);
        setChartData(chart, entries, colors, new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (value == 0) {
                    return "";
                }
                GloryPieEntry gpe = (GloryPieEntry) entry;
                return gpe.getMyValue();
            }
        });
    }

    public void showH2hChart(PieChart chart, String[] contents, float[] values) {

        ChartStyle style = new ChartStyle();
        setPieChartParams(chart, style);

        // 饼图内色块content
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < contents.length; i ++) {
            // 第一个参数对应getFormattedValue里的value值，直接定义一个myvalue直接拼成最后显示的内容
            PieEntry entry = new GloryPieEntry(values[i], contents[i], contents[i] + "(" + (int) values[i] + ")");
            entries.add(entry);
        }

        int[] colors = new int[5];
        colors[0] = chart.getContext().getResources().getColor(R.color.pie_court_hard);
        colors[1] = chart.getContext().getResources().getColor(R.color.pie_court_clay);
        colors[2] = chart.getContext().getResources().getColor(R.color.pie_court_grass);
        colors[3] = chart.getContext().getResources().getColor(R.color.pie_court_inhard);
        colors[4] = chart.getContext().getResources().getColor(R.color.pie_level_1000);
        setChartData(chart, entries, colors, new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (value == 0) {
                    return "";
                }
                GloryPieEntry gpe = (GloryPieEntry) entry;
                return gpe.getMyValue();
            }
        });
    }

    private void setPieChartParams(PieChart chart, ChartStyle style) {
        // 自定义了GloryEntry，用于显示一行内容，用value(即entry中的y值来绘制就可以了)，label是指x值
        chart.setDrawEntryLabels(false);

        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setDragDecelerationFrictionCoef(0.95f);

        // 显示内环
        if (style.isShowCenterHole()) {
            chart.setDrawHoleEnabled(true);
            chart.setHoleColor(Color.WHITE);
            // 内环的占比
            chart.setHoleRadius(30f);

            // 整个饼图分为内环、外环，还有一环是在另一个上层图层，带有透明属性
            // 下面的设置代表从中心向外占外环的31%（超过内环1%，于是有了叠加效果）
            chart.setTransparentCircleRadius(31f);
            chart.setTransparentCircleColor(Color.WHITE);
            chart.setTransparentCircleAlpha(110);
        }
        else {
            chart.setDrawHoleEnabled(false);
        }

        // 内环中间的文字
        if (style.getCenterText() != null) {
            chart.setDrawCenterText(true);
            chart.setCenterTextTypeface(mTfLight);
            chart.setCenterText(style.getCenterText());
        }

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
//        chart.setOnChartValueSelectedListener(this);

        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        // 隐藏色块上的内容文字
        if (style.isHideEntries()) {
            chart.setDrawEntryLabels(false);// 只显示percent
        }
        else {
            chart.setEntryLabelColor(Color.WHITE);
            chart.setEntryLabelTypeface(mTfRegular);
            chart.setEntryLabelTextSize(10f);
        }

        // 显示饼图周围的 color-value 列表说明
        if (style.isShowLegend()) {
            Legend l = chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(true);
            l.setXEntrySpace(2f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

        }
        else {
            // 这里只是我做了不绘制的控制，但是legend还是会占空间，造成圆饼显示不了layout设置的那么大
            // 所以还是要设置orientation等
            Legend l = chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setDrawInside(true);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            chart.setDrawLegendEnabled(false);
        }
        // 饼图和legend之间的距离，相当于饼图的margin，不过比较奇怪的是，只设置了top，但是4个方向都有offset
        chart.setExtraTopOffset(10);
//        chart.setExtraOffsets(5, 10, 5, 5);
    }

    private void setChartData(PieChart chart, final ArrayList<PieEntry> entries, Object colors, IValueFormatter valueFormatter) {
        // legend 显示的说明文字（说明这张图标是干什么的）
        PieDataSet dataSet = new PieDataSet(entries, "");
        // 色块显示图标
        dataSet.setDrawIcons(false);
        // 色块之间的间距
        dataSet.setSliceSpace(1f);
        // 图标偏移位置
        dataSet.setIconsOffset(new MPPointF(0, 40));
        // 选择后色块向外扩大的距离
        dataSet.setSelectionShift(5f);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        // 色块颜色
        try {
            dataSet.setColors((int[]) colors);
        } catch (Exception e) {
            try {
                dataSet.setColors((List<Integer>) colors);
            } catch (Exception e1) {

            }
        }
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        // 默认的百分比格式转换
//        data.setValueFormatter(new PercentFormatter());
        // 这里使用自定义的格式
        data.setValueFormatter(valueFormatter);
        // percent的文字
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }
}
