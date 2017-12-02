package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2015/7/18.
 */
public class AdvancedCanvas extends View {
    private Paint mPaint;
    public AdvancedCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(16);
        mPaint.setStrokeWidth(4);

    }

    public AdvancedCanvas(Context context) {
        this(context, null);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.translate(canvas.getWidth()/2,400);
        canvas.drawCircle(0, 0, 200, mPaint);
        canvas.save();
        canvas.translate(-150, -150);
        RectF rectF=new RectF(0,0,300,300);
        Path path=new Path();
        path.addArc(rectF, 180, 180);
        canvas.drawTextOnPath("hey,coder, shapen your skill,yeah yeah", path, 28, 0, mPaint);
        canvas.restore();
        Paint smallPaint=new Paint(mPaint);
        smallPaint.setStrokeWidth(1);
        float y=200;
        int count=60;
        for (int i = 0; i < count; i++) {
            if(i%5==0){
                canvas.drawLine(0,y,0,y+12,mPaint);
                canvas.drawText(String.valueOf(i/5+1),-4,y+25,smallPaint);
            }else{
                canvas.drawLine(0,y,0,y+5,smallPaint);
            }
            canvas.rotate(6);
        }
        smallPaint.setStrokeWidth(4);
        mPaint.setColor(Color.GRAY);
        canvas.drawCircle(0, 0,14, mPaint);
        smallPaint.setStyle(Paint.Style.FILL);
        smallPaint.setColor(Color.YELLOW);
        canvas.drawCircle(0,0, 10, smallPaint);
        canvas.drawLine(0,10,0,-120,smallPaint);

    }

}
