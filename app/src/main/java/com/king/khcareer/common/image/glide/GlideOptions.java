package com.king.khcareer.common.image.glide;

import com.bumptech.glide.request.RequestOptions;
import com.king.mytennis.view.R;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/17 11:02
 */
public class GlideOptions {

    public static RequestOptions getCommonOptions() {
        RequestOptions commonOptions = new RequestOptions();
        commonOptions.centerCrop();
//            commonOptions.placeholder(R.drawable.default_loading);
        commonOptions.error(R.drawable.default_img);
        return commonOptions;
    }

    public static RequestOptions getDefaultPlayerOptions() {
        RequestOptions commonOptions = new RequestOptions();
        commonOptions.centerCrop();
        commonOptions.error(R.drawable.pic_def);
        return commonOptions;
    }

    public static RequestOptions getDefaultMatchOptions() {
        RequestOptions commonOptions = new RequestOptions();
        commonOptions.centerCrop();
        commonOptions.error(R.drawable.default_img);
        return commonOptions;
    }

    public static RequestOptions getEditorPlayerOptions() {
        RequestOptions commonOptions = new RequestOptions();
        commonOptions.centerCrop();
        commonOptions.error(R.drawable.view7_folder_cover_player);
        return commonOptions;
    }

    public static RequestOptions getEditorMatchOptions() {
        RequestOptions commonOptions = new RequestOptions();
        commonOptions.centerCrop();
        commonOptions.error(R.drawable.view7_folder_cover_more);
        return commonOptions;
    }

}
