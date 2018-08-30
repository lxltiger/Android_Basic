package com.lxl.yuer.advance;

import android.app.Activity;
import android.app.Application;

import com.lxl.yuer.advance.dagger.DaggerNetComponent;
import com.lxl.yuer.advance.dagger.HomeActivitySubComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class App extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerNetComponent.create().inject(this);
    }



    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
