package com.king.khcareer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/5 10:52
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachParent(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentLayoutRes(), container, false);
        onCreate(view);
        return view;
    }

    protected abstract void onAttachParent(Context context);

    protected abstract int getContentLayoutRes();

    protected abstract void onCreate(View view);

}
