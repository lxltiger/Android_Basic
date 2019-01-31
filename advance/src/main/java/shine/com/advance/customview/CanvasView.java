package shine.com.advance.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2015/6/27.
 * 自定义多圈图
 * 使用到了层和基本绘制技巧
 */
public class CanvasView extends View {
    // 描边宽度占比
    private static final float STROKE_WIDTH = 1F / 256F,
            CRICLE_LARGER_RADIU = 3F / 32F, // 大圆半径
            ARC_LENGTH=1F / 8F,//扇形边长
            LINE_LENGTH = 3F / 32F ;// 线段长度占比
    //描边宽度
    private float strokeWidth;
    private float ccX, ccY;// 中心圆圆心坐标
    private float largeCricleRadiu;// 大圆半径
    private float lineLength;// 线段长度
    private float arcLength;// 线段长度

    //控件宽度
    private int width;
    private Paint mPaint;
    private TextPaint textPaint;
    private Paint arcPaint;
    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //强制宽高一致
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        Log.d("MutilpCircleView", "measure");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width=w;
        Log.d("MutilpCircleView", "width:" + width);
        calculate();
    }
    //计算圆圈的位置大小
    private void calculate() {
        //描边宽
        strokeWidth=width*STROKE_WIDTH;
        largeCricleRadiu=width*CRICLE_LARGER_RADIU;
        lineLength=width*LINE_LENGTH;
        arcLength=width*ARC_LENGTH;
        Log.d("MutilpCircleView", "lineLength:" + lineLength);
        ccX=width/2;
        ccY=width/2+largeCricleRadiu;
        setParam();
    }

    private void setParam() {
        mPaint.setStrokeWidth(strokeWidth);
    }

    //使文字居中的偏移量
    private float textOffsetY;
    private void initPaint(){
        mPaint =new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.WHITE);

        textPaint=new TextPaint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(25);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textOffsetY=(textPaint.descent()+textPaint.ascent())/2;

        arcPaint = new Paint();
        arcPaint.setStyle(Paint.Style.FILL);
        arcPaint.setColor(0x55EC6941);

    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(0xFFF29B76);
        canvas.drawCircle(ccX, ccY, largeCricleRadiu, mPaint);
        canvas.drawText("shapen", ccX, ccY - textOffsetY, textPaint);
        //绘制左上方图形
        drawTopLeft(canvas);
        //右上方及圆弧
        drawRightCircle(canvas, 30);


        //底部
        drawCircle(canvas, -180);
        //左底
        drawCircle(canvas, -100);
        //右底
        drawCircle(canvas, 100);
    }


    private void drawTopLeft(Canvas canvas) {
        canvas.save();
        //将画布移动到圆心
        canvas.translate(ccX, ccY);
        canvas.rotate(-30);
        canvas.drawLine(0, -largeCricleRadiu, 0, -lineLength * 2, mPaint);
        canvas.drawCircle(0, -lineLength * 3, largeCricleRadiu, mPaint);
        canvas.drawText("lxllxl", 0, -lineLength * 3 - textOffsetY, textPaint);
        canvas.drawLine(0, -4 * largeCricleRadiu, 0, -lineLength * 5, mPaint);
        canvas.drawCircle(0, -lineLength * 6, largeCricleRadiu, mPaint);
        canvas.drawText("lylyll", 0, -lineLength * 6 - textOffsetY, textPaint);

        canvas.restore();
    }

    private void drawCircle(Canvas canvas,int degree){
        canvas.save();
        canvas.translate(ccX, ccY);
        canvas.rotate(degree);
        canvas.drawLine(0, -largeCricleRadiu, 0, -lineLength * 2, mPaint);
        canvas.drawCircle(0, -lineLength * 3, largeCricleRadiu, mPaint);
        canvas.translate(0, -lineLength * 3);
        canvas.rotate(-degree);
        canvas.drawText("lylyll", 0,  - textOffsetY,textPaint);
        canvas.restore();


    }
    /*
    右上方圆圈及弧
     */
 private void drawRightCircle(Canvas canvas,int degree){
        canvas.save();
        canvas.translate(ccX, ccY);
        canvas.rotate(degree);
        canvas.drawLine(0, -largeCricleRadiu, 0, -lineLength * 2, mPaint);
        canvas.drawCircle(0, -lineLength * 3, largeCricleRadiu, mPaint);
        canvas.drawText("lylyll", 0, -lineLength * 3 - textOffsetY, textPaint);
        drawArc(canvas,degree);
        canvas.restore();
    }

    private void drawArc(Canvas canvas,int degree) {
        canvas.save();
        //将画布移到右圆中心
        canvas.translate(0, -lineLength * 3);
        canvas.rotate(-degree);
        RectF rectF = new RectF(-arcLength,-arcLength,arcLength,arcLength);
        canvas.drawArc(rectF, -22.5f, -135f, true, arcPaint);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(4);
        arcPaint.setColor(Color.WHITE);
        canvas.drawArc(rectF, -22.5f, -135f, false, arcPaint);
        //文字圆弧的半径
        float textarc=5f/32f*width;
        canvas.save();
        canvas.rotate(-135f / 2);
        for (float i = 0; i <33.5f*5 ; i+=33.5f) {
            canvas.save();
            canvas.rotate(i);
            canvas.drawText("lxl",0,-textarc,textPaint);
            canvas.restore();
        }

        canvas.restore();
        canvas.restore();
    }


}
