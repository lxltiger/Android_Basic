package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2015/7/19.
 *  有关颜色过滤的3种方法
 */
public class CircleView extends View implements Runnable {
    private Paint mPaint;
    private int radius = 100;
    //Բ�����
    private int x;
    private int y;
    private boolean isClicked=false;
    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        //在我们绘制棱角分明的图像时，比如一个矩形、一张位图，我们不需要打开抗锯齿。
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        //当setStrokeWidth(0)的时候描边宽度并不为0而是只占一个像素
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.argb(225, 225, 128, 103));
        //使用色彩矩阵
        //第一行表示的R（红色）的向量，第二行表示的G（绿色）的向量，第三行表示的B（蓝色）的向量，最后一行表示A（透明度）的向量
        //这个矩阵不同的位置表示的RGBA值，其范围在0.0F至2.0F之间，1为保持原图的RGB值。每一行的第五列数字表示偏移值
       /* ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.5f, 0, 0, 0, 0,
                0, 0.5f, 0, 0, 0,
                0, 0, 0.5f, 0, 0,
                0, 0, 0, 1, 0,
        });*/
//        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        //使用光照色彩过滤
//        mPaint.setColorFilter(new LightingColorFilter(0xFFFF00FF, 0x00000000));
        //点击改变色彩的过滤
        /*setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClicked){
                    mPaint.setColorFilter(null);
                }else{
                    mPaint.setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0X00FFFF00));
                }
                isClicked=!isClicked;
                invalidate();
            }
        });*/
        //混合过滤
        mPaint.setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.DARKEN));
    }



    @Override
    public void draw(Canvas canvas) {
        x = canvas.getWidth() / 2;
        y = canvas.getHeight() / 2;
        canvas.drawCircle(x, y, radius, mPaint);
    }


    @Override
    public void run() {
        while (true) {
            if (radius <= 200) {
                radius += 10;
            } else {
                radius = 0;
            }
            postInvalidate();
            SystemClock.sleep(100);
        }
    }
}
