package com.lxl.tiger.designpattern.mvc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HeartModel implements IHeartModel, Runnable {
    List<BeatObserver> beatObservers = new ArrayList<>();
    List<BPMBeatObserver> bpmBeatObservers = new ArrayList<>();
    private int heartRate;
    private Random random = new Random(System.currentTimeMillis());

    public HeartModel() {
        new Thread(this).start();
    }

    @Override
    public int getHeartBeat() {
        return heartRate;
    }

    @Override
    public void register(BeatObserver observer) {
        beatObservers.add(observer);

    }

    @Override
    public void remove(BeatObserver observer) {
        beatObservers.remove(observer);

    }

    @Override
    public void notifyBeatObserver() {
        for (BeatObserver beatObserver : beatObservers) {
            beatObserver.updateBeat();
        }
    }

    @Override
    public void register(BPMBeatObserver observer) {
        bpmBeatObservers.add(observer);

    }

    @Override
    public void remove(BPMBeatObserver observer) {
        bpmBeatObservers.remove(observer);

    }

    @Override
    public void notifyBPMBeatObserver() {
        for (BPMBeatObserver bpmBeatObserver : bpmBeatObservers) {
            bpmBeatObserver.updateBPM();
        }
    }

    @Override
    public void run() {
        for (; ; ) {
           int rate = 6000/(random.nextInt(10)+60);
            notifyBeatObserver();
            if (heartRate != rate) {
                heartRate = rate;
                notifyBPMBeatObserver();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
