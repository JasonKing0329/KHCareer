package com.king.khcareer.glory.title;

import android.view.View;
import android.widget.TextView;

import com.king.mytennis.view.R;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/21 10:37
 */
public class HeaderAdapter extends AbstractExpandableAdapterItem {

    @BindView(R.id.tv_key)
    TextView tvKey;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    public int getLayoutResId() {
        return R.layout.adapter_glory_list_group;
    }

    @Override
    public void onBindViews(View root) {
        ButterKnife.bind(this, root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doExpandOrUnexpand();
            }
        });
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        HeaderItem item = (HeaderItem) model;
        tvKey.setText(item.getHeaderBean().getKey());
        tvContent.setText(item.getHeaderBean().getContent());
    }

    @Override
    public void onExpansionToggled(boolean expanded) {

    }
}
