package com.lxl.yuer.advance.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/*
* 一些频繁的广播操作如锁屏是不能静态注册
* 在Actvity里可动态注册，但受其生命周期影响
* 可在服务里注册，在后台监听
* */
public class ScreenReceiver extends BroadcastReceiver {
    private static final String TAG = "ScreenReceiver";
    public ScreenReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() called with: "+  intent );
    }
}
