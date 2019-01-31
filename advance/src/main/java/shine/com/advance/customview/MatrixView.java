package shine.com.advance.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;


/**
 * Created by Administrator on 2015/7/21.
 * 矩阵的使用
 * 图片的拖动旋转缩放
 */
public class MatrixView extends AppCompatImageView {
    private static final String TAG = "MatrixView";
    //当前矩阵
    private Matrix mMatrixCurrent;
    //当前点
    private PointF mPointCurrent = new PointF();
    //两指间中点,确定缩放的参考点
    private PointF mPointMid = new PointF();

    public MatrixView(Context context) {
        this(context, null);
    }

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMatrixCurrent = new Matrix();
    }

    //两指间距离
    private float distance;
    //计算角度
    private float degree;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        switch (pointerCount) {
            case 1:
                return handleDrag(event);
            case 2:
                return handleRotateScale(event);
            default:
                return super.onTouchEvent(event);
        }
    }

    private boolean handleDrag(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - mPointCurrent.x;
                float dy = event.getY() - mPointCurrent.y;
                mMatrixCurrent.postTranslate(dx, dy);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "handleDrag: " + event);
                break;
        }
        mPointCurrent.set(event.getX(), event.getY());
        setImageMatrix(mMatrixCurrent);
        return true;
    }

    private boolean handleRotateScale(MotionEvent event) {
        //多点触控既要动作类型也要索引信息
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                distance = calculateSpace(event);
                Log.d("MatrixView", "distance:" + distance);
                if (distance > 10) {
                    //获取两指中点
                    setMid(event);
                }
                degree = calculateDegree(event);
                break;
            case MotionEvent.ACTION_MOVE:
                float newDistance = calculateSpace(event);
                //移动距离超过10
                if (Math.abs(newDistance - distance) > 10) {
                    //计算缩放笔列
                    float scale = newDistance / distance;
                    Log.d("MatrixView", "scale:" + scale);
                    mMatrixCurrent.postScale(scale, scale, mPointMid.x, mPointMid.y);
                    distance = newDistance;
                }
                //计算旋转角度
                float degreeNew = calculateDegree(event);
                //围绕屏幕中心旋转
                mMatrixCurrent.postRotate(degreeNew - degree, mPointMid.x, mPointMid.y);
                setImageMatrix(mMatrixCurrent);
                degree = degreeNew;
                break;
            //   两个手指离开一个可能会导致当前单点触摸的位置发生巨大变化导致图片也位移，所以需要更新当前触点位置，如果不在这里处理就需要在move事件中判断
            // 手指虽然离开了，event仍然包含离开手指的信息，event.getActionIndex()就是离开手指的index
            case MotionEvent.ACTION_POINTER_UP:
                Log.d(TAG, "ACTION_POINTER_UP: " + event);
                //actionIndex取值范围是变化的，和触点数量有关，因为是两个手指，actionIndex的值只有［0，1］，将离开的actionIndex异或1就是没有离开的
                int pointerIndex = event.getActionIndex() ^ 1;
                float x = event.getX(pointerIndex);
                float y = event.getY(pointerIndex);
                mPointCurrent.set(x, y);
                break;
        }
        return false;
    }

    /*
    计算两指间的距离，用来缩放
     */
    private float calculateSpace(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /*
    计算两指构成直线的角度，用来旋转
     */
    private float calculateDegree(MotionEvent event) {
        float deltaX = event.getX(0) - event.getX(1);
        float deltaY = event.getY(0) - event.getY(1);
        //atan2(y,x)所表达的意思是坐标原点为起点，指向(x,y)的射线在坐标平面上与x轴正方向之间的角的角度。
//        ATAN2(a,b)的取值范围介于 -pi 到 pi 之间（不包括 -pi） 当deltaY值从正到负过度，或反过来都会出现值的符号反转，即从pi突然到－pi的变化
// 但由于这个差值基本等于旋转360度所以看不出来图片逆向旋转了，如果使用atan会出现90度到负90的变化，旋转180就能看出来，需要特殊处理
        //结果为正表示从 X 轴逆时针旋转的角度，结果为负表示从 X 轴顺时针旋转的角度。
        double rad = Math.atan2(deltaY, deltaX);
        return (float) Math.toDegrees(rad);
    }

    /*
    计算两指的中间点
     */

    private void setMid(MotionEvent event) {
        mPointMid.set((event.getX(0) + event.getX(1)) / 2, (event.getY(0) + event.getY(1)) / 2);
    }

}
