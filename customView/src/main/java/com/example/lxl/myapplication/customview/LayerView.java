package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.lxl.myapplication.R;

/**
 * Created by Administrator on 2015/7/24.
 * 图层的使用
 */
public class LayerView extends View{
    private Paint mPaint;
    private Path mPath;
    private Bitmap bitmap;
    private Matrix matrix;
    public LayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //关闭硬件加速
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        initPaint();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a3);
        matrix = new Matrix();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(2);
        mPath = new Path();

    }

    int width;
    int height;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width=w/2;
        height=h/2;
        bitmap=Bitmap.createScaledBitmap(bitmap,w,h,true);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();
      //  canvas.scale(0.8f, 0.6f,width*2,0);
        //表示横纵向的错切比率
        matrix.postScale(0.5f, 0.5f);
        canvas.setMatrix(matrix);
        canvas.skew(0.5f,0);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.restore();
    }

    private void demo1(Canvas canvas){
        canvas.drawRect(200, 200, 400, 400, mPaint);
        //save()方法则是在当前的Bitmap中进行操作，并且只能针对Bitmap的形变和裁剪进行操作
//        canvas.save();
        //saveLayerXXX方法会将所有的操作存到一个新的Bitmap中而不影响当前Canvas的Bitmap
//        canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        //可只对我们需要的区域设层
        canvas.saveLayer(200, 200, 400, 400, null, Canvas.ALL_SAVE_FLAG);

        canvas.translate(300, 300);
        canvas.rotate(45);

        mPaint.setColor(Color.GRAY);
        canvas.drawRect(-50, -50, 50, 50, mPaint);
        canvas.restore();


    }

    private void demo2(Canvas canvas){
        RectF rect=new RectF(width-100,height-100,width+100,height+100);
        canvas.drawRect(rect, mPaint);
//        int id1 = canvas.save(Canvas.CLIP_SAVE_FLAG);
        int id1 = canvas.saveLayer(rect, null, Canvas.CLIP_SAVE_FLAG);
//        canvas.clipRect(rect);
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(rect, mPaint);

        int id2 = canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(5);
        canvas.drawRect(rect, mPaint);
        //使用的是save方法，id1恢复到第一次剪裁前的状态，如果是id2恢复到剪裁后的状态，后面的操作将限制在剪裁区

        canvas.restoreToCount(id2);
        mPaint.setColor(Color.YELLOW);
        rect.set(width - 200, height - 200, width + 200, height + 200);
        canvas.drawRect(rect,mPaint);
    }
    private void demo3(Canvas canvas){
        RectF rect=new RectF(width-200,height-200,width+200,height+200);
        int id1 = canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(rect);
        canvas.drawColor(Color.GREEN);
        rect.set(width - 100, height - 100, width + 100, height + 100);
        int id2 = canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(rect);
        canvas.drawColor(Color.BLUE);
        canvas.restoreToCount(id1);
        int id3=canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(10);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(rect, mPaint);
    }

}
