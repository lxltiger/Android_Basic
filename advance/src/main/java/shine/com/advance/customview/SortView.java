package shine.com.advance.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import shine.com.advance.R;

/**
 */
public class SortView extends View {


    private Paint paint;
    private int interval;

    public SortView(Context context) {
        super(context);
        init(null, 0);
    }

    public SortView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SortView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width_size = MeasureSpec.getSize(widthMeasureSpec);
//        int height_size = MeasureSpec.getSize(heightMeasureSpec);
//        setMeasuredDimension(width_size - getPaddingLeft() - getPaddingRight(), height_size);

    }

    int width;
    int height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        interval = (width - getPaddingLeft() - getPaddingRight()) / 9;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 10; i++) {
            int x = i * interval;
            canvas.drawLine(x + getPaddingLeft(), 0, x + getPaddingLeft(), (i + 1) * 5, paint);
        }

        canvas.save();

        for (int i = 0; i < 100; i++) {
            canvas.drawColor(Color.WHITE);
            canvas.drawLine(i + getPaddingLeft(), i, i + getPaddingLeft(), i+50, paint);
            SystemClock.sleep(200);
        }

        canvas.restore();
    }


}
