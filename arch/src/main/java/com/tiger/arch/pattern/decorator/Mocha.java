package com.tiger.arch.pattern.decorator;

public class Mocha extends CondimentDecorator {

    Beverage beverage;

    public Mocha(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription()+"--"+Mocha.class.getSimpleName();
    }

    @Override
   public float cost() {
        return beverage.cost()+0.12f;
    }
}
