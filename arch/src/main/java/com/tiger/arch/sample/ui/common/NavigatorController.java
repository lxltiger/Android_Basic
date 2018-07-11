package com.tiger.arch.sample.ui.common;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.tiger.arch.R;
import com.tiger.arch.sample.ui.search.SearchFragment;

public class NavigatorController {


    private final FragmentManager mFragmentManager;
    private final int containerId;

    public NavigatorController(AppCompatActivity activity) {
        containerId = R.id.container;
        mFragmentManager = activity.getSupportFragmentManager();
    }

    public void navigateToSearch() {
        SearchFragment searchFragment = new SearchFragment();
        mFragmentManager.beginTransaction()
                .replace(containerId, searchFragment)
                .commitAllowingStateLoss();
    }
}
