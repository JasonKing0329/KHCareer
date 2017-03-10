package com.king.mytennis.player;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.HorizontalBarChart;
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
import com.king.mytennis.pubdata.bean.PlayerBean;
import com.king.mytennis.utils.ConstellationUtil;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/10 9:30
 */
public class PlayerChartDialog extends CustomDialog {

    private HorizontalBarChart mChart;
    private List<PlayerBean> playerList;
    /**
     * 长度为13,0为白羊座
     */
    private int[] arrConstel;
    protected Typeface mTfLight;

    private static final float BAR_WIDTH = 4f;
    /**
     * 只能是整5或者整10
     */
    private static final float SPACE_FOR_BAR = 5f;

    public PlayerChartDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        mTfLight = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf");
        // 最后一位是unknown
        arrConstel = new int[13];
        HashMap<String, Object> map = new HashMap<>();
        actionListener.onLoadData(map);
        playerList = (List<PlayerBean>) map.get("data");
        showChart();
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_player_chart, null);
        mChart = (HorizontalBarChart) view.findViewById(R.id.chart_constellation);
        return view;
    }

    @Override
    protected View getCustomToolbar() {
        return null;
    }

    /**
     * 对于HorizontalBarChart里面的axis方向，可以看做是BarChart顺时针旋转了90度
     * 因此XAxis即为纵轴，YAxis getAxisLeft即为顶部横轴，Right即为底部的横轴
     */
    private void showChart() {

        mChart.setTouchEnabled(false);

        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
//        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        mChart.setDrawGridBackground(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(mTfLight);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        // 这个指明了纵坐标之间的间隔，由于设置了SPACE_FOR_BAR为5，因此不能再为10
//        xl.setGranularity(10f);
        // 必须指明总共画几个label，否则会自动隔一个隐藏一个(纵坐标的显示内容)
        xl.setLabelCount(arrConstel.length);
        // 默认情况下纵坐标只显示SPACE_FOR_BAR偶数倍的值，必须用setLabelCount指明才会全部显示
        // 用formatter的方式转换纵坐标的标注
        xl.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                // index为以原点开始向顶部伸展的序号
                int index = (int) value / (int) SPACE_FOR_BAR;
                if (index == 0) {
                    return "Unknown";
                }
                return ConstellationUtil.getConstellationChnByIndex(arrConstel.length - 1 - index);
            }
        });

        YAxis yl = mChart.getAxisLeft();
        yl.setTypeface(mTfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = mChart.getAxisRight();
        yr.setTypeface(mTfLight);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        yl.setEnabled(false);
        yr.setEnabled(false);

        setData();
        mChart.setFitBars(true);
        mChart.animateY(2500);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
    }

    private void setData() {

        // 统计星座对应数量
        for (PlayerBean bean : playerList) {
            try {
                int index = ConstellationUtil.getConstellationIndex(bean.getBirthday());
                arrConstel[index]++;
            } catch (ConstellationUtil.ConstellationParseException e) {
                e.printStackTrace();
                arrConstel[12]++;
            }
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        // HorizontalBarChart纵坐标是从底部原点向顶部伸展的，为了适应从上往下的顺序，value要反着添加
        for (int i = 0; i < arrConstel.length; i++) {
            yVals1.add(new BarEntry(i * SPACE_FOR_BAR, arrConstel[arrConstel.length - 1 - i]));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Constellation");
            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    // 默认会显示float浮点数，会带".0"，转换成整数
                    return String.valueOf((int) value);
                }
            });

            set1.setDrawIcons(false);
            set1.setColor(getContext().getResources().getColor(R.color.colorAccent));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(BAR_WIDTH);
            mChart.setData(data);
        }
    }

}
