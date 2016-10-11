package com.king.mytennis.view_v_7_0.interaction;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.king.mytennis.http.Command;
import com.king.mytennis.http.bean.ImageUrlBean;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.service.ImageUtil;
import com.king.mytennis.view.settings.SettingProperty;

import java.io.File;

/**
 * Created by Administrator on 2016/10/11.
 */
public class HttpSelectorAdapter extends ImageSelectorAdapter {

    public HttpSelectorAdapter(Context context, ImageUrlBean imageUrlBean) {
        super(context, imageUrlBean);
    }

    @Override
    protected void onBindItemImage(ImageView imageView, int position) {
        String url = "http://" + SettingProperty.getServerBaseUrl(mContext) + imageUrlBean.getUrlList().get(position);
        ImageUtil.load(url, imageView);
    }

    @Override
    protected void onBindItemMark(ImageView markNew, int position) {

        markNew.setVisibility(View.VISIBLE);

        File file = null;
        if (imageFlag.equals(Command.TYPE_IMG_PLAYER)) {
            file = new File(Configuration.IMG_PLAYER_BASE + imageUrlBean.getKey());
        }
        else if (imageFlag.equals(Command.TYPE_IMG_MATCH)) {
            file = new File(Configuration.IMG_MATCH_BASE + imageUrlBean.getKey());
        }
        else if (imageFlag.equals(Command.TYPE_IMG_PLAYER_HEAD)) {
            file = new File(Configuration.IMG_PLAYER_HEAD + imageUrlBean.getKey());
        }
        // 如果本地已存在，不显示new角标
        if (file != null && file.exists() && file.isDirectory()) {
            File files[] = file.listFiles();
            for (File f:files) {
                if (imageUrlBean.getUrlList().get(position).endsWith(f.getName())) {
                    markNew.setVisibility(View.GONE);
                    break;
                }
            }
        }
    }
}
