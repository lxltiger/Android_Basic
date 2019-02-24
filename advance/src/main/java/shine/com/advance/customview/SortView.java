package shine.com.advance.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Random;

import shine.com.advance.algor.BubbleSort;

/**
 */
public class SortView extends View {
    private static final String TAG = "SortView";

    private Paint paint;
    private float interval;
    private int STAGE_ONE = 1000;
    private int STAGE_TWO = 2000;
    private int STAGE_THREE = 3000;
    private long times;
    private AnimationListener animationListener;

    int width;
    int height;
    List<RectF> rects = new ArrayList<>(20);
    private Random random = new Random();
    long last = 0;
    private int[] numbers;
    private Queue<Point> command;
    private Point pair;
    private boolean start;
    private TextPaint textPaint;


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
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(30);
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        numbers = new int[10];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(200);
        }

        command = BubbleSort.formSortCommand(numbers);


    }

    public void setAnimationListener(AnimationListener animationListener) {
        this.animationListener = animationListener;
    }


    public void start() {
        start = true;
        times = AnimationUtils.currentAnimationTimeMillis();
        last = AnimationUtils.currentAnimationTimeMillis();
        pair = command.poll();
        if (pair != null) {
            invalidate();
        } else {
            start = false;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width_size = MeasureSpec.getSize(widthMeasureSpec);
//        int height_size = MeasureSpec.getSize(heightMeasureSpec);
//        setMeasuredDimension(width_size - getPaddingLeft() - getPaddingRight(), height_size);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        interval = (width - getPaddingLeft() - getPaddingRight()) / 10f;
        int N = numbers.length;
        for (int i = 0; i < N; i++) {
            float left = i * interval + getPaddingLeft();
            int bottom = 200;
            int top = bottom - numbers[i];
            float right = left + 24;
            RectF rect = new RectF(left, top, right, bottom);
            rects.add(rect);
//            Log.d(TAG, "onSizeChanged: " + rect.toString());
        }
    }

    private String text = "";

    private void update() {
        int first = pair.x;
        int second = pair.y;


        long current = AnimationUtils.currentAnimationTimeMillis();
        long stage = current - times;
        long draw_time_consume = current - last;
        last = current;
        int margin = second - first;
        float dx = (draw_time_consume * interval * margin) / STAGE_ONE;
        float dy = (draw_time_consume * 200f / STAGE_ONE);

        if (numbers[first] < numbers[second]) {
            if (stage < 1000) {
                text = String.format(Locale.CHINA, "%d 小于 %d 不需交换", numbers[first], numbers[second]);
                invalidate();
            } else {
                start();
            }
        } else {
            text = String.format(Locale.CHINA, "%d 交换 %d", numbers[first], numbers[second]);
            if (stage <= STAGE_ONE) {
                RectF point = rects.get(first);
                point.offset(dx / 2, dy);
                invalidate();
            } else if (stage <= STAGE_TWO) {
                RectF point = rects.get(second);
                point.offset(-dx, 0);
                invalidate();
            } else if (stage < STAGE_THREE) {
                RectF point = rects.get(first);
                point.offset(dx / 2, -dy);
                invalidate();
            } else {
                Collections.swap(rects, first, second);
                swap(numbers, first, second);
                start();
            }
        }


    }

    private void swap(int[] numbers, int i, int j) {
        int temp = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = temp;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        for (RectF rect : rects) {
            canvas.drawRect(rect, paint);
        }


        if (start) {
            float startX=pair.x*interval+getPaddingLeft()+12;
            float stopx=pair.y*interval+getPaddingLeft()+12;
            canvas.drawLine(startX, 200, startX, 220, paint);
            canvas.drawLine(startX,220,stopx,220,paint);
            canvas.drawLine(stopx,200,stopx,220,paint);
            canvas.drawText(text, (startX+stopx)/2, 225, textPaint);

            update();
        }


    }

    public interface AnimationListener {
        void onAnimationEnd();
    }


}
