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
 * //混合模式des-out 效果的演示
 * 只在源图像和目标图像不相交的地方绘制目标图像
 */
public class DesOutView extends View {
    private Paint mPaint;
    //源图
    private Bitmap mBitmapScr;
    public DesOutView(Context context) {
        this(context,null);
    }

    public DesOutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmapScr = BitmapFactory.decodeResource(getResources(), R.drawable.a3_mask);

    }

    @Override
    public void draw(Canvas canvas) {
        int x=canvas.getWidth()/2- mBitmapScr.getWidth()/2;
        int y=canvas.getHeight()/2- mBitmapScr.getHeight()/2;
        canvas.drawColor(Color.WHITE);
        //将绘制操作保存到新的图层
        int scr=canvas.saveLayer(0,0,canvas.getWidth(),canvas.getHeight(),null,Canvas.ALL_SAVE_FLAG);
        // 先绘制一层颜色
        canvas.drawColor(0xFF8f66DA);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawBitmap(mBitmapScr, x, y, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(scr);

    }
}
