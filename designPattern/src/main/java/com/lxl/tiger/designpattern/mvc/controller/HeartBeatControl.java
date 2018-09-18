package com.lxl.tiger.designpattern.mvc.controller;

import com.lxl.tiger.designpattern.mvc.model.HeartModelAdapter;
import com.lxl.tiger.designpattern.mvc.model.IBeatModel;
import com.lxl.tiger.designpattern.mvc.model.IHeartModel;
import com.lxl.tiger.designpattern.mvc.view.DJView;

public class HeartBeatControl implements ControllerInterface {

    private final DJView djView;
    IHeartModel beatModel;

    public HeartBeatControl(IHeartModel beatModel) {
        this.beatModel = beatModel;
        djView = new DJView(this, new HeartModelAdapter(beatModel));
        djView.createView();
        djView.createControls();
        djView.disableStopMenuItem();
        djView.disableStartMenuItem();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void increaseBPM() {

    }

    @Override
    public void decreaseBPM() {

    }

    @Override
    public void setBPM(int bpm) {

    }
}
