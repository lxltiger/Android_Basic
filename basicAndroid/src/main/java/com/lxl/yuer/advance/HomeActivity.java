package com.lxl.yuer.advance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.lxl.yuer.advance.date.DateDemoActivity;

import java.util.Random;


/**
 * this app will display some useful knowledge I collect
 */
public class HomeActivity extends AppCompatActivity  {
    private static final String TAG = "HomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }


    private void jump(Class T) {

        Intent intent = new Intent(this, T);
        startActivity(intent);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_date_demo:
                jump(DateDemoActivity.class);
                break;

        }
    }




}
