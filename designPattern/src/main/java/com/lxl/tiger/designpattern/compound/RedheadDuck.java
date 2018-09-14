package com.lxl.tiger.designpattern.compound;

public class RedheadDuck implements Quackable {

    Observable observable;

    public RedheadDuck() {
        observable=new Observable(this);

    }

    public void quack() {
        System.out.println("RedheadDuck Quack");
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