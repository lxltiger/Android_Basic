package com.lxl.advance;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lxl.advance.databinding.ActivityConversionBinding;

/**
 * 转换邦定
 */
public class ConversionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityConversionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_conversion);
        binding.setImageUrl("http://pic.meizitu.com/wp-content/uploads/2015a/10/18/01.jpg");

    }

    /*有的设备没有显示出来*/
    @BindingAdapter("android:src")
    public static void showImage(ImageView view,String url) {
        Log.d("ConversionActivity", url);
        Glide.with(view.getContext()).load(url).into(view);
    }


}
