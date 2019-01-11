package shine.com.advance.customview;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Locale;

/*
 * 字体相关的视图
 * 详细了解字体参数的意义
 * MaskFilter的使用
 * */
public class FontView extends View {
    private static final String TAG = "FontView";
    private static final String TEXT = "ap爱哥ξτβбпшㄎㄊěǔぬも┰┠№＠↓";
    private Paint paint;
    private Paint.FontMetrics fontMetrics;
    private float width;
    private BlurMaskFilter blurMaskFilter;


    public FontView(Context context) {
        this(context, null);
    }

    public FontView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //setMaskFilter不支持硬件加速，对某个View关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setTextSize(28);
        fontMetrics = paint.getFontMetrics();
        printFontMetrics(fontMetrics);
        Rect mBound = new Rect();
        //测量包裹文本的最小边界，测量的标准是以baseline为起点的ø
        paint.getTextBounds(TEXT, 0, TEXT.length(), mBound);
        width = paint.measureText(TEXT);

        /*
         *设置画笔遮罩滤镜
         *SOLID其效果就是在图像的Alpha边界外产生一层与Paint颜色一致的阴影效果而不影响图像本身，
         *NORMAL会将整个图像模糊掉：
         *OUTER会在Alpha边界外产生一层阴影且会将原本的图像变透明：
         *INNER则会在图像内部产生模糊：
         * */
        blurMaskFilter = new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID);
    }



    //文本的绘制是从Baseline开始,FontMetrics的这些值和要绘制什么文本是无关的，而仅与绘制文本Paint的size和typeface有关
    private void printFontMetrics(Paint.FontMetrics fontMetrics) {
        String desription = String.format(Locale.CHINA,
                "top=%f,ascent=%f,bottom=%f,descent=%f,leading=%f",
                fontMetrics.top,
                fontMetrics.ascent,
                fontMetrics.bottom,
                fontMetrics.descent,
                fontMetrics.leading);
        Log.d(TAG, "printFontMetrics: " + desription);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 这一段绘制有浮动效果的阴影
        paint.setColor(0xFF603811);
        paint.setStyle(Paint.Style.FILL);
        paint.setMaskFilter(blurMaskFilter);
        canvas.drawRect(0, 0, width, 100, paint);
        paint.setMaskFilter(null);

//        绘制文字
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(Color.BLACK);
        canvas.drawText(TEXT, 0, -fontMetrics.top, paint);

    }
}
