package com.example.lxl.myapplication.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.lxl.myapplication.R;
import com.example.lxl.myapplication.utils.MeasureUtil;

/**
 * Created by Administrator on 2015/7/21.
 * 矩阵的使用
 * 图片的旋转缩放
 */
public class MatrixView extends ImageView {
    //当前矩阵
    private Matrix mMatrixCurrent;
    //前矩阵
    private Matrix mMatrixOld;
    private Bitmap mBitmap;
    private int screenWidth;
    private int screenHeight;
    //当前点
    private PointF mPointCurrent=new PointF();
    //两指间中点,确定缩放的参考点
    private PointF mPointMid=new PointF();
    public MatrixView(Context context) {
        this(context, null);
    }

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        screenWidth = MeasureUtil.getScreenWidth((Activity) context);
        screenHeight = MeasureUtil.getScreenHeight((Activity) context);
        mMatrixCurrent = new Matrix();
        //创建原图
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.a);
        Log.d("MatrixView", "sfsfsfdfds");
        mBitmap=Bitmap.createScaledBitmap(bitmap,screenWidth,screenHeight,true);
        setImageBitmap(mBitmap);
    }

    //定义手势模式，拖拽，
    private final static  int DRAG=0;
    //缩放，旋转
    private final static  int SCALE=1;
    //无手势
    private final static  int NONE=2;

    //默认拖拽
    private int mode=0;
    //两指间距离
    private float distance;
    //计算角度
    private float degree;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //多点触控既要动作类型也要索引信息
        switch (event.getAction()& MotionEvent.ACTION_MASK) {
            //一个手指
            case MotionEvent.ACTION_DOWN:
                //进入拖拽模式
                Log.d("MatrixView", "jdogjd");
                mode=DRAG;

                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                //计算两指间距离
                 distance = calculateSpace(event);
                Log.d("MatrixView", "distance:" + distance);
                if(distance>10){
                    //获取两指中点
                    setMid(event);
                    //进入 缩放或旋转模式
                    mode=SCALE;
                }

                break;
            //一指手指离开
            case MotionEvent.ACTION_UP:
                Log.d("MatrixView", "up");
                mode=NONE;
                break;
                //另一个也离开
            case MotionEvent.ACTION_POINTER_UP:
                Log.d("MatrixView", "ACTION_POINTER_UP");

                mode=DRAG;
                break;
            case MotionEvent.ACTION_MOVE:
                //平移
                if(mode==DRAG){
                    mMatrixCurrent.postTranslate(event.getX()-mPointCurrent.x,event.getY()-mPointCurrent.y);
                }else if(event.getPointerCount()==2&&mode==SCALE) {
                    //计算移动后两点的距离
                    float newDistance = calculateSpace(event);
                    //移动距离超过10
                    if(Math.abs(newDistance-distance)>10){
                        //计算缩放笔列
                        float scale=newDistance/distance;
                        Log.d("MatrixView", "scale:" + scale);
                        mMatrixCurrent.postScale(scale, scale, mPointMid.x, mPointMid.y);
                    }
                    //计算旋转角度
                    float degreeNew = calculateDegree(event);
                    //围绕屏幕中心旋转
                    mMatrixCurrent.postRotate(degreeNew - degree,screenWidth/2,screenHeight/2);

                }
                break;

        }
        if(event.getPointerCount()==2&&mode==SCALE) {
            distance = calculateSpace(event);
            //计算角度
            degree = calculateDegree(event);
        }
        mPointCurrent.set( event.getX(), event.getY());
        setImageMatrix(mMatrixCurrent);
        return true;

    }

    /*
    计算两指间的距离，用来缩放
     */
    private float calculateSpace(MotionEvent event){
        float x=event.getX(0)-event.getX(1);
        float y=event.getY(0)-event.getY(1);
        return (float) Math.sqrt(x*x+y*y);
    }

    /*
    计算两指构成直线的角度，用来旋转

     */
    private  float calculateDegree(MotionEvent event){
        float deltaX=event.getX(0)-event.getX(1);
        float deltaY=event.getY(0)-event.getY(1);
        //atan2(y,x)所表达的意思是坐标原点为起点，指向(x,y)的射线在坐标平面上与x轴正方向之间的角的角度。
//        ATAN2(a,b)的取值范围介于 -pi 到 pi 之间（不包括 -pi）
        //结果为正表示从 X 轴逆时针旋转的角度，结果为负表示从 X 轴顺时针旋转的角度。
        double rad = Math.atan2(deltaY, deltaX);
        Log.d("MatrixView", "rad:" + rad);
        float degree=(float) Math.toDegrees(rad);
        Log.d("MatrixView", "degree:" + degree);
        return degree;
    }

    /*
    计算两指的中间点
     */

    private void setMid(MotionEvent event){
        mPointMid.set((event.getX(0)+event.getX(1))/2,(event.getY(0)+event.getY(1))/2);
    }

}
