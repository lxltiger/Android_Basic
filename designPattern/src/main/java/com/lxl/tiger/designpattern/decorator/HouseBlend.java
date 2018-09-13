package com.lxl.tiger.designpattern.decorator;

public class HouseBlend extends Beverage {
    @Override
    public float cost() {
        return 0.89f;
    }

    @Override
    public String getDescription() {
        return HouseBlend.class.getSimpleName();
    }
}
