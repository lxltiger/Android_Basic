package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2015/7/23.
 * 贝塞尔曲线的应用
 */
public class WaveView extends View {
    private Paint mPaint;
    private Path mPath;
    private int width;
    private int height;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFFA2D6AE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
        height=h;

        y_wave=height/8;
        y_control=-height/16;
    }
    //左右端点的高度
    private float y_wave;
    //控制点的坐标
    private float x_control;
    private float y_control;
    private boolean isIncrease=false;
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mPath.moveTo(-width / 4, y_wave);
        mPath.quadTo(x_control, y_control, width / 4 + width, y_wave);
        mPath.lineTo(width, height);
        mPath.lineTo(0, height);
        mPath.close();
        canvas.drawPath(mPath,mPaint);

        if(x_control<=-width/4){
            isIncrease=true;
        }else if(x_control>=width+width/4){
            isIncrease=false;
        }

        x_control=isIncrease?x_control+20:x_control-20;
        if (y_wave <= height) {
            y_wave+=2;
            y_control+=2;
        }
        mPath.reset();
        invalidate();

    }
}
