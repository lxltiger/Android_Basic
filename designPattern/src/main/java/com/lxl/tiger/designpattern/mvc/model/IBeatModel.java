package com.lxl.tiger.designpattern.mvc.model;

/**
 * Model持有View需要的状态和执行Controller请求的操作逻辑
 * 通过观察者模式通知View和Controller状态的更新
 */
public interface IBeatModel {

    void initialize();

    void on();

    void off();

    void setBPM(int bpm);

    int getBPM();

//    notify when beat change
    void register(BeatObserver observer);
    void remove(BeatObserver observer);

    void notifyBeatObserver();

//     notify beat change  every minute
    void register(BPMBeatObserver observer);
    void remove(BPMBeatObserver observer);
    void notifyBPMBeatObserver();


}
