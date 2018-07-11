package com.tiger.arch.sample.utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.tiger.arch.R;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<Response<R>>> {
    private Type responseType;

    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<Response<R>> adapt(Call<R> call) {
        return new LiveData<Response<R>>() {
            AtomicBoolean start=new AtomicBoolean(false);
            @Override
            protected void onActive() {
                super.onActive();
                if (start.compareAndSet(false,true))

                    call.enqueue(new Callback<R>() {
                    @Override
                    public void onResponse(Call<R> call1, Response<R> response) {
                        postValue(response);
                    }

                    @Override
                    public void onFailure(Call<R> call1, Throwable t) {
                        postValue(null);
                    }
                });
            }
        };
    }
}
