package com.tiger.arch.codelab.lifecycle;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Lifecycle 的event和state解决了事件和状态不匹配的问题
 * 比如在onstart执行的网络请求，当成功回调的时候可能已经是onStop状态了
 * lifecycle提供的状态查询有效的解决此类问题
 */
public class MyLifecycleObserver implements DefaultLifecycleObserver {
    private static final String TAG = "MyLifecycleObserver";

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "onStart: ");

    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "onDestroy: ");
    }
}