package com.recorder.api;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.recorder.api.*;

public class ImageRecordManager {
    private static final String PREFS_NAME = "ImageRecordPrefs";
    private static final String KEY_IMAGE_PATHS = "saved_image_paths";

    public String imagePath=Environment.getExternalStorageDirectory()+"/DCIM/Screenshots";

    private Context context;
    private SharedPreferences sharedPreferences;

    public ImageRecordManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // 检查并请求权限

        if (!PermissionUtils.hasStoragePermission((Activity) context)) {
            PermissionUtils.requestStoragePermission((Activity) context);
        }


    }

    // 保存当前图片列表
    public void saveCurrentImagePaths() {
        Set<String> currentImagePaths = getCurrentImagePaths();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_IMAGE_PATHS, currentImagePaths);
        editor.apply();
    }

    // 获取上次保存的图片列表
    public Set<String> getLastSavedImagePaths() {
        return sharedPreferences.getStringSet(KEY_IMAGE_PATHS, new HashSet<>());
    }
    // 获取当前设备中的所有图片路径
    /*
    public Set<String> getCurrentImagePaths() {
        Set<String> imagePaths = new HashSet<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            while (cursor.moveToNext()) {
                String imagePath = cursor.getString(dataIndex);
                imagePaths.add(imagePath);
            }
            cursor.close();
        }
        return imagePaths;
    }
     */
    public Set<String> getCurrentImagePaths() {
        Set<String> screenshotPaths = new HashSet<>();
        File screenshotDir = new File(imagePath);

        if (screenshotDir.exists() && screenshotDir.isDirectory()) {
            File[] files = screenshotDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))) {
                        screenshotPaths.add(file.getAbsolutePath());
                    }
                }
            }
            else {
                Log.d("my", "null");
            }
        } else {
            Log.d("my", "Screenshot directory does not exist or is not a directory.");
        }
        return screenshotPaths;
    }
    //获取新增图片列表
    public List<String> getNewImagePaths() {
        Set<String> lastSavedImagePaths = getLastSavedImagePaths();
        Set<String> currentImagePaths = getCurrentImagePaths();
        List<String> newImages = new ArrayList<>(currentImagePaths);
        newImages.removeAll(lastSavedImagePaths);
        return newImages;
    }

}
