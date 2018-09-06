package com.tiger.arch.pattern.factory;

public class ChicagoPizzaIngredientFactory implements PizzaIngredientFactory {
    @Override
    public String creteDough() {
        return " ChicagoPizzaDougn";
    }

    @Override
    public String createSauce() {
        return " ChicagoPizzaSauce";
    }
}
