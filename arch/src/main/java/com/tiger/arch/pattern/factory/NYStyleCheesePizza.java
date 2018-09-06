package com.tiger.arch.pattern.factory;

public class NYStyleCheesePizza extends Pisa {
    PizzaIngredientFactory ingredientFactory;

    public NYStyleCheesePizza(PizzaIngredientFactory ingredientFactory) {
        this.ingredientFactory = ingredientFactory;
    }



    @Override
    void prepare() {
        dough = ingredientFactory.creteDough();
        sauce = ingredientFactory.createSauce();
    }
}
