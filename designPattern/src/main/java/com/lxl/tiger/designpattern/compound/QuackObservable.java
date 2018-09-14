package com.lxl.tiger.designpattern.compound;

public interface QuackObservable {

    void register(Observer observer);

    void notifyObserver();
}
