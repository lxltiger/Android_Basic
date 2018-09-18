package com.lxl.tiger.designpattern.mvc.model;

public interface IHeartModel {
   int getHeartBeat();

  void register(BeatObserver observer);
  void remove(BeatObserver observer);
  void notifyBeatObserver();

  void register(BPMBeatObserver observer);
  void remove(BPMBeatObserver observer);
  void notifyBPMBeatObserver();
}
