package com.king.khcareer.common.image.interaction;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

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
    protected void onBindItemImage(ImageView imageView, int position) {
        String url = "file://" + imageUrlBean.getUrlList().get(position);
        ImageUtil.load(url, imageView);
    }

    @Override
    protected void onBindItemMark(ImageView markNew, int position) {
        markNew.setVisibility(View.GONE);
    }
}
