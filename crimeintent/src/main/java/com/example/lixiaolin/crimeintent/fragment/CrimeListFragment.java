package com.example.lixiaolin.crimeintent.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.lixiaolin.crimeintent.R;
import com.example.lixiaolin.crimeintent.activity.CrimePagerActivity;
import com.example.lixiaolin.crimeintent.adapter.CrimeAdapter;
import com.example.lixiaolin.crimeintent.entity.Crime;
import com.example.lixiaolin.crimeintent.entity.CrimeLab;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 由
 */
public class CrimeListFragment extends Fragment {
    private static final String SUBTITLE_STATUS= "com.lxl.status";
    private RecyclerView mRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private boolean isSubtitleShow=true;
    private ActionBar mToolbar;
    private int mSize;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        AppCompatActivity appCompatActivity= (AppCompatActivity) getActivity();
        mToolbar = appCompatActivity.getSupportActionBar();
        if (savedInstanceState != null) {
            isSubtitleShow = savedInstanceState.getBoolean(SUBTITLE_STATUS, false);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crime_list, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_crime_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
       Log.d("crime","on view created");
        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("crime", "on resume");
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("crime", "on save instance");
        outState.putBoolean(SUBTITLE_STATUS, isSubtitleShow);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("crime", "onCreateOptionsMenu");
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem menuItem = menu.findItem(R.id.item_toggle);
        if (isSubtitleShow) {
            menuItem.setTitle("hide");
        }else {
            menuItem.setTitle("show");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                Crime crime=new Crime();
                CrimeLab.getInstance(getActivity()).addCrime(crime);
                Intent intent=CrimePagerActivity.newIntent(getActivity(),mSize);
                getActivity().startActivity(intent);
                return true;
            case R.id.item_toggle:
                isSubtitleShow=!isSubtitleShow;
                //reload option menu
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void updateSubtitle() {

        if (isSubtitleShow) {
            mToolbar.setSubtitle(getString(R.string.crime_number,mSize));
        }else{
            mToolbar.setSubtitle(null);
        }
    }
    private void updateUI() {
        Log.d("crime","update UI");
        List<Crime> crimeList = CrimeLab.getInstance(getActivity()).getCrime();
        mSize=crimeList.size();
        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimeList,getActivity());
            mRecyclerView.setAdapter(mCrimeAdapter);
        }else{
//            mCrimeAdapter.notifyItemChanged(mCrimeAdapter.getPosition());
            mCrimeAdapter.setCrimeList(crimeList);
            mCrimeAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }
}
