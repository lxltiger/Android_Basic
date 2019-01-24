package shine.com.advance;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.TextView;

import shine.com.advance.customview.MarqueeView;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private VelocityTracker velocityTracker;
    private MarqueeView marqueeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        velocityTracker = VelocityTracker.obtain();
        marqueeView = (MarqueeView) findViewById(R.id.btn);


    }

    private void print() {
        Log.d(TAG, "onCreate: " + marqueeView.getLeft());
        Log.d(TAG, "onCreate: " + marqueeView.getTop());
        Log.d(TAG, "onCreate: " + marqueeView.getX());
        Log.d(TAG, "onCreate: " + marqueeView.getY());
        Log.d(TAG, "onCreate: " + marqueeView.getTranslationX());
        Log.d(TAG, "onCreate: " + marqueeView.getTranslationY());
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        velocityTracker.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: " + event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                velocityTracker.clear();
                velocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
                float xVelocity = velocityTracker.getXVelocity();
                float yVelocity = velocityTracker.getYVelocity();
                Log.d(TAG, "onTouchEvent: " + xVelocity);
                Log.d(TAG, "onTouchEvent: " + yVelocity);
                break;
        }
        return super.onTouchEvent(event);
    }


    public void test(View view) {
        marqueeView.setText("sdodgiodshgodghidghogdsssds华盛顿送的活动送花送活动山东省的欢送会都是动画还送红色的红红的送电话送货电话8888888dsd");

    }
}
