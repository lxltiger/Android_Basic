package com.tiger.arch.sample.api;

import android.os.Build;
import android.util.Log;

import com.tiger.arch.sample.Tls12SocketFactory;
import com.tiger.arch.sample.utils.LiveDataCallAdapterFactory;

import javax.net.ssl.SSLContext;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tiger.arch.sample.Tls12SocketFactory.enableTls12OnPreLollipop;

public class NetWork {
    private static final String TAG = "NetWork";

    private static GithubService sGithubService;

    public static GithubService githubService() {
        if (sGithubService == null) {
//            OkHttpClient okHttpClient = initOkHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .client(configOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .build();
            sGithubService = retrofit.create(GithubService.class);

        }
        return sGithubService;
    }


    private static OkHttpClient configOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(TAG, "message " + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (Build.VERSION.SDK_INT < 20) {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, null, null);
                Tls12SocketFactory socketFactory = new Tls12SocketFactory(sslContext.getSocketFactory());
                builder.sslSocketFactory(socketFactory, socketFactory.getDefaultTrustManager());
            } catch (Exception e) {
                Log.e(TAG, "configOkHttpClient: unable to use tls");

            }

        }

        return builder.addInterceptor(loggingInterceptor).build();
    }
}
