package com.lxl.tiger.designpattern.strategy;

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
