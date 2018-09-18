package com.lxl.tiger.designpattern.mvc.controller;

import com.lxl.tiger.designpattern.mvc.model.IBeatModel;
import com.lxl.tiger.designpattern.mvc.view.DJView;

public class BeatControl implements ControllerInterface {
    IBeatModel beatModel;
    DJView djView;

    public BeatControl(IBeatModel beatModel) {
        this.beatModel = beatModel;
        djView = new DJView(this, beatModel);
        djView.createView();
        djView.createControls();
        djView.disableStopMenuItem();
        djView.enableStartMenuItem();
        beatModel.initialize();
    }

    @Override
    public void start() {
        beatModel.on();
        djView.disableStartMenuItem();
        djView.enableStopMenuItem();
    }

    @Override
    public void stop() {
        beatModel.off();
        djView.disableStopMenuItem();
        djView.enableStartMenuItem();
    }

    @Override
    public void increaseBPM() {
        int bpm = beatModel.getBPM();
        beatModel.setBPM(bpm + 1);
    }

    @Override
    public void decreaseBPM() {
        int bpm = beatModel.getBPM();
        beatModel.setBPM(bpm - 1);
    }

    @Override
    public void setBPM(int bpm) {
        beatModel.setBPM(bpm);
    }
}
