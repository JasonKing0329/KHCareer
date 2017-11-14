package com.king.khcareer.login;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.FileIO;
import com.king.khcareer.common.config.Configuration;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class InitService implements Handler.Callback {

    private InitCallback initCallback;

    private Handler handler;

    private Activity activity;

    public InitService(Activity activity, InitCallback initCallback) {
        this.activity = activity;
        this.initCallback = initCallback;
        handler = new Handler(this);
    }

    public void execute() {
        new InitAppThread().start();
    }

    private void loadConfiguration() {

        Configuration.setInstance(new FileIO().readConfigInfor());
        MultiUserManager.getInstance().loadUsers(activity);
        MultiUserManager.getInstance().loadFromPreference();
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message.what == 1) {
            initCallback.onInitFinished();
        }
        return true;
    }

    public class InitAppThread extends Thread  {

        public void run() {
            loadConfiguration();

            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        }

    }
}
