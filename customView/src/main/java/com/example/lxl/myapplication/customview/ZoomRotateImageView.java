package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

/**
 * Created by lixiaolin on 17/7/24.
 */

public class ZoomRotateImageView extends ImageView {
    private static final String TAG = "ZoomRotateImageView";
    private Matrix mMatrix;
    private int pivotX;
    private int pivotY;
    private ScaleGestureDetector mDetector;
    private int mLastAngel;

    public ZoomRotateImageView(Context context) {
        super(context);
        init(context);
    }

    public ZoomRotateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        mDetector = new ScaleGestureDetector(context, mListener);
    }

    ScaleGestureDetector.SimpleOnScaleGestureListener mListener=new ScaleGestureDetector.SimpleOnScaleGestureListener(){
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            mMatrix.postScale(scaleFactor,scaleFactor,pivotX,pivotY);
            setImageMatrix(mMatrix);
            return true;
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent() called with: event = [" + event + "]");
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }

        switch (event.getPointerCount()) {
            case 3:
                mDetector.onTouchEvent(event);
                break;
            case 2:
                handleRotate(event);
                break;
        }

        return super.onTouchEvent(event);
    }

    private void handleRotate(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        float deltaX = event.getX(0) - event.getX(1);
        float deltaY= event.getY(0) - event.getY(1);
        Log.d(TAG, "deltaX:" + deltaX);
        Log.d(TAG, "deltaY:" + deltaY);
        double rad = Math.atan(deltaY / deltaX);
        double temo = Math.tan(deltaY / deltaX);
        Log.d(TAG, "rad:" + rad);

        int angel = (int) (rad*180/ Math.PI);
        Log.d(TAG, "angel:" + angel);


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                mLastAngel=angel;
                break;
            case MotionEvent.ACTION_MOVE:
                int delta=angel-mLastAngel;
                if (delta > 90 || delta < -90) {
                    Log.e(TAG, "delta:" + delta);
                }else{
                    Log.d(TAG, "delta:" + delta);
                }
                if (delta > 90) {
                    mMatrix.postRotate(-5, pivotX, pivotY);
                } else if (delta < -90) {
                    mMatrix.postRotate(5, pivotX, pivotY);
                }else {
                    mMatrix.postRotate(angel - mLastAngel, pivotX, pivotY);
                }
                setImageMatrix(mMatrix);
                mLastAngel=angel;
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged() called with: w = [" + w + "], h = [" + h + "], oldw = [" + oldw + "], oldh = [" + oldh + "]");
        if (w != oldw || h != oldh) {
            int intrinsicWidth = getDrawable().getIntrinsicWidth();
            int intrinsicHeight = getDrawable().getIntrinsicHeight();
            Log.d(TAG, "intrinsicWidth:" + intrinsicWidth);
            Log.d(TAG, "intrinsicHeight:" + intrinsicHeight);
            int translateX=(w-intrinsicWidth)/2;
            int translateY=(h-intrinsicHeight)/2;
            Log.d(TAG, "translateX:" + translateX);
            Log.d(TAG, "translateY:" + translateY);
            mMatrix.setTranslate(translateX,translateY);
            setImageMatrix(mMatrix);
            pivotX=w/2;
            pivotY=h/2;

        }

    }
}
