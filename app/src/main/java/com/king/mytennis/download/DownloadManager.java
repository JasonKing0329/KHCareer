package com.king.mytennis.download;

import com.king.mytennis.http.DownloadClient;
import com.king.mytennis.http.progress.ProgressListener;
import com.king.mytennis.utils.DebugLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/9/2.
 */
public class DownloadManager {

    private class DownloadPack {
        DownloadItem item;
        ProgressListener progressListener;
        public DownloadPack(DownloadItem item, ProgressListener progressListener) {
            this.item = item;
            this.progressListener = progressListener;
        }
    }

    private int MAX_TASK;
    private Queue<DownloadPack> downloadQueue;
    private List<DownloadPack> executingdList;
    private DownloadCallback mCallback;

    private String savePath;

    public DownloadManager(DownloadCallback callback, int maxTask) {
        MAX_TASK = maxTask;
        mCallback = callback;
        downloadQueue = new LinkedList<>();
        executingdList = new ArrayList<>();
    }

    public void setSavePath(String path) {
        savePath = path;
    }

    public void downloadFile(DownloadItem item, ProgressListener progressListener) {
        DebugLog.e(item.getKey());
        // 检查正在执行的任务，如果已经在执行则放弃重复执行，没有则新建下载任务
        for (DownloadPack pack:executingdList) {
            if (pack.item.getKey().equals(item.getKey()) && pack.item.getFlag().equals(item.getFlag())) {
                return;
            }
        }

        // 新建下载任务
        DebugLog.e("new pack:" + item.getKey());
        DownloadPack pack = new DownloadPack(item, progressListener);

        // 如果正在执行的任务已经达到MAX_TASK，则进入下载队列进行排队
        if (executingdList.size() >= MAX_TASK) {
            DebugLog.e("进入排队:" + item.getKey());
            downloadQueue.offer(pack);
            return;
        }

        // 满足执行条件，开始执行新的下载任务
        startDownloadService(pack);
    }

    private boolean startDownloadService(final DownloadPack pack) {
        if (pack == null) {
            DebugLog.e("没有排队的任务了");
            // 这里只是确认没有待排队下载的任务了
            return false;
        }
        // 任务可执行，加入到正在执行列表中
        DebugLog.e("开始执行任务：" + pack.item.getKey());
        executingdList.add(pack);

        // 开始网络请求下载
        new DownloadClient(pack.progressListener).getDownloadService().download(pack.item.getKey(), pack.item.getFlag())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<ResponseBody>() {
                @Override
                public void onCompleted() {
                    DebugLog.e("任务完成：" + pack.item.getKey());
                    completeDownload(pack);
                }

                @Override
                public void onError(Throwable e) {
                    DebugLog.e(e.toString());
                    for (StackTraceElement element:e.getStackTrace()) {
                        DebugLog.e(element.toString());
                    }
                    completeDownload(pack);
                    mCallback.onDownloadError(pack.item);
                }

                @Override
                public void onNext(final ResponseBody responseBody) {
                    saveFile(pack.item.getName(), responseBody.byteStream());
                    mCallback.onDownloadFinish(pack.item);
                }
            });
        return true;
    }

    private void completeDownload(DownloadPack pack) {
        // 完成后从正在执行列表中删除当前任务
        for (DownloadPack execPack:executingdList) {
            if (pack == execPack) {
                executingdList.remove(execPack);
                break;
            }
        }
        // 从排队队列中选取排在最前面的任务进行执行
        if (startDownloadService(downloadQueue.peek())) {
            downloadQueue.poll();
        }

        // 所有任务执行完了才是真的全部下载完了
        if (executingdList.size() == 0) {
            mCallback.onDownloadAllFinish();
        }
    }

    /**
     * 保存应用
     *
     * @param input  输入流
     */
    private File saveFile(String filename, InputStream input) {
        File file = new File(savePath + "/" + filename);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int ch;
            while ((ch = input.read(buf)) != -1) {
                fileOutputStream.write(buf, 0, ch);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}
