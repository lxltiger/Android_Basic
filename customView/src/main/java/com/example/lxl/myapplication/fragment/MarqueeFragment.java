package com.example.lxl.myapplication.fragment;


import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.lxl.myapplication.R;
import com.example.lxl.myapplication.databinding.FragmentAnimationBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarqueeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MarqueeFragment";

    TextView mTvMarqueeText;
    //测量跑马灯长度
    private Paint mPaint;
    //设置跑马灯布局参数，主要是长度，和获取边距
    private ViewGroup.MarginLayoutParams mMarginLayoutParams;
    private FragmentAnimationBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_animation, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvMarqueeText=mBinding.tvMarqueeText;
        mBinding.btnStart.setOnClickListener(this);
        mBinding.btnStop.setOnClickListener(this);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(1.0f);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTypeface(mTvMarqueeText.getTypeface());
        mPaint.setTextSize(mTvMarqueeText.getTextSize());
        mMarginLayoutParams = (ViewGroup.MarginLayoutParams) mTvMarqueeText.getLayoutParams();
    }

    String marquees[]={"最近想学习Android动画，看了很多论坛大牛的帖子和Demo," +
            "但是始终无法形成一个全局概念，很多知识看了之后感觉只吸收到皮毛。" +
            "现在让我自己写一个view还是不知道如何下手。请问要怎样高效的学习Android动画呢？"
    ,"我觉得有必要强调一下”有意义“，有时候很多开发者有时候在炫技，" +
            "动画很生硬，转场不自然。包括Yalantis开源的个别项目也是" +
            "。NickButcher的这场演讲就来告诉你，如何构建有意义的动效，" +
            "当然有些产品中的动效还和交互设计师的专业水平有关。" +
            "所以可以仔细琢磨下slides和sample 项目中的动画实现，" +
            "一些细节的把握","yeah yeah yeah"};
    int index=0;
    private boolean mCanceled=false;

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                /*if (mTvMarqueeText.getAnimation() == null) {
                    if (marquees.length > 0) {
                        startAnimation();
                    }
                }else{
                    Log.d(TAG, "already has animation");
                }*/
                mBinding.tvMarquee.startScroll();
                break;
            case R.id.btn_stop:
               /* mCanceled=true;
                //clearAnimation 会结束当前动画调用End回调，如果end调用了下一个动画，会继续播
                mTvMarqueeText.clearAnimation();*/
               mBinding.tvMarquee.stopScroll();
                break;
        }
    }

    private void startAnimation() {
        //循环下一条跑马灯
        index=index%marquees.length;
        mTvMarqueeText.setText(marquees[index]);
        //测量长度
        int textWidth = (int) mPaint.measureText(mTvMarqueeText.getText().toString());
        //设置动画时长
        long duration= textWidth *10;
        Log.d(TAG, "textWidth:" + textWidth +"duration:" + duration);
        //设置TextView宽度
        mMarginLayoutParams.width= textWidth;
        mTvMarqueeText.setLayoutParams(mMarginLayoutParams);
        //创建动画
        TranslateAnimation translateAnimation = new TranslateAnimation(0, -textWidth - mMarginLayoutParams.leftMargin, 0, 0);
        translateAnimation.setDuration(duration);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "start index:" + index);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "end");
                if (!mCanceled) {
                    index++;
                    startAnimation();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, "repeat");
            }
        });
        mTvMarqueeText.startAnimation(translateAnimation);
    }
}
