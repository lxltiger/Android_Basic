package com.tiger.arch.codelab.seekbar;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class SeekBarViewModel extends ViewModel {


    private MutableLiveData<Integer> mData = new MutableLiveData<>();

    public LiveData<Integer> getData() {
        return mData;
    }

    public void setData(int data) {
        mData.setValue(data);
    }
}
