package com.lxl.yuer.advance.dagger;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RandomUsersModule {



    @Provides
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    GsonConverterFactory gsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    public Retrofit provideRetrofit(OkHttpClient client,  GsonConverterFactory gsonConverterFactory) {

        return new Retrofit.Builder()
                .client(client)
                .baseUrl("https://www.baidu.com")
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Provides
    RandomUserApi RandomUserApi(Retrofit retrofit) {
        return retrofit.create(RandomUserApi.class);
    }
}
