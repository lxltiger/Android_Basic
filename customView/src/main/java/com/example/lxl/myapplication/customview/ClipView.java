package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2015/7/24.
 * 图形的剪裁
 */
public class ClipView extends View{
    private Paint mPaint;
    private Path mPath;
    public ClipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(2);
        mPath = new Path();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.BLUE);
       /* mPath.moveTo(50, 50);
        mPath.lineTo(75, 23);
        mPath.lineTo(150, 100);
        mPath.lineTo(80, 110);
        mPath.close();*/
        mPath.addCircle(100,100,100,Path.Direction.CCW);
//        canvas.drawPath(mPath,mPaint);
        canvas.clipPath(mPath);
        canvas.drawColor(Color.RED);
    }
}
