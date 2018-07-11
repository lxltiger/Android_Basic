package com.tiger.arch.sample.ui.common;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

public class DataBoundViewHolder<V extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public final V binding;

    public DataBoundViewHolder(V binding) {
        super(binding.getRoot());
        this.binding = binding;

    }
}
