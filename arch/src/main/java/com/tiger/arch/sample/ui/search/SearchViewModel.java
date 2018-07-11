package com.tiger.arch.sample.ui.search;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.util.Log;

import com.tiger.arch.App;
import com.tiger.arch.sample.repository.RepoRepository;
import com.tiger.arch.sample.utils.AbsentLiveData;
import com.tiger.arch.sample.utils.Objects;
import com.tiger.arch.sample.vo.Repo;
import com.tiger.arch.sample.vo.Resource;

import java.util.List;
import java.util.Locale;

public class SearchViewModel extends AndroidViewModel {
    private static final String TAG = "SearchViewModel";
    private MutableLiveData<String> query = new MutableLiveData<>();
    private LiveData<Resource<List<Repo>>> results;
    private final RepoRepository mRepository;

    public SearchViewModel(Application application) {
        super(application);
        App app = (App) application;
        mRepository = app.getRepository();
        results = Transformations.switchMap(query, new Function<String, LiveData<Resource<List<Repo>>>>() {
            @Override
            public LiveData<Resource<List<Repo>>> apply(String input) {
                Log.d(TAG, "receive " + input);
                if (input == null || input.trim().length() == 0) {
                    return AbsentLiveData.create();
                } else {
                    return mRepository.query(input);
                }
            }
        });
    }

    public void startQuery(String input) {
        String keyword = input.toLowerCase(Locale.getDefault()).trim();
        if (Objects.equals(keyword,query.getValue())) {
            return;
        }

        // TODO: 2018/6/26 0026 reset load more
        query.setValue(keyword);
    }


    public LiveData<Resource<List<Repo>>> getResults() {
        return results;
    }
}
