package wuhu.anhui.materialdesign_udacity;


import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;


/**
 * A simple {@link Fragment} subclass.
 * cardView的对称和非对称大小变化演示
 */
public class SizeChangeFragment extends Fragment implements View.OnClickListener {

    private CardView mCardView;
    //cardview 的大小状态,动画之前小,动画之后大
    private boolean isSmall=true;
    //是否对称缩放,通过控制duration长度实现
    private boolean symmetric=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_size_change, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCardView = (CardView) view.findViewById(R.id.cardView);
        mCardView.setOnClickListener(this);
    }
    @TargetApi(21)
    private void changeSize() {
        Interpolator interpolator = AnimationUtils.
                loadInterpolator(getActivity(), android.R.interpolator.linear_out_slow_in);
        ObjectAnimator scale_x = ObjectAnimator.ofFloat(mCardView, View.SCALE_X, isSmall?1.5f:1f);
        scale_x.setInterpolator(interpolator);
        //如果对称设置时长和Y轴一样长
        scale_x.setDuration(symmetric?600L:200L);
        ObjectAnimator scale_y = ObjectAnimator.ofFloat(mCardView, View.SCALE_Y, isSmall?1.5f:1f);
        scale_y.setInterpolator(interpolator);
        scale_y.setDuration(600L);
        scale_x.start();
        scale_y.start();
        //切换大小状态
        isSmall=!isSmall;
        if (isSmall) {
            //在原始状态切换 对称/非对称缩放
            symmetric=!symmetric;
        }
    }
    @Override
    public void onClick(View v) {
       changeSize();
    }
}
