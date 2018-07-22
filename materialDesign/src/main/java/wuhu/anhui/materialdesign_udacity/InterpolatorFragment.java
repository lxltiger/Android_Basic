package wuhu.anhui.materialdesign_udacity;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewAnimator;


/**
 * A simple {@link Fragment} subclass.
 *   插值器效果演示
 */
public class InterpolatorFragment extends Fragment implements View.OnClickListener,  AdapterView.OnItemSelectedListener {


    private TextView mTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_interpolator, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTextView = (TextView) view.findViewById(R.id.textView);
        Spinner spinner = (Spinner) view.findViewById(R.id.interpolator_spinner);
        spinner.setOnItemSelectedListener(this);
        view.findViewById(R.id.btn_start_animation).setOnClickListener(this);

    }

    /**
     * ValueAnimator  提供时间计算engine,设置在执行动画的对象上
     * 一般使用非线性插值器,对值进行平滑过渡
     */
    private void propertyAnimationDemo1() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        //ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(0f, 5f, 3f, 10f);
        valueAnimator.setDuration(600);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                Log.d("InterpolatorFragment", "value:" + value);
            }
        });
    }
    /*
    * ObjectAnimation
    *对任意对象的任意属性进行操作
     */
    private void propertyAlpha() {
        ObjectAnimator objectAnimator =
                ObjectAnimator.ofFloat(mTextView, ViewAnimator.ALPHA, 1f, 0f, 1f);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
    }

    private void propertyRotate() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mTextView, ViewAnimator.ROTATION, 0f, 360f);
        objectAnimator.setDuration(5000);
        objectAnimator.start();
    }

    private void propertyTranslation() {
        float cur_translation=mTextView.getTranslationX();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mTextView, ViewAnimator.TRANSLATION_X,
                cur_translation, -500f, cur_translation);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
    }

    private void propertyScale() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mTextView, ViewAnimator.SCALE_Y, 1f, 3f, 1f);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
    }

    private void propertySet() {
        ObjectAnimator animatorTranslation = ObjectAnimator.ofFloat(mTextView, ViewAnimator.TRANSLATION_X,0f, -500f, 0f);
        ObjectAnimator animatorRotate = ObjectAnimator.ofFloat(mTextView, ViewAnimator.ROTATION, 0f, 360f);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(mTextView, ViewAnimator.ALPHA, 1f, 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorAlpha).with(animatorRotate).after(animatorTranslation);
        animatorSet.setDuration(5000);
        animatorSet.start();
    }

    private void propertySet2() {
        Animator animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.basic_object_animation);
        animator.setTarget(mTextView);
        animator.start();
    }
    @Override
    public void onClick(View v) {
//        propertyAnimationDemo1();
//        propertyAlpha();
//        propertyRotate();

//        propertyTranslation();
//        propertyScale();
//        propertySet2();


    }



    // 插值器的简单演示,此回调在初始化就会调用一次,默认选择第一个值
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Interpolator interpolator = new AnticipateInterpolator();
        mTextView.setTranslationX(300f);
        mTextView.animate().setDuration(2000)
                .setInterpolator(interpolator)
                .translationXBy(-300f)
                .start();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
