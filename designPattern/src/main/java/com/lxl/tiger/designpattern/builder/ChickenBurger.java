package com.lxl.tiger.designpattern.builder;

public class ChickenBurger extends Burger {
    @Override
    public String getName() {
        return ChickenBurger.class.getSimpleName();
    }

    @Override
    public float getPrice() {
        return 3.5f;
    }
}
