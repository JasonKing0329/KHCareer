package com.king.khcareer.player.h2hlist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.ImageUtil;
import com.king.khcareer.utils.DebugLog;
import com.king.mytennis.view.R;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/21 16:15
 */
public class HeaderAdapter extends AbstractExpandableAdapterItem {

    private TextView tvName;
    private TextView tvCountry;
    private TextView tvWin;
    private TextView tvLose;
    private ImageView ivPlayer;

    private int colorWin;
    private int colorLose;
    private int colorTie;

    // only random image path for the first time
    private Map<String, String> imagePathMap;

    public HeaderAdapter() {
        imagePathMap = new HashMap<>();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.adapter_h2hlist_group;
    }

    @Override
    public void onBindViews(View root) {
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doExpandOrUnexpand();
            }
        });
        tvName = (TextView) root.findViewById(R.id.tv_name);
        tvCountry = (TextView) root.findViewById(R.id.tv_country);
        tvWin = (TextView) root.findViewById(R.id.tv_win);
        tvLose = (TextView) root.findViewById(R.id.tv_lose);
        ivPlayer = (ImageView) root.findViewById(R.id.iv_player);

        colorWin = root.getContext().getResources().getColor(R.color.h2hlist_color_win);
        colorLose = root.getContext().getResources().getColor(R.color.h2hlist_color_lose);
        colorTie = root.getContext().getResources().getColor(R.color.h2hlist_color_tie);
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        DebugLog.e("position=" + position);
        HeaderItem item = (HeaderItem) model;
        for (int i = 0; i < item.getChildItemList().size(); i ++) {
            item.getChildItemList().get(i).setHeaderPosition(position);
        }

        H2hHeaderBean bean = item.getHeader();
        tvName.setText(String.valueOf(bean.getPlayer()));
        tvCountry.setText(bean.getCountry());
        tvWin.setText(String.valueOf(bean.getWin()));
        tvLose.setText(String.valueOf(bean.getLose()));
        if (bean.getWin() > bean.getLose()) {
            tvWin.setTextColor(colorWin);
            tvLose.setTextColor(colorLose);
        }
        else if (bean.getWin() < bean.getLose()) {
            tvWin.setTextColor(colorLose);
            tvLose.setTextColor(colorWin);
        }
        else {
            tvWin.setTextColor(colorTie);
            tvLose.setTextColor(colorTie);
        }

        if (imagePathMap.get(bean.getPlayer()) == null) {
            String path = ImageFactory.getDetailPlayerPath(bean.getPlayer());
            imagePathMap.put(bean.getPlayer(), path);
        }
        ImageUtil.load("file://" + imagePathMap.get(bean.getPlayer()), ivPlayer, R.drawable.image_load_error);
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onExpansionToggled(boolean expanded) {

    }
}
