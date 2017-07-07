package com.king.khcareer.player.h2hlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.king.khcareer.base.CustomDialog;
import com.king.mytennis.view.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/1 0001.
 */

public class SortDialog extends CustomDialog {

    /**
     * refer h2hlist_sort_type in array.xml
     */
    public static final int SORT_TYPE_NAME = 0;
    public static final int SORT_TYPE_TOTAL = 1;
    public static final int SORT_TYPE_WIN = 2;
    public static final int SORT_TYPE_LOSE = 3;
    public static final int SORT_TYPE_PUREWIN = 4;
    public static final int SORT_TYPE_PURELOSE = 5;

    /**
     * refer sort_order in array.xml
     */
    public static final int SORT_ORDER_ASC = 0;
    public static final int SORT_ORDER_DESC = 1;

    private Spinner spType;
    private Spinner spOrder;
    private String[] arrType, arrOrder;

    public SortDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_h2hlist_sort, null);
        spOrder = (Spinner) view.findViewById(R.id.sp_order);
        spType = (Spinner) view.findViewById(R.id.sp_type);

        arrOrder = getContext().getResources().getStringArray(R.array.sort_order);
        arrType = getContext().getResources().getStringArray(R.array.h2hlist_sort_type);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, arrType);
        spType.setAdapter(adapter);
        spType.setSelection(0);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, arrOrder);
        spOrder.setAdapter(adapter);
        spOrder.setSelection(0);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == saveButton) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", spType.getSelectedItemPosition());
            map.put("order", spOrder.getSelectedItemPosition());
            actionListener.onSave(map);
        }
        super.onClick(view);
    }

    @Override
    protected View getCustomToolbar() {
        setTitleIcon(R.drawable.ic_sort_grey_700_36dp);
        return null;
    }
}
