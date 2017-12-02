package com.example.lxl.myapplication.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lxl.myapplication.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PorterBuffFragment extends Fragment {
    //代表布局文件索引
    private int mode = 9;

    public PorterBuffFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        switch (mode) {
            case 1:
                view = inflater.inflate(R.layout.fragment_porter_buff_view, container, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.des_in_view, container, false);
                break;
            case 3:
                view = inflater.inflate(R.layout.des_out_view, container, false);
                break;
            case 4:
                view = inflater.inflate(R.layout.screen_view, container, false);
                break;
            case 5:
                view = inflater.inflate(R.layout.eraser_view, container, false);
                break;
            case 6:
                view = inflater.inflate(R.layout.fontview, container, false);
                break;
            case 7:
                view = inflater.inflate(R.layout.mask_filter_view, container, false);
                break;
            case 8:
                view = inflater.inflate(R.layout.emboss_mask_filter_view, container, false);
                break;
            case 9:
                view = inflater.inflate(R.layout.path_effect_view, container, false);
                break;

        }
        return view;
    }


}
