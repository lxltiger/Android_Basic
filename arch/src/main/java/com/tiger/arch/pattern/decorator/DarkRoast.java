package com.tiger.arch.pattern.decorator;

public class DarkRoast extends Beverage {
    @Override
    public float cost() {
        return 1.58f;
    }

    @Override
    public String getDescription() {
        return DarkRoast.class.getSimpleName();
    }
}
