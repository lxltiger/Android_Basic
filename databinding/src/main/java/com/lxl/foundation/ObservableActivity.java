package com.lxl.foundation;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayMap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lxl.foundation.databinding.ActivityObservableBinding;
import com.lxl.foundation.entity.ContactObservable;
import com.lxl.foundation.entity.ContactPlain;

/**
 * 演示View和Model的邦定
 */
public class ObservableActivity extends AppCompatActivity {

    private ContactObservable mContact=new ContactObservable();
    private ContactPlain mContactPlain=new ContactPlain();
    private ObservableArrayMap<String,Object> mObservableMap=new ObservableArrayMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_observable);
        ActivityObservableBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_observable);
        binding.setContact(mContact);
        binding.setContactWithAge(mContactPlain);
        binding.setContactMap(mObservableMap);
    }

    /**
     * Model的改变使View自动更新
     * @param view
     */
    public void change(View view) {
//        类是Observable
        mContact.setMob("010-3245987");
        mContact.setAddress("north");
        mContact.setName("shine");
//        字段是Obsevable
        mContactPlain.mName.set("lxl");
        mContactPlain.mAddress.set("south");
        mContactPlain.mAge.set(23);
//        Log.d("ObservableActivity", "mContactPlain.mAge.get():" + mContactPlain.mAge.get());
//        使用可观测的集合
        mObservableMap.put("name", "duang");
        mObservableMap.put("address", "east");
        mObservableMap.put("age",18);
    }
}
