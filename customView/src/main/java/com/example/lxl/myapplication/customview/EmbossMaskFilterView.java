package com.example.lxl.myapplication.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import com.example.lxl.myapplication.utils.MeasureUtil;

/**
 * Created by Administrator on 2015/7/20.
 * 浮雕遮罩滤镜,制作巧克力块
 */
public class EmbossMaskFilterView extends View {
    private Paint mPaint;
    //x轴方向2块
    private int x_count =2;
    //y轴方向4块
    private int y_count =4;
    //各块巧克力的坐标
    private PointF[] mPoint=new PointF[x_count * y_count];
    //单个巧克力的Y坐标
    private float Y;

    int width;
    int height;

    public EmbossMaskFilterView(Context context) {
        this(context,null);
    }

    public EmbossMaskFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //EmbossMaskFilterView不支持硬件加速，对某个View关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF603811);
        //direction，是光照方向的意思，该数组必须要有而且只能有三个值即float[x,y,z]，这三个值代表了一个空间坐标系，
        //区别于直接照明的二次照明称为间接照明，产生的光线叫做环境光ambient，参数中的该值就是用来设置环境光的,在Android中环境光默认为白色，其值越大，阴影越浅
        //specular就是跟高光有关的，其值是个双向值越小或越大高光越强中间值则是最弱的
        //blurRadius则是设置图像究竟“凸”出多大距离
        mPaint.setMaskFilter(new EmbossMaskFilter(new float[]{1,1,1},0.1f,10f,20f));
        //单个巧克力的宽高
         width= MeasureUtil.getScreenWidth((Activity)context)/ x_count;
         height= MeasureUtil.getScreenHeight((Activity)context)/ y_count;
        for (int i = 0; i < x_count * y_count; i++) {
            //区分第一列和第二列
            if(i%2==0){
                Y=i*height/2f;
                mPoint[i]=new PointF(0,Y);
            }else{
                mPoint[i]=new PointF(width,Y);
            }
        }

    }

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < x_count * y_count; i++) {
            canvas.drawRect(mPoint[i].x,mPoint[i].y,mPoint[i].x+width,mPoint[i].y+height,mPaint);
        }
    }
}
