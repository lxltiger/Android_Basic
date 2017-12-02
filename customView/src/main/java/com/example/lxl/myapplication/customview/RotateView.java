package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.lxl.myapplication.R;

/**
 * Created by Administrator on 2015/6/27.
 */
public class RotateView extends View {

    private Paint paint;
    private float angle;

    public RotateView(Context context) {
        this(context, null);
    }

    public RotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }
    private void initPaint(){
        paint=new Paint();
        paint.setColor(getResources().getColor(R.color.colorPrimary));
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();
        canvas.rotate(angle, 150, 150);
        canvas.drawRect(100, 100, 200, 200, paint);
        canvas.restore();
        angle++;

        invalidate();
    }
}
