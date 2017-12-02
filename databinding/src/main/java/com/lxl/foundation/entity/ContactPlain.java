package com.lxl.foundation.entity;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

/**
 * author:
 * 时间:2017/5/23
 * qq:1220289215
 * 类描述：字段可观察的类
 */

public class ContactPlain {
    public final ObservableField<String> mName=new ObservableField<>();
    public final ObservableField<String> mAddress=new ObservableField<>();
    public final ObservableInt mAge=new ObservableInt();

}
