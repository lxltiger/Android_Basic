package com.tiger.arch.pattern.oberver;

public interface Subject {

    void register(Observer observer);

    void unregister(Observer observer);

    void notifyAllObservers();
}
