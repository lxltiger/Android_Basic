package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.example.lxl.myapplication.R;

/**
 * Created by Administrator on 2015/7/20.
 * //混合模式screen 效果的演示
 * 滤色产生的效果是Android提供的几个色彩混合模式中最好的，它可以让图像焦媃幻化，有一种色调均和的感觉
 */
public class ScreenView extends View {
    private Paint mPaint;
    //源图
    private Bitmap mBitmapScr;
    public ScreenView(Context context) {
        this(context,null);
    }

    public ScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmapScr = BitmapFactory.decodeResource(getResources(), R.drawable.a3);

    }

    @Override
    public void draw(Canvas canvas) {
        int x=canvas.getWidth()/2- mBitmapScr.getWidth()/2;
        int y=canvas.getHeight()/2- mBitmapScr.getHeight()/2;
        canvas.drawColor(Color.WHITE);
        //将绘制操作保存到新的图层
        int scr=canvas.saveLayer(0,0,canvas.getWidth(),canvas.getHeight(),null,Canvas.ALL_SAVE_FLAG);
        // 先绘制一层颜色
        canvas.drawColor(0xcc1c093e);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        canvas.drawBitmap(mBitmapScr, x, y, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(scr);

    }
}
