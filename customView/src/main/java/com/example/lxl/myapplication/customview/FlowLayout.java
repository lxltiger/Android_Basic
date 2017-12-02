package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: document your custom view class.
 */
public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取此ViewGroupd的宽高
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
//        Log.d("test", "sizeWidth"+sizeWidth+"sizeHeight"+sizeHeight);

        /*
        *判断宽高的模式是否为WrapContent，此时需要测量子控件的宽高
        * 父控件的宽由子控件最长的决定，高由子控件累加决定
         */
        //测量得到的宽高
        int width = 0;
        int height = 0;
        //流式布局一行的宽高
        int lineWidth = 0;
        int lineHeight = 0;
        //获取子空间个数
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //测量子控件
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //获取布局参数
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = lp.leftMargin + child.getMeasuredWidth() + lp.rightMargin;
            int childHeight = lp.topMargin + child.getMeasuredHeight() + lp.bottomMargin;
            if (lineWidth + childWidth > sizeWidth-getPaddingLeft()-getPaddingRight()) {
                //如果子控件累加的宽度超过父控件则换行，记录当前父控件最大行宽,
                width = Math.max(width, lineWidth);
                //累加行高
                height += lineHeight;
                //重置行宽,行高
                lineWidth = childWidth;
                lineHeight = childHeight;
            } else {
                //没有换行 累加行宽
                lineWidth += childWidth;
                //记录最大行高
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //如果是最后一个控件
            if (i == childCount - 1) {
                //比较宽度
                width = Math.max(width, lineWidth);
                //累加高度
                height += lineHeight;
            }
        }

        setMeasuredDimension(
                modeWidth == MeasureSpec.AT_MOST ? width+getPaddingLeft()+getPaddingRight() : sizeWidth,
                modeHeight == MeasureSpec.AT_MOST ? height+getPaddingTop()+getPaddingBottom() : sizeHeight);

    }



        //所有View的集合，以行为单位
       private List<List<View>> mViews=new ArrayList<List<View>>();
        //行高
       private List<Integer> mLineHeight=new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
            //设置子控件的布局
        mViews.clear();
        mLineHeight.clear();
        //父控件宽度，已测量
        int width=getWidth();
        //行宽、高
        int lineWidth=0;
        int lineHeight=0;
        int childCount=getChildCount();
        //一行的View集合
        List<View> lineViews=new ArrayList<View>();
        //按行把子控件分类
        for (int i = 0; i < childCount; i++) {
            View child=getChildAt(i);
            MarginLayoutParams  marginLayoutParams= (MarginLayoutParams) child.getLayoutParams();
            int childWidth=marginLayoutParams.leftMargin+child.getMeasuredWidth()+marginLayoutParams.rightMargin;
            int childHeight=marginLayoutParams.topMargin+child.getMeasuredHeight()+marginLayoutParams.bottomMargin;
            if(lineWidth+childWidth>width-getPaddingLeft()-getPaddingRight()){
                //超出一行
                mViews.add(lineViews);
                mLineHeight.add(lineHeight);
                lineWidth=0;
                lineHeight=childHeight;
                lineViews=new ArrayList<View>();

            }
                lineViews.add(child);
                lineWidth+=childWidth;
                lineHeight=Math.max(lineHeight,childHeight);
        }
        //最后一行
        mViews.add(lineViews);
        mLineHeight.add(lineHeight);
        //遍历设置布局
        //行数
        int lineNum=mViews.size();
        int left=getPaddingLeft();
        int top=getPaddingTop();
        for(int i=0;i<lineNum;i++){
            lineViews=mViews.get(i);
            int line_Height=mLineHeight.get(i);
            for (int j=0;j<lineViews.size();j++){
                View child=lineViews.get(j);
                if(child.getVisibility()==View.GONE){
                    continue;
                }
                MarginLayoutParams marginLayoutParams= (MarginLayoutParams) child.getLayoutParams();
                int cl=left+marginLayoutParams.leftMargin;
                int cr=cl+child.getMeasuredWidth();
                int ct=top+marginLayoutParams.topMargin;
                int cb=ct+child.getMeasuredHeight();
                child.layout(cl,ct,cr,cb);
                left+=marginLayoutParams.leftMargin+child.getMeasuredWidth()+marginLayoutParams.rightMargin;
            }
                left=getPaddingLeft();
                top+=line_Height;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

    /*
    *与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
