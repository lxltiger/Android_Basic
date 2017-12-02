package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author:
 * 时间:2017/9/28
 * qq:1220289215
 * 类描述：
 */

public class MarqueeTextView extends AppCompatTextView {
    private static final String TAG = "MarqueeTextView";

    private OverScroller mOverScroller;
    private boolean isStop=true;
    private List<String> contents=new ArrayList<>();
    private TextPaint mTextPaint;
    private int mScreenWidth;
    private int mDuration;
    private int mStartX=1200;

    public MarqueeTextView(Context context) {
        super(context);
        init(context);
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        mOverScroller=new OverScroller(context,new LinearInterpolator());
        contents.addAll(Arrays.asList("duasdsddddddng", "heifdfdddddddddddddddddddddddddhe", "test"));
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        float textSize = getTextSize();
        Log.d(TAG, "textSize:" + textSize);
        mTextPaint.setTextSize(textSize);
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        Log.d(TAG, "mScreenWidth:" + mScreenWidth);

    }


    int mIndex=0;
    public void startScroll() {
        isStop=false;
        mIndex = ++mIndex % 3;
        String content = contents.get(mIndex);
        setText(content);
        int measureText = (int) mTextPaint.measureText(content)+getPaddingLeft()+getPaddingRight();
        //设置布局参数，否则文字超过父控件文字不能显示
        FrameLayout.LayoutParams layoutParams=null;
        if (measureText > mScreenWidth) {
            layoutParams = new FrameLayout.LayoutParams(measureText, ViewGroup.LayoutParams.WRAP_CONTENT);
        }else{
            layoutParams=new FrameLayout.LayoutParams(mScreenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        layoutParams.gravity= Gravity.CENTER_VERTICAL;
        setLayoutParams(layoutParams);
        mDuration=(mStartX+measureText)*6;
        mOverScroller.startScroll(-mStartX, 0, mStartX+measureText, 0, mDuration);
//        mOverScroller.startScroll(-1200, 0, 600, 0, 5000);
        invalidate();
    }

    public void stopScroll() {
        isStop=true;
        mOverScroller.forceFinished(true);
    }
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (isStop) {
            return;
        }
        Log.d(TAG, "computeScroll() called");
        if (mOverScroller.computeScrollOffset()) {
            scrollTo(mOverScroller.getCurrX(), 0);
            invalidate();
        }else{
            isStop=true;
            startScroll();
            Log.d("MarqueeTextView", "finsih");
        }

    }
}
