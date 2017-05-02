package com.king.khcareer.player.h2hlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.king.khcareer.base.CustomDialog;
import com.king.mytennis.view.R;

/**
 * Created by Administrator on 2017/5/1 0001.
 */

public class FilterDialog extends CustomDialog implements AdapterView.OnItemSelectedListener {

    private Spinner spType;
    private String[] arrType;
    private FilterCallback filterCallback;
    private EditText etValue1, etValue2;

    public FilterDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_h2hlist_filter, null);
        spType = (Spinner) view.findViewById(R.id.sp_type);
        etValue1 = (EditText) view.findViewById(R.id.et_value1);
        etValue2 = (EditText) view.findViewById(R.id.et_value2);

        spType.setOnItemSelectedListener(this);
        arrType = getContext().getResources().getStringArray(R.array.h2hlist_filter_type);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, arrType);
        spType.setAdapter(adapter);
        spType.setSelection(0);

        return view;
    }

    @Override
    protected View getCustomToolbar() {
        return null;
    }

    @Override
    public void onClick(View view) {
        if (view == saveButton) {
            if (filterCallback != null) {
                switch (spType.getSelectedItemPosition()) {
                    case 0:// none
                        filterCallback.onFilterNothing();
                        break;
                    case 1:// country
                        filterCallback.onFilterCountry(etValue1.getText().toString());
                        break;
                    case 2:// rank
                        filterCallback.onFilterRank(getMin(), getMax());
                        break;
                    case 3:// count
                        filterCallback.onFilterCount(getMin(), getMax());
                        break;
                    case 4:// win
                        filterCallback.onFilterWin(getMin(), getMax());
                        break;
                    case 5:// lose
                        filterCallback.onFilterLose(getMin(), getMax());
                        break;
                    case 6:// delta
                        filterCallback.onFilterDeltaWin(getMin(), getMax());
                        break;
                }
            }
            super.onClick(view);
        }
    }

    private int getMax() {
        int max;
        try {
            max = Integer.parseInt(etValue2.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            max = Integer.MAX_VALUE;
        }
        return max;
    }

    private int getMin() {
        int min;
        try {
            min = Integer.parseInt(etValue1.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            min = Integer.MIN_VALUE;
        }
        return min;
    }

    public void setFilterCallback(FilterCallback filterCallback) {
        this.filterCallback = filterCallback;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:// none
                etValue1.setVisibility(View.INVISIBLE);
                etValue2.setVisibility(View.INVISIBLE);
                break;
            case 1:// country
                etValue1.setVisibility(View.VISIBLE);
                etValue2.setVisibility(View.INVISIBLE);
                etValue1.setText("");
                etValue1.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                break;
            case 2:// rank
            case 3:// count
            case 4:// win
            case 5:// lose
            case 6:// delta
                etValue1.setVisibility(View.VISIBLE);
                etValue2.setVisibility(View.VISIBLE);
                etValue1.setText("");
                etValue2.setText("");
                etValue1.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                etValue2.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface FilterCallback {
        void onFilterNothing();
        void onFilterCountry(String country);
        void onFilterRank(int min, int max);
        void onFilterCount(int min, int max);
        void onFilterWin(int min, int max);
        void onFilterLose(int min, int max);
        void onFilterDeltaWin(int min, int max);
    }
}
