package com.tiger.arch.sample.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tiger.arch.sample.AppExecutors;
import com.tiger.arch.sample.api.GithubService;
import com.tiger.arch.sample.api.NetWork;
import com.tiger.arch.sample.api.RepoSearchResponse;
import com.tiger.arch.sample.db.GithubDatabase;
import com.tiger.arch.sample.db.RepoDao;
import com.tiger.arch.sample.vo.Repo;
import com.tiger.arch.sample.vo.Resource;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class RepoRepository {

    private static final String TAG = "RepoRepository";
    private final GithubDatabase mGithubDatabase;
    private final GithubService mGithubService;
    private final AppExecutors mAppExecutors;
    private final RepoDao mRepoDao;
    private final MediatorLiveData<Resource<List<Repo>>> result=new MediatorLiveData<>();

    private RepoRepository(GithubDatabase githubDatabase, GithubService githubService, AppExecutors appExecutors) {
        mGithubDatabase = githubDatabase;
        mRepoDao = githubDatabase.repo();
        mGithubService = githubService;
        mAppExecutors = appExecutors;
    }

    public RepoRepository(@NonNull Context context,AppExecutors appExecutors) {
      this(GithubDatabase.instance(context), NetWork.githubService(), appExecutors);
    }


    public LiveData<Resource<List<Repo>>> query(String query) {

        result.setValue(Resource.onLoading(null));
        LiveData<List<Repo>> localRepos = mRepoDao.loadRepos();
        result.addSource(localRepos, new Observer<List<Repo>>() {
            @Override
            public void onChanged(@Nullable List<Repo> repos) {
//                不再监听，否则插入的时候会更新
                Log.d(TAG, "here");
                result.removeSource(localRepos);
                if (repos == null|| repos.isEmpty()) {
                    fetchFromInternet(localRepos,query);
                }else{

                    result.addSource(localRepos, new Observer<List<Repo>>() {
                        @Override
                        public void onChanged(@Nullable List<Repo> repos) {
                            result.setValue(Resource.onSuccess(repos));
                        }
                    });
                }

            }
        });


        return result;
    }

    private void fetchFromInternet(LiveData<List<Repo>> localRepos, String query) {
        Log.d(TAG, "fetchFromInternet: "+query);
        LiveData<Response<RepoSearchResponse>> repoSearchResponseCall = mGithubService.searchRepos(query);
        result.addSource(localRepos, repos -> result.setValue(Resource.onLoading(repos)));
        result.addSource(repoSearchResponseCall, new Observer<Response<RepoSearchResponse>>() {
            @Override
            public void onChanged(@Nullable Response<RepoSearchResponse> repoSearchResponseResponse) {
                result.removeSource(localRepos);
                result.removeSource(repoSearchResponseCall);
                if (repoSearchResponseResponse.isSuccessful()) {
                    List<Repo> items = repoSearchResponseResponse.body().getItems();
                    mRepoDao.insertRepo(items);
                    result.addSource(mRepoDao.loadRepos(), new Observer<List<Repo>>() {
                        @Override
                        public void onChanged(@Nullable List<Repo> repos) {
                            result.setValue(Resource.onSuccess(repos));

                        }
                    });
                }else{
                    result.addSource(localRepos, new Observer<List<Repo>>() {
                        @Override
                        public void onChanged(@Nullable List<Repo> repos) {
                            result.setValue(Resource.onError(repoSearchResponseResponse.message(), repos));
                        }
                    });
                }

            }
        });
    }
}
