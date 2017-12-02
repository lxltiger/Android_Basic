package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/7/19.
 */
public class ViewGroupOne extends ViewGroup {
    public ViewGroupOne(Context context) {
        this(context, null);
    }

    public ViewGroupOne(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取宽高的测量模式和值
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int childCount=getChildCount();
        //上边两个子控件的宽和高
        int topWidth=0;
        int topHeight=0;
        //下边两个控件的宽和高
        int bottomWidth=0;
        int bottomHeight=0;
        MarginLayoutParams layoutParams;
        for (int i = 0; i < childCount; i++) {
            View viewChild = getChildAt(i);
            layoutParams = (MarginLayoutParams) viewChild.getLayoutParams();
            if (i == 0 || i == 1) {
                topWidth+=viewChild.getWidth()+layoutParams.leftMargin+layoutParams.rightMargin;
            }
            if(i==2||i==3){
                bottomWidth+=viewChild.getWidth()+layoutParams.leftMargin+layoutParams.rightMargin;
            }
            if (i == 0 || i == 2) {
                topHeight+=viewChild.getHeight()+layoutParams.topMargin+layoutParams.bottomMargin;
            }
            if(i==1||i==3){
                bottomHeight+=viewChild.getHeight()+layoutParams.topMargin+layoutParams.bottomMargin;
            }
        }
        int width = Math.max(topWidth, bottomWidth);
        int height = Math.max(topHeight, bottomHeight);

        setMeasuredDimension(widthMode==MeasureSpec.EXACTLY?widthSize:width,
                heightMode==MeasureSpec.EXACTLY?heightSize:height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //获取子VIew的个数
        int childCount=getChildCount();
        int width=getMeasuredWidth();
        int height=getMeasuredHeight();
        Log.d("ViewGroupOne", "width:" + width);
        Log.d("ViewGroupOne", "height:" + height);
        MarginLayoutParams layoutParams=null;
        //子控件的宽高
        int cWidth=0,cHeight=0;
        //子控件的布局参数
        int cl=0,ct=0;
        int cr=0,cb=0;
        for (int i = 0; i <childCount; i++) {
            View childView = getChildAt(i);
            cWidth =childView.getMeasuredWidth();
            Log.d("ViewGroupOne", "cWidth:" + cWidth);
            cHeight = childView.getMeasuredHeight();
            Log.d("ViewGroupOne", "cHeight:" + cHeight);
            layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            switch (i) {
                //左上角
                case 0:
                    cl=layoutParams.leftMargin;
                    ct=layoutParams.topMargin;
                    break;
                //右上角
                case 1:
                    ct=layoutParams.topMargin;
                    cl=width-cWidth-layoutParams.rightMargin;
                    break;
                //左下角
                case 2:
                    cl=layoutParams.leftMargin;
                    ct=height-cHeight-layoutParams.bottomMargin;
                    break;
                //右下角
                case 3:
                    cl=width-cWidth-layoutParams.rightMargin;
                    ct=height-cHeight-layoutParams.bottomMargin;
                    break;
            }
            cr=cl+cWidth;
            cb=ct+cHeight;
            Log.d("ViewGroupOne", "cl:" + cl);
            Log.d("ViewGroupOne", "ct:" + ct);
            Log.d("ViewGroupOne", "cr:" + cr);
            Log.d("ViewGroupOne", "cb:" + cb);
            childView.layout(cl,ct,cr,cb);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
