package com.king.khcareer.common.image.interaction;

import android.content.Context;

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
        List<Integer> indexList = mAdapter.getSelectedKey();

        List<String> selectedList = new ArrayList<>();
        for (int i = 0; i < indexList.size(); i ++) {
            String path = imageUrlBean.getUrlList().get(indexList.get(i));
            selectedList.add(path);
        }
        actionListener.onSave(selectedList);
    }
}
