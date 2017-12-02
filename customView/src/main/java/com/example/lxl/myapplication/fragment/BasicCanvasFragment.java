package com.example.lxl.myapplication.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lxl.myapplication.R;

/**
 *
 */
public class BasicCanvasFragment extends Fragment {
    private int mode = 6;

    public BasicCanvasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        switch (mode) {
            case 1:
                view = inflater.inflate(R.layout.fragment_basic_canvas, container, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.view_wave, container, false);
                break;
            case 3:
                view = inflater.inflate(R.layout.view_path, container, false);
                break;
            case 4:
                view = inflater.inflate(R.layout.view_poly_line, container, false);
                break;
            case 5:
                view = inflater.inflate(R.layout.view_clip, container, false);
                break;
            case 6:
                view = inflater.inflate(R.layout.view_layer, container, false);
                break;


        }
        return view;
    }


}
