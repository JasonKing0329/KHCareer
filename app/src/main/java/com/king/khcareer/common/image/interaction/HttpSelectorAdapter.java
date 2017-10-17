package com.king.khcareer.common.image.interaction;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.king.khcareer.model.http.Command;
import com.king.khcareer.model.http.bean.ImageItemBean;
import com.king.khcareer.model.http.bean.ImageUrlBean;
import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.common.image.ImageUtil;
import com.king.khcareer.settings.SettingProperty;

import java.io.File;

/**
 * Created by Administrator on 2016/10/11.
 */
public class HttpSelectorAdapter extends ImageSelectorAdapter {

    public HttpSelectorAdapter(Context context, ImageUrlBean imageUrlBean) {
        super(context, imageUrlBean);
    }

    @Override
    public void onBindItemImage(ImageView imageView, ImageItemBean bean) {

        String url = "http://" + SettingProperty.getServerBaseUrl(mContext) + bean.getUrl();
        ImageUtil.load(url, imageView);
    }

    @Override
    public void onBindItemMark(ImageView markNew, ImageItemBean bean) {

        markNew.setVisibility(View.VISIBLE);

        File file = null;
        if (bean.getKey().equals(Command.TYPE_IMG_PLAYER)) {
            file = new File(Configuration.IMG_PLAYER_BASE + imageUrlBean.getKey());
        }
        else if (bean.getKey().equals(Command.TYPE_IMG_MATCH)) {
            file = new File(Configuration.IMG_MATCH_BASE + imageUrlBean.getKey());
        }
        else if (bean.getKey().equals(Command.TYPE_IMG_PLAYER_HEAD)) {
            file = new File(Configuration.IMG_PLAYER_HEAD + imageUrlBean.getKey());
        }
        // 如果本地已存在，不显示new角标
        if (file != null && file.exists() && file.isDirectory()) {
            File files[] = file.listFiles();
            for (File f:files) {
                if (bean.getUrl().endsWith(f.getName())) {
                    markNew.setVisibility(View.GONE);
                    break;
                }
            }
        }
    }
}
