package com.king.khcareer.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2017/1/2 0002.
 */

@TargetApi(Build.VERSION_CODES.M)
public class PermissionUtil {

    public static void requestOtherPermission(Activity activity) {
        int status = ContextCompat.checkSelfPermission(activity, Manifest.permission.WAKE_LOCK);
        if (status != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WAKE_LOCK},0);
        }
        status = ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);
        if (status != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.INTERNET},0);
        }
        status = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_WIFI_STATE);
        if (status != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_WIFI_STATE},0);
        }
        status = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE);
        if (status != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_NETWORK_STATE},0);
        }
        status = ContextCompat.checkSelfPermission(activity, "com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY");
        if (status != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{"com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY"}, 0);
        }
    }

    public static boolean isStoragePermitted(Activity activity) {
        int status = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return status == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestStoragePermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},0);
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.MOUNT_FORMAT_FILESYSTEMS},0);
        ActivityCompat.requestPermissions(activity ,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
    }
}
