package shine.com.advance.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * Created by lixiaolin on 17/7/23.
 */

public class PanView2 extends FrameLayout {
    private static final String TAG = "PanView2";
    private int mScaledTouchSlop;
    private OverScroller mOverScroller;
    private GestureDetector mDetector;
    private float mTouchX;
    private float mTouchY;

    public PanView2(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PanView2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    private void init(Context context) {
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mOverScroller = new OverScroller(context);
        mDetector = new GestureDetector(context, listener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent() called with: ev = [" + ev + "]");
        int actionMasked = ev.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = ev.getX();
                mTouchY = ev.getY();
                mDetector.onTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = ev.getX();
                final float y = ev.getY();
                int deltaX = (int) Math.abs(mTouchX - x);
                int deltaY = (int) Math.abs(mTouchY - y);
                if (deltaX > mScaledTouchSlop || deltaY > mScaledTouchSlop) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged() " +
                "called with: w = [" + w + "], h = [" + h + "], oldw = [" + oldw + "], oldh = [" + oldh + "]");
        if (getChildCount()>0) {
            Log.d(TAG, "getChildAt(0).getWidth():" + getChildAt(0).getWidth());

        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
//        return super.onTouchEvent(event);
    }

    GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.d(TAG, "onDown() called with: e = [" + e + "]");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            fling(-(int) velocityX, -(int) velocityY);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d(TAG, " distanceX = [" + distanceX + "], distanceY = [" + distanceY + "]");
            scrollBy((int) distanceX, (int) distanceY);
            return true;
        }
    };

    private void fling(int velocityX, int velocityY) {
        if (getChildCount() == 1) {
            int maxX = getChildAt(0).getWidth() - (getWidth() - getPaddingLeft() - getPaddingRight());
            int maxY = getChildAt(0).getHeight() - (getHeight() - getPaddingTop() - getPaddingBottom());
            mOverScroller.fling(getScrollX(), getScrollY(), velocityX, velocityY,
                    0, Math.max(0, maxX), 0, Math.max(0, maxY));
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mOverScroller.computeScrollOffset()) {
            int x=mOverScroller.getCurrX();
            int y = mOverScroller.getCurrY();
            scrollTo(x,y);
            invalidate();
        }
    }

    @Override
    public void scrollTo(int x, int y) {

        if (getChildCount() == 1) {
            int width = getWidth() - getPaddingLeft() - getPaddingRight();
            int height = getHeight() - getPaddingTop() - getPaddingBottom();
            int childWidth = getChildAt(0).getWidth();
            int childHeight = getChildAt(0).getHeight();
            x = clamp(x, width, childWidth);
            y = clamp(y, height, childHeight);

        }


        super.scrollTo(x, y);


    }

    private int clamp(int currPosition, int measure, int childMeasure) {
        if (currPosition < 0 || measure > childMeasure) {
            return 0;
        }

        if (currPosition + measure > childMeasure) {
            return childMeasure - measure;
        }

        return currPosition;
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int widthSpec = MeasureSpec.makeMeasureSpec(lp.leftMargin + lp.rightMargin, MeasureSpec.UNSPECIFIED);
        int heightSpec = MeasureSpec.makeMeasureSpec(lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);
        child.measure(widthSpec, heightSpec);
    }


}
