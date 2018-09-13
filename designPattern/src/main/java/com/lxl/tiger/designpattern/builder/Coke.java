package com.lxl.tiger.designpattern.builder;

public class Coke extends ColdDrink {
    @Override
    public String getName() {
        return Coke.class.getSimpleName();
    }

    @Override
    public float getPrice() {
        return 1.0f;
    }
}
