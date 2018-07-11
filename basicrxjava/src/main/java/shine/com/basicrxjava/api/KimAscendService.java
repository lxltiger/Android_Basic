package shine.com.basicrxjava.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import shine.com.basicrxjava.entity.Login;

public interface KimAscendService {

    @POST("user/login")
    Call<Login> login(@Body RequestBody body);

    @POST("user/loginout")
    Call<ResponseBody> logout(@Header("accessToken") String accessToken );


}
