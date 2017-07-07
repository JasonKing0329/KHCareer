package com.king.khcareer.download;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.king.khcareer.model.http.progress.ProgressListener;
import com.king.khcareer.base.CustomDialog;
import com.king.mytennis.view.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class DownloadDialog extends CustomDialog implements DownloadCallback, Handler.Callback {

    public interface OnDownloadListener {
        void onDownloadFinish(DownloadItem item);
        void onDownloadFinish(List<DownloadItem> downloadList);
    }

    private TextView emptyView;
    private RecyclerView downloadRecyclerView;
    private DownloadAdapter adapter;

    /**
     * 全部下载内容
     */
    private List<DownloadItemProxy> itemList;
    /**
     * 直接下载，不提示
     */
    private boolean startNoOption;
    /**
     * 不直接下载的提示内容
     */
    private String optionMessage;
    /**
     * 下载目录
     */
    private String savePath;

    /**
     * 标志重新下载（不是重新打开下载框）
     */
    private boolean newUpdateFlag;

    private DownloadManager downloadManager;
    private Handler handler;

    private OnDownloadListener onDownloadListener;
    private List<DownloadItem> downloadList;

    public DownloadDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        setTitle(R.string.download_title);

        itemList = new ArrayList<>();
        downloadManager = new DownloadManager(this, 1);
        handler = new Handler(this);

        HashMap<String, Object> map = new HashMap<>();
        actionListener.onLoadData(map);

        downloadList = (List<DownloadItem>) map.get("items");
        savePath = (String) map.get("savePath");
        if (map.get("noOption") != null) {
            startNoOption = (Boolean) map.get("noOption");
        }
        if (map.get("optionMsg") != null) {
            optionMessage = (String) map.get("optionMsg");
        }

        downloadManager.setSavePath(savePath);
        fillProxy(downloadList);
        newUpdateFlag = true;
    }

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
    }

    private void fillProxy(List<DownloadItem> list) {
        itemList.clear();
        for (DownloadItem item:list) {
            DownloadItemProxy proxy = new DownloadItemProxy();
            proxy.setItem(item);
            proxy.setProgress(0);
            itemList.add(proxy);
        }
    }

    @Override
    protected View getCustomView() {
        View view = getLayoutInflater().inflate(R.layout.dlg_download, null);
        emptyView = (TextView) view.findViewById(R.id.download_empty);
        downloadRecyclerView = (RecyclerView) view.findViewById(R.id.download_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        downloadRecyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    protected View getCustomToolbar() {
        setTitleIcon(R.drawable.ic_file_download_grey_700_36dp);
        return null;
    }

    @Override
    public void show() {
        super.show();

        if (newUpdateFlag) {
            updateDownloadList();
        }
    }

    private void updateDownloadList() {
        if (itemList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            downloadRecyclerView.setVisibility(View.INVISIBLE);
        }
        else {
            if (startNoOption) {
                showListAndStartDownload();
            }
            else {
                new AlertDialog.Builder(getContext())
                        .setMessage(optionMessage)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showListAndStartDownload();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
                            }
                        })
                        .show();
            }
        }
    }

    private void showListAndStartDownload() {
        // 开始下载任务后再show就要显示之前的内容
        newUpdateFlag = false;

        adapter = new DownloadAdapter(getContext(), itemList);
        downloadRecyclerView.setAdapter(adapter);
        startDownload();
    }

    private void startDownload() {
        for (int i = 0; i < itemList.size(); i ++) {
            final int index = i;
            downloadManager.downloadFile(itemList.get(i).getItem(), new ProgressListener() {
                private int lastProgress;
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    int progress = (int)(100 * 1f * bytesRead / contentLength);
//                    DebugLog.e("progress:" + progress);

                    if (progress - lastProgress > 8 || done) {// 避免更新太过频繁
                        lastProgress = progress;
                        Bundle bundle = new Bundle();
                        bundle.putInt("index", index);
                        bundle.putInt("progress", progress);
                        Message message = new Message();
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                }
            });
        }
    }

    public void newUpdate(List<DownloadItem> downloadItems) {
        fillProxy(downloadItems);
        newUpdateFlag = true;
    }

    @Override
    public void onDownloadFinish(DownloadItem item) {
        if (onDownloadListener != null) {

            // 下载完成后才设置文件路径
            item.setPath(savePath + "/" + item.getName());
            onDownloadListener.onDownloadFinish(item);
        }
    }

    @Override
    public void onDownloadError(DownloadItem item) {

    }

    @Override
    public void onDownloadAllFinish() {
        if (onDownloadListener != null) {
            for (DownloadItem item:downloadList) {
                item.setPath(savePath + "/" + item.getName());
            }
            onDownloadListener.onDownloadFinish(downloadList);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        Bundle bundle = msg.getData();
        int index = bundle.getInt("index");
        int progress = bundle.getInt("progress");
        itemList.get(index).setProgress(progress);
        adapter.notifyDataSetChanged();
        return false;
    }
}
