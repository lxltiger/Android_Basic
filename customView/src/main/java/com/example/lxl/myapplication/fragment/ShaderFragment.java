package com.example.lxl.myapplication.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lxl.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShaderFragment extends Fragment {
private int mode=7;

    public ShaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=null;
        switch (mode){
            case 1:
               view=inflater.inflate(R.layout.view_bitmap_shader, container, false);
                break;
            case 2:
               view=inflater.inflate(R.layout.view_brick, container, false);
                break;
            case 3:
               view=inflater.inflate(R.layout.view_linear_gradient, container, false);
                break;
            case 4:
               view=inflater.inflate(R.layout.view_matrix, container, false);
                break;
            case 5:
               view=inflater.inflate(R.layout.view_mutil_circle, container, false);
                break;
            case 6:
               view=inflater.inflate(R.layout.view_bitmap_mesh, container, false);
                break;
            case 7:
               view=inflater.inflate(R.layout.view_bitmap_mesh2, container, false);
                break;
        }
        return view;
    }


}
