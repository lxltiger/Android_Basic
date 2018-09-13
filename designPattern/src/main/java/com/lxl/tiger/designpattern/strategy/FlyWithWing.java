package com.lxl.tiger.designpattern.strategy;

public class FlyWithWing implements FlyBehavior {
    @Override
    public void fly() {
        System.out.println(FlyWithWing.class.getSimpleName());
    }
}
