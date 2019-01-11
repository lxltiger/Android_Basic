package shine.com.advance.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by Administrator on 2015/7/21.
 * 演示路径效果，无PathEffect、CornerPathEffect、DiscretePathEffect、DashPathEffect、PathDashPathEffect、ComposePathEffect、SumPathEffect
 */
public class PathEffectView extends View {
    private Paint mPaint;
    private PathEffect[] mPathEffects = new PathEffect[7];
    private Path mPath;
    //偏移量
    private int mPhrase=3;
    public PathEffectView(Context context) {
        this(context, null);
    }

    public PathEffectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPath = new Path();
        mPath.moveTo(0, 20);
        Random random = new Random();
        for (int i = 0; i <= 30; i++) {
            mPath.lineTo(i*30, random.nextFloat()*50);
        }

    }
    //如何解决new对象的问题
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        for (int i = 0; i < mPathEffects.length; i++) {
            mPaint.setPathEffect(mPathEffects[i]);
            canvas.drawPath(mPath,mPaint);
            canvas.translate(0,120);
        }
        //实例化路径效果
        mPathEffects[0]=null;
        //radius，意思就是转角处的圆滑程度
        mPathEffects[1]=new CornerPathEffect(20);
        //离散路径效果其会在路径上绘制很多“杂点”的突出来模拟一种类似生锈铁丝的效果
        // 第一个指定这些突出的“杂点”的密度，值越小杂点越密集，第二个参数呢则是“杂点”突出的大小，值越大突出的距离越大
        mPathEffects[2]=new DiscretePathEffect(3,5);
        //浮点数组的依次表示实线、虚线的长度，大于2即可
        //第二个参数我称之为偏移值，动态改变其值会让路径产生动画的效果
        mPathEffects[3]=new DashPathEffect(new float[]{20, 10, 5, 10},mPhrase);
        //PathDashPathEffect和DashPathEffect是类似的，不同的是PathDashPathEffect可以让我们自己定义路径虚线的样式
        Path path = new Path();
        path.addRect(0,0,8,8, Path.Direction.CCW);
        mPathEffects[4] = new PathDashPathEffect(path, 12, mPhrase, PathDashPathEffect.Style.ROTATE);
        //ComposePathEffect先将路径变成innerpe的效果，再去复合outerpe的路径效果
        mPathEffects[5]=new ComposePathEffect( mPathEffects[2], mPathEffects[4]);
        //SumPathEffect把两种路径效果加起来再作用于路径
        mPathEffects[6]=new SumPathEffect( mPathEffects[4], mPathEffects[3]);
        mPhrase+=1;
        invalidate();
    }
}
