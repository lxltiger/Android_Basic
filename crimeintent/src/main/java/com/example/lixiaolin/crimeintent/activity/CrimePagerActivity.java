package com.example.lixiaolin.crimeintent.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.lixiaolin.crimeintent.R;
import com.example.lixiaolin.crimeintent.entity.Crime;
import com.example.lixiaolin.crimeintent.entity.CrimeLab;
import com.example.lixiaolin.crimeintent.fragment.CrimeFragment;

import java.util.List;

public class CrimePagerActivity extends AppCompatActivity {
    private static final String POSITION = "com.lxl.position";
    private ViewPager mViewPager;
    private List<Crime> mCrimeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        mViewPager = (ViewPager) findViewById(R.id.vp_crime);
        mCrimeList= CrimeLab.getInstance(this).getCrime();
        int position = getIntent().getIntExtra(POSITION, 0);
        FragmentManager fragmentManager=getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return CrimeFragment.getInstance(position);
            }

            @Override
            public int getCount() {
                return mCrimeList.size();
            }
        });
        mViewPager.setCurrentItem(position);

    }

    public static Intent newIntent(Context context,int position) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(POSITION, position);
        return intent;
    }
}
