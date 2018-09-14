package com.lxl.tiger.designpattern.compound;

public class RubberDuck implements Quackable {
    Observable observable;

    public RubberDuck() {
        observable=new Observable(this);

    }

    public void quack() {
        System.out.println("Squeak");
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