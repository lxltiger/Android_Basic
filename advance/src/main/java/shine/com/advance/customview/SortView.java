package shine.com.advance.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.lang.ref.WeakReference;
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
    private static final int DURATION = 3000;

    private Paint paint;
    private float interval;

    int width;
    int height;
    List<RectF> rects = new ArrayList<>(20);
    private Random random = new Random();
    private int[] numbers;
    private Queue<Point> command;
    private TextPaint textPaint;

    private ScheduleHandler handler;
    private TextHolder textHolder;

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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        handler = new ScheduleHandler(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (handler != null) {
            handler.getLooper().quit();
            handler=null;

        }
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
        textHolder = TextHolder.of();
    }

    private static class ScheduleHandler extends Handler {
        private static final int UPDATE = 1;
        private WeakReference<SortView> reference;

        private ScheduleHandler(SortView sortView) {
            this.reference = new WeakReference<>(sortView);

        }

        @Override
        public void handleMessage(Message msg) {

            SortView sortView = reference.get();
            if (sortView == null) {
                return;
            }
            switch (msg.what) {
                case UPDATE:
                    sortView.fire();
                    break;
            }
        }
    }



    public void fire() {
        final Point point = command.poll();
        if (point != null) {
            final int pos_first = point.x;
            final int pos_second = point.y;
            float startX = pos_first * interval + getPaddingLeft() + 12;
            float stopX = pos_second * interval + getPaddingLeft() + 12;

            if (numbers[pos_first] > numbers[pos_second]) {
                String text = String.format(Locale.CHINA, "%d交换%d", numbers[pos_first], numbers[pos_second]);
                textHolder.update((int)startX,(int)stopX,text);
                update(pos_first, pos_second);
            } else {
                String text = String.format(Locale.CHINA, "%d小于%d不需交换", numbers[pos_first], numbers[pos_second]);
                textHolder.update((int)startX,(int)stopX,text);
                invalidate();
                handler.sendEmptyMessageDelayed(ScheduleHandler.UPDATE, 1500);
            }
        }else{
            textHolder.update(0, 0, "");
            invalidate();
        }
    }

    private void update(int pos_first, int pos_second) {
        float distance = (pos_second - pos_first) * interval;
        RectF first = rects.get(pos_first);
        RectF second = rects.get(pos_second);
        Keyframe keyframe1 = Keyframe.ofFloat(0, first.left);
        Keyframe keyframe3 = Keyframe.ofFloat(0.3f, first.left + distance / 2);

        Keyframe keyframe2 = Keyframe.ofFloat(0, first.bottom);
        Keyframe keyframe4 = Keyframe.ofFloat(0.3f, first.bottom + 200);

        Keyframe keyframe5 = Keyframe.ofFloat(0.4f, second.left);
        Keyframe keyframe6 = Keyframe.ofFloat(0.6f, second.left - distance);

        Keyframe keyframe7 = Keyframe.ofFloat(0.7f, first.left + distance / 2);
        Keyframe keyframe9 = Keyframe.ofFloat(1, first.left + distance);

        Keyframe keyframe8 = Keyframe.ofFloat(0.7f, first.bottom + 200);
        Keyframe keyframe10 = Keyframe.ofFloat(1f, 200);


        PropertyValuesHolder holder1 = PropertyValuesHolder.ofKeyframe("x", keyframe1, keyframe3, keyframe7, keyframe9);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofKeyframe("y", keyframe2, keyframe4, keyframe8, keyframe10);
        PropertyValuesHolder holder3 = PropertyValuesHolder.ofKeyframe("second", keyframe5, keyframe6);
        ValueAnimator valueAnimator = ValueAnimator.ofPropertyValuesHolder(holder1, holder2, holder3).setDuration(DURATION);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float x = (float) animation.getAnimatedValue("x");
                float y = (float) animation.getAnimatedValue("y");

                first.left = x;
                first.right = x + 24;
                first.bottom = y;
                first.top = y - numbers[pos_first];

                float secondx = (float) animation.getAnimatedValue("second");
                second.left = secondx;
                second.right = secondx + 24;

                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                swap(numbers, pos_first, pos_second);
                Collections.swap(rects, pos_first, pos_second);
                handler.sendEmptyMessageDelayed(ScheduleHandler.UPDATE, 500);
//                fire();

            }
        });

        valueAnimator.start();
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

        canvas.drawLine(textHolder.startX, 200, textHolder.startX, 220, paint);
        canvas.drawLine(textHolder.startX, 220, textHolder.stopX, 220, paint);
        canvas.drawLine(textHolder.stopX, 200, textHolder.stopX, 220, paint);
        canvas.drawText(textHolder.text, (textHolder.startX + textHolder.stopX) / 2, 250, textPaint);

    }


    private static class TextHolder {
        private int startX;
        private int stopX;
        private String text;

        public TextHolder(int startX, int stopX, String text) {
            this.startX = startX;
            this.stopX = stopX;
            this.text = text;
        }

        public static TextHolder of() {
            return new TextHolder(0, 0, "");
        }

        public void update(int startX, int startY, String text) {
            this.startX = startX;
            this.stopX = startY;
            this.text = text;
        }
    }
}
