package com.example.lxl.myapplication.acitivity;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.example.lxl.myapplication.render.MyGLRender;


public class GLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        MyGLSurfaceView surfaceView = new MyGLSurfaceView(this);
        setContentView(surfaceView);


    }


    class MyGLSurfaceView extends GLSurfaceView {

        private static final float TOUCH_SCALE_FACTOR = 180.0f / 320;
        private final MyGLRender mMyGLRender;
        private float mPreviousX;
        private float mPreviousY;


        public MyGLSurfaceView(Context context) {
            super(context);
            setEGLContextClientVersion(2);
            mMyGLRender = new MyGLRender();
            setRenderer(mMyGLRender);
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            Log.d("MyGLSurfaceView", "motionEvent:" + motionEvent);
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_MOVE:

                    float dx = x - mPreviousX;
                    float dy = y - mPreviousY;

                    // reverse direction of rotation above the mid-line
                    if (y > getHeight() / 2) {
                        dx = dx * -1 ;
                    }

                    // reverse direction of rotation to left of the mid-line
                    if (x < getWidth() / 2) {
                        dy = dy * -1 ;
                    }

                    mMyGLRender.setAngle(mMyGLRender.getAngle() +
                            ((dx + dy) * TOUCH_SCALE_FACTOR));
                    requestRender();
                    break;
            }

            mPreviousX=x;
            mPreviousY=y;
            return true;
        }


    }
}
