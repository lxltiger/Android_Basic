package wuhu.anhui.materialdesign_udacity;


import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 *  多个CardView的无序进入动画,暂时针对5.0以上系统
 */
public class MultipleChaoticElementsFragment extends Fragment implements View.OnClickListener {


    private int mCount;
    private ViewGroup mRoot;
    private float mMax_width_offset;
    private float mMax_height_offset;
    //获取随机数制作混乱
    private Random mRandom = new Random();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiple_elements, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRoot = (ViewGroup) view.findViewById(R.id.root);
        mRoot.setOnClickListener(this);
        mCount = mRoot.getChildCount();
        //可以偏移的最大宽度和高度
        mMax_width_offset = 2f*getActivity().getResources().getDisplayMetrics().widthPixels;
        mMax_height_offset = 2f*getActivity().getResources().getDisplayMetrics().heightPixels;
        animateViewsIn();
    }

    @TargetApi(21)
    private void animateViewsIn() {

        float offset = getResources().getDimensionPixelSize(R.dimen.offset_y);
        Interpolator interpolator =
                AnimationUtils.loadInterpolator(getActivity(), android.R.interpolator.linear_out_slow_in);

        for (int i = 0; i < mCount; i++) {
            View view = mRoot.getChildAt(i);
            view.setVisibility(View.VISIBLE);
            view.setAlpha(0.85f);
            float x_offset=mRandom.nextFloat()*mMax_width_offset;
            if (mRandom.nextBoolean()) {
                x_offset*=-1;
            }
            view.setTranslationX(x_offset);
            float y_offset = mRandom.nextFloat() * mMax_height_offset;
            if (mRandom.nextBoolean()) {
                y_offset*=-1;
            }
            view.setTranslationY(y_offset);
            // then animate back to natural position
            view.animate()
                    .translationY(0f)
                    .translationX(0f)
                    .alpha(1f)
                    .setInterpolator(interpolator)
                    .setDuration(1000L)
                    .start();
        }
    }



    @Override
    public void onClick(View v) {
        animateViewsIn();
    }
}
