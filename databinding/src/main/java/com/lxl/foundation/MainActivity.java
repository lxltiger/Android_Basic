package com.lxl.advance;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lxl.advance.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandler(new HomeClickHandler(this));
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        Log.d("MainActivity", "widthPixels:" + widthPixels);
        float density = getResources().getDisplayMetrics().density;
        Log.d("MainActivity", "density:" + density);
        Log.d("MainActivity", "widthPixels/density:" + (widthPixels / density));
    }
}
