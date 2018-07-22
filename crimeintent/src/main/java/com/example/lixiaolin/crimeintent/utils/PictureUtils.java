package com.example.lixiaolin.crimeintent.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

/**
 * Created by lixiaolin on 15/10/29.
 */
public class PictureUtils {

    /**
     *
     * @param filePath
     * @param screenWidth the width of the screen ,changes as rotate
     * @param screenHeight the height of the screen ,changes as rotate
     * @return
     */
    public static Bitmap getScaleBitmap(String filePath, int screenWidth, int screenHeight) {
        Log.d("pictureUtils", "screenWidth" + screenWidth);
        Log.d("pictureUtils", "screenHeight" + screenHeight);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        float width = options.outWidth;
        float height = options.outHeight;
        int sample = 1;
        if (width > screenWidth || height > screenHeight) {
            sample = width > height ? Math.round(height / screenHeight) : Math.round(width / screenWidth);
        }
        options.inSampleSize = sample;
        Log.d("pictureUtils", "sample" + sample);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }

    public static Bitmap getScaleBitmap(Activity activity, String path) {
        Log.d("pictureUtils", "path" + path);
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        return getScaleBitmap(path, point.x, point.y);
    }
}
