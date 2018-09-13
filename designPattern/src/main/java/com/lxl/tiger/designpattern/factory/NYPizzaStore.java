package com.lxl.tiger.designpattern.factory;

public class NYPizzaStore extends PisaStore {
    @Override
    protected Pisa cretePias(String item) {
        PizzaIngredientFactory factory = new NYPizzaIngredientFactory();
        if (item.equals("cheese")) {
            return new NYStyleCheesePizza(factory);
        } else return null;
    }
}
