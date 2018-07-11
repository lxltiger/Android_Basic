package com.tiger.arch;

import android.app.Application;

import com.tiger.arch.sample.AppExecutors;
import com.tiger.arch.sample.repository.RepoRepository;

public class App extends Application {
    private static final String TAG = "App";
    private AppExecutors mAppExecutors;
    private RepoRepository mRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors=new AppExecutors();
        mRepository = new RepoRepository(this, mAppExecutors);
    }

    public AppExecutors appExecutors() {
        return mAppExecutors;
    }

    public RepoRepository getRepository() {
        return mRepository;
    }
}
