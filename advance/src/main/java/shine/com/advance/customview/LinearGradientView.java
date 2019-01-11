package shine.com.advance.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import shine.com.advance.R;


/**
 * Shader的子类  线性渐变
 * 简单渐变 paint.setShader(new LinearGradient(0, 0, screenWidth/2, screenHeight/2, Color.YELLOW, Color.RED, Shader.TileMode.REPEAT));
 * <p>
 * 复杂渐变positions表示的是渐变的相对区域，其取值只有0到1;positions可以为空,为空均匀分布颜色
 * paint.setShader(new LinearGradient(0, 0, screenWidth, screenHeight, new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE},
 * new float[]{0,0.1f,0.3f,0.5f,0.6f},Shader.TileMode.REPEAT));
 */
public class LinearGradientView extends View {
    private Paint paint;
    //原图
    private Bitmap mBitmapScr;
    //倒影图
    private Bitmap mBitmapRev;
    //原图顶点
    private int x = 0;
    private int y = 0;
    private int width;
    private int height;
    private RectF mirrorRect;
    private PorterDuffXfermode xfermode;

    public LinearGradientView(Context context) {
        this(context, null);
    }

    public LinearGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        paint = new Paint();
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        //原图
        mBitmapScr = BitmapFactory.decodeResource(getResources(), R.drawable.james);
        width = mBitmapScr.getWidth();
        height = mBitmapScr.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        //复制原图下半部分并沿Y轴旋转 即颠倒过来
        mBitmapRev = Bitmap.createBitmap(mBitmapScr, x, height / 2, width, height / 2, matrix, true);
        //复制的图片会放在原图下方实现镜面效果，所以设置画笔的渐变的范围在原图下方1/2
        paint.setShader(new LinearGradient(x, y + height, x, y + height + height / 2, 0xAA000000, Color.TRANSPARENT, Shader.TileMode.CLAMP));
//        要混合的镜像范围
        mirrorRect = new RectF(x, y + height, x + width, y + height * 3 / 2);
    }


//    锁屏后重开，draw会被调用  操作要对称，如果只有setXfermode（null）那么重绘就没有混合效果
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(mBitmapScr, x, y, null);

        int sc = canvas.saveLayer(mirrorRect, null, Canvas.ALL_SAVE_FLAG);
//        在原图下方绘制复制图
        canvas.drawBitmap(mBitmapRev, x, y + height, null);
//         paint绘制的区域和复制的图片混合 实现渐变的效果
        paint.setXfermode(xfermode);
        canvas.drawRect(mirrorRect, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(sc);
    }
}
