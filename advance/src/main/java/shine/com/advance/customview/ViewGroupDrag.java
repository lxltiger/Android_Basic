package shine.com.advance.customview;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 触发事件顺序
 * shouldInterceptTouchEvent：

 DOWN:
 getOrderedChildIndex(findTopChildUnder)
 ->onEdgeTouched

 MOVE:
 getOrderedChildIndex(findTopChildUnder)
 ->getViewHorizontalDragRange &
 getViewVerticalDragRange(checkTouchSlop)(MOVE中可能不止一次)
 ->clampViewPositionHorizontal&
 clampViewPositionVertical
 ->onEdgeDragStarted
 ->tryCaptureView
 ->onViewCaptured
 ->onViewDragStateChanged

 processTouchEvent:

 DOWN:
 getOrderedChildIndex(findTopChildUnder)
 ->tryCaptureView
 ->onViewCaptured
 ->onViewDragStateChanged
 ->onEdgeTouched
 MOVE:
 ->STATE==DRAGGING:dragTo
 ->STATE!=DRAGGING:
 onEdgeDragStarted
 ->getOrderedChildIndex(findTopChildUnder)
 ->getViewHorizontalDragRange&
 getViewVerticalDragRange(checkTouchSlop)
 ->tryCaptureView
 ->onViewCaptured
 ->onViewDragStateChanged
 */
public class ViewGroupDrag extends LinearLayout {

    private ViewDragHelper mViewDragHelper;
    //可随意移动的View
    private View mDragView;
    //自动弹回的View
    private View mAutoBackView;
    //边界跟踪View，不能直接拖动
    private View mEdgeTrackerView;
    private Point mAutoBackViewOrginPos=new Point();
    public ViewGroupDrag(Context context, AttributeSet attrs) {
        super(context, attrs);
        //第一个就是当前的ViewGroup，第二个sensitivity，主要用于设置touchSlop:
        //helper.mTouchSlop = (int) (helper.mTouchSlop * (1 / sensitivity));
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            //ViewDragHelper中拦截和处理事件时，需要会回调CallBack中的很多方法来决定一些事，比如：哪些View可以移动、对个移动的View的边界的控制等等
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //根据传入的第一个view参数决定哪些可以捕获,mEdgeTrackerView不能直接拖动
                return child==mDragView||child==mAutoBackView;
            }

            //在该方法中对child移动的边界进行控制，
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
               final  int leftBound=getPaddingLeft();
                final int rightBound=getWidth()-child.getWidth()-leftBound;
                return Math.min(rightBound,Math.max(left,leftBound));
            }

            //left , top 分别为即将移动到的位置，比如横向的情况下，我希望只在ViewGroup的内部移动，
// 即：最小>=leftbound，最大<=ViewGroup.getWidth()-paddingright-child.getWidth
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int topBound=getPaddingTop();
                final int bottomBound=getHeight()-child.getHeight()-topBound;
                return Math.min(bottomBound,Math.max(top,topBound));
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if(releasedChild==mAutoBackView){
                    mViewDragHelper.settleCapturedViewAt(mAutoBackViewOrginPos.x,mAutoBackViewOrginPos.y);
                    invalidate();
                }
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mViewDragHelper.captureChildView(mEdgeTrackerView,pointerId);
            }
            //当子View可以消耗事件比如可以点击，就会先走OnInterceptTouchEvent方法判断可否捕获，而在判断过程中先调用
            //getViewHorizontalDragRange和getViewVerticalDragRange两个方法，只有着两个方法返回值大于0才能正常铺货。
            //如果不消耗事件在OnTouchEvent的TouchDown就确定了CaptureView;
            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth()-child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight()-child.getMeasuredHeight();
            }
        });

        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public void computeScroll() {
        if(mViewDragHelper.continueSettling(true)){
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mAutoBackViewOrginPos.x=mAutoBackView.getLeft();
        mAutoBackViewOrginPos.y=mAutoBackView.getTop();
    }

    //完成布局视图填充后
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView=getChildAt(0);
        mAutoBackView=getChildAt(1);
        mEdgeTrackerView=getChildAt(2);
    }



    //决定我们是否应该拦截当前的事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
