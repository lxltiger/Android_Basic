package com.lxl.tiger.designpattern.factory;

public class ChicagoStyleCheesePizza extends Pisa {
    PizzaIngredientFactory ingredientFactory;

    public ChicagoStyleCheesePizza(PizzaIngredientFactory ingredientFactory) {
        this.ingredientFactory = ingredientFactory;
    }


    /*public ChicagoStyleCheesePizza() {
        name = "Chicago Style Deep Dish Cheese Pizza";
        dough = "Extra Thick Crust Dough";
        sauce = "Plum Tomato Sauce";
        toppings.add("Shredded Mozzarella Cheese");
    }*/

    @Override
    void prepare() {
        dough = ingredientFactory.creteDough();
        sauce = ingredientFactory.createSauce();
    }

    void cut() {
        System.out.println("Cutting the pizza into square slices");
    }
}
