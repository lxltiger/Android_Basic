package com.tiger.arch.sample.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * 在Fragment视图销毁的时候置为空
 * @param <T>
 */
public class AutoClearValue<T> {

    private T value;

    public AutoClearValue(Fragment fragment, T value) {
        FragmentManager fragmentManager = fragment.getFragmentManager();
       fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                super.onFragmentDestroyed(fm, f);
                if (f ==fragment) {
                    AutoClearValue.this.value=null;
                    fragmentManager.unregisterFragmentLifecycleCallbacks(this);
                }
            }
        },false );

        this.value = value;

    }

    public T get() {
        return value;
    }
}
