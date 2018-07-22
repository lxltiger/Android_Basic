package wuhu.anhui.materialdesign_udacity;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.View;
/*
* 曲线动画切换的详情页,头像的颜色没有按预期显示*/
public class CurvedMotionDetailActivity extends AppCompatActivity {

    public static final String EXTRA_COLOR = "EXTRA_COLOR";
    public static final String EXTRA_CURVE = "EXTRA_CURVE";
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curved_motion_detail);
        // tint the circle to match the launching activity
        View avatar = findViewById(R.id.avatar);
        avatar.setBackgroundTintList(
                ColorStateList.valueOf(getIntent().getIntExtra(EXTRA_COLOR, 0xffff00ff)));

        // check if we should used curved motion and load an appropriate transition
        boolean curve = getIntent().getBooleanExtra(EXTRA_CURVE, false);
        getWindow().setSharedElementEnterTransition(TransitionInflater.from(this)
                .inflateTransition(curve ? R.transition.curve : R.transition.move));
    }
}
