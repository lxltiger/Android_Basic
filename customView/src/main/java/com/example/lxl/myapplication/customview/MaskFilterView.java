package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.lxl.myapplication.R;

/**
 * Created by Administrator on 2015/7/20.
 * 遮罩过滤器
 */
public class MaskFilterView extends View {
    private Paint mPaint;
    //图片阴影画笔
    private Paint mPaintShadow;

    //源图
    private Bitmap mBitmap;
    private Bitmap mBitmapShadow;
    public MaskFilterView(Context context) {
        this(context,null);
    }

    public MaskFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setMaskFilter不支持硬件加速，对某个View关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF603811);
        // 设置画笔遮罩滤镜
        //上面我们用到的是SOLID，其效果就是在图像的Alpha边界外产生一层与Paint颜色一致的阴影效果而不影响图像本身，
        // 除了SOLID还有三种，NORMAL,OUTER和INNER,
        // NORMAL会将整个图像模糊掉：
        //OUTER会在Alpha边界外产生一层阴影且会将原本的图像变透明：
        //INNER则会在图像内部产生模糊：
        mPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));

        //BlurMaskFilter是根据Alpha通道的边界来计算模糊的，如果是一张图片没有任何效果
        //从Bitmap中获取其Alpha通道，并在绘制Bitmap前先以该Alpha通道绘制一个模糊效果
        mPaintShadow=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        mPaintShadow.setColor(Color.GRAY);
        mPaintShadow.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);
        mBitmapShadow = mBitmap.extractAlpha();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
//        canvas.drawColor(Color.GRAY);
        canvas.drawRect(100,100,500,500,mPaint);

        //绘制图片阴影
        canvas.drawBitmap(mBitmapShadow, 100, 600,mPaintShadow);
        canvas.drawBitmap(mBitmap, 100, 600,null);
    }
}
