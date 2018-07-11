package com.tiger.arch.codelab.seekbar;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.tiger.arch.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeekBarFragment extends Fragment {
    private static final String TAG = "SeekBarFragment";

    private SeekBar mSeekBar;
    private SeekBarViewModel mSeekBarViewModel;

    public SeekBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seek_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSeekBar = view.findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Log.d(TAG, "onProgressChanged: "+progress);
                    mSeekBarViewModel.setData(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        一定要用getActivity表明两个fragment共用SeekBarViewModel，否则一个更新另一个观察不到
        mSeekBarViewModel = ViewModelProviders.of(getActivity()).get(SeekBarViewModel.class);
        mSeekBarViewModel.getData().observe(getActivity(), value->{
            Log.d(TAG, "onActivityCreated: ");
            mSeekBar.setProgress(value);
        });
    }
}
