package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/8/15.
 */
public class DripView extends View {
    private static final String TAG = "DripView";
    //画圆过程的关键参数
    private float blackMagic = 0.551915024494f;
    private float keyC;
    private float step=1.5f;
    private float radius;
    private Path mPath;
    private Paint mPaint;
    private PointF mP0;
    private PointF mP1;
    private PointF mP2;
    private PointF mP3;
    private PointF mP4;
    private PointF mP5;
    private PointF mP6;
    private PointF mP7;
    private PointF mP8;
    private PointF mP9;
    private PointF mP10;
    private PointF mP11;

    public DripView(Context context) {
        this(context,null);
    }

    public DripView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DripView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius=w/2;
        Log.d(TAG, "radius:" + radius);
        //用于设定坐标
        keyC=radius*blackMagic;
        //第四象限的四分之一圆
        mP0 = new PointF(radius, 2*radius);
        mP1 = new PointF(radius+keyC, 2*radius);
        mP2 = new PointF(2*radius, radius+keyC);
        mP3 = new PointF(2*radius, radius);
        //第一象限的四分之一圆
        mP4 = new PointF(2*radius, radius-keyC);
        mP5 = new PointF(radius+keyC, 20);
        mP6 = new PointF(radius, 0);
        //第二象限的四分之一圆
        mP7 = new PointF(radius-keyC, 20);
        mP8 = new PointF(0, radius-keyC);
        mP9 = new PointF(0, radius);
        //第三象限的四分之一圆
        mP10 = new PointF(0, radius+keyC);
        mP11 = new PointF(radius-keyC, 2*radius);
    }

    private void init() {
        Log.d(TAG, "init");
        mPaint = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1f);
        mPaint.setColor(Color.BLUE);
    }

    private void drawDrip(Canvas canvas) {
        mPath.moveTo(mP0.x, mP0.y);
        mPath.cubicTo(mP1.x, mP1.y, mP2.x, mP2.y, mP3.x, mP3.y);
        mPath.cubicTo(mP4.x, mP4.y, mP5.x, mP5.y, mP6.x, mP6.y);
        mPath.cubicTo(mP7.x, mP7.y, mP8.x, mP8.y, mP9.x, mP9.y);
        mPath.cubicTo(mP10.x, mP10.y, mP11.x, mP11.y, mP0.x, mP0.y);
        canvas.drawPath(mPath,mPaint);
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
       mPath.reset();
        if (mP0.y>=2*radius) {
           mP0.y=radius+keyC;
           mP1.y=radius+keyC;
           mP11.y=radius+keyC;
            mP10.y=radius;
            mP2.y=radius;

        }else{
            mP0.y+=step;
            mP1.y+=step;
            mP11.y+=step;
            mP10.y+=step;
            mP2.y+=step;
        }
        drawDrip(canvas);
//        invalidate();
    }
}
