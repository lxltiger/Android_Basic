package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.UNSPECIFIED;
import static android.view.View.MeasureSpec.getSize;

/**
 * Created by 李晓林 on 2016/11/24
 * qq:1220289215
 */

public class IconView extends View {
    private static final String TAG = "IconView";
    private Bitmap mBitmap;
    private TextPaint mPaint;
    private String text = "sfhdffoshdfidsf";
    public IconView(Context context) {
        super(context);
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int width = getSize(widthMeasureSpec);
        Log.d(TAG, "modeWidth:" + modeWidth);
        Log.d(TAG, "width:" + width);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "modeHeight:" + modeHeight);
        Log.d(TAG, "height:" + height);
        Log.d(TAG, "mBitmap.getWidth():" + mBitmap.getWidth());
        Log.d(TAG, "mBitmap.getHeight():" + mBitmap.getHeight());
        int resultWidth=0;

        switch (modeWidth) {
            case EXACTLY:
                resultWidth=width;
                break;
            case UNSPECIFIED:
            case AT_MOST:
                float text = mPaint.measureText(this.text);
                resultWidth = Math.max((int)text, mBitmap.getWidth());
                resultWidth = Math.min(resultWidth+getPaddingLeft()+getPaddingRight(), width);
                break;
        }
        int resultHeight=0;
        switch (modeHeight) {
            case EXACTLY:
                resultHeight=height;
                break;
            case UNSPECIFIED:
            case AT_MOST:
                Log.d(TAG, "mPaint.descent():" + mPaint.descent());
                Log.d(TAG, "mPaint.ascent():" + mPaint.ascent());
                int  temp = (int) (2*(mPaint.descent() - mPaint.ascent()));
                resultHeight = Math.min(temp+mBitmap.getHeight()+getPaddingTop()+getPaddingBottom(), height);
                break;
        }
        Log.d(TAG, "resultHeight:" + resultHeight);
        Log.d(TAG, "resultWidth:" + resultWidth);
        setMeasuredDimension(resultWidth,resultHeight);
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap=bitmap;
    }

    private void init() {
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setTextSize(25f);
        mPaint.setColor(Color.GRAY);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap,getPaddingLeft(),getPaddingTop(),null);
        canvas.drawText(text,getWidth()/2, mBitmap.getHeight() + getHeight() / 2 - mBitmap.getHeight() / 2 - mPaint.ascent(),mPaint);
    }
}
