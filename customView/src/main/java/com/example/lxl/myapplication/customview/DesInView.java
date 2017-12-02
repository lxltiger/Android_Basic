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
 * PorterBuffer DesIn模式的应用
 * 只在源图像和目标图像相交的地方绘制目标图像？
 */
public class DesInView extends View {
    private Paint mPaint;
    private PorterDuffXfermode porterDuffXfermode;
    private Bitmap mBitmapDes;
    private Bitmap mBitmapScr;

    public DesInView(Context context) {
        this(context, null);
    }

    public DesInView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        mBitmapDes = BitmapFactory.decodeResource(getResources(), R.drawable.a3);
        mBitmapScr = BitmapFactory.decodeResource(getResources(), R.drawable.a3_mask);
    }

    @Override
    public void draw(Canvas canvas) {

        int x=canvas.getWidth()/2- mBitmapDes.getWidth()/2;
        int y=canvas.getHeight()/2- mBitmapDes.getHeight()/2;

        //如果不绘制背景会呈黑色
        canvas.drawColor(Color.WHITE);
        //将绘制操作保存到新的图层
        int scr=canvas.saveLayer(0,0,canvas.getWidth(),canvas.getHeight(),null,Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(mBitmapDes, x, y, mPaint);
        //先绘制目标图
        mPaint.setXfermode(porterDuffXfermode);
        // 再绘制src源图
        canvas.drawBitmap(mBitmapScr, x, y, mPaint);
        //还原模式
        mPaint.setXfermode(null);
        canvas.restoreToCount(scr);

    }
}
