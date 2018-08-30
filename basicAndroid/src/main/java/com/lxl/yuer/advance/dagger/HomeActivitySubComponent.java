package com.lxl.yuer.advance.dagger;

import com.lxl.yuer.advance.HomeActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
@Subcomponent(modules = NetModule.class)
public interface HomeActivitySubComponent extends AndroidInjector<HomeActivity>{

    @Subcomponent.Builder
     abstract class Builder extends AndroidInjector.Builder<HomeActivity> {
//        abstract Builder netModule(NetModule netModule);
    }

}
