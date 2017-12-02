package com.lxl.foundation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lxl.foundation.databinding.ActivityUserBinding;
import com.lxl.foundation.entity.User;

/**
 * 用户界面，演示数据绑定，button和checkbox监听
 */
public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ActivityUserBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        User user = new User("one", "two", 23);
        binding.setUser(user);
        binding.setHandler(new UserClickHandler());
    }
}
