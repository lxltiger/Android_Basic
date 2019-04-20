package com.lxl.advance;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lxl.advance.adapter.UserAdapter;
import com.lxl.advance.databinding.ActivityDynamicBinding;

/**
 * ViewBinding在RecycleView adapter中的应用
 */
public class DynamicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDynamicBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_dynamic);
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new UserAdapter());
    }
}
