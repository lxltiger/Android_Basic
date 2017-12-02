package com.example.lxl.myapplication.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.lxl.myapplication.R;
import com.example.lxl.myapplication.utils.MeasureUtil;

/**
 * Created by Administrator on 2015/7/21.
 * Shader的子类  线性渐变
 *
 */
public class LinearGradientView extends View {
    private Paint mPaint;
    private int screenWidth;
    private int screenHeight;
    //原图
    private Bitmap mBitmapScr;
    //倒影图
    private Bitmap mBitmapRev;
    //原图顶点
    private int x;
    private int y=50;

    public LinearGradientView(Context context) {
        this(context, null);
    }

    public LinearGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initResource(context);
    }
    private void initPaint() {
        // 实例化画笔
        mPaint = new Paint();
//        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
//        mPaintCircle.setColor(0xFF000000);
//        mPaintCircle.setStyle(Paint.Style.STROKE);
//        mPaintCircle.setStrokeWidth(5);
    }

    private void initResource(Context context) {
         screenWidth = MeasureUtil.getScreenWidth((Activity) context);
         screenHeight = MeasureUtil.getScreenHeight((Activity) context);
        //简单渐变
//        mPaint.setShader(new LinearGradient(0, 0, screenWidth/2, screenHeight/2, Color.YELLOW, Color.RED, Shader.TileMode.REPEAT));
        //复杂渐变positions表示的是渐变的相对区域，其取值只有0到1;positions可以为空,为空均匀分布颜色
//        mPaint.setShader(new LinearGradient(0, 0, screenWidth, screenHeight, new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE},
//                new float[]{0,0.1f,0.3f,0.5f,0.6f},Shader.TileMode.REPEAT));

            //应用----------------------------------
        //原图
        mBitmapScr= BitmapFactory.decodeResource(getResources(), R.drawable.james);
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        //实例化倒影
        mBitmapRev = Bitmap.createBitmap(mBitmapScr,0, 0, mBitmapScr.getWidth(), mBitmapScr.getHeight(), matrix, true);
        //设置画笔的渐变,在原图下方1/4高出
        //设置原图图片
         x=screenWidth/2-mBitmapScr.getWidth()/2;
        mPaint.setShader(new LinearGradient(x,y+mBitmapScr.getHeight(),x,y+mBitmapScr.getHeight()+mBitmapScr.getHeight()/4,
                0xAA000000, Color.TRANSPARENT, Shader.TileMode.CLAMP));
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(mBitmapScr, x, y, null);

        int sc = canvas.saveLayer(x, y + mBitmapScr.getHeight(), x + mBitmapScr.getWidth(), y + mBitmapScr.getHeight() * 2, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(mBitmapRev, x, y+ mBitmapScr.getHeight(), null);
        canvas.drawRect(x, y + mBitmapScr.getHeight(), x + mBitmapScr.getWidth(), y + mBitmapScr.getHeight() * 2, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);
    }
}
