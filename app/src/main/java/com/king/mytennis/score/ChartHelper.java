package com.king.mytennis.score;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 饼图周围的 color-value 列表说明 称之为 legend
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/22 13:21
 */
public class ChartHelper {

    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    public ChartHelper(Context context) {
        mTfRegular = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");

    }

    /**
     *
     * @param chart
     * @param contents 饼图中每块代表的内容
     * @param percents 饼图中每块的占比
     * @param colors 饼图中每块对应的颜色
     * @param style 饼图的设置项
     */
    public void showPieChart(PieChart chart, String[] contents, float[] percents
        , List<Integer> colors, ChartStyle style) {

        chart.setUsePercentValues(true);
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
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setDrawInside(true);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            chart.setDrawLegendEnabled(false);
        }
        // 饼图和legend之间的距离，相当于饼图的margin，不过比较奇怪的是，只设置了top，但是4个方向都有offset
        chart.setExtraTopOffset(10);
//        chart.setExtraOffsets(5, 10, 5, 5);

        // 饼图内色块content以及其percent value
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < contents.length; i ++) {
            PieEntry entry = new PieEntry(percents[i], contents[i]);// 还可以加第三个参数，对应的图标
            entries.add(entry);
        }

        setChartData(chart, entries, colors);

    }
    
    private void setChartData(PieChart chart, ArrayList<PieEntry> entries, List<Integer> colors) {
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
        
        // 色块颜色
        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
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
