package shine.com.advance.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import shine.com.advance.R;


/**
 * 贝塞尔曲线的应用
 */
public class WaveView extends View {

    private Paint paint;
    private int width;
    private int height;
    // 控制波浪高度 距离屏幕顶端的距离
    private int y_control;
    //    波浪的X坐标
    int x_control;
    //    贝塞儿曲线起点和终点距离屏幕顶端的距离
    private int side_height;

    private Path path;
    private boolean increase;
    private int wave_color;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes( attrs,R.styleable.WaveView);
        wave_color = a.getInt(R.styleable.WaveView_wave_color,Color.BLUE);
        a.recycle();
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(wave_color);
        paint.setStyle(Paint.Style.FILL);
        path = new Path();

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        y_control = -height / 16;
        side_height = height / 8;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
//       起点在屏幕左边不可见的位置
        path.moveTo(-width / 4, side_height);
//        x_control y_control 控制曲线凸起的地方 像波浪
        path.quadTo(x_control, y_control, width + width / 4, side_height);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();
        canvas.drawPath(path, paint);

//用来移动凸点的x轴左边 实现波浪的移动 只能朝一个方向
        if (x_control <= -width / 4) {
            increase = true;

        } else if (x_control >= width + width / 4) {
            increase = false;

        }

        x_control += increase ? 20 : -20;

//        降低曲线绘制的高度 实现水位下降
        if (side_height < height) {
            y_control += 2;
            side_height += 2;
        }
        invalidate();

    }
}
