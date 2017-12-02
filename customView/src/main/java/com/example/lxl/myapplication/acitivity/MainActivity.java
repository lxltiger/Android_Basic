package com.example.lxl.myapplication.acitivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.lxl.myapplication.fragment.MarqueeFragment;
/*
开启硬件加速之后，许多2D的绘制方法会抛出异常：
Canvas •clipPath()
•clipRegion()
•drawPicture()
•drawTextOnPath()
•drawVertices()

•Paint •setLinearText()
•setMaskFilter()
•setRasterizer()

•Xfermodes •AvoidXfermode
•PixelXorXfermode



In addition, some operations behave differently with hardware acceleration enabled:

•Canvas •clipRect(): XOR, Difference and ReverseDifference clip modes are ignored. 3D transforms do not apply to the clip rectangle
•drawBitmapMesh(): colors array is ignored

•Paint •setDither(): ignored
•setFilterBitmap(): filtering is always on
•setShadowLayer(): works with text only


•PorterDuffXfermode •PorterDuff.Mode.DARKEN will be equivalent to SRC_OVER when blending against the framebuffer.
•PorterDuff.Mode.LIGHTEN will be equivalent to SRC_OVER when blending against the framebuffer.
•PorterDuff.Mode.OVERLAY will be equivalent to SRC_OVER when blending against the framebuffer.

•ComposeShader •ComposeShader can only contain shaders of different types (a BitmapShader and a LinearGradient for instance, but not two instances of BitmapShader )
•ComposeShader cannot contain a ComposeShader



 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager=getSupportFragmentManager();
        Fragment fragment=fragmentManager.findFragmentById(android.R.id.content);
        if(fragment==null){
            fragment=new MarqueeFragment();
            fragmentManager.beginTransaction().add(android.R.id.content,fragment).commit();
        }
        /*setContentView(R.layout.iconview);
        IconView view = (IconView) findViewById(R.id.iconView);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);
        view.setBitmap(bitmap);*/
    }



}
