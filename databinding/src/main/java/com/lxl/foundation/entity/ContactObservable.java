package com.lxl.advance.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.lxl.advance.BR;


/**
 * author:
 * 时间:2017/5/22
 * qq:1220289215
 * 类描述：可通知View更新的Model
 */

public class ContactObservable extends BaseObservable {
    private String name;
    private String mob;
    private String address;

    public ContactObservable() {
    }

    public ContactObservable(String name, String mob, String address) {
        this.name = name;
        this.mob = mob;
        this.address = address;
    }

    @Bindable
    public String getMob() {
        return mob;
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setMob(String mob) {
        this.mob = mob;
        notifyPropertyChanged(BR.mob);
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }
}
