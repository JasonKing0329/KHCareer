package com.king.mytennis.match;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;

import java.util.HashMap;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 13:58
 */
public class MatchEditDialog extends CustomDialog {

    private EditText edtName;
    private EditText edtSeq;

    public MatchEditDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_match_manage, null);
        edtName = (EditText) view.findViewById(R.id.match_manage_edit_name);
        edtSeq = (EditText) view.findViewById(R.id.match_manage_edit_sequence);
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
            String seq = edtSeq.getText().toString();
            if (TextUtils.isEmpty(seq)) {
                edtSeq.setError("Sequence can't be null");
                return;
            }
            MatchSeqBean bean = new MatchSeqBean();
            bean.setName(edtName.getText().toString());
            bean.setSequence(Integer.parseInt(seq));

            actionListener.onSave(bean);
        }
    }

    @Override
    public void show() {
        super.show();
        HashMap<String, Object> data = new HashMap<String, Object>();
        actionListener.onLoadData(data);
        MatchSeqBean bean = (MatchSeqBean) data.get("bean");
        if (bean == null) {
            edtName.setText("");
            edtSeq.setText("");
        }
        else {
            edtName.setText(bean.getName());
            edtSeq.setText(String.valueOf(bean.getSequence()));
        }
    }
}
