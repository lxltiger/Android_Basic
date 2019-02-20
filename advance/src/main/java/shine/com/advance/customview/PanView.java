package shine.com.advance.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * Created by lixiaolin on 17/7/23.
 * 可以两个纬度滑动的scrollView，只能有一个子类
 */

public class PanView extends FrameLayout {
    private static final String TAG = "PanView";
    private OverScroller mOverScroller;
    private int eventX;
    private int eventY;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;

    private boolean isDrag = false;

    public PanView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PanView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    private void init(Context context) {
        mOverScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mVelocityTracker = VelocityTracker.obtain();
        mMinimumFlingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }


    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        Log.d(TAG, "measureChild() called with: child = [" + child + "], parentWidthMeasureSpec = [" + parentWidthMeasureSpec + "], parentHeightMeasureSpec = [" + parentHeightMeasureSpec + "]");
        int widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        child.measure(widthSpec, heightSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        Log.d(TAG, "measureChildWithMargins() called with: child = [" + child + "], parentWidthMeasureSpec = [" + parentWidthMeasureSpec + "], widthUsed = [" + widthUsed + "], parentHeightMeasureSpec = [" + parentHeightMeasureSpec + "], heightUsed = [" + heightUsed + "]");
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int width = lp.leftMargin + lp.rightMargin;
        int height = lp.topMargin + lp.bottomMargin;
        int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.UNSPECIFIED);
        int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED);
        child.measure(widthSpec, heightSpec);

    }


    //如果是drag事件(手指移动的距离大于touchSlap)就拦截，否则让子View处理
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.d(TAG, "onInterceptTouchEvent() called with: ev = [" + event + "]");
        int actionMasked = event.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                isDrag = false;
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                eventX = (int) event.getX();
                eventY = (int) event.getY();
                mVelocityTracker.clear();
                mVelocityTracker.addMovement(event);
                break;
            //此处计算的位移是以手指落点为参照的绝对值，不是相对上次移动的位置
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                int deltaX = eventX - x;
                int deltaY = eventY - y;
                Log.d(TAG, "deltaX:" + deltaX);
                Log.d(TAG, "deltaY:" + deltaY);
                if (Math.abs(deltaX) > mTouchSlop || Math.abs(deltaY) > mTouchSlop) {
                    isDrag = true;
                    mVelocityTracker.addMovement(event);
//                   一旦拦截，接下来的事件又onTouch处理
                    return true;
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mVelocityTracker.clear();
                isDrag = false;
                break;


        }

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        mVelocityTracker.addMovement(event);
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
//如果没有子视图处理事件，比如点击，就会走到这一步，必须消费，才能继续接受后续事件
//                如果有子视图消费，但被拦截了，就不会走这里
                return true;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                int deltaX = eventX - x;
                int deltaY = eventY - y;
                //如果子视图不处理事件，就回直接走此onTouchEvent，我们在down中消费后，需要自己判断是否是drag
                if (!isDrag && (Math.abs(deltaX) > mTouchSlop || Math.abs(deltaY) > mTouchSlop)) {
                    isDrag = true;

                }
                if (isDrag) {
                    scrollBy(deltaX, deltaY);
                    eventX = x;
                    eventY = y;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                isDrag = false;
                mVelocityTracker.clear();
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                break;

            case MotionEvent.ACTION_UP:
                isDrag = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                float xVelocity = mVelocityTracker.getXVelocity();
                float yVelocity = mVelocityTracker.getYVelocity();
                if (Math.abs(xVelocity) > mMinimumFlingVelocity || Math.abs(yVelocity) > mMinimumFlingVelocity) {
                    int width = getWidth() - getPaddingLeft() - getPaddingRight();
                    Log.d(TAG, "width:" + width);
                    int height = getHeight() - getPaddingTop() - getPaddingBottom();
                    Log.d(TAG, "height:" + height);
                    int childWidth = getChildAt(0).getWidth();
                    int childHeight = getChildAt(0).getHeight();
                    Log.d(TAG, "childWidth:" + childWidth);
                    Log.d(TAG, "childHeight:" + childHeight);
                    //  dang当子视图的宽或高比父视图大时才有滑动
                    int maxX = Math.max(0, childWidth - width);
                    int maxY = Math.max(0, childHeight - height);

                    mOverScroller.fling(getScrollX(), getScrollY(), -(int) mVelocityTracker.getXVelocity(),
                            -(int) mVelocityTracker.getYVelocity(), 0, maxX, 0, maxY);
                    invalidate();
                }
                Log.d(TAG, "getScrollX():" + getScrollX());
                Log.d(TAG, "getScrollY():" + getScrollY());

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void scrollTo(int x, int y) {

        x = clamp(x, getWidth(), getChildAt(0).getWidth());
        y = clamp(y, getHeight(), getChildAt(0).getHeight());

        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mOverScroller.computeScrollOffset()) {
//            滑动轨迹
            Log.d(TAG, "getScrollX():" + getScrollX()+"scrollY ="+getScrollY());

//要移到的位置，随着动画不断在改变，是根据初始位置，加速度计算来的
            int x = mOverScroller.getCurrX();
            int y = mOverScroller.getCurrY();
            Log.d(TAG, "x:" + x+"y "+y);

            x = clamp(x, getWidth(), getChildAt(0).getWidth());
            y = clamp(y, getHeight(), getChildAt(0).getHeight());

            scrollTo(x, y);
            invalidate();
        }
    }


    /**
     * @param offset       currX currY
     * @param measure      父视图的宽度
     * @param childMeasure 子视图的宽度
     * @return 偏移值
     * 限制滑动偏移范围，否则会出现白色空白区域
     */
    private int clamp(int offset, int measure, int childMeasure) {
        //检查上和左边界  如果超过左，上顶点，返回0，父视图比较大，不存在偏移，返回0
        if (offset < 0 || measure > childMeasure) {
            return 0;
        }

        //超越了子视图的右边或底部边界
        if (offset + measure > childMeasure) {
            return childMeasure - measure;
        }
        return offset;
    }
}


