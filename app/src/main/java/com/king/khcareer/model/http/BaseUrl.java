package com.king.khcareer.model.http;

import com.king.khcareer.utils.DebugLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/3 0003.
 */
public class BaseUrl {
    private static BaseUrl instance;
    private  String baseUrl;
    private List<BaseUrlSubscriber> subscribers;

    private BaseUrl() {
        subscribers = new ArrayList<>();
    }

    public static BaseUrl getInstance() {
        if (instance == null) {
            instance = new BaseUrl();
        }
        return instance;
    }

    public  String getBaseUrl() {
        return "http://" + baseUrl + "/";
    }

    public void addSubscriber(BaseUrlSubscriber subscriber) {
        if (!subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }
    }

    public  void setBaseUrl(String baseUrl) {
        DebugLog.e(baseUrl);
        if (!baseUrl.equals(this.baseUrl)) {
            this.baseUrl = baseUrl;

            for (BaseUrlSubscriber subscriber:subscribers) {
                subscriber.onBaseUrlChanged(getBaseUrl());
            }
        }
    }
}
