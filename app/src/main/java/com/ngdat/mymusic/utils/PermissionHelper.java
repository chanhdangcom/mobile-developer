package com.ngdat.mymusic.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHelper {

    private static final int DEFAULT_REQUEST_CODE = 123;
    private static final int NOTIFICATION_REQUEST_CODE = 124; // Mã yêu cầu cho quyền thông báo

    private static String getAudioPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.READ_MEDIA_AUDIO
                : Manifest.permission.READ_EXTERNAL_STORAGE;
    }

    private static String getNotificationPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.POST_NOTIFICATIONS
                : null;
    }

    public static void checkAndRequestPermissions(Activity activity) {
        checkAndRequestPermissions(activity, DEFAULT_REQUEST_CODE);
    }

    public static void checkAndRequestPermissions(Activity activity, int requestCode) {
        // Kiểm tra quyền audio
        if (!hasPermissions(activity)) {
            requestPermission(activity, requestCode);
        }

        // Kiểm tra quyền thông báo cho Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission(activity)) {
            requestNotificationPermission(activity);
        }
    }

    public static boolean hasPermissions(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, getAudioPermission())
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasNotificationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(activity, getNotificationPermission())
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true; // Không cần kiểm tra quyền thông báo cho Android < 13
    }

    public static void requestPermission(Activity activity) {
        requestPermission(activity, DEFAULT_REQUEST_CODE);
    }

    public static void requestPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{getAudioPermission()},
                requestCode);
    }

    public static void requestNotificationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{getNotificationPermission()},
                    NOTIFICATION_REQUEST_CODE);
        }
    }
}
