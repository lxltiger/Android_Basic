package com.lxl.yuer.advance.dagger;

import android.app.Activity;

import com.lxl.yuer.advance.HomeActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = HomeActivitySubComponent.class)
public abstract class HomeActivityModule {

    @Binds
    @IntoMap
    @ActivityKey(HomeActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindHomeActivityInjectFactory(HomeActivitySubComponent.Builder builder);
}
