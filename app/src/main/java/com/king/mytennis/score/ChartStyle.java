package com.king.mytennis.score;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/22 15:02
 */
public class ChartStyle {
    private boolean showCenterHole;
    /**
     * 饼图周围横排或者纵向说明色块内容的区域
     */
    private boolean showLegend;
    private String centerText;
    /**
     * 隐藏饼图色块上的文字
     */
    private boolean hideEntries;

    public String getCenterText() {
        return centerText;
    }

    public void setCenterText(String centerText) {
        this.centerText = centerText;
    }

    public boolean isShowCenterHole() {
        return showCenterHole;
    }

    public void setShowCenterHole(boolean showCenterHole) {
        this.showCenterHole = showCenterHole;
    }

    public boolean isShowLegend() {
        return showLegend;
    }

    public void setShowLegend(boolean showLegend) {
        this.showLegend = showLegend;
    }

    public boolean isHideEntries() {
        return hideEntries;
    }

    public void setHideEntries(boolean hideEntries) {
        this.hideEntries = hideEntries;
    }
}
