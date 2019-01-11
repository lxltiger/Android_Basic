package shine.com.advance.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import shine.com.advance.R;

/*
 * 图像混合模式、手势、Path的综合应用
 * 手指移动的地方会被擦除，类似橡皮擦的效果
 * */
public class EraseView extends View {

    private Paint paint;
    private Bitmap bg;
    private int width;
    private int height;
    private Path path;
    private Canvas fgCanvas;
    private Bitmap fg;
    private float curX;
    private float curY;

    public EraseView(Context context) {
        this(context, null);
    }

    public EraseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EraseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        //设置路径结合处样式
        paint.setStrokeJoin(Paint.Join.ROUND);
        //设置笔触类型
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.beauty);
        width = bg.getWidth();
        height = bg.getHeight();
        fg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        fgCanvas = new Canvas(fg);
        fgCanvas.drawColor(Color.GRAY);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                curX = event.getX();
                curY = event.getY();
                path.reset();
                path.moveTo(curX, curY);
                return true;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                if (Math.abs(x - curX) >= 2 || Math.abs(y - curY) >= 2) {
                    path.quadTo(curX, curY, x, y);
                    invalidate();
                    curX = x;
                    curY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                path.reset();
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bg, 0, 0, null);
        canvas.drawBitmap(fg, 0, 0, null);
        fgCanvas.drawPath(path, paint);

    }
}
