package com.lxl.tiger.designpattern.mvc.model;

public class HeartModelAdapter implements IBeatModel {
    private IHeartModel heartModel;

    public HeartModelAdapter(IHeartModel heartModel) {
        this.heartModel = heartModel;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void on() {

    }

    @Override
    public void off() {

    }

    @Override
    public void setBPM(int bpm) {

    }

    @Override
    public int getBPM() {
        return heartModel.getHeartBeat();
    }

    @Override
    public void register(BeatObserver observer) {
        heartModel.register(observer);
    }

    @Override
    public void remove(BeatObserver observer) {
        heartModel.remove(observer);
    }

    @Override
    public void notifyBeatObserver() {
        heartModel.notifyBeatObserver();
    }

    @Override
    public void register(BPMBeatObserver observer) {
        heartModel.register(observer);
    }

    @Override
    public void remove(BPMBeatObserver observer) {
        heartModel.remove(observer);

    }

    @Override
    public void notifyBPMBeatObserver() {
        heartModel.notifyBPMBeatObserver();
    }
}
