package com.example.lxl.myapplication.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.lxl.myapplication.R;
import com.example.lxl.myapplication.utils.MeasureUtil;

/**
 * Created by Administrator on 2015/7/20.
 * 橡皮檫效果，我们可以通过手指不断地触摸屏幕绘制Path，再以Path作遮罩遮掉填充的色块显示下层的图像：
 *
 */
public class EraserView extends View {
    private Paint mPaint;
    //背景图
    private Bitmap mBitmapBg;
    //前景图
    private Bitmap mBitmapFg;
    //绘制橡皮才路径的画布
    private Canvas mCanvas;
    private Path mPath;
    public EraserView(Context context) {
        this(context, null);
    }

    public EraserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);


    }



    private void init(Context context) {
        //获取控件宽高
        int width = MeasureUtil.getScreenWidth((Activity)context);
        int height = MeasureUtil.getScreenHeight((Activity)context);
        mPath = new Path();
        // 实例化画笔并开启其抗锯齿和抗抖动
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        //// 设置画笔透明度为0是关键！我们要让绘制的路径是透明的，然后让该路径与前景的底色混合“抠”出绘制路径
        mPaint.setARGB(128, 255, 0, 0);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setStyle(Paint.Style.STROKE);
        //设置路径结合处样式
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //设置笔触类型
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(50);
        //创建前景图
        mBitmapFg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //注入画布
        mCanvas = new Canvas(mBitmapFg);
        // 绘制画布背景为中性灰
        mCanvas.drawColor(0xFF808080);
        //创建背景图
        mBitmapBg = BitmapFactory.decodeResource(getResources(), R.drawable.a4);
        //缩放背景图至屏幕大小
        mBitmapBg = Bitmap.createScaledBitmap(mBitmapBg, width, height, true);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(mBitmapBg,0,0,null);
        canvas.drawBitmap(mBitmapFg,0,0,null);
        /*
        当我们在屏幕上移动手指绘制路径时会把路径通过mCanvas绘制到fgBitmap上
         * 每当我们手指移动一次均会将路径mPath作为目标图像绘制到mCanvas上，而在上面我们先在mCanvas上绘制了中性灰色
         * 两者会因为DST_IN模式的计算只显示中性灰，但是因为mPath的透明，计算生成的混合图像也会是透明的
         * 所以我们会得到“橡皮擦”的效果
         */
        mCanvas.drawPath(mPath, mPaint);
    }

    private float preX;
    private float preY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取当前点
        float x=event.getX();
        float y=event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(x, y);
//                preX = x;
//                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.quadTo(preX, preY, x, y);

                break;
        }
        preX = x;
        preY = y;
        invalidate();
        return true;
    }
}
