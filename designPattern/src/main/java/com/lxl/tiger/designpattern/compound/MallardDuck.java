package com.lxl.tiger.designpattern.compound;

public class MallardDuck implements Quackable {
    Observable observable;

    public MallardDuck() {
        observable=new Observable(this);
    }

    @Override
    public void quack() {
        System.out.println(MallardDuck.class.getSimpleName()+" quack ");
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
