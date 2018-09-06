package com.tiger.arch.pattern.adapter;

public class TurkeyAdapter implements Duck {

    Turkey turkey;

    public TurkeyAdapter(Turkey turkey) {
        this.turkey = turkey;
    }

    @Override
    public void swim() {
        turkey.swim();
    }

    @Override
    public void fly() {
        for (int i = 0; i < 3; i++) {
            turkey.fly();
        }
    }
}
