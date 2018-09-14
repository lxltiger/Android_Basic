package com.lxl.tiger.designpattern.compound;

public class QuackCount implements Quackable {

    static int count;
    Quackable duck;
    Observable observable;


    public QuackCount(Quackable duck) {
        this.duck = duck;
        observable=new Observable(this);

    }
    @Override
    public void quack() {
        count++;
        duck.quack();
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
    static int getCount() {
        return count;
    }
}
