package com.king.khcareer.player.manage;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.utils.PinyinUtil;
import com.king.khcareer.base.CustomDialog;
import com.king.mytennis.view.R;

import java.util.HashMap;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 13:58
 */
public class PlayerEditDialog extends CustomDialog {

    private EditText edtName;
    private EditText edtNameEng;
    private EditText edtBirthday;
    private EditText edtCountry;
    private EditText edtCity;

    public PlayerEditDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_player_manage, null);
        edtName = (EditText) view.findViewById(R.id.player_manage_edit_name);
        edtBirthday = (EditText) view.findViewById(R.id.player_manage_edit_birthday);
        edtCountry = (EditText) view.findViewById(R.id.player_manage_edit_country);
        edtCity = (EditText) view.findViewById(R.id.player_manage_edit_city);
        edtNameEng = (EditText) view.findViewById(R.id.player_manage_edit_name_eng);

        return view;
    }

    @Override
    protected View getCustomToolbar() {
        return null;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == saveButton) {
            // all params can be null except name and country
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
            String birthday = edtBirthday.getText().toString();
            String engName = edtNameEng.getText().toString();
            PlayerBean bean = new PlayerBean();
            bean.setBirthday(birthday);
            bean.setCountry(country);
            bean.setCity(city);
            bean.setNameChn(name);
            bean.setNameEng(engName);
            bean.setNamePinyin(PinyinUtil.getPinyin(name));

            actionListener.onSave(bean);
        }
    }

    @Override
    public void show() {
        super.show();
        HashMap<String, Object> data = new HashMap<String, Object>();
        actionListener.onLoadData(data);
        PlayerBean bean = (PlayerBean) data.get("bean");
        if (bean == null) {
            edtName.setText("");
            edtBirthday.setText("");
            edtCountry.setText("");
            edtCity.setText("");
            edtNameEng.setText("");
        }
        else {
            edtName.setText(bean.getNameChn());
            edtNameEng.setText(bean.getNameEng());
            edtCountry.setText(bean.getCountry());
            edtCity.setText(bean.getCity());
            edtBirthday.setText(bean.getBirthday());
        }
    }
}
