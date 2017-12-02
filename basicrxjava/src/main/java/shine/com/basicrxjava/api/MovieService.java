package shine.com.basicrxjava.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import shine.com.basicrxjava.entity.Movie;

/**
 * Created by Administrator on 2016/7/25.
 */
public interface MovieService {
    @GET("top250")
    Observable<Movie> getTopMovie(@Query("start") int start, @Query("count") int count);
}
