package com.lxl.tiger.designpattern.oberver;

public interface Subject {

    void register(Observer observer);

    void unregister(Observer observer);

    void notifyAllObservers();
}
