package com.example.lxl.myapplication.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.lxl.myapplication.R;
import com.example.lxl.myapplication.utils.MeasureUtil;

/**
 * Created by Administrator on 2015/7/22.
 * BitmapMesh的演示
 */
public class BitmapMeshView2 extends View {
    //横纵方向的网格数
    private final static int xBlock = 9;
    private final static int yBlock = 9;
    //交点数
    private int count = (xBlock + 1) * (yBlock + 1);
    private float[] matrixMove;
    //横纵交点的原坐标
    //横纵交点的移动后坐标
    private float[] matrixOriginal;
    private Bitmap mBitmap;

    //分别绘制原点、移动后的点、两点的连线
    private Paint mPaintOriginal;
    private Paint mPaintMove;
    private Paint mPaintLine;

    float xBlcokWidth;
    float yBlcokHeight;
    public BitmapMeshView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        initPaint();
        int screenWidth= MeasureUtil.getScreenWidth((Activity) context);
        int screenheight= MeasureUtil.getScreenHeight((Activity)context);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.girl);
        mBitmap=Bitmap.createScaledBitmap(bitmap,screenWidth,screenheight,true);
        //奇数位存储x坐标，偶数位存储y坐标
        matrixOriginal = new float[count * 2];
        matrixMove = new float[count * 2];
        //获取图片宽高
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        //x方向小块的宽度
         xBlcokWidth = width / xBlock;
        //y方向小块的高度
         yBlcokHeight = height / yBlock;
        initXY();

    }

    private void initXY() {
        for (int i = 0; i <= yBlock; i++) {
            //y方向每点的高度
            float fy = yBlcokHeight * i;
            for (int j = 0; j <= xBlock; j++) {
                float fx = xBlcokWidth * j;
                //记录原坐标和移动后的坐标
                setXY(fx, fy, index, matrixMove);
                setXY(fx, fy, index, matrixOriginal);
                index++;
            }
        }

    }

    private void initPaint() {
        mPaintOriginal = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOriginal.setColor(0x660000FF);
        mPaintMove = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintMove.setColor(0x99FF0000);
        mPaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLine.setColor(0xFFFFFB00);
    }

    private int index = 0;

    private void setXY(float fx, float fy, int index, float[] array) {
        array[index * 2 + 0] = fx;
        array[index * 2 + 1] = fy;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmapMesh(mBitmap, xBlock, yBlock, matrixMove, 0, null, 0, null);
        drawGuide(canvas);
        System.arraycopy(matrixOriginal,0,matrixMove,0,matrixMove.length);
    }

    //手指触摸的坐标
    private float x_touch, y_touch;

    private void drawGuide(Canvas canvas) {
        //绘制原点
        for (int i = 0; i < count; i++) {
            float x_matrixOriginal = matrixOriginal[2 * i];
            float y_matrixOriginal = matrixOriginal[2 * i + 1];
            canvas.drawCircle(x_matrixOriginal, y_matrixOriginal, 4f, mPaintOriginal);

            float x_matrixMove = matrixMove[2 * i];
            float y_matrixMove = matrixMove[2 * i + 1];
            canvas.drawLine(x_matrixOriginal, y_matrixOriginal, x_matrixMove, y_matrixMove, mPaintOriginal);

//            float x_matrixMove = matrixMove[2 * i];
//            float y_matrixMove = matrixMove[2 * i + 1];
            canvas.drawCircle(x_matrixMove, y_matrixMove, 4f, mPaintMove);

        }
        for (int i = 0; i < count; i++) {
        }
        canvas.drawCircle(x_touch,y_touch,6f,mPaintLine);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x_touch = event.getX();
        y_touch = event.getY();
        smudge();
        invalidate();
        return true;
    }

    /*
    *设计点的偏移
    * 离触点越近偏移越大
     */

    private void smudge() {
        for (int i = 0; i < count; i++) {
            float x_matrixMove = matrixMove[2 * i];
            float y_matrixMove = matrixMove[2 * i + 1];
            //x和y方法的偏移
            float x_distance_deviation = x_touch - x_matrixMove;
            float y_distance_deviation = y_touch - y_matrixMove;
            float kv_kat = x_distance_deviation * x_distance_deviation + y_distance_deviation * y_distance_deviation;
            float pull = (float) (1000000 / kv_kat / Math.sqrt(kv_kat));

            if (pull >= 1) {
                matrixMove[2 * i] = x_touch;
                matrixMove[2 * i + 1] = y_touch;
            } else {
                matrixMove[2 * i] = x_matrixMove + x_distance_deviation * pull;
                matrixMove[2 * i + 1] = y_matrixMove + y_distance_deviation * pull;
            }

        }

    }
}
