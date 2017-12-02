package com.example.lxl.myapplication.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.lxl.myapplication.R;
import com.example.lxl.myapplication.utils.MeasureUtil;

/**
 * Created by Administrator on 2015/7/21.
 * Shader在三维软件中我们称之为着色器，是来给图像着色的或者更通俗的说法是上色
 * BitmapShader为其一子类
 */
public class BitmapShaderView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private int screenWidth;
    private int screenHeight;
    private Rect rect=new Rect(100,100,600,800);
    public BitmapShaderView(Context context) {
        this(context,null);
    }

    public BitmapShaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initResource(context);
    }
    private void initPaint() {
        // 实例化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    private void initResource(Context context) {
         screenWidth = MeasureUtil.getScreenWidth((Activity) context);
         screenHeight = MeasureUtil.getScreenHeight((Activity) context);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);
        Matrix matrix = new Matrix();
        //set方法会重置前面的转换操作
        matrix.setTranslate(200, 200);
        //顺时针旋转10度
        matrix.preRotate(10);
        //X轴和Y轴都重复
        BitmapShader shader=new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        shader.setLocalMatrix(matrix);
        //X轴和Y轴都边缘拉伸
//        mPaint.setShader(new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        //X轴和Y轴都镜像
//        mPaint.setShader(new BitmapShader(mBitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR));
        //X轴拉伸Y轴镜像，BitmapShader是先应用了Y轴的模式而X轴是后应用的
//        mPaint.setShader(new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.MIRROR));

        mPaint.setShader(shader);
    }



    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(rect, mPaint);
    }
}
