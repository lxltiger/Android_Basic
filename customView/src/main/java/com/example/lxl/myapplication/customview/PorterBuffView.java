package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.lxl.myapplication.other.PorterDuffBO;

/**
 * Created by Administrator on 2015/6/27.
 * 演示图片的混合
 */
public class PorterBuffView extends View{

    private Paint mPaint;
    private PorterDuffXfermode porterDuffXfermode;
    private PorterDuffBO porterDuffBO;// PorterDuffView类的业务对象
    private int small=200;
    private int big=400;

    public PorterBuffView(Context context) {
        this(context,null);
    }

    public PorterBuffView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //实例化业务逻辑类
        porterDuffBO = new PorterDuffBO();
        //实例化混合模式,两个图像混合，较深的颜色总是会覆盖较浅的颜色，如果两者深浅相同则混合
//        porterDuffXfermode=new PorterDuffXfermode(PorterDuff.Mode.DARKEN);
        //只绘制目标图像
//        porterDuffXfermode=new PorterDuffXfermode(PorterDuff.Mode.DST);
        //在源图像和目标图像相交的地方绘制目标图像而在不相交的地方绘制源图像
//        porterDuffXfermode=new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP);
        //只在源图像和目标图像相交的地方绘制目标图像
        porterDuffXfermode=new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Log.d("PorterBuffView", "width:" + width);
        Log.d("PorterBuffView", "height:" + height);
        //第一个小图的坐标
        int x1=0;
        int y1=0;
        //第二个小图坐标
        int x2 = width - small;
        int y2=0;
        //大图坐标
        int x3=width/2-big/2;
        int y3=height/2-big/2;
        //设置尺寸
        porterDuffBO.setSize(small);
        //绘制左上角正方形
        canvas.drawBitmap(porterDuffBO.initSrcBitmap(),x1,y1,mPaint);
        //绘制右上角正方形
        canvas.drawBitmap(porterDuffBO.initDisBitmap(), x2, y2, mPaint);
        //离屏缓存
        int sr = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
        porterDuffBO.setSize(big);
        //先绘制目标图
        canvas.drawBitmap(porterDuffBO.initDisBitmap(), x3, y3, mPaint);
        //设置混合模式
        mPaint.setXfermode(porterDuffXfermode);
        canvas.drawBitmap(porterDuffBO.initSrcBitmap(), x3, y3, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(sr);
    }
}
