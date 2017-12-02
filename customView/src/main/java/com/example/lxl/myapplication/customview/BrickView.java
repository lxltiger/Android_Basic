package com.example.lxl.myapplication.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.lxl.myapplication.R;
import com.example.lxl.myapplication.utils.MeasureUtil;

/**
 * Created by Administrator on 2015/7/21.
 * Shader在三维软件中我们称之为着色器，是来给图像着色的或者更通俗的说法是上色
 * BitmapShader为其一子类，演示应用
 */
public class BrickView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private int screenWidth;
    private int screenHeight;
    //手指的坐标
    private float x;
    private float y;
    //画圆的笔
    private Paint mPaintCircle;
    public BrickView(Context context) {
        this(context,null);
    }

    public BrickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initResource(context);
    }
    private void initPaint() {
        // 实例化画笔
        mPaint = new Paint();
        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaintCircle.setColor(0xFF000000);
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setStrokeWidth(5);
    }

    private void initResource(Context context) {
         screenWidth = MeasureUtil.getScreenWidth((Activity) context);
         screenHeight = MeasureUtil.getScreenHeight((Activity) context);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.brick);
        //X轴和Y轴都重复
        mPaint.setShader(new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_MOVE){
            x = event.getX();
            y=event.getY();
            invalidate();
        }

        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.GRAY);
        //画墙
        canvas.drawCircle(x,y,100,mPaint);
        //画圆周
        canvas.drawCircle(x,y,100,mPaintCircle);
    }
}
