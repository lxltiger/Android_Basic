package com.tiger.arch.codelab.time;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tiger.arch.R;

/**
 * 每秒刷新一次界面
 * 使用TimeViewModel执行定时任务，将结果设置到LiveData中，在UI界面监听此LiveData值的变化
 */
public class TimeActivity extends AppCompatActivity {
    private static final String TAG = "TimeActivity";
    private TimeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        mViewModel = ViewModelProviders.of(this).get(TimeViewModel.class);
        subscribeUI();

    }

    private void subscribeUI() {
        mViewModel.getElapsedTime().observe(this, aLong -> {
                    TextView textView = findViewById(R.id.textView);
                    textView.setText(getString(R.string.elapse, aLong));
                }
        );

    }
}
