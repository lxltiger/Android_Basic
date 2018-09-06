package com.tiger.arch.pattern.factory;

public class NYPizzaIngredientFactory implements PizzaIngredientFactory {
    @Override
    public String creteDough() {
        return "NYPizzaDough";

    }

    @Override
    public String createSauce() {
        return "NYPizzaSauce";
    }
}
