package com.tiger.arch.sample;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tiger.arch.R;
import com.tiger.arch.sample.api.NetWork;
import com.tiger.arch.sample.api.RepoSearchResponse;
import com.tiger.arch.sample.ui.common.NavigatorController;
import com.tiger.arch.sample.ui.search.SearchViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GithubActivity extends AppCompatActivity {

    private NavigatorController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        mController = new NavigatorController(this);
        if (savedInstanceState == null) {
            mController.navigateToSearch();
        }
    }
}
