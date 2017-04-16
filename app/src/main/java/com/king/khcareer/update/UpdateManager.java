package com.king.khcareer.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

import com.king.khcareer.download.DownloadDialog;
import com.king.khcareer.download.DownloadItem;
import com.king.khcareer.model.http.Command;
import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.utils.DebugLog;
import com.king.khcareer.base.CustomDialog;
import com.king.mytennis.view.R;
import com.king.khcareer.settings.SettingProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */
public class UpdateManager implements IUpdateView {

    private UpdatePresenter mPresenter;
    private Context mContext;
    private UpdateListener updateListener;

    private boolean isUpdating;
    private boolean isShowing;

    private boolean showMessageWarning;

    public UpdateManager(Context context) {
        mContext = context;
        mPresenter = new UpdatePresenter(this);
    }

    public void setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public void showMessageWarning() {
        showMessageWarning = true;
    }

    public void startCheck() {
        if (!TextUtils.isEmpty(SettingProperty.getServerBaseUrl(mContext))) {
            // 检测App更新，必须在配置过服务器以后
            mPresenter.checkAppUpdate(mContext);
        }
    }
    @Override
    public void onAppUpdateFound(final AppCheckBean bean) {
        isShowing = true;
        String msg = String.format(mContext.getString(R.string.app_update_found), bean.getAppVersion());
        showOptionDialog(mContext, null, msg
                , mContext.getResources().getString(R.string.yes)
                , null
                , mContext.getResources().getString(R.string.no)
                , new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            isUpdating = true;
                            startDownloadNewApp(bean);
                        }
                    }
                }
                , new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (updateListener != null) {
                            updateListener.onUpdateDialogDismiss();
                        }
                    }
                }
        );
        if (updateListener != null) {
            updateListener.onUpdateDialogShow();
        }
    }

    /**
     *
     * @param context
     * @param msg
     * @param positiveText
     * @param neutralText can be null
     * @param negativeText
     * @param clickListener
     * @param dismissListener
     */
    public void showOptionDialog(Context context, String title, String msg, String positiveText
            , String neutralText, String negativeText, DialogInterface.OnClickListener clickListener, DialogInterface.OnDismissListener dismissListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title == null) {
            title = context.getString(R.string.warning);
        }
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveText, clickListener);
        if (neutralText != null) {
            builder.setNeutralButton(neutralText, clickListener);
        }
        builder.setNegativeButton(negativeText, clickListener);
        builder.setOnDismissListener(dismissListener);
        builder.show();
    }

    @Override
    public void onAppIsLatest() {
        if (showMessageWarning) {
            Toast.makeText(mContext, R.string.app_is_latest, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onServiceDisConnected() {
        DebugLog.e("服务器连接失败");
        if (showMessageWarning) {
            Toast.makeText(mContext, R.string.gdb_server_offline, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestError() {
        DebugLog.e("更新app失败");
        if (showMessageWarning) {
            Toast.makeText(mContext, R.string.gdb_request_fail, Toast.LENGTH_LONG).show();
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    private void startDownloadNewApp(final AppCheckBean bean) {
        final DownloadDialog dialog = new DownloadDialog(mContext, new CustomDialog.OnCustomDialogActionListener() {
            @Override
            public boolean onSave(Object object) {
                return false;
            }

            @Override
            public boolean onCancel() {
                return false;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {
                DownloadItem item = new DownloadItem();
                item.setKey(bean.getAppName());
                item.setFlag(Command.TYPE_APP);
                item.setSize(bean.getAppSize());
                item.setName(bean.getAppName());
                List<DownloadItem> list = new ArrayList<>();
                list.add(item);

                data.put("items", list);
                data.put("savePath", Configuration.APP_UPDATE_DIR);
                data.put("noOption", true);

                // 下载之前删掉以前下载的APK
                mPresenter.clearAppFolder();
            }
        });
        dialog.setOnDownloadListener(new DownloadDialog.OnDownloadListener() {
            @Override
            public void onDownloadFinish(DownloadItem item) {
                isUpdating = false;
                mPresenter.installApp((Activity) mContext, item.getPath());
                dialog.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
            }

            @Override
            public void onDownloadFinish(List<DownloadItem> downloadList) {

            }
        });
        dialog.show();
    }

    public boolean isUpdating() {
        return isUpdating;
    }
}
