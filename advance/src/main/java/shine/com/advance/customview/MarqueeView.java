package shine.com.advance.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;

public class MarqueeView extends View {
    private static final String TAG = "MarqueeView";
    private  String content = "";
    private int width;
    private int height;
    private TextPaint paint;
    private Paint.FontMetrics fontMetrics;
    private OverScroller scroller;

    public MarqueeView(Context context) {
        this(context,null);
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Log.d(TAG, "init: ");
        scroller = new OverScroller(context,new LinearInterpolator());
        paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(30);
        fontMetrics = paint.getFontMetrics();
        height = (int) (fontMetrics.bottom - fontMetrics.top);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: ");
        setMeasuredDimension(300, height);
    }

    public void setText(String content) {
        Log.d(TAG, "setText: ");
        this.content=content;
        width = (int) paint.measureText(content);
        scroller.startScroll(-100, 0, 100+width, 0, 12000);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: ");
        canvas.drawText(content,0,-fontMetrics.top,paint);
    }

    @Override
    public void computeScroll() {
        Log.d(TAG, "computeScroll: ");
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(),0);
            invalidate();
        }
    }
}
