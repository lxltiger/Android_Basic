package shine.com.advance.anim;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import shine.com.advance.R;

public class AnimationActivity extends AppCompatActivity {
    private static final String TAG = "AnimationActivity";
    private Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        test = (Button) findViewById(R.id.btn_test);
    }

    public void test(View view) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(100f, 50f,200,100f).setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float
                        animatedValue = (float) animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: "+animatedValue);
//                test.setTranslationX((float) animation.getAnimatedValue());
                test.getLayoutParams().width= (int) animatedValue;
                test.requestLayout();
            }
        });
        valueAnimator.start();
    }
}
