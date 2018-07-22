package com.example.lixiaolin.crimeintent.activity;

import android.support.v4.app.Fragment;

import com.example.lixiaolin.crimeintent.fragment.CrimeListFragment;

/**
 * Created by lixiaolin on 15/10/18.
 */
public class CrimeListActivity extends BaseActivity{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
