package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lixiaolin on 17/7/6.
 * 对视图的测量
 */

public class MeasureViewGroup extends ViewGroup {
    private static final String TAG = "MeasureViewActivity";

    public MeasureViewGroup(Context context) {
        super(context);
    }

    public MeasureViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, Integer.toBinaryString(widthMeasureSpec));
        Log.d(TAG, Integer.toBinaryString(heightMeasureSpec));

        int mode_width = MeasureSpec.getMode(widthMeasureSpec);
        int size_width = MeasureSpec.getSize(widthMeasureSpec);

        int mode_height = MeasureSpec.getMode(heightMeasureSpec);
        int size_height = MeasureSpec.getSize(heightMeasureSpec);

        switch (mode_width) {
            case MeasureSpec.EXACTLY:
                Log.d(TAG, "mode_width exactly");
                break;
            case MeasureSpec.AT_MOST:
                Log.d(TAG, "mode_width at most");

                break;
            case MeasureSpec.UNSPECIFIED:
                Log.d(TAG, "mode_width unspecified");
                break;
        }
        switch (mode_height) {
            case MeasureSpec.EXACTLY:
                Log.d(TAG, "mode_height exactly");
                break;
            case MeasureSpec.AT_MOST:
                Log.d(TAG, "mode_height at most");

                break;
            case MeasureSpec.UNSPECIFIED:
                Log.d(TAG, "mode_height unspecified");
                break;
        }


        Log.d(TAG, "size_width:" + size_width);
        Log.d(TAG, "size_height:" + size_height);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);

            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout() called with: changed = [" + changed + "], l = [" + l + "], t = [" + t + "], r = [" + r + "], b = [" + b + "]");
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                int measuredHeight = view.getMeasuredHeight();
                int measuredWidth = view.getMeasuredWidth();
                view.layout(l, t, measuredWidth, measuredHeight);
            }
        }
    }
}
