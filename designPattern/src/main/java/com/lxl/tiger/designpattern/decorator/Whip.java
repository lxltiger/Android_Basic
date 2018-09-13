package com.lxl.tiger.designpattern.decorator;

public class Whip extends CondimentDecorator {

    Beverage beverage;

    public Whip(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription()+Whip.class.getSimpleName();
    }

    @Override
    public float cost() {
        return beverage.cost()+0.14f;
    }
}
