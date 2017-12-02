package com.lxl.yuer.advance.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxl.yuer.advance.R;

/**
 * author:
 * 时间:2017/6/7
 * qq:1220289215
 * 类描述：对话框的样式,无标题，无边框，无背景色
 * 1. setStyle(DialogFragment.STYLE_NO_TITLE,R.style.Mdialog);
 *
 * 2. getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
 *getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
 *getDialog().getWindow().setDimAmount(0.5f);//背景黑暗度
 *
 */

public class WaitingDialog extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        设置对话框无标题，如果设置No_Framne,不能响应按键，回退时直接退出Activity，而不仅仅是Dialog
//        主题为自定义，消除对话框背景、边框等
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Mdialog);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        这三句话与上面的setStyle等价
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getDialog().getWindow().setDimAmount(0.5f);//背景黑暗度
        return inflater.inflate(R.layout.dialog_waiting, container, false);

    }

    @Override
    public void onStart() {
        //对齐方式
//        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        super.onStart();
    }

    public static WaitingDialog newInstance(String message) {
        Bundle args = new Bundle();
        args.putString("message",message);
        WaitingDialog fragment = new WaitingDialog();
        fragment.setArguments(args);
        return fragment;
    }

}
