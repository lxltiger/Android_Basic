package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

/**
 * Created by lixiaolin on 17/8/6.
 * 正常的图像有一定的宽高比，如果不协调会变形，所以需要根据图像尺寸来设置预览比列
 */

public class AutoFitTextureView extends TextureView {
    private static final String TAG = "AutoFitTextureView";
    private int mWidth=0;
    private int mHeight=0;

    public AutoFitTextureView(Context context) {
        super(context);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //设置视图的宽高比
    public void setRatio(int width, int height) {
        Log.d(TAG, "setRatio() called with: width = [" + width + "], height = [" + height + "]");
        if (width <= 0 || height <= 0) {
            throw new RuntimeException("wrong parameters");
        }

        mWidth = width;
        mHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "width:" + width);
        Log.d(TAG, "height:" + height);

        if (mWidth == 0 || mHeight == 0) {
            setMeasuredDimension(width,height);
        }else{
            if (width < height * mWidth / mHeight) {
                setMeasuredDimension(width, width * mHeight / mWidth);
            }else{
                setMeasuredDimension(height*mWidth/mHeight,height);
            }
        }
    }
}
