package shine.com.advance.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import shine.com.advance.R;

/*
 * 图像混合模式的演示
 * */
public class XfermodeView extends View {

    private Paint paint;
    private Bitmap des;
    private Bitmap src;
    private PorterDuffXfermode porterDuffXfermode;
    private int width;
    private int height;
    private PorterDuff.Mode mode;

    public XfermodeView(Context context) {
        this(context, null);
    }

    public XfermodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XfermodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XfermodeView);
        int xfermode = a.getInt(R.styleable.XfermodeView_xfermode, -1);
        switch (xfermode) {
            case 0:
                //         只在源图像和目标图像相交的地方绘制目标图像
                mode=PorterDuff.Mode.DST_IN;
                break;
            case 1:
//                 * 只在源图像和目标图像不相交的地方绘制目标图像
                    mode=PorterDuff.Mode.DST_OUT;
                break;
        }
        a.recycle();
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        porterDuffXfermode = new PorterDuffXfermode(mode);
        des = BitmapFactory.decodeResource(getResources(), R.drawable.beauty);
        src = BitmapFactory.decodeResource(getResources(), R.drawable.beauty_mask);
        width = src.getWidth();
        height = src.getHeight();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //如果不绘制背景会呈黑色
        canvas.drawColor(Color.WHITE);

        int saveLayer = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
//        先绘制的是目标des
        if (PorterDuff.Mode.DST_IN==mode) {
            canvas.drawBitmap(des, 0, 0, null);
        } else if (PorterDuff.Mode.DST_OUT==mode) {
            canvas.drawColor(0xFF8f66DA);
        }
        paint.setXfermode(porterDuffXfermode);
        canvas.drawBitmap(src, 0, 0, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(saveLayer);

    }
}
