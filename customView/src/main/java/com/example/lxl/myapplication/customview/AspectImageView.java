package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.lxl.myapplication.utils.Utils;


/**
 * Created by lixiaolin on 17/7/27.
 * 宽高成比列的图片
 */

public class AspectImageView extends ImageView {
    private static final String TAG = "AspectImageView";
    public AspectImageView(Context context) {
        super(context);
    }

    public AspectImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Utils.showMeasureSpec(widthMeasureSpec,heightMeasureSpec);
        Drawable drawable=getDrawable();
        int desireWidth=0;
        float aspect;
        if (drawable == null) {
            aspect=1;
        }else{
            desireWidth=drawable.getIntrinsicWidth();
            aspect=(float) desireWidth/drawable.getIntrinsicHeight();
        }
        Log.d(TAG, "aspect:" + aspect);
        Log.d(TAG, "desireWidth:" + desireWidth);

        int width = View.resolveSize(desireWidth, widthMeasureSpec);
        Log.d(TAG, "width:" + width);
        int height= (int) (width/aspect);
        Log.d(TAG, "height:" + height);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
            int heightMeasure = MeasureSpec.getSize(heightMeasureSpec);

            if (height > heightMeasure) {
                height=heightMeasure;
                width= (int) (height*aspect);
            }
        }

        Log.d(TAG, "last width:" + width);
        Log.d(TAG, "last height:" + height);

        setMeasuredDimension(width,height);

    }
}
