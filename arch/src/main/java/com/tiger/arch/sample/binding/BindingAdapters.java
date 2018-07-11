package com.tiger.arch.sample.binding;

import android.databinding.BindingAdapter;
import android.view.View;

public class BindingAdapters {

    @BindingAdapter("visibleGone")
    public static void show(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);

    }
}
