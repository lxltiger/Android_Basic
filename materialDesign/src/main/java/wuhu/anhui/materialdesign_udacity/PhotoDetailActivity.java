package wuhu.anhui.materialdesign_udacity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;

public class PhotoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        ThreeTwoImageView imageView = (ThreeTwoImageView) findViewById(R.id.imageView);
        Glide.with(this).load(getIntent().getData()).into(imageView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.BOTTOM);
            slide.addTarget(R.id.scrollView);
            slide.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in));
            slide.setDuration(1000);
            getWindow().setEnterTransition(slide);

        }
    }
}
