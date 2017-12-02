package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/10/25.
 */

public class VisitView extends View {

    private Paint mPaint;

    public VisitView(Context context) {
        this(context,null);
    }

    public VisitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setTextSize(40f);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


    }
}
