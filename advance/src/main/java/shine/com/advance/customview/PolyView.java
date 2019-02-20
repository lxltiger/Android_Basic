package shine.com.advance.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import shine.com.advance.R;


/**
 * Created by Administrator on 2015/7/19.
 */
public class PolyView extends View {
    private Bitmap mBitmap;
    private Matrix mMatrix;

    private LinearGradient mLinearGradient;

    private Matrix mGradientMatrix;
    private Paint mPaint;
    public PolyView(Context context) {
        this(context, null);
    }

    int mWidth;
    int mHeight;
    public PolyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);
        mMatrix = new Matrix();
         mWidth = mBitmap.getWidth();
         mHeight=mBitmap.getHeight();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha((int)(225*0.9));
        mLinearGradient = new LinearGradient(0, 0, 0.5f, 0, Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);
        mGradientMatrix = new Matrix();
        mGradientMatrix.setScale(mWidth,1);
        mLinearGradient.setLocalMatrix(mGradientMatrix);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();
        float[] src={0,0,mWidth,0,mHeight,mWidth,0,mHeight};
        float[] des={0,0,mWidth,50,mWidth,mHeight-50,0,mHeight};
        mMatrix.setPolyToPoly(src,0,des,0,src.length>>1);
        canvas.concat(mMatrix);
        canvas.drawBitmap(mBitmap, mMatrix, null);
        canvas.drawRect(0,0,mWidth,mHeight,mPaint);
        canvas.restore();
    }
}
