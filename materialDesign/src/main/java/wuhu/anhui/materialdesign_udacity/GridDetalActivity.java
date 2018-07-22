package wuhu.anhui.materialdesign_udacity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GridDetalActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_detal);
        mImageView = (ImageView) findViewById(R.id.imageView);

        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.TOP);
        ViewGroup mViewGroup = (ViewGroup) findViewById(android.R.id.content);
        TransitionManager.beginDelayedTransition(mViewGroup,slide);
        mImageView.setVisibility(View.INVISIBLE);
    }
}
