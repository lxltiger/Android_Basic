package com.tiger.arch.codelab.time;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.SystemClock;

import java.util.Timer;
import java.util.TimerTask;

public class TimeViewModel extends ViewModel {

    private MutableLiveData<Long> mElapsedTime = new MutableLiveData<>();
    private static final int SECOND = 1000;
    private long mInitial;

    public TimeViewModel() {
        Timer timer = new Timer();
        mInitial = SystemClock.uptimeMillis();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mElapsedTime.postValue((SystemClock.uptimeMillis() - mInitial) / 1000);
            }
        }, SECOND, SECOND);
    }


    public MutableLiveData<Long> getElapsedTime() {
        return mElapsedTime;
    }
}
