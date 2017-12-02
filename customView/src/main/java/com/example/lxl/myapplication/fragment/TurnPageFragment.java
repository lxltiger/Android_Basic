package com.example.lxl.myapplication.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lxl.myapplication.R;
import com.example.lxl.myapplication.customview.TurnPageView;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class TurnPageFragment extends Fragment {

    public TurnPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_turn_page, container, false);
        TurnPageView turnPageView = (TurnPageView) view.findViewById(R.id.turn_page);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.a3);
        Bitmap bitmap1= BitmapFactory.decodeResource(getResources(),R.drawable.a4);
        Bitmap bitmap2= BitmapFactory.decodeResource(getResources(),R.drawable.girl);
        Bitmap bitmap3= BitmapFactory.decodeResource(getResources(),R.drawable.a);
        Bitmap bitmap4= BitmapFactory.decodeResource(getResources(),R.drawable.boy);
        List<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(bitmap);
        bitmaps.add(bitmap1);
        bitmaps.add(bitmap2);
        bitmaps.add(bitmap3);
        bitmaps.add(bitmap4);
        turnPageView.setBitmaps(bitmaps);


        return view;
    }


}
