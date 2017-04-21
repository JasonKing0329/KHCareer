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

    private final int ITEM_TYPE_HEAD = 1;
    private final int ITEM_TYPE_RECORD = 2;

    protected RecordAdapter(List<HeaderItem> data) {
        super(data);
    }

    @NonNull
    @Override
    public AbstractAdapterItem<Object> getItemView(Object type) {
        int itemType = (int) type;
        switch (itemType) {
            case ITEM_TYPE_HEAD:
                return new HeaderAdapter();
            case ITEM_TYPE_RECORD:
                return new RecordItemAdapter();
        }
        return null;
    }

    @Override
    public Object getItemViewType(Object t) {
        if (t instanceof HeaderItem) {
            return ITEM_TYPE_HEAD;
        }
        else if (t instanceof RecordItem) {
            return ITEM_TYPE_HEAD;
        }
        return -1;
    }

}
