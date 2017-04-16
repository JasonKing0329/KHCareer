package com.king.khcareer.common.image.interaction;

import android.content.Context;

import com.king.khcareer.download.DownloadItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 * 根据downloadFlag，确认后需要组装下载列表数据
 */
public class HttpImageSelector extends ImageSelector {

    public HttpImageSelector(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
    }

    @Override
    protected void initData(HashMap<String, Object> map) {
    }

    @Override
    protected void initAdapter() {
        mAdapter = new HttpSelectorAdapter(getContext(), imageUrlBean);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onConfirm() {
        List<Integer> indexList = mAdapter.getSelectedKey();
        List<DownloadItem> list = new ArrayList<>();
        for (int i = 0; i < indexList.size(); i ++) {
            DownloadItem item = new DownloadItem();
            item.setKey(imageUrlBean.getUrlList().get(indexList.get(i)));
            item.setFlag(imageFlag);
            item.setSize(imageUrlBean.getSizeList().get(indexList.get(i)));

            String url = imageUrlBean.getUrlList().get(indexList.get(i));
            if (url.contains("/")) {
                String[] array = url.split("/");
                url = array[array.length - 1];
            }
            item.setName(url);

            list.add(item);
        }
        actionListener.onSave(list);
    }
}
