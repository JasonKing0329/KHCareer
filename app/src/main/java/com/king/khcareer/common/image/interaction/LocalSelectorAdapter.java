package com.king.khcareer.common.image.interaction;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.king.khcareer.model.http.bean.ImageItemBean;
import com.king.khcareer.model.http.bean.ImageUrlBean;
import com.king.khcareer.common.image.ImageUtil;

/**
 * Created by Administrator on 2016/10/11.
 */
public class LocalSelectorAdapter extends ImageSelectorAdapter {
    public LocalSelectorAdapter(Context context, ImageUrlBean imageUrlBean) {
        super(context, imageUrlBean);
    }

    @Override
    public void onBindItemImage(ImageView imageView, ImageItemBean bean) {
        String url = "file://" + bean.getUrl();
        ImageUtil.load(url, imageView);
    }

    @Override
    public void onBindItemMark(ImageView markNew, ImageItemBean bean) {
        markNew.setVisibility(View.GONE);
    }
}
