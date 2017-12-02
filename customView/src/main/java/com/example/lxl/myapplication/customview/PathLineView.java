package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Administrator on 2015/7/23.
 * Path的应用 折线图的制作
 */
public class PathLineView extends View {
    private Paint mPaint;
    private TextPaint textPaint;
    private Path mPath;
    private int width;
    private int height;
    private Canvas mCanvas;
    public PathLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(6);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);

        mCanvas = new Canvas();
        initData();
    }

    private List<PointF> mPoints = new ArrayList<PointF>();
    //x轴和y轴的刻度
    private String[] x_vector = new String[9];
    private String[] y_vector = new String[9];
    //最大值
    private float x_max;
    private float y_max;

    private void initData() {
        Random random = new Random();
        PointF pointF = null;
        for (int i = 0; i < 9; i++) {
            pointF=new PointF(random.nextInt(100),random.nextInt(100));

            mPoints.add(pointF);
        }
        for (int i = 0; i < 9; i++) {
            if(mPoints.get(i).x>x_max){
                x_max=mPoints.get(i).x;
            }
            if(mPoints.get(i).y>y_max){
                y_max=mPoints.get(i).y;
            }


        }
        //计算x和y轴的刻度阶梯,保留两位小数
//        float x_average=Float.valueOf(String.format(Locale.CHINA, "%.1f", (x_max + 10) / 9));
//        float y_average=Float.valueOf(String.format(Locale.CHINA,"%.1f",(y_max+10)/9));
        String x_average=String.format(Locale.CHINA, "%.1f", (x_max + 10) / 9);
        String y_average=String.format(Locale.CHINA, "%.1f", (y_max + 10) / 9);
        BigDecimal x_bigDecimal=new BigDecimal(x_average);
        BigDecimal y_bigDecimal=new BigDecimal(y_average);

        for (int i = 0; i <9; i++) {
            BigDecimal multiply=new BigDecimal(i+1+"");
            x_vector[i]=x_bigDecimal.multiply(multiply).toString();
            y_vector[i]=y_bigDecimal.multiply(multiply).toString();
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //高与宽同等
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //获取控件宽高
        width = w;
        height = h;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(0xFF9596C4);
        mPath.moveTo(width / 8f, width / 8f);
        mPath.lineTo(width / 8f, width * 3 / 4f);
        mPath.lineTo(width * 3 / 4f, width * 3 / 4f);
        canvas.drawPath(mPath, mPaint);

        mPaint.setStrokeWidth(2);
        //绘制网格和x轴刻度
        for (int i = 1; i < 10; i++) {
            float fx=width / 8f + width * i / 16f;
            float fy=width * 3 / 4f;
            mPath.moveTo(fx, fy);
            mPath.rLineTo(0, -9 * width / 16f);
            canvas.drawPath(mPath, mPaint);
            canvas.drawText(x_vector[i-1],fx,fy+20,textPaint);

        }
        //绘制网格和y轴刻度
        for (int i = 1; i < 10; i++) {
            float fx=width / 8f;
            float fy=width*3 / 4f-width * i / 16f;
            mPath.moveTo(fx, fy);
            mPath.rLineTo(9 * width / 16f, 0);
            canvas.drawPath(mPath, mPaint);
            canvas.drawText(y_vector[i - 1], fx-30, fy+10 , textPaint);
        }

        drawPolyLine(canvas);

    }

    /*
    *绘制折线图
     */
    private void drawPolyLine(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap( width*9 / 16,width*9 / 16, Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(bitmap);
        // 为画布填充一个半透明的红色
        mCanvas.drawARGB(75, 255, 0, 0);
        mPath.reset();
        mPath.moveTo(0,width*9 / 16);
        float fx=0f;
        float fy=0f;
         for (int i = 0; i <mPoints.size(); i++) {
            fx=mPoints.get(i).x/x_max*bitmap.getWidth();
             fy=bitmap.getHeight()-mPoints.get(i).y/y_max*bitmap.getHeight();
             mCanvas.drawCircle(fx,fy,4 ,mPaint);
             mPath.lineTo(fx,fy);
        }
        mCanvas.drawPath(mPath,mPaint);
        canvas.drawBitmap(bitmap,width / 8f, width*3 / 16f,null);
    }
}
