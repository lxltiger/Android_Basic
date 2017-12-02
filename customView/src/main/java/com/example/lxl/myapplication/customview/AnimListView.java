package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2015/7/22.
 * 未使用
 */
public class AnimListView extends ListView {

    private Matrix matrix;
    private Camera camera;
    public AnimListView(Context context) {
        this(context, null);
    }

    public AnimListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        matrix = new Matrix();
        camera = new Camera();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        camera.save();
      //  camera.rotate(30, 0, 0);
        camera.getMatrix(matrix);
        matrix.preTranslate(-getWidth() / 2, -getHeight() / 2);
        matrix.postTranslate(getWidth() / 2, getHeight() / 2);
        canvas.concat(matrix);
        super.onDraw(canvas);
        camera.restore();
    }
}
