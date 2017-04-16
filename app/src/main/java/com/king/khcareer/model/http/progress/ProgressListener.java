package com.king.khcareer.model.http.progress;

public interface ProgressListener {

    /**
     * 更新进度
     *
     * @param bytesRead
     * @param contentLength
     * @param done
     */
    void update(long bytesRead, long contentLength, boolean done);

}
