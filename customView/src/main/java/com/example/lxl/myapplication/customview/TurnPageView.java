package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/27.
 * 图片翻页的演示
 */
public class TurnPageView extends View {

    private List<Bitmap> mBitmaps = null;
    //控件的宽和高
    private int width;
    private int height;
    //手指移动的横坐标
    private float x_clip;
    //左右自动回收的距离,设置为在距边界1/5处放手后自动回收
    private float left_auto_roll;
    private float right_auto_roll;

    public TurnPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("TurnPageView", "constructor");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d("TurnPageView", "onsizechagnge");
        width = w;
        height = h;
        x_clip = width;
        left_auto_roll = w / 5f;
        right_auto_roll = w * 4 / 5f;
        initBitmap();

    }

    private void initBitmap() {
        if (mBitmaps != null) {
            List<Bitmap> bitmapsTemp = new ArrayList<>();
            for (int i = mBitmaps.size() - 1; i >= 0; i--) {
                Bitmap bitmap = Bitmap.createScaledBitmap(mBitmaps.get(i), width, height, true);
                bitmapsTemp.add(bitmap);
            }
            mBitmaps = bitmapsTemp;
        }
    }

    float currentPos;
    boolean isNext=false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isNext=true;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            //如果落在回滚区域
            case MotionEvent.ACTION_DOWN:
                x_clip = event.getX();
                if(x_clip<left_auto_roll){
                    pageIndex--;
                    isNext=false;
                    invalidate();
                }

                break;
           case MotionEvent.ACTION_MOVE:
               x_clip = event.getX();
               invalidate();
               break;
            case MotionEvent.ACTION_UP:

              judge();
                if(isNext&&x_clip==0){
                    pageIndex++;
                    x_clip=width;
                    invalidate();
                }
                break;

        }

        return true;
    }

    private void judge(){
        if (x_clip < left_auto_roll) {
            x_clip = 0;
            invalidate();
        }
        if (x_clip > right_auto_roll) {
            x_clip=width;

            invalidate();
        }
    }

    //每次只加载两张,记第一张的索引为0
    private int pageIndex=0;
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.d("TurnPageView", "draw");
        if (mBitmaps == null) {
            Log.d("TurnPageView", "nothing");
            return;
        }
        Log.d("TurnPageView", "pageIndex:" + pageIndex);
        int start= mBitmaps.size()-2-pageIndex;
        int end=0;
        if(start>=0){
            end=start+1;
        } else{
            start=0;
            end=0;
        }
        if(start>mBitmaps.size()-2){
            start=mBitmaps.size()-2;
            end=start+1;
        }
        Log.d("TurnPageView", "start:" + start);
        Log.d("TurnPageView", "end:" + end);
        for (int i = start; i <=end; i++) {
            canvas.save();
            if (i==end&&end!=0) {
                canvas.clipRect(0, 0, x_clip, height);
            }
            canvas.drawBitmap(mBitmaps.get(i), 0, 0, null);
            canvas.restore();
        }
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        if (null != bitmaps && bitmaps.size() > 0) {
            mBitmaps = bitmaps;
            // invalidate();
        } else {

            Log.d("TurnPageView", "legal");
        }
    }


}
