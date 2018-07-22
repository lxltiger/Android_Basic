package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.lxl.myapplication.R;


/**
 * Created by lixiaolin on 17/7/7.
 */

public class MeasureView extends View {
    private static final String TAG = "MeasureView";
    private Bitmap mBitmap;

    public MeasureView(Context context) {
        super(context);
        init();

    }

    public MeasureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeasureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

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



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap,0,0,null);
    }
}
