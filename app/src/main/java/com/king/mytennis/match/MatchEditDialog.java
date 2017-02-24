package com.king.mytennis.match;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.king.mytennis.pubdata.bean.MatchBean;
import com.king.mytennis.pubdata.bean.MatchNameBean;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;

import java.util.HashMap;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 13:58
 */
public class MatchEditDialog extends CustomDialog implements AdapterView.OnItemSelectedListener {

    private EditText edtName;
    private EditText edtWeek;
    private EditText edtCountry;
    private EditText edtCity;
    private Spinner spLevel;
    private Spinner spCourt;
    private Spinner spRegion;
    private Spinner spMonth;

    private String[] arr_level, arr_court, arr_region, arr_month;
    private int nLevel, nCourt, nRegion, nMonth;

    public MatchEditDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_match_manage, null);
        edtName = (EditText) view.findViewById(R.id.match_manage_edit_name);
        edtWeek = (EditText) view.findViewById(R.id.match_manage_edit_week);
        edtCountry = (EditText) view.findViewById(R.id.match_manage_edit_country);
        edtCity = (EditText) view.findViewById(R.id.match_manage_edit_city);
        spLevel = (Spinner) view.findViewById(R.id.match_spinner_level);
        spCourt = (Spinner) view.findViewById(R.id.match_spinner_court);
        spRegion = (Spinner) view.findViewById(R.id.match_spinner_region);
        spMonth = (Spinner) view.findViewById(R.id.match_spinner_month);

        initSpinner();
        return view;
    }

    private void initSpinner() {
        arr_month = new String[12];
        for (int i = 0; i < 12;) {
            if (i < 9)
                arr_month[i] = "" + (++i);
            else
                arr_month[i] = "" + (++i);
        }
        arr_court = getContext().getResources().getStringArray(
                R.array.spinner_court);
        arr_level = getContext().getResources().getStringArray(
                R.array.spinner_level);
        arr_region = getContext().getResources().getStringArray(
                R.array.spinner_region);

        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, arr_month);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_item);
        spMonth.setAdapter(spinnerAdapter);
        spMonth.setOnItemSelectedListener(this);
        spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, arr_court);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourt.setAdapter(spinnerAdapter);
        spCourt.setOnItemSelectedListener(this);
        spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, arr_level);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLevel.setAdapter(spinnerAdapter);
        spLevel.setOnItemSelectedListener(this);
        spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, arr_region);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRegion.setAdapter(spinnerAdapter);
        spRegion.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spMonth) {
            nMonth = position;
        } else if (parent == spCourt) {
            nCourt = position;
        } else if (parent == spLevel) {
            nLevel = position;
        } else if (parent == spRegion) {
            nRegion = position;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected View getCustomToolbar() {
        return null;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == saveButton) {
            String week = edtWeek.getText().toString();
            if (TextUtils.isEmpty(week)) {
                edtWeek.setError("Week can't be null");
                return;
            }
            String name = edtName.getText().toString();
            if (TextUtils.isEmpty(name)) {
                edtName.setError("Name can't be null");
                return;
            }
            String country = edtCountry.getText().toString();
            if (TextUtils.isEmpty(country)) {
                edtCountry.setError("Country can't be null");
                return;
            }
            String city = edtCity.getText().toString();
            if (TextUtils.isEmpty(city)) {
                edtCity.setError("City can't be null");
                return;
            }
            MatchNameBean bean = new MatchNameBean();
            MatchBean mb = new MatchBean();
            bean.setName(name);
            mb.setCountry(country);
            mb.setCity(city);
            mb.setCourt(arr_court[nCourt]);
            mb.setLevel(arr_level[nLevel]);
            mb.setRegion(arr_region[nRegion]);
            mb.setMonth(Integer.parseInt(arr_month[nMonth]));
            mb.setWeek(Integer.parseInt(week));
            bean.setMatchBean(mb);

            actionListener.onSave(bean);
        }
    }

    @Override
    public void show() {
        super.show();
        HashMap<String, Object> data = new HashMap<String, Object>();
        actionListener.onLoadData(data);
        MatchNameBean bean = (MatchNameBean) data.get("bean");
        if (bean == null) {
            edtName.setText("");
            edtWeek.setText("");
            edtCountry.setText("");
            edtCity.setText("");
            spRegion.setSelection(0);
            spCourt.setSelection(0);
            spLevel.setSelection(0);
            spMonth.setSelection(0);
        }
        else {
            edtName.setText(bean.getName());
            edtWeek.setText(String.valueOf(bean.getMatchBean().getWeek()));
            edtCountry.setText(bean.getMatchBean().getCountry());
            edtCity.setText(bean.getMatchBean().getCity());
            setSpinnerSelection(spCourt, arr_court, bean.getMatchBean().getCourt());
            setSpinnerSelection(spRegion, arr_region, bean.getMatchBean().getRegion());
            setSpinnerSelection(spLevel, arr_level, bean.getMatchBean().getLevel());
            setSpinnerSelection(spMonth, arr_month, String.valueOf(bean.getMatchBean().getMonth()));
        }
    }

    public void setSpinnerSelection(Spinner spinner, String[] array, String text) {
        for (int i = 0; i < array.length; i ++) {
            if (array[i].equals(text)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
