package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.lxl.myapplication.R;

import java.util.Random;


/**
 * TODO: document your custom view class.
 */
public class RandomView extends View {
    private static final String TAG = "RandomView";
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private Paint mPaint;
    //文字的边界
    private Rect mBound;
    public RandomView(Context context) {
        this(context, null);
    }

    public RandomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RandomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mExampleString=getExampleString();
                postInvalidate();
            }
        });
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RandomView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.RandomView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.RandomView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.RandomView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.RandomView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.RandomView_exampleDrawable);
//            mExampleDrawable.setCallback(this);
        }

        a.recycle();

//        // Set up a default TextPaint object
//        mTextPaint = new TextPaint();
//        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
//        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
//        invalidateTextPaintAndMeasurements();

        mPaint=new Paint();
        mPaint.setTextSize(mExampleDimension);
        mBound=new Rect();

        mPaint.getTextBounds(mExampleString,0,mExampleString.length(),mBound);

    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);
        Log.d(TAG, "mTextWidth:" + mTextWidth);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
        Log.d(TAG, "mTextHeight:" + mTextHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure() called with: widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");
            //获取宽高的测量模式和测量值
            int widthSize=MeasureSpec.getSize(widthMeasureSpec);
            int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        Log.d(TAG, "widthSize:" + widthSize);
        Log.d(TAG, "widthMode:" + widthMode);
            int heightSize=MeasureSpec.getSize(heightMeasureSpec);
            int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        Log.d(TAG, "heightSize:" + heightSize);
        Log.d(TAG, "heightMode:" + heightMode);
        //如果模式不是wrapContent,宽高就是测量值，如果是则需计算
            mPaint.setTextSize(mExampleDimension);
            mPaint.getTextBounds(mExampleString,0,mExampleString.length(),mBound);
        Log.d(TAG, "mBound:" + mBound);
            int width=mBound.width()+getPaddingLeft()+getPaddingRight();
            int height=mBound.height()+getPaddingTop()+getPaddingBottom();

        Log.d(TAG, "width:" + width);
        Log.d(TAG, "height:" + height);
            setMeasuredDimension(widthMode==MeasureSpec.EXACTLY?widthSize:width,
                    heightMode==MeasureSpec.EXACTLY?heightSize:height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        //画矩形边框
        Log.d(TAG, "getMeasuredHeight():" + getMeasuredHeight());
        Log.d(TAG, "getMeasuredWidth():" + getMeasuredWidth());
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        canvas.drawRect(mBound,mPaint);
        mPaint.setColor(mExampleColor);
        Log.d(TAG, "getWidth():" + getWidth());
        Log.d(TAG, "getHeight():" + getHeight());
        //x默认是mExampleString这个字符的左边在屏幕的位置，如果设置了paint.setTextAlign(Paint.Align.CENTER);那就是字符的中心，y是指定这个字符baseline在屏幕上的位置。
        canvas.drawText(mExampleString,getWidth()/2-mBound.width()/2,getHeight()/2+mBound.height()/2,mPaint);

    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        StringBuilder stringBuilder=new StringBuilder();
        Random random=new Random();
        for(int i=0;i<4;i++){
            int rand=random.nextInt(10);
            stringBuilder.append(rand);
        }
        return stringBuilder.toString();
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
