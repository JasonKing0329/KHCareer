package com.king.khcareer.common.image.interaction;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.king.khcareer.base.KApplication;
import com.king.khcareer.common.image.glide.GlideOptions;
import com.king.khcareer.model.http.bean.ImageItemBean;
import com.king.khcareer.model.http.bean.ImageUrlBean;

/**
 * Created by Administrator on 2016/10/11.
 */
public class LocalSelectorAdapter extends ImageSelectorAdapter {

    private RequestOptions imageOptions;

    public LocalSelectorAdapter(Context context, ImageUrlBean imageUrlBean) {
        super(context, imageUrlBean);
        imageOptions = GlideOptions.getCommonOptions();
    }

    @Override
    public void onBindItemImage(ImageView imageView, ImageItemBean bean) {
        Glide.with(KApplication.getInstance())
                .load(bean.getUrl())
                .apply(imageOptions)
                .into(imageView);
    }

    @Override
    public void onBindItemMark(ImageView markNew, ImageItemBean bean) {
        markNew.setVisibility(View.GONE);
    }
}
