package com.king.khcareer.common.image.interaction;

import android.content.Context;

import com.king.khcareer.model.http.bean.ImageItemBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
public class LocalImageSelector extends ImageSelector {
    public LocalImageSelector(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        hideSaveButton();
        showDeleteButton();
    }

    @Override
    protected void initData(HashMap<String, Object> data) {

    }

    @Override
    protected void initAdapter() {
        mAdapter = new LocalSelectorAdapter(getContext(), imageUrlBean);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onConfirm() {
        List<ImageItemBean> itemList = mAdapter.getSelectedKey();

        List<String> selectedList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i ++) {
            String path = itemList.get(i).getUrl();
            selectedList.add(path);
        }
        actionListener.onSave(selectedList);
    }
}
