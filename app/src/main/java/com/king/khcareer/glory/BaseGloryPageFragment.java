package com.king.khcareer.glory;

import android.content.Context;

import com.king.khcareer.base.BaseFragment;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/1 18:37
 */
public abstract class BaseGloryPageFragment extends BaseFragment {

    protected IGloryHolder gloryHolder;

    @Override
    protected void onAttachParent(Context context) {
        gloryHolder = (IGloryHolder) context;
    }

}
