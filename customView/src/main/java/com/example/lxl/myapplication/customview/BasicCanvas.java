package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class BasicCanvas extends View {
    private Paint mPaint;

    public BasicCanvas(Context context) {
        this(context, null);

    }

    public BasicCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(100, 100, 90, mPaint);
        RectF rectOne=new RectF(10,200,110,300);

        canvas.drawArc(rectOne,180,90,false,mPaint);
        rectOne.set(10, 300, 110, 400);

        canvas.drawArc(rectOne, 180, 180, true, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GREEN);
        rectOne.set(10, 400, 110, 500);
        canvas.drawArc(rectOne, 0, 360, false, mPaint);
        canvas.drawLine(10, 500, 110, 600, mPaint);
        rectOne.set(200, 10, 500, 210);
        canvas.drawOval(rectOne, mPaint);
        rectOne.set(200, 220, 400, 620);
        canvas.drawRect(rectOne, mPaint);
        rectOne.set(410, 220, 610, 620);
        canvas.drawRoundRect(rectOne,20,20,mPaint);
        Path path = new Path();
        path.moveTo(10,630);
        path.lineTo(40, 680);
        path.lineTo(300, 790);
        path.lineTo(10,630);
        canvas.drawPath(path,mPaint);

    }


}
