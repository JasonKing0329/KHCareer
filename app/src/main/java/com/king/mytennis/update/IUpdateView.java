package com.king.mytennis.update;

/**
 * Created by Administrator on 2016/9/6.
 */
public interface IUpdateView {
    void onAppUpdateFound(AppCheckBean bean);
    void onAppIsLatest();
    void onServiceDisConnected();
    void onRequestError();
}
