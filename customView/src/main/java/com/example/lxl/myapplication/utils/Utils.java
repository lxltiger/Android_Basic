package com.example.lxl.myapplication.utils;

import android.util.Log;
import android.view.View;

/**
 * Created by lixiaolin on 17/7/27.
 */

public class Utils {
    private static final String TAG = "Utils";
    private Utils(){}
    public static void showMeasureSpec(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, Integer.toBinaryString(widthMeasureSpec));
        Log.d(TAG, Integer.toBinaryString(heightMeasureSpec));

        int mode_width = View.MeasureSpec.getMode(widthMeasureSpec);
        int size_width = View.MeasureSpec.getSize(widthMeasureSpec);

        int mode_height = View.MeasureSpec.getMode(heightMeasureSpec);
        int size_height = View.MeasureSpec.getSize(heightMeasureSpec);

        switch (mode_width) {
            case View.MeasureSpec.EXACTLY:
                Log.d(TAG, "mode_width exactly");
                break;
            case View.MeasureSpec.AT_MOST:
                Log.d(TAG, "mode_width at most");

                break;
            case View.MeasureSpec.UNSPECIFIED:
                Log.d(TAG, "mode_width unspecified");
                break;
        }

        Log.d(TAG, "size_width:" + size_width);

        switch (mode_height) {
            case View.MeasureSpec.EXACTLY:
                Log.d(TAG, "mode_height exactly");
                break;
            case View.MeasureSpec.AT_MOST:
                Log.d(TAG, "mode_height at most");

                break;
            case View.MeasureSpec.UNSPECIFIED:
                Log.d(TAG, "mode_height unspecified");
                break;
        }


        Log.d(TAG, "size_height:" + size_height);

    }
}
