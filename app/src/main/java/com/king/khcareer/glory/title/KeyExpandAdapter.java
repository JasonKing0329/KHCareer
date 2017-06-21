package com.king.khcareer.glory.title;

import android.support.annotation.NonNull;

import com.zaihuishou.expandablerecycleradapter.adapter.BaseExpandableAdapter;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/21 16:07
 */
public class KeyExpandAdapter extends BaseExpandableAdapter {

    private final int ITEM_TYPE_HEAD = 1;
    private final int ITEM_TYPE_ITEM = 2;

    private OnRecordItemListener onRecordItemListener;
    private boolean showCompetitor;
    private boolean hideSequence;
    private boolean showLose;

    protected KeyExpandAdapter(List<HeaderItem> data, OnRecordItemListener onRecordItemListener
            , boolean showCompetitor, boolean hideSequence, boolean showLose) {
        super(data);
        this.onRecordItemListener = onRecordItemListener;
        this.showCompetitor = showCompetitor;
        this.hideSequence = hideSequence;
        this.showLose = showLose;
    }

    @NonNull
    @Override
    public AbstractAdapterItem<Object> getItemView(Object type) {
        int itemType = (int) type;
        switch (itemType) {
            case ITEM_TYPE_HEAD:
                return new HeaderAdapter();
            case ITEM_TYPE_ITEM:
                return new SubItemAdapter(showCompetitor, hideSequence, showLose, onRecordItemListener);
        }
        return null;
    }

    @Override
    public Object getItemViewType(Object t) {
        if (t instanceof HeaderItem) {
            return ITEM_TYPE_HEAD;
        }
        else if (t instanceof SubItem) {
            return ITEM_TYPE_ITEM;
        }
        return -1;
    }

}
