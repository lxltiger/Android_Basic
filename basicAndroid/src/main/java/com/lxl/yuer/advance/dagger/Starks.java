package com.lxl.yuer.advance.dagger;

import android.util.Log;

import javax.inject.Inject;

public class Starks implements House {
    private static final String TAG = "Starks";
    @Inject
    public Starks(){ }
    @Override
    public void prepareForWar() {
        //do something
        Log.d(TAG, "prepareForWar: "+this.getClass().getSimpleName());
    }

    @Override
    public void reportForWar() {
        //do something
        Log.d(TAG, "reporting: "+this.getClass().getSimpleName());

    }
}