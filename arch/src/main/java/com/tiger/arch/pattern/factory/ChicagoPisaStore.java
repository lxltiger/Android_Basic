package com.tiger.arch.pattern.factory;

public class ChicagoPisaStore extends PisaStore {
    @Override
    protected Pisa cretePias(String item) {
        PizzaIngredientFactory factory = new ChicagoPizzaIngredientFactory();

        if (item.equals("cheese")) {
            return new ChicagoStyleCheesePizza(factory);
        } else return null;
    }
}
