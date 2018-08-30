package com.lxl.yuer.advance.dagger;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AppMudule {

    @Binds
    abstract Context bindContext(Application application);
}
