package shine.com.basicrxjava.api;

import android.os.Build;
import android.util.Log;

import javax.net.ssl.SSLContext;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import shine.com.basicrxjava.api.BeautyService;
import shine.com.basicrxjava.api.KimAscendService;
import shine.com.basicrxjava.api.MovieService;

/**
 * Created by Administrator on 2016/7/25.
 */
public class NetWork {
    private static final String TAG = "NetWork";
    private static MovieService sMovieService;
    private static BeautyService sBeautyService;
    private static KimAscendService sGithubService;

    public static MovieService getMovieService() {
        if (sMovieService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl("https://api.douban.com/v2/movie/").build();
            sMovieService = retrofit.create(MovieService.class);
        }
        return sMovieService;
    }

    public static BeautyService getBeautyService() {
        if (sBeautyService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://gank.io/api/")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            sBeautyService = retrofit.create(BeautyService.class);
        }
        return sBeautyService;
    }

    public static KimAscendService kimService() {
        if (sGithubService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.33/lamp/")
                    .client(configOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            sGithubService = retrofit.create(KimAscendService.class);

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
