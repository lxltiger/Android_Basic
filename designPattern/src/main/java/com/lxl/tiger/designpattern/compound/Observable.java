package com.lxl.tiger.designpattern.compound;

import java.util.ArrayList;

public class Observable implements QuackObservable {

    QuackObservable duck;
    ArrayList<Observer> duckObservers=new ArrayList<>();
    public Observable(QuackObservable duck) {
        this.duck = duck;
    }

    @Override
    public void register(Observer observer) {
        duckObservers.add(observer);
    }

    @Override
    public void notifyObserver() {
        for (Observer duckObserver : duckObservers) {
            duckObserver.update(duck);
        }
    }
}
