package com.lxl.tiger.designpattern.compound;

public class GooseAdapter implements Quackable {

    Goose goose;

    public GooseAdapter(Goose goose) {
        this.goose = goose;
        observable=new Observable(this);
    }

    @Override
    public void quack() {
        goose.honk();
        notifyObserver();

    }

    Observable observable;


    @Override
    public void register(Observer observer) {
        observable.register(observer);
    }

    @Override
    public void notifyObserver() {
        observable.notifyObserver();
    }
}
