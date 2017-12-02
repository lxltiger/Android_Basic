package shine.com.basicrxjava;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import shine.com.basicrxjava.api.BeautyService;
import shine.com.basicrxjava.api.MovieService;

/**
 * Created by Administrator on 2016/7/25.
 */
public class NetWork {
    private static MovieService sMovieService;
    private static BeautyService sBeautyService;
    public static MovieService getMovieService() {
        if (sMovieService == null) {
            Retrofit retrofit=new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl("https://api.douban.com/v2/movie/").build();
            sMovieService = retrofit.create(MovieService.class);
        }
        return sMovieService;
    }

    public static BeautyService getBeautyService() {
        if (sBeautyService == null) {
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl("http://gank.io/api/")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            sBeautyService = retrofit.create(BeautyService.class);
        }
        return sBeautyService;
    }
}
