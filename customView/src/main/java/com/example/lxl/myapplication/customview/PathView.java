package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2015/7/23.
 * Path的应用
 */
public class PathView extends View {
    private Paint mPaint;
    private TextPaint textPaint;
    private Path mPath;
    private int width;
    private int height;
    private RectF rectF;
    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xFFA2D6AE);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);
        textPaint.setColor(Color.GRAY);
        rectF=new RectF(200,200,400,400);
    }



    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mPath.moveTo(100, 100);
        //forceMoveTo值为true时将会把弧的起点作为Path的起点
//        mPath.arcTo(rectF, 90, 180,true);
        mPath.addOval(rectF, Path.Direction.CW);
        canvas.drawPath(mPath,mPaint);
        canvas.drawTextOnPath("hfsdiuhhhhhhhhhhhhhhhhhhhhh",mPath,0,0,textPaint);



    }
}
