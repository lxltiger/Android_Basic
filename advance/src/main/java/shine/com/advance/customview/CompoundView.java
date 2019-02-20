package shine.com.advance.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import shine.com.advance.R;


/**
 * 图片和文字的混合控件
 * 图片在上 文字在下说明
 * 控件宽度谁长谁决定，高度共同决定
 * 图片有居中和拉伸两种方式
 */
public class CompoundView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;



    private Paint mPaint;
    private Rect mBound;
    //图片和文字的边框
    private Rect mRect;
    //图片
    private Bitmap mImage;
    //图片类型
    private int mImageScaleType;

    private int mWidth;
    private int mHeight;
    public CompoundView(Context context) {
        this(context, null);
    }

    public CompoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompoundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CompoundView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.CompoundView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.CompoundView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.CompoundView_exampleDimension,
                mExampleDimension);
        mImage= BitmapFactory.decodeResource(getResources(),a.getResourceId(R.styleable.CompoundView_image,0));
        mImageScaleType=a.getInt(R.styleable.CompoundView_imageScaleType,0);

        if (a.hasValue(R.styleable.CompoundView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.CompoundView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();


//
//        // Update TextPaint and text measurements from attributes
//        invalidateTextPaintAndMeasurements();
        mBound=new Rect();
        mRect=new Rect();
        mPaint=new Paint();
        mPaint.setTextSize(mExampleDimension);
        mPaint.getTextBounds(mExampleString,0,mExampleString.length(),mBound);


    }



    /*
     *主要计算在WrapContent时的参数
     * 测量控件的实际大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        //控件的实际宽高

        //如果是精确测量如MatchParent ，精确值
        if(widthMode==MeasureSpec.EXACTLY){
            mWidth=widthSize;
        }else{
            //如果是WrapContent 测量图片和文字的宽度
            int imageWidth=getPaddingLeft()+getPaddingRight()+mImage.getWidth();
            int textWidth=getPaddingLeft()+getPaddingRight()+mBound.width();
            //取较大值
            int expected=Math.max(imageWidth,textWidth);
            mWidth=Math.min(widthSize,expected);
        }
        if(heightMode==MeasureSpec.EXACTLY){
            mHeight=heightSize;
        }else{
            //如果是WrapContent 测量图片和文字的高度
            int expected=getPaddingBottom()+getPaddingTop()+mImage.getHeight()+mBound.height();
            mHeight=Math.min(heightSize,expected);
        }
        Log.e("test","mWidth"+mWidth+"mHeight"+mHeight);
        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        //绘制外边框
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        //设置文字和图片的边框
        mRect.left=getPaddingLeft();
        mRect.right=mWidth-getPaddingRight();
        mRect.top=getPaddingTop();
        mRect.bottom=mHeight-getPaddingBottom();

        mPaint.setColor(mExampleColor);
        mPaint.setStyle(Paint.Style.FILL);
        //如果字体宽度超出边界显示...
        if(mBound.width()>mWidth){
            TextPaint textPaint=new TextPaint(mPaint);
            String msg= TextUtils.ellipsize(mExampleString,textPaint,mWidth-getPaddingLeft()-
                    getPaddingRight(), TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg,getPaddingLeft(),mHeight-getPaddingBottom(),mPaint);
        }else{
            //绘制在屏幕中间
            canvas.drawText(mExampleString,mWidth/2-mBound.width()/2,mHeight-getPaddingBottom(),mPaint);
        }
        mRect.bottom-=mBound.height();
        if(mImageScaleType==1){
            mRect.left=mWidth/2-mImage.getWidth()/2;
            mRect.right=mWidth/2+mImage.getWidth()/2;
            mRect.top=mRect.bottom/2-mImage.getHeight()/2;
            mRect.bottom=mRect.bottom/2+mImage.getHeight()/2;

        }
        Log.e("test","left"+mRect.left+"right"+mRect.right+"top"+mRect.top+"bottom"+mRect.bottom);
        canvas.drawBitmap(mImage,null,mRect,mPaint);
    }


}
