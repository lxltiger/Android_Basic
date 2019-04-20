package com.lxl.advance;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.lxl.advance.databinding.ActivityCollectionBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在View中使用集合的几种方式
 */
public class CollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollectionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_collection);
        List<String> list = new ArrayList<>();
        list.add("---------------one");
        list.add("two");

        SparseArray<String> sparse = new SparseArray<>();
        sparse.put(0,"---three");
        sparse.put(4,"four--------");

        Map<String, String> map = new HashMap<>();
        map.put("key","-----five----");
        map.put("water","-----six----");

//        设置键值
        binding.setKeyForSparse(4);
        binding.setKeyForMap("key");
//        设置数据
        binding.setList(list);
        binding.setMap(map);
        binding.setSparse(sparse);

    }
}
