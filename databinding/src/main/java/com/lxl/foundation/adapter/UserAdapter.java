package com.lxl.foundation.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxl.foundation.R;
import com.lxl.foundation.Utils;
import com.lxl.foundation.databinding.ItemUserBinding;
import com.lxl.foundation.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * author:
 * 时间:2017/5/24
 * qq:1220289215
 * 类描述：
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private List<User> mUserList = new ArrayList<>();

    public UserAdapter() {
        for (int i = 0; i < 10; i++) {
            mUserList.add(new User(Utils.getRandomFirstName(),Utils.getRandomFirstName(),18));
        }
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        holder.bind(mUserList.get(position));
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    static class UserHolder extends RecyclerView.ViewHolder {

        private final ItemUserBinding mBinding;

        public UserHolder(View itemView) {
            super(itemView);
//            这里是与普通Holder的区别
            mBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(@NonNull User user) {
            mBinding.setUser(user);
        }
    }
}

