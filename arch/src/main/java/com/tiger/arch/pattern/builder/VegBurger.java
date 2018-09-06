package com.tiger.arch.pattern.builder;

public class VegBurger extends Burger
{
    @Override
    public String getName() {
        return VegBurger.class.getSimpleName();
    }

    @Override
    public float getPrice() {
        return 2.5f;
    }
}
