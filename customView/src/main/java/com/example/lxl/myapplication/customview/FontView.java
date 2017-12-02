package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static android.R.attr.flipInterval;
import static android.R.attr.y;

/**
 * Created by Administrator on 2015/7/20.
 * 文本绘制具体见text_draw.jpeg
 * ---------------------------------------- top
 *      ---------------------------assent
 * &_____this 10:3  a pen grit     ____baseline &文字绘制的起点
 *      ------------------------------descent
 * ------------------------------------------bottom
 */
public class FontView extends View {
    private static final String TEXT = "ap爱哥ξτβбпшㄎㄊěǔぬも┰┠№＠↓";
    private static final String TEXT2 = "This is used by widgets to control text layout. You should not need to use this class directly unless you " +
            "are implementing your own widget or custom display object, or would be tempted to call Canvas.drawText() directly";
    private Paint mPaint;
    private Paint mLinePaint;
    private Paint.FontMetrics fontMetrics;

    private TextPaint textPaint;
    private StaticLayout staticLayout;
    private Rect mBound;

    public FontView(Context context) {
        this(context,null);
    }

    public FontView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
       mPaint.setTextSize(40);
        mPaint.setColor(Color.BLACK);

        mBound = new Rect();
        //测量包裹文本的最小边界，测量的标准是以baseline为起点的，所以接的top是负值，left,bottom是小正值
        mPaint.getTextBounds(TEXT,0,TEXT.length(), mBound);

        //文本的绘制是从Baseline开始,FontMetrics的这些值和要绘制什么文本是无关的，而仅与绘制文本Paint的size和typeface有关
        fontMetrics=mPaint.getFontMetrics();

        Log.d("FontView", "fontMetrics.top:" + fontMetrics.top);
        Log.d("FontView", "fontMetrics.ascent:" + fontMetrics.ascent);
        Log.d("FontView", "fontMetrics.bottom:" + fontMetrics.bottom);
        Log.d("FontView", "fontMetrics.descent:" + fontMetrics.descent);
        Log.d("FontView", "fontMetrics.leading:" + fontMetrics.leading);
        //用于在屏幕中央画一条基准线
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(1);
        mLinePaint.setColor(Color.RED);
        //专门处理文字的画笔，可换行
        textPaint = new TextPaint(mPaint);
        // 获取字体并设置画笔字体
//        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "kt.ttf");
//        textPaint.setTypeface(typeface);
        // 设置画笔文本倾斜
        textPaint.setTextSkewX(-0.25F);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //将文字绘制在屏幕左上方
        canvas.drawText(TEXT,-mBound.left,-mBound.top,mPaint);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.drawLine(0,height/2,width,height/2,mLinePaint);
        //绘制在屏幕中央
        float x=width/2-mPaint.measureText(TEXT)/2;
        float y=height/2-(fontMetrics.ascent+fontMetrics.descent)/2;

        canvas.drawText(TEXT, x, y, mPaint);

//        staticLayout = new StaticLayout(TEXT2,textPaint,width, Layout.Alignment.ALIGN_NORMAL,1.0f,0f,false);
//        staticLayout.draw(canvas);
        canvas.restore();
    }
}
