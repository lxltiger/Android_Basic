package com.lxl.yuer.advance.dagger;

import android.content.Context;

import com.lxl.yuer.advance.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {AppMudule.class,HomeActivityModule.class, AndroidInjectionModule.class})
public interface NetComponent extends AndroidInjector<App>{
    /*void inject(HomeActivity activity); */

   /* @Component.Builder
    interface Builder {
        @BindsInstance
        NetComponent.Builder application(App app);

//        NetComponent.Builder netModule(NetModule netModule);
        NetComponent build();
    }*/

}
