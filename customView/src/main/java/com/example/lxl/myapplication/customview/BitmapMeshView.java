package com.example.lxl.myapplication.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.example.lxl.myapplication.R;

/**
 * Created by Administrator on 2015/7/22.
 * 修改图片
 * 不能存储计算后点的值，每次调用drawBitmapMesh方法改变图像都是以基准点坐标为参考的
 */
public class BitmapMeshView extends View{
    //横纵方向的网格数
    private final static int xBlock=19;
    private final static int yBlock=19;
    //横纵交点的坐标
    private float[] verts;
    private Bitmap mBitmap;
    public BitmapMeshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.james);
        int count=(xBlock+1)*(yBlock+1);
        //奇数位存储x坐标，偶数位存储y坐标
        verts = new float[count * 2];
        //获取图片宽高
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        //x方向小块的宽度
        float xBlcokWidth = width / xBlock;
        //y方向小块的高度
        float yBlcokHeight = height / yBlock;

        for (int i = 0; i <=yBlock; i++) {
            //y方向每点的高度
            float fy=yBlcokHeight*i;
            for (int j = 0; j <= xBlock; j++) {
                float fx=xBlcokWidth*j+(yBlock-i)*width/yBlock;
                setXY(fx,fy,index);
                index++;
            }
        }
    }

    private int index=0;
    private void setXY(float fx,float fy,int index){
        verts[index*2+0]=fx;
        verts[index*2+1]=fy;
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmapMesh(mBitmap,xBlock,yBlock,verts,0,null,0,null);
    }
}
