package shine.com.advance.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import shine.com.advance.R;
/*
* 自定义View
* 如何从自定义属性文件中获取属性
* 在OnMeasure正确处理AT_MOST测量参数
* 颜色过滤的三个实现类的简单应用，用于变换图像颜色
* */
public class CircleView extends View {
    private static final String TAG = "CircleView";
    private Paint paint;
    private int color_filter;
    private int color;
    private boolean flag = false;
    //使用色彩矩阵
    //第一行表示的R（红色）的向量，第二行表示的G（绿色）的向量，第三行表示的B（蓝色）的向量，最后一行表示A（透明度）的向量
    //这个矩阵不同的位置表示的RGBA值，其范围在0.0F至2.0F之间，1为保持原图的RGB值。每一行的第五列数字表示偏移值
    ColorMatrix colorMatrix = new ColorMatrix(new float[]{
            0.5f, 0, 0, 0, 0,
            0, 0.5f, 0, 0, 0,
            0, 0, 0.5f, 0, 0,
            0, 0, 0, 1, 0,
    });
    private ColorFilter matrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
    /*
    *LightingColorFilter (int mul, int add)
    *mul全称是colorMultiply意为色彩倍增，而add全称是colorAdd意为色彩添加，这两个值都是16进制的色彩值0xAARRGGBB
    * 当LightingColorFilter(0xFFFFFFFF, 0x00000000)的时候原图是不会有任何改变的，
    * 如果我们想增加红色的值，那么LightingColorFilter(0xFFFFFFFF, 0x00XX0000)就好，其中XX取值为00至FF
    * 0xFFFF00FF  会去掉绿色
    * */
    private ColorFilter lightingColorFilter = new LightingColorFilter(0xFFFF00FF, 0x00000000);
    /*
    * PorterDuffColorFilter(int color, PorterDuff.Mode mode)
    * 这个构造方法也接受两个值，一个是16进制表示的颜色值这个很好理解，而另一个是PorterDuff内部类Mode中的一个常量值，
    * 这个值表示混合模式,混合我们设置的color和我们画布上的元素，有很多种方式
    * */
    private ColorFilter porterDuffColorFilter = new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.DARKEN);

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
//        init(context);

    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        color = a.getColor(R.styleable.CircleView_circle_color, Color.RED);
        color_filter = a.getInt(R.styleable.CircleView_color_filter, -1);
        a.recycle();
        init();

    }


    private void init() {
        paint = new Paint();
        paint.setColor(color);
        switch (color_filter) {
            case 0:
                paint.setColorFilter(matrixColorFilter);
                break;
            case 1:
                //使用光照色彩过滤
                flag =true;
                paint.setColorFilter(lightingColorFilter);
                break;
            case 2:
                paint.setColorFilter(porterDuffColorFilter);
                break;
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    paint.setColorFilter(null);
                } else {
                    paint.setColorFilter(lightingColorFilter);
                }
                flag = !flag;
                invalidate();
            }
        });


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        String wm = MeasureSpec.toString(widthMeasureSpec);
//        String hm = MeasureSpec.toString(heightMeasureSpec);
//        Log.d(TAG, hm+" onMeasure: " + wm);
        int w_mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int h_mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int defaultSize = 200;
        if (w_mode == MeasureSpec.AT_MOST) {
            width = defaultSize;
        }
        if (h_mode == MeasureSpec.AT_MOST) {
            height = defaultSize;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int radius = Math.min(width, height) / 2;

        canvas.drawCircle(width / 2 + getPaddingLeft(), height / 2 + getPaddingTop(), radius, paint);
    }
}
