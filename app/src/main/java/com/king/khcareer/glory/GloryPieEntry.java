package com.king.khcareer.glory;

import com.github.mikephil.charting.data.PieEntry;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/22 13:57
 */
public class GloryPieEntry extends PieEntry {

    private String myValue;

    public GloryPieEntry(float value, String label) {
        super(value, label);
    }
    public GloryPieEntry(float value, String label, String myValue) {
        this(value, label);
        this.myValue = myValue;
    }

    public String getMyValue() {
        return myValue;
    }

    public void setMyValue(String myValue) {
        this.myValue = myValue;
    }
}
