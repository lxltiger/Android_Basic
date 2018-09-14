package com.lxl.tiger.designpattern.compound;

public class CountDuckFactory extends AbstractDuckFactory {
    public Quackable createMallardDuck() {
        return new QuackCount(new MallardDuck());
    }

    public Quackable createRedheadDuck() {
        return new QuackCount(new RedheadDuck());
    }

    public Quackable createDuckCall() {
        return new QuackCount(new DuckCall());
    }

    public Quackable createRubberDuck() {
        return new QuackCount(new RubberDuck());
    }
}
