package com.tiger.arch.sample.api;

import android.arch.lifecycle.LiveData;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GithubService {

    @GET("search/repositories")
    LiveData<Response<RepoSearchResponse>> searchRepos(@Query("q") String query);

    @POST("user/login")
    Call<ResponseBody> login(@Body RequestBody body);

    @POST("user/loginout")
    Call<ResponseBody> logout(@Header("accessToken") String accessToken );
}
