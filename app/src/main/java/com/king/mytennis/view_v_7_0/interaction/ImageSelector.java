package com.king.mytennis.view_v_7_0.interaction;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.king.mytennis.download.DownloadItem;
import com.king.mytennis.http.bean.ImageUrlBean;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class ImageSelector extends CustomDialog {

    private RecyclerView recyclerView;

    private ImageSelectorAdapter mAdapter;
    private ImageUrlBean imageUrlBean;

    private String downloadFlag;

    public ImageSelector(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);

        HashMap<String, Object> map = new HashMap<>();
        actionListener.onLoadData(map);
        imageUrlBean = (ImageUrlBean) map.get("data");
        downloadFlag = (String) map.get("flag");
        setTitle(imageUrlBean.getKey());
    }

    @Override
    protected View getCustomView() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_image_selector, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.dlg_bg_selector_recyclerview);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ImageSelectorAdapter(getContext(), imageUrlBean);
        recyclerView.setAdapter(mAdapter);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, getContext().getResources().getDimensionPixelSize(R.dimen.dlg_loadfrom_list_height)
        );
        view.setLayoutParams(params);

        return view;
    }

    @Override
    protected View getCustomToolbar() {
        return null;
    }

    @Override
    public void onClick(View view) {
        if (view == saveButton) {
            List<Integer> indexList = mAdapter.getSelectedKey();
            List<DownloadItem> list = new ArrayList<>();
            for (int i = 0; i < indexList.size(); i ++) {
                DownloadItem item = new DownloadItem();
                item.setKey(imageUrlBean.getUrlList().get(0));
                item.setFlag(downloadFlag);
                item.setSize(imageUrlBean.getSizeList().get(0));
                item.setName(imageUrlBean.getUrlList().get(0));
                list.add(item);
            }
            actionListener.onSave(list);
        }
        super.onClick(view);
    }
}
