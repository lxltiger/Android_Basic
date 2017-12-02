package shine.com.basicrxjava.api;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import shine.com.basicrxjava.entity.Beauty;

/**
 * Created by Administrator on 2016/7/25.
 */
public interface BeautyService {
    @GET("data/福利/{number}/{page}")
    Observable<Beauty> getBeauty(@Path("number") int number,@Path("page") int page);
}
