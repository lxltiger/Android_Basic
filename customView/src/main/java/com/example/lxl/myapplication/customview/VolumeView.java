package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2015/7/19.
 */
public class VolumeView extends View {
    private Paint mPaint;
    private RectF mRectF;
    public VolumeView(Context context) {
        this(context, null);
    }

    public VolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(8);

    }
    //当前音量
    int mCurrentVolume =10;
    @Override
    public void draw(Canvas canvas) {
        mPaint.setColor(Color.LTGRAY);
        mRectF=new RectF(10,10,canvas.getWidth()-10,canvas.getHeight()-10);
        //分成32份，一半作为间隙
        int volume=36;
        float length=360/volume;
        for (int i = 0; i <volume+1; i++) {
            canvas.drawArc(mRectF,i*length,length/2,false,mPaint);
        }
        mPaint.setColor(Color.BLUE);
        for (int i = 0; i < mCurrentVolume; i++) {
            canvas.drawArc(mRectF,i*length,length/2,false,mPaint);
        }

    }
    float up;
    float down;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取Y点坐标

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                down=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                up=event.getY();
                if(up>down){
                    if(mCurrentVolume >0)
                        mCurrentVolume--;
                }else{
                    if(mCurrentVolume <36)
                        mCurrentVolume++;
                }
                down=up;
                postInvalidate();
                break;
        }
//        y=event.getY();
        return true;
    }
}
