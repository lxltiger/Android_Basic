package com.lxl.tiger.designpattern.compound;

public class DuckCall implements Quackable {

    Observable observable;

    public DuckCall() {
        this.observable = new Observable(this);
    }

    public void quack() {
        System.out.println("Kwak");
        notifyObserver();
    }


    @Override
    public void register(Observer observer) {
        observable.register(observer);
    }

    @Override
    public void notifyObserver() {
        observable.notifyObserver();
    }
}