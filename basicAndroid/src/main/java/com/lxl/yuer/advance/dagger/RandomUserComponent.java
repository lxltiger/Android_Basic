package com.lxl.yuer.advance.dagger;

import com.lxl.yuer.advance.HomeActivity;

import dagger.Component;

@Component(modules = {RandomUsersModule.class, OkHttpClientModule.class,ContextModule.class})
public interface RandomUserComponent {
    void inject(HomeActivity activity);
//    RandomUserApi getRandomUserService();
//    Picasso getPicasso();
}