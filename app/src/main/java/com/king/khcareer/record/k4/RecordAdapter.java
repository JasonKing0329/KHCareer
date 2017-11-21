package com.king.khcareer.record.k4;

import android.support.annotation.NonNull;

import com.zaihuishou.expandablerecycleradapter.adapter.BaseExpandableAdapter;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/21 16:07
 */
public class RecordAdapter extends BaseExpandableAdapter {

    private final int ITEM_TYPE_YEAR = 1;
    private final int ITEM_TYPE_HEAD = 2;
    private final int ITEM_TYPE_RECORD = 3;

    private OnItemMenuListener onItemMenuListener;
    private OnHeadLongClickListener onHeadLongClickListener;

    protected RecordAdapter(List<YearItem> data, OnItemMenuListener onItemMenuListener, OnHeadLongClickListener onHeadLongClickListener) {
        super(data);
        this.onItemMenuListener = onItemMenuListener;
        this.onHeadLongClickListener = onHeadLongClickListener;
    }

    @NonNull
    @Override
    public AbstractAdapterItem<Object> getItemView(Object type) {
        int itemType = (int) type;
        switch (itemType) {
            case ITEM_TYPE_YEAR:
                return new YearAdapter();
            case ITEM_TYPE_HEAD:
                return new HeaderAdapter(onHeadLongClickListener);
            case ITEM_TYPE_RECORD:
                return new RecordItemAdapter(onItemMenuListener);
        }
        return null;
    }

    @Override
    public Object getItemViewType(Object t) {
        if (t instanceof YearItem) {
            return ITEM_TYPE_YEAR;
        }
        else if (t instanceof HeaderItem) {
            return ITEM_TYPE_HEAD;
        }
        else if (t instanceof RecordItem) {
            return ITEM_TYPE_RECORD;
        }
        return -1;
    }

}
