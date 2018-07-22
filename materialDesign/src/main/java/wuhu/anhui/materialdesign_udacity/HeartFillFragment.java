package wuhu.anhui.materialdesign_udacity;


import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



/**
 * A simple {@link Fragment} subclass.
 * 空心和实心切换动画
 *  勾和叉的旋转切换动画
 */
public class HeartFillFragment extends Fragment implements View.OnClickListener {
    private AnimatedVectorDrawable mHeartEmpty;
    private AnimatedVectorDrawable mHeartFull;
    private AnimatedVectorDrawable mTick;
    private AnimatedVectorDrawable mCross;
    private ImageView mImageView;
    private ImageView mImageView2;
    private boolean full=false;
    private boolean tick=true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_heart_fill, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mImageView = (ImageView) view.findViewById(R.id.image_view);
        mImageView.setOnClickListener(this);
        mHeartEmpty = (AnimatedVectorDrawable) getActivity().getDrawable(R.drawable.avd_heart_empty);
        mHeartFull = (AnimatedVectorDrawable) getActivity().getDrawable(R.drawable.avd_heart_fill);

        mImageView2 = (ImageView) view.findViewById(R.id.image_view2);
        mTick = (AnimatedVectorDrawable) getActivity().getDrawable(R.drawable.avd_tick_to_cross);
        mCross = (AnimatedVectorDrawable) getActivity().getDrawable(R.drawable.avd_cross_to_tick);
        mImageView2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view:
                AnimatedVectorDrawable drawable=full?mHeartEmpty:mHeartFull;
                mImageView.setImageDrawable(drawable);
                drawable.start();
                full=!full;
                break;
            case R.id.image_view2:
                AnimatedVectorDrawable vectorDrawable=tick?mTick:mCross;
                mImageView2.setImageDrawable(vectorDrawable);
                vectorDrawable.start();
                tick=!tick;
                break;
        }

    }
}
