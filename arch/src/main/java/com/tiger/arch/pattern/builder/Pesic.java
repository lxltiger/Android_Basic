package com.tiger.arch.pattern.builder;

public class Pesic extends ColdDrink {
    @Override
    public String getName() {
        return Pesic.class.getSimpleName();
    }

    @Override
    public float getPrice() {
        return 2.0f;
    }
}
