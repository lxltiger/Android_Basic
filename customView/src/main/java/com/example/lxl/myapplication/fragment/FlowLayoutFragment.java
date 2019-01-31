package com.example.lxl.myapplication.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lxl.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlowLayoutFragment extends Fragment {
    private String[] name={"android","iso","http","html5","sfsfdfds","sfsfsf"};


    public FlowLayoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_flow_layout, container, false);
        FlowLayout flowLayout= (FlowLayout)view.findViewById(R.id.flow_layout);
        /*for (int i=0;i<name.length;i++){
            Button button=new Button(this);
            ViewGroup.MarginLayoutParams layoutParams=new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            button.setText(name[i]);
            flowLayout.addView(button,layoutParams);
        }*/
//        LayoutInflater inflater=LayoutInflater.from(getActivity());
        for (int i=0;i<name.length;i++){
            TextView textView= (TextView) inflater.inflate(R.layout.textview,flowLayout,false);
            textView.setText(name[i]);
            flowLayout.addView(textView);
        }

        return view;
    }


}
