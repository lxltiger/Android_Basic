package com.tiger.arch.pattern.strategy;

public class MallardDuck extends Duck {


    public MallardDuck() {
        flyBehavior = new FlyWithWing();
        quackBehavior=new Quack();
    }

    @Override
    void display() {
        System.out.println("maller fly swim");
    }
}
