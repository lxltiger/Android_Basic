package com.lxl.yuer.advance.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SimpleService extends Service {
    private static final String TAG = "SimpleService";
    private ScreenReceiver mScreenReceiver;

    public SimpleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called with: " + "intent = [" + intent + "]");
        return new SimpleBinder();
    }

    /**
     * 在生命周期只调用一次
     * 可在后台动态监听特殊服务
     */

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: service");
        super.onCreate();
        mScreenReceiver = new ScreenReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(mScreenReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called with: " + "intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     *销毁的时候取消监听，否则会泄漏
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called with: " );
        super.onDestroy();
        unregisterReceiver(mScreenReceiver);
    }

    private void print() {
        Log.d(TAG, "execute method from activity");
    }

    /**
     * 用于邦定的activity来调用服务的方法
     */
    class SimpleBinder extends Binder {
        public void call() {
            print();
        }
    }


}
