package com.lxl.advance;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lxl.advance.databinding.ActivityResourceBinding;

/**
 * 资源在View中的邦定
 */
public class ResourceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityResourceBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_resource);
        binding.setFirstName("zhang");
        binding.setLastName("sanfeng");
        binding.setPad(false);

    }
}
