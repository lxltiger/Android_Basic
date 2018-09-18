package com.lxl.tiger.designpattern.mvc.model;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class BeatModelImpl implements IBeatModel,MetaEventListener {
    Sequencer sequencer;
    int bpm=90;
    List<BeatObserver> beatObservers = new ArrayList<>();
    List<BPMBeatObserver> bpmBeatObservers = new ArrayList<>();
    Sequence sequence;
    Track track;
    @Override
    public void initialize() {
        setUpMidi();
        buildTrackAndStart();
    }

    public void setUpMidi() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.addMetaEventListener(this);
            sequence = new Sequence(Sequence.PPQ,4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(getBPM());
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void buildTrackAndStart() {
        int[] trackList = {35, 0, 46, 0};

        sequence.deleteTrack(null);
        track = sequence.createTrack();

        makeTracks(trackList);
        track.add(makeEvent(192,9,1,0,4));
        try {
            sequencer.setSequence(sequence);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void makeTracks(int[] list) {

        for (int i = 0; i < list.length; i++) {
            int key = list[i];

            if (key != 0) {
                track.add(makeEvent(144,9,key, 100, i));
                track.add(makeEvent(128,9,key, 100, i+1));
            }
        }
    }

    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);

        } catch(Exception e) {
            e.printStackTrace();
        }
        return event;
    }
    @Override
    public void on() {
        sequencer.start();
        setBPM(90);
    }

    @Override
    public void off() {
        setBPM(0);
        sequencer.stop();

    }

    @Override
    public void setBPM(int bpm) {
        this.bpm = bpm;
        sequencer.setTempoInBPM(bpm);
        notifyBPMBeatObserver();

    }

    @Override
    public int getBPM() {
        return bpm;
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

    void beatEvent() {
        notifyBeatObserver();
    }
    @Override
    public void notifyBPMBeatObserver() {
        for (BPMBeatObserver bpmBeatObserver : bpmBeatObservers) {
            bpmBeatObserver.updateBPM();
        }
    }

    @Override
    public void meta(MetaMessage message) {
        if (message.getType() == 47) {
            beatEvent();
            sequencer.start();
            setBPM(getBPM());
        }
    }
}
