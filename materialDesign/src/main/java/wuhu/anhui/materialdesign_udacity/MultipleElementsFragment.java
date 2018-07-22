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



/**
 * A simple {@link Fragment} subclass.
 *  多个CardView有序的进入动画,暂时针对5.0以上系统
 */
public class MultipleElementsFragment extends Fragment implements View.OnClickListener {


    private int mCount;
    private ViewGroup mRoot;



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
        animateViewsIn();
    }

    @TargetApi(21)
    private void animateViewsIn() {

        float offset = getResources().getDimensionPixelSize(R.dimen.offset_y);
        Interpolator interpolator =
                AnimationUtils.loadInterpolator(getActivity(), android.R.interpolator.linear_out_slow_in);

        // loop over the children setting an increasing translation y but the same animation
        // duration + interpolation
        for (int i = 0; i < mCount; i++) {
            View view = mRoot.getChildAt(i);
            view.setVisibility(View.VISIBLE);
            view.setTranslationY(offset);
            view.setAlpha(0.85f);
            // then animate back to natural position
            view.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setInterpolator(interpolator)
                    .setDuration(1000L)
                    .start();
            // increase the offset distance for the next view
            offset *= 1.5f;
        }
    }



    @Override
    public void onClick(View v) {
        animateViewsIn();
    }
}
